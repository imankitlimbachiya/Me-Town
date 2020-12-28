package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AchievementActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgSuperAchievement, imgNormalAchievement;
    TextView txtGoToKnowMore, txtAchievementDescription;
    RecyclerView SuperAchievementView, NormalAchievementView;
    LinearLayout SuperAchievementLayout, NormalAchievementLayout;
    RelativeLayout SuperAchievementViewLayout, NormalAchievementViewLayout;
    ArrayList<CategoryMainModel> superAchievementList = new ArrayList<>();
    ArrayList<CategoryMainModel> normalAchievementList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        Log.e("Activity", "AchievementActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgSuperAchievement = findViewById(R.id.imgSuperAchievement);
        imgNormalAchievement = findViewById(R.id.imgNormalAchievement);

        txtAchievementDescription = findViewById(R.id.txtAchievementDescription);
        txtGoToKnowMore = findViewById(R.id.txtGoToKnowMore);

        txtGoToKnowMore.setPaintFlags(txtGoToKnowMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        SuperAchievementViewLayout = findViewById(R.id.SuperAchievementViewLayout);
        NormalAchievementViewLayout = findViewById(R.id.NormalAchievementViewLayout);

        imgBack.setOnClickListener(this);
        SuperAchievementViewLayout.setOnClickListener(this);
        NormalAchievementViewLayout.setOnClickListener(this);

        SuperAchievementLayout = findViewById(R.id.SuperAchievementLayout);
        NormalAchievementLayout = findViewById(R.id.NormalAchievementLayout);

        SuperAchievementView = findViewById(R.id.SuperAchievementView);
        NormalAchievementView = findViewById(R.id.NormalAchievementView);
    }

    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.SuperAchievementViewLayout:
                if (SuperAchievementView.getVisibility() == View.VISIBLE) {
                    SuperAchievementViewLayout.setBackground(getResources().getDrawable(R.drawable.super_achievement_bg));
                    imgSuperAchievement.setImageDrawable(getResources().getDrawable(R.drawable.bottom_arrow_white));
                    SuperAchievementView.setVisibility(View.GONE);
                } else {
                    SuperAchievementViewLayout.setBackground(getResources().getDrawable(R.drawable.top_full_super_achievement_bg));
                    imgSuperAchievement.setImageDrawable(getResources().getDrawable(R.drawable.top_arrow_white));
                    AddSuperAchievementItems();
                }
                CheckViewVisible();
                break;
            case R.id.NormalAchievementViewLayout:
                if (NormalAchievementView.getVisibility() == View.VISIBLE) {
                    NormalAchievementViewLayout.setBackground(getResources().getDrawable(R.drawable.normal_achievement_bg));
                    imgNormalAchievement.setImageDrawable(getResources().getDrawable(R.drawable.bottom_arrow_white));
                    NormalAchievementView.setVisibility(View.GONE);
                } else {
                    NormalAchievementViewLayout.setBackground(getResources().getDrawable(R.drawable.top_full_normal_achievement_bg));
                    imgNormalAchievement.setImageDrawable(getResources().getDrawable(R.drawable.top_arrow_white));
                    AddNormalAchievementItems();
                }
                CheckViewVisible();
                break;
        }
    }

    public void CheckViewVisible() {
        if (SuperAchievementView.getVisibility() == View.VISIBLE || NormalAchievementView.getVisibility() == View.VISIBLE) {
            txtAchievementDescription.setVisibility(View.GONE);
        } else {
            txtAchievementDescription.setVisibility(View.VISIBLE);
        }
    }

    public void AddSuperAchievementItems() {
        superAchievementList.clear();
        for (int i = 1; i <= 3; i++) {
            CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(i), "Got 15 compliment from other users");
            superAchievementList.add(categoryMainModel);
        }

        if (superAchievementList.size() > 0) {
            SuperAchievementView.setVisibility(View.VISIBLE);
            SuperAchievementAdapter superAchievementAdapter = new SuperAchievementAdapter(mContext, superAchievementList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            SuperAchievementView.setLayoutManager(mLayoutManager);
            SuperAchievementView.setItemAnimator(new DefaultItemAnimator());
            SuperAchievementView.setAdapter(superAchievementAdapter);
            superAchievementAdapter.notifyDataSetChanged();
        }
    }

    public static class SuperAchievementAdapter extends RecyclerView.Adapter<SuperAchievementAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CategoryMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            // RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                // OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public SuperAchievementAdapter(Context mContext, ArrayList<CategoryMainModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.super_achievement_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            CategoryMainModel categoryMainModel = arrayList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AchievementActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public void AddNormalAchievementItems() {
        normalAchievementList.clear();
        for (int i = 1; i <= 3; i++) {
            CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(i), "Got 15 compliment from other users");
            normalAchievementList.add(categoryMainModel);
        }

        if (normalAchievementList.size() > 0) {
            NormalAchievementView.setVisibility(View.VISIBLE);
            NormalAchievementAdapter normalAchievementAdapter = new NormalAchievementAdapter(mContext, normalAchievementList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            NormalAchievementView.setLayoutManager(mLayoutManager);
            NormalAchievementView.setItemAnimator(new DefaultItemAnimator());
            NormalAchievementView.setAdapter(normalAchievementAdapter);
            normalAchievementAdapter.notifyDataSetChanged();
        }
    }

    public static class NormalAchievementAdapter extends RecyclerView.Adapter<NormalAchievementAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CategoryMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            // RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                // OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public NormalAchievementAdapter(Context mContext, ArrayList<CategoryMainModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.normal_achievement_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            CategoryMainModel categoryMainModel = arrayList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AchievementActivity.class);
                    mContext.startActivity(intent);
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
