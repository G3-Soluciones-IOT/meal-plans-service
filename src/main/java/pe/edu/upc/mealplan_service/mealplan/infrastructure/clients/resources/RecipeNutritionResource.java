package pe.edu.upc.mealplan_service.mealplan.infrastructure.clients.resources;

public record RecipeNutritionResource(
        double calories,
        double carbs,
        double proteins,
        double fats
) {
}
