package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserItemReferenceActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    RecyclerView OtherItemByNicknameView, OtherItemView;
    ArrayList<StaticCategoryModel> otherItemByNicknameList = new ArrayList<>();
    ArrayList<StaticCategoryModel> otherItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_item_reference);

        Log.e("Activity", "UserItemReferenceActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(this);

        OtherItemByNicknameView = findViewById(R.id.OtherItemByNicknameView);

        AddOtherItemByNicknameItems();

        OtherItemView = findViewById(R.id.OtherItemView);

        AddOtherItems();
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

    public void AddOtherItemByNicknameItems() {
        otherItemByNicknameList.clear();
        for (int i = 1; i <= 4; i++) {
            StaticCategoryModel staticCategoryModel = new StaticCategoryModel(String.valueOf(i), "Got 15 compliment from other users");
            otherItemByNicknameList.add(staticCategoryModel);
        }

        if (otherItemByNicknameList.size() > 0) {
            OtherItemByNicknameAdapter otherItemByNicknameAdapter = new OtherItemByNicknameAdapter(mContext, otherItemByNicknameList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
            OtherItemByNicknameView.setLayoutManager(mLayoutManager);
            OtherItemByNicknameView.setItemAnimator(new DefaultItemAnimator());
            OtherItemByNicknameView.setAdapter(otherItemByNicknameAdapter);
            otherItemByNicknameAdapter.notifyDataSetChanged();
        }
    }

    public static class OtherItemByNicknameAdapter extends RecyclerView.Adapter<OtherItemByNicknameAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<StaticCategoryModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            // RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                // OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public OtherItemByNicknameAdapter(Context mContext, ArrayList<StaticCategoryModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_item_by_nickname_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            StaticCategoryModel staticCategoryModel = arrayList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SetRangeActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public void AddOtherItems() {
        otherItemList.clear();
        for (int i = 1; i <= 10; i++) {
            StaticCategoryModel staticCategoryModel = new StaticCategoryModel(String.valueOf(i), "Got 15 compliment from other users");
            otherItemList.add(staticCategoryModel);
        }

        if (otherItemList.size() > 0) {
            OtherItemAdapter otherItemAdapter = new OtherItemAdapter(mContext, otherItemList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
            OtherItemView.setLayoutManager(mLayoutManager);
            OtherItemView.setItemAnimator(new DefaultItemAnimator());
            OtherItemView.setAdapter(otherItemAdapter);
            otherItemAdapter.notifyDataSetChanged();
        }
    }

    public static class OtherItemAdapter extends RecyclerView.Adapter<OtherItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<StaticCategoryModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            // RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                // OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public OtherItemAdapter(Context mContext, ArrayList<StaticCategoryModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_item_by_nickname_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            StaticCategoryModel staticCategoryModel = arrayList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
