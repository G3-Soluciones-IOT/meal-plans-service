package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class ExternalProfileAndNutritionistService {

    private final RestClient profilesClient;
    private final RestClient nutritionistsClient;

    public ExternalProfileAndNutritionistService(
            @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClientBuilder,
            @Value("${services.profiles.base-url}") String profilesBaseUrl,
            @Value("${services.nutritionists.base-url}") String nutritionistsBaseUrl) {
        this.profilesClient = restClientBuilder.baseUrl(profilesBaseUrl).build();
        this.nutritionistsClient = restClientBuilder.baseUrl(nutritionistsBaseUrl).build();
    }

    public void validateUserProfile(Long userId) {
        if (!exists(profilesClient, "/api/v1/user-profiles/exists/by-user/{userId}", userId)
                && !exists(profilesClient, "/api/v1/user-profiles/{profileId}", userId)) {
            throw new IllegalArgumentException("No regular user profile found with ID: " + userId);
        }
    }

    public void validateNutritionist(Long userId) {
        if (!existsNutritionistByUserId(userId)) {
            throw new IllegalArgumentException("No nutritionist found with userId: " + userId);
        }
    }

    private boolean exists(RestClient client, String uri, Long id) {
        try {
            client.get().uri(uri, id).retrieve().toBodilessEntity();
            return true;
        } catch (RestClientException ex) {
            return false;
        }
    }

    private boolean existsNutritionistByUserId(Long userId) {
        try {
            nutritionistsClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/nutritionists/by-user")
                            .queryParam("userId", userId)
                            .build())
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (RestClientException ex) {
            return false;
        }
    }
}
