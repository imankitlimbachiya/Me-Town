package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.Adapters.BusinessAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.PostHireHelperModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HiringHelperActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    Button btnGoToAddJobKeyword, btnPostHiring;
    TextView txtWhatIsJobKeyword, txtError;
    LinearLayout ButtonLayout, ResponseLayout;
    RelativeLayout NoResponseLayout;
    RecyclerView BusinessView;
    ArrayList<PostHireHelperModel> businessList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiring_helper);

        Log.e("Activity", "HiringHelperActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetIntentData();

        PostHireHelperListApi();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        ButtonLayout = findViewById(R.id.ButtonLayout);
        ResponseLayout = findViewById(R.id.ResponseLayout);
        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        btnGoToAddJobKeyword = findViewById(R.id.btnGoToAddJobKeyword);
        btnPostHiring = findViewById(R.id.btnPostHiring);

        txtError = findViewById(R.id.txtError);
        txtWhatIsJobKeyword = findViewById(R.id.txtWhatIsJobKeyword);
        txtWhatIsJobKeyword.setPaintFlags(txtWhatIsJobKeyword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        BusinessView = findViewById(R.id.BusinessView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnGoToAddJobKeyword.setOnClickListener(this);
        btnPostHiring.setOnClickListener(this);
    }

    public void GetIntentData() {
        String WhereFrom = getIntent().getStringExtra("WhereFrom");
        if (WhereFrom.equals("Merchant")) {
            ButtonLayout.setVisibility(View.GONE);
            btnPostHiring.setVisibility(View.VISIBLE);
        } else {
            ButtonLayout.setVisibility(View.VISIBLE);
            btnPostHiring.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnGoToAddJobKeyword:

                break;
            case R.id.btnPostHiring:
                Intent PostHiringHelper = new Intent(mContext, PostHiringHelperActivity.class);
                startActivity(PostHiringHelper);
                break;
        }
    }

    private void PostHireHelperListApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().POST_HIRE_HELPER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().POST_HIRE_HELPER_LIST + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            PostHireHelperModel postHireHelperModel = new PostHireHelperModel();
                            postHireHelperModel.setID(arrayData.getJSONObject(i).getString("id"));
                            postHireHelperModel.setUserID(arrayData.getJSONObject(i).getString("user_id"));
                            postHireHelperModel.setKeywordID(arrayData.getJSONObject(i).getString("keyword_id"));
                            postHireHelperModel.setSalary(arrayData.getJSONObject(i).getString("salary"));
                            postHireHelperModel.setStartWorkingTime(arrayData.getJSONObject(i).getString("start_working_time"));
                            postHireHelperModel.setEndWorkingTime(arrayData.getJSONObject(i).getString("end_working_time"));
                            postHireHelperModel.setDescription(arrayData.getJSONObject(i).getString("description"));
                            postHireHelperModel.setLocationName(arrayData.getJSONObject(i).getString("location_name"));
                            postHireHelperModel.setLatitude(arrayData.getJSONObject(i).getString("lats"));
                            postHireHelperModel.setLongitude(arrayData.getJSONObject(i).getString("longs"));
                            postHireHelperModel.setUserRange(arrayData.getJSONObject(i).getString("user_range"));
                            postHireHelperModel.setDeletedAt(arrayData.getJSONObject(i).getString("deleted_at"));
                            postHireHelperModel.setCreatedAt(arrayData.getJSONObject(i).getString("created_at"));
                            postHireHelperModel.setUpdatedAt(arrayData.getJSONObject(i).getString("updated_at"));
                            postHireHelperModel.setKeyword(arrayData.getJSONObject(i).getString("keyword"));
                            postHireHelperModel.setBusinessesID(arrayData.getJSONObject(i).getString("businesses_id"));
                            postHireHelperModel.setName(arrayData.getJSONObject(i).getString("name"));
                            businessList.add(postHireHelperModel);
                        }
                        if (businessList.size() > 0) {
                            NoResponseLayout.setVisibility(View.GONE);
                            ResponseLayout.setVisibility(View.VISIBLE);
                            BusinessAdapter businessAdapter = new BusinessAdapter(mContext, businessList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            BusinessView.setLayoutManager(mLayoutManager);
                            BusinessView.setItemAnimator(new DefaultItemAnimator());
                            BusinessView.setAdapter(businessAdapter);
                            businessAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                        ResponseLayout.setVisibility(View.GONE);
                        NoResponseLayout.setVisibility(View.VISIBLE);
                        txtError.setText(ErrorMessage);
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
                Log.e("HEADER", "" + APIConstant.getInstance().POST_HIRE_HELPER_LIST + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().POST_HIRE_HELPER_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}