package com.example.mohdz.spiceai.model;

/**
 * Created by zeeshan on 2017-10-08.
 */

public class Recipe {
    private String name;
    private int id;

    public Recipe(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
