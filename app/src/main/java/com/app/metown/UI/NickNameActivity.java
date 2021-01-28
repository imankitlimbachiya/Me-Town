package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NickNameActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    Button btnDone;
    ImageView imgBack;
    EditText edtNickName;

    String Email = "", MobileNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);

        Log.e("Activity", "NickNameActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetIntentData();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        edtNickName = findViewById(R.id.edtNickName);

        btnDone = findViewById(R.id.btnDone);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnDone.setOnClickListener(this);
    }

    public void GetIntentData() {
        MobileNumber = getIntent().getStringExtra("MobileNumber");
        Email = getIntent().getStringExtra("Email");
        /*Log.e("MobileNumber", "" + MobileNumber);
        Log.e("Email", "" + Email);*/
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnDone:
                String NickName = edtNickName.getText().toString().trim();
                if (NickName.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Nick Name", Toast.LENGTH_LONG).show();
                } else {
                    Intent InvitationCode = new Intent(mContext, InvitationCodeActivity.class);
                    InvitationCode.putExtra("MobileNumber", MobileNumber);
                    InvitationCode.putExtra("Email", Email);
                    InvitationCode.putExtra("NickName", NickName);
                    startActivity(InvitationCode);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}