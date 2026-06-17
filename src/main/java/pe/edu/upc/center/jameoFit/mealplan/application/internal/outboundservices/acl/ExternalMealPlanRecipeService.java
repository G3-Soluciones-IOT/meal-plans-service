package pe.edu.upc.center.jameoFit.mealplan.application.internal.outboundservices.acl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pe.edu.upc.center.jameoFit.mealplan.infrastructure.clients.resources.RecipeNutritionResource;
import pe.edu.upc.center.jameoFit.mealplan.infrastructure.clients.resources.RecipeResource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ExternalMealPlanRecipeService {

    private final RestClient restClient;

    public ExternalMealPlanRecipeService(
            RestClient.Builder restClientBuilder,
            @Value("${services.recipes.base-url}") String recipesBaseUrl) {
        this.restClient = restClientBuilder.baseUrl(recipesBaseUrl).build();
    }

    public Optional<RecipeResource> fetchRecipeById(int recipeId) {
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/api/v1/recipes/{recipeId}", recipeId)
                    .retrieve()
                    .body(RecipeResource.class));
        } catch (RestClientException ex) {
            return Optional.empty();
        }
    }

    public List<RecipeResource> fetchAllRecipes() {
        try {
            var recipes = restClient.get()
                    .uri("/api/v1/recipes")
                    .retrieve()
                    .body(RecipeResource[].class);
            return recipes == null ? List.of() : Arrays.asList(recipes);
        } catch (RestClientException ex) {
            return List.of();
        }
    }

    public RecipeNutritionResource fetchNutrition(int recipeId) {
        try {
            var nutrition = restClient.get()
                    .uri("/api/v1/recipes/{recipeId}/nutrition", recipeId)
                    .retrieve()
                    .body(RecipeNutritionResource.class);
            return nutrition == null ? new RecipeNutritionResource(0, 0, 0, 0) : nutrition;
        } catch (RestClientException ex) {
            return new RecipeNutritionResource(0, 0, 0, 0);
        }
    }
}
