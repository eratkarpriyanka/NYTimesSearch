package com.underarmour.nytimes.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseObj {

    @SerializedName("docs")
    ArrayList<Article> listArticles;

    public ArrayList<Article> getListArticles() {
        return listArticles;
    }

    public void setListArticles(ArrayList<Article> listArticles) {
        this.listArticles = listArticles;
    }
}
