package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    RelativeLayout FollowerLayout, ManageBlockUserLayout, ManageHiddenUserLayout, OtherSettingLayout, LogoutLayout, DeleteLayout;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch PrimaryNotificationSwitch, OtherNotificationSwitch, DoNotDisturbSwitch, VibrationSwitch;

    String PrimaryNotification = "1", Vibration = "1", DoNotDisturb = "1", OtherNotification = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Log.e("Activity", "SettingActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        FollowerLayout = findViewById(R.id.FollowerLayout);
        ManageBlockUserLayout = findViewById(R.id.ManageBlockUserLayout);
        ManageHiddenUserLayout = findViewById(R.id.ManageHiddenUserLayout);
        OtherSettingLayout = findViewById(R.id.OtherSettingLayout);
        LogoutLayout = findViewById(R.id.LogoutLayout);
        DeleteLayout = findViewById(R.id.DeleteLayout);

        PrimaryNotificationSwitch = findViewById(R.id.PrimaryNotificationSwitch);
        OtherNotificationSwitch = findViewById(R.id.OtherNotificationSwitch);
        DoNotDisturbSwitch = findViewById(R.id.DoNotDisturbSwitch);
        VibrationSwitch = findViewById(R.id.VibrationSwitch);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        FollowerLayout.setOnClickListener(this);
        ManageBlockUserLayout.setOnClickListener(this);
        ManageHiddenUserLayout.setOnClickListener(this);
        OtherSettingLayout.setOnClickListener(this);
        LogoutLayout.setOnClickListener(this);
        DeleteLayout.setOnClickListener(this);

        PrimaryNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PrimaryNotification = "1";
                } else {
                    PrimaryNotification = "0";
                }
                UserSettingApi();
            }
        });

        OtherNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    OtherNotification = "1";
                } else {
                    OtherNotification = "0";
                }
                UserSettingApi();
            }
        });

        DoNotDisturbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    DoNotDisturb = "1";
                } else {
                    DoNotDisturb = "0";
                }
                UserSettingApi();
            }
        });

        VibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Vibration = "1";
                } else {
                    Vibration = "0";
                }
                UserSettingApi();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.FollowerLayout:
                Intent Follower = new Intent(mContext, FollowerActivity.class);
                startActivity(Follower);
                break;
            case R.id.ManageBlockUserLayout:
                GoToManageUserActivity("Block");
                break;
            case R.id.ManageHiddenUserLayout:
                GoToManageUserActivity("Hidden");
                break;
            case R.id.OtherSettingLayout:
                Intent intent = new Intent(mContext, OtherSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.LogoutLayout:
                LogoutApi();
                break;
            case R.id.DeleteLayout:
                DeleteUserApi();
                break;
        }
    }

    public void GoToManageUserActivity(String WhichUser) {
        Intent ManageUser = new Intent(mContext, ManageUserActivity.class);
        ManageUser.putExtra("WhichUser", WhichUser);
        startActivity(ManageUser);
    }

    private void LogoutApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().LOGOUT + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            // Log.e("HAS_ERROR", " " + HAS_ERROR);
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                String SuccessMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
                                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                sharedPreferencesEditor.clear();
                                sharedPreferencesEditor.apply();
                                sharedPreferencesEditor.commit();
                                Intent Main = new Intent(mContext, MainActivity.class);
                                startActivity(Main);
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().LOGOUT + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().LOGOUT);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void UserSettingApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().USER_SETTING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().USER_SETTING + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                String msg = JsonMain.getString("msg");
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            } else {
                                String msg = JsonMain.getString("msg");
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("ERROR", "" + error.getMessage());
                    }
                }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                // params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().USER_SETTING + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String UserID = sharedPreferences.getString("UserID", "");
                String params = "{\"id\":\"" + UserID + "\",\"primary_notification\":\"" + PrimaryNotification +
                        "\",\"vibration\":\"" + Vibration + "\",\"do_not_disturb\":\"" + DoNotDisturb
                        + "\",\"other_notification\":\"" + OtherNotification + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().USER_SETTING + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().USER_SETTING);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void DeleteUserApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().DELETE_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().DELETE_USER + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                String SuccessMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
                                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                sharedPreferencesEditor.clear();
                                sharedPreferencesEditor.apply();
                                sharedPreferencesEditor.commit();
                                Intent Main = new Intent(mContext, MainActivity.class);
                                startActivity(Main);
                                finish();
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
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
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().DELETE_USER + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().DELETE_USER);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
