package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
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

public class OrganiseMeetUpActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    Switch AlarmSwitch;
    TextView txtAppointmentTime, txtDone;
    EditText edtDateAndTime, edtMinuteBefore, edtMeetUpAddress, edtName, edtContact, edtDetails;
    String Alarm = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organise_meet_up);

        Log.e("Activity", "OrganiseMeetUpActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        ViewSetText();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtDone = findViewById(R.id.txtDone);
        txtAppointmentTime = findViewById(R.id.txtAppointmentTime);

        AlarmSwitch = findViewById(R.id.AlarmSwitch);

        edtDateAndTime = findViewById(R.id.edtDateAndTime);
        edtMinuteBefore = findViewById(R.id.edtMinuteBefore);
        edtMeetUpAddress = findViewById(R.id.edtMeetUpAddress);
        edtName = findViewById(R.id.edtName);
        edtContact = findViewById(R.id.edtContact);
        edtDetails = findViewById(R.id.edtDetails);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);

        AlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Alarm = "1";
                } else {
                    Alarm = "0";
                }
            }
        });
    }

    public void ViewSetText() {
        String AppointmentTime = "<font color='#000000'>Appointment time</font>" + " " + "<font color='#FFCE5D'><small>●</small></font>";
        txtAppointmentTime.setText(Html.fromHtml(AppointmentTime));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtDone:
                String AppointmentTime = edtDateAndTime.getText().toString().trim();
                String MinuteBefore = edtMinuteBefore.getText().toString().trim();
                String MeetUpAddress = edtMeetUpAddress.getText().toString().trim();
                String Name = edtName.getText().toString().trim();
                String Contact = edtContact.getText().toString().trim();
                String Details = edtDetails.getText().toString().trim();
                if (AppointmentTime.equals("")) {
                    Toast.makeText(mContext, "Please Enter your Appointment time.", Toast.LENGTH_LONG).show();
                } else if (MinuteBefore.equals("")) {
                    Toast.makeText(mContext, "Please Enter your Appointment time Minute before.", Toast.LENGTH_LONG).show();
                } else if (MeetUpAddress.equals("")) {
                    Toast.makeText(mContext, "Please Enter your Appointment Meet Up address.", Toast.LENGTH_LONG).show();
                } else if (Name.equals("")) {
                    Toast.makeText(mContext, "Please Enter your Appointment Name.", Toast.LENGTH_LONG).show();
                } else if (Contact.equals("")) {
                    Toast.makeText(mContext, "Please Enter your Appointment Contact.", Toast.LENGTH_LONG).show();
                } else if (!ConstantFunction.isValidMobile(Contact)) {
                    Toast.makeText(mContext, "Please Enter your valid Appointment Contact.", Toast.LENGTH_LONG).show();
                } else if (Details.equals("")) {
                    Toast.makeText(mContext, "Please Enter your Appointment Details.", Toast.LENGTH_LONG).show();
                } else {
                   AddOrganiseMeetUpApi(AppointmentTime, Alarm, MinuteBefore, Name, MeetUpAddress, Contact, Details, "24.18074693156234","72.40220123313713");
                }
                break;
        }
    }

    private void AddOrganiseMeetUpApi(final String AppointmentTime, final String Alarm, final String SetBefore, final String Name,
                                      final String MeetUpAddress, final String Contact, final String Details, final String Latitude,
                                      final String Longitude) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ADD_ORGANISE_MEET_UP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().ADD_ORGANISE_MEET_UP + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                String SuccessMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_ORGANISE_MEET_UP + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appointment_time", AppointmentTime);
                params.put("alarm", Alarm);
                params.put("set_before", SetBefore);
                params.put("name", Name);
                params.put("meet_up_address", MeetUpAddress);
                params.put("contact", Contact);
                params.put("details", Details);
                params.put("lat", Latitude);
                params.put("longi", Longitude);
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_ORGANISE_MEET_UP + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().ADD_ORGANISE_MEET_UP);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
