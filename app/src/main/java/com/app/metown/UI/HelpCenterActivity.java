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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.ItemMainModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpCenterActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtInquire;
    RecyclerView AttributeView, QuestionView;
    ArrayList<ItemMainModel> attributeList = new ArrayList<>();
    ArrayList<ItemMainModel> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);

        Log.e("Activity", "HelpCenterActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtInquire = findViewById(R.id.txtInquire);

        imgBack.setOnClickListener(this);
        txtInquire.setOnClickListener(this);

        AttributeView = findViewById(R.id.AttributeView);

        AddAttributeItems();

        QuestionView = findViewById(R.id.QuestionView);

        AddQuestionItems();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtInquire:
                if ((APIConstant.getInstance().ISConstant % 2) == 0) {
                    // number is even
                    Intent intent = new Intent(mContext, InquireActivity.class);
                    startActivity(intent);
                } else {
                    // number is odd
                    Intent intent = new Intent(mContext, InquireInformationActivity.class);
                    startActivity(intent);
                }
                APIConstant.getInstance().ISConstant = APIConstant.getInstance().ISConstant + 1;
                break;
        }
    }

    public void AddAttributeItems() {
        attributeList.clear();
        ItemMainModel itemMainModel;

        itemMainModel = new ItemMainModel("1", "Operation\nPolicy");
        attributeList.add(itemMainModel);

        itemMainModel = new ItemMainModel("2", "Account\n/ Verification");
        attributeList.add(itemMainModel);

        itemMainModel = new ItemMainModel("3", "Buy / Sell");
        attributeList.add(itemMainModel);

        itemMainModel = new ItemMainModel("4", "Items");
        attributeList.add(itemMainModel);

        itemMainModel = new ItemMainModel("5", "Trade Manners");
        attributeList.add(itemMainModel);

        itemMainModel = new ItemMainModel("6", "Event\n/ Invitation");
        attributeList.add(itemMainModel);

        itemMainModel = new ItemMainModel("7", "Sanction");
        attributeList.add(itemMainModel);

        itemMainModel = new ItemMainModel("8", "Area\nadvertisement");
        attributeList.add(itemMainModel);

        itemMainModel = new ItemMainModel("9", "ETC");
        attributeList.add(itemMainModel);

        itemMainModel = new ItemMainModel("10", "Commercial\nBusiness");
        attributeList.add(itemMainModel);

        itemMainModel = new ItemMainModel("10", "");
        attributeList.add(itemMainModel);

        itemMainModel = new ItemMainModel("10", "");
        attributeList.add(itemMainModel);

        if (attributeList.size() > 0) {
            AttributeAdapter attributeAdapter = new AttributeAdapter(mContext, attributeList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 3, RecyclerView.VERTICAL, false);
            AttributeView.setLayoutManager(mLayoutManager);
            AttributeView.setItemAnimator(new DefaultItemAnimator());
            AttributeView.setAdapter(attributeAdapter);
            attributeAdapter.notifyDataSetChanged();
        }
    }

    public static class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtAttributeName;

            MyViewHolder(View view) {
                super(view);

                txtAttributeName = view.findViewById(R.id.txtAttributeName);
            }
        }

        public AttributeAdapter(Context mContext, ArrayList<ItemMainModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attribute_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            ItemMainModel itemMainModel = arrayList.get(position);

            String AttributeName = itemMainModel.getItemName();
            if (AttributeName.equals("") || AttributeName.equals("null") || AttributeName.equals(null) || AttributeName == null) {
                holder.txtAttributeName.setText("");
            } else {
                holder.txtAttributeName.setText(itemMainModel.getItemName());
            }

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

    public void AddQuestionItems() {
        questionList.clear();
        for (int i = 1; i <= 10; i++) {
            ItemMainModel itemMainModel = new ItemMainModel("1", "Operation\nPolicy");
            questionList.add(itemMainModel);
        }

        if (attributeList.size() > 0) {
            QuestionAdapter questionAdapter = new QuestionAdapter(mContext, questionList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            QuestionView.setLayoutManager(mLayoutManager);
            QuestionView.setItemAnimator(new DefaultItemAnimator());
            QuestionView.setAdapter(questionAdapter);
            questionAdapter.notifyDataSetChanged();
        }
    }

    public static class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtAttributeName;

            MyViewHolder(View view) {
                super(view);

                txtAttributeName = view.findViewById(R.id.txtAttributeName);
            }
        }

        public QuestionAdapter(Context mContext, ArrayList<ItemMainModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            ItemMainModel itemMainModel = arrayList.get(position);

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
