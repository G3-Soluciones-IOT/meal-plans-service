package pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.transform;

import pe.edu.upc.center.jameoFit.mealplan.domain.model.entities.MealPlanTag;
import pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.resources.MealPlanTagResource;

import java.util.List;

public class MealPlanTagResourceFromEntityAssembler {
    public static List<MealPlanTagResource> toResourceFromEntity(List<MealPlanTag> entities) {
        return entities.stream()
                .map(entity -> new MealPlanTagResource(
                        entity.getId(),
                        entity.getTag(),
                        entity.getMealPlan().getId()
                ))
                .toList();
    }
    }

