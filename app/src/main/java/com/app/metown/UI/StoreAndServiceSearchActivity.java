package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Adapters.KeywordSearchResultAdapter;
import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;

import java.util.ArrayList;

public class StoreAndServiceSearchActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    TextView txtGoCheck;
    ImageView imgBack;
    RecyclerView KeywordSearchResultView;
    ArrayList<CategoryMainModel> keywordSearchResultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_and_service_search);

        Log.e("Activity","StoreAndServiceSearchActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtGoCheck = findViewById(R.id.txtGoCheck);

        txtGoCheck.setPaintFlags(txtGoCheck.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        imgBack.setOnClickListener(this);

        KeywordSearchResultView = findViewById(R.id.KeywordSearchResultView);

        AddKeywordSearchResultItems();
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

    public void AddKeywordSearchResultItems() {
        keywordSearchResultList.clear();
        for (int i = 1; i <= 4; i++) {
            CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(i), "Item name");
            keywordSearchResultList.add(categoryMainModel);
        }

        if (keywordSearchResultList.size() > 0) {
            KeywordSearchResultAdapter secondHandSearchItemAdapter = new KeywordSearchResultAdapter(mContext, keywordSearchResultList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            KeywordSearchResultView.setLayoutManager(mLayoutManager);
            KeywordSearchResultView.setItemAnimator(new DefaultItemAnimator());
            KeywordSearchResultView.setAdapter(secondHandSearchItemAdapter);
            secondHandSearchItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
