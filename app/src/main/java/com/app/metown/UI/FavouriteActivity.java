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
import com.app.metown.Models.CategoryMainModel;
import com.app.metown.Models.ItemMainModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

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
    TextView txtGoToKnowMore, txtViewStoreService;
    RelativeLayout ResponseLayout, JobKeywordResponseLayout, NoResponseLayout;
    RecyclerView FavouriteCategoryView, CategoryFavouriteItemView, JobKeywordView;
    ArrayList<CategoryMainModel> favouriteCategoryList = new ArrayList<>();
    ArrayList<ItemMainModel> categoryFavouriteItemList = new ArrayList<>();
    ArrayList<ItemMainModel> jobKeywordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        Log.e("Activity", "FavouriteActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtGoToKnowMore = findViewById(R.id.txtGoToKnowMore);
        txtViewStoreService = findViewById(R.id.txtViewStoreService);

        ResponseLayout = findViewById(R.id.ResponseLayout);
        JobKeywordResponseLayout = findViewById(R.id.JobKeywordResponseLayout);
        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        imgBack.setOnClickListener(this);

        FavouriteCategoryView = findViewById(R.id.FavouriteCategoryView);
        CategoryFavouriteItemView = findViewById(R.id.CategoryFavouriteItemView);
        JobKeywordView = findViewById(R.id.JobKeywordView);

        AddFavouriteCategoryItems();
        GetFavouriteListApi();
        AddCategoryFavouriteItems("1");
    }

    private void GetFavouriteListApi() {
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
                            // Log.e("HAS_ERROR", " " + HAS_ERROR);
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray SalesAraay=JsonMain.getJSONArray("data");
                                Log.e("SalesAraay", " " + SalesAraay.toString());



                            } else {
                                String msg = JsonMain.getString("msg");

                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                                Log.e("msg", " " + msg);
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_SALE_LIST + params);
                return params;
            }

            /*protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", MobileNumber);
                params.put("type", Type);
                Log.e("PARAMETER", "" + APIConstant.getInstance().CHANGE_MOBILE + params);
                return params;
            }*/

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"type\":\"" + "3" + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SALE_LIST + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_FAVORITE_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
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
                            // Log.e("HAS_ERROR", " " + HAS_ERROR);
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray SalesAraay=JsonMain.getJSONArray("data");
                                Log.e("SalesAraay", " " + SalesAraay.toString());



                            } else {
                                String msg = JsonMain.getString("msg");

                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                                Log.e("msg", " " + msg);
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
                Log.e("HEADER", "" + APIConstant.getInstance().SET_JOB_KEYWORD + params);
                return params;
            }

            /*protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", MobileNumber);
                params.put("type", Type);
                Log.e("PARAMETER", "" + APIConstant.getInstance().CHANGE_MOBILE + params);
                return params;
            }*/

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"keyword\":\"" + "Sofa1" + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().SET_JOB_KEYWORD + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_FAVORITE_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
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

    private void GetJobKeywordListApi() {
        String req = "req";
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
                            // Log.e("HAS_ERROR", " " + HAS_ERROR);
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray JobKeywordArray=JsonMain.getJSONArray("data");
                                Log.e("JOB_KEYWORD", " " + JobKeywordArray.toString());

                                for (int i = 0; i <JobKeywordArray.length(); i++) {
                                    JSONObject jokeyword=JobKeywordArray.getJSONObject(i);
                                    Log.e("JOB_KEYWORD", " " + jokeyword.getString("keyword"));

                                    ItemMainModel itemMainModel = new ItemMainModel(String.valueOf(i), jokeyword.getString("keyword"));
                                    jobKeywordList.add(itemMainModel);
                                }

                                if (jobKeywordList.size() > 0) {
                                    NoResponseLayout.setVisibility(View.GONE);
                                    ResponseLayout.setVisibility(View.GONE);
                                    JobKeywordResponseLayout.setVisibility(View.VISIBLE);
                                    txtGoToKnowMore.setPaintFlags(txtGoToKnowMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
                                Log.e("msg", " " + msg);
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

            /*protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", MobileNumber);
                params.put("type", Type);
                Log.e("PARAMETER", "" + APIConstant.getInstance().CHANGE_MOBILE + params);
                return params;
            }*/

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SALE_LIST + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_FAVORITE_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }


    public void AddFavouriteCategoryItems() {
        favouriteCategoryList.clear();
        CategoryMainModel categoryMainModel;

        categoryMainModel = new CategoryMainModel("1", "Second hand");
        favouriteCategoryList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("2", "Advertising");
        favouriteCategoryList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("3", "Stores & Services");
        favouriteCategoryList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("4", "My Community");
        favouriteCategoryList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("5", "Job Keyword");
        favouriteCategoryList.add(categoryMainModel);

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
        ArrayList<CategoryMainModel> arrayList;
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

        public FavouriteCategoryAdapter(Context mContext, ArrayList<CategoryMainModel> arrayList) {
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
            final CategoryMainModel categoryMainModel = arrayList.get(position);

            holder.txtCategory.setText(categoryMainModel.getCategoryName());

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
                    switch (categoryMainModel.getCategoryName()) {
                        case "Stores & Services":
                        case "My Community":
                            AddCategoryFavouriteItems("0");
                            break;
                        case "Job Keyword":
//                            SetJoBKeywordApi();
                            GetJobKeywordListApi();
                            AddCategoryFavouriteItems("2");
                            break;
                        default:
                            AddCategoryFavouriteItems("1");
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

    public void AddCategoryFavouriteItems(String ResponseAvailable) {

        if (ResponseAvailable.equals("0")) {
            ResponseLayout.setVisibility(View.GONE);
            JobKeywordResponseLayout.setVisibility(View.GONE);
            NoResponseLayout.setVisibility(View.VISIBLE);
            txtViewStoreService.setPaintFlags(txtViewStoreService.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else if (ResponseAvailable.equals("1")) {
            categoryFavouriteItemList.clear();
            for (int i = 1; i <= 10; i++) {
                ItemMainModel itemMainModel = new ItemMainModel(String.valueOf(i), "Item name");
                categoryFavouriteItemList.add(itemMainModel);
            }

            if (categoryFavouriteItemList.size() > 0) {
                NoResponseLayout.setVisibility(View.GONE);
                JobKeywordResponseLayout.setVisibility(View.GONE);
                ResponseLayout.setVisibility(View.VISIBLE);
                CategoryFavouriteItemAdapter categoryFavouriteItemAdapter = new CategoryFavouriteItemAdapter(mContext, categoryFavouriteItemList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                CategoryFavouriteItemView.setLayoutManager(mLayoutManager);
                CategoryFavouriteItemView.setItemAnimator(new DefaultItemAnimator());
                CategoryFavouriteItemView.setAdapter(categoryFavouriteItemAdapter);
                categoryFavouriteItemAdapter.notifyDataSetChanged();
            }
        } else {
            jobKeywordList.clear();
//            for (int i = 1; i <= 10; i++) {
//                ItemMainModel itemMainModel = new ItemMainModel(String.valueOf(i), "Item name");
//                jobKeywordList.add(itemMainModel);
//            }
//
//            if (jobKeywordList.size() > 0) {
//                NoResponseLayout.setVisibility(View.GONE);
//                ResponseLayout.setVisibility(View.GONE);
//                JobKeywordResponseLayout.setVisibility(View.VISIBLE);
//                txtGoToKnowMore.setPaintFlags(txtGoToKnowMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                JobKeywordAdapter jobKeywordAdapter = new JobKeywordAdapter(mContext, jobKeywordList);
//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
//                JobKeywordView.setLayoutManager(mLayoutManager);
//                JobKeywordView.setItemAnimator(new DefaultItemAnimator());
//                JobKeywordView.setAdapter(jobKeywordAdapter);
//                jobKeywordAdapter.notifyDataSetChanged();
//            }
        }
    }

    public static class CategoryFavouriteItemAdapter extends RecyclerView.Adapter<CategoryFavouriteItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {


            MyViewHolder(View view) {
                super(view);

            }
        }

        public CategoryFavouriteItemAdapter(Context mContext, ArrayList<ItemMainModel> arrayList) {
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

    public static class JobKeywordAdapter extends RecyclerView.Adapter<JobKeywordAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtSelect,txtJobKeyword;

            MyViewHolder(View view) {
                super(view);

                txtSelect = view.findViewById(R.id.txtSelect);
                txtJobKeyword = view.findViewById(R.id.txtJobKeyword);

            }
        }

        public JobKeywordAdapter(Context mContext, ArrayList<ItemMainModel> arrayList) {
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
            ItemMainModel itemMainModel = arrayList.get(position);
//            holder.txtSelect.setText(itemMainModel.getItemName());
            holder.txtJobKeyword.setText(itemMainModel.getItemName());
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
