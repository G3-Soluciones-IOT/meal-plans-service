package pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.transform;

import pe.edu.upc.center.jameoFit.mealplan.domain.model.commands.UpdateMealPlanEntryCommand;
import pe.edu.upc.center.jameoFit.mealplan.domain.model.valueobjects.RecipeId;
import pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.resources.MealPlanEntryResource;

public class UpdateMealPlanEntryCommandFromResourceAssembler {
    public static UpdateMealPlanEntryCommand toCommandFromResource(MealPlanEntryResource resource) {
        return new UpdateMealPlanEntryCommand(
                resource.id(),
                new RecipeId(resource.recipeId()),
                resource.day(),           // â† primero day
                resource.mealPlanType(),  // â† luego mealPlanTypeId
                resource.mealPlanId()
        );
    }
}
