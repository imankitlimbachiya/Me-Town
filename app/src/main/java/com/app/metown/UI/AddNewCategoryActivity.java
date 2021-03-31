package com.app.metown.UI;

import android.content.Context;
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

public class AddNewCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    EditText edtAddCategory;
    Button btnRequestCategory;
    String BusinessID = "4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);

        Log.e("Activity", "AddNewCategoryActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        edtAddCategory = findViewById(R.id.edtAddCategory);

        btnRequestCategory = findViewById(R.id.btnRequestCategory);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnRequestCategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnRequestCategory:
                /*Intent intent = new Intent(mContext, NotificationNewsActivity.class);
                startActivity(intent);*/
                String MyServiceTag = edtAddCategory.getText().toString().trim();
                if (MyServiceTag.equals("")) {
                    Toast.makeText(mContext, "Please add category.", Toast.LENGTH_LONG).show();
                } else {
                    AddServiceKindApi(BusinessID, MyServiceTag);
                }
                break;
        }
    }

    private void AddServiceKindApi(final String BusinessID, final String MyServiceTag) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ADD_SERVICE_KIND, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().ADD_SERVICE_KIND + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String msg = JsonMain.getString("msg");
                    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_SERVICE_KIND + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"business_id\":\"" + BusinessID + "\",\"my_service_tag\":\"" + MyServiceTag + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_SERVICE_KIND + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().ADD_SERVICE_KIND);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}