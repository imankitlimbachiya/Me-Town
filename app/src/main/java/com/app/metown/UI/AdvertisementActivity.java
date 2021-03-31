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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdvertisementActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtGoodWayForAdvertise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        Log.e("Activity", "AdvertisementActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtGoodWayForAdvertise = findViewById(R.id.txtGoodWayForAdvertise);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        txtGoodWayForAdvertise.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtGoodWayForAdvertise:
                Intent intent = new Intent(mContext, SelectAdvertisingCategoryActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void GetAdvertisingListApi(final String AdvertiseID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_ADVERTISING_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_ADVERTISING_LIST + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; arrayData.length() > i; i++) {

                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
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
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_ADVERTISING_LIST + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("advertise_id", AdvertiseID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_ADVERTISING_LIST + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_ADVERTISING_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}