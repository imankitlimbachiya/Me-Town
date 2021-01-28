package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AnnouncementActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    RecyclerView AnnouncementView;
    ArrayList<StaticCategoryModel> announcementViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        Log.e("Activity", "AchievementActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(this);

        AnnouncementView = findViewById(R.id.AnnouncementView);

        AddAnnouncementItems();
    }

    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    public void AddAnnouncementItems() {
        announcementViewList.clear();
        for (int i = 1; i <= 10; i++) {
            StaticCategoryModel staticCategoryModel = new StaticCategoryModel(String.valueOf(i), "Got 15 compliment from other users");
            announcementViewList.add(staticCategoryModel);
        }

        if (announcementViewList.size() > 0) {
            AnnouncementView.setVisibility(View.VISIBLE);
            AnnouncementAdapter announcementAdapter = new AnnouncementAdapter(mContext, announcementViewList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            AnnouncementView.setLayoutManager(mLayoutManager);
            AnnouncementView.setItemAnimator(new DefaultItemAnimator());
            AnnouncementView.setAdapter(announcementAdapter);
            announcementAdapter.notifyDataSetChanged();
        }
    }

    public static class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<StaticCategoryModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            // RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                // OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public AnnouncementAdapter(Context mContext, ArrayList<StaticCategoryModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            StaticCategoryModel staticCategoryModel = arrayList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent intent = new Intent(mContext, SettingActivity.class);
                    mContext.startActivity(intent);*/
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
