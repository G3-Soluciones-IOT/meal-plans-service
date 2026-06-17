package pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.resources;

public record MealPlanTemplateResource(
        int id,
        String name,
        String description,
        String category,
        Integer nutritionistId,
        String nutritionistName,
        double calories,
        double carbs,
        double proteins,
        double fats
) {}
