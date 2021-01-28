package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Adapters.PopularKeywordAdapter;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;

import java.util.ArrayList;

public class StoreAndServiceNearYouActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    Button btnRegisterMyBusiness;
    RecyclerView PopularKeywordView;
    ArrayList<StaticCategoryModel> popularKeywordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_and_service_near_you);

        Log.e("Activity","StoreAndServiceNearYouActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        btnRegisterMyBusiness = findViewById(R.id.btnRegisterMyBusiness);

        imgBack.setOnClickListener(this);
        btnRegisterMyBusiness.setOnClickListener(this);

        PopularKeywordView = findViewById(R.id.PopularKeywordView);

        AddPopularKeywordItems();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnRegisterMyBusiness:
                Intent RegisterMyBusiness = new Intent(mContext, RegisterMyBusinessActivity.class);
                startActivity(RegisterMyBusiness);
                break;
        }
    }

    public void AddPopularKeywordItems() {
        popularKeywordList.clear();
        for (int i = 1; i <= 10; i++) {
            StaticCategoryModel staticCategoryModel = new StaticCategoryModel(String.valueOf(i), "Notification comes here");
            popularKeywordList.add(staticCategoryModel);
        }

        if (popularKeywordList.size() > 0) {
            PopularKeywordAdapter popularKeywordAdapter = new PopularKeywordAdapter(mContext, popularKeywordList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
            PopularKeywordView.setLayoutManager(mLayoutManager);
            PopularKeywordView.setItemAnimator(new DefaultItemAnimator());
            PopularKeywordView.setAdapter(popularKeywordAdapter);
            popularKeywordAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
