package pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.transform;

import pe.edu.upc.center.jameoFit.mealplan.domain.model.aggregates.MealPlan;
import pe.edu.upc.center.jameoFit.mealplan.domain.model.entities.MealPlanTag;
import pe.edu.upc.center.jameoFit.mealplan.interfaces.rest.resources.MealPlanResource;

import java.util.List;

public class MealPlanResourceFromEntityAssembler {

    public static MealPlanResource toResourceFromEntity(MealPlan mealPlan) {
        Double calories = null, carbs = null, proteins = null, fats = null;

        if (mealPlan.getMacros() != null) {
            calories = mealPlan.getMacros().getCalories();
            carbs = mealPlan.getMacros().getCarbs();
            proteins = mealPlan.getMacros().getProteins();
            fats = mealPlan.getMacros().getFats();
        }

        Integer profileId = null;
        if (mealPlan.getProfileId() != null) {
            profileId = mealPlan.getProfileId().userProfileId();
        }

        List<String> tagStrings;
        try {
            tagStrings = mealPlan.getTags() != null ?
                    mealPlan.getTags().getMealPlanTags()
                            .stream()
                            .map(MealPlanTag::getTag)
                            .toList()
                    : List.of();
        } catch (Exception ignored) {
            tagStrings = List.of();
        }

        // entries mapping
        var entries = MealPlanEntryResourceFromEntityAssembler.toResourceFromEntities(
                mealPlan.getEntries() == null ? List.of() : mealPlan.getEntries().getMealPlanEntries()
        );

        return new MealPlanResource(
                mealPlan.getId(),
                mealPlan.getName(),
                mealPlan.getDescription(),
                calories,
                carbs,
                proteins,
                fats,
                profileId,
                mealPlan.getCategory(),
                mealPlan.getIsCurrent(),
                entries,
                tagStrings
        );
    }
}
