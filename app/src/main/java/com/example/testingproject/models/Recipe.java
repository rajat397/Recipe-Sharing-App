package com.example.testingproject.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Recipe implements Parcelable {

    public Recipe(){}
    public Recipe(String recipeId, String publishedBy, String dishTitle, String ingredients, String description, Date postedAt, ArrayList<String> recipeImages,boolean validate) {
        this.recipeId = recipeId;
        this.publishedBy = publishedBy;
        this.dishTitle = dishTitle;
        this.ingredients = ingredients;
        this.description = description;
        this.postedAt = postedAt;
        this.recipeImages = recipeImages;
        this.validate=validate;
    }

    protected Recipe(Parcel in) {
        recipeId = in.readString();
        publishedBy = in.readString();
        dishTitle = in.readString();
        ingredients = in.readString();
        description = in.readString();
        recipeImages = in.createStringArrayList();
        validate = in.readByte() != 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

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
    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    private String recipeId;
    private String publishedBy;
    private String dishTitle;
    private String ingredients;
    private String description;
    private Date postedAt;
    ArrayList<String>recipeImages;



    private boolean validate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(recipeId);
        parcel.writeString(publishedBy);
        parcel.writeString(dishTitle);
        parcel.writeString(ingredients);
        parcel.writeString(description);
        parcel.writeStringList(recipeImages);
        parcel.writeByte((byte) (validate ? 1 : 0));
    }
}
