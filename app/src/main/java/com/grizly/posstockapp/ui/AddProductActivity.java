package com.grizly.posstockapp.ui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.grizly.posstockapp.ApplicationContext;
import com.grizly.posstockapp.R;
import com.grizly.posstockapp.models.Product;
import com.grizly.posstockapp.models.SpinnerItem;
import com.grizly.posstockapp.utils.Config;
import com.grizly.posstockapp.utils.InternalStorageContentProvider;
import com.grizly.posstockapp.utils.Methods;
import com.grizly.posstockapp.utils.SnackS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import eu.janmuller.android.simplecropimage.CropImage;

public class AddProductActivity extends AppCompatActivity {

    ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppCompatTextView title = (AppCompatTextView) toolbar.findViewById(R.id.title);
        title.setText("New Product");
        setSupportActionBar(toolbar);

        getCancel_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        productList = Product.getPrefArraylist(Config.PREF_KEY_LIST_PRODUCTS, AddProductActivity.this);

        getCreate_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getProduct_et().getText().toString().trim().length() < 1 ||
                        getQuantity_et().getText().toString().trim().length() < 1) {
                    Toast.makeText(AddProductActivity.this, "Missing Fields", Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<SpinnerItem> spinnerItemList = SpinnerItem.getPrefArraylist(Config.PREF_KEY_LIST_SPINNER, AddProductActivity.this);

                    spinnerItemList.add(new SpinnerItem(
                            productList.size(),
                            getProduct_et().getText().toString()));

                    productList.add(new Product(getQuantity_et().getText().toString(),
                            prod_img,
                            getProduct_et().getText().toString(),
                            getProduct_et().getText().toString()));
                    Methods.savePrefObject(productList, Config.PREF_KEY_LIST_PRODUCTS, AddProductActivity.this);
                    Methods.savePrefObject(spinnerItemList, Config.PREF_KEY_LIST_SPINNER, AddProductActivity.this);

                    sendBroadcast(new Intent(Config.ACTION_NEW_PRODUCT));
                    finish();
                }
            }
        });

        getImage_et().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        takePicture();
                    } else {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            SnackS.snackInfoLarge(AddProductActivity.this, getResources().getString(R.string.permission_storage));
                        }

                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                    }
                } else {
                    takePicture();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private File mFileTemp;
    public static final String TEMP_PHOTO_FILE_PATH = ApplicationContext.getContext().getString(R.string.app_name) + "/";
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo_plan";

    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;

    int REQUEST_STORAGE_PERMISSION = 3;
    public static File mFile;

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mFileTemp = new File(Environment.getExternalStorageDirectory().toString() + "/" + TEMP_PHOTO_FILE_PATH + TEMP_PHOTO_FILE_NAME + productList.size() + ".jpg");
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                /*
                 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
				 */
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

            Log.d("crop", "cannot take picture", e);
        }
    }

    public void checkDirectory() {

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFile = new File(Environment.getExternalStorageDirectory().toString() + "/" + TEMP_PHOTO_FILE_PATH);
        } else {
            mFile = new File(getFilesDir().toString() + "/" + TEMP_PHOTO_FILE_PATH);
        }
        if (!mFile.isDirectory()) {
            mFile.mkdirs();
        }
    }

    String prod_img = "";

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkDirectory();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory().toString() + "/" + TEMP_PHOTO_FILE_PATH + TEMP_PHOTO_FILE_NAME + productList.size() + ".jpg");
        } else {
            mFileTemp = new File(getFilesDir().toString() + "/" + TEMP_PHOTO_FILE_PATH + TEMP_PHOTO_FILE_NAME + productList.size() + ".jpg");
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {

            case REQUEST_CODE_TAKE_PICTURE:

                startCropImage();
                break;

            case REQUEST_CODE_CROP_IMAGE:

                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {

                    return;
                }

                prod_img = path;

                Uri uri = Uri.parse(path);

                File sd = Environment.getExternalStorageDirectory();
                File image = new File(path);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);


                getProduct_img().setImageBitmap(bitmap);

                break;
        }
    }

    private void startCropImage() {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    public AppCompatEditText getProduct_et() {
        return (AppCompatEditText) findViewById(R.id.product_id);
    }

    public AppCompatEditText getQuantity_et() {
        return (AppCompatEditText) findViewById(R.id.quantity_et);
    }

    public AppCompatButton getCancel_btn() {
        return (AppCompatButton) findViewById(R.id.cancel);
    }

    public AppCompatButton getCreate_btn() {
        return (AppCompatButton) findViewById(R.id.create);
    }

    public SimpleDraweeView getProduct_img() {
        return (SimpleDraweeView) findViewById(R.id.prod_img);
    }

    public AppCompatTextView getImage_et() {
        return (AppCompatTextView) findViewById(R.id.image_et);
    }

}
