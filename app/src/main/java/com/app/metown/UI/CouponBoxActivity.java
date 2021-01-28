package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.app.metown.Models.ItemMainModel;
import com.app.metown.Models.TopicKeywordModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CouponBoxActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    RecyclerView CouponView;
    ArrayList<ItemMainModel> couponList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_box);

        Log.e("Activity", "CouponBoxActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(this);

        CouponView = findViewById(R.id.CouponView);

        AddCouponItems();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    public void AddCouponItems() {
        couponList.clear();
        for (int i = 1; i <= 10; i++) {
            ItemMainModel itemMainModel = new ItemMainModel(String.valueOf(i), "Item name");
            couponList.add(itemMainModel);
        }

        if (couponList.size() > 0) {
            CouponAdapter couponAdapter = new CouponAdapter(mContext, couponList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            CouponView.setLayoutManager(mLayoutManager);
            CouponView.setItemAnimator(new DefaultItemAnimator());
            CouponView.setAdapter(couponAdapter);
            couponAdapter.notifyDataSetChanged();
        }
    }

    public static class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            RelativeLayout OptionLayout, GoReviewLayout;

            MyViewHolder(View view) {
                super(view);

                OptionLayout = view.findViewById(R.id.OptionLayout);
                GoReviewLayout = view.findViewById(R.id.GoReviewLayout);
            }
        }

        public CouponAdapter(Context mContext, ArrayList<ItemMainModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            ItemMainModel itemMainModel = arrayList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent Review = new Intent(mContext, FAQDetailActivity.class);
                    mContext.startActivity(Review);
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
