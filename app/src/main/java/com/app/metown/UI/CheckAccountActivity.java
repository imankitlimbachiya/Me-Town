package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckAccountActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    Button btnAccountOwner;
    ImageView imgBack, imgProfile;
    TextView txtMobileNumber, txtCreateNewAccount;

    String MobileNumber = "", DeviceType = "A", FCMToken = "5B4EB961-B66B-4958-8195-BBD4EBF3956D", ProfilePicture = "", Otp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_account);

        Log.e("Activity","CheckAccountActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetIntentData();

        ViewSetText();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgProfile = findViewById(R.id.imgProfile);

        btnAccountOwner = findViewById(R.id.btnAccountOwner);

        txtMobileNumber = findViewById(R.id.txtMobileNumber);
        txtCreateNewAccount = findViewById(R.id.txtCreateNewAccount);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnAccountOwner.setOnClickListener(this);
        txtCreateNewAccount.setOnClickListener(this);
    }

    public void GetIntentData() {
        MobileNumber = getIntent().getStringExtra("MobileNumber");
        Otp = getIntent().getStringExtra("Otp");
        ProfilePicture = getIntent().getStringExtra("ProfilePicture");
    }

    public void ViewSetText() {
        Glide.with(mContext).load(ProfilePicture).into(imgProfile);
        txtMobileNumber.setText(MobileNumber);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnAccountOwner:
                LoginApi(MobileNumber, DeviceType, FCMToken, Otp);
                break;
            case R.id.txtCreateNewAccount:
                Intent Main = new Intent(mContext, MainActivity.class);
                startActivity(Main);
                finish();
                break;
        }
    }

    private void LoginApi(final String PhoneNumber, final String DeviceType, final String FCMToken, final String Otp) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().LOG_IN,
                new Response.Listener<String>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().LOG_IN + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONObject objectData = JsonMain.getJSONObject("data");

                                JSONObject objectUser = objectData.getJSONObject("user");

                                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                sharedPreferencesEditor.putString("UserID", objectUser.getString("user_id"));
                                sharedPreferencesEditor.putString("UniqueID", objectUser.getString("unique_id"));
                                sharedPreferencesEditor.putString("NickName", objectUser.getString("nick_name"));
                                sharedPreferencesEditor.putString("Email", objectUser.getString("email"));
                                sharedPreferencesEditor.putString("SocialID", objectUser.getString("social_id"));
                                sharedPreferencesEditor.putString("PhoneNumber", objectUser.getString("phone_number"));
                                sharedPreferencesEditor.putString("InvitationCode", objectUser.getString("invitation_code"));
                                sharedPreferencesEditor.putString("Status", objectUser.getString("status"));
                                sharedPreferencesEditor.putString("EmailVerify", objectUser.getString("email_verify"));
                                sharedPreferencesEditor.putString("ProfilePicture", objectUser.getString("profile_pic"));

                                JSONObject objectToken = objectData.getJSONObject("token");

                                sharedPreferencesEditor.putString("Token", objectToken.getString("token"));
                                sharedPreferencesEditor.putString("Type", objectToken.getString("type"));
                                sharedPreferencesEditor.apply();
                                sharedPreferencesEditor.commit();

                                Intent SetLocation = new Intent(mContext, SetLocationActivity.class);
                                startActivity(SetLocation);
                                finish();
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
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().LOG_IN + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"phone_number\":\"" + PhoneNumber + "\",\"device_type\":\"" + DeviceType +
                        "\",\"fcm_token\":\"" + FCMToken + "\",\"otp\":\"" + Otp + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().LOG_IN + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().LOG_IN);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
