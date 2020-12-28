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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.metown.R;

public class CheckAccountActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    Button btnAccountOwner;
    ImageView imgBack;
    TextView txtCreateNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_account);

        Log.e("Activity","CheckAccountActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        btnAccountOwner = findViewById(R.id.btnAccountOwner);

        txtCreateNewAccount = findViewById(R.id.txtCreateNewAccount);

        imgBack.setOnClickListener(this);
        btnAccountOwner.setOnClickListener(this);
        txtCreateNewAccount.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnAccountOwner:
                Intent Filter = new Intent(mContext, SetLocationActivity.class);
                startActivity(Filter);
                break;
            case R.id.txtCreateNewAccount:
                Intent PhoneVerify = new Intent(mContext, PhoneVerifyActivity.class);
                startActivity(PhoneVerify);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
