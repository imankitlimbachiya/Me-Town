package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.app.metown.Adapters.OpenTownAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.LocationModel;
import com.app.metown.Models.TownModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    Button btnApply, btnInviteFriend;
    TextView txtTownPeopleCount, txtLocation;
    LinearLayout OpenLocationLayout;
    RecyclerView OpenTownView, LocationView;
    ArrayList<TownModel> townList = new ArrayList<>();
    ArrayList<LocationModel> locationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Log.e("Activity", "LocationActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetUserDefault();

        TownStatusApi();

        OpenTownListApi();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        btnApply = findViewById(R.id.btnApply);
        btnInviteFriend = findViewById(R.id.btnInviteFriend);

        txtLocation = findViewById(R.id.txtLocation);
        txtTownPeopleCount = findViewById(R.id.txtTownPeopleCount);

        OpenLocationLayout = findViewById(R.id.OpenLocationLayout);

        OpenTownView = findViewById(R.id.OpenTownView);
        LocationView = findViewById(R.id.LocationView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        OpenLocationLayout.setOnClickListener(this);
        btnApply.setOnClickListener(this);
        btnInviteFriend.setOnClickListener(this);
    }

    public void GetUserDefault() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
        String LocationName = sharedPreferences.getString("LocationName", "");
        String Latitude = sharedPreferences.getString("Latitude", "");
        String Longitude = sharedPreferences.getString("Longitude", "");
        txtLocation.setText(LocationName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.OpenLocationLayout:
                // GetLocationApi();
                break;
            case R.id.btnApply:
                ApplyCommunityApi();
                break;
            case R.id.btnInviteFriend:
                Intent InviteFriend = new Intent(mContext, InviteFriendActivity.class);
                startActivity(InviteFriend);
                break;
        }
    }

    private void GetLocationApi() {
        String req = "req";
        locationList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().GET_LOCATION, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_LOCATION + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            LocationModel locationModel = new LocationModel();
                            locationModel.setID(arrayData.getJSONObject(i).getString("id"));
                            locationModel.setLocationName(arrayData.getJSONObject(i).getString("location_name"));
                            locationModel.setLatitude(arrayData.getJSONObject(i).getString("lats"));
                            locationModel.setLongitude(arrayData.getJSONObject(i).getString("longs"));
                            locationModel.setUserRange(arrayData.getJSONObject(i).getString("user_range"));
                            locationList.add(locationModel);
                        }
                        if (locationList.size() > 0) {
                            LocationView.setVisibility(View.VISIBLE);
                            LocationAdapter locationAdapter = new LocationAdapter(mContext, locationList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            LocationView.setLayoutManager(mLayoutManager);
                            LocationView.setItemAnimator(new DefaultItemAnimator());
                            LocationView.setAdapter(locationAdapter);
                            locationAdapter.notifyDataSetChanged();
                        }
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
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_LOCATION + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_LOCATION);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<LocationModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtLocation;

            MyViewHolder(View view) {
                super(view);

                txtLocation = view.findViewById(R.id.txtLocation);
            }
        }

        public LocationAdapter(Context mContext, ArrayList<LocationModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final LocationModel locationModel = arrayList.get(position);

            holder.txtLocation.setText(locationModel.getLocationName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // LocationName = locationModel.getLocationName();
                    // txtLocation.setText(LocationName);
                    // LocationView.setVisibility(View.GONE);
                    // TownStatusApi(LocationName);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void ApplyCommunityApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().APPLY_COMMUNITY, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().APPLY_COMMUNITY + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String Message = JsonMain.getString("msg");
                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
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
                Log.e("HEADER", "" + APIConstant.getInstance().APPLY_COMMUNITY + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String LocationName = sharedPreferences.getString("LocationName", "");
                String params = "{\"name\":\"" + LocationName + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().APPLY_COMMUNITY + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().APPLY_COMMUNITY);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void TownStatusApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().TOWN_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().TOWN_STATUS + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            String TownID = arrayData.getJSONObject(i).getString("id");
                            String TownName = arrayData.getJSONObject(i).getString("name");
                            String TownIsOpen = arrayData.getJSONObject(i).getString("is_open");
                            String TownRequirePeople = arrayData.getJSONObject(i).getString("requiredPeoples");
                            String TownPeople = arrayData.getJSONObject(i).getString("peoples");

                            txtTownPeopleCount.setText(TownPeople + "/" + TownRequirePeople);

                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("IsLocation", "1");
                            editor.putString("LocationID", TownID);
                            editor.apply();
                            editor.commit();

                            if (TownIsOpen.equals("1")) {
                                Intent Home = new Intent(mContext, HomeActivity.class);
                                startActivity(Home);
                                finish();
                            }
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();

                        txtTownPeopleCount.setText("0/0");
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
                Log.e("HEADER", "" + APIConstant.getInstance().TOWN_STATUS + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String LocationName = sharedPreferences.getString("LocationName", "");
                String params = "{\"name\":\"" + LocationName + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().TOWN_STATUS + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().TOWN_STATUS);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void OpenTownListApi() {
        String req = "req";
        townList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().OPEN_TOWN_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().OPEN_TOWN_LIST + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    TownModel townModel = new TownModel();
                                    townModel.setTownID(arrayData.getJSONObject(i).getString("id"));
                                    townModel.setTownName(arrayData.getJSONObject(i).getString("name"));
                                    townList.add(townModel);
                                }
                                if (townList.size() > 0) {
                                    OpenTownAdapter openTownListAdapter = new OpenTownAdapter(mContext, townList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    OpenTownView.setLayoutManager(mLayoutManager);
                                    OpenTownView.setItemAnimator(new DefaultItemAnimator());
                                    OpenTownView.setAdapter(openTownListAdapter);
                                    openTownListAdapter.notifyDataSetChanged();
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
                Log.e("HEADER", "" + APIConstant.getInstance().OPEN_TOWN_LIST + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().OPEN_TOWN_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}