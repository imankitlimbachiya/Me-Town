package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ChangePhoneNumberActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    EditText edtNewNumber, edtVerificationNumber;
    TextView txtAskAnythingForHelp;
    Button btnSendCode, btnChangePhoneNumber;

    String Data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_number);

        Log.e("Activity","ChangePhoneNumberActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtAskAnythingForHelp = findViewById(R.id.txtAskAnythingForHelp);

        edtNewNumber = findViewById(R.id.edtNewNumber);
        edtVerificationNumber = findViewById(R.id.edtVerificationNumber);

        btnSendCode = findViewById(R.id.btnSendCode);
        btnChangePhoneNumber = findViewById(R.id.btnChangePhoneNumber);

        imgBack.setOnClickListener(this);
        btnSendCode.setOnClickListener(this);
        btnChangePhoneNumber.setOnClickListener(this);

        if (Data.equals("")) {
            btnChangePhoneNumber.setEnabled(false);
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
                String NewMobileNumber = edtNewNumber.getText().toString().trim();
                if (NewMobileNumber.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your New Mobile Number", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidMobile(NewMobileNumber)) {
                    Toast.makeText(mContext, "Please Enter Your Valid New Mobile Number", Toast.LENGTH_LONG).show();
                } else {
                    if (ConstantFunction.isNetworkAvailable(mContext)) {
                        OtpApi(NewMobileNumber, "2");
                    } else {
                        Toast.makeText(mContext, "Please Check Your Network Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnChangePhoneNumber:
                NewMobileNumber = edtNewNumber.getText().toString().trim();
                String VerificationNumber = edtVerificationNumber.getText().toString().trim();
                if (NewMobileNumber.equals("")) {
                    Toast.makeText(mContext, "Please Enter Your New Mobile Number", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidMobile(NewMobileNumber)) {
                    Toast.makeText(mContext, "Please Enter Your Valid New Mobile Number", Toast.LENGTH_LONG).show();
                } else if (VerificationNumber.equals("")) {
                    Toast.makeText(mContext, "Please Enter Verification Number", Toast.LENGTH_LONG).show();
                } else {
                    ChangeMobileApi(NewMobileNumber, VerificationNumber);
                }
                break;
        }
    }

    private void OtpApi(final String NewMobileNumber, final String Type) {
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
                                Data = JsonMain.getString("data");
                                Toast.makeText(mContext, Data, Toast.LENGTH_LONG).show();
                                edtVerificationNumber.setText(Data);
                                btnChangePhoneNumber.setEnabled(true);
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
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"mobile\":\"" + NewMobileNumber + "\",\"type\":\"" + Type + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().OTP + params);
                return params.getBytes();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().OTP);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void ChangeMobileApi(final String MobileNumber, final String Otp) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().CHANGE_MOBILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().CHANGE_MOBILE + response);
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

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"phone_number\":\"" + MobileNumber + "\",\"otp\":\"" + Otp + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().CHANGE_MOBILE + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().OTP);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
