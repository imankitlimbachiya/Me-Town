package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.JobKeywordModel;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.Models.ItemMainModel;
import com.app.metown.Models.ItemModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavouriteActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtGoToKnowMore, txtError, txtViewStoreService;
    LinearLayout ResponseLayout;
    RelativeLayout FavouriteItemResponseLayout, JobKeywordResponseLayout, NoResponseLayout;
    RecyclerView FavouriteCategoryView, CategoryFavouriteItemView, JobKeywordView;
    ArrayList<StaticCategoryModel> favouriteCategoryList = new ArrayList<>();
    ArrayList<ItemModel> categoryFavouriteItemList = new ArrayList<>();
    ArrayList<JobKeywordModel> jobKeywordList = new ArrayList<>();
    JSONArray arrayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        Log.e("Activity", "FavouriteActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        AddFavouriteCategoryItems();

        GetFavouriteListApi("1");

        // AddCategoryFavouriteItems("1");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtGoToKnowMore = findViewById(R.id.txtGoToKnowMore);
        txtError = findViewById(R.id.txtError);
        txtViewStoreService = findViewById(R.id.txtViewStoreService);

        ResponseLayout = findViewById(R.id.ResponseLayout);

        FavouriteItemResponseLayout = findViewById(R.id.FavouriteItemResponseLayout);
        JobKeywordResponseLayout = findViewById(R.id.JobKeywordResponseLayout);
        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        FavouriteCategoryView = findViewById(R.id.FavouriteCategoryView);
        CategoryFavouriteItemView = findViewById(R.id.CategoryFavouriteItemView);
        JobKeywordView = findViewById(R.id.JobKeywordView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
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

    public void AddFavouriteCategoryItems() {
        favouriteCategoryList.clear();

        StaticCategoryModel staticCategoryModel;

        staticCategoryModel = new StaticCategoryModel("1", "Second hand");
        favouriteCategoryList.add(staticCategoryModel);

        staticCategoryModel = new StaticCategoryModel("2", "Advertising");
        favouriteCategoryList.add(staticCategoryModel);

        staticCategoryModel = new StaticCategoryModel("3", "Stores & Services");
        favouriteCategoryList.add(staticCategoryModel);

        staticCategoryModel = new StaticCategoryModel("4", "My Community");
        favouriteCategoryList.add(staticCategoryModel);

        staticCategoryModel = new StaticCategoryModel("5", "Job Keyword");
        favouriteCategoryList.add(staticCategoryModel);

        if (favouriteCategoryList.size() > 0) {
            FavouriteCategoryAdapter favouriteCategoryAdapter = new FavouriteCategoryAdapter(mContext, favouriteCategoryList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            FavouriteCategoryView.setLayoutManager(mLayoutManager);
            FavouriteCategoryView.setItemAnimator(new DefaultItemAnimator());
            FavouriteCategoryView.setAdapter(favouriteCategoryAdapter);
            favouriteCategoryAdapter.notifyDataSetChanged();
        }
    }

    public class FavouriteCategoryAdapter extends RecyclerView.Adapter<FavouriteCategoryAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<StaticCategoryModel> arrayList;
        int pos = 0;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtCategory;
            View CategoryView;

            MyViewHolder(View view) {
                super(view);

                txtCategory = view.findViewById(R.id.txtCategory);

                CategoryView = view.findViewById(R.id.CategoryView);
            }
        }

        public FavouriteCategoryAdapter(Context mContext, ArrayList<StaticCategoryModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_category_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, final int position) {
            final StaticCategoryModel staticCategoryModel = arrayList.get(position);

            holder.txtCategory.setText(staticCategoryModel.getCategoryName());

            if (pos == position) {
                holder.txtCategory.setTextColor(Color.parseColor("#000000"));
                holder.CategoryView.setBackgroundColor(Color.parseColor("#000000"));
            } else {
                holder.txtCategory.setTextColor(Color.parseColor("#AFAFAF"));
                holder.CategoryView.setBackgroundColor(Color.parseColor("#AFAFAF"));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (staticCategoryModel.getCategoryName()) {
                        case "Second hand":
                            GetFavouriteListApi("1");
                            break;
                        case "Advertising":
                            GetFavouriteListApi("2");
                            break;
                        case "Stores & Services":
                            GetFavouriteListApi("3");
                            break;
                        case "My Community":
                            GetFavouriteListApi("4");
                            break;
                        case "Job Keyword":
                            GetJobKeywordListApi();
                            break;
                    }
                    pos = position;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void GetFavouriteListApi(final String Type) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_FAVORITE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_FAVORITE_LIST + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                arrayData = new JSONArray();
                                arrayData = JsonMain.getJSONArray("data");
                                SetArrayData(arrayData);
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                                ResponseLayout.setVisibility(View.GONE);
                                NoResponseLayout.setVisibility(View.VISIBLE);
                                txtError.setText(ErrorMessage);
                                // txtViewStoreService.setPaintFlags(txtViewStoreService.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            }
                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_FAVORITE_LIST + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"type\":\"" + Type + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_FAVORITE_LIST + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_FAVORITE_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void SetArrayData(JSONArray arrayData) {
        categoryFavouriteItemList.clear();
        try {
            for (int i = 0; i < arrayData.length(); i++) {
                ItemModel itemModel = new ItemModel();
                itemModel.setItemID(arrayData.getJSONObject(i).getString("product_id"));
                itemModel.setItemName(arrayData.getJSONObject(i).getString("name"));
                itemModel.setItemImages(arrayData.getJSONObject(i).getString("images"));
                itemModel.setItemType(arrayData.getJSONObject(i).getString("type"));
                itemModel.setItemFavouriteCount(arrayData.getJSONObject(i).getString("favourite_count"));
                categoryFavouriteItemList.add(itemModel);
            }
            if (categoryFavouriteItemList.size() > 0) {
                NoResponseLayout.setVisibility(View.GONE);
                ResponseLayout.setVisibility(View.VISIBLE);
                JobKeywordResponseLayout.setVisibility(View.GONE);
                FavouriteItemResponseLayout.setVisibility(View.VISIBLE);
                CategoryFavouriteItemAdapter categoryFavouriteItemAdapter = new CategoryFavouriteItemAdapter(mContext, categoryFavouriteItemList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                CategoryFavouriteItemView.setLayoutManager(mLayoutManager);
                CategoryFavouriteItemView.setItemAnimator(new DefaultItemAnimator());
                CategoryFavouriteItemView.setAdapter(categoryFavouriteItemAdapter);
                categoryFavouriteItemAdapter.notifyDataSetChanged();
            } else {
                ResponseLayout.setVisibility(View.GONE);
                NoResponseLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static class CategoryFavouriteItemAdapter extends RecyclerView.Adapter<CategoryFavouriteItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgItem;
            TextView txtItemName, txtItemPrice;

            MyViewHolder(View view) {
                super(view);

                imgItem = view.findViewById(R.id.imgItem);

                txtItemName = view.findViewById(R.id.txtItemName);
                txtItemPrice = view.findViewById(R.id.txtItemPrice);
            }
        }

        public CategoryFavouriteItemAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_favourite_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, final int position) {
            ItemModel itemModel = arrayList.get(position);

            String Images = itemModel.getItemImages();
            String[] separated = Images.split(",");
            Glide.with(mContext).load(separated[0]).into(holder.imgItem);

            holder.txtItemName.setText(itemModel.getItemName());
            holder.txtItemPrice.setText(itemModel.getItemPrice());

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

    private void SetJoBKeywordApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().SET_JOB_KEYWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().SET_JOB_KEYWORD + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {

                                }
                            } else {
                                String msg = JsonMain.getString("msg");
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError ", "" + error.toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().SET_JOB_KEYWORD + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().SET_JOB_KEYWORD);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void GetJobKeywordListApi() {
        String req = "req";
        jobKeywordList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().GET_JOB_KEYWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_JOB_KEYWORD + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    JobKeywordModel jobKeywordModel = new JobKeywordModel();
                                    jobKeywordModel.setJobID(arrayData.getJSONObject(i).getString("id"));
                                    jobKeywordModel.setJobKeyword(arrayData.getJSONObject(i).getString("keyword"));
                                    jobKeywordList.add(jobKeywordModel);
                                }
                                if (jobKeywordList.size() > 0) {
                                    NoResponseLayout.setVisibility(View.GONE);
                                    ResponseLayout.setVisibility(View.VISIBLE);
                                    FavouriteItemResponseLayout.setVisibility(View.GONE);
                                    JobKeywordResponseLayout.setVisibility(View.VISIBLE);
                                    JobKeywordAdapter jobKeywordAdapter = new JobKeywordAdapter(mContext, jobKeywordList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    JobKeywordView.setLayoutManager(mLayoutManager);
                                    JobKeywordView.setItemAnimator(new DefaultItemAnimator());
                                    JobKeywordView.setAdapter(jobKeywordAdapter);
                                    jobKeywordAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String msg = JsonMain.getString("msg");
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_JOB_KEYWORD + params);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_JOB_KEYWORD + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_JOB_KEYWORD);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class JobKeywordAdapter extends RecyclerView.Adapter<JobKeywordAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<JobKeywordModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtSelect, txtJobKeyword;

            MyViewHolder(View view) {
                super(view);

                txtSelect = view.findViewById(R.id.txtSelect);
                txtJobKeyword = view.findViewById(R.id.txtJobKeyword);
            }
        }

        public JobKeywordAdapter(Context mContext, ArrayList<JobKeywordModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_keyword_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, final int position) {
            JobKeywordModel jobKeywordModel = arrayList.get(position);

            holder.txtJobKeyword.setText(jobKeywordModel.getJobKeyword());

            holder.txtSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent Announcement = new Intent(mContext, AnnouncementActivity.class);
                    mContext.startActivity(Announcement);
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
