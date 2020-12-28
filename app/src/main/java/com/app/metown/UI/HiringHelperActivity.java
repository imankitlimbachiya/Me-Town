package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Adapters.BusinessAdapter;
import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;

import java.util.ArrayList;

public class HiringHelperActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    Button btnGoToAddJobKeyword;
    TextView txtWhatIsJobKeyword;
    RecyclerView BusinessView;
    ArrayList<CategoryMainModel> businessList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiring_helper);

        Log.e("Activity","HiringHelperActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        btnGoToAddJobKeyword = findViewById(R.id.btnGoToAddJobKeyword);

        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(this);
        btnGoToAddJobKeyword.setOnClickListener(this);

        txtWhatIsJobKeyword = findViewById(R.id.txtWhatIsJobKeyword);

        txtWhatIsJobKeyword.setPaintFlags(txtWhatIsJobKeyword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        BusinessView = findViewById(R.id.BusinessView);

        AddBusinessItems();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnGoToAddJobKeyword:
                finish();
                break;
        }
    }

    public void AddBusinessItems() {
        businessList.clear();
        for (int i = 1; i <= 5; i++) {
            CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(i), "Notification comes here");
            businessList.add(categoryMainModel);
        }

        if (businessList.size() > 0) {
            BusinessAdapter businessAdapter = new BusinessAdapter(mContext, businessList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            BusinessView.setLayoutManager(mLayoutManager);
            BusinessView.setItemAnimator(new DefaultItemAnimator());
            BusinessView.setAdapter(businessAdapter);
            businessAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
