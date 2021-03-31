package com.app.metown.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BusinessStoreActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    EditText edtAddComment;
    TextView txtStoreName, txtAddress, txtStoreLikeCount, txtTitle, txtTimePosted, txtPosition, txtSalary,
            txtStartWorkingTime, txtDetail;
    LinearLayout CallLayout, ReviewLayout, FavoriteLayout, ChatLayout;
    String UserID= "", HelperID = "", PhoneNumber = "", MerchantID = "";
    Dialog dialog;
    RecyclerView PostView;
    ArrayList<String> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_store);

        Log.e("Activity", "BusinessStoreActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GetIntentData();

        ViewInitialization();

        ViewOnClick();

        GetStoreMerchantWithHelperProfileApi(UserID, HelperID);
    }

    public void GetUserDefault() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        MerchantID = sharedPreferences.getString("MerchantID", "");
    }

    public void GetIntentData() {
        UserID =  getIntent().getStringExtra("UserID");
        HelperID =  getIntent().getStringExtra("HelperID");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtStoreName = findViewById(R.id.txtStoreName);
        txtAddress = findViewById(R.id.txtAddress);
        txtStoreLikeCount = findViewById(R.id.txtStoreLikeCount);
        txtTitle = findViewById(R.id.txtTitle);
        txtTimePosted = findViewById(R.id.txtTimePosted);
        txtPosition = findViewById(R.id.txtPosition);
        txtSalary = findViewById(R.id.txtSalary);
        txtStartWorkingTime = findViewById(R.id.txtStartWorkingTime);
        txtDetail = findViewById(R.id.txtDetail);

        CallLayout = findViewById(R.id.CallLayout);
        ReviewLayout = findViewById(R.id.ReviewLayout);
        FavoriteLayout = findViewById(R.id.FavoriteLayout);
        ChatLayout = findViewById(R.id.ChatLayout);

        PostView = findViewById(R.id.PostView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        CallLayout.setOnClickListener(this);
        ReviewLayout.setOnClickListener(this);
        FavoriteLayout.setOnClickListener(this);
        ChatLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.CallLayout:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + PhoneNumber));
                startActivity(intent);
                break;
            case R.id.ReviewLayout:
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_comment_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                edtAddComment = dialog.findViewById(R.id.edtAddComment);
                dialog.findViewById(R.id.txtAdd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Text = edtAddComment.getText().toString().trim();
                        if (Text.equals("")) {
                            Toast.makeText(mContext, "Please Enter a Review.", Toast.LENGTH_LONG).show();
                        } else {
                            AddStoreReviewApi(MerchantID, Text);
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.FavoriteLayout:
//                AddEditFavoriteApi(ID);
                break;
            case R.id.ChatLayout:
//                Intent ChatCommercial = new Intent(mContext, ChatCommercialActivity.class);
//                ChatCommercial.putExtra("ConversationID", "");
//                ChatCommercial.putExtra("ToUserID", SellerID);
//                ChatCommercial.putExtra("ToUserName", "");
//                ChatCommercial.putExtra("ProductID", ID);
//                ChatCommercial.putExtra("ProductName", "");
//                ChatCommercial.putExtra("ProductImages", Images);
//                ChatCommercial.putExtra("ProductPrice", "");
//                startActivity(ChatCommercial);
                break;
        }
    }

    private void AddStoreReviewApi(final String MerchantID, final String Text) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ADD_STORE_REVIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().ADD_STORE_REVIEW + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONObject objectData = JsonMain.getJSONObject("data");
                        String Images = objectData.getString("images");
                        String[] separated = Images.split(",");
                        postList.addAll(Arrays.asList(separated));
                        if (postList.size() > 0) {
                            BusinessPostAdapter businessPostAdapter = new BusinessPostAdapter(mContext, postList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
                            PostView.setLayoutManager(mLayoutManager);
                            PostView.setItemAnimator(new DefaultItemAnimator());
                            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
                            pagerSnapHelper.attachToRecyclerView(PostView);
                            PostView.setAdapter(businessPostAdapter);
                            businessPostAdapter.notifyDataSetChanged();
                        }
                        txtStoreName.setText(objectData.getString("name"));
                        txtAddress.setText(objectData.getString("address"));
                        txtStoreLikeCount.setText(objectData.getString("favourite_count") + " users like this store");
                        txtTitle.setText(objectData.getString("category_title"));
                        txtTimePosted.setText(objectData.getString("created_at"));
                        // txtPosition.setText(objectData.getString("created_at"));
                        txtSalary.setText(objectData.getJSONObject("helper_detail").getString("salary"));
                        txtStartWorkingTime.setText(objectData.getJSONObject("helper_detail").getString("start_working_time"));
                        txtDetail.setText(objectData.getJSONObject("helper_detail").getString("description"));
                        PhoneNumber = objectData.getJSONObject("user_detail").getString("phone_number");
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
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_STORE_REVIEW + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("merchant_id", MerchantID);
                params.put("text", Text);
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_STORE_REVIEW + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().ADD_STORE_REVIEW);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void AddEditFavoriteApi(final String ProductID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ADD_EDIT_FAVORITE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().ADD_EDIT_FAVORITE + response);
                    JSONObject JsonMain = new JSONObject(response);
                    // String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
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
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_EDIT_FAVORITE + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"product_id\":\"" + ProductID + "\",\"is_favorite\":\"" + "1" + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_EDIT_FAVORITE + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().ADD_EDIT_FAVORITE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void GetStoreMerchantWithHelperProfileApi(final String UserID, final String HelperID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_STORE_MERCHANT_WITH_HELPER_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_STORE_MERCHANT_WITH_HELPER_PROFILE + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONObject objectData = JsonMain.getJSONObject("data");
                        String Images = objectData.getString("images");
                        String[] separated = Images.split(",");
                        postList.addAll(Arrays.asList(separated));
                        if (postList.size() > 0) {
                            BusinessPostAdapter businessPostAdapter = new BusinessPostAdapter(mContext, postList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
                            PostView.setLayoutManager(mLayoutManager);
                            PostView.setItemAnimator(new DefaultItemAnimator());
                            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
                            pagerSnapHelper.attachToRecyclerView(PostView);
                            PostView.setAdapter(businessPostAdapter);
                            businessPostAdapter.notifyDataSetChanged();
                        }
                        txtStoreName.setText(objectData.getString("name"));
                        txtAddress.setText(objectData.getString("address"));
                        txtStoreLikeCount.setText(objectData.getString("favourite_count") + " users like this store");
                        txtTitle.setText(objectData.getString("category_title"));
                        txtTimePosted.setText(objectData.getString("created_at"));
                        // txtPosition.setText(objectData.getString("created_at"));
                        txtSalary.setText(objectData.getJSONObject("helper_detail").getString("salary"));
                        txtStartWorkingTime.setText(objectData.getJSONObject("helper_detail").getString("start_working_time"));
                        txtDetail.setText(objectData.getJSONObject("helper_detail").getString("description"));
                        PhoneNumber = objectData.getJSONObject("user_detail").getString("phone_number");
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
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_STORE_MERCHANT_WITH_HELPER_PROFILE + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", UserID);
                params.put("helper_id", HelperID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_STORE_MERCHANT_WITH_HELPER_PROFILE + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_STORE_MERCHANT_WITH_HELPER_PROFILE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class BusinessPostAdapter extends RecyclerView.Adapter<BusinessPostAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<String> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgPost;

            MyViewHolder(View view) {
                super(view);

                imgPost = view.findViewById(R.id.imgPost);
            }
        }

        public BusinessPostAdapter(Context mContext, ArrayList<String> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            String string = arrayList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SetRangeActivity.class);
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