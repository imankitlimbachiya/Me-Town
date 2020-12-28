package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import com.app.metown.Adapters.SelectCategoryMainAdapter;
import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;

import java.util.ArrayList;

public class PostForStoreAndServiceActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtPhoto, txtTitle, txtCategories, txtPrice, txtDescription, txtContactNumber, txtDone;
    LinearLayout SelectCategoryLayout, RangeSettingLayout;
    RelativeLayout LearnAboutLocalAdsLayout;
    RecyclerView SelectCategoryView;
    ArrayList<CategoryMainModel> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_for_store_service);

        Log.e("Activity","PostForStoreAndServiceActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtPhoto = findViewById(R.id.txtPhoto);
        txtTitle = findViewById(R.id.txtTitle);
        txtCategories = findViewById(R.id.txtCategories);
        txtPrice = findViewById(R.id.txtPrice);
        txtDescription = findViewById(R.id.txtDescription);
        txtContactNumber = findViewById(R.id.txtContactNumber);

        String Photo = "<font color='#000000'>Photos</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtPhoto.setText(Html.fromHtml(Photo));

        String Title = "<font color='#000000'>Title</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtTitle.setText(Html.fromHtml(Title));

        String Categories = "<font color='#000000'>Categories</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtCategories.setText(Html.fromHtml(Categories));

        String Price = "<font color='#000000'>Price</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtPrice.setText(Html.fromHtml(Price));

        String Description = "<font color='#000000'>Description</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtDescription.setText(Html.fromHtml(Description));

        String ContactNumber = "<font color='#000000'>Contact number</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtContactNumber.setText(Html.fromHtml(ContactNumber));


        txtDone = findViewById(R.id.txtDone);

        SelectCategoryLayout = findViewById(R.id.SelectCategoryLayout);
        RangeSettingLayout = findViewById(R.id.RangeSettingLayout);
        LearnAboutLocalAdsLayout = findViewById(R.id.LearnAboutLocalAdsLayout);

        imgBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
        SelectCategoryLayout.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtDone:
                Intent Home = new Intent(mContext, HomeActivity.class);
                startActivity(Home);
                finish();
                break;
            case R.id.SelectCategoryLayout:
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.select_category_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                SelectCategoryView = dialog.findViewById(R.id.SelectCategoryView);
                AddSelectCategoryItems();
                /*dialog.findViewById(R.id.UnSelectBarangayLayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });*/
                dialog.show();
                break;
            case R.id.RangeSettingLayout:
                Intent SetRange = new Intent(mContext, SetRangeActivity.class);
                startActivity(SetRange);
                break;
            case R.id.LearnAboutLocalAdsLayout:
                Intent intent = new Intent(mContext, FAQDetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void AddSelectCategoryItems() {
        categoryList.clear();
        for (int i = 1; i <= 20; i++) {
            CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(i), "Item name");
            categoryList.add(categoryMainModel);
        }

        if (categoryList.size() > 0) {
            SelectCategoryMainAdapter selectCategoryMainAdapter = new SelectCategoryMainAdapter(mContext, categoryList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            SelectCategoryView.setLayoutManager(mLayoutManager);
            SelectCategoryView.setItemAnimator(new DefaultItemAnimator());
            SelectCategoryView.setAdapter(selectCategoryMainAdapter);
            selectCategoryMainAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
