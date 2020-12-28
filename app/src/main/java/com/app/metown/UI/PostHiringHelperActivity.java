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

public class PostHiringHelperActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtJobKeyword, txtSalary, txtPhoto, txtWorkingTime, txtDescription, txtSetTime, txtDone;
    LinearLayout SelectJobKeywordLayout;
    RecyclerView SelectCategoryView;
    ArrayList<CategoryMainModel> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_hiring_helper);

        Log.e("Activity","PostHiringHelperActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtJobKeyword = findViewById(R.id.txtJobKeyword);
        txtSalary = findViewById(R.id.txtSalary);
        txtPhoto = findViewById(R.id.txtPhoto);
        txtWorkingTime = findViewById(R.id.txtWorkingTime);
        txtDescription = findViewById(R.id.txtDescription);

        String JobKeyword = "<font color='#000000'>Job Keyword</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtJobKeyword.setText(Html.fromHtml(JobKeyword));

        String Salary = "<font color='#000000'>Salary</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtSalary.setText(Html.fromHtml(Salary));

        String Photo = "<font color='#000000'>Photos</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtPhoto.setText(Html.fromHtml(Photo));

        String WorkingTime = "<font color='#000000'>Working Time</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtWorkingTime.setText(Html.fromHtml(WorkingTime));

        String Description = "<font color='#000000'>Description</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtDescription.setText(Html.fromHtml(Description));

        SelectJobKeywordLayout = findViewById(R.id.SelectJobKeywordLayout);

        txtSetTime = findViewById(R.id.txtSetTime);
        txtDone = findViewById(R.id.txtDone);

        imgBack.setOnClickListener(this);
        SelectJobKeywordLayout.setOnClickListener(this);
        txtSetTime.setOnClickListener(this);
        txtDone.setOnClickListener(this);
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
            case R.id.SelectJobKeywordLayout:
                OpenSelectCategoryDialog();
                break;
            case R.id.txtSetTime:
                OpenSetTimeDialog();
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

    public void OpenSetTimeDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.set_time_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
