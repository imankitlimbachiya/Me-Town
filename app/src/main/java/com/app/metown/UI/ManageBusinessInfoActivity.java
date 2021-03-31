package com.app.metown.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManageBusinessInfoActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtStoreName, txtStoreAddress, txtDeleteMerchant;
    RelativeLayout GeneralInfoLayout, ServiceKindLayout, NotificationLayout, AdditionalInfoLayout, PriceLayout;
    String Merchant = "", UserID = "";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_business_info);

        Log.e("Activity", "ManageBusinessInfoActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetUserDefault();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtStoreName = findViewById(R.id.txtStoreName);
        txtStoreAddress = findViewById(R.id.txtStoreAddress);
        txtDeleteMerchant = findViewById(R.id.txtDeleteMerchant);

        GeneralInfoLayout = findViewById(R.id.GeneralInfoLayout);
        AdditionalInfoLayout = findViewById(R.id.AdditionalInfoLayout);
        ServiceKindLayout = findViewById(R.id.ServiceKindLayout);
        PriceLayout = findViewById(R.id.PriceLayout);
        NotificationLayout = findViewById(R.id.NotificationLayout);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        GeneralInfoLayout.setOnClickListener(this);
        AdditionalInfoLayout.setOnClickListener(this);
        ServiceKindLayout.setOnClickListener(this);
        PriceLayout.setOnClickListener(this);
        NotificationLayout.setOnClickListener(this);
        txtDeleteMerchant.setOnClickListener(this);
    }

    public void GetUserDefault() {
        sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
        UserID = sharedPreferences.getString("UserID", "");
        String MerchantStoreName = sharedPreferences.getString("MerchantStoreName", "");
        txtStoreName.setText(MerchantStoreName);
        String MerchantStoreAddress = sharedPreferences.getString("MerchantStoreAddress", "");
        txtStoreAddress.setText(MerchantStoreAddress);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.PriceLayout:
                Intent Price = new Intent(mContext, PriceActivity.class);
                startActivity(Price);
                break;
            case R.id.GeneralInfoLayout:
                if (Merchant.equals("0")) {
                    Intent RegisterMerchant = new Intent(mContext, RegisterMerchantActivity.class);
                    startActivity(RegisterMerchant);
                } else {
                    Toast.makeText(mContext, "You're already Merchant.", Toast.LENGTH_LONG).show();
                    /*Intent MerchantMenu = new Intent(mContext, MerchantMenuActivity.class);
                    startActivity(MerchantMenu);*/
                }
                break;
            case R.id.ServiceKindLayout:
                Intent ServiceKind = new Intent(mContext, ServiceKindActivity.class);
                startActivity(ServiceKind);
                break;
            case R.id.AdditionalInfoLayout:
                if (Merchant.equals("0")) {
                    Intent AdditionalInfo = new Intent(mContext, AdditionalInfoActivity.class);
                    startActivity(AdditionalInfo);
                } else {
                    Toast.makeText(mContext, "You're already Merchant.", Toast.LENGTH_LONG).show();
                    /*Intent MerchantMenu = new Intent(mContext, MerchantMenuActivity.class);
                    startActivity(MerchantMenu);*/
                }
                break;
            case R.id.NotificationLayout:
                Intent NotificationNews = new Intent(mContext, NotificationNewsActivity.class);
                startActivity(NotificationNews);
                break;
            case R.id.txtDeleteMerchant:
                DeleteMerchantApi();
                break;
        }
    }

    private void DeleteMerchantApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().DELETE_MERCHANT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().DELETE_MERCHANT + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        finish();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
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
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                params.put("Content-Transfer-Encoding", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().DELETE_MERCHANT + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().DELETE_MERCHANT);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}