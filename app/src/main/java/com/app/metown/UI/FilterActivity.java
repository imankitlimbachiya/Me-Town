package com.app.metown.UI;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.app.metown.Adapters.CategoryAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.CategoryModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    RecyclerView CategoryView;
    ArrayList<CategoryModel> categoryList = new ArrayList<>();
    String CategoryType = "1", ParentID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Log.e("Activity", "FilterActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(this);

        CategoryView = findViewById(R.id.CategoryView);

        GetCategoryApi(CategoryType, ParentID);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgBack) {
            finish();
        }
    }

    private void GetCategoryApi(final String CategoryType, final String ParentID) {
        String req = "req";
        categoryList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_CATEGORY + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equals("false")) {
                                JSONObject objectData = JsonMain.getJSONObject("data");
                                JSONArray arrayCategoryList = objectData.getJSONArray("category_list");
                                for (int i = 0; i < arrayCategoryList.length(); i++) {
                                    CategoryModel categoryModel = new CategoryModel();
                                    categoryModel.setCategoryID(arrayCategoryList.getJSONObject(i).getString("id"));
                                    categoryModel.setCategoryTitle(arrayCategoryList.getJSONObject(i).getString("category_title"));
                                    categoryList.add(categoryModel);
                                }
                                if (categoryList.size() > 0) {
                                    CategoryAdapter categoryAdapter = new CategoryAdapter(mContext, categoryList);
                                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
                                    CategoryView.setLayoutManager(mLayoutManager);
                                    CategoryView.setItemAnimator(new DefaultItemAnimator());
                                    CategoryView.setAdapter(categoryAdapter);
                                    categoryAdapter.notifyDataSetChanged();
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
            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer N1QwNDNINDBQZVk0S1VycWMzWWRhY29Ba3pyeWpW5f4552c2ae884");
                params.put("Content-Type", "application/json");
                return params;
            }*/

            // Form data passing
            /*protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("email", edtEmail.getText().toString());
                // params.put("password", edtPassword.getText().toString());
                // params.put("secretkey", APIConstant.getInstance().ApiSecretsKey);
                Log.e("PARAMETER", "" + APIConstant.getInstance().SIGN_UP + params);
                return params;
            }*/

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"category_type\":\"" + CategoryType + "\",\"parent_id\":\"" + ParentID + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_CATEGORY + params);
                return params.getBytes();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_CATEGORY);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
