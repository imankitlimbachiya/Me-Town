package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.app.metown.AppConstants.ConstantFunction;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PhoneVerifyActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtPrivacyPolicy;
    EditText edtEnterYourEmail, edtEnterYourMobileNumber, edtEnterCode;
    Button btnSendCode, btnAgreeToGetStarted;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    String Otp = "", Type = "", SocialID = "", Email = "", NickName = "", FCMToken = "5B4EB961-B66B-4958-8195-BBD4EBF3956D";
    String UserID = "", PhoneNumber = "", ProfilePicture = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);

        Log.e("Activity", "PhoneVerifyActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetUserDefault();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        edtEnterYourEmail = findViewById(R.id.edtEnterYourEmail);
        edtEnterYourMobileNumber = findViewById(R.id.edtEnterYourMobileNumber);
        edtEnterCode = findViewById(R.id.edtEnterCode);

        btnSendCode = findViewById(R.id.btnSendCode);
        btnAgreeToGetStarted = findViewById(R.id.btnAgreeToGetStarted);

        txtPrivacyPolicy = findViewById(R.id.txtPrivacyPolicy);
        txtPrivacyPolicy.setPaintFlags(txtPrivacyPolicy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Type = getIntent().getStringExtra("Type");
        SocialID = getIntent().getStringExtra("SocialID");
        Email = getIntent().getStringExtra("Email");
        NickName = getIntent().getStringExtra("NickName");
        /*Log.e("Type", "" + Type);
        Log.e("SocialID", "" + SocialID);
        Log.e("Email", "" + Email);
        Log.e("NickName", "" + NickName);*/

        if (Otp.equals("")) {
            btnAgreeToGetStarted.setEnabled(false);
        }

        if (Type.equals("Social")) {
            edtEnterYourEmail.setText(Email);
            edtEnterYourEmail.setEnabled(false);
        }
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnSendCode.setOnClickListener(this);
        btnAgreeToGetStarted.setOnClickListener(this);
    }

    @SuppressLint("CommitPrefEdits")
    public void GetUserDefault() {
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnSendCode:
                String MobileNumber = edtEnterYourMobileNumber.getText().toString().trim();
                String Email = edtEnterYourEmail.getText().toString().trim();
                if (Email.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Email", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidEmail(Email)) {
                    Toast.makeText(mContext, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
                } else if (MobileNumber.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Mobile Number", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidMobile(MobileNumber)) {
                    Toast.makeText(mContext, "Please Enter Your Valid Mobile Number", Toast.LENGTH_LONG).show();
                } else {
                    if (ConstantFunction.isNetworkAvailable(mContext)) {
                        if (Type.equals("Login")) {
                            OtpApi(MobileNumber, "1");
                        } else {
                            OtpApi(MobileNumber, "2");
                        }
                    } else {
                        Toast.makeText(mContext, "Please Check Your Network Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnAgreeToGetStarted:
                MobileNumber = edtEnterYourMobileNumber.getText().toString().trim();
                String Otp = edtEnterCode.getText().toString().trim();
                Email = edtEnterYourEmail.getText().toString().trim();
                if (Email.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Email", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidEmail(Email)) {
                    Toast.makeText(mContext, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
                } else if (MobileNumber.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Mobile Number", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidMobile(MobileNumber)) {
                    Toast.makeText(mContext, "Please Enter Your Valid Mobile Number", Toast.LENGTH_LONG).show();
                } else if (Otp.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Code", Toast.LENGTH_LONG).show();
                } else {
                    // Log.e("UserID ", "" + UserID);
                    if (UserID.equals("")) {
                        if (Type.equals("Social")) {
                            SignUpApi(NickName, Email, MobileNumber, FCMToken, SocialID);
                        } else if (Type.equals("SignUp")) {
                            Intent NickName = new Intent(mContext, NickNameActivity.class);
                            NickName.putExtra("MobileNumber", MobileNumber);
                            NickName.putExtra("Email", Email);
                            startActivity(NickName);
                            finish();
                        } else {
                            Toast.makeText(mContext, "You Are Not Member Of MeTown.\nPlease Sign up.", Toast.LENGTH_LONG).show();
                            Intent Main = new Intent(mContext, MainActivity.class);
                            startActivity(Main);
                            finish();
                        }
                    } else {
                        Intent CheckAccount = new Intent(mContext, CheckAccountActivity.class);
                        CheckAccount.putExtra("MobileNumber", MobileNumber);
                        CheckAccount.putExtra("Otp", Otp);
                        CheckAccount.putExtra("ProfilePicture", ProfilePicture);
                        startActivity(CheckAccount);
                        finish();
                    }
                }
                break;
        }
    }

    private void OtpApi(final String MobileNumber, final String Type) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().OTP + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            // Log.e("HAS_ERROR", " " + HAS_ERROR);
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONObject objectData = JsonMain.getJSONObject("data");
                                if (objectData.has("user")) {
                                    JSONObject objectUser = objectData.getJSONObject("user");
                                    UserID = objectUser.getString("user_id");
                                    PhoneNumber = objectUser.getString("phone_number");
                                    ProfilePicture = objectUser.getString("profile_pic");
                                }
                                Otp = objectData.getString("otp");
                                edtEnterCode.setText(Otp);
                                btnAgreeToGetStarted.setEnabled(true);
                            } else {
                                Toast.makeText(mContext, "No Data Available...", Toast.LENGTH_LONG).show();
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().OTP + params);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"mobile\":\"" + MobileNumber + "\",\"type\":\"" + Type + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().OTP + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().OTP);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void SignUpApi(final String NickName, final String Email, final String PhoneNumber, final String FCMToken, final String SocialID) {
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
                Log.e("HEADER", "" + APIConstant.getInstance().SIGN_UP + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"nick_name\":\"" + NickName + "\",\"email\":\"" + Email + "\",\"phone_number\":\"" + PhoneNumber +
                        "\",\"device_type\":\"" + "A" + "\",\"social_id\":\"" + SocialID + "\",\"referral_code\":\"" + "" +
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
