package com.example.mohdz.spiceai.model;

/**
 * Created by carlotutor on 2017-10-12.
 */

public class Ingredient {
    private String name;
    private int id;

    public Ingredient(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public int getId() { return id; }
}
