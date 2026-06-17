package pe.edu.upc.center.jameoFit.mealplan.domain.services;

import pe.edu.upc.center.jameoFit.mealplan.domain.model.commands.SeedMealPlanTypesCommand;

public interface MealPlanTypeCommandService {
    void handle(SeedMealPlanTypesCommand command);
}
