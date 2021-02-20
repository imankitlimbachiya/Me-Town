package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.Adapters.CategoryAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.CategoryModel;
import com.app.metown.Models.PostTownModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangeTownPostActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtAreaOld;
    EditText edtNewTown;
    Button btnMovePost;
    RecyclerView PostTownView;
    ArrayList<PostTownModel> postTownList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_town_post);

        Log.e("Activity", "ChangeTownPostActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetTownWithUserPostSaleCountApi();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtAreaOld = findViewById(R.id.txtAreaOld);

        edtNewTown = findViewById(R.id.edtNewTown);

        btnMovePost = findViewById(R.id.btnMovePost);

        PostTownView = findViewById(R.id.PostTownView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnMovePost.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnMovePost:
                String OldTown = txtAreaOld.getText().toString().trim();
                String NewTown = edtNewTown.getText().toString().trim();
                if (OldTown.equals("")) {
                    Toast.makeText(mContext, "Please select your town to replace new one.", Toast.LENGTH_LONG).show();
                } else if (NewTown.equals("")) {
                    Toast.makeText(mContext, "Please enter add new town for your selected town.", Toast.LENGTH_LONG).show();
                } else {
                    PostSaleChangeTownApi(OldTown, NewTown);
                }
                break;
        }
    }

    private void GetTownWithUserPostSaleCountApi() {
        String req = "req";
        postTownList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_TOWN_WITH_USER_POST_SALE_COUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_TOWN_WITH_USER_POST_SALE_COUNT + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    PostTownModel postTownModel = new PostTownModel();
                                    postTownModel.setID(arrayData.getJSONObject(i).getString("id"));
                                    postTownModel.setCount(arrayData.getJSONObject(i).getString("count"));
                                    postTownModel.setName(arrayData.getJSONObject(i).getString("name"));
                                    postTownList.add(postTownModel);
                                }
                                if (postTownList.size() > 0) {
                                    PostTownAdapter postTownAdapter = new PostTownAdapter(mContext, postTownList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    PostTownView.setLayoutManager(mLayoutManager);
                                    PostTownView.setItemAnimator(new DefaultItemAnimator());
                                    PostTownView.setAdapter(postTownAdapter);
                                    postTownAdapter.notifyDataSetChanged();
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
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_TOWN_WITH_USER_POST_SALE_COUNT + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_TOWN_WITH_USER_POST_SALE_COUNT);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class PostTownAdapter extends RecyclerView.Adapter<PostTownAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<PostTownModel> arrayList;
        int pos = -1;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtArea, txtPost;
            View BottomView;
            RelativeLayout MainLayout;

            MyViewHolder(View view) {
                super(view);

                txtArea = view.findViewById(R.id.txtArea);
                txtPost = view.findViewById(R.id.txtPost);

                BottomView = view.findViewById(R.id.BottomView);

                MainLayout = view.findViewById(R.id.MainLayout);
            }
        }

        public PostTownAdapter(Context mContext, ArrayList<PostTownModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_town_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, final int position) {
            final PostTownModel postTownModel = arrayList.get(position);

            holder.txtArea.setText(postTownModel.getName());
            holder.txtPost.setText(postTownModel.getCount() + " posts");

            if (position == getItemCount() - 1) {
                holder.BottomView.setVisibility(View.GONE);
            } else {
                holder.BottomView.setVisibility(View.VISIBLE);
            }

            if (pos == position) {
                if (pos == 0) {
                    holder.MainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.post_town_o_bg));
                } else if (pos == getItemCount() - 1) {
                    // holder.BottomView.setVisibility(View.GONE);
                    holder.MainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.post_town_l_bg));
                } else {
                    holder.MainLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.veryDarkSkyPink));
                }
                holder.txtArea.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                holder.txtPost.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                txtAreaOld.setText(postTownModel.getName());
            } else {
                holder.MainLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
                holder.txtArea.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
                holder.txtPost.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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

    private void PostSaleChangeTownApi(final String LocationName, final String NewLocationName) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().POST_SALE_CHANGE_TOWN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().POST_SALE_CHANGE_TOWN + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                String SuccessMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
                                GetTownWithUserPostSaleCountApi();
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
                Log.e("HEADER", "" + APIConstant.getInstance().POST_SALE_CHANGE_TOWN + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("location_name", LocationName);
                params.put("new_location_name", NewLocationName);
                Log.e("PARAMETER", "" + APIConstant.getInstance().POST_SALE_CHANGE_TOWN + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().POST_SALE_CHANGE_TOWN);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
