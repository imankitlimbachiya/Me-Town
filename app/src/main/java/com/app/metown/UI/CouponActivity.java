package com.app.metown.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.CouponModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CouponActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtError;
    Button btnIssueCoupon;
    RecyclerView CouponView;
    ArrayList<CouponModel> couponList = new ArrayList<>();
    String offSet = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        Log.e("Activity", "CouponActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        MerchantCouponListApi(offSet);
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtError = findViewById(R.id.txtError);

        btnIssueCoupon = findViewById(R.id.btnIssueCoupon);

        CouponView = findViewById(R.id.CouponView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnIssueCoupon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnIssueCoupon:
                Intent SelectAdvertisingPost = new Intent(mContext, SelectAdvertisingPostActivity.class);
                startActivity(SelectAdvertisingPost);
                break;
        }
    }

    private void MerchantCouponListApi(final String offSet) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().MERCHANT_COUPON_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().MERCHANT_COUPON_LIST + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            CouponModel couponModel = new CouponModel();
                            couponModel.setCouponID(arrayData.getJSONObject(i).getString("id"));
                            couponModel.setCouponAdvertiseID(arrayData.getJSONObject(i).getString("advertise_id"));
                            couponModel.setCouponName(arrayData.getJSONObject(i).getString("title_of_coupon"));
                            couponModel.setCouponImages(arrayData.getJSONObject(i).getJSONObject("Product_Detail").getString("images"));
                            couponModel.setCouponCount(arrayData.getJSONObject(i).getString("issue_coupon"));
                            couponModel.setDetailOfCoupon(arrayData.getJSONObject(i).getString("detail_of_coupon"));
                            couponList.add(couponModel);
                        }
                        if (couponList.size() > 0) {
                            IssueCouponAdapter issueCouponAdapter = new IssueCouponAdapter(mContext, couponList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            CouponView.setLayoutManager(mLayoutManager);
                            CouponView.setItemAnimator(new DefaultItemAnimator());
                            CouponView.setAdapter(issueCouponAdapter);
                            issueCouponAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                        txtError.setVisibility(View.VISIBLE);
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
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().MERCHANT_COUPON_LIST + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("offset", offSet);
                Log.e("PARAMETER", "" + APIConstant.getInstance().MERCHANT_COUPON_LIST + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MERCHANT_COUPON_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class IssueCouponAdapter extends RecyclerView.Adapter<IssueCouponAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CouponModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgCoupon;
            TextView txtCouponTitle, txtCouponDetail, txtCouponCount;

            MyViewHolder(View view) {
                super(view);

                imgCoupon = view.findViewById(R.id.imgCoupon);

                txtCouponTitle = view.findViewById(R.id.txtCouponTitle);
                txtCouponDetail = view.findViewById(R.id.txtCouponDetail);
                txtCouponCount = view.findViewById(R.id.txtCouponCount);
            }
        }

        public IssueCouponAdapter(Context mContext, ArrayList<CouponModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_coupon_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final CouponModel couponModel = arrayList.get(position);

            String Images = couponModel.getCouponImages();
            String[] separated = Images.split(",");
            Glide.with(mContext).load(separated[0]).into(holder.imgCoupon);
            holder.txtCouponTitle.setText(couponModel.getCouponName());
            holder.txtCouponDetail.setText(couponModel.getDetailOfCoupon());
            holder.txtCouponCount.setText("Total Coupon : " + couponModel.getCouponCount());
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