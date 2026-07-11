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
import pe.edu.upc.mealplan_service.mealplan.infrastructure.clients.resources.RecipeNutritionResource;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.clients.resources.RecipeResource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ExternalMealPlanRecipeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalMealPlanRecipeService.class);
    private static final String INTERNAL_HEADER = "X-Internal-Request";

    private final RestClient restClient;
    private final String internalSecret;

    public ExternalMealPlanRecipeService(
            @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClientBuilder,
            @Value("${services.recipes.base-url}") String recipesBaseUrl,
            @Value("${authorization.internal-service.secret}") String internalSecret) {
        this.restClient = restClientBuilder.baseUrl(recipesBaseUrl).build();
        this.internalSecret = internalSecret;
    }

    public Optional<RecipeResource> fetchRecipeById(int recipeId) {
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/api/v1/recipes/{recipeId}", recipeId)
                    .header(INTERNAL_HEADER, internalSecret)
                    .retrieve()
                    .body(RecipeResource.class));
        } catch (HttpClientErrorException.NotFound ex) {
            return Optional.empty();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED || ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                LOGGER.error("recipes-service rejected internal recipe lookup for recipeId {} with status {}",
                        recipeId, ex.getStatusCode(), ex);
                throw new IllegalStateException("recipes-service rejected internal recipe lookup request", ex);
            }
            LOGGER.error("recipes-service recipe lookup failed for recipeId {} with status {}",
                    recipeId, ex.getStatusCode(), ex);
            throw new IllegalStateException("recipes-service recipe lookup request failed", ex);
        } catch (RestClientException ex) {
            LOGGER.error("recipes-service recipe lookup failed for recipeId {}", recipeId, ex);
            throw new IllegalStateException("recipes-service recipe lookup request failed", ex);
        }
    }

    public List<RecipeResource> fetchAllRecipes() {
        try {
            var recipes = restClient.get()
                    .uri("/api/v1/recipes")
                    .header(INTERNAL_HEADER, internalSecret)
                    .retrieve()
                    .body(RecipeResource[].class);
            return recipes == null ? List.of() : Arrays.asList(recipes);
        } catch (HttpClientErrorException.NotFound ex) {
            return List.of();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED || ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                LOGGER.error("recipes-service rejected internal recipes list request with status {}",
                        ex.getStatusCode(), ex);
                throw new IllegalStateException("recipes-service rejected internal recipes list request", ex);
            }
            LOGGER.error("recipes-service recipes list request failed with status {}", ex.getStatusCode(), ex);
            throw new IllegalStateException("recipes-service recipes list request failed", ex);
        } catch (RestClientException ex) {
            LOGGER.error("recipes-service recipes list request failed", ex);
            throw new IllegalStateException("recipes-service recipes list request failed", ex);
        }
    }

    public RecipeNutritionResource fetchNutrition(int recipeId) {
        try {
            var nutrition = restClient.get()
                    .uri("/api/v1/recipes/{recipeId}/nutrition", recipeId)
                    .header(INTERNAL_HEADER, internalSecret)
                    .retrieve()
                    .body(RecipeNutritionResource.class);
            return nutrition == null ? new RecipeNutritionResource(0, 0, 0, 0) : nutrition;
        } catch (HttpClientErrorException.NotFound ex) {
            return new RecipeNutritionResource(0, 0, 0, 0);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED || ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                LOGGER.error("recipes-service rejected internal nutrition lookup for recipeId {} with status {}",
                        recipeId, ex.getStatusCode(), ex);
                throw new IllegalStateException("recipes-service rejected internal nutrition lookup request", ex);
            }
            LOGGER.error("recipes-service nutrition lookup failed for recipeId {} with status {}",
                    recipeId, ex.getStatusCode(), ex);
            throw new IllegalStateException("recipes-service nutrition lookup request failed", ex);
        } catch (RestClientException ex) {
            LOGGER.error("recipes-service nutrition lookup failed for recipeId {}", recipeId, ex);
            throw new IllegalStateException("recipes-service nutrition lookup request failed", ex);
        }
    }
}
