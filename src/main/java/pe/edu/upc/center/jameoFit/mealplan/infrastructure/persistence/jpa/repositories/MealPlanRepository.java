package pe.edu.upc.center.jameoFit.mealplan.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.center.jameoFit.mealplan.domain.model.aggregates.MealPlan;

import java.util.List;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Integer> {

    List<MealPlan> findAllByProfileId_UserProfileId(Integer userProfileId);

    List<MealPlan> findAllByCreatedByNutritionistId(Integer nutritionistUserId);

    List<MealPlan> findAllByCreatedByNutritionistIdIsNotNullAndProfileId_UserProfileIdIsNull();
}
