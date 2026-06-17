package pe.edu.upc.center.jameoFit.mealplan.domain.model.commands;

import pe.edu.upc.center.jameoFit.mealplan.domain.model.valueobjects.RecipeId;

public record CreateMealPlanEntryCommand(
        int mealPlanId,
        int recipeId,
        String type, // "Breakfast"|"Lunch"|"Dinner"|"Snack"
        int day
) {
}


