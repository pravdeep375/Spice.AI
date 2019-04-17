package com.example.mohdz.spiceai.view.interfaces;

import android.view.View;

import com.example.mohdz.spiceai.model.Recipe;
import com.example.mohdz.spiceai.model.RecipeDetail;

/**
 * Created by zeeshan on 2017-10-30.
 */

public interface RecipeFragmentListener {
    void createList(View v, RecipeDetail r);
}
