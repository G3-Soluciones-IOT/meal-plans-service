package pe.edu.upc.center.jameoFit.mealplan.domain.services;

import pe.edu.upc.center.jameoFit.mealplan.domain.model.entities.MealPlanType;
import pe.edu.upc.center.jameoFit.mealplan.domain.model.queries.GetAllMealPlanTypesQuery;
import pe.edu.upc.center.jameoFit.mealplan.domain.model.queries.GetMealPlanTypeByNameQuery;

import java.util.List;
import java.util.Optional;

public interface MealPlanTypeQueryService {
    List<MealPlanType> handle(GetAllMealPlanTypesQuery query);
    Optional<MealPlanType> handle(GetMealPlanTypeByNameQuery query);
}
