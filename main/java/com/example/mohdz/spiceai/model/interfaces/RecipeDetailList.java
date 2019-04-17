package com.example.mohdz.spiceai.model.interfaces;

import com.example.mohdz.spiceai.model.RecipeDetail;

import java.util.ArrayList;

/**
 * Created by zeeshan on 2017-10-30.
 */

public interface RecipeDetailList {
    ArrayList<RecipeDetail> getRecipeDetailList();

    public RecipeDetail add(RecipeDetail toAdd);

    public RecipeDetail remove(int index);

    public RecipeDetail findRecipeDetail(int id);

    public RecipeDetail findRecipeDetail(String name);
}
