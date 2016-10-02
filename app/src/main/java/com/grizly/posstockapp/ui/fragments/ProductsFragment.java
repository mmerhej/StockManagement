package com.grizly.posstockapp.ui.fragments;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.grizly.posstockapp.models.Product;
import com.grizly.posstockapp.models.SpinnerItem;
import com.grizly.posstockapp.ui.AddProductActivity;
import com.grizly.posstockapp.ui.registration.LoginActivity;
import com.grizly.posstockapp.utils.Config;
import com.grizly.posstockapp.utils.Methods;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by tam on 8/22/2016.
 */
public class ProductsFragment extends Fragment {

    View view;

    public ArrayList<Product> productList = new ArrayList<Product>();
    public ArrayList<Product> useproductList = new ArrayList<Product>();

    public ProductsAdapter productAdapter;
    public ListView productLV;

    public RelativeLayout addProductLayout, emptyLayout;

    @Override
    public void onDestroy() {

        if (mIntentReceiver != null)
            getActivity().unregisterReceiver(mIntentReceiver);

        super.onDestroy();
    }

    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(Config.ACTION_NEW_ORDER)) {
                productList.clear();
                useproductList.clear();
                useproductList = Product.getPrefArraylist(Config.PREF_KEY_LIST_PRODUCTS, getActivity());
                for (int i = 0; i < useproductList.size(); i++) {
                    productList.add((Product) ((useproductList).get(i)));
                }
                productAdapter.notifyDataSetChanged();

                if (productList.size() < 1)
                    emptyLayout.setVisibility(View.VISIBLE);
                else
                    emptyLayout.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onResume() {
        productList.clear();
        useproductList.clear();
        useproductList = Product.getPrefArraylist(Config.PREF_KEY_LIST_PRODUCTS, getActivity());
        for (int i = 0; i < useproductList.size(); i++) {
            productList.add((Product) ((useproductList).get(i)));
        }
        productAdapter.notifyDataSetChanged();

        if (productList.size() < 1)
            emptyLayout.setVisibility(View.VISIBLE);
        else
            emptyLayout.setVisibility(View.GONE);

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static ProductsFragment newInstance() {
        ProductsFragment fragment = new ProductsFragment();
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
        view = inflater.inflate(R.layout.products_fragment, container, false);

        productLV = (ListView) view.findViewById(R.id.list);
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_layout);
        addProductLayout = (RelativeLayout) view.findViewById(R.id.add_layout);

        productAdapter = new ProductsAdapter(getActivity(), productList);
        try {
            productLV.setAdapter(productAdapter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        addProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                startActivity(intent);
            }
        });

        if (productList.size() < 1)
            emptyLayout.setVisibility(View.VISIBLE);
        else
            emptyLayout.setVisibility(View.GONE);


        return view;
    }

    public class ProductsAdapter extends BaseAdapter {

        ArrayList<Product> productList;
        ViewHolder holder;
        Context context;

        public ProductsAdapter(Context context, ArrayList<Product> productList) {
            this.productList = productList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Product getItem(int position) {
            return productList.get(position);
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
                convertView = inflater.inflate(R.layout.row_product, null);

                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();

            }

            holder.productName.get().setText(getItem(i).getProduct());
            holder.productInStock.get().setText(getItem(i).getInStock());
            holder.leftIcon.get().setText(getItem(i).getProduct().substring(0, 1).toUpperCase());

            if (getItem(i).getImageUrl().length() > 2) {
                File image = new File(getItem(i).getImageUrl());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

                holder.prod_img.get().setImageBitmap(bitmap);
            }

            holder.item_product_row_layout.get().setOnLongClickListener(new View.OnLongClickListener() {
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

        public WeakReference<TextView> productName;
        public WeakReference<TextView> productInStock;
        public WeakReference<TextView> leftIcon;
        public WeakReference<View> circularBackgroud;
        public WeakReference<SimpleDraweeView> prod_img;
        public WeakReference<RelativeLayout> item_product_row_layout;

        public ViewHolder(View view) {
            item_product_row_layout = new WeakReference<RelativeLayout>((RelativeLayout) view.findViewById(R.id.item_product_row_layout));
            productName = new WeakReference<TextView>((TextView) view.findViewById(R.id.name));
            productInStock = new WeakReference<TextView>((TextView) view.findViewById(R.id.inStock));
            leftIcon = new WeakReference<TextView>((TextView) view.findViewById(R.id.leftIcon));
            circularBackgroud = new WeakReference<View>((View) view.findViewById(R.id.circularView));
            prod_img = new WeakReference<SimpleDraweeView>((SimpleDraweeView) view.findViewById(R.id.prod_img));
        }
    }

    void showDeleteDialog(final int pos) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setMessage("What do you want to do with this product ?")
                .setCancelable(true)
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        productList.remove(pos);
                        Methods.savePrefObject(productList, Config.PREF_KEY_LIST_PRODUCTS, getActivity());

                        ArrayList<SpinnerItem> spinnerItemList = SpinnerItem.getPrefArraylist(Config.PREF_KEY_LIST_SPINNER, getActivity());
                        spinnerItemList.remove(pos);
                        Methods.savePrefObject(spinnerItemList, Config.PREF_KEY_LIST_SPINNER, getActivity());

                        getActivity().sendBroadcast(new Intent(Config.ACTION_NEW_PRODUCT));
                        productAdapter.notifyDataSetChanged();

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

        product_et.setText(productList.get(pos).getProduct());
        quantity_et.setText(productList.get(pos).getInStock());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productList.get(pos).setInStock(quantity_et.getText().toString());
                productList.get(pos).setProduct(product_et.getText().toString());
                Methods.savePrefObject(productList, Config.PREF_KEY_LIST_PRODUCTS, getActivity());

                ArrayList<SpinnerItem> spinnerItemList = SpinnerItem.getPrefArraylist(Config.PREF_KEY_LIST_SPINNER, getActivity());
                spinnerItemList.get(pos).name = productList.get(pos).getProduct();

                Methods.savePrefObject(spinnerItemList, Config.PREF_KEY_LIST_SPINNER, getActivity());

                getActivity().sendBroadcast(new Intent(Config.ACTION_NEW_PRODUCT));

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
