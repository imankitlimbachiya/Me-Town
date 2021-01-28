package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.Adapters.KeywordSearchResultAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreAndServiceSearchActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    TextView txtGoCheck;
    ImageView imgBack;
    RecyclerView KeywordSearchResultView;
    ArrayList<StaticCategoryModel> keywordSearchResultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_and_service_search);

        Log.e("Activity","StoreAndServiceSearchActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtGoCheck = findViewById(R.id.txtGoCheck);

        txtGoCheck.setPaintFlags(txtGoCheck.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        imgBack.setOnClickListener(this);

        KeywordSearchResultView = findViewById(R.id.KeywordSearchResultView);

        AddKeywordSearchResultItems();
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

    public void AddKeywordSearchResultItems() {
        keywordSearchResultList.clear();
        for (int i = 1; i <= 4; i++) {
            StaticCategoryModel staticCategoryModel = new StaticCategoryModel(String.valueOf(i), "Item name");
            keywordSearchResultList.add(staticCategoryModel);
        }

        if (keywordSearchResultList.size() > 0) {
            KeywordSearchResultAdapter secondHandSearchItemAdapter = new KeywordSearchResultAdapter(mContext, keywordSearchResultList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            KeywordSearchResultView.setLayoutManager(mLayoutManager);
            KeywordSearchResultView.setItemAnimator(new DefaultItemAnimator());
            KeywordSearchResultView.setAdapter(secondHandSearchItemAdapter);
            secondHandSearchItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
