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
import com.app.metown.Models.CommentModel;
import com.app.metown.Models.ItemMainModel;
import com.app.metown.Models.PostModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyCommunityActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    LinearLayout MyPostLayout, MyCommentLayout;
    TextView txtMyPost, txtMyComment;
    View MyPostView, MyCommentView;
    RecyclerView PostView, CommentView;
    ArrayList<PostModel> postList = new ArrayList<>();
    ArrayList<CommentModel> commentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_community);

        Log.e("Activity", "MyCommunityActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetCommunityApi("23.112659", "72.547752", "1000");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        MyPostView = findViewById(R.id.MyPostView);
        MyCommentView = findViewById(R.id.MyCommentView);

        MyPostLayout = findViewById(R.id.MyPostLayout);
        MyCommentLayout = findViewById(R.id.MyCommentLayout);

        txtMyPost = findViewById(R.id.txtMyPost);
        txtMyComment = findViewById(R.id.txtMyComment);

        txtMyPost.setTextColor(getResources().getColor(R.color.black));
        MyPostView.setBackgroundColor(getResources().getColor(R.color.black));
        txtMyComment.setTextColor(getResources().getColor(R.color.grey));
        MyCommentView.setBackgroundColor(getResources().getColor(R.color.grey));

        PostView = findViewById(R.id.PostView);
        CommentView = findViewById(R.id.CommentView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        MyPostLayout.setOnClickListener(this);
        MyCommentLayout.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.MyPostLayout:
                txtMyPost.setTextColor(getResources().getColor(R.color.black));
                MyPostView.setBackgroundColor(getResources().getColor(R.color.black));
                txtMyComment.setTextColor(getResources().getColor(R.color.grey));
                MyCommentView.setBackgroundColor(getResources().getColor(R.color.grey));
                GetCommunityApi("23.112659", "72.547752", "1000");
                break;
            case R.id.MyCommentLayout:
                txtMyComment.setTextColor(getResources().getColor(R.color.black));
                MyCommentView.setBackgroundColor(getResources().getColor(R.color.black));
                txtMyPost.setTextColor(getResources().getColor(R.color.grey));
                MyPostView.setBackgroundColor(getResources().getColor(R.color.grey));
                GetCommentListApi("1", "6");
                break;
        }
    }

    private void GetCommunityApi(final String Latitude, final String Longitude, final String UserRange) {
        String req = "req";
        postList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_COMMUNITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_COMMUNITY + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray jsonArray = JsonMain.getJSONArray("data");
                                for (int i = 0; jsonArray.length() > i; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    PostModel postModel = new PostModel();
                                    postModel.setID(jsonObject.getString("id"));
                                    postModel.setKeyword(jsonObject.getString("keyword"));
                                    postModel.setDescription(jsonObject.getString("description"));
                                    postModel.setImage(jsonObject.getString("image"));
                                    postModel.setDistance(jsonObject.getString("distance"));
                                    postList.add(postModel);
                                }
                                if (postList.size() > 0) {
                                    CommentView.setVisibility(View.GONE);
                                    PostView.setVisibility(View.VISIBLE);
                                    PostAdapter postAdapter = new PostAdapter(mContext, postList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    PostView.setLayoutManager(mLayoutManager);
                                    PostView.setItemAnimator(new DefaultItemAnimator());
                                    PostView.setAdapter(postAdapter);
                                    postAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_COMMUNITY + params);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"lats\":\"" + Latitude + "\",\"longs\":\"" + Longitude + "\",\"userRange\":\"" + UserRange + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_COMMUNITY + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_COMMUNITY);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<PostModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtCategoryName;

            MyViewHolder(View view) {
                super(view);

                txtCategoryName = view.findViewById(R.id.txtCategoryName);
            }
        }

        public PostAdapter(Context mContext, ArrayList<PostModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            PostModel postModel = arrayList.get(position);

            holder.txtCategoryName.setText(postModel.getKeyword());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void GetCommentListApi(final String Type, final String ProductID) {
        String req = "req";
        commentList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_COMMENT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_COMMENT_LIST + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray jsonArray = JsonMain.getJSONArray("data");
                                for (int i = 0; jsonArray.length() > i; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    CommentModel commentModel = new CommentModel();
                                    commentModel.setProductID(jsonObject.getString("product_id"));
                                    commentModel.setName(jsonObject.getString("name"));
                                    commentModel.setAddress(jsonObject.getString("address"));
                                    commentModel.setImages(jsonObject.getString("images"));
                                    commentModel.setCreatedAt(jsonObject.getString("created_at"));
                                    commentModel.setType(jsonObject.getString("type"));
                                    commentModel.setFavoriteCount(jsonObject.getString("Favorite_count"));
                                    commentModel.setCommentCount(jsonObject.getString("commnet_count"));
                                    commentModel.setComment(jsonObject.getString("comment"));
                                    commentList.add(commentModel);
                                }
                                if (commentList.size() > 0) {
                                    PostView.setVisibility(View.GONE);
                                    CommentView.setVisibility(View.VISIBLE);
                                    CommentAdapter commentAdapter = new CommentAdapter(mContext, commentList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    CommentView.setLayoutManager(mLayoutManager);
                                    CommentView.setItemAnimator(new DefaultItemAnimator());
                                    CommentView.setAdapter(commentAdapter);
                                    commentAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_COMMENT_LIST + params);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"type\":\"" + Type + "\",\"product_id\":\"" + ProductID + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_COMMENT_LIST + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_COMMENT_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CommentModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgComment;
            TextView txtComment;
            RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                imgComment = view.findViewById(R.id.imgComment);

                txtComment = view.findViewById(R.id.txtComment);

                OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public CommentAdapter(Context mContext, ArrayList<CommentModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_comment_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            CommentModel commentModel = arrayList.get(position);

            holder.txtComment.setText(commentModel.getComment());

            String Images = commentModel.getImages();
            String[] separated = Images.split(",");
            Glide.with(mContext).load(separated[0]).into(holder.imgComment);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent TopicList = new Intent(mContext, TopicListActivity.class);
                    mContext.startActivity(TopicList);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void MyCommunity() {
        String req = "req";
        postList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().MY_COMMUNITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().MY_COMMUNITY + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                /*JSONArray jsonArray = JsonMain.getJSONArray("data");
                                for (int i = 0; jsonArray.length() > i; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    PostModel postModel = new PostModel();
                                    postModel.setID(jsonObject.getString("id"));
                                    postModel.setKeyword(jsonObject.getString("keyword"));
                                    postModel.setDescription(jsonObject.getString("description"));
                                    postModel.setImage(jsonObject.getString("image"));
                                    postList.add(postModel);
                                }
                                if (postList.size() > 0) {
                                    CommentView.setVisibility(View.GONE);
                                    PostView.setVisibility(View.VISIBLE);
                                    PostAdapter postAdapter = new PostAdapter(mContext, postList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    PostView.setLayoutManager(mLayoutManager);
                                    PostView.setItemAnimator(new DefaultItemAnimator());
                                    PostView.setAdapter(postAdapter);
                                    postAdapter.notifyDataSetChanged();
                                }*/
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
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
                Log.e("HEADER", "" + APIConstant.getInstance().MY_COMMUNITY + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_COMMUNITY);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyCommunity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
