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
import com.app.metown.Models.ReviewModel;
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

public class ReviewFromBuyerAndSellerActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    View AllView, FromBuyerView, FromSellerView;
    TextView txtAll, txtFromBuyer, txtFromSeller, txtError;
    LinearLayout AllLayout, FromBuyerLayout, FromSellerLayout, ResponseLayout;
    RelativeLayout NoResponseLayout;
    RecyclerView ReviewFromBuyerAndSellerView;
    ArrayList<ReviewModel> allReviewList = new ArrayList<>();
    String offSet = "0", Type = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_from_buyer_seller);

        Log.e("Activity", "ReviewFromBuyerAndSellerActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        ViewSetText();

        GetAllBuyerSellerReviewApi(offSet, Type);
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        AllView = findViewById(R.id.AllView);
        FromBuyerView = findViewById(R.id.FromBuyerView);
        FromSellerView = findViewById(R.id.FromSellerView);

        txtAll = findViewById(R.id.txtAll);
        txtFromBuyer = findViewById(R.id.txtFromBuyer);
        txtFromSeller = findViewById(R.id.txtFromSeller);
        txtError = findViewById(R.id.txtError);

        AllLayout = findViewById(R.id.AllLayout);
        FromBuyerLayout = findViewById(R.id.FromBuyerLayout);
        FromSellerLayout = findViewById(R.id.FromSellerLayout);
        ResponseLayout = findViewById(R.id.ResponseLayout);
        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        ReviewFromBuyerAndSellerView = findViewById(R.id.ReviewFromBuyerAndSellerView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        AllLayout.setOnClickListener(this);
        FromBuyerLayout.setOnClickListener(this);
        FromSellerLayout.setOnClickListener(this);
    }

    public void ViewSetText() {
        txtAll.setTextColor(getResources().getColor(R.color.black));
        AllView.setBackgroundColor(getResources().getColor(R.color.black));
        txtFromBuyer.setTextColor(getResources().getColor(R.color.grey));
        FromBuyerView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtFromSeller.setTextColor(getResources().getColor(R.color.grey));
        FromSellerView.setBackgroundColor(getResources().getColor(R.color.grey));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.AllLayout:
                txtAll.setTextColor(getResources().getColor(R.color.black));
                AllView.setBackgroundColor(getResources().getColor(R.color.black));
                txtFromBuyer.setTextColor(getResources().getColor(R.color.grey));
                FromBuyerView.setBackgroundColor(getResources().getColor(R.color.grey));
                txtFromSeller.setTextColor(getResources().getColor(R.color.grey));
                FromSellerView.setBackgroundColor(getResources().getColor(R.color.grey));
                GetAllBuyerSellerReviewApi(offSet, Type);
                break;
            case R.id.FromBuyerLayout:
                txtFromBuyer.setTextColor(getResources().getColor(R.color.black));
                FromBuyerView.setBackgroundColor(getResources().getColor(R.color.black));
                txtAll.setTextColor(getResources().getColor(R.color.grey));
                AllView.setBackgroundColor(getResources().getColor(R.color.grey));
                txtFromSeller.setTextColor(getResources().getColor(R.color.grey));
                FromSellerView.setBackgroundColor(getResources().getColor(R.color.grey));
                GetAllBuyerSellerReviewApi(offSet, "3");
                break;
            case R.id.FromSellerLayout:
                txtFromSeller.setTextColor(getResources().getColor(R.color.black));
                FromSellerView.setBackgroundColor(getResources().getColor(R.color.black));
                txtAll.setTextColor(getResources().getColor(R.color.grey));
                AllView.setBackgroundColor(getResources().getColor(R.color.grey));
                txtFromBuyer.setTextColor(getResources().getColor(R.color.grey));
                FromBuyerView.setBackgroundColor(getResources().getColor(R.color.grey));
                GetAllBuyerSellerReviewApi(offSet, "2");
                break;
        }
    }

    private void GetAllBuyerSellerReviewApi(final String offSet, final String Type) {
        String req = "req";
        allReviewList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ALL_BUYER_SELLER_REVIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().ALL_BUYER_SELLER_REVIEW + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            ReviewModel reviewModel = new ReviewModel();
                            reviewModel.setToUsersProfilePicture(arrayData.getJSONObject(i).getJSONObject("to_users_detail").getString("profile_pic"));
                            reviewModel.setToUsersNickName(arrayData.getJSONObject(i).getJSONObject("to_users_detail").getString("nick_name"));
                            reviewModel.setNotes(arrayData.getJSONObject(i).getString("notes"));
                            allReviewList.add(reviewModel);
                        }
                        if (allReviewList.size() > 0) {
                            NoResponseLayout.setVisibility(View.GONE);
                            ResponseLayout.setVisibility(View.VISIBLE);
                            ReviewFromBuyerAndSellerAdapter reviewFromBuyerAndSellerAdapter = new ReviewFromBuyerAndSellerAdapter(mContext, allReviewList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            ReviewFromBuyerAndSellerView.setLayoutManager(mLayoutManager);
                            ReviewFromBuyerAndSellerView.setItemAnimator(new DefaultItemAnimator());
                            ReviewFromBuyerAndSellerView.setAdapter(reviewFromBuyerAndSellerAdapter);
                            reviewFromBuyerAndSellerAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                        ResponseLayout.setVisibility(View.GONE);
                        NoResponseLayout.setVisibility(View.VISIBLE);
                        txtError.setText(ErrorMessage);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
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
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().ALL_BUYER_SELLER_REVIEW + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"offset\":\"" + offSet + "\",\"type\":\"" + Type + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().ALL_BUYER_SELLER_REVIEW + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().ALL_BUYER_SELLER_REVIEW);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class ReviewFromBuyerAndSellerAdapter extends RecyclerView.Adapter<ReviewFromBuyerAndSellerAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ReviewModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            CircleImageView imgUser;
            TextView txtNickName, txtGoReview;

            MyViewHolder(View view) {
                super(view);

                imgUser = view.findViewById(R.id.imgUser);

                txtNickName = view.findViewById(R.id.txtNickName);
                txtGoReview = view.findViewById(R.id.txtGoReview);
            }
        }

        public ReviewFromBuyerAndSellerAdapter(Context mContext, ArrayList<ReviewModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_from_buyer_seller_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final ReviewModel reviewModel = arrayList.get(position);

            String ProfilePicture = reviewModel.getToUsersProfilePicture();
            if (ProfilePicture.equals("") || ProfilePicture.equals("null") || ProfilePicture.equals(null) || ProfilePicture == null) {

            } else {
                Glide.with(mContext).load(ProfilePicture).into(holder.imgUser);
            }

            holder.txtNickName.setText(reviewModel.getToUsersNickName());
            holder.txtGoReview.setText(reviewModel.getNotes());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent Favourite = new Intent(mContext, FavouriteActivity.class);
                    mContext.startActivity(Favourite);
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
