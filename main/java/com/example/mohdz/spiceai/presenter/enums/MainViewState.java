package com.example.mohdz.spiceai.presenter.enums;

import android.support.v4.app.Fragment;

import com.example.mohdz.spiceai.view.fragments.DashboardFragment;
import com.example.mohdz.spiceai.view.fragments.IngredientListFragment;
import com.example.mohdz.spiceai.view.fragments.RecipeFragment;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zeeshan on 2017-10-30.
 */

public enum MainViewState {

    DASHBOARD_FRAGMENT(new DashboardFragment()),
    RECIPE_FRAGMENT(new RecipeFragment()),
    INGREDIENT_LIST_FRAGMENT(new IngredientListFragment());

    private Fragment fragment;

    MainViewState(Fragment fragment){
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
