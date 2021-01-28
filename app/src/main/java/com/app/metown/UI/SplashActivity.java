package com.app.metown.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.app.metown.R;

public class SplashActivity extends AppCompatActivity {

    Context mContext;
    int SPLASH_TIME_OUT = 1000;
    String UserID;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.e("Activity", "SplashActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        UserID = sharedPreferences.getString("UserID", "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (UserID.equals("")) {
                    intent = new Intent(mContext, MainActivity.class);
                } else {
                    intent = new Intent(mContext, HomeActivity.class);
                }
                startActivity(intent);
                finish();

                /*Intent intent = new Intent(mContext, OrganiseMeetUpActivity.class);
                startActivity(intent);
                finish();*/

            }
        }, SPLASH_TIME_OUT);
        super.onResume();
    }
}
