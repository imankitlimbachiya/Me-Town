package com.app.metown.UI;

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
import android.widget.EditText;
import android.widget.ImageView;
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
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.BusinessItemPriceModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PriceActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    Button btnAddPrice;
    RelativeLayout NoResponseLayout;
    RecyclerView PriceView;
    ArrayList<BusinessItemPriceModel> businessItemPriceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        Log.e("Activity", "PriceActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetBusinessItemPriceListApi();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        btnAddPrice = findViewById(R.id.btnAddPrice);

        PriceView = findViewById(R.id.PriceView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnAddPrice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnAddPrice:
                Intent AddPrice = new Intent(mContext, AddPriceActivity.class);
                AddPrice.putExtra("ID", "");
                AddPrice.putExtra("BusinessID", "");
                AddPrice.putExtra("ServiceItem", "");
                AddPrice.putExtra("PriceKind", "");
                startActivity(AddPrice);
                break;
        }
    }

    private void GetBusinessItemPriceListApi() {
        String req = "req";
        businessItemPriceList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_BUSINESS_ITEM_PRICE_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_BUSINESS_ITEM_PRICE_LIST + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            BusinessItemPriceModel businessItemPriceModel = new BusinessItemPriceModel();
                            businessItemPriceModel.setID(arrayData.getJSONObject(i).getString("id"));
                            businessItemPriceModel.setBusinessID(arrayData.getJSONObject(i).getString("business_id"));
                            businessItemPriceModel.setServiceItem(arrayData.getJSONObject(i).getString("service_item"));
                            businessItemPriceModel.setPriceKind(arrayData.getJSONObject(i).getString("price_kind"));
                            businessItemPriceModel.setPrice(arrayData.getJSONObject(i).getString("price"));
                            businessItemPriceModel.setAdditionalInfo(arrayData.getJSONObject(i).getString("additional_info"));
                            businessItemPriceModel.setIsMainMenu(arrayData.getJSONObject(i).getString("is_main_menu"));
                            businessItemPriceModel.setCreatedAt(arrayData.getJSONObject(i).getString("created_at"));
                            businessItemPriceList.add(businessItemPriceModel);
                        }
                        if (businessItemPriceList.size() > 0) {
                            NoResponseLayout.setVisibility(View.GONE);
                            PriceView.setVisibility(View.VISIBLE);
                            BusinessItemPriceAdapter businessItemPriceAdapter = new BusinessItemPriceAdapter(mContext, businessItemPriceList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            PriceView.setLayoutManager(mLayoutManager);
                            PriceView.setItemAnimator(new DefaultItemAnimator());
                            PriceView.setAdapter(businessItemPriceAdapter);
                            businessItemPriceAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String msg = JsonMain.getString("msg");
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                        PriceView.setVisibility(View.GONE);
                        NoResponseLayout.setVisibility(View.VISIBLE);
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_BUSINESS_ITEM_PRICE_LIST + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"business_id\":\"" + "4" + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_BUSINESS_ITEM_PRICE_LIST + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_BUSINESS_ITEM_PRICE_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class BusinessItemPriceAdapter extends RecyclerView.Adapter<BusinessItemPriceAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<BusinessItemPriceModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtMain, txtItemName;
            EditText edtPriceHere;
            ImageView imgDelete;

            MyViewHolder(View view) {
                super(view);

                txtMain = view.findViewById(R.id.txtMain);
                txtItemName = view.findViewById(R.id.txtItemName);

                edtPriceHere = view.findViewById(R.id.edtPriceHere);

                imgDelete = view.findViewById(R.id.imgDelete);
            }
        }

        public BusinessItemPriceAdapter(Context mContext, ArrayList<BusinessItemPriceModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_item_price_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull final MyViewHolder holder, int position) {
            final BusinessItemPriceModel businessItemPriceModel = arrayList.get(position);

            holder.txtItemName.setText(businessItemPriceModel.getServiceItem());

            holder.edtPriceHere.setText(businessItemPriceModel.getPrice());

            if (businessItemPriceModel.getIsMainMenu().equals("0")) {
                holder.txtMain.setVisibility(View.VISIBLE);
            } else {
                holder.txtMain.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent AddPrice = new Intent(mContext, AddPriceActivity.class);
                    AddPrice.putExtra("ID", businessItemPriceModel.getID());
                    AddPrice.putExtra("BusinessID", businessItemPriceModel.getBusinessID());
                    AddPrice.putExtra("ServiceItem", businessItemPriceModel.getServiceItem());
                    AddPrice.putExtra("PriceKind", businessItemPriceModel.getPriceKind());
                    mContext.startActivity(AddPrice);
                }
            });

            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteBusinessItemPriceApi(businessItemPriceModel.getID());
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void DeleteBusinessItemPriceApi(final String ID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().DELETE_BUSINESS_ITEM_PRICE, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().DELETE_BUSINESS_ITEM_PRICE + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        String msg = JsonMain.getString("msg");
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                        GetBusinessItemPriceListApi();
                    } else {
                        String msg = JsonMain.getString("msg");
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().DELETE_BUSINESS_ITEM_PRICE + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"id\":\"" + ID + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().DELETE_BUSINESS_ITEM_PRICE + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().DELETE_BUSINESS_ITEM_PRICE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        GetBusinessItemPriceListApi();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}