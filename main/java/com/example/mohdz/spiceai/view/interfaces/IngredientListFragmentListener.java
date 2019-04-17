package com.example.mohdz.spiceai.view.interfaces;

import android.view.View;

import com.example.mohdz.spiceai.model.Ingredient;

import java.util.ArrayList;

/**
 * Created by zeeshan on 2017-10-30.
 */

public interface IngredientListFragmentListener {
    void updateList(View view, String title, ArrayList<Ingredient> items);
}
