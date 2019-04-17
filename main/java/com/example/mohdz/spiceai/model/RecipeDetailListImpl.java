package com.example.mohdz.spiceai.model;

import com.example.mohdz.spiceai.model.interfaces.RecipeDetailList;

import java.util.ArrayList;

/**
 * Created by carlotutor on 2017-10-31.
 */

public class RecipeDetailListImpl implements RecipeDetailList {

    public ArrayList<RecipeDetail> recipeDetails;

    public RecipeDetailListImpl(){
        recipeDetails = new ArrayList<>();
    }

    public ArrayList<RecipeDetail> getRecipeDetailList()
    {
        return recipeDetails;
    }

    @Override
    public RecipeDetail add(RecipeDetail toAdd)
    {
        recipeDetails.add(recipeDetails.size(), toAdd);
        return toAdd;
    }

    public RecipeDetail remove(int id)
    {
        for (int i = 0; i < recipeDetails.size(); i++)
        {
            if (recipeDetails.get(i).getRecipeId() == id)
            {
                return recipeDetails.remove(i);
            }
        }
        return null;
    }

    public RecipeDetail findRecipeDetail(int id)
    {
        for (int i = 0; i < recipeDetails.size(); i++)
        {
            if (recipeDetails.get(i).getRecipeId() == id)
            {
                return recipeDetails.get(i);
            }
        }
        return null;
    }

    public RecipeDetail findRecipeDetail(String name)
    {
        for (int i = 0; i < recipeDetails.size(); i++)
        {
            if (recipeDetails.get(i).getRecipeName().equals(name))
            {
                return findRecipeDetail(i);
            }
        }
        return null;
    }
}
