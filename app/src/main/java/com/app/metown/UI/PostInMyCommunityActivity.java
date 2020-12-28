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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Adapters.SelectCategoryMainAdapter;
import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;

import java.util.ArrayList;

public class PostInMyCommunityActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtChooseTopic, txtChooseKeyword, txtPhoto, txtDescription, txtDone;
    LinearLayout SelectTopicLayout, SelectKeywordLayout, RangeSettingLayout;
    RecyclerView SelectCategoryView;
    ArrayList<CategoryMainModel> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_in_my_community);

        Log.e("Activity","PostInMyCommunityActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtChooseTopic = findViewById(R.id.txtChooseTopic);
        txtChooseKeyword = findViewById(R.id.txtChooseKeyword);
        txtPhoto = findViewById(R.id.txtPhoto);
        txtDescription = findViewById(R.id.txtDescription);

        String ChooseTopic = "<font color='#000000'>Choose topic</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtChooseTopic.setText(Html.fromHtml(ChooseTopic));

        String ChooseKeyword = "<font color='#000000'>Choose Keyword</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtChooseKeyword.setText(Html.fromHtml(ChooseKeyword));

        String Photo = "<font color='#000000'>Photos</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtPhoto.setText(Html.fromHtml(Photo));

        String Description = "<font color='#000000'>Description</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtDescription.setText(Html.fromHtml(Description));

        txtDone = findViewById(R.id.txtDone);

        SelectTopicLayout = findViewById(R.id.SelectTopicLayout);
        SelectKeywordLayout = findViewById(R.id.SelectKeywordLayout);
        RangeSettingLayout = findViewById(R.id.RangeSettingLayout);

        imgBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
        SelectTopicLayout.setOnClickListener(this);
        SelectKeywordLayout.setOnClickListener(this);
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
            case R.id.SelectTopicLayout:
                OpenSelectCategoryDialog();
                break;
            case R.id.SelectKeywordLayout:
                OpenSelectCategoryDialog();
                break;
            case R.id.RangeSettingLayout:
                Intent SetRange = new Intent(mContext, SetRangeActivity.class);
                startActivity(SetRange);
                break;
        }
    }

    public void OpenSelectCategoryDialog() {
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
        super.onBackPressed();
        finish();
    }
}
