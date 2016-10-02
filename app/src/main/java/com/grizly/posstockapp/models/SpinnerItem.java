package com.grizly.posstockapp.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grizly.posstockapp.utils.Config;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SpinnerItem {

    public int id;
    public String name;

    public SpinnerItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    static public ArrayList getPrefArraylist(String Key, Context context) {
        ArrayList<SpinnerItem> list = null;
        try {
            SharedPreferences settings = context.getSharedPreferences(Config.PREFS_NAME, 0);
            Gson gson = new Gson();
            String json = settings.getString(Key, null);
            Type type = new TypeToken<ArrayList<SpinnerItem>>() {
            }.getType();
            if (json != null)
                list = gson.fromJson(json, type);
        } catch (Exception ex) {
        }
        return list;
    }
}
