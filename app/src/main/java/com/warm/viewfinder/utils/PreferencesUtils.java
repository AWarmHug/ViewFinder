package com.warm.viewfinder.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.warm.viewfinder.MyApp;

public class PreferencesUtils {
    public static final String NAME = "ViewFinder";

    public static void setStringPreferences(String preference, String key,
                                            String value) {
        SharedPreferences sharedPreferences = MyApp.getInstance()
                .getApplicationContext()
                .getSharedPreferences(preference, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringPreferences(String preference, String key,
                                              String defaultValue) {
        SharedPreferences sharedPreferences = MyApp.getInstance()
                .getApplicationContext()
                .getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }


    public static void setIntPreferences(String preference, String key,
                                         int value) {
        SharedPreferences sharedPreferences = MyApp.getInstance()
                .getApplicationContext()
                .getSharedPreferences(preference, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getIntPreferences(String preference, String key,
                                        int defaultValue) {
        SharedPreferences sharedPreferences = MyApp.getInstance()
                .getApplicationContext()
                .getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }


    public static void setBooleanPreferences(String key,
                                             boolean value) {
        SharedPreferences sharedPreferences = MyApp.getInstance()
                .getApplicationContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBooleanPreferences(String key,
                                                boolean defaultValue) {

        SharedPreferences sharedPreferences = MyApp.getInstance()
                .getApplicationContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }


}
