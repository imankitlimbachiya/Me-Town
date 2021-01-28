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
import com.app.metown.Adapters.KeywordSearchResultAdapter;
import com.app.metown.Adapters.SecondHandNewSearchItemAdapter;
import com.app.metown.Adapters.SecondHandSearchItemAdapter;
import com.app.metown.Adapters.ServiceNearbyCategoryAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.CategoryModel;
import com.app.metown.Models.ItemModel;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.Models.ItemMainModel;
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
    ArrayList<CategoryModel> categoryList = new ArrayList<>();
    ArrayList<ItemModel> itemList = new ArrayList<>();
    ArrayList<ItemMainModel> secondHandNewSearchItemList = new ArrayList<>();
    ArrayList<StaticCategoryModel> serviceNearbyCategoryList = new ArrayList<>();

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

        GetSearchSecondHandApi("");

        GetSaleListApi();
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
                GetSearchSecondHandApi("");
                GetSaleListApi();
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
                GetServiceListApi();
                break;
            case R.id.UserLayout:
                txtUser.setTextColor(getResources().getColor(R.color.black));
                UserView.setBackgroundColor(getResources().getColor(R.color.black));
                txtSecondHand.setTextColor(getResources().getColor(R.color.grey));
                SecondHandView.setBackgroundColor(getResources().getColor(R.color.grey));
                txtServiceNearby.setTextColor(getResources().getColor(R.color.grey));
                ServiceNearbyView.setBackgroundColor(getResources().getColor(R.color.grey));
                SecondHandResponseLayout.setVisibility(View.GONE);
                ServiceNearbyResponseLayout.setVisibility(View.GONE);
                UserResponseLayout.setVisibility(View.VISIBLE);
                GetUserListApi();
                break;
        }
    }

    private void GetSearchSecondHandApi(final String Keyword) {
        String req = "req";
        categoryList.clear();
        itemList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_SEARCH_SECOND_HAND,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_SEARCH_SECOND_HAND + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONObject objectData = JsonMain.getJSONObject("data");
                                JSONArray arrayCategory = objectData.getJSONArray("Category");
                                for (int i = 0; i < arrayCategory.length(); i++) {
                                    CategoryModel categoryModel = new CategoryModel();
                                    categoryModel.setCategoryID(arrayCategory.getJSONObject(i).getString("id"));
                                    categoryModel.setCategoryParentID(arrayCategory.getJSONObject(i).getString("parent_id"));
                                    categoryModel.setCategoryTitle(arrayCategory.getJSONObject(i).getString("category_title"));
                                    categoryModel.setCategoryImage(arrayCategory.getJSONObject(i).getString("image"));
                                    categoryModel.setCategoryType(arrayCategory.getJSONObject(i).getString("category_type"));
                                    categoryModel.setCategoryStatus(arrayCategory.getJSONObject(i).getString("status"));
                                    categoryModel.setImages(arrayCategory.getJSONObject(i).getString("images"));
                                    /*categoryModel.setImages(arrayCategory.getJSONObject(i).getString("deleted_at"));
                                    categoryModel.setImages(arrayCategory.getJSONObject(i).getString("created_at"));
                                    categoryModel.setImages(arrayCategory.getJSONObject(i).getString("updated_at"));*/
                                    categoryList.add(categoryModel);
                                }
                                if (categoryList.size() > 0) {
                                    CategorySearchAdapter categorySearchAdapter = new CategorySearchAdapter(mContext, categoryList);
                                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.HORIZONTAL, false);
                                    CategoryView.setLayoutManager(mLayoutManager);
                                    CategoryView.setItemAnimator(new DefaultItemAnimator());
                                    CategoryView.setAdapter(categorySearchAdapter);
                                    categorySearchAdapter.notifyDataSetChanged();
                                }

                                JSONArray arrayProduct = objectData.getJSONArray("Product");
                                for (int i = 0; i < arrayProduct.length(); i++) {
                                    ItemModel itemModel = new ItemModel();
                                    itemModel.setItemID(arrayProduct.getJSONObject(i).getString("id"));
                                    itemModel.setItemSellerID(arrayProduct.getJSONObject(i).getString("seller_id"));
                                    itemModel.setItemBuyerID(arrayProduct.getJSONObject(i).getString("buyer_id"));
                                    itemModel.setItemCategoryID(arrayProduct.getJSONObject(i).getString("category_id"));
                                    itemModel.setItemCategoryTitle(arrayProduct.getJSONObject(i).getString("category_title"));
                                    itemModel.setItemName(arrayProduct.getJSONObject(i).getString("name"));
                                    itemModel.setItemDescription(arrayProduct.getJSONObject(i).getString("description"));
                                    itemModel.setItemStatus(arrayProduct.getJSONObject(i).getString("status"));
                                    itemModel.setItemType(arrayProduct.getJSONObject(i).getString("type"));
                                    itemModel.setItemPrice(arrayProduct.getJSONObject(i).getString("price"));
                                    itemModel.setItemLatitude(arrayProduct.getJSONObject(i).getString("lats"));
                                    itemModel.setItemLongitude(arrayProduct.getJSONObject(i).getString("longs"));
                                    itemModel.setItemUpdatedAt(arrayProduct.getJSONObject(i).getString("updated_at"));
                                    itemModel.setItemIsNegotiable(arrayProduct.getJSONObject(i).getString("is_negotiable"));
                                    itemModel.setItemImages(arrayProduct.getJSONObject(i).getString("images"));
                                    itemModel.setItemStatusTitle(arrayProduct.getJSONObject(i).getString("status_title"));
                                    itemModel.setItemTypeTitle(arrayProduct.getJSONObject(i).getString("type_title"));
                                    itemList.add(itemModel);
                                }
                                if (itemList.size() > 0) {
                                    SecondHandSearchItemAdapter secondHandSearchItemAdapter = new SecondHandSearchItemAdapter(mContext, itemList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
                                    SecondHandSearchItemView.setLayoutManager(mLayoutManager);
                                    SecondHandSearchItemView.setItemAnimator(new DefaultItemAnimator());
                                    SecondHandSearchItemView.setAdapter(secondHandSearchItemAdapter);
                                    secondHandSearchItemAdapter.notifyDataSetChanged();
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_SEARCH_SECOND_HAND + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("keyword", Keyword);
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SEARCH_SECOND_HAND + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_SEARCH_SECOND_HAND);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void GetSaleListApi() {
        String req = "req";
        secondHandNewSearchItemList.clear();
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
        serviceNearbyCategoryList.clear();
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
                                for (int i = 0; i < ServiceArray.length(); i++) {
                                    StaticCategoryModel staticCategoryModel = new StaticCategoryModel(String.valueOf(1), ServiceArray.getJSONObject(1).getString("name"));
                                    serviceNearbyCategoryList.add(staticCategoryModel);
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_SERVICE_LIST + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"location_id\":\"" + "1" + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SERVICE_LIST + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_SERVICE_LIST);
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
                                for (int i = 0; i < ServiceArray.length(); i++) {
                                    StaticCategoryModel staticCategoryModel = new StaticCategoryModel(String.valueOf(1), ServiceArray.getJSONObject(1).getString("name"));
                                    serviceNearbyCategoryList.add(staticCategoryModel);
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
