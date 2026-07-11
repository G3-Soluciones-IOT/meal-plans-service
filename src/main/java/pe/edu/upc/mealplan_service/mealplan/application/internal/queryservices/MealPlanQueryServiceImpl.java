package pe.edu.upc.mealplan_service.mealplan.application.internal.queryservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.ExternalMealPlanRecipeService;
import pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan;
import pe.edu.upc.mealplan_service.mealplan.domain.model.queries.*;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanQueryService;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.clients.resources.RecipeResource;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanEntryRepository;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanRepository;
import pe.edu.upc.mealplan_service.mealplan.interfaces.rest.resources.MealPlanEntryDetailedResource;

import java.util.List;
import java.util.Optional;

@Service
public class MealPlanQueryServiceImpl implements MealPlanQueryService {

    private final MealPlanRepository mealPlanRepository;
    private final MealPlanEntryRepository mealPlanEntryRepository;
    private final ExternalMealPlanRecipeService externalMealPlanRecipeService;

    public MealPlanQueryServiceImpl(MealPlanRepository mealPlanRepository,
                                    MealPlanEntryRepository mealPlanEntryRepository,
                                    ExternalMealPlanRecipeService externalMealPlanRecipeService) {
        this.mealPlanRepository = mealPlanRepository;
        this.externalMealPlanRecipeService = externalMealPlanRecipeService;
        this.mealPlanEntryRepository = mealPlanEntryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MealPlan> handle(GetMealPlanByIdQuery query) {
        return this.mealPlanRepository.findById(query.mealPlanId())
                .map(this::initializeCollections);
    }

    public List<RecipeResource> getAllRecipes() {
        return externalMealPlanRecipeService.fetchAllRecipes();
    }

    public List<MealPlanEntryDetailedResource> handle(GetEntriesWithRecipeInfo query) {
        var entries = mealPlanEntryRepository.findAllByMealPlan_Id(query.mealPlanId());

        return entries.stream().map(entry -> {
            var recipeOpt = externalMealPlanRecipeService.fetchRecipeById(entry.getRecipeId().recipeId());
            var recipe = recipeOpt.orElse(null);

            return new MealPlanEntryDetailedResource(
                    entry.getId(),
                    entry.getRecipeId().recipeId(),
                    recipe != null ? recipe.name() : null,
                    recipe != null ? recipe.description() : null,
                    entry.getDay(),
                    entry.getMealPlanType().getId(),
                    entry.getMealPlan().getId()
            );
        }).toList();
    }

    @Override
    public List<RecipeResource> handle(GetAllRecipesQuery query) {
        return externalMealPlanRecipeService.fetchAllRecipes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealPlan> handle(GetAllMealPlanQuery query) {
        return this.mealPlanRepository.findAll().stream()
                .map(this::initializeCollections)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealPlan> handle(GetAllMealPlanByProfileIdQuery query) {
        return this.mealPlanRepository.findAllByProfileId_UserProfileId(query.ProfileId()).stream()
                .map(this::initializeCollections)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealPlan> handle(GetOriginalTemplatesQuery query) {
        return this.mealPlanRepository
                .findAllByCreatedByNutritionistIdIsNotNullAndProfileId_UserProfileIdIsNull().stream()
                .map(this::initializeCollections)
                .toList();
    }

    private MealPlan initializeCollections(MealPlan mealPlan) {
        if (mealPlan.getEntries() != null && mealPlan.getEntries().getMealPlanEntries() != null) {
            mealPlan.getEntries().getMealPlanEntries().forEach(entry -> {
                entry.getId();
                entry.getRecipeId();
                if (entry.getMealPlanType() != null) {
                    entry.getMealPlanType().getId();
                }
            });
        }
        if (mealPlan.getTags() != null && mealPlan.getTags().getMealPlanTags() != null) {
            mealPlan.getTags().getMealPlanTags().forEach(tag -> {
                tag.getId();
                tag.getTag();
            });
        }
        return mealPlan;
    }
}
