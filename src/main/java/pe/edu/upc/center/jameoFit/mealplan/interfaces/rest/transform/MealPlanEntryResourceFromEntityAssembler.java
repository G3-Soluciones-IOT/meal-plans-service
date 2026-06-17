package pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.transform;

import pe.edu.upc.center.jameoFit.mealplan.domain.model.entities.MealPlanEntry;
import pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.resources.MealPlanEntryResource;

import java.util.List;

public class MealPlanEntryResourceFromEntityAssembler {
    public static List<MealPlanEntryResource> toResourceFromEntities(List<MealPlanEntry> entities) {
        return entities.stream()
                .map(entity -> new MealPlanEntryResource(
                        entity.getId(),
                        entity.getRecipeId().recipeId(),
                        entity.getDay(),
                        entity.getMealPlanType().getId(),
                        entity.getMealPlan().getId()
                ))
                .toList();
    }
}
