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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NickNameActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    Button btnDone;
    ImageView imgBack;
    EditText edtNickName;

    String NickName = "", InvitationCode = "", Email = "", MobileNumber = "", Otp = "", FCMToken = "5B4EB961-B66B-4958-8195-BBD4EBF3956D";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);

        Log.e("Activity", "NickNameActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        edtNickName = findViewById(R.id.edtNickName);

        btnDone = findViewById(R.id.btnDone);

        imgBack.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        MobileNumber = getIntent().getStringExtra("MobileNumber");
        Otp = getIntent().getStringExtra("Otp");
        Email = getIntent().getStringExtra("Email");
        InvitationCode = getIntent().getStringExtra("InvitationCode");
        // Log.e("MobileNumber", "" + MobileNumber);
        // Log.e("Otp", "" + Otp);
        // Log.e("Email", "" + Email);
        // Log.e("InvitationCode", "" + InvitationCode);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnDone:
                NickName = edtNickName.getText().toString().trim();
                if (NickName.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Nick Name", Toast.LENGTH_LONG).show();
                } else {
                    SignUpApi(NickName, Email, MobileNumber, InvitationCode, FCMToken);
                }
                break;
        }
    }

    private void SignUpApi(final String NickName, final String Email, final String PhoneNumber, final String ReferralCode, final String FCMToken) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().SIGN_UP,
                new Response.Listener<String>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().SIGN_UP + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            Log.e("HAS_ERROR", "" + HAS_ERROR);
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
                                sharedPreferencesEditor.apply();
                                sharedPreferencesEditor.commit();

                                JSONObject objectToken = objectData.getJSONObject("token");

                                SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor preferencesEditor = preferences.edit();
                                preferencesEditor.putString("Token", objectToken.getString("token"));
                                preferencesEditor.putString("Type", objectToken.getString("type"));
                                preferencesEditor.apply();
                                preferencesEditor.commit();

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
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"nick_name\":\"" + NickName + "\",\"email\":\"" + Email + "\",\"phone_number\":\"" + PhoneNumber +
                        "\",\"device_type\":\"" + "A" + "\",\"social_id\":\"" + "" + "\",\"referral_code\":\"" + ReferralCode +
                        "\",\"fcm_token\":\"" + FCMToken + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().SIGN_UP + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().SIGN_UP);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}