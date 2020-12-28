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

public class EmailVerifyActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    EditText edtEnterYourEmail, edtEnterCode;
    TextView txtPrivacyPolicy;
    Button btnSendCode, btnAgreeToGetStarted;

    String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        Log.e("Activity", "EmailVerifyActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        btnSendCode = findViewById(R.id.btnSendCode);
        btnAgreeToGetStarted = findViewById(R.id.btnAgreeToGetStarted);

        edtEnterYourEmail = findViewById(R.id.edtEnterYourEmail);
        edtEnterCode = findViewById(R.id.edtEnterCode);

        imgBack.setOnClickListener(this);
        btnSendCode.setOnClickListener(this);
        btnAgreeToGetStarted.setOnClickListener(this);

        txtPrivacyPolicy = findViewById(R.id.txtPrivacyPolicy);
        txtPrivacyPolicy.setPaintFlags(txtPrivacyPolicy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (code.equals("")) {
            btnAgreeToGetStarted.setEnabled(false);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnSendCode:
                String Email = edtEnterYourEmail.getText().toString().trim();
                if (Email.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Email", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidEmail(Email)) {
                    Toast.makeText(mContext, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
                } else {
                    if (ConstantFunction.isNetworkAvailable(mContext)) {
                        SendEmailVerificationCodeApi(Email);
                    } else {
                        Toast.makeText(mContext, "Please Check Your Network Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnAgreeToGetStarted:
                Email = edtEnterYourEmail.getText().toString().trim();
                String Code = edtEnterCode.getText().toString().trim();
                if (Email.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Email", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidEmail(Email)) {
                    Toast.makeText(mContext, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
                } else if (Code.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your Valid Email", Toast.LENGTH_SHORT).show();
                } else {
                    if (ConstantFunction.isNetworkAvailable(mContext)) {
                        EmailVerifyApi(Email, Code);
                    } else {
                        Toast.makeText(mContext, "Please Check Your Network Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void SendEmailVerificationCodeApi(final String Email) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().SEND_EMAIL_VERIFICATION_CODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().SEND_EMAIL_VERIFICATION_CODE + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            String Message = JsonMain.getString("msg");
                            Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                            code = JsonMain.getString("code");
                            edtEnterCode.setText(code);
                            btnAgreeToGetStarted.setEnabled(true);
                            // Log.e("HAS_ERROR", " " + HAS_ERROR);
                            /*if (HAS_ERROR.equalsIgnoreCase("false")) {
                                code = JsonMain.getString("code");
                                Toast.makeText(mContext, code, Toast.LENGTH_LONG).show();
                                edtEnterCode.setText(code);
                                btnAgreeToGetStarted.setEnabled(true);
                            } else {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                            }*/
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                return params;
            }

            /*protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", MobileNumber);
                params.put("type", Type);
                Log.e("PARAMETER", "" + APIConstant.getInstance().CHANGE_MOBILE + params);
                return params;
            }*/

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"email\":\"" + Email + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().SEND_EMAIL_VERIFICATION_CODE + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().SEND_EMAIL_VERIFICATION_CODE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void EmailVerifyApi(final String Email, final String Code) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().EMAIL_VERIFY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().EMAIL_VERIFY + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            String msg = JsonMain.getString("msg");
                            // Log.e("HAS_ERROR", " " + HAS_ERROR);
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                return params;
            }

            /*protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", MobileNumber);
                params.put("type", Type);
                Log.e("PARAMETER", "" + APIConstant.getInstance().CHANGE_MOBILE + params);
                return params;
            }*/

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"email\":\"" + Email + "\",\"code\":\"" + Code + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().EMAIL_VERIFY + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().EMAIL_VERIFY);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
