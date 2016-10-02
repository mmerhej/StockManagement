package com.grizly.posstockapp.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grizly.posstockapp.utils.Config;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Product {

    public String inStock;
    public String barcode;
    public String imageUrl;
    public String product;


    public Product(String inStock, String imageUrl, String barcode, String product) {
        this.inStock = inStock;
        this.imageUrl = imageUrl;
        this.barcode = barcode;
        this.product = product;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProduct() {
        return product;
    }

    public String getInStock() {
        return inStock;

    }

    static public ArrayList getPrefArraylist(String Key, Context context) {
        ArrayList<Product> list = null;
        try {
            SharedPreferences settings = context.getSharedPreferences(Config.PREFS_NAME, 0);
            Gson gson = new Gson();
            String json = settings.getString(Key, null);
            Type type = new TypeToken<ArrayList<Product>>() {
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
