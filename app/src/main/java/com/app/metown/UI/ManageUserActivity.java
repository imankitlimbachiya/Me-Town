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
import com.app.metown.Models.ManageUserModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageUserActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtError;
    EasyPopup mQQPop;;
    RecyclerView UserView;
    RelativeLayout ResponseLayout, NoResponseLayout;
    ArrayList<ManageUserModel> manageUserList = new ArrayList<>();
    String ID = "", ToUserID = "", Type = "", WhichUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        Log.e("Activity", "ManageUserActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        OpenEasyPopup();

        GetIntentData();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtError = findViewById(R.id.txtError);

        UserView = findViewById(R.id.UserView);

        ResponseLayout = findViewById(R.id.ResponseLayout);
        NoResponseLayout = findViewById(R.id.NoResponseLayout);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
    }

    public void GetIntentData() {
        WhichUser = getIntent().getStringExtra("WhichUser");

        if (WhichUser.equals("Block")) {
            GetBlockHiddenUserApi("2");
        } else if (WhichUser.equals("Hidden")) {
            GetBlockHiddenUserApi("1");
        }
    }

    public void OpenEasyPopup() {
        mQQPop = EasyPopup.create()
                .setContext(mContext)
                .setContentView(R.layout.manage_user_menu)
                // .setAnimationStyle(R.style.RightTop2PopAnim)
                .setOnViewListener(new EasyPopup.OnViewListener() {
                    @Override
                    public void initViews(View view, EasyPopup basePopup) {
                        TextView txtAddEdit = view.findViewById(R.id.txtAddEdit);
                        txtAddEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AddEditBlockHiddenUserApi(ToUserID, Type);
                            }
                        });
                        TextView txtDelete = view.findViewById(R.id.txtDelete);
                        txtDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DeleteBlockHiddenUserApi(ID);
                            }
                        });
                    }
                })
                .setFocusAndOutsideEnable(true)
                // .setBackgroundDimEnable(true)
                // .setDimValue(0.5f)
                // .setDimColor(Color.RED)
                // .setDimView(mTitleBar)
                .apply();
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

    private void GetBlockHiddenUserApi(final String Type) {
        String req = "req";
        manageUserList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_BLOCK_HIDDEN_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_BLOCK_HIDDEN_USER + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    ManageUserModel manageUserModel = new ManageUserModel();
                                    manageUserModel.setID(arrayData.getJSONObject(i).getString("id"));
                                    manageUserModel.setUserID(arrayData.getJSONObject(i).getString("user_id"));
                                    manageUserModel.setToUserID(arrayData.getJSONObject(i).getString("to_user_id"));
                                    manageUserModel.setType(arrayData.getJSONObject(i).getString("type"));
                                    manageUserModel.setCreatedAt(arrayData.getJSONObject(i).getString("created_at"));
                                    manageUserModel.setNickName(arrayData.getJSONObject(i).getString("nick_name"));
                                    manageUserList.add(manageUserModel);
                                }
                                if (manageUserList.size() > 0) {
                                    NoResponseLayout.setVisibility(View.GONE);
                                    ResponseLayout.setVisibility(View.VISIBLE);
                                    ManageUserAdapter manageUserAdapter = new ManageUserAdapter(mContext, manageUserList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    UserView.setLayoutManager(mLayoutManager);
                                    UserView.setItemAnimator(new DefaultItemAnimator());
                                    UserView.setAdapter(manageUserAdapter);
                                    manageUserAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                                ResponseLayout.setVisibility(View.GONE);
                                NoResponseLayout.setVisibility(View.VISIBLE);
                                txtError.setText(ErrorMessage);
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_BLOCK_HIDDEN_USER + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"type\":\"" + Type + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_BLOCK_HIDDEN_USER + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_BLOCK_HIDDEN_USER);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class ManageUserAdapter extends RecyclerView.Adapter<ManageUserAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ManageUserModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtNickName;
            RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                txtNickName = view.findViewById(R.id.txtNickName);

                OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public ManageUserAdapter(Context mContext, ArrayList<ManageUserModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_user_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, final int position) {
            final ManageUserModel manageUserModel = arrayList.get(position);

            holder.txtNickName.setText(manageUserModel.getNickName());

            holder.OptionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ID = "";
                    ToUserID = "";
                    Type = "";
                    ID = manageUserModel.getID();
                    ToUserID = manageUserModel.getToUserID();
                    Type = manageUserModel.getType();
                    mQQPop.showAtAnchorView(view, YGravity.BELOW, XGravity.LEFT, 50, -55);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void AddEditBlockHiddenUserApi(final String ToUserID, final String Type) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ADD_EDIT_BLOCK_HIDDEN_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().ADD_EDIT_BLOCK_HIDDEN_USER + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                mQQPop.dismiss();
                                GetBlockHiddenUserApi("1");
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
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_EDIT_BLOCK_HIDDEN_USER + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"to_user_id\":\"" + ToUserID + "\",\"type\":\"" + Type + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_EDIT_BLOCK_HIDDEN_USER + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().ADD_EDIT_BLOCK_HIDDEN_USER);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void DeleteBlockHiddenUserApi(final String ID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().DELETE_BLOCK_HIDDEN_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().DELETE_BLOCK_HIDDEN_USER + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                mQQPop.dismiss();
                                GetBlockHiddenUserApi("1");
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
                Log.e("HEADER", "" + APIConstant.getInstance().DELETE_BLOCK_HIDDEN_USER + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"id\":\"" + ID + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().DELETE_BLOCK_HIDDEN_USER + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().DELETE_BLOCK_HIDDEN_USER);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}