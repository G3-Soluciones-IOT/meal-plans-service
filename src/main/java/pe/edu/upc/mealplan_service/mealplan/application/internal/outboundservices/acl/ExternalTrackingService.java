package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.clients.resources.TrackingMealPlanEntryRequest;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.clients.resources.TrackingResource;

import java.util.Optional;

@Service
public class ExternalTrackingService {

    private final RestClient restClient;

    public ExternalTrackingService(
            RestClient.Builder restClientBuilder,
            @Value("${services.tracking.base-url}") String trackingBaseUrl) {
        this.restClient = restClientBuilder.baseUrl(trackingBaseUrl).build();
    }

    public Optional<TrackingResource> getTrackingByUserId(Long userId) {
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/api/v1/tracking/users/{userId}", userId)
                    .retrieve()
                    .body(TrackingResource.class));
        } catch (RestClientException ex) {
            return Optional.empty();
        }
    }

    public void addMealPlanEntryToTracking(TrackingMealPlanEntryRequest request) {
        restClient.post()
                .uri("/api/v1/tracking/{trackingId}/meal-plan-entries", request.trackingId())
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
