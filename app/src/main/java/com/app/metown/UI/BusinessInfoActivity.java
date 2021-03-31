package com.app.metown.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.StoreModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BusinessInfoActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtBusinessName, txtStoreName, txtAddress, txtAddPrice, txtEditServiceKindPrice, txtAddBenefit, txtDescription,
            txtLocationAddress;
    Button btnManageBusinessInfo, btnAdvertiseYourBusinessInYourTown;
    RecyclerView PostView, StoreItemView;
    ArrayList<String> postList = new ArrayList<>();
    ArrayList<StoreModel> itemList = new ArrayList<>();
    String UserID = "", ProductID = "";
    float Latitude, Longitude;
    GoogleMap mMap;
    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_info);

        Log.e("Activity", "BusinessInfoActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GetUserDefault();

        ViewInitialization();

        ViewOnClick();

        GetStoreMerchantProfileApi(UserID, ProductID);
    }

    public void GetUserDefault() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        UserID = sharedPreferences.getString("UserID", "");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtBusinessName = findViewById(R.id.txtBusinessName);
        txtStoreName = findViewById(R.id.txtStoreName);
        txtAddress = findViewById(R.id.txtAddress);
        txtAddPrice = findViewById(R.id.txtAddPrice);
        txtEditServiceKindPrice = findViewById(R.id.txtEditServiceKindPrice);
        txtAddBenefit = findViewById(R.id.txtAddBenefit);
        txtDescription = findViewById(R.id.txtDescription);
        txtLocationAddress = findViewById(R.id.txtLocationAddress);

        btnManageBusinessInfo = findViewById(R.id.btnManageBusinessInfo);
        btnAdvertiseYourBusinessInYourTown = findViewById(R.id.btnAdvertiseYourBusinessInYourTown);

        PostView = findViewById(R.id.PostView);
        StoreItemView = findViewById(R.id.StoreItemView);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnManageBusinessInfo.setOnClickListener(this);
        btnAdvertiseYourBusinessInYourTown.setOnClickListener(this);
        txtAddPrice.setOnClickListener(this);
        txtEditServiceKindPrice.setOnClickListener(this);
        txtAddBenefit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnManageBusinessInfo:
                Intent ManageBusinessInfo = new Intent(mContext, ManageBusinessInfoActivity.class);
                startActivity(ManageBusinessInfo);
                finish();
                break;
            case R.id.btnAdvertiseYourBusinessInYourTown:
                Intent SelectAdvertisingCategory = new Intent(mContext, SelectAdvertisingCategoryActivity.class);
                startActivity(SelectAdvertisingCategory);
                break;
            case R.id.txtAddPrice:
                Intent Price = new Intent(mContext, PriceActivity.class);
                startActivity(Price);
                break;
            case R.id.txtEditServiceKindPrice:
                Intent ServiceKind = new Intent(mContext, ServiceKindActivity.class);
                startActivity(ServiceKind);
                break;
            case R.id.txtAddBenefit:

                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(Latitude, Longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        enableUserLocation();
        AddMarker(latLng);
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    private void AddMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.addMarker(markerOptions);
    }

    private void GetStoreMerchantProfileApi(final String UserID, final String ProductID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().STORE_MERCHANT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().STORE_MERCHANT_PROFILE + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONObject objectData = JsonMain.getJSONObject("data");
                        String Name = objectData.getString("name");
                        txtBusinessName.setText(Name);
                        txtStoreName.setText(Name);
                        String Address = objectData.getString("address");
                        txtAddress.setText(Address);
                        String Description = objectData.getString("description");
                        txtDescription.setText(Description);
                        String LocationName = objectData.getString("location_name");
                        txtLocationAddress.setText("Address  " + LocationName);
                        String Images = objectData.getString("images");
                        String[] separated = Images.split(",");
                        postList.addAll(Arrays.asList(separated));
                        if (postList.size() > 0) {
                            PostAdapter postAdapter = new PostAdapter(mContext, postList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
                            PostView.setLayoutManager(mLayoutManager);
                            PostView.setItemAnimator(new DefaultItemAnimator());
                            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
                            pagerSnapHelper.attachToRecyclerView(PostView);
                            PostView.setAdapter(postAdapter);
                            postAdapter.notifyDataSetChanged();
                        }
                        Latitude = Float.parseFloat(objectData.getString("lats"));
                        Longitude = Float.parseFloat(objectData.getString("longs"));
                        JSONArray arrayStoreItem = objectData.getJSONArray("storeItem");
                        for (int i = 0; i < arrayStoreItem.length(); i++) {
                            StoreModel storeModel = new StoreModel();
                            storeModel.setID(arrayStoreItem.getJSONObject(i).getString("id"));
                            storeModel.setName(arrayStoreItem.getJSONObject(i).getString("name"));
                            storeModel.setAddress(arrayStoreItem.getJSONObject(i).getString("address"));
                            storeModel.setImages(arrayStoreItem.getJSONObject(i).getString("images"));
                            storeModel.setDescription(arrayStoreItem.getJSONObject(i).getString("description"));
                            storeModel.setType(arrayStoreItem.getJSONObject(i).getString("type"));
                            storeModel.setSellerID(arrayStoreItem.getJSONObject(i).getString("seller_id"));
                            itemList.add(storeModel);
                        }
                        if (itemList.size() > 0) {
                            StoreItemAdapter storeItemAdapter = new StoreItemAdapter(mContext, itemList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            StoreItemView.setLayoutManager(mLayoutManager);
                            StoreItemView.setItemAnimator(new DefaultItemAnimator());
                            StoreItemView.setAdapter(storeItemAdapter);
                            storeItemAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String Message = JsonMain.getString("msg");
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
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
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().STORE_MERCHANT_PROFILE + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", UserID);
                params.put("product_id", ProductID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().STORE_MERCHANT_PROFILE + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().STORE_MERCHANT_PROFILE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<String> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgPost;

            MyViewHolder(View view) {
                super(view);

                imgPost = view.findViewById(R.id.imgPost);
            }
        }

        public PostAdapter(Context mContext, ArrayList<String> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            String string = arrayList.get(position);

            Glide.with(mContext).load(string).into(holder.imgPost);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public static class StoreItemAdapter extends RecyclerView.Adapter<StoreItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<StoreModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgItem;
            TextView txtItemName, txtItemPrice;

            MyViewHolder(View view) {
                super(view);

                imgItem = view.findViewById(R.id.imgItem);

                txtItemName = view.findViewById(R.id.txtItemName);
                txtItemPrice = view.findViewById(R.id.txtItemPrice);
            }
        }

        public StoreItemAdapter(Context mContext, ArrayList<StoreModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            StoreModel storeModel = arrayList.get(position);

            String Images = storeModel.getImages();
            if (Images.equals("") || Images.equals("null") || Images.equals(null) || Images == null) {

            } else {
                Glide.with(mContext).load(storeModel.getImages()).into(holder.imgItem);
            }

            holder.txtItemName.setText(storeModel.getName());
            // holder.txtItemPrice.setText(storeModel.getPrice());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AchievementActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}