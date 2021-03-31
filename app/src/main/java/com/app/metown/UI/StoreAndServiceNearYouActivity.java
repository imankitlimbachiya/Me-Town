package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.Adapters.PopularKeywordAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.CategoryModel;
import com.app.metown.Models.JobKeywordModel;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreAndServiceNearYouActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtNickName;
    CircleImageView imgProfile;
    EditText edtFindKeyword;
    Button btnRegisterMyBusiness;
    RecyclerView PopularKeywordView;
    ArrayList<JobKeywordModel> popularKeywordList = new ArrayList<>();
    String Keyword = "", offSet = "0", ProfilePicture = "", NickName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_and_service_near_you);

        Log.e("Activity", "StoreAndServiceNearYouActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // GetUserDefault();

        ViewInitialization();

        ViewOnClick();

        // ViewSetText();

        GetPopularKeywordApi(Keyword, offSet);
    }

    public void GetUserDefault() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        ProfilePicture = sharedPreferences.getString("ProfilePicture", "");
        NickName = sharedPreferences.getString("NickName", "");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        imgProfile = findViewById(R.id.imgProfile);

        edtFindKeyword = findViewById(R.id.edtFindKeyword);

        btnRegisterMyBusiness = findViewById(R.id.btnRegisterMyBusiness);

        PopularKeywordView = findViewById(R.id.PopularKeywordView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnRegisterMyBusiness.setOnClickListener(this);

        edtFindKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                Keyword = charSequence.toString().trim();
                popularKeywordList.clear();
                GetPopularKeywordApi(Keyword, offSet);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Keyword = charSequence.toString().trim();
                popularKeywordList.clear();
                GetPopularKeywordApi(Keyword, offSet);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Keyword = editable.toString().trim();
                popularKeywordList.clear();
                GetPopularKeywordApi(Keyword, offSet);
            }
        });
    }

    public void ViewSetText() {
        Glide.with(mContext).load(ProfilePicture).into(imgProfile);
        txtNickName.setText(NickName);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnRegisterMyBusiness:
                Intent RegisterMyBusiness = new Intent(mContext, RegisterMyBusinessActivity.class);
                startActivity(RegisterMyBusiness);
                break;
        }
    }

    private void GetPopularKeywordApi(final String Keyword, final String offSet) {
        String req = "req";
        popularKeywordList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().POPULAR_KEYWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().POPULAR_KEYWORD + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; arrayData.length() > i; i++) {
                            JobKeywordModel jobKeywordModel = new JobKeywordModel();
                            jobKeywordModel.setJobID(arrayData.getJSONObject(i).getString("id"));
                            jobKeywordModel.setJobKeyword(arrayData.getJSONObject(i).getString("keyword"));
                            jobKeywordModel.setSearchCount(arrayData.getJSONObject(i).getString("searchcount"));
                            popularKeywordList.add(jobKeywordModel);
                        }
                        if (popularKeywordList.size() > 0) {
                            PopularKeywordAdapter popularKeywordAdapter = new PopularKeywordAdapter(mContext, popularKeywordList);
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
                            PopularKeywordView.setLayoutManager(mLayoutManager);
                            PopularKeywordView.setItemAnimator(new DefaultItemAnimator());
                            PopularKeywordView.setAdapter(popularKeywordAdapter);
                            popularKeywordAdapter.notifyDataSetChanged();
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
                Log.e("HEADER", "" + APIConstant.getInstance().POPULAR_KEYWORD + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("keyword", Keyword);
                params.put("offset", offSet);
                Log.e("PARAMETER", "" + APIConstant.getInstance().POPULAR_KEYWORD + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().POPULAR_KEYWORD);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}