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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.ItemMainModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TopicListActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    Button btnTopicLookingFor;
    RecyclerView TopicItemView;
    ArrayList<ItemMainModel> topicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);

        Log.e("Activity","TopicListActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        btnTopicLookingFor = findViewById(R.id.btnTopicLookingFor);

        imgBack.setOnClickListener(this);
        btnTopicLookingFor.setOnClickListener(this);

        TopicItemView = findViewById(R.id.TopicItemView);

        AddTopicItems();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnTopicLookingFor:
                Intent intent = new Intent(mContext, AddTopicActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void AddTopicItems() {
        topicList.clear();
        for (int i = 1; i <= 10; i++) {
            ItemMainModel itemMainModel = new ItemMainModel(String.valueOf(i), "Item name");
            topicList.add(itemMainModel);
        }

        if (topicList.size() > 0) {
            TopicItemAdapter topicItemAdapter = new TopicItemAdapter(mContext, topicList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            TopicItemView.setLayoutManager(mLayoutManager);
            TopicItemView.setItemAnimator(new DefaultItemAnimator());
            TopicItemView.setAdapter(topicItemAdapter);
            topicItemAdapter.notifyDataSetChanged();
        }
    }

    public static class TopicItemAdapter extends RecyclerView.Adapter<TopicItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            // RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                // OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public TopicItemAdapter(Context mContext, ArrayList<ItemMainModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            ItemMainModel itemMainModel = arrayList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent AddTopic = new Intent(mContext, AddTopicActivity.class);
                    mContext.startActivity(AddTopic);
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
