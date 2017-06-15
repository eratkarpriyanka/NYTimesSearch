package com.underarmour.nytimes.network;


import com.underarmour.nytimes.models.SearchApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ISearchApiEndpoint {

    // Request method and URL specified in the annotation

    @GET("svc/search/v2/articlesearch.json")
    Call<SearchApiResponse> getArticles(@Query("q") String query,@Query("fq") String filteredQuery, @Query("sort") String sortBy, @Query("page") String page);
}
