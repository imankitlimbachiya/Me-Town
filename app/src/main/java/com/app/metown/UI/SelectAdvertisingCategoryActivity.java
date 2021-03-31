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
import android.widget.LinearLayout;
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

public class SelectAdvertisingCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    Button btnConfirm;
    EditText edtAdvertisePrice, edtUserInRadius, edtUserViewAdd;
    String BusinessID = "4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_advertising_category);

        Log.e("Activity", "SelectAdvertisingCategoryActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        edtAdvertisePrice = findViewById(R.id.edtAdvertisePrice);
        edtUserInRadius = findViewById(R.id.edtUserInRadius);
        edtUserViewAdd = findViewById(R.id.edtUserViewAdd);

        btnConfirm = findViewById(R.id.btnConfirm);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnConfirm:
                String AdvertisePrice = edtAdvertisePrice.getText().toString().trim();
                String UserInRadius = edtUserInRadius.getText().toString().trim();
                String UserViewAdd = edtUserViewAdd.getText().toString().trim();
                if (AdvertisePrice.equals("")) {
                    Toast.makeText(mContext, "Please enter your advertise price.", Toast.LENGTH_LONG).show();
                } else if (UserInRadius.equals("")) {
                    Toast.makeText(mContext, "Please enter your user in your radius.", Toast.LENGTH_LONG).show();
                } else if (UserViewAdd.equals("")) {
                    Toast.makeText(mContext, "Please enter your user that view your add.", Toast.LENGTH_LONG).show();
                } else {
                    AddAdvertisingApi(BusinessID, AdvertisePrice, UserInRadius, UserViewAdd);
                }
                break;
        }
    }

    private void AddAdvertisingApi(final String BusinessID, final String Price, final String ViewInRadius, final String ViewYourAdd) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ADD_ADVERTISING, new Response.Listener<String>() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().ADD_ADVERTISING + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_ADVERTISING + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("business_id", BusinessID);
                params.put("price", Price);
                params.put("viewin_radius", ViewInRadius);
                params.put("viewyour_add", ViewYourAdd);
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_ADVERTISING + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().ADD_ADVERTISING);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}