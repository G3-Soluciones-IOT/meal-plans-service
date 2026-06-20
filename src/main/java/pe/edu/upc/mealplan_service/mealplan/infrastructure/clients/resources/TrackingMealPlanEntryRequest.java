package pe.edu.upc.mealplan_service.mealplan.infrastructure.clients.resources;

public record TrackingMealPlanEntryRequest(
        Long userId,
        Long trackingId,
        Long recipeId,
        String mealPlanType,
        int day
) {
}
