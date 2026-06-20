package pe.edu.upc.mealplan_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MealPlansServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealPlansServiceApplication.class, args);
    }
}
