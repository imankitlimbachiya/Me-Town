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

import com.app.metown.Adapters.ComplimentAdapter;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;

import java.util.ArrayList;

public class ComplimentActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtQuestion, txtSubmit;
    RecyclerView ComplimentView;
    ArrayList<StaticCategoryModel> complimentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compliment);

        Log.e("Activity","ComplimentActivity");

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

        txtQuestion = findViewById(R.id.txtQuestion);

        txtQuestion.setPaintFlags(txtQuestion.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ComplimentView = findViewById(R.id.ComplimentView);

        AddComplimentItems();
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

    public void AddComplimentItems() {
        complimentList.clear();
        for (int i = 1; i <= 10; i++) {
            StaticCategoryModel staticCategoryModel = new StaticCategoryModel(String.valueOf(i), "Notification comes here");
            complimentList.add(staticCategoryModel);
        }

        if (complimentList.size() > 0) {
            ComplimentAdapter complimentAdapter = new ComplimentAdapter(mContext, complimentList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            ComplimentView.setLayoutManager(mLayoutManager);
            ComplimentView.setItemAnimator(new DefaultItemAnimator());
            ComplimentView.setAdapter(complimentAdapter);
            complimentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
