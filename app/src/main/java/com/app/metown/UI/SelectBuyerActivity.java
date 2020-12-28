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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.app.metown.R;

public class SelectBuyerActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    LinearLayout ReviewCompletedLayout;
    RelativeLayout LeaveReviewLayout;
    Button btnFindBuyerFromRecentChat, btnMaybeLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_buyer);

        Log.e("Activity", "SelectBuyerActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        ReviewCompletedLayout = findViewById(R.id.ReviewCompletedLayout);

        LeaveReviewLayout = findViewById(R.id.LeaveReviewLayout);

        btnFindBuyerFromRecentChat = findViewById(R.id.btnFindBuyerFromRecentChat);
        btnMaybeLater = findViewById(R.id.btnMaybeLater);

        imgBack.setOnClickListener(this);
        ReviewCompletedLayout.setOnClickListener(this);
        LeaveReviewLayout.setOnClickListener(this);
        btnFindBuyerFromRecentChat.setOnClickListener(this);
        btnMaybeLater.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.ReviewCompletedLayout:
                Intent ReviewByMe = new Intent(mContext, ReviewByMeActivity.class);
                startActivity(ReviewByMe);
                break;
            case R.id.LeaveReviewLayout:
                GoToLeaveReviewActivity();
                break;
            case R.id.btnFindBuyerFromRecentChat:
                break;
            case R.id.btnMaybeLater:
                GoToLeaveReviewActivity();
                break;
        }
    }

    public void GoToLeaveReviewActivity() {
        Intent LeaveReview = new Intent(mContext, LeaveReviewActivity.class);
        startActivity(LeaveReview);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
