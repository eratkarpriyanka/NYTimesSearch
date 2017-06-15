package com.underarmour.nytimes.app;


import android.app.Application;
import android.content.Context;

public class NYTimesSearchApp extends Application{

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
