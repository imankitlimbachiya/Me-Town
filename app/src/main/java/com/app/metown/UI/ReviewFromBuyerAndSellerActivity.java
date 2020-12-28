package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewFromBuyerAndSellerActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    View AllView, FromBuyerView, FromSellerView;
    TextView txtAll, txtFromBuyer, txtFromSeller;
    LinearLayout AllLayout, FromBuyerLayout, FromSellerLayout;
    RecyclerView ReviewFromBuyerAndSellerView;
    ArrayList<CategoryMainModel> reviewFromBuyerAndSellerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_from_buyer_seller);

        Log.e("Activity", "ReviewFromBuyerAndSellerActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        AllView = findViewById(R.id.AllView);
        FromBuyerView = findViewById(R.id.FromBuyerView);
        FromSellerView = findViewById(R.id.FromSellerView);

        txtAll = findViewById(R.id.txtAll);
        txtFromBuyer = findViewById(R.id.txtFromBuyer);
        txtFromSeller = findViewById(R.id.txtFromSeller);

        AllLayout = findViewById(R.id.AllLayout);
        FromBuyerLayout = findViewById(R.id.FromBuyerLayout);
        FromSellerLayout = findViewById(R.id.FromSellerLayout);

        txtAll.setTextColor(getResources().getColor(R.color.black));
        AllView.setBackgroundColor(getResources().getColor(R.color.black));
        txtFromBuyer.setTextColor(getResources().getColor(R.color.grey));
        FromBuyerView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtFromSeller.setTextColor(getResources().getColor(R.color.grey));
        FromSellerView.setBackgroundColor(getResources().getColor(R.color.grey));

        imgBack.setOnClickListener(this);
        AllLayout.setOnClickListener(this);
        FromBuyerLayout.setOnClickListener(this);
        FromSellerLayout.setOnClickListener(this);

        ReviewFromBuyerAndSellerView = findViewById(R.id.ReviewFromBuyerAndSellerView);

        AddReviewFromBuyerAndSellerItems();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.AllLayout:
                txtAll.setTextColor(getResources().getColor(R.color.black));
                AllView.setBackgroundColor(getResources().getColor(R.color.black));
                txtFromBuyer.setTextColor(getResources().getColor(R.color.grey));
                FromBuyerView.setBackgroundColor(getResources().getColor(R.color.grey));
                txtFromSeller.setTextColor(getResources().getColor(R.color.grey));
                FromSellerView.setBackgroundColor(getResources().getColor(R.color.grey));
                AddReviewFromBuyerAndSellerItems();
                break;
            case R.id.FromBuyerLayout:
                txtFromBuyer.setTextColor(getResources().getColor(R.color.black));
                FromBuyerView.setBackgroundColor(getResources().getColor(R.color.black));
                txtAll.setTextColor(getResources().getColor(R.color.grey));
                AllView.setBackgroundColor(getResources().getColor(R.color.grey));
                txtFromSeller.setTextColor(getResources().getColor(R.color.grey));
                FromSellerView.setBackgroundColor(getResources().getColor(R.color.grey));
                AddReviewFromBuyerAndSellerItems();
                break;
            case R.id.FromSellerLayout:
                txtFromSeller.setTextColor(getResources().getColor(R.color.black));
                FromSellerView.setBackgroundColor(getResources().getColor(R.color.black));
                txtAll.setTextColor(getResources().getColor(R.color.grey));
                AllView.setBackgroundColor(getResources().getColor(R.color.grey));
                txtFromBuyer.setTextColor(getResources().getColor(R.color.grey));
                FromBuyerView.setBackgroundColor(getResources().getColor(R.color.grey));
                AddReviewFromBuyerAndSellerItems();
                break;
        }
    }

    public void AddReviewFromBuyerAndSellerItems() {
        reviewFromBuyerAndSellerList.clear();
        for (int i = 1; i <= 3; i++) {
            CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(i), "Item name");
            reviewFromBuyerAndSellerList.add(categoryMainModel);
        }

        if (reviewFromBuyerAndSellerList.size() > 0) {
            ReviewFromBuyerAndSellerAdapter reviewFromBuyerAndSellerAdapter = new ReviewFromBuyerAndSellerAdapter(mContext, reviewFromBuyerAndSellerList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            ReviewFromBuyerAndSellerView.setLayoutManager(mLayoutManager);
            ReviewFromBuyerAndSellerView.setItemAnimator(new DefaultItemAnimator());
            ReviewFromBuyerAndSellerView.setAdapter(reviewFromBuyerAndSellerAdapter);
            reviewFromBuyerAndSellerAdapter.notifyDataSetChanged();
        }
    }

    public static class ReviewFromBuyerAndSellerAdapter extends RecyclerView.Adapter<ReviewFromBuyerAndSellerAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CategoryMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            // RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                // OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public ReviewFromBuyerAndSellerAdapter(Context mContext, ArrayList<CategoryMainModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_from_buyer_seller_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            CategoryMainModel categoryMainModel = arrayList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent Favourite = new Intent(mContext, FavouriteActivity.class);
                    mContext.startActivity(Favourite);
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
