package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.ServiceKindModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyServiceTagActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    Button btnYouCanNotFindWhatYouAreLookingFor;
    RecyclerView ServiceKindView;
    ArrayList<ServiceKindModel> serviceKindList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service_tag);

        Log.e("Activity", "MyServiceTagActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetServiceKindApi();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        btnYouCanNotFindWhatYouAreLookingFor = findViewById(R.id.btnYouCanNotFindWhatYouAreLookingFor);

        ServiceKindView = findViewById(R.id.ServiceKindView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnYouCanNotFindWhatYouAreLookingFor.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnYouCanNotFindWhatYouAreLookingFor:
                Intent intent = new Intent(mContext, AddNewCategoryActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void GetServiceKindApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_SERVICE_KIND,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_SERVICE_KIND + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    ServiceKindModel serviceKindModel = new ServiceKindModel();
                                    serviceKindModel.setServiceKindID(arrayData.getJSONObject(i).getString("id"));
                                    serviceKindModel.setServiceKindBusinessID(arrayData.getJSONObject(i).getString("business_id"));
                                    serviceKindModel.setServiceKindMyServiceTag(arrayData.getJSONObject(i).getString("my_service_tag"));
                                    serviceKindModel.setServiceKindCreatedAt(arrayData.getJSONObject(i).getString("created_at"));
                                    serviceKindList.add(serviceKindModel);
                                }
                                if (serviceKindList.size() > 0) {
                                    ServiceKindAdapter serviceKindAdapter = new ServiceKindAdapter(mContext, serviceKindList);
                                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
                                    ServiceKindView.setLayoutManager(mLayoutManager);
                                    ServiceKindView.setItemAnimator(new DefaultItemAnimator());
                                    ServiceKindView.setAdapter(serviceKindAdapter);
                                    serviceKindAdapter.notifyDataSetChanged();
                                }
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
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_SERVICE_KIND + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"business_id\":\"" + "4" + "\",\"my_service_tag\":\"" + "this is test" + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SERVICE_KIND + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_SERVICE_KIND);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class ServiceKindAdapter extends RecyclerView.Adapter<ServiceKindAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ServiceKindModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtServiceItemName;
            ImageView imgCancel;

            MyViewHolder(View view) {
                super(view);

                txtServiceItemName = view.findViewById(R.id.txtServiceItemName);

                imgCancel = view.findViewById(R.id.imgCancel);
            }
        }

        public ServiceKindAdapter(Context mContext, ArrayList<ServiceKindModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_kind_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull final MyViewHolder holder, int position) {
            final ServiceKindModel serviceKindModel = arrayList.get(position);

            holder.txtServiceItemName.setText(serviceKindModel.getServiceKindMyServiceTag());

            holder.imgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Would you like to delete?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    DeleteServiceKindApi(serviceKindModel.getServiceKindID(),
                                            serviceKindModel.getServiceKindBusinessID(),
                                            serviceKindModel.getServiceKindMyServiceTag());
                                }
                            });
                    builder.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void DeleteServiceKindApi(final String ID, final String BusinessID, final String MyServicesTag) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().DELETE_SERVICE_KIND,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().DELETE_SERVICE_KIND + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                String msg = JsonMain.getString("msg");
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                                GetServiceKindApi();
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
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().DELETE_SERVICE_KIND + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"id\":\"" + ID + "\",\"business_id\":\"" + BusinessID + "\",\"my_service_tag\":\"" + MyServicesTag + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().DELETE_SERVICE_KIND + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().DELETE_SERVICE_KIND);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
