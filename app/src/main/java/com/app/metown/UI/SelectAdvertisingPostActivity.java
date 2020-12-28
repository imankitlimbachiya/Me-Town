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

import androidx.appcompat.app.AppCompatActivity;

import com.app.metown.R;

public class SelectAdvertisingPostActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    Button btnMakeNewPost;
    LinearLayout NextLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_advertising_post);

        Log.e("Activity", "SelectAdvertisingPostActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        btnMakeNewPost = findViewById(R.id.btnMakeNewPost);

        NextLayout = findViewById(R.id.NextLayout);

        imgBack.setOnClickListener(this);
        NextLayout.setOnClickListener(this);
        btnMakeNewPost.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.NextLayout:
                Intent SelectAdvertisingPostDetail = new Intent(mContext, SelectAdvertisingPostDetailActivity.class);
                startActivity(SelectAdvertisingPostDetail);
                break;
            case R.id.btnMakeNewPost:
                Intent ManageBusinessInfo = new Intent(mContext, ManageBusinessInfoActivity.class);
                startActivity(ManageBusinessInfo);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
