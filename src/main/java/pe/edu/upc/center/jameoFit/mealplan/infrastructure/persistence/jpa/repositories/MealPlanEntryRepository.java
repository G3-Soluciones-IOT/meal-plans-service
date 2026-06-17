package pe.edu.upc.center.jameoFit.mealplan.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.center.jameoFit.mealplan.domain.model.entities.MealPlanEntry;

import java.util.List;

@Repository
public interface MealPlanEntryRepository extends JpaRepository<MealPlanEntry, Integer> {
   List<MealPlanEntry> findAllByMealPlan_Id(Integer mealPlanId);
}
