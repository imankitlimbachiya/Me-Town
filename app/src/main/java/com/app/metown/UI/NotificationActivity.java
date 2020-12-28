package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Adapters.KeywordAlertNotificationAdapter;
import com.app.metown.Adapters.NotificationAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.NotificationModel;
import com.app.metown.R;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgEdit;
    TextView txtActivity, txtKeywordAlert, txtAddKeywordAlert;
    RecyclerView NotificationView, KeywordAlertNotificationView;
    ArrayList<NotificationModel> notificationList = new ArrayList<>();
    ArrayList<NotificationModel> keywordAlertNotificationList = new ArrayList<>();
    LinearLayout ActivityLayout, KeywordAlertLayout, ResponseLayout;
    RelativeLayout NoResponseLayout;
    View ActivityView, KeywordAlertsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Log.e("Activity","NotificationActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgEdit = findViewById(R.id.imgEdit);

        ActivityView = findViewById(R.id.ActivityView);
        KeywordAlertsView = findViewById(R.id.KeywordAlertsView);

        txtActivity = findViewById(R.id.txtActivity);
        txtKeywordAlert = findViewById(R.id.txtKeywordAlert);
        txtAddKeywordAlert = findViewById(R.id.txtAddKeywordAlert);

        txtActivity.setTextColor(getResources().getColor(R.color.black));
        ActivityView.setBackgroundColor(getResources().getColor(R.color.black));
        txtKeywordAlert.setTextColor(getResources().getColor(R.color.grey));
        KeywordAlertsView.setBackgroundColor(getResources().getColor(R.color.grey));

        ActivityLayout = findViewById(R.id.ActivityLayout);
        KeywordAlertLayout = findViewById(R.id.KeywordAlertLayout);
        ResponseLayout = findViewById(R.id.ResponseLayout);
        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        imgBack.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        ActivityLayout.setOnClickListener(this);
        KeywordAlertLayout.setOnClickListener(this);
        txtAddKeywordAlert.setOnClickListener(this);

        NotificationView = findViewById(R.id.NotificationView);
        KeywordAlertNotificationView = findViewById(R.id.KeywordAlertNotificationView);

        AddNotificationItems();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.ActivityLayout:
                txtActivity.setTextColor(getResources().getColor(R.color.black));
                ActivityView.setBackgroundColor(getResources().getColor(R.color.black));
                txtKeywordAlert.setTextColor(getResources().getColor(R.color.grey));
                KeywordAlertsView.setBackgroundColor(getResources().getColor(R.color.grey));
                AddNotificationItems();
                break;
            case R.id.KeywordAlertLayout:
                txtKeywordAlert.setTextColor(getResources().getColor(R.color.black));
                KeywordAlertsView.setBackgroundColor(getResources().getColor(R.color.black));
                txtActivity.setTextColor(getResources().getColor(R.color.grey));
                ActivityView.setBackgroundColor(getResources().getColor(R.color.grey));
                AddKeywordAlertNotificationItems();
                break;
            case R.id.imgEdit:
                GoToKeywordAlertActivity();
                break;
            case R.id.txtAddKeywordAlert:
                GoToKeywordAlertActivity();
                break;
        }
    }

    public void GoToKeywordAlertActivity() {
        Intent KeywordAlert = new Intent(mContext, KeywordAlertActivity.class);
        startActivity(KeywordAlert);
    }

    public void AddNotificationItems() {
        NoResponseLayout.setVisibility(View.GONE);
        ResponseLayout.setVisibility(View.VISIBLE);

        imgEdit.setVisibility(View.GONE);
        KeywordAlertNotificationView.setVisibility(View.GONE);
        NotificationView.setVisibility(View.VISIBLE);

        notificationList.clear();
        for (int i = 1; i <= 10; i++) {
            NotificationModel notificationModel = new NotificationModel(String.valueOf(i), "Notification comes here");
            notificationList.add(notificationModel);
        }

        if (notificationList.size() > 0) {
            NoResponseLayout.setVisibility(View.GONE);
            ResponseLayout.setVisibility(View.VISIBLE);
            NotificationAdapter notificationAdapter = new NotificationAdapter(mContext, notificationList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            NotificationView.setLayoutManager(mLayoutManager);
            NotificationView.setItemAnimator(new DefaultItemAnimator());
            NotificationView.setAdapter(notificationAdapter);
            notificationAdapter.notifyDataSetChanged();
        } else {
            ResponseLayout.setVisibility(View.GONE);
            NoResponseLayout.setVisibility(View.VISIBLE);
        }
    }

    public void AddKeywordAlertNotificationItems() {
        if ((APIConstant.getInstance().ISConstant % 2) == 0) {
            // number is even
            ResponseLayout.setVisibility(View.GONE);
            NoResponseLayout.setVisibility(View.VISIBLE);
        } else {
            // number is odd
            NoResponseLayout.setVisibility(View.GONE);
            ResponseLayout.setVisibility(View.VISIBLE);
            imgEdit.setVisibility(View.VISIBLE);
            NotificationView.setVisibility(View.GONE);
            KeywordAlertNotificationView.setVisibility(View.VISIBLE);

            keywordAlertNotificationList.clear();
            for (int i = 1; i <= 10; i++) {
                NotificationModel notificationModel = new NotificationModel(String.valueOf(i), "Notification comes here");
                keywordAlertNotificationList.add(notificationModel);
            }

            if (keywordAlertNotificationList.size() > 0) {
                NoResponseLayout.setVisibility(View.GONE);
                ResponseLayout.setVisibility(View.VISIBLE);
                KeywordAlertNotificationAdapter keywordAlertNotificationAdapter = new KeywordAlertNotificationAdapter(mContext, keywordAlertNotificationList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                KeywordAlertNotificationView.setLayoutManager(mLayoutManager);
                KeywordAlertNotificationView.setItemAnimator(new DefaultItemAnimator());
                KeywordAlertNotificationView.setAdapter(keywordAlertNotificationAdapter);
                keywordAlertNotificationAdapter.notifyDataSetChanged();
            } else {
                ResponseLayout.setVisibility(View.GONE);
                NoResponseLayout.setVisibility(View.VISIBLE);
            }
        }

        APIConstant.getInstance().ISConstant = APIConstant.getInstance().ISConstant + 1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
