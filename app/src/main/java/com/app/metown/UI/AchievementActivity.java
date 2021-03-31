package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.app.metown.Models.AchievementModel;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AchievementActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgSuperAchievement, imgNormalAchievement;
    TextView txtGoToKnowMore, txtAchievementDescription;
    RecyclerView SuperAchievementView, NormalAchievementView;
    LinearLayout SuperAchievementLayout, NormalAchievementLayout;
    RelativeLayout SuperAchievementViewLayout, NormalAchievementViewLayout;
    ArrayList<AchievementModel> superAchievementList = new ArrayList<>();
    ArrayList<AchievementModel> normalAchievementList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        Log.e("Activity", "AchievementActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgSuperAchievement = findViewById(R.id.imgSuperAchievement);
        imgNormalAchievement = findViewById(R.id.imgNormalAchievement);

        txtAchievementDescription = findViewById(R.id.txtAchievementDescription);
        txtGoToKnowMore = findViewById(R.id.txtGoToKnowMore);

        txtGoToKnowMore.setPaintFlags(txtGoToKnowMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        SuperAchievementViewLayout = findViewById(R.id.SuperAchievementViewLayout);
        NormalAchievementViewLayout = findViewById(R.id.NormalAchievementViewLayout);

        SuperAchievementLayout = findViewById(R.id.SuperAchievementLayout);
        NormalAchievementLayout = findViewById(R.id.NormalAchievementLayout);

        SuperAchievementView = findViewById(R.id.SuperAchievementView);
        NormalAchievementView = findViewById(R.id.NormalAchievementView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        SuperAchievementViewLayout.setOnClickListener(this);
        NormalAchievementViewLayout.setOnClickListener(this);
    }

    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.SuperAchievementViewLayout:
                if (SuperAchievementView.getVisibility() == View.VISIBLE) {
                    SuperAchievementViewLayout.setBackground(getResources().getDrawable(R.drawable.super_achievement_bg));
                    imgSuperAchievement.setImageDrawable(getResources().getDrawable(R.drawable.bottom_arrow_white));
                    SuperAchievementView.setVisibility(View.GONE);
                } else {
                    SuperAchievementViewLayout.setBackground(getResources().getDrawable(R.drawable.top_full_super_achievement_bg));
                    imgSuperAchievement.setImageDrawable(getResources().getDrawable(R.drawable.top_arrow_white));
                    GetSuperAchievementApi();
                }
                CheckViewVisible();
                break;
            case R.id.NormalAchievementViewLayout:
                if (NormalAchievementView.getVisibility() == View.VISIBLE) {
                    NormalAchievementViewLayout.setBackground(getResources().getDrawable(R.drawable.normal_achievement_bg));
                    imgNormalAchievement.setImageDrawable(getResources().getDrawable(R.drawable.bottom_arrow_white));
                    NormalAchievementView.setVisibility(View.GONE);
                } else {
                    NormalAchievementViewLayout.setBackground(getResources().getDrawable(R.drawable.top_full_normal_achievement_bg));
                    imgNormalAchievement.setImageDrawable(getResources().getDrawable(R.drawable.top_arrow_white));
                    GetNormalAchievementApi();
                }
                CheckViewVisible();
                break;
        }
    }

    public void CheckViewVisible() {
        if (SuperAchievementView.getVisibility() == View.VISIBLE || NormalAchievementView.getVisibility() == View.VISIBLE) {
            txtAchievementDescription.setVisibility(View.GONE);
        } else {
            txtAchievementDescription.setVisibility(View.VISIBLE);
        }
    }

    private void GetSuperAchievementApi() {
        String req = "req";
        superAchievementList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().SUPER_ACHIEVEMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().SUPER_ACHIEVEMENT + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    AchievementModel achievementModel = new AchievementModel();
                                    achievementModel.setTitle(arrayData.getJSONObject(i).getString("title"));
                                    achievementModel.setCriteria(arrayData.getJSONObject(i).getString("criteria"));
                                    achievementModel.setStatus(arrayData.getJSONObject(i).getString("status"));
                                    superAchievementList.add(achievementModel);
                                }
                                if (superAchievementList.size() > 0) {
                                    SuperAchievementView.setVisibility(View.VISIBLE);
                                    SuperAchievementAdapter superAchievementAdapter = new SuperAchievementAdapter(mContext, superAchievementList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    SuperAchievementView.setLayoutManager(mLayoutManager);
                                    SuperAchievementView.setItemAnimator(new DefaultItemAnimator());
                                    SuperAchievementView.setAdapter(superAchievementAdapter);
                                    superAchievementAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
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
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().SUPER_ACHIEVEMENT + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String UserID = sharedPreferences.getString("UserID", "");
                params.put("user_id", UserID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().SUPER_ACHIEVEMENT + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().SUPER_ACHIEVEMENT);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class SuperAchievementAdapter extends RecyclerView.Adapter<SuperAchievementAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<AchievementModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtAchievementTitle;

            MyViewHolder(View view) {
                super(view);

                txtAchievementTitle = view.findViewById(R.id.txtAchievementTitle);
            }
        }

        public SuperAchievementAdapter(Context mContext, ArrayList<AchievementModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.super_achievement_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            AchievementModel achievementModel = arrayList.get(position);

            holder.txtAchievementTitle.setText(achievementModel.getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AchievementActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void GetNormalAchievementApi() {
        String req = "req";
        normalAchievementList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().NORMAL_ACHIEVEMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().NORMAL_ACHIEVEMENT + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    AchievementModel achievementModel = new AchievementModel();
                                    achievementModel.setTitle(arrayData.getJSONObject(i).getString("title"));
                                    achievementModel.setCriteria(arrayData.getJSONObject(i).getString("criteria"));
                                    achievementModel.setStatus(arrayData.getJSONObject(i).getString("status"));
                                    normalAchievementList.add(achievementModel);
                                }
                                if (normalAchievementList.size() > 0) {
                                    NormalAchievementView.setVisibility(View.VISIBLE);
                                    NormalAchievementAdapter normalAchievementAdapter = new NormalAchievementAdapter(mContext, normalAchievementList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    NormalAchievementView.setLayoutManager(mLayoutManager);
                                    NormalAchievementView.setItemAnimator(new DefaultItemAnimator());
                                    NormalAchievementView.setAdapter(normalAchievementAdapter);
                                    normalAchievementAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
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
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().NORMAL_ACHIEVEMENT + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String UserID = sharedPreferences.getString("UserID", "");
                params.put("user_id", UserID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().NORMAL_ACHIEVEMENT + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().NORMAL_ACHIEVEMENT);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class NormalAchievementAdapter extends RecyclerView.Adapter<NormalAchievementAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<AchievementModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtAchievementTitle;

            MyViewHolder(View view) {
                super(view);

                txtAchievementTitle = view.findViewById(R.id.txtAchievementTitle);
            }
        }

        public NormalAchievementAdapter(Context mContext, ArrayList<AchievementModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.normal_achievement_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            AchievementModel achievementModel = arrayList.get(position);

            holder.txtAchievementTitle.setText(achievementModel.getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AchievementActivity.class);
                    mContext.startActivity(intent);
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
