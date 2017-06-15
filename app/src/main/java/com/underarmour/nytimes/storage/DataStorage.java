package com.underarmour.nytimes.storage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.underarmour.nytimes.app.NYTimesSearchApp;

/**
 * This class is used to store and access data from sharedPref
 */
public class DataStorage {

    private SharedPreferences mSettings;
    private static DataStorage dataStorage=null;

    public static DataStorage getInstance(){

        if(dataStorage==null){
            dataStorage = new DataStorage();
        }
        return dataStorage;
    }

    public DataStorage(){
        mSettings = PreferenceManager.getDefaultSharedPreferences(NYTimesSearchApp.sContext);
    }

    public void saveData(String key, String value){

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getData(String key){

        String defaultValue="";
        String value = mSettings.getString(key,defaultValue);
        return value;
    }

    public void saveObject(String key,Object objData){
        SharedPreferences.Editor prefsEditor = mSettings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(objData);
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public Object getObjectData(String key){
        Gson gson = new Gson();
        String json = mSettings.getString(key, "");
        Object obj = gson.fromJson(json, Object.class);
        return obj;
    }
}