package pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.transform;

import pe.edu.upc.center.jameoFit.mealplan.domain.model.commands.CreateMealPlanCommand;
import pe.edu.upc.center.jameoFit.mealplan.domain.model.valueobjects.MealPlanMacros;
import pe.edu.upc.center.jameoFit.mealplan.domain.model.valueobjects.UserProfileId;
import pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.resources.CreateMealPlanResource;

public class CreateMealPlanCommandFromResourceAssembler {

    /**
     * Build CreateMealPlanCommand from resource.
     * Accepts nutritionistId as Long (from controller) and converts to Integer for command.
     * If nutritionistId == null then createdByNutritionistId is null.
     */
    public static CreateMealPlanCommand toCommandFromResource(CreateMealPlanResource resource, Long nutritionistIdLong) {
        Integer nutritionistId = nutritionistIdLong == null ? null : (nutritionistIdLong > Integer.MAX_VALUE ? throwOverflow() : nutritionistIdLong.intValue());

        MealPlanMacros macros = new MealPlanMacros(
                resource.calories() == null ? 0.0 : resource.calories().doubleValue(),
                resource.carbs() == null ? 0.0 : resource.carbs().doubleValue(),
                resource.proteins() == null ? 0.0 : resource.proteins().doubleValue(),
                resource.fats() == null ? 0.0 : resource.fats().doubleValue()
        );

        UserProfileId profileId = null;
        if (resource.profileId() != null && resource.profileId() > 0) {
            profileId = new UserProfileId(resource.profileId());
        }

        return new CreateMealPlanCommand(
                resource.name(),
                resource.description(),
                macros,
                profileId,
                resource.category(),
                resource.isCurrent() != null ? resource.isCurrent() : false,
                resource.tags() != null ? resource.tags() : java.util.List.of(),
                nutritionistId
        );
    }

    private static Integer throwOverflow() {
        throw new IllegalArgumentException("nutritionistId is too large to fit into Integer");
    }
}
