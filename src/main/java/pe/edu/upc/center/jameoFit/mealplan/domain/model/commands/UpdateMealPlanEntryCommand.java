package pe.edu.upc.center.jameoFit.mealplan.domain.model.commands;

import pe.edu.upc.center.jameoFit.mealplan.domain.model.valueobjects.RecipeId;

public record UpdateMealPlanEntryCommand(
        int id,
        RecipeId recipeId,
        int day,
        int mealPlanTypeId,
        int mealPlanId
) {
}
