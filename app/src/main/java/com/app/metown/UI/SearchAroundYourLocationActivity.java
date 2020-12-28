package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.Adapters.CategorySearchAdapter;
import com.app.metown.Adapters.SecondHandItemAdapter;
import com.app.metown.Adapters.SecondHandNewSearchItemAdapter;
import com.app.metown.Adapters.SecondHandSearchItemAdapter;
import com.app.metown.Adapters.ServiceNearbyCategoryAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.CategoryMainModel;
import com.app.metown.Models.ItemMainModel;
import com.app.metown.Models.ItemMainModel1;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchAroundYourLocationActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgSearch;
    LinearLayout SecondHandLayout, ServiceNearbyLayout, UserLayout, SecondHandResponseLayout, ServiceNearbyResponseLayout, UserResponseLayout;
    TextView txtSecondHand, txtServiceNearby, txtUser;
    View SecondHandView, ServiceNearbyView, UserView;
    RecyclerView CategoryView, SecondHandSearchItemView, SecondHandNewSearchItemView, ServiceNearbyCategoryView;
    ArrayList<CategoryMainModel> categorySearchList = new ArrayList<>();
    ArrayList<ItemMainModel> secondHandSearchItemList = new ArrayList<>();
    ArrayList<ItemMainModel> secondHandNewSearchItemList = new ArrayList<>();
    ArrayList<CategoryMainModel> serviceNearbyCategoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_around_your_location);

        Log.e("Activity", "SearchAroundYourLocationActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgSearch = findViewById(R.id.imgSearch);

        SecondHandView = findViewById(R.id.SecondHandView);
        ServiceNearbyView = findViewById(R.id.ServiceNearbyView);
        UserView = findViewById(R.id.UserView);

        SecondHandLayout = findViewById(R.id.SecondHandLayout);
        ServiceNearbyLayout = findViewById(R.id.ServiceNearbyLayout);
        UserLayout = findViewById(R.id.UserLayout);
        SecondHandResponseLayout = findViewById(R.id.SecondHandResponseLayout);
        ServiceNearbyResponseLayout = findViewById(R.id.ServiceNearbyResponseLayout);
        UserResponseLayout = findViewById(R.id.UserResponseLayout);

        txtSecondHand = findViewById(R.id.txtSecondHand);
        txtServiceNearby = findViewById(R.id.txtServiceNearby);
        txtUser = findViewById(R.id.txtUser);

        txtSecondHand.setTextColor(getResources().getColor(R.color.black));
        SecondHandView.setBackgroundColor(getResources().getColor(R.color.black));
        txtServiceNearby.setTextColor(getResources().getColor(R.color.grey));
        ServiceNearbyView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtUser.setTextColor(getResources().getColor(R.color.grey));
        UserView.setBackgroundColor(getResources().getColor(R.color.grey));

        imgBack.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        SecondHandLayout.setOnClickListener(this);
        ServiceNearbyLayout.setOnClickListener(this);
        UserLayout.setOnClickListener(this);

        CategoryView = findViewById(R.id.CategoryView);
        SecondHandSearchItemView = findViewById(R.id.SecondHandSearchItemView);
        SecondHandNewSearchItemView = findViewById(R.id.SecondHandNewSearchItemView);
        ServiceNearbyCategoryView = findViewById(R.id.ServiceNearbyCategoryView);

        SecondHandResponseLayout.setVisibility(View.VISIBLE);
        ServiceNearbyResponseLayout.setVisibility(View.GONE);
        UserResponseLayout.setVisibility(View.GONE);

        AddCategorySearchItems();

        AddSecondHandSearchItems();

        AddSecondHandNewSearchItems();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgSearch:
                Intent StoreAndServiceSearch = new Intent(mContext, StoreAndServiceSearchActivity.class);
                startActivity(StoreAndServiceSearch);
                break;
            case R.id.SecondHandLayout:
                txtSecondHand.setTextColor(getResources().getColor(R.color.black));
                SecondHandView.setBackgroundColor(getResources().getColor(R.color.black));
                txtServiceNearby.setTextColor(getResources().getColor(R.color.grey));
                ServiceNearbyView.setBackgroundColor(getResources().getColor(R.color.grey));
                txtUser.setTextColor(getResources().getColor(R.color.grey));
                UserView.setBackgroundColor(getResources().getColor(R.color.grey));
                SecondHandResponseLayout.setVisibility(View.VISIBLE);
                ServiceNearbyResponseLayout.setVisibility(View.GONE);
                AddCategorySearchItems();
                AddSecondHandSearchItems();
                AddSecondHandNewSearchItems();
                break;
            case R.id.ServiceNearbyLayout:
                txtServiceNearby.setTextColor(getResources().getColor(R.color.black));
                ServiceNearbyView.setBackgroundColor(getResources().getColor(R.color.black));
                txtUser.setTextColor(getResources().getColor(R.color.grey));
                UserView.setBackgroundColor(getResources().getColor(R.color.grey));
                txtSecondHand.setTextColor(getResources().getColor(R.color.grey));
                SecondHandView.setBackgroundColor(getResources().getColor(R.color.grey));
                SecondHandResponseLayout.setVisibility(View.GONE);
                UserResponseLayout.setVisibility(View.GONE);
                ServiceNearbyResponseLayout.setVisibility(View.VISIBLE);
                AddServiceNearbyCategoryItems();
                break;
            case R.id.UserLayout:
                GetUserListApi();
                txtUser.setTextColor(getResources().getColor(R.color.black));
                UserView.setBackgroundColor(getResources().getColor(R.color.black));
                txtSecondHand.setTextColor(getResources().getColor(R.color.grey));
                SecondHandView.setBackgroundColor(getResources().getColor(R.color.grey));
                txtServiceNearby.setTextColor(getResources().getColor(R.color.grey));
                ServiceNearbyView.setBackgroundColor(getResources().getColor(R.color.grey));
                SecondHandResponseLayout.setVisibility(View.GONE);
                ServiceNearbyResponseLayout.setVisibility(View.GONE);
                UserResponseLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void AddCategorySearchItems() {
        categorySearchList.clear();
        CategoryMainModel categoryMainModel;

        categoryMainModel = new CategoryMainModel("1", "Digital / Gadget");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("2", "Game & Hobby");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("3", "Furniture");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("4", "Women's");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("5", "Baby & Kids");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("6", "Men's");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("7", "Living");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("8", "Sports");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("9", "Digital / Gadget");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("10", "Game & Hobby");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("11", "Furniture");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("12", "Women's");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("13", "Baby & Kids");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("14", "Men's");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("15", "Living");
        categorySearchList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("16", "Sports");
        categorySearchList.add(categoryMainModel);

        if (categorySearchList.size() > 0) {
            CategorySearchAdapter categorySearchAdapter = new CategorySearchAdapter(mContext, categorySearchList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.HORIZONTAL, false);
            CategoryView.setLayoutManager(mLayoutManager);
            CategoryView.setItemAnimator(new DefaultItemAnimator());
            CategoryView.setAdapter(categorySearchAdapter);
            categorySearchAdapter.notifyDataSetChanged();
        }
    }

    public void AddSecondHandSearchItems() {
        secondHandSearchItemList.clear();
        for (int i = 1; i <= 10; i++) {
            ItemMainModel itemMainModel = new ItemMainModel(String.valueOf(i), "Item name");
            secondHandSearchItemList.add(itemMainModel);
        }

        if (secondHandSearchItemList.size() > 0) {
            SecondHandSearchItemAdapter secondHandSearchItemAdapter = new SecondHandSearchItemAdapter(mContext, secondHandSearchItemList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            SecondHandSearchItemView.setLayoutManager(mLayoutManager);
            SecondHandSearchItemView.setItemAnimator(new DefaultItemAnimator());
            SecondHandSearchItemView.setAdapter(secondHandSearchItemAdapter);
            secondHandSearchItemAdapter.notifyDataSetChanged();
        }
    }

    public void AddSecondHandNewSearchItems() {
        secondHandNewSearchItemList.clear();
        GetSaleListApi();
    }

    public void AddServiceNearbyCategoryItems() {
        serviceNearbyCategoryList.clear();
        GetServiceListApi();
    }


    private void GetSaleListApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_SALE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_SALE_LIST + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray SalesArray = JsonMain.getJSONArray("data");
                                for (int i = 1; i <= 4; i++) {
                                    JSONObject sales = SalesArray.getJSONObject(i);
                                    ItemMainModel itemMainModel = new ItemMainModel(String.valueOf(i), sales.getString("name"));
                                    secondHandNewSearchItemList.add(itemMainModel);
                                }
                                if (secondHandNewSearchItemList.size() > 0) {
                                    SecondHandNewSearchItemAdapter secondHandNewSearchItemAdapter = new SecondHandNewSearchItemAdapter(mContext, secondHandNewSearchItemList);
                                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
                                    SecondHandNewSearchItemView.setLayoutManager(mLayoutManager);
                                    SecondHandNewSearchItemView.setItemAnimator(new DefaultItemAnimator());
                                    SecondHandNewSearchItemView.setAdapter(secondHandNewSearchItemAdapter);
                                    secondHandNewSearchItemAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String msg = JsonMain.getString("msg");
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("ERROR", "" + error.getMessage());
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_SALE_LIST + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"location_id\":\"" + "1" + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SALE_LIST + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_SALE_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void GetServiceListApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_SERVICE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_SERVICE_LIST + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray ServiceArray = JsonMain.getJSONArray("data");
                                // Log.e("Service RESPONSE", "" + ServiceArray.getJSONObject(1).getString("name").toString());
                                for (int i = 0; i < ServiceArray.length(); i++) {
                                    CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(1), ServiceArray.getJSONObject(1).getString("name"));
                                    serviceNearbyCategoryList.add(categoryMainModel);
                                }
                                if (serviceNearbyCategoryList.size() > 0) {
                                    ServiceNearbyCategoryAdapter serviceNearbyCategoryAdapter = new ServiceNearbyCategoryAdapter(mContext, serviceNearbyCategoryList);
                                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
                                    ServiceNearbyCategoryView.setLayoutManager(mLayoutManager);
                                    ServiceNearbyCategoryView.setItemAnimator(new DefaultItemAnimator());
                                    ServiceNearbyCategoryView.setAdapter(serviceNearbyCategoryAdapter);
                                    serviceNearbyCategoryAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String msg = JsonMain.getString("msg");
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("ERROR", "" + error.getMessage());
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_SALE_LIST + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"location_id\":\"" + "1" + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SALE_LIST + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_SALE_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void GetUserListApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_USER_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_USER_LIST + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            // Log.e("User RESPONSE", "" + JsonMain.toString());
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray ServiceArray = JsonMain.getJSONArray("data");
                                // Log.e("Service RESPONSE", "" + ServiceArray.getJSONObject(1).getString("name").toString());
                                for (int i = 0; i < ServiceArray.length(); i++) {
                                    CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(1), ServiceArray.getJSONObject(1).getString("name"));
                                    serviceNearbyCategoryList.add(categoryMainModel);
                                }
                                if (serviceNearbyCategoryList.size() > 0) {
                                    ServiceNearbyCategoryAdapter serviceNearbyCategoryAdapter = new ServiceNearbyCategoryAdapter(mContext, serviceNearbyCategoryList);
                                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
                                    ServiceNearbyCategoryView.setLayoutManager(mLayoutManager);
                                    ServiceNearbyCategoryView.setItemAnimator(new DefaultItemAnimator());
                                    ServiceNearbyCategoryView.setAdapter(serviceNearbyCategoryAdapter);
                                    serviceNearbyCategoryAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String msg = JsonMain.getString("msg");
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("ERROR", "" + error.getMessage());
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_USER_LIST + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"location_id\":\"" + "1" + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_USER_LIST + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_USER_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
