package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FollowerActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    TextView txtFindMore;
    ImageView imgBack;
    RelativeLayout ResponseLayout, NoResponseLayout;
    RecyclerView FollowerView;
    ArrayList<StaticCategoryModel> followerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        Log.e("Activity", "FollowerActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        ResponseLayout = findViewById(R.id.ResponseLayout);
        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        txtFindMore = findViewById(R.id.txtFindMore);

        txtFindMore.setPaintFlags(txtFindMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        imgBack.setOnClickListener(this);

        FollowerView = findViewById(R.id.FollowerView);

        AddFollowerItems();
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

    public void AddFollowerItems() {
        if ((APIConstant.getInstance().ISConstant % 2) == 0) {
            // number is even
            ResponseLayout.setVisibility(View.GONE);
            NoResponseLayout.setVisibility(View.VISIBLE);
        } else {
            // number is odd
            NoResponseLayout.setVisibility(View.GONE);
            ResponseLayout.setVisibility(View.VISIBLE);

            followerList.clear();
            for (int i = 1; i <= 10; i++) {
                StaticCategoryModel staticCategoryModel = new StaticCategoryModel(String.valueOf(i), "");
                followerList.add(staticCategoryModel);
            }

            if (followerList.size() > 0) {
                FollowerAdapter followerAdapter = new FollowerAdapter(mContext, followerList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                FollowerView.setLayoutManager(mLayoutManager);
                FollowerView.setItemAnimator(new DefaultItemAnimator());
                FollowerView.setAdapter(followerAdapter);
                followerAdapter.notifyDataSetChanged();
            }
        }

        APIConstant.getInstance().ISConstant = APIConstant.getInstance().ISConstant + 1;
    }

    public static class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<StaticCategoryModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtAttributeName;

            MyViewHolder(View view) {
                super(view);

                txtAttributeName = view.findViewById(R.id.txtAttributeName);
            }
        }

        public FollowerAdapter(Context mContext, ArrayList<StaticCategoryModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_adapter, parent, false);
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
