package com.underarmour.nytimes.models;

import com.google.gson.annotations.SerializedName;

public class SearchApiResponse {

    @SerializedName("response")
    ResponseObj response;

    public ResponseObj getResponse() {
        return response;
    }

    public void setResponse(ResponseObj response) {
        this.response = response;
    }
}
