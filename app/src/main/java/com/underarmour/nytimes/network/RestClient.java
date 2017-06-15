package com.underarmour.nytimes.network;


import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public static final String TAG = RestClient.class.getSimpleName();

    // Trailing slash is needed
    public static final String API_BASE_URL = "https://api.nytimes.com/";
    public static final String API_KEY = "api-key";
    public static final String API_KEY_VALUE = "d31fe793adf546658bd67e2b6a7fd11a";

    public static ISearchApiEndpoint createService() {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                Request originalRequest = chain.request();
                HttpUrl originalHttpUrl = originalRequest.url();
                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(API_KEY, API_KEY_VALUE).build();

                Request.Builder builder = originalRequest.newBuilder().url(url);

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ISearchApiEndpoint apiService = retrofit.create(ISearchApiEndpoint.class);
        return apiService;

    }
}
