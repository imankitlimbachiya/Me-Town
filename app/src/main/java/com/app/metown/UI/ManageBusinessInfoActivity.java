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

public class ManageBusinessInfoActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    RelativeLayout GeneralInfoLayout, ServiceKindLayout, NotificationLayout, AdditionalInfoLayout, PriceLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_business_info);

        Log.e("Activity","ManageBusinessInfoActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        GeneralInfoLayout = findViewById(R.id.GeneralInfoLayout);
        AdditionalInfoLayout = findViewById(R.id.AdditionalInfoLayout);
        ServiceKindLayout = findViewById(R.id.ServiceKindLayout);
        PriceLayout = findViewById(R.id.PriceLayout);
        NotificationLayout = findViewById(R.id.NotificationLayout);

        imgBack.setOnClickListener(this);
        GeneralInfoLayout.setOnClickListener(this);
        AdditionalInfoLayout.setOnClickListener(this);
        ServiceKindLayout.setOnClickListener(this);
        PriceLayout.setOnClickListener(this);
        NotificationLayout.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.PriceLayout:
                Intent Price = new Intent(mContext, PriceActivity.class);
                startActivity(Price);
                break;
            case R.id.GeneralInfoLayout:
                Intent RegisterMerchant = new Intent(mContext, RegisterMerchantActivity.class);
                startActivity(RegisterMerchant);
                break;
            case R.id.ServiceKindLayout:
                Intent ServiceKind = new Intent(mContext, ServiceKindActivity.class);
                startActivity(ServiceKind);
                break;
            case R.id.AdditionalInfoLayout:
                Intent AdditionalInfo = new Intent(mContext, AdditionalInfoActivity.class);
                startActivity(AdditionalInfo);
                break;
            case R.id.NotificationLayout:
                Intent NotificationNews = new Intent(mContext, NotificationNewsActivity.class);
                startActivity(NotificationNews);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
