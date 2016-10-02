package com.grizly.posstockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.grizly.posstockapp.models.Customer;
import com.grizly.posstockapp.models.Order;
import com.grizly.posstockapp.models.Product;
import com.grizly.posstockapp.models.SpinnerItem;
import com.grizly.posstockapp.models.User;
import com.grizly.posstockapp.ui.StockActivity;
import com.grizly.posstockapp.ui.registration.LoginActivity;
import com.grizly.posstockapp.ui.registration.RegisterActivity;
import com.grizly.posstockapp.utils.Config;
import com.grizly.posstockapp.utils.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {

    ArrayList<Customer> customerList = new ArrayList<>();
    ArrayList<Order> orderList = new ArrayList<>();
    ArrayList<Product> productList = new ArrayList<>();
    ArrayList<User> userList = new ArrayList<>();
    ArrayList<SpinnerItem> spinnerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String url = "http://192.168.1.73:8080/RESTfulProject/REST/Login/Get";

        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            Log.d("Response", response.toString());
                            JSONArray json;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                json = new JSONArray(response.toString());
                                for(int i=0;i<json.length();i++){

                                    JSONObject e = json.getJSONObject(i);

                                    Log.d("user",e.getString("username"));
                                    Log.d("pass",e.getString("password"));
                                    Methods.clearPref(SplashActivity.this,Config.PREF_KEY_USERNAME);
                                    Methods.clearPref(SplashActivity.this,Config.PREF_KEY_PASSWORD);
                                    Methods.clearPref(SplashActivity.this,Config.PREF_KEY_REGISTERED);
                                    Methods.savePre(SplashActivity.this, e.getString("username").trim(), Config.PREF_KEY_USERNAME);
                                    Methods.savePre(SplashActivity.this, e.getString("password").trim(), Config.PREF_KEY_PASSWORD);
                                    Methods.savePre(SplashActivity.this, "1", Config.PREF_KEY_REGISTERED);

                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        Volley.newRequestQueue(this).add(jsonRequest);

        String urlCustomers = "http://192.168.1.73:8080/RESTfulProject/REST/Customers/Get";

        JsonArrayRequest jsonRequest1 = new JsonArrayRequest
                (Request.Method.GET, urlCustomers, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            Log.d("Response", response.toString());
                            JSONArray json;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                json = new JSONArray(response.toString());
                                ArrayList<Customer> customerList1 = new ArrayList<>();
                                ArrayList<SpinnerItem> spinnerItemList1 = new ArrayList<>();
                                for(int i=0;i<json.length();i++){

                                    JSONObject e = json.getJSONObject(i);

                                    Log.d("fullName",e.getString("fullName"));
                                    Log.d("phoneNumber",e.getString("phoneNumber"));
                                    Log.d("role",e.getString("role"));

                                    try {


                                        spinnerItemList1.add(new SpinnerItem(Integer.parseInt(
                                                e.getString("phoneNumber")),
                                                e.getString("fullName")));

                                        customerList1.add(new Customer( e.getString("fullName"),
                                                "",
                                                e.getString("phoneNumber"),
                                                e.getString("role")));

                                    } catch (NumberFormatException | JSONException e1) {
                                        e1.printStackTrace();
                                    }


                                }
                                Methods.clearPref(SplashActivity.this, Config.PREF_KEY_LIST_CUSTOMERS);
                                Methods.clearPref(SplashActivity.this, Config.PREF_KEY_LIST_CUSTOMER_SPINNER);
                                Methods.savePrefObject(customerList1, Config
                                        .PREF_KEY_LIST_CUSTOMERS, SplashActivity.this);
                                Methods.savePrefObject(spinnerItemList1, Config
                                        .PREF_KEY_LIST_CUSTOMER_SPINNER, SplashActivity.this);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest1);

        String urlOrders = "http://192.168.1.73:8080/RESTfulProject/REST/Orders/Get";

        JsonArrayRequest jsonRequest2 = new JsonArrayRequest
                (Request.Method.GET, urlOrders, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            Log.d("Response", response.toString());
                            JSONArray json;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                json = new JSONArray(response.toString());
                                ArrayList<Order> orderList1 = new ArrayList<>();
                                for(int i=0;i<json.length();i++){

                                    JSONObject e = json.getJSONObject(i);

                                    Log.d("product",e.getString("product"));
                                    Log.d("customer",e.getString("customer"));
                                    Log.d("type",e.getString("type"));

                                    try {

                                        orderList1.add(new Order(e.getString("product"),
                                                e.getString("customer"),
                                                e.getString("type"), e.getString("quantity"),
                                                DateFormat.getDateTimeInstance().format(new Date())));

                                    } catch (NumberFormatException | JSONException e1) {
                                        e1.printStackTrace();
                                    }

                                }
                                Methods.clearPref(SplashActivity.this, Config.PREF_KEY_LIST_ORDERS);
                                Methods.savePrefObject(orderList1, Config.PREF_KEY_LIST_ORDERS,SplashActivity.this);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest2);

        String urlProducts = "http://192.168.1.73:8080/RESTfulProject/REST/Products/Get";

        JsonArrayRequest jsonRequestProducts = new JsonArrayRequest
                (Request.Method.GET, urlProducts, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            Log.d("Response", response.toString());
                            JSONArray json;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                json = new JSONArray(response.toString());
                                ArrayList<Product> productList1 = new ArrayList<>();
                                ArrayList<SpinnerItem> spinnerItemList1 = new ArrayList<>();
                                for(int i=0;i<json.length();i++){

                                    JSONObject e = json.getJSONObject(i);

                                    Log.d("barcode",e.getString("barcode"));
                                    Log.d("product",e.getString("product"));
                                    Log.d("inStock",e.getString("inStock"));

                                    try {


                                        spinnerItemList1.add(new SpinnerItem(
                                                i++,
                                                e.getString("product")));

                                        productList1.add(new Product( e.getString("inStock"),
                                                "",
                                                e.getString("barcode"),
                                                e.getString("product")));

                                    } catch (NumberFormatException | JSONException e1) {
                                        e1.printStackTrace();
                                    }

                                }
                                Methods.clearPref(SplashActivity.this, Config.PREF_KEY_LIST_PRODUCTS);
                                Methods.clearPref(SplashActivity.this, Config.PREF_KEY_LIST_SPINNER);
                                Methods.savePrefObject(productList1, Config
                                        .PREF_KEY_LIST_PRODUCTS, SplashActivity.this);
                                Methods.savePrefObject(spinnerItemList1, Config
                                        .PREF_KEY_LIST_SPINNER, SplashActivity.this);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequestProducts);



        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                if (Methods.getPref(SplashActivity.this, Config.PREF_KEY_REGISTERED).equals("1")) {
                    if (Methods.getPref(SplashActivity.this, Config.PREF_KEY_REMEMBER_ME).equals("1")) {
                        Intent intent = new Intent(SplashActivity.this, StockActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Methods.savePrefObject(customerList, Config.PREF_KEY_LIST_CUSTOMERS, SplashActivity.this);
                    Methods.savePrefObject(orderList, Config.PREF_KEY_LIST_ORDERS, SplashActivity.this);
                    Methods.savePrefObject(productList, Config.PREF_KEY_LIST_PRODUCTS, SplashActivity.this);
                    Methods.savePrefObject(userList, Config.PREF_KEY_LIST_USERS, SplashActivity.this);
                    Methods.savePrefObject(spinnerList, Config.PREF_KEY_LIST_SPINNER, SplashActivity.this);
                    Methods.savePrefObject(spinnerList, Config.PREF_KEY_LIST_CUSTOMER_SPINNER, SplashActivity.this);

                    Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}
