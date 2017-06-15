package com.underarmour.nytimes.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Article {

    @SerializedName("web_url")
    String webUrl;
    @SerializedName("snippet")
    String snippet;
    @SerializedName("lead_paragraph")
    String paragraph;
    @SerializedName("multimedia")
    ArrayList<Multimedia> mediaList;
    @SerializedName("headline")
    Headline headline;
    @SerializedName("source")
    String sourceText;

    // empty constructor for parceler library
    public Article(){}

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Headline getHeadline() {
        return headline;
    }

    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public ArrayList<Multimedia> getMediaList() {
        return mediaList;
    }

    public void setMediaList(ArrayList<Multimedia> mediaList) {
        this.mediaList = mediaList;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }
}
