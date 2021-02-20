package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondHandPostActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    CircleImageView imgUser;
    EditText edtMessage;
    TextView txtSecondHandPostTitle, txtNickName;
    LinearLayout FollowUserLayout, FavoriteLayout, ChatLayout;
    SharedPreferences sharedPreferences;
    String ID, SellerID, BuyerID, CategoryID, CategoryTitle, Name, Description, Status, Type, Price, Latitude, Longitude,
            IsNegotiable, Images, StatusTitle, FavouriteCount, CommentCount;
    String NickName, ProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_hand_post);

        Log.e("Activity", "SecondHandPostActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GetIntentData();

        GetUserDefault();

        ViewInitialization();

        ViewOnClick();

        ViewSetText();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgUser = findViewById(R.id.imgUser);

        edtMessage = findViewById(R.id.edtMessage);

        txtSecondHandPostTitle = findViewById(R.id.txtSecondHandPostTitle);
        txtNickName = findViewById(R.id.txtNickName);

        FollowUserLayout = findViewById(R.id.FollowUserLayout);
        FavoriteLayout = findViewById(R.id.FavoriteLayout);
        ChatLayout = findViewById(R.id.ChatLayout);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        FollowUserLayout.setOnClickListener(this);
        FavoriteLayout.setOnClickListener(this);
        ChatLayout.setOnClickListener(this);
    }

    public void GetIntentData() {
        ID = getIntent().getStringExtra("ID");
        SellerID = getIntent().getStringExtra("SellerID");
        BuyerID = getIntent().getStringExtra("BuyerID");
        CategoryID = getIntent().getStringExtra("CategoryID");
        CategoryTitle = getIntent().getStringExtra("CategoryTitle");
        Name = getIntent().getStringExtra("Name");
        Description = getIntent().getStringExtra("Description");
        Status = getIntent().getStringExtra("Status");
        Type = getIntent().getStringExtra("Type");
        Price = getIntent().getStringExtra("Price");
        Latitude = getIntent().getStringExtra("Latitude");
        Longitude = getIntent().getStringExtra("Longitude");
        IsNegotiable = getIntent().getStringExtra("IsNegotiable");
        Images = getIntent().getStringExtra("Images");
        StatusTitle = getIntent().getStringExtra("StatusTitle");
        FavouriteCount = getIntent().getStringExtra("FavouriteCount");
        CommentCount = getIntent().getStringExtra("CommentCount");
    }

    public void GetUserDefault() {
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        NickName = sharedPreferences.getString("NickName", "");
        ProfilePicture = sharedPreferences.getString("ProfilePicture", "");
    }

    public void ViewSetText() {
        txtSecondHandPostTitle.setText(Name);
        txtNickName.setText(NickName);
        Glide.with(mContext).load(ProfilePicture).into(imgUser);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.FollowUserLayout:
                FollowUserApi(SellerID);
                break;
            case R.id.FavoriteLayout:

                break;
            case R.id.ChatLayout:
                String Message = edtMessage.getText().toString().trim();
                if (Message.equals("")) {
                    Toast.makeText(mContext, "Please enter your message in details.", Toast.LENGTH_LONG).show();
                } else {
                    SendMessageApi(SellerID, Message, ID);
                }
                break;
        }
    }

    private void SendMessageApi(final String ToUserID, final String Body, final String ProductID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().SEND_MESSAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().SEND_MESSAGE + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            String Message = JsonMain.getString("msg");
                            if (HAS_ERROR.equals("false")) {
                                String SuccessMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        // Log.e("ERROR", "" + error.getMessage());
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
                Log.e("HEADER", "" + APIConstant.getInstance().SEND_MESSAGE + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("touser_id", ToUserID);
                params.put("type", "text");
                params.put("body", Body);
                params.put("product_id", ProductID);
                params.put("conversation_id", "");
                params.put("image", "");
                Log.e("PARAMETER", "" + APIConstant.getInstance().SEND_MESSAGE + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().SEND_MESSAGE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
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
                            String Message = JsonMain.getString("msg");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                String SuccessMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
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
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
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