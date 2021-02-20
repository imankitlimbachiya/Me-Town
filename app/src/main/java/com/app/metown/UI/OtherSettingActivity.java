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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.metown.R;

public class OtherSettingActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtVerify;
    RelativeLayout ChangePhoneNumberLayout, ChangeTownOfPostLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_setting);

        Log.e("Activity", "OtherSettingActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtVerify = findViewById(R.id.txtVerify);

        ChangePhoneNumberLayout = findViewById(R.id.ChangePhoneNumberLayout);
        ChangeTownOfPostLayout = findViewById(R.id.ChangeTownOfPostLayout);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        txtVerify.setOnClickListener(this);
        ChangePhoneNumberLayout.setOnClickListener(this);
        ChangeTownOfPostLayout.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.ChangePhoneNumberLayout:
                Intent intent = new Intent(mContext, ChangePhoneNumberActivity.class);
                startActivity(intent);
                break;
            case R.id.ChangeTownOfPostLayout:
                Intent ChangeTownPost = new Intent(mContext, ChangeTownPostActivity.class);
                startActivity(ChangeTownPost);
                break;
            case R.id.txtVerify:
                Intent EmailVerify = new Intent(mContext, EmailVerifyActivity.class);
                startActivity(EmailVerify);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
