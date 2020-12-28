package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.app.metown.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    RelativeLayout AchievementLayout, MyCommunityLayout, FeedbackLayout, ReviewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.e("Activity","ProfileActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        AchievementLayout = findViewById(R.id.AchievementLayout);
        MyCommunityLayout = findViewById(R.id.MyCommunityLayout);
        FeedbackLayout = findViewById(R.id.FeedbackLayout);
        ReviewLayout = findViewById(R.id.ReviewLayout);

        imgBack.setOnClickListener(this);
        AchievementLayout.setOnClickListener(this);
        MyCommunityLayout.setOnClickListener(this);
        FeedbackLayout.setOnClickListener(this);
        ReviewLayout.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.AchievementLayout:
                Intent intent = new Intent(mContext, AchievementActivity.class);
                startActivity(intent);
                break;
            case R.id.MyCommunityLayout:
                Intent MyCommunity = new Intent(mContext, MyCommunityActivity.class);
                startActivity(MyCommunity);
                break;
            case R.id.FeedbackLayout:
                Intent Feedback = new Intent(mContext, FeedbackActivity.class);
                startActivity(Feedback);
                break;
            case R.id.ReviewLayout:
                Intent ReviewFromBuyerAndSeller = new Intent(mContext, ReviewFromBuyerAndSellerActivity.class);
                startActivity(ReviewFromBuyerAndSeller);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}