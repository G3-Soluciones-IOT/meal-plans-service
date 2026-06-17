package pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.resources;

public record MealPlanEntryDetailedResource(
        int id,
        int recipeId,
        String recipeName,
        String recipeDescription,
        int day,
        int mealPlanType,
        int mealPlanId
) {
}
