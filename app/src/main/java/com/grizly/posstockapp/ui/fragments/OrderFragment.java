package com.grizly.posstockapp.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.grizly.posstockapp.R;
import com.grizly.posstockapp.models.Order;
import com.grizly.posstockapp.models.Product;
import com.grizly.posstockapp.models.SpinnerItem;
import com.grizly.posstockapp.utils.Config;
import com.grizly.posstockapp.utils.Methods;
import com.grizly.posstockapp.utils.SnackS;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tam on 8/22/2016.
 */
public class OrderFragment extends Fragment {

    View view;

    public ArrayList<Order> orderList = new ArrayList<Order>();
    public ArrayList<Order> useorderList = new ArrayList<Order>();

    SpinnerAdapter orderAdapter, customerAdapter;
    Spinner spinner, customerSpinner;

    AppCompatEditText type;
    EditText quantity_et, type_et;
    AppCompatButton btn;
    public static ArrayList<SpinnerItem> productlist = new ArrayList();
    public static ArrayList<SpinnerItem> customerlist = new ArrayList();

    public static ArrayList<SpinnerItem> ddd = new ArrayList();


    String product, customer;

    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(Config.ACTION_NEW_PRODUCT)) {
                productlist = SpinnerItem.getPrefArraylist(Config.PREF_KEY_LIST_SPINNER, getActivity());

                orderAdapter = new SpinnerAdapter();
                orderAdapter.addItems(productlist);
                spinner = (Spinner) view.findViewById(R.id.product_spinner);
                spinner.setAdapter(orderAdapter);
            } else if (action.equals(Config.ACTION_NEW_CUSTOMER)) {
                customerlist = SpinnerItem.getPrefArraylist(Config.PREF_KEY_LIST_CUSTOMER_SPINNER, getActivity());

                customerAdapter = new SpinnerAdapter();
                customerAdapter.addItems(customerlist);
                customerSpinner = (Spinner) view.findViewById(R.id.customer_spinner);
                customerSpinner.setAdapter(customerAdapter);
            }
        }
    };

    @Override
    public void onDestroy() {
        if (mIntentReceiver != null)
            getActivity().unregisterReceiver(mIntentReceiver);
        super.onDestroy();
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_NEW_PRODUCT);
        filter.addAction(Config.ACTION_NEW_CUSTOMER);
        getActivity().registerReceiver(mIntentReceiver, filter);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.orders_fragment, container, false);


        type = (AppCompatEditText) view.findViewById(R.id.type_et);
        btn = (AppCompatButton) view.findViewById(R.id.register);
        quantity_et = (EditText) view.findViewById(R.id.quantity_et);
        type_et = (EditText) view.findViewById(R.id.type_et);

        productlist = SpinnerItem.getPrefArraylist(Config.PREF_KEY_LIST_SPINNER, getActivity());
        customerlist = SpinnerItem.getPrefArraylist(Config.PREF_KEY_LIST_CUSTOMER_SPINNER, getActivity());

        orderAdapter = new SpinnerAdapter();
        orderAdapter.addItems(productlist);
        spinner = (Spinner) view.findViewById(R.id.product_spinner);
        spinner.setAdapter(orderAdapter);

        customerAdapter = new SpinnerAdapter();
        customerAdapter.addItems(customerlist);
        customerSpinner = (Spinner) view.findViewById(R.id.customer_spinner);
        customerSpinner.setAdapter(customerAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customerSpinner.getSelectedItem() == null
                        || spinner.getSelectedItem() == null
                        || quantity_et.getText().toString().length() < 1
                        || type.getText().toString().trim().length() < 1) {

//                    Toast.makeText(getActivity(), "Missing Fields", Toast.LENGTH_SHORT).show();
                    SnackS.snackAlert(getActivity(), "You must fill all fields.");
                } else {
                    ArrayList<Product> productList = Product.getPrefArraylist(Config.PREF_KEY_LIST_PRODUCTS, getActivity());

                    try {

                        if (Integer.parseInt(productList.get(spinner.getSelectedItemPosition()).inStock)
                                < Integer.parseInt(quantity_et.getText().toString())) {

                            SnackS.snackAlert(getActivity(), "Quantity not available");
                        } else {

                            ArrayList<Order> orderList = Order.getPrefArraylist(Config.PREF_KEY_LIST_ORDERS, getActivity());

                            orderList.add(new Order(customerlist.get(customerSpinner.getSelectedItemPosition()).name,
                                    productlist.get(customerSpinner.getSelectedItemPosition()).name,
                                    type.getText().toString(), quantity_et.getText().toString(),
                                    DateFormat.getDateTimeInstance().format(new Date())));
                            Methods.savePrefObject(orderList, Config.PREF_KEY_LIST_ORDERS, getActivity());

                            if ((Integer.parseInt(productList.get(spinner.getSelectedItemPosition()).inStock) -
                                    Integer.parseInt(quantity_et.getText().toString())) == 0) {

                                productList.remove(spinner.getSelectedItemPosition());
                                productlist.remove(spinner.getSelectedItemPosition());

                                orderAdapter = new SpinnerAdapter();
                                orderAdapter.addItems(productlist);
                                spinner = (Spinner) view.findViewById(R.id.product_spinner);
                                spinner.setAdapter(orderAdapter);


                            } else {
                                productList.get(spinner.getSelectedItemPosition()).setInStock(Integer.toString(
                                        Integer.parseInt(productList.get(spinner.getSelectedItemPosition()).inStock) -
                                                Integer.parseInt(quantity_et.getText().toString()))
                                );
                            }

                            Methods.savePrefObject(productList, Config.PREF_KEY_LIST_PRODUCTS, getActivity());

                            getActivity().sendBroadcast(new Intent(Config.ACTION_NEW_ORDER));

                            quantity_et.setText("");
                            type_et.setText("");
                            SnackS.snackAlert(getActivity(), "Order Created");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        orderAdapter.notifyDataSetChanged();

        return view;
    }

    public class SpinnerAdapter extends BaseAdapter {
        private List<SpinnerItem> mItems = new ArrayList<SpinnerItem>();
        LayoutInflater mInflater;

        public void clear() {
            mItems.clear();
        }

        public void addItem(SpinnerItem item) {
            mItems.add(item);
        }

        public void addItems(List<SpinnerItem> item) {
            mItems.addAll(item);
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public SpinnerItem getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getDropDownView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
                mInflater = LayoutInflater.from(getActivity());
                view = mInflater.inflate(R.layout.spinner_item_dropdown, parent, false);
                view.setTag("DROPDOWN");
            }

            TextView textView = (TextView) view.findViewById(R.id.item_text);
            textView.setText(getTitle(position));

            return view;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
                mInflater = LayoutInflater.from(getActivity());
                view = mInflater.inflate(R.layout.
                        spinner_item, parent, false);
                view.setTag("NON_DROPDOWN");
            }
            TextView textView = (TextView) view.findViewById(R.id.item_text);
            textView.setText(getTitle(position));
            return view;
        }

        private String getTitle(int position) {
            return position >= 0 && position < mItems.size() ? mItems.get(position).name : "";
        }
    }

}
