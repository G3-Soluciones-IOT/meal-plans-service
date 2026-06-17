package pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.resources;

public record MealPlanEntryResource(
        int id,
        int recipeId,
        int day,
        int mealPlanType,
        int mealPlanId
) {
}
