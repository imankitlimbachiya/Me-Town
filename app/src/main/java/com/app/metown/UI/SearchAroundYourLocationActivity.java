package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.app.metown.Adapters.SecondHandNewSearchItemAdapter;
import com.app.metown.Adapters.SecondHandSearchItemAdapter;
import com.app.metown.Adapters.SecondHandSearchUserAdapter;
import com.app.metown.Adapters.ServiceNearbyCategoryAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.CategoryModel;
import com.app.metown.Models.ItemModel;
import com.app.metown.Models.ManageUserModel;
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
    EditText edtSearchAroundYourLocation;
    RelativeLayout NoResponseLayout;
    LinearLayout SecondHandLayout, ServiceNearbyLayout, UserLayout, SecondHandResponseLayout, ServiceNearbyResponseLayout,
            UserResponseLayout, ResponseLayout;
    TextView txtSecondHand, txtUserUnseenProduct, txtServiceNearby, txtUser, txtError;
    View SecondHandView, ServiceNearbyView, UserView;
    RecyclerView CategoryView, SecondHandSearchItemView, SecondHandNewSearchItemView, ServiceNearbyCategoryView, UserListView;
    ArrayList<CategoryModel> categoryList = new ArrayList<>();
    ArrayList<ItemModel> itemList = new ArrayList<>();
    ArrayList<ManageUserModel> userList = new ArrayList<>();
    ArrayList<CategoryModel> serviceList = new ArrayList<>();
    String Keyword = "", offSet = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_around_your_location);

        Log.e("Activity", "SearchAroundYourLocationActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetSearchSecondHandApi(Keyword);
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgSearch = findViewById(R.id.imgSearch);

        edtSearchAroundYourLocation = findViewById(R.id.edtSearchAroundYourLocation);

        SecondHandView = findViewById(R.id.SecondHandView);
        ServiceNearbyView = findViewById(R.id.ServiceNearbyView);
        UserView = findViewById(R.id.UserView);

        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        ResponseLayout = findViewById(R.id.ResponseLayout);
        SecondHandLayout = findViewById(R.id.SecondHandLayout);
        ServiceNearbyLayout = findViewById(R.id.ServiceNearbyLayout);
        UserLayout = findViewById(R.id.UserLayout);
        SecondHandResponseLayout = findViewById(R.id.SecondHandResponseLayout);
        ServiceNearbyResponseLayout = findViewById(R.id.ServiceNearbyResponseLayout);
        UserResponseLayout = findViewById(R.id.UserResponseLayout);

        txtError = findViewById(R.id.txtError);
        txtSecondHand = findViewById(R.id.txtSecondHand);
        txtUserUnseenProduct = findViewById(R.id.txtUserUnseenProduct);
        txtServiceNearby = findViewById(R.id.txtServiceNearby);
        txtUser = findViewById(R.id.txtUser);

        CategoryView = findViewById(R.id.CategoryView);
        SecondHandSearchItemView = findViewById(R.id.SecondHandSearchItemView);
        SecondHandNewSearchItemView = findViewById(R.id.SecondHandNewSearchItemView);
        ServiceNearbyCategoryView = findViewById(R.id.ServiceNearbyCategoryView);
        UserListView = findViewById(R.id.UserListView);

        SecondHandResponseLayout.setVisibility(View.VISIBLE);
        ServiceNearbyResponseLayout.setVisibility(View.GONE);
        UserResponseLayout.setVisibility(View.GONE);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        SecondHandLayout.setOnClickListener(this);
        ServiceNearbyLayout.setOnClickListener(this);
        UserLayout.setOnClickListener(this);

        edtSearchAroundYourLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    GoToStoreAndServiceSearchActivity();
                    return true;
                }
                return false;
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgSearch:
                GoToStoreAndServiceSearchActivity();
                break;
            case R.id.SecondHandLayout:
                GetSearchSecondHandApi(Keyword);
                break;
            case R.id.ServiceNearbyLayout:
                GetServiceNearByApi();
                break;
            case R.id.UserLayout:
                GetUserListApi(offSet);
                break;
        }
    }

    public void GoToStoreAndServiceSearchActivity() {
        Keyword = edtSearchAroundYourLocation.getText().toString().trim();
        if (Keyword.equals("")) {
            Toast.makeText(mContext, "Please enter keyword which you want to search.", Toast.LENGTH_LONG).show();
        } else {
            Intent StoreAndServiceSearch = new Intent(mContext, StoreAndServiceSearchActivity.class);
            StoreAndServiceSearch.putExtra("Keyword", Keyword);
            startActivity(StoreAndServiceSearch);
        }
    }

    private void GetSearchSecondHandApi(final String Keyword) {
        txtSecondHand.setTextColor(getResources().getColor(R.color.black));
        SecondHandView.setBackgroundColor(getResources().getColor(R.color.black));
        txtServiceNearby.setTextColor(getResources().getColor(R.color.grey));
        ServiceNearbyView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtUser.setTextColor(getResources().getColor(R.color.grey));
        UserView.setBackgroundColor(getResources().getColor(R.color.grey));

        String req = "req";
        categoryList.clear();
        itemList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_SEARCH_SECOND_HAND, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    NoResponseLayout.setVisibility(View.GONE);
                    ResponseLayout.setVisibility(View.VISIBLE);
                    ServiceNearbyResponseLayout.setVisibility(View.GONE);
                    UserResponseLayout.setVisibility(View.GONE);
                    SecondHandResponseLayout.setVisibility(View.VISIBLE);
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

                        JSONArray arrayProductView = objectData.getJSONArray("ProductView");
                        for (int i = 0; i < arrayProductView.length(); i++) {
                            ItemModel itemModel = new ItemModel();
                            itemModel.setItemID(arrayProductView.getJSONObject(i).getString("id"));
                            itemModel.setItemSellerID(arrayProductView.getJSONObject(i).getString("seller_id"));
                            itemModel.setItemBuyerID(arrayProductView.getJSONObject(i).getString("buyer_id"));
                            itemModel.setItemCategoryID(arrayProductView.getJSONObject(i).getString("category_id"));
                            itemModel.setItemCategoryTitle(arrayProductView.getJSONObject(i).getString("category_title"));
                            itemModel.setItemName(arrayProductView.getJSONObject(i).getString("name"));
                            itemModel.setItemDescription(arrayProductView.getJSONObject(i).getString("description"));
                            itemModel.setItemStatus(arrayProductView.getJSONObject(i).getString("status"));
                            itemModel.setItemType(arrayProductView.getJSONObject(i).getString("type"));
                            itemModel.setItemPrice(arrayProductView.getJSONObject(i).getString("price"));
                            itemModel.setItemLatitude(arrayProductView.getJSONObject(i).getString("lats"));
                            itemModel.setItemLongitude(arrayProductView.getJSONObject(i).getString("longs"));
                            itemModel.setItemUpdatedAt(arrayProductView.getJSONObject(i).getString("updated_at"));
                            itemModel.setItemIsNegotiable(arrayProductView.getJSONObject(i).getString("is_negotiable"));
                            itemModel.setItemImages(arrayProductView.getJSONObject(i).getString("images"));
                            // itemModel.setItemImages(arrayProductView.getJSONObject(i).getString("location_name"));
                            itemModel.setItemStatusTitle(arrayProductView.getJSONObject(i).getString("status_title"));
                            itemModel.setItemTypeTitle(arrayProductView.getJSONObject(i).getString("type_title"));
                            itemList.add(itemModel);
                        }
                        if (itemList.size() > 0) {
                            txtUserUnseenProduct.setVisibility(View.VISIBLE);
                            txtUserUnseenProduct.setText("'User Name', have you seen these?");
                            SecondHandNewSearchItemAdapter secondHandNewSearchItemAdapter = new SecondHandNewSearchItemAdapter(mContext, itemList);
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
                            SecondHandNewSearchItemView.setLayoutManager(mLayoutManager);
                            SecondHandNewSearchItemView.setItemAnimator(new DefaultItemAnimator());
                            SecondHandNewSearchItemView.setAdapter(secondHandNewSearchItemAdapter);
                            secondHandNewSearchItemAdapter.notifyDataSetChanged();
                        } else {
                            txtUserUnseenProduct.setVisibility(View.GONE);
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
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

    private void GetServiceNearByApi() {
        txtServiceNearby.setTextColor(getResources().getColor(R.color.black));
        ServiceNearbyView.setBackgroundColor(getResources().getColor(R.color.black));
        txtUser.setTextColor(getResources().getColor(R.color.grey));
        UserView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtSecondHand.setTextColor(getResources().getColor(R.color.grey));
        SecondHandView.setBackgroundColor(getResources().getColor(R.color.grey));

        String req = "req";
        serviceList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_SERVICE_NEARBY, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_SERVICE_NEARBY + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONObject objectData = JsonMain.getJSONObject("data");
                        JSONArray arrayCategory = objectData.getJSONArray("Category");
                        for (int i = 0; i < arrayCategory.length(); i++) {
                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setCategoryID(arrayCategory.getJSONObject(i).getString("id"));
                            // categoryModel.setCategoryID(arrayData.getJSONObject(i).getString("parent_id"));
                            categoryModel.setCategoryTitle(arrayCategory.getJSONObject(i).getString("category_title"));
                            // categoryModel.setCategoryID(arrayData.getJSONObject(i).getString("image"));
                            // categoryModel.setCategoryID(arrayData.getJSONObject(i).getString("category_type"));
                            // categoryModel.setCategoryID(arrayData.getJSONObject(i).getString("status"));
                            // categoryModel.setCategoryID(arrayData.getJSONObject(i).getString("deleted_at"));
                            // categoryModel.setCategoryID(arrayData.getJSONObject(i).getString("created_at"));
                            // categoryModel.setCategoryID(arrayData.getJSONObject(i).getString("updated_at"));
                            // categoryModel.setCategoryID(arrayData.getJSONObject(i).getString("images"));
                            serviceList.add(categoryModel);
                        }
                        if (serviceList.size() > 0) {
                            NoResponseLayout.setVisibility(View.GONE);
                            ResponseLayout.setVisibility(View.VISIBLE);
                            SecondHandResponseLayout.setVisibility(View.GONE);
                            UserResponseLayout.setVisibility(View.GONE);
                            ServiceNearbyResponseLayout.setVisibility(View.VISIBLE);
                            ServiceNearbyCategoryAdapter serviceNearbyCategoryAdapter = new ServiceNearbyCategoryAdapter(mContext, serviceList);
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false);
                            ServiceNearbyCategoryView.setLayoutManager(mLayoutManager);
                            ServiceNearbyCategoryView.setItemAnimator(new DefaultItemAnimator());
                            ServiceNearbyCategoryView.setAdapter(serviceNearbyCategoryAdapter);
                            serviceNearbyCategoryAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        // Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
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
                params.put("Content-Transfer-Encoding", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_SERVICE_NEARBY + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_SERVICE_NEARBY);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void GetUserListApi(final String offSet) {
        txtUser.setTextColor(getResources().getColor(R.color.black));
        UserView.setBackgroundColor(getResources().getColor(R.color.black));
        txtSecondHand.setTextColor(getResources().getColor(R.color.grey));
        SecondHandView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtServiceNearby.setTextColor(getResources().getColor(R.color.grey));
        ServiceNearbyView.setBackgroundColor(getResources().getColor(R.color.grey));

        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_USER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_USER_LIST + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            ManageUserModel manageUserModel = new ManageUserModel();
                            manageUserModel.setID(arrayData.getJSONObject(i).getString("id"));
                            manageUserModel.setUniqueID(arrayData.getJSONObject(i).getString("unique_id"));
                            manageUserModel.setNickName(arrayData.getJSONObject(i).getString("nick_name"));
                            manageUserModel.setProfilePicture(arrayData.getJSONObject(i).getString("profile_pic"));
                            manageUserModel.setDistance(arrayData.getJSONObject(i).getString("distance"));
                            userList.add(manageUserModel);
                        }
                        if (userList.size() > 0) {
                            NoResponseLayout.setVisibility(View.GONE);
                            ResponseLayout.setVisibility(View.VISIBLE);
                            SecondHandResponseLayout.setVisibility(View.GONE);
                            ServiceNearbyResponseLayout.setVisibility(View.GONE);
                            UserResponseLayout.setVisibility(View.VISIBLE);
                            SecondHandSearchUserAdapter secondHandSearchUserAdapter = new SecondHandSearchUserAdapter(mContext, userList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            UserListView.setLayoutManager(mLayoutManager);
                            UserListView.setItemAnimator(new DefaultItemAnimator());
                            UserListView.setAdapter(secondHandSearchUserAdapter);
                            secondHandSearchUserAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        // Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                        ResponseLayout.setVisibility(View.GONE);
                        NoResponseLayout.setVisibility(View.VISIBLE);
                        txtError.setText(ErrorMessage);
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
                // params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_USER_LIST + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String LocationID = sharedPreferences.getString("LocationID", "");
                String params = "{\"location_id\":\"" + LocationID + "\",\"offset\":\"" + offSet + "\"}";
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