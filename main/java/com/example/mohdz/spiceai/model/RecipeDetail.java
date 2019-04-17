package com.example.mohdz.spiceai.model;

import java.util.ArrayList;

/**
 * Created by carlotutor on 2017-10-12.
 */

public class RecipeDetail {
    private ArrayList<String> directions, description, ingredients; // ingredients >> json.ing
    private String recipeName;
    private int recipeId;
    private ArrayList<Integer> ingredient_ids;

    public RecipeDetail(){
        this.directions = new ArrayList<>();
        this.description = new ArrayList<>();
        this.ingredients = new ArrayList<>();
        this.ingredient_ids = new ArrayList<>();
        this.recipeName = "";
        this.recipeId = -1;
    }

    public RecipeDetail(ArrayList<String> directions, ArrayList<String> description,
                        ArrayList<String> ingredients, String recipeName, int recipeId,
                        ArrayList<Integer> ingredient_ids) {
        this.directions = directions;
        this.description = description;
        this.ingredients = ingredients;
        this.recipeName = recipeName;
        this.recipeId = recipeId;
        this.ingredient_ids = ingredient_ids;
    }

    public ArrayList<String> getDirections() {
        return directions;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public ArrayList<Integer> getIngredient_ids() {
        return ingredient_ids;
    }
}

/*
Sample JOSN
  {
    "directions": ["Heat oil in heavy large skillet over medium-high heat. Add onion; saut\u00e9 until translucent and beginning to brown, about 4 minutes. Add wine and anchovy paste. Boil until reduced to 3/4 cup, about 3 minutes. Add tomatoes with juice; bring to boil.", "Sprinkle fish with salt and pepper. Add fish to skillet atop tomato mixture. Reduce heat to low, cover, and simmer until fish is cooked through, about 9 minutes. Using slotted metal spatula, transfer fish to plate and tent with foil to keep warm. Mix olives, 2 teaspoons oregano, and orange peel into sauce in skillet. Increase heat to high and boil until sauce is reduced and thickened, about 6 minutes. Season to taste with salt and pepper. Place 1 fish fillet on each of 4 plates. Pour sauce over and around fish, sprinkle with remaining 1 teaspoon oregano, and serve with warm toasted bread."],
    "fat": null,
    "categories": ["Fish", "Olive", "Tomato", "Saut\u00e9", "Low Fat", "Low Cal", "High Fiber", "Dinner", "Healthy", "Simmer", "Bon App\u00e9tit", "Pescatarian", "Dairy Free", "Peanut Free", "Tree Nut Free", "Soy Free", "Kosher"],
    "calories": null,
    "desc": "The Sicilian-style tomato sauce has tons of Mediterranean flavor, thanks to the orange peel, olives, and oregano.",
    "protein": null,
    "title": "Mahi-Mahi in Tomato Olive Sauce ",
    "ingredients": ["2 tablespoons extra-virgin olive oil", "1 cup chopped onion", "1 cup dry white wine", "1 teaspoon anchovy paste", "2 14 1/2-ounce cans diced tomatoes with garlic, basil, and oregano in juice", "4 6-ounce mahi-mahi fillets", "1/2 cup large green olives, quartered, pitted", "3 teaspoons chopped fresh oregano, divided", "1 teaspoon (packed) finely grated orange peel", "Country-style white bread cut into 1/2-inch-thick slices, toasted"],
    "sodium": null,
    "ing": ["tea", "white bread", "orange", "onion", "olive oil", "mahi mahi fillets", "olive", "white wine", "oregano"],
    "recipe_id": 1,
    "ing_ids": [1403, 1271, 603, 1667, 1118, 718, 1865, 919, 2230]
  }
*/
