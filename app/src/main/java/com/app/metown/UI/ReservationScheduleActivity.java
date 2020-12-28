package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Adapters.ReservationScheduleAdapter;
import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;

import java.util.ArrayList;

public class ReservationScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    RecyclerView ReservationScheduleView;
    ArrayList<CategoryMainModel> reservationScheduleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_schedule);

        Log.e("Activity","ReservationScheduleActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(this);

        ReservationScheduleView = findViewById(R.id.ReservationScheduleView);

        AddReservationScheduleItems();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    public void AddReservationScheduleItems() {
        reservationScheduleList.clear();
        for (int i = 1; i <= 10; i++) {
            CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(i), "Notification comes here");
            reservationScheduleList.add(categoryMainModel);
        }

        if (reservationScheduleList.size() > 0) {
            ReservationScheduleAdapter reservationScheduleAdapter = new ReservationScheduleAdapter(mContext, reservationScheduleList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            ReservationScheduleView.setLayoutManager(mLayoutManager);
            ReservationScheduleView.setItemAnimator(new DefaultItemAnimator());
            ReservationScheduleView.setAdapter(reservationScheduleAdapter);
            reservationScheduleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
