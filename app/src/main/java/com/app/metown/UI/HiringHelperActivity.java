package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.app.metown.Models.StaticCategoryModel;
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
    Button btnGoToAddJobKeyword;
    TextView txtWhatIsJobKeyword;
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

        PostHireHelperListApi();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        btnGoToAddJobKeyword = findViewById(R.id.btnGoToAddJobKeyword);

        imgBack = findViewById(R.id.imgBack);

        txtWhatIsJobKeyword = findViewById(R.id.txtWhatIsJobKeyword);
        txtWhatIsJobKeyword.setPaintFlags(txtWhatIsJobKeyword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        BusinessView = findViewById(R.id.BusinessView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnGoToAddJobKeyword.setOnClickListener(this);
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
        }
    }

    private void PostHireHelperListApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().POST_HIRE_HELPER_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().POST_HIRE_HELPER_LIST + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray jsonArray = JsonMain.getJSONArray("data");
                                for (int i = 0; jsonArray.length() > i; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    PostHireHelperModel postHireHelperModel = new PostHireHelperModel();
                                    postHireHelperModel.setID(jsonObject.getString("id"));
                                    postHireHelperModel.setUserID(jsonObject.getString("user_id"));
                                    postHireHelperModel.setKeywordID(jsonObject.getString("keyword_id"));
                                    postHireHelperModel.setSalary(jsonObject.getString("salary"));
                                    postHireHelperModel.setStartWorkingTime(jsonObject.getString("start_working_time"));
                                    postHireHelperModel.setEndWorkingTime(jsonObject.getString("end_working_time"));
                                    postHireHelperModel.setDescription(jsonObject.getString("description"));
                                    postHireHelperModel.setDeletedAt(jsonObject.getString("deleted_at"));
                                    postHireHelperModel.setCreatedAt(jsonObject.getString("created_at"));
                                    postHireHelperModel.setUpdatedAt(jsonObject.getString("updated_at"));
                                    postHireHelperModel.setKeyword(jsonObject.getString("keyword"));
                                    businessList.add(postHireHelperModel);
                                }
                                if (businessList.size() > 0) {
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
                            }
                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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
