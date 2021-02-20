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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Adapters.ServiceNearbyCategoryAdapter;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.Models.ItemMainModel;
import com.app.metown.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProfileOfStoreActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtViewAllReview;
    GoogleMap mMap;
    SupportMapFragment supportMapFragment;
    RecyclerView ServiceNearbyCategoryView, StoreNewsView, ReviewAllView;
    ArrayList<StaticCategoryModel> serviceNearbyCategoryList = new ArrayList<>();
    ArrayList<ItemMainModel> storeNewsList = new ArrayList<>();
    ArrayList<StaticCategoryModel> reviewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_of_store);

        Log.e("Activity", "ProfileOfStoreActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtViewAllReview = findViewById(R.id.txtViewAllReview);

        imgBack.setOnClickListener(this);
        txtViewAllReview.setOnClickListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        ServiceNearbyCategoryView = findViewById(R.id.ServiceNearbyCategoryView);
        StoreNewsView = findViewById(R.id.StoreNewsView);
        ReviewAllView = findViewById(R.id.ReviewAllView);

        AddServiceNearbyCategoryItems();

        AddStoreNewsItems();

        AddReviewAllItems();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtViewAllReview:
                Intent ReviewAll = new Intent(mContext, ReviewAllActivity.class);
                startActivity(ReviewAll);
                break;
        }
    }

    public void AddServiceNearbyCategoryItems() {
        serviceNearbyCategoryList.clear();
        for (int i = 1; i <= 12; i++) {
            StaticCategoryModel staticCategoryModel = new StaticCategoryModel(String.valueOf(i), "Item name");
            serviceNearbyCategoryList.add(staticCategoryModel);
        }

        /*if (serviceNearbyCategoryList.size() > 0) {
            ServiceNearbyCategoryAdapter serviceNearbyCategoryAdapter = new ServiceNearbyCategoryAdapter(mContext, serviceNearbyCategoryList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false);
            ServiceNearbyCategoryView.setLayoutManager(mLayoutManager);
            ServiceNearbyCategoryView.setItemAnimator(new DefaultItemAnimator());
            ServiceNearbyCategoryView.setAdapter(serviceNearbyCategoryAdapter);
            serviceNearbyCategoryAdapter.notifyDataSetChanged();
        }*/
    }

    public void AddStoreNewsItems() {
        storeNewsList.clear();
        for (int i = 1; i <= 2; i++) {
            ItemMainModel categoryModel = new ItemMainModel(String.valueOf(i), "Item name");
            storeNewsList.add(categoryModel);
        }

        if (storeNewsList.size() > 0) {
            StoreNewsAdapter storeNewsAdapter = new StoreNewsAdapter(mContext, storeNewsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            StoreNewsView.setLayoutManager(mLayoutManager);
            StoreNewsView.setItemAnimator(new DefaultItemAnimator());
            StoreNewsView.setAdapter(storeNewsAdapter);
            storeNewsAdapter.notifyDataSetChanged();
        }
    }

    public static class StoreNewsAdapter extends RecyclerView.Adapter<StoreNewsAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            // RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                // OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public StoreNewsAdapter(Context mContext, ArrayList<ItemMainModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_news_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            ItemMainModel itemMainModel = arrayList.get(position);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public void AddReviewAllItems() {
        reviewList.clear();
        for (int i = 1; i <= 5; i++) {
            StaticCategoryModel staticCategoryModel = new StaticCategoryModel(String.valueOf(i), "Item name");
            reviewList.add(staticCategoryModel);
        }

        if (reviewList.size() > 0) {
            ReviewAdapter reviewAdapter = new ReviewAdapter(mContext, reviewList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            ReviewAllView.setLayoutManager(mLayoutManager);
            ReviewAllView.setItemAnimator(new DefaultItemAnimator());
            ReviewAllView.setAdapter(reviewAdapter);
            reviewAdapter.notifyDataSetChanged();
        }
    }

    public static class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<StaticCategoryModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtReply;

            MyViewHolder(View view) {
                super(view);

                txtReply = view.findViewById(R.id.txtReply);
            }
        }

        public ReviewAdapter(Context mContext, ArrayList<StaticCategoryModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            StaticCategoryModel staticCategoryModel = arrayList.get(position);

            holder.txtReply.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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