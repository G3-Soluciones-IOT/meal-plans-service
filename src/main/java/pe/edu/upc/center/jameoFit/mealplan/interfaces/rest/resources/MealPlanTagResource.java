package pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.resources;

public record MealPlanTagResource(
        int id,
        String tag,
        int mealPlanId
) {
}
