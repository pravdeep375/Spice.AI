package com.example.mohdz.spiceai.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.mohdz.spiceai.R;

import com.example.mohdz.spiceai.model.DataDump;
import com.example.mohdz.spiceai.model.Ingredient;
import com.example.mohdz.spiceai.model.IngredientListImpl;
import com.example.mohdz.spiceai.model.Recipe;
import com.example.mohdz.spiceai.model.RecipeDetail;
import com.example.mohdz.spiceai.model.interfaces.IngredientList;
import com.example.mohdz.spiceai.presenter.MainPresenterImpl;
import com.example.mohdz.spiceai.presenter.enums.MainViewState;
import com.example.mohdz.spiceai.presenter.interfaces.MainPresenter;
import com.example.mohdz.spiceai.view.fragments.DashboardFragment;
import com.example.mohdz.spiceai.view.interfaces.DashboardFragmentListener;
import com.example.mohdz.spiceai.view.interfaces.IngredientListFragmentListener;
import com.example.mohdz.spiceai.view.interfaces.MainView;

import com.example.mohdz.spiceai.view.interfaces.RecipeFragmentListener;
import com.hannesdorfmann.mosby3.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;

/**
 * Created by zeeshan on 2017-10-28.
 */

public class MainActivity extends MvpViewStateActivity<MainView, MainPresenter, ViewState<MainView>> implements MainView, DashboardFragmentListener, RecipeFragmentListener, IngredientListFragmentListener, View.OnClickListener{

    private FrameLayout toolbarBackButton;
    private ArrayList<String> groceryListsToDisplay;
    private IngredientListImpl ingredientList;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<RecipeDetail> recipesToDisplay;
    private ArrayList<Recipe> allRecipes;
    private RecipeDetail recipeDetails;
    private Boolean hideButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbarBackButton = (FrameLayout)findViewById(R.id.toolbarBackButton);
        assert toolbarBackButton != null;
        toolbarBackButton.setOnClickListener(this);
    }

    public Context getContext(){
        return this;
    }
    @Override
    public ViewState<MainView> createViewState() {
        return new MainRetainViewState();
    }
    @Override
    public void onNewViewStateInstance() {
        getPresenter().onStart();
    }

    @NonNull
    @Override
    public MainPresenter createPresenter(){
        return new MainPresenterImpl();
    }

    @Override
    public void onClick(View view) {
        MainRetainViewState mainRetainViewState = (MainRetainViewState) viewState;
        switch (view.getId()) {
            case R.id.toolbarBackButton:
                mainRetainViewState.setShowDashboard();
                getPresenter().toolbarBackButtonPressed();
                break;
        }
    }

    @Override
    public void buttonPressed(View view, int id) {
        MainRetainViewState mainRetainViewState = (MainRetainViewState) viewState;
        switch (view.getId()) {
            case R.id.updateInventory:
                mainRetainViewState.setShowInventory();
                getPresenter().updateInventoryButtonPressed();
                break;
            case -1:
                mainRetainViewState.setShowRecipe();
                hideButton = true;
                getPresenter().recipeSearched(id);
                break;
        }
    }

    @Override
    public void createList(View view, RecipeDetail r) {
        MainRetainViewState mainRetainViewState = (MainRetainViewState) viewState;
        switch (view.getId()) {
            case R.id.create_list_button:
                mainRetainViewState.setShowRecipe();
                hideButton = true;
                getPresenter().createList(r);
                break;
        }
    }

    @Override
    public void search(View view, int id) {
        MainRetainViewState mainRetainViewState = (MainRetainViewState) viewState;
        switch (view.getId()) {
            case R.id.autoCompleteTextView:
                mainRetainViewState.setShowRecipe();
                hideButton = false;
                getPresenter().recipeSearched(id);
                break;
        }
    }

    public void updateList(View view, String title, ArrayList<Ingredient> items){
        MainRetainViewState mainRetainViewState = (MainRetainViewState) viewState;
        switch (view.getId()) {
            case R.id.deleteButton:
                if(title == "Inventory"){
                    mainRetainViewState.setShowInventory();
                }else{
                    mainRetainViewState.setShowGroceryList();
                }
                getPresenter().deleteListItem(title, items);
                break;
            case R.id.AddItemAutoCompleteTextView:
                if(title == "Inventory"){
                    mainRetainViewState.setShowInventory();
                }else{
                    mainRetainViewState.setShowGroceryList();
                }
                getPresenter().addListItem(title, items);
        }
    }

    public void listButton(Button view){
        MainRetainViewState mainRetainViewState = (MainRetainViewState) viewState;
        mainRetainViewState.setShowGroceryList();
        getPresenter().groceryListButtonPressed(view.getText().toString());
    }

    @Override
    public void switchFragment(MainViewState viewState) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = viewState.getFragment();
        ft.replace(R.id.fragmentContainer, fragment);
        ft.commit();
    }

    //LOADING DATA FROM PRESENTER
    public void loadRecipesToDisplay(ArrayList<RecipeDetail> data){
        recipesToDisplay = data;
    }
    public void loadAllRecipes(ArrayList<Recipe> data){ allRecipes = data; }
    public void loadRecipeDetails(RecipeDetail data){
        recipeDetails = data;
    }
    public void loadGroceryListsToDisplay(ArrayList<String> data){
        groceryListsToDisplay = data;
    }
    public void loadIngredientList(IngredientListImpl data){ ingredientList = data; }
    public void loadAllIngredients(ArrayList<Ingredient> data){ingredients = data;}

    //For fragments
    public ArrayList<String> getGroceryListsToDisplay() { return groceryListsToDisplay; }
    public IngredientListImpl getIngredientList() { return ingredientList; }
    public ArrayList<Ingredient> getAllIngredients(){return ingredients;}
    public ArrayList<Recipe> getAllRecipes() { return allRecipes; }
    public RecipeDetail getRecipeDetails() { return recipeDetails; }
    public ArrayList<RecipeDetail> getRecipesToDisplay() {
        //presenter.loadRecipesToDisplay();
        return recipesToDisplay;
    }

    public Boolean hideButton(){
        return hideButton;
    }
}
