package com.example.testingproject.models;

import java.util.ArrayList;
import java.util.Date;

public class Recipe {

    public Recipe(){}
    public Recipe(String recipeId, String publishedBy, String dishTitle, String ingredients, String description, Date postedAt, ArrayList<String> recipeImages) {
        this.recipeId = recipeId;
        this.publishedBy = publishedBy;
        this.dishTitle = dishTitle;
        this.ingredients = ingredients;
        this.description = description;
        this.postedAt = postedAt;
        this.recipeImages = recipeImages;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public String getDishTitle() {
        return dishTitle;
    }

    public void setDishTitle(String dishTitle) {
        this.dishTitle = dishTitle;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt;
    }

    public ArrayList<String> getRecipeImages() {
        return recipeImages;
    }

    public void setRecipeImages(ArrayList<String> recipeImages) {
        this.recipeImages = recipeImages;
    }

    private String recipeId;
    private String publishedBy;
    private String dishTitle;
    private String ingredients;
    private String description;
    private Date postedAt;
    ArrayList<String>recipeImages;
}
