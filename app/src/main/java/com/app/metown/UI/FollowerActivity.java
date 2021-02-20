package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.app.metown.Models.FollowModel;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowerActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    TextView txtFindMore;
    ImageView imgBack;
    RelativeLayout ResponseLayout, NoResponseLayout;
    RecyclerView FollowerView;
    ArrayList<FollowModel> followerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        Log.e("Activity", "FollowerActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetFollowUserApi();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        ResponseLayout = findViewById(R.id.ResponseLayout);
        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        txtFindMore = findViewById(R.id.txtFindMore);
        txtFindMore.setPaintFlags(txtFindMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        FollowerView = findViewById(R.id.FollowerView);
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

    private void GetFollowUserApi() {
        String req = "req";
        followerList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_FOLLOW_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_FOLLOW_USER + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    FollowModel followModel = new FollowModel();
                                    followModel.setID(arrayData.getJSONObject(i).getString("id"));
                                    followModel.setFollower(arrayData.getJSONObject(i).getString("follower"));
                                    followModel.setFollowing(arrayData.getJSONObject(i).getString("following"));
                                    followModel.setNickName(arrayData.getJSONObject(i).getJSONObject("user_detail").getString("nick_name"));
                                    followModel.setProfilePicture(arrayData.getJSONObject(i).getJSONObject("user_detail").getString("profile_pic"));
                                    followerList.add(followModel);
                                }
                                if (followerList.size() > 0) {
                                    NoResponseLayout.setVisibility(View.GONE);
                                    ResponseLayout.setVisibility(View.VISIBLE);
                                    FollowerAdapter followerAdapter = new FollowerAdapter(mContext, followerList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    FollowerView.setLayoutManager(mLayoutManager);
                                    FollowerView.setItemAnimator(new DefaultItemAnimator());
                                    FollowerView.setAdapter(followerAdapter);
                                    followerAdapter.notifyDataSetChanged();
                                } else {
                                    ResponseLayout.setVisibility(View.GONE);
                                    NoResponseLayout.setVisibility(View.VISIBLE);
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", mContext.MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_FOLLOW_USER + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_FOLLOW_USER);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<FollowModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            CircleImageView imgFollower;
            TextView txtNickName, txtAddress;
            Button btnSelect;

            MyViewHolder(View view) {
                super(view);

                imgFollower = view.findViewById(R.id.imgFollower);

                txtNickName = view.findViewById(R.id.txtNickName);
                txtAddress = view.findViewById(R.id.txtAddress);

                btnSelect = view.findViewById(R.id.btnSelect);
            }
        }

        public FollowerAdapter(Context mContext, ArrayList<FollowModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final FollowModel followModel = arrayList.get(position);

            Glide.with(mContext).load(followModel.getProfilePicture()).into(holder.imgFollower);
            holder.txtNickName.setText(followModel.getNickName());

            holder.btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FollowUserApi(followModel.getID());
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void FollowUserApi(final String ID) {
        String req = "req";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().FOLLOW_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            Log.e("RESPONSE", "" + APIConstant.getInstance().FOLLOW_USER + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                                GetFollowUserApi();
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", mContext.MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Authorization", Type + " " + Token);
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().FOLLOW_USER + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"following\":\"" + ID + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().FOLLOW_USER + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().FOLLOW_USER);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
