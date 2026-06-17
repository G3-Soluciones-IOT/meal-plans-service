package pe.edu.upc.center.jameoFit.mealplan.domain.services;

import pe.edu.upc.center.jameoFit.mealplan.domain.model.commands.CreateMealPlanEntryCommand;

public interface MealPlanEntryCommandService {
    int handle(CreateMealPlanEntryCommand command);
}
