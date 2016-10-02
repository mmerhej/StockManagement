package com.grizly.posstockapp.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grizly.posstockapp.utils.Config;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Customer {

    String fullName;
    String imageUrl;
    String role;
    String phoneNumber;

    public Customer(String fullName, String imageUrl, String phoneNumber, String role) {
        this.fullName = fullName;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRole() {
        return role;
    }

    static public ArrayList getPrefArraylist(String Key, Context context) {
        ArrayList<Customer> list = null;
        try {
            SharedPreferences settings = context.getSharedPreferences(Config.PREFS_NAME, 0);
            Gson gson = new Gson();
            String json = settings.getString(Key, null);
            Type type = new TypeToken<ArrayList<Customer>>() {
            }.getType();
            if (json != null)
                list = gson.fromJson(json, type);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    static public String getPrefString(String Key, Context context) {
        String json = null;
        try {
            SharedPreferences settings = context.getSharedPreferences(Config.PREFS_NAME, 0);

            json = settings.getString(Key, null);
            Log.v("test=====",json);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return json;
    }
}
