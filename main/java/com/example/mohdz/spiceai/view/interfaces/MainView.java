package com.example.mohdz.spiceai.view.interfaces;

import android.content.Context;

import com.example.mohdz.spiceai.model.Ingredient;
import com.example.mohdz.spiceai.model.IngredientListImpl;
import com.example.mohdz.spiceai.model.Recipe;
import com.example.mohdz.spiceai.model.RecipeDetail;
import com.example.mohdz.spiceai.presenter.enums.MainViewState;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.ArrayList;

/**
 * Created by zeeshan on 2017-10-28.
 */

public interface MainView extends MvpView {
    void switchFragment(MainViewState viewState);
    Context getContext();
    void loadRecipesToDisplay(ArrayList<RecipeDetail> data);
    void loadAllRecipes(ArrayList<Recipe> data);
    void loadRecipeDetails(RecipeDetail data);
    void loadGroceryListsToDisplay(ArrayList<String> data);
    void loadIngredientList(IngredientListImpl data);
    void loadAllIngredients(ArrayList<Ingredient> data);

}
