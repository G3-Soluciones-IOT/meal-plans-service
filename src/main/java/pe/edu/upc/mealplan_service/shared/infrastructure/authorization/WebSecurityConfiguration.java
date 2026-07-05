package pe.edu.upc.mealplan_service.shared.infrastructure.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.function.Supplier;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfiguration {

    @Bean
    public InternalServiceAuthenticationFilter internalServiceAuthenticationFilter(
            @Value("${authorization.internal-service.secret}") String internalSecret) {
        return new InternalServiceAuthenticationFilter(internalSecret);
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            InternalServiceAuthenticationFilter internalServiceAuthenticationFilter,
            @Value("${legacy.jwt.issuer:iam-service}") String legacyJwtIssuer
    ) throws Exception {
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/actuator/**"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/**").access((authentication, context) ->
                        hasPermissionOrLegacyJwt((Supplier<Authentication>) authentication, context, legacyJwtIssuer, "read:meal-plans"))
                .requestMatchers(HttpMethod.HEAD, "/**").access((authentication, context) ->
                        hasPermissionOrLegacyJwt((Supplier<Authentication>) authentication, context, legacyJwtIssuer, "read:meal-plans"))
                .anyRequest().access((authentication, context) ->
                        hasPermissionOrLegacyJwt((Supplier<Authentication>) authentication, context, legacyJwtIssuer, "write:meal-plans")));

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                jwt.jwtAuthenticationConverter(MealPlansJwtAuthenticationConverter.jwtAuthenticationConverter())));
        http.addFilterBefore(internalServiceAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private AuthorizationDecision hasPermissionOrLegacyJwt(
            Supplier<Authentication> authenticationSupplier,
            RequestAuthorizationContext context,
            String legacyJwtIssuer,
            String permission) {
        var authentication = authenticationSupplier.get();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(
                hasAuthority(authentication, permission)
                        || hasAuthority(authentication, "ROLE_SERVICE")
                        || isLegacyJwt(authentication, legacyJwtIssuer));
    }

    private boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> expectedAuthority.equals(authority.getAuthority()));
    }

    private boolean isLegacyJwt(Authentication authentication, String legacyJwtIssuer) {
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthentication)) {
            return false;
        }
        var issuer = jwtAuthentication.getToken().getIssuer();
        return issuer != null && legacyJwtIssuer.equals(issuer.toString());
    }
}
