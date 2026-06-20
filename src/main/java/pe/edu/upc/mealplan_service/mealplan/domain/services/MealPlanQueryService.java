package pe.edu.upc.mealplan_service.mealplan.domain.services;

import pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan;
import pe.edu.upc.center.jameoFit.mealplan.domain.model.queries.*;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.*;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.clients.resources.RecipeResource;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.MealPlanEntryDetailedResource;

import java.util.List;
import java.util.Optional;

public interface MealPlanQueryService {
    Optional<MealPlan> handle(GetMealPlanByIdQuery query);
    List<MealPlan> handle(GetAllMealPlanQuery query);
    List<MealPlan> handle(GetAllMealPlanByProfileIdQuery query);
    List<MealPlanEntryDetailedResource> handle(GetEntriesWithRecipeInfo query);
    List<RecipeResource> handle(GetAllRecipesQuery query);
    List<MealPlan> handle(GetOriginalTemplatesQuery query);

}
