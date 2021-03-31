package com.app.metown.UI;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.app.metown.Models.AnnouncementModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnnouncementActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtError;
    LinearLayout ResponseLayout;
    RelativeLayout NoResponseLayout;
    RecyclerView AnnouncementView;
    ArrayList<AnnouncementModel> announcementViewList = new ArrayList<>();
    String offSet = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        Log.e("Activity", "AchievementActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetAnnouncementListApi(offSet);
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtError = findViewById(R.id.txtError);

        ResponseLayout = findViewById(R.id.ResponseLayout);
        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        AnnouncementView = findViewById(R.id.AnnouncementView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    private void GetAnnouncementListApi(final String offSet) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_ANNOUNCEMENT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().MERCHANT_COUPON_LIST + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            AnnouncementModel announcementModel = new AnnouncementModel();
                            announcementModel.setID(arrayData.getJSONObject(i).getString("id"));
                            announcementModel.setTitle(arrayData.getJSONObject(i).getString("title"));
                            announcementModel.setDetail(arrayData.getJSONObject(i).getString("detail"));
                            announcementModel.setCreatedAt(arrayData.getJSONObject(i).getString("created_at"));
                            announcementViewList.add(announcementModel);
                        }
                        if (announcementViewList.size() > 0) {
                            NoResponseLayout.setVisibility(View.GONE);
                            ResponseLayout.setVisibility(View.VISIBLE);
                            AnnouncementView.setVisibility(View.VISIBLE);
                            AnnouncementAdapter announcementAdapter = new AnnouncementAdapter(mContext, announcementViewList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            AnnouncementView.setLayoutManager(mLayoutManager);
                            AnnouncementView.setItemAnimator(new DefaultItemAnimator());
                            AnnouncementView.setAdapter(announcementAdapter);
                            announcementAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                        ResponseLayout.setVisibility(View.GONE);
                        NoResponseLayout.setVisibility(View.VISIBLE);
                        txtError.setVisibility(View.VISIBLE);
                        txtError.setText(ErrorMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
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
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().MERCHANT_COUPON_LIST + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("offset", offSet);
                Log.e("PARAMETER", "" + APIConstant.getInstance().MERCHANT_COUPON_LIST + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MERCHANT_COUPON_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<AnnouncementModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtTimeAgo, txtAnnouncementTitle;

            MyViewHolder(View view) {
                super(view);

                txtTimeAgo = view.findViewById(R.id.txtTimeAgo);
                txtAnnouncementTitle = view.findViewById(R.id.txtAnnouncementTitle);
            }
        }

        public AnnouncementAdapter(Context mContext, ArrayList<AnnouncementModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            AnnouncementModel announcementModel = arrayList.get(position);

            holder.txtTimeAgo.setText(announcementModel.getCreatedAt());
            holder.txtAnnouncementTitle.setText(announcementModel.getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent intent = new Intent(mContext, SettingActivity.class);
                    mContext.startActivity(intent);*/
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