package pe.edu.upc.center.jameoFit.mealplan.infrastructure.clients.resources;

public record TrackingMealPlanEntryRequest(
        Long userId,
        Long trackingId,
        Long recipeId,
        String mealPlanType,
        int day
) {
}
