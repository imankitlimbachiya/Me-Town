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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelectBuyerActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgItem;
    TextView txtItemName;
    LinearLayout ReviewCompletedLayout;
    RelativeLayout LeaveReviewLayout;
    Button btnFindBuyerFromRecentChat, btnMaybeLater;
    String ItemID = "", ItemName = "", ItemImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_buyer);

        Log.e("Activity", "SelectBuyerActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GetIntentData();

        ViewInitialization();

        ViewOnClick();

        ViewSetText();

        MySaleSoldReviewApi(ItemID);
    }

    public void GetIntentData() {
        ItemID = getIntent().getStringExtra("ItemID");
        ItemName = getIntent().getStringExtra("ItemName");
        ItemImage = getIntent().getStringExtra("ItemImage");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgItem = findViewById(R.id.imgItem);

        txtItemName = findViewById(R.id.txtItemName);

        ReviewCompletedLayout = findViewById(R.id.ReviewCompletedLayout);

        LeaveReviewLayout = findViewById(R.id.LeaveReviewLayout);

        btnFindBuyerFromRecentChat = findViewById(R.id.btnFindBuyerFromRecentChat);
        btnMaybeLater = findViewById(R.id.btnMaybeLater);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        ReviewCompletedLayout.setOnClickListener(this);
        LeaveReviewLayout.setOnClickListener(this);
        btnFindBuyerFromRecentChat.setOnClickListener(this);
        btnMaybeLater.setOnClickListener(this);
    }

    public void ViewSetText() {
        txtItemName.setText(ItemName);
        Glide.with(mContext).load(ItemImage).into(imgItem);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.ReviewCompletedLayout:
                Intent ReviewByMe = new Intent(mContext, ReviewByMeActivity.class);
                startActivity(ReviewByMe);
                break;
            case R.id.LeaveReviewLayout:
                GoToLeaveReviewActivity();
                break;
            case R.id.btnFindBuyerFromRecentChat:
                break;
            case R.id.btnMaybeLater:
                GoToLeaveReviewActivity();
                break;
        }
    }

    private void MySaleSoldReviewApi(final String ID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().MY_SALE_SOLD_REVIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().MY_SALE_SOLD_REVIEW + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
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
                params.put("Content-Transfer-Encoding", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().MY_SALE_SOLD_REVIEW + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().MY_SALE_SOLD_REVIEW + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_SALE_SOLD_REVIEW);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public void GoToLeaveReviewActivity() {
        Intent LeaveReview = new Intent(mContext, LeaveReviewActivity.class);
        startActivity(LeaveReview);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}