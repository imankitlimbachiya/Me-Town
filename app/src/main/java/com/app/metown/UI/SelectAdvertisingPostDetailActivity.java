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
import android.widget.TextView;
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
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelectAdvertisingPostDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgItem;
    Button btnIssueCoupon;
    String[] separated;
    EditText edtTitleOfCoupon, edtPeriod, edtCouponCount, edtDetailOfCoupon;
    TextView txtItemName;
    String ItemID = "", ItemImages = "", ItemName = "", ItemAddress = "", BusinessID = "12";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_advertising_post_detail);

        Log.e("Activity", "SelectAdvertisingPostDetailActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GetIntentData();

        ViewInitialization();

        ViewOnClick();

        ViewSetText();
    }

    public void GetIntentData() {
        ItemID = getIntent().getStringExtra("ItemID");
        ItemImages = getIntent().getStringExtra("ItemImages");
        ItemName = getIntent().getStringExtra("ItemName");
        ItemAddress = getIntent().getStringExtra("ItemAddress");

        separated = ItemImages.split(",");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgItem = findViewById(R.id.imgItem);

        edtTitleOfCoupon = findViewById(R.id.edtTitleOfCoupon);
        edtPeriod = findViewById(R.id.edtPeriod);
        edtCouponCount = findViewById(R.id.edtCouponCount);
        edtDetailOfCoupon = findViewById(R.id.edtDetailOfCoupon);

        txtItemName = findViewById(R.id.txtItemName);

        btnIssueCoupon = findViewById(R.id.btnIssueCoupon);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnIssueCoupon.setOnClickListener(this);
    }

    public void ViewSetText() {
        Glide.with(mContext).load(separated[0]).into(imgItem);
        txtItemName.setText(ItemName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnIssueCoupon:
                String TitleOfCoupon = edtTitleOfCoupon.getText().toString().trim();
                String Period = edtPeriod.getText().toString().trim();
                String CouponCount = edtCouponCount.getText().toString().trim();
                String DetailOfCoupon = edtDetailOfCoupon.getText().toString().trim();
                if (TitleOfCoupon.equals("")) {
                    Toast.makeText(mContext, "Please enter title of coupon.", Toast.LENGTH_LONG).show();
                } else if (Period.equals("")) {
                    Toast.makeText(mContext, "Please enter Period.", Toast.LENGTH_LONG).show();
                } else if (CouponCount.equals("")) {
                    Toast.makeText(mContext, "Please enter Coupon Count.", Toast.LENGTH_LONG).show();
                } else {
                    AddCouponForProductApi(BusinessID, TitleOfCoupon, Period, CouponCount, DetailOfCoupon, ItemID);
                }
                break;
        }
    }

    private void AddCouponForProductApi(final String BusinessID, final String TitleOfCoupon, final String Period, final String IssueCoupon,
                                        final String DetailOfCoupon, final String ProductID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ADD_COUPON_FOR_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().ADD_COUPON_FOR_PRODUCT + response);
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
                // params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_COUPON_FOR_PRODUCT + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("business_id", BusinessID);
                params.put("title_of_coupon", TitleOfCoupon);
                params.put("period", Period);
                params.put("issue_coupon", IssueCoupon);
                params.put("detail_of_coupon", DetailOfCoupon);
                params.put("product_id", ProductID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_COUPON_FOR_PRODUCT + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().ADD_COUPON_FOR_PRODUCT);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}