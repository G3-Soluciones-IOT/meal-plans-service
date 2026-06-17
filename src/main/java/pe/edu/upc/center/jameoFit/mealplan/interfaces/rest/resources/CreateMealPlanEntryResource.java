package pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.resources;

public record CreateMealPlanEntryResource(
        int recipeId,
        String type,
        int day,
        Long userId // necesario para actualizar tracking
) {
}
