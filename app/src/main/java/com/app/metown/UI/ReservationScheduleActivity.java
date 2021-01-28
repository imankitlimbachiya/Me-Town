package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.app.metown.Adapters.ReservationScheduleAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.OrganiseMeetUpModel;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReservationScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    RecyclerView ReservationScheduleView;
    ArrayList<OrganiseMeetUpModel> reservationScheduleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_schedule);

        Log.e("Activity", "ReservationScheduleActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetOrganiseMeetUpApi();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        ReservationScheduleView = findViewById(R.id.ReservationScheduleView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    private void GetOrganiseMeetUpApi() {
        String req = "req";
        reservationScheduleList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_ORGANISE_MEET_UP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_ORGANISE_MEET_UP + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    OrganiseMeetUpModel organiseMeetUpModel = new OrganiseMeetUpModel();
                                    organiseMeetUpModel.setID(arrayData.getJSONObject(i).getString("id"));
                                    organiseMeetUpModel.setAppointmentTime(arrayData.getJSONObject(i).getString("appointment_time"));
                                    organiseMeetUpModel.setAlarm(arrayData.getJSONObject(i).getString("alarm"));
                                    organiseMeetUpModel.setSetBefore(arrayData.getJSONObject(i).getString("set_before"));
                                    organiseMeetUpModel.setUserID(arrayData.getJSONObject(i).getString("user_id"));
                                    organiseMeetUpModel.setName(arrayData.getJSONObject(i).getString("name"));
                                    organiseMeetUpModel.setMeetUpAddress(arrayData.getJSONObject(i).getString("meet_up_address"));
                                    organiseMeetUpModel.setContact(arrayData.getJSONObject(i).getString("contact"));
                                    organiseMeetUpModel.setDetails(arrayData.getJSONObject(i).getString("details"));
                                    organiseMeetUpModel.setLatitude(arrayData.getJSONObject(i).getString("lat"));
                                    organiseMeetUpModel.setLongitude(arrayData.getJSONObject(i).getString("longi"));
                                    organiseMeetUpModel.setCreatedAt(arrayData.getJSONObject(i).getString("created_at"));
                                    reservationScheduleList.add(organiseMeetUpModel);
                                }
                                if (reservationScheduleList.size() > 0) {
                                    ReservationScheduleAdapter reservationScheduleAdapter = new ReservationScheduleAdapter(mContext, reservationScheduleList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    ReservationScheduleView.setLayoutManager(mLayoutManager);
                                    ReservationScheduleView.setItemAnimator(new DefaultItemAnimator());
                                    ReservationScheduleView.setAdapter(reservationScheduleAdapter);
                                    reservationScheduleAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_ORGANISE_MEET_UP + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_ORGANISE_MEET_UP);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
