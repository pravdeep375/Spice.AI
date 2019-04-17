package com.example.mohdz.spiceai.model;

import android.content.Context;
import android.util.Log;

import com.example.mohdz.spiceai.model.interfaces.IngredientList;
import com.example.mohdz.spiceai.model.interfaces.RecipeDetailList;
import com.example.mohdz.spiceai.model.interfaces.RecipeList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zeeshan on 2017-10-31.
 */

public class DataDump {
    private RecipeDetailList allRecipeDetailList;
    private RecipeList allRecipeList;
    private IngredientList allIngredientList;
    private JSONObject obj;

    //server side
    public DataDump(){

    }
}
