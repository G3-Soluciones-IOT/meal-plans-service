package pe.edu.upc.center.jameoFit.mealplan.domain.model.valueobjects;

public record RecipeId(int recipeId) {
    public RecipeId {
        if (recipeId < 0) {
            throw new IllegalArgumentException("Profile profileId cannot be negative");
        }
    }

    public RecipeId() {
        this(0);
    }
}
