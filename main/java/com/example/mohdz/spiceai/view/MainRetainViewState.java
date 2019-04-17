package com.example.mohdz.spiceai.view;

import com.example.mohdz.spiceai.presenter.enums.MainViewState;
import com.example.mohdz.spiceai.view.interfaces.MainView;
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState;

/**
 * Created by zeeshan on 2017-10-30.
 */

public class MainRetainViewState implements ViewState<MainView> {

    final int DASHBOARD = 0;
    final int RECIPE = 1;
    final int INGREDIENT_LIST = 2;

    int viewState = DASHBOARD;

    @Override
    public void apply(MainView view, boolean retained) {
        switch (viewState) {
            case (DASHBOARD):
                view.switchFragment(MainViewState.DASHBOARD_FRAGMENT);
                break;
            case (RECIPE):
                view.switchFragment(MainViewState.RECIPE_FRAGMENT);
                break;
            case (INGREDIENT_LIST):
                view.switchFragment(MainViewState.INGREDIENT_LIST_FRAGMENT);
                break;
        }
    }

    public void setShowDashboard(){
        viewState = DASHBOARD;
    }

    public void setShowInventory(){
        viewState = INGREDIENT_LIST;
    }

    public void setShowGroceryList(){
        viewState = INGREDIENT_LIST;
    }

    public void setShowRecipe(){
        viewState = RECIPE;
    }
}
