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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.ItemMainModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyCommunityActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    LinearLayout MyPostLayout, MyCommentLayout, MyPostResponseLayout, MyCommentResponseLayout;
    TextView txtMyPost, txtMyComment;
    View MyPostView, MyCommentView;
    RecyclerView MyPostItemView, MyCommentItemView;
    ArrayList<ItemMainModel> myPostItemList = new ArrayList<>();
    ArrayList<ItemMainModel> myCommentItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_community);

        Log.e("Activity","MyCommunityActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        MyPostView = findViewById(R.id.MyPostView);
        MyCommentView = findViewById(R.id.MyCommentView);

        MyPostLayout = findViewById(R.id.MyPostLayout);
        MyCommentLayout = findViewById(R.id.MyCommentLayout);
        MyPostResponseLayout = findViewById(R.id.MyPostResponseLayout);
        MyCommentResponseLayout = findViewById(R.id.MyCommentResponseLayout);

        txtMyPost = findViewById(R.id.txtMyPost);
        txtMyComment = findViewById(R.id.txtMyComment);

        txtMyPost.setTextColor(getResources().getColor(R.color.black));
        MyPostView.setBackgroundColor(getResources().getColor(R.color.black));
        txtMyComment.setTextColor(getResources().getColor(R.color.grey));
        MyCommentView.setBackgroundColor(getResources().getColor(R.color.grey));

        imgBack.setOnClickListener(this);
        MyPostLayout.setOnClickListener(this);
        MyCommentLayout.setOnClickListener(this);

        MyPostItemView = findViewById(R.id.MyPostItemView);
        MyCommentItemView = findViewById(R.id.MyCommentItemView);

        MyPostResponseLayout.setVisibility(View.VISIBLE);
        MyCommentResponseLayout.setVisibility(View.GONE);

        AddMyPostItems();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.MyPostLayout:
                txtMyPost.setTextColor(getResources().getColor(R.color.black));
                MyPostView.setBackgroundColor(getResources().getColor(R.color.black));
                txtMyComment.setTextColor(getResources().getColor(R.color.grey));
                MyCommentView.setBackgroundColor(getResources().getColor(R.color.grey));
                MyCommentResponseLayout.setVisibility(View.GONE);
                MyPostResponseLayout.setVisibility(View.VISIBLE);
                AddMyPostItems();
                break;
            case R.id.MyCommentLayout:
                txtMyComment.setTextColor(getResources().getColor(R.color.black));
                MyCommentView.setBackgroundColor(getResources().getColor(R.color.black));
                txtMyPost.setTextColor(getResources().getColor(R.color.grey));
                MyPostView.setBackgroundColor(getResources().getColor(R.color.grey));
                MyPostResponseLayout.setVisibility(View.GONE);
                MyCommentResponseLayout.setVisibility(View.VISIBLE);
                AddMyCommentItems();
                break;
        }
    }

    public void AddMyPostItems() {
        myPostItemList.clear();
        for (int i = 1; i <= 10; i++) {
            ItemMainModel itemMainModel = new ItemMainModel(String.valueOf(i), "Item name");
            myPostItemList.add(itemMainModel);
        }

        if (myPostItemList.size() > 0) {
            MyPostItemAdapter myPostItemAdapter = new MyPostItemAdapter(mContext, myPostItemList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            MyPostItemView.setLayoutManager(mLayoutManager);
            MyPostItemView.setItemAnimator(new DefaultItemAnimator());
            MyPostItemView.setAdapter(myPostItemAdapter);
            myPostItemAdapter.notifyDataSetChanged();
        }
    }

    public void AddMyCommentItems() {
        myCommentItemList.clear();
        for (int i = 1; i <= 10; i++) {
            ItemMainModel itemMainModel = new ItemMainModel(String.valueOf(i), "Item name");
            myCommentItemList.add(itemMainModel);
        }

        if (myCommentItemList.size() > 0) {
            MyCommentItemAdapter myCommentItemAdapter = new MyCommentItemAdapter(mContext, myCommentItemList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            MyCommentItemView.setLayoutManager(mLayoutManager);
            MyCommentItemView.setItemAnimator(new DefaultItemAnimator());
            MyCommentItemView.setAdapter(myCommentItemAdapter);
            myCommentItemAdapter.notifyDataSetChanged();
        }
    }

    public static class MyPostItemAdapter extends RecyclerView.Adapter<MyPostItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            // RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                // OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public MyPostItemAdapter(Context mContext, ArrayList<ItemMainModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            ItemMainModel itemMainModel = arrayList.get(position);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public static class MyCommentItemAdapter extends RecyclerView.Adapter<MyCommentItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public MyCommentItemAdapter(Context mContext, ArrayList<ItemMainModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_comment_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            ItemMainModel itemMainModel = arrayList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent TopicList = new Intent(mContext, TopicListActivity.class);
                    mContext.startActivity(TopicList);
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
