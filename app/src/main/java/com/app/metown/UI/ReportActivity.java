package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Adapters.ReportAdapter;
import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    RecyclerView ReportView;
    ImageView imgBack;
    ArrayList<CategoryMainModel> reportList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Log.e("Activity","ReportActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(this);

        ReportView = findViewById(R.id.ReportView);

        AddReportItems();
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

    public void AddReportItems() {
        reportList.clear();
        for (int i = 1; i <= 5; i++) {
            CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(i), "Notification comes here");
            reportList.add(categoryMainModel);
        }

        if (reportList.size() > 0) {
            ReportAdapter reportAdapter = new ReportAdapter(mContext, reportList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            ReportView.setLayoutManager(mLayoutManager);
            ReportView.setItemAnimator(new DefaultItemAnimator());
            ReportView.setAdapter(reportAdapter);
            reportAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
