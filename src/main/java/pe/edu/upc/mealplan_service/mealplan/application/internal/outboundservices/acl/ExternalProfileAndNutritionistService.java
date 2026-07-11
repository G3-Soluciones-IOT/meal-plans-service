package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class ExternalProfileAndNutritionistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalProfileAndNutritionistService.class);
    private static final String INTERNAL_HEADER = "X-Internal-Request";

    private final RestClient profilesClient;
    private final RestClient nutritionistsClient;
    private final String internalSecret;

    public ExternalProfileAndNutritionistService(
            @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClientBuilder,
            @Value("${services.profiles.base-url}") String profilesBaseUrl,
            @Value("${services.nutritionists.base-url}") String nutritionistsBaseUrl,
            @Value("${authorization.internal-service.secret}") String internalSecret) {
        this.profilesClient = restClientBuilder.baseUrl(profilesBaseUrl).build();
        this.nutritionistsClient = restClientBuilder.baseUrl(nutritionistsBaseUrl).build();
        this.internalSecret = internalSecret;
    }

    public void validateUserProfile(Long userId) {
        if (!exists(profilesClient, "/api/v1/user-profiles/exists/by-user/{userId}", userId)
                && !exists(profilesClient, "/api/v1/user-profiles/{profileId}", userId)) {
            throw new IllegalArgumentException("No regular user profile found with ID: " + userId);
        }
    }

    public void validateNutritionist(Long nutritionistId) {
        if (!exists(nutritionistsClient, "/api/v1/nutritionists/{nutritionistId}", nutritionistId)) {
            throw new IllegalArgumentException("No nutritionist found with id: " + nutritionistId);
        }
    }

    private boolean exists(RestClient client, String uri, Long id) {
        try {
            client.get()
                    .uri(uri, id)
                    .header(INTERNAL_HEADER, internalSecret)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED || ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                LOGGER.error("Internal validation request to {} with id {} was rejected with status {}",
                        uri, id, ex.getStatusCode(), ex);
                throw new IllegalStateException("Internal validation request was rejected by target service", ex);
            }
            LOGGER.error("Internal validation request to {} with id {} failed with status {}",
                    uri, id, ex.getStatusCode(), ex);
            throw new IllegalStateException("Internal validation request failed", ex);
        } catch (RestClientException ex) {
            LOGGER.error("Internal validation request to {} with id {} failed", uri, id, ex);
            throw new IllegalStateException("Internal validation request failed", ex);
        }
    }

}
