package com.grizly.posstockapp.ui.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.grizly.posstockapp.R;
import com.grizly.posstockapp.utils.Config;
import com.grizly.posstockapp.utils.Methods;
import com.grizly.posstockapp.utils.SnackS;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppCompatTextView title = (AppCompatTextView) toolbar.findViewById(R.id.title);
        title.setText("Register");
        setSupportActionBar(toolbar);

        getRegister_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getUsername_et().getText().toString().trim().length() < 1
                        || getPassword_et().getText().toString().trim().length() < 1 ){

                    SnackS.snackAlert(RegisterActivity.this, "Please fill in all fields.");
                }else{
                    if(!getPassword_et().getText().toString().equals(getConfirm_btn().getText().toString())){
                        SnackS.snackAlert(RegisterActivity.this, "The two passwords doesn\'t match");
                    }else {
                        Methods.savePre(RegisterActivity.this, getUsername_et().getText().toString().trim(), Config.PREF_KEY_USERNAME);
                        Methods.savePre(RegisterActivity.this, getPassword_et().getText().toString().trim(), Config.PREF_KEY_PASSWORD);
                        Methods.savePre(RegisterActivity.this, "1", Config.PREF_KEY_REGISTERED);

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

    }

    public AppCompatEditText getUsername_et(){
        return (AppCompatEditText) findViewById(R.id.username);
    }

    public AppCompatEditText getPassword_et(){
        return (AppCompatEditText) findViewById(R.id.password);
    }

    public AppCompatEditText getConfirm_btn(){
        return (AppCompatEditText) findViewById(R.id.confirm_password);
    }

    public AppCompatButton getRegister_btn(){
        return (AppCompatButton) findViewById(R.id.register);
    }

}
