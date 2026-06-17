package pe.edu.upc.center.jameoFit.mealplan.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.center.jameoFit.mealplan.domain.model.entities.MealPlanType;
import pe.edu.upc.center.jameoFit.mealplan.domain.model.valueobjects.MealPlanTypes;

import java.util.Optional;

@Repository
public interface MealPlanTypeRepository extends JpaRepository<MealPlanType, Integer> {
    boolean existsByType(MealPlanTypes type);
    Optional<MealPlanType> findByType(MealPlanTypes type);
}
