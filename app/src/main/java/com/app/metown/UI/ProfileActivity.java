package com.app.metown.UI;

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
import com.app.metown.Models.FeedbackModel;
import com.app.metown.Models.ReviewModel;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    CircleImageView imgUser;
    TextView txtNickName, txtRate, txtFollow, txtUniqueID, txtAchievementCount, txtItemCount, txtReviewCount, txtVerifyTime,
            txtVerifyTimeAddress;
    RelativeLayout OptionLayout, AchievementLayout, ItemLayout, MyCommunityLayout, FeedbackLayout, ReviewLayout;
    RecyclerView FeedbackView, ReviewView;
    ArrayList<FeedbackModel> feedbackList = new ArrayList<>();
    ArrayList<ReviewModel> reviewList = new ArrayList<>();
    String UserID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.e("Activity", "ProfileActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetIntentData();

        GetUserDefault();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgUser = findViewById(R.id.imgUser);

        txtNickName = findViewById(R.id.txtNickName);
        txtRate = findViewById(R.id.txtRate);
        txtFollow = findViewById(R.id.txtFollow);
        txtUniqueID = findViewById(R.id.txtUniqueID);
        txtItemCount = findViewById(R.id.txtItemCount);
        txtVerifyTime = findViewById(R.id.txtVerifyTime);
        txtReviewCount = findViewById(R.id.txtReviewCount);
        txtAchievementCount = findViewById(R.id.txtAchievementCount);
        txtVerifyTimeAddress = findViewById(R.id.txtVerifyTimeAddress);

        OptionLayout = findViewById(R.id.OptionLayout);
        AchievementLayout = findViewById(R.id.AchievementLayout);
        ItemLayout = findViewById(R.id.ItemLayout);
        MyCommunityLayout = findViewById(R.id.MyCommunityLayout);
        FeedbackLayout = findViewById(R.id.FeedbackLayout);
        ReviewLayout = findViewById(R.id.ReviewLayout);

        FeedbackView = findViewById(R.id.FeedbackView);
        ReviewView = findViewById(R.id.ReviewView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        ItemLayout.setOnClickListener(this);
        AchievementLayout.setOnClickListener(this);
        MyCommunityLayout.setOnClickListener(this);
        FeedbackLayout.setOnClickListener(this);
        ReviewLayout.setOnClickListener(this);
    }

    public void GetIntentData() {
        UserID = getIntent().getStringExtra("UserID");

        GetProfileApi(UserID);
    }

    public void GetUserDefault() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String DefaultUserID = sharedPreferences.getString("UserID", "");

        if (DefaultUserID.equals(UserID)) {
            txtRate.setVisibility(View.INVISIBLE);
            txtFollow.setVisibility(View.INVISIBLE);
            OptionLayout.setVisibility(View.VISIBLE);
        } else {
            txtRate.setVisibility(View.VISIBLE);
            txtFollow.setVisibility(View.VISIBLE);
            OptionLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.AchievementLayout:
                Intent intent = new Intent(mContext, AchievementActivity.class);
                startActivity(intent);
                break;
            case R.id.ItemLayout:
                Intent MyItemSale = new Intent(mContext, MyItemSaleActivity.class);
                startActivity(MyItemSale);
                break;
            case R.id.MyCommunityLayout:
                Intent MyCommunity = new Intent(mContext, MyCommunityActivity.class);
                startActivity(MyCommunity);
                break;
            case R.id.FeedbackLayout:
                Intent Feedback = new Intent(mContext, FeedbackActivity.class);
                startActivity(Feedback);
                break;
            case R.id.ReviewLayout:
                Intent ReviewFromBuyerAndSeller = new Intent(mContext, ReviewFromBuyerAndSellerActivity.class);
                startActivity(ReviewFromBuyerAndSeller);
                break;
        }
    }

    private void GetProfileApi(final String UserID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_PROFILE + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONObject objectData = JsonMain.getJSONObject("data");
                        String NickName = objectData.getString("nick_name");
                        txtNickName.setText(NickName);
                        String UniqueID = objectData.getString("unique_id");
                        txtUniqueID.setText(UniqueID + " of user ID");
                        String AchievementCount = objectData.getString("achivementcount");
                        txtAchievementCount.setText(AchievementCount + " Achievements");
                        String SecondaryCount = objectData.getString("secondarycount");
                        txtItemCount.setText(SecondaryCount + " Items");

                        String Verified = objectData.getString("verified");
                        txtVerifyTime.setText("Verified " + Verified + " times in 'address'");
                        txtVerifyTimeAddress.setText("Verified " + Verified + " times in 'address'");

                        String ProfilePicture = objectData.getString("profile_pic");
                        Glide.with(mContext).load(ProfilePicture).placeholder(R.drawable.profile_default).into(imgUser);
                        JSONArray arrayReview = objectData.getJSONArray("review");
                        if (arrayReview.length() == 0) {
                            ReviewLayout.setVisibility(View.VISIBLE);
                            txtReviewCount.setText("0 Review");
                        } else {
                            for (int i = 0; i < arrayReview.length(); i++) {
                                ReviewModel reviewModel = new ReviewModel();
                                reviewModel.setToUsersProfilePicture(arrayReview.getJSONObject(i).getJSONObject("to_users_detail").getString("profile_pic"));
                                reviewModel.setToUsersNickName(arrayReview.getJSONObject(i).getJSONObject("to_users_detail").getString("nick_name"));
                                reviewModel.setNotes(arrayReview.getJSONObject(i).getString("notes"));
                                reviewList.add(reviewModel);
                            }
                            if (reviewList.size() > 0) {
                                ReviewLayout.setVisibility(View.VISIBLE);
                                txtReviewCount.setText(reviewList.size() + " Review");
                                ReviewAdapter reviewAdapter = new ReviewAdapter(mContext, reviewList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                ReviewView.setLayoutManager(mLayoutManager);
                                ReviewView.setItemAnimator(new DefaultItemAnimator());
                                ReviewView.setAdapter(reviewAdapter);
                                reviewAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_PROFILE + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", UserID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_PROFILE + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_PROFILE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<FeedbackModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtFeedback;

            MyViewHolder(View view) {
                super(view);

                txtFeedback = view.findViewById(R.id.txtFeedback);
            }
        }

        public FeedbackAdapter(Context mContext, ArrayList<FeedbackModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            FeedbackModel feedbackModel = arrayList.get(position);

            // holder.txtFeedback.setText(feedbackModel.getKeyword());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public static class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ReviewModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            CircleImageView imgProfile;
            TextView txtNickNameReview, txtGoReview;

            MyViewHolder(View view) {
                super(view);

                imgProfile = view.findViewById(R.id.imgProfile);

                txtNickNameReview = view.findViewById(R.id.txtNickNameReview);
                txtGoReview = view.findViewById(R.id.txtGoReview);
            }
        }

        public ReviewAdapter(Context mContext, ArrayList<ReviewModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_review_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final ReviewModel reviewModel = arrayList.get(position);

            String ToUsersProfilePicture = reviewModel.getToUsersProfilePicture();
            if (ToUsersProfilePicture.equals("") || ToUsersProfilePicture.equals("null") ||
                    ToUsersProfilePicture.equals(null) || ToUsersProfilePicture == null) {

            } else {
                Glide.with(mContext).load(reviewModel.getToUsersProfilePicture()).into(holder.imgProfile);
            }

            holder.txtNickNameReview.setText(reviewModel.getToUsersNickName());
            holder.txtGoReview.setText(reviewModel.getNotes());
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