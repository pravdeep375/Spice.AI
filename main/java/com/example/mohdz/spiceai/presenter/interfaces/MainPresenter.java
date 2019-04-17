package com.example.mohdz.spiceai.presenter.interfaces;

import com.example.mohdz.spiceai.model.Ingredient;
import com.example.mohdz.spiceai.model.RecipeDetail;
import com.example.mohdz.spiceai.view.interfaces.MainView;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

import java.util.ArrayList;

/**
 * Created by zeeshan on 2017-10-28.
 */

public interface MainPresenter extends MvpPresenter<MainView> {

    void onStart();
    void toolbarBackButtonPressed();
    void updateInventoryButtonPressed();
    void groceryListButtonPressed(String name);
    void recipeTilePressed();
    void recipeSearched(int id);
    void createList(RecipeDetail r);
    void deleteListItem(String title, ArrayList<Ingredient> items);
    void loadRecipesToDisplay();
    void addListItem(String title, ArrayList<Ingredient> items);
}
