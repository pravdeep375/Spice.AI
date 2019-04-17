package com.example.mohdz.spiceai.model;

import com.example.mohdz.spiceai.model.interfaces.RecipeList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pravdeep Deol on 2017-10-31.
 */

public class RecipeListImpl implements RecipeList {
    public ArrayList<Recipe> recipes;

    public ArrayList<Recipe> getRecipeList() {
        return recipes;
    }

    public Recipe add(Recipe toAdd){
        recipes.add(recipes.size(), toAdd);
        return toAdd;
    }

    public Recipe remove(int id){
        for (int i=0; i<recipes.size();i++){
            if (recipes.get(i).getId()==id){
                return recipes.remove(i);
            }
        }

        return null;
    }
}
