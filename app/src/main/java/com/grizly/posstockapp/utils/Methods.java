package com.grizly.posstockapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


@SuppressLint("NewApi")
public class Methods {

    static public String getPref(Context c, String STOTREDkey) {
        String mobileNum = "";
        try {
            SharedPreferences settings = c.getSharedPreferences(Config.PREFS_NAME, 0);
            mobileNum = settings.getString(STOTREDkey, "");
        } catch (Exception ex) {
        }
        return mobileNum;
    }


    static public Object getPrefObject(String Key, Context context, Class<?> myClass) {
        Object value = null;
        try {
            SharedPreferences settings = context.getSharedPreferences(Config.PREFS_NAME, 0);
            Gson gson = new Gson();
            String json = settings.getString(Key, "");
            if (json.length() > 0)
                value = gson.fromJson(json, myClass);
        } catch (Exception ex) {
        }
        return value;
    }

    static public boolean savePrefObject(Object obj, String Key, Context context) {
        SharedPreferences settings = context.getSharedPreferences(Config.PREFS_NAME, 0);
        SharedPreferences.Editor prefsEditor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        prefsEditor.putString(Key, json);
        prefsEditor.commit();
        return true;
    }

    static public boolean clearPref(Context c , String key) {
        SharedPreferences settings = c.getSharedPreferences(Config.PREFS_NAME, 0);
        //SharedPreferences.Editor editor = settings.edit();
        if (settings != null)
            settings.edit().remove(key).apply();
        //editor.clear();
        //editor.commit();
        return true;
    }

    static public boolean savePre(Context c, String value, String key) {
        SharedPreferences settings = c.getSharedPreferences(Config.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }
}