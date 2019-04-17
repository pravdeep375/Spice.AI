package com.example.mohdz.spiceai.model;

import com.example.mohdz.spiceai.model.interfaces.IngredientList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pravdeep Deol on 2017-10-31.
 */

public class IngredientListImpl implements IngredientList {


    private ArrayList<Ingredient> ingredients;
    private String name;

    public IngredientListImpl(){
        name = "";
        ingredients = new ArrayList<>();
    }

    public ArrayList<Ingredient> getIngredients() { return ingredients; }
    public String getName() { return name; }
    public void setIngredients(ArrayList<Ingredient> ingredients) { this.ingredients = ingredients; }
    public void setName(String name){ this.name = name; }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredients;
    }

    public void add(Ingredient toAdd){
        ingredients.add(toAdd);
    }
    public Ingredient remove(int id){
        for (int i=0; i<ingredients.size();i++){
            if (ingredients.get(i).getId()==id){
                return ingredients.remove(i);
            }
        }

        return null;
    }

    public void removeItem(Ingredient item){
        ingredients.remove(item);
    }

}
