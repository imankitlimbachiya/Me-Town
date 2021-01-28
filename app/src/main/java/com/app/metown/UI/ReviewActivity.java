package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Adapters.ReviewOptionAdapter;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    TextView txtSubmit;
    ImageView imgBack;
    RecyclerView ReviewOptionView;
    ArrayList<StaticCategoryModel> reviewOptionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Log.e("Activity","ReviewActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtSubmit = findViewById(R.id.txtSubmit);

        imgBack.setOnClickListener(this);
        txtSubmit.setOnClickListener(this);

        ReviewOptionView = findViewById(R.id.ReviewOptionView);

        AddReviewOptionItems();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtSubmit:
                finish();
                break;
        }
    }

    public void AddReviewOptionItems() {
        reviewOptionList.clear();
        StaticCategoryModel staticCategoryModel;

        staticCategoryModel = new StaticCategoryModel("1", "Kind");
        reviewOptionList.add(staticCategoryModel);

        staticCategoryModel = new StaticCategoryModel("2", "Cheap price");
        reviewOptionList.add(staticCategoryModel);

        staticCategoryModel = new StaticCategoryModel("3", "Satisfied");
        reviewOptionList.add(staticCategoryModel);

        staticCategoryModel = new StaticCategoryModel("4", "Not enough");
        reviewOptionList.add(staticCategoryModel);

        if (reviewOptionList.size() > 0) {
            ReviewOptionAdapter reviewOptionAdapter = new ReviewOptionAdapter(mContext, reviewOptionList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
            ReviewOptionView.setLayoutManager(mLayoutManager);
            ReviewOptionView.setItemAnimator(new DefaultItemAnimator());
            ReviewOptionView.setAdapter(reviewOptionAdapter);
            reviewOptionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
