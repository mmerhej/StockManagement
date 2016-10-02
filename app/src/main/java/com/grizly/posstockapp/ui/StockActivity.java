package com.grizly.posstockapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flyco.tablayout.SlidingTabLayout;
import com.grizly.posstockapp.R;
import com.grizly.posstockapp.models.Customer;
import com.grizly.posstockapp.models.Order;
import com.grizly.posstockapp.models.Product;
import com.grizly.posstockapp.models.User;
import com.grizly.posstockapp.ui.fragments.CustomersFragment;
import com.grizly.posstockapp.ui.fragments.HistoryFragment;
import com.grizly.posstockapp.ui.fragments.OrderFragment;
import com.grizly.posstockapp.ui.fragments.ProductsFragment;
import com.grizly.posstockapp.ui.registration.LoginActivity;
import com.grizly.posstockapp.utils.Config;
import com.grizly.posstockapp.utils.Methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockActivity extends AppCompatActivity {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private ViewPager mViewPager;
    private SlidingTabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppCompatTextView title = (AppCompatTextView) toolbar.findViewById(R.id.title);
        title.setText(getString(R.string.estock));
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.stock_container);
        setupViewPager(mViewPager);

        mTabLayout = (SlidingTabLayout) findViewById(R.id.stock_tabs);
        mTabLayout.setViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(ProductsFragment.newInstance(), getString(R.string.products));
        adapter.addFragment(CustomersFragment.newInstance(), getString(R.string.customers));
        adapter.addFragment(OrderFragment.newInstance(), getString(R.string.orders));
        adapter.addFragment(HistoryFragment.newInstance(), getString(R.string.history));

        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout_action:
                showLogoutDialog();
                return true;
            case R.id.save_action:
                try {
                    //addUsers();
                    addProducts();
                    addCutomers();
                    addOrders();

                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

void showLogoutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("Are you sure you want to Logout ?")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Methods.savePre(StockActivity.this, "0", Config.PREF_KEY_REMEMBER_ME);
                        Intent intent = new Intent(StockActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    void addCutomers() {
        final String customerList = Customer.getPrefString
                (Config.PREF_KEY_LIST_CUSTOMERS, StockActivity.this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.73:8080/RESTfulProject/REST/Customers/Post";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT)
                            //.show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("param", customerList);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(strRequest);

    }
    void addOrders() {
        final String orderList = Order.getPrefString
                (Config.PREF_KEY_LIST_ORDERS, StockActivity.this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.73:8080/RESTfulProject/REST/Orders/Post";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT)
                          //  .show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("param", orderList);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(strRequest);

    }
    void addUsers() {
        final String userList = User.getPrefString
                (Config.PREF_KEY_LIST_USERS, StockActivity.this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.73:8080/RESTfulProject/REST/Login/Post";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                       // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT)
                         //   .show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("param", userList);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(strRequest);

    }
    void addProducts() {
        final String productList = Product.getPrefString
                (Config.PREF_KEY_LIST_PRODUCTS, StockActivity.this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.73:8080/RESTfulProject/REST/Products/Post";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT)
                         //   .show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("param", productList);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(strRequest);

    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
//            return null;
        }
    }

}
