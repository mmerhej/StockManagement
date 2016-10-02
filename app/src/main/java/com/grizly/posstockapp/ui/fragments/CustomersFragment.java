package com.grizly.posstockapp.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.grizly.posstockapp.R;
import com.grizly.posstockapp.models.Customer;
import com.grizly.posstockapp.models.SpinnerItem;
import com.grizly.posstockapp.ui.AddCustomerActivity;
import com.grizly.posstockapp.utils.Config;
import com.grizly.posstockapp.utils.Methods;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by tam on 8/22/2016.
 */
public class CustomersFragment extends Fragment {

    View view;
    public ArrayList<Customer> customerList = new ArrayList<Customer>();
    public ArrayList<Customer> useCustomerList = new ArrayList<Customer>();
    public CustomerAdapter customerAdapter;
    public ListView customerLV;
    public RelativeLayout addCustomerLayout, emptyLayout;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        customerList.clear();
        useCustomerList.clear();
        useCustomerList = Customer.getPrefArraylist(Config.PREF_KEY_LIST_CUSTOMERS, getActivity());
        for (int i = 0; i < useCustomerList.size(); i++) {
            customerList.add((Customer) ((useCustomerList).get(i)));
        }

        customerAdapter.notifyDataSetChanged();

        if (customerList.size() < 1)
            emptyLayout.setVisibility(View.VISIBLE);
        else
            emptyLayout.setVisibility(View.GONE);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static CustomersFragment newInstance() {
        CustomersFragment fragment = new CustomersFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_fragment, container, false);

        customerLV = (ListView) view.findViewById(R.id.list);
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_layout);
        addCustomerLayout = (RelativeLayout) view.findViewById(R.id.add_layout);
        customerAdapter = new CustomerAdapter(getActivity(), customerList);
        try {
            customerLV.setAdapter(customerAdapter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        addCustomerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCustomerActivity.class);
                startActivity(intent);
            }
        });

        if (customerList.size() < 1)
            emptyLayout.setVisibility(View.VISIBLE);
        else
            emptyLayout.setVisibility(View.GONE);

        return view;
    }

    public class CustomerAdapter extends BaseAdapter {

        ArrayList<Customer> customerList;
        ViewHolder holder;
        Context context;

        public CustomerAdapter(Context context, ArrayList<Customer> customerList) {
            this.customerList = customerList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return customerList.size();
        }

        @Override
        public Customer getItem(int position) {
            return customerList.get(position);
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
                convertView = inflater.inflate(R.layout.row_customer, null);

                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();

            }

            holder.customerName.get().setText(getItem(i).getFullName());
            holder.customerPn.get().setText(getItem(i).getPhoneNumber());
            holder.leftIcon.get().setText(getItem(i).getFullName().substring(0, 1).toUpperCase());

            if (getItem(i).getImageUrl().length() > 2) {
                File image = new File(getItem(i).getImageUrl());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

                holder.prod_img.get().setImageBitmap(bitmap);
            }

            holder.customer_row.get().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDeleteDialog(i);
                    return false;
                }
            });

            switch (i % 6) {
                case 0:
                    holder.circularBackgroud.get().setBackground(context.getResources().getDrawable(R.drawable.circular_background_green));
                    break;

                case 1:
                    holder.circularBackgroud.get().setBackground(context.getResources().getDrawable(R.drawable.circular_background_blue));
                    break;

                case 2:
                    holder.circularBackgroud.get().setBackground(context.getResources().getDrawable(R.drawable.circular_background_purple));
                    break;

                case 3:
                    holder.circularBackgroud.get().setBackground(context.getResources().getDrawable(R.drawable.circular_background_yellow));
                    break;

                case 4:
                    holder.circularBackgroud.get().setBackground(context.getResources().getDrawable(R.drawable.circular_background_grey));
                    break;

                case 5:
                    holder.circularBackgroud.get().setBackground(context.getResources().getDrawable(R.drawable.circular_background_red));
                    break;

                default:
                    break;
            }

            return convertView;
        }
    }

    public class ViewHolder {

        public WeakReference<TextView> customerName;
        public WeakReference<TextView> customerPn;
        public WeakReference<TextView> leftIcon;
        public WeakReference<View> circularBackgroud;
        public WeakReference<SimpleDraweeView> prod_img;
        public WeakReference<RelativeLayout> customer_row;

        public ViewHolder(View view) {
            customer_row = new WeakReference<RelativeLayout>((RelativeLayout) view.findViewById(R.id.customer_row));
            customerName = new WeakReference<TextView>((TextView) view.findViewById(R.id.name));
            customerPn = new WeakReference<TextView>((TextView) view.findViewById(R.id.phone));
            leftIcon = new WeakReference<TextView>((TextView) view.findViewById(R.id.leftIcon));
            circularBackgroud = new WeakReference<View>((View) view.findViewById(R.id.circularView));
            prod_img = new WeakReference<SimpleDraweeView>((SimpleDraweeView) view.findViewById(R.id.prod_img));

        }
    }

    void showDeleteDialog(final int pos) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setMessage("What do you want to do with this customer ?")
                .setCancelable(true)
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        customerList.remove(pos);
                        Methods.savePrefObject(customerList, Config.PREF_KEY_LIST_CUSTOMERS, getActivity());

                        ArrayList<SpinnerItem> spinnerItemList = SpinnerItem.getPrefArraylist(Config.PREF_KEY_LIST_CUSTOMER_SPINNER, getActivity());
                        spinnerItemList.remove(pos);
                        Methods.savePrefObject(spinnerItemList, Config.PREF_KEY_LIST_CUSTOMER_SPINNER, getActivity());

                        getActivity().sendBroadcast(new Intent(Config.ACTION_NEW_CUSTOMER));
                        customerAdapter.notifyDataSetChanged();

                    }
                })
                .setNegativeButton("edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editProductDialog(pos);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    Dialog dialog;
    AppCompatEditText product_et, quantity_et;
    AppCompatButton cancel, edit;

    public void editProductDialog(final int pos) {

        dialog = new Dialog(getActivity(), R.style.ThemeDialogCustom);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_product, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);

        product_et = (AppCompatEditText) view.findViewById(R.id.product_et);
        quantity_et = (AppCompatEditText) view.findViewById(R.id.quantity_et);
        cancel = (AppCompatButton) view.findViewById(R.id.cancel);
        edit = (AppCompatButton) view.findViewById(R.id.edit);

        product_et.setText(customerList.get(pos).getFullName());
        quantity_et.setText(customerList.get(pos).getPhoneNumber());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerList.get(pos).setPhoneNumber(quantity_et.getText().toString());
                customerList.get(pos).setFullName(product_et.getText().toString());
                Methods.savePrefObject(customerList, Config.PREF_KEY_LIST_CUSTOMERS, getActivity());

                ArrayList<SpinnerItem> spinnerItemList = SpinnerItem.getPrefArraylist(Config.PREF_KEY_LIST_CUSTOMER_SPINNER, getActivity());
                spinnerItemList.get(pos).name = customerList.get(pos).getFullName();

                Methods.savePrefObject(spinnerItemList, Config.PREF_KEY_LIST_CUSTOMER_SPINNER, getActivity());

                getActivity().sendBroadcast(new Intent(Config.ACTION_NEW_CUSTOMER));

                onResume();

                dialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
