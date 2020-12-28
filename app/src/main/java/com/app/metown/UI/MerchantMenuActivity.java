package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.metown.R;

public class MerchantMenuActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    LinearLayout MyBusinessLayout;
    RelativeLayout CouponLayout, ReservationLayout, RatingLayout, AchievementLayout, HiringHelperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_menu);

        Log.e("Activity","MerchantMenuActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        MyBusinessLayout = findViewById(R.id.MyBusinessLayout);

        CouponLayout = findViewById(R.id.CouponLayout);
        ReservationLayout = findViewById(R.id.ReservationLayout);
        RatingLayout = findViewById(R.id.RatingLayout);
        AchievementLayout = findViewById(R.id.AchievementLayout);
        HiringHelperLayout = findViewById(R.id.HiringHelperLayout);

        imgBack.setOnClickListener(this);
        CouponLayout.setOnClickListener(this);
        ReservationLayout.setOnClickListener(this);
        RatingLayout.setOnClickListener(this);
        AchievementLayout.setOnClickListener(this);
        HiringHelperLayout.setOnClickListener(this);
        MyBusinessLayout.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.CouponLayout:
                Intent Coupon = new Intent(mContext, CouponActivity.class);
                startActivity(Coupon);
                break;
            case R.id.ReservationLayout:
                Intent ReservationSchedule = new Intent(mContext, ReservationScheduleActivity.class);
                startActivity(ReservationSchedule);
                break;
            case R.id.RatingLayout:
                Intent Feedback = new Intent(mContext, FeedbackActivity.class);
                startActivity(Feedback);
                break;
            case R.id.AchievementLayout:
                Intent Achievement = new Intent(mContext, AchievementActivity.class);
                startActivity(Achievement);
                break;
            case R.id.HiringHelperLayout:
                Intent HiringHelper = new Intent(mContext, HiringHelperActivity.class);
                startActivity(HiringHelper);
                break;
            case R.id.MyBusinessLayout:
                Intent BusinessInfo = new Intent(mContext, BusinessInfoActivity.class);
                startActivity(BusinessInfo);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
