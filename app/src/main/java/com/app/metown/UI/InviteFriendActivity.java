package com.app.metown.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

public class InviteFriendActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgFacebook;
    LinearLayout AvailableCouponLayout;
    Button btnInvitation, btnGoToCouponBox;
    String InvitationCode = "", offSet = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);

        Log.e("Activity", "InviteFriendActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GetUserDefault();

        ViewInitialization();

        ViewOnClick();

        ViewSetText();

        UserDownloadCouponListApi(offSet);
    }

    public void GetUserDefault() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        InvitationCode = sharedPreferences.getString("InvitationCode", "");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgFacebook = findViewById(R.id.imgFacebook);

        AvailableCouponLayout = findViewById(R.id.AvailableCouponLayout);

        btnInvitation = findViewById(R.id.btnInvitation);
        btnGoToCouponBox = findViewById(R.id.btnGoToCouponBox);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        imgFacebook.setOnClickListener(this);
        btnInvitation.setOnClickListener(this);
        btnGoToCouponBox.setOnClickListener(this);
    }

    public void ViewSetText() {
        btnInvitation.setText(InvitationCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnInvitation:

                break;
            case R.id.imgFacebook:

                break;
            case R.id.btnGoToCouponBox:
                Intent intent = new Intent(mContext, CouponBoxActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void UserDownloadCouponListApi(final String offSet) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().USER_DOWNLOAD_COUPON_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().USER_DOWNLOAD_COUPON_LIST + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String msg = JsonMain.getString("msg");
                    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        AvailableCouponLayout.setVisibility(View.VISIBLE);
                    } else {
                        AvailableCouponLayout.setVisibility(View.GONE);
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
                Log.e("HEADER", "" + APIConstant.getInstance().USER_DOWNLOAD_COUPON_LIST + params);
                return params;
            }

            // Raw data passing
            /*@Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"id\":\"" + ID + "\",\"business_id\":\"" + BusinessID + "\",\"service_item\":\"" + ServiceItem +
                        "\",\"price_kind\":\"" + PriceKind + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().USER_DOWNLOAD_COUPON_LIST + params);
                return params.getBytes();
            }*/
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().USER_DOWNLOAD_COUPON_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}