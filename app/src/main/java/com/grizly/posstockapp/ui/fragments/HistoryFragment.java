package com.grizly.posstockapp.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.grizly.posstockapp.R;
import com.grizly.posstockapp.models.Order;
import com.grizly.posstockapp.models.SpinnerItem;
import com.grizly.posstockapp.utils.Config;
import com.grizly.posstockapp.utils.Methods;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by tam on 8/22/2016.
 */
public class HistoryFragment extends Fragment {

    View view;

    public ArrayList<Order> orderList = new ArrayList<Order>();
    public ArrayList<Order> useorderList = new ArrayList<Order>();

    public ProductsAdapter productAdapter;
    public ListView productLV;

    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(Config.ACTION_NEW_ORDER)) {
                orderList.clear();
                useorderList.clear();
                useorderList = Order.getPrefArraylist(Config.PREF_KEY_LIST_ORDERS, getActivity());
                for(int i = 0 ;  i < useorderList.size(); i++){
                    orderList.add((Order) ((useorderList).get(i)));
                }
                productAdapter.notifyDataSetChanged();
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
        orderList.clear();
        useorderList.clear();
        useorderList = Order.getPrefArraylist(Config.PREF_KEY_LIST_ORDERS, getActivity());
        for(int i = 0 ;  i < useorderList.size(); i++){
            orderList.add((Order) ((useorderList).get(i)));
        }
        productAdapter.notifyDataSetChanged();

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_NEW_ORDER);
        getActivity().registerReceiver(mIntentReceiver, filter);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history_fragment, container, false);

        productLV = (ListView) view.findViewById(R.id.list);
        productAdapter = new ProductsAdapter(getActivity(), orderList);
        try {
            productLV.setAdapter(productAdapter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return view;
    }

    public class ProductsAdapter extends BaseAdapter {

        ArrayList<Order> orderList;
        ViewHolder holder;
        Context context;

        public ProductsAdapter(Context context, ArrayList<Order> orderList) {
            this.orderList = orderList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return orderList.size();
        }

        @Override
        public Order getItem(int position) {
            return orderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {

            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_history, null);

                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

                holder.layout.get().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        showDeleteDialog(i);
                        return false;
                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();

            }

            holder.customer.get().setText(getItem(i).getCustomer());
            holder.product.get().setText(getItem(i).getQuantity() + " " + getItem(i).getProduct());
            holder.date.get().setText(getItem(i).getDate());

            return convertView;
        }
    }

    public class ViewHolder {

        public WeakReference<TextView> customer;
        public WeakReference<TextView> product;
        public WeakReference<TextView> date;
        public WeakReference<LinearLayout> layout;

        public ViewHolder(View view) {
            layout = new WeakReference<LinearLayout>((LinearLayout) view.findViewById(R.id.item_customer_row_layout));
            customer = new WeakReference<TextView>((TextView) view.findViewById(R.id.customerName));
            product = new WeakReference<TextView>((TextView) view.findViewById(R.id.product_info));
            date = new WeakReference<TextView>((TextView) view.findViewById(R.id.order_date));
        }
    }

    void showDeleteDialog(final int pos) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setMessage("Are you sure you want to delete this product ?")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        orderList.remove(pos);
                        Methods.savePrefObject(orderList, Config.PREF_KEY_LIST_ORDERS, getActivity());

                        productAdapter.notifyDataSetChanged();

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
}
