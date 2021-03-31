package com.app.metown.UI;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.NotificationModel;
import com.app.metown.R;
import com.app.metown.SwipeDelete.SwipeToDeleteCallback;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgEdit, imgDelete;
    TextView txtActivity, txtKeywordAlert, txtAddKeywordAlert, txtError;
    RecyclerView NotificationView, KeywordAlertView;
    NotificationAdapter notificationAdapter;
    ArrayList<NotificationModel> notificationList = new ArrayList<>();
    ArrayList<NotificationModel> keywordAlertList = new ArrayList<>();
    LinearLayout ActivityLayout, KeywordAlertLayout, ResponseLayout, NoResponseKeywordAlertLayout;
    RelativeLayout NoResponseLayout;
    View ActivityView, KeywordAlertsView;
    String Type = "1", offSet = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Log.e("Activity", "NotificationActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        GetNotificationListApi(Type, offSet);

        EnableSwipeToDeleteAndUndo(NotificationView);

        EnableSwipeToDeleteAndUndo(KeywordAlertView);
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgEdit = findViewById(R.id.imgEdit);
        imgDelete = findViewById(R.id.imgDelete);

        ActivityView = findViewById(R.id.ActivityView);
        KeywordAlertsView = findViewById(R.id.KeywordAlertsView);

        txtActivity = findViewById(R.id.txtActivity);
        txtKeywordAlert = findViewById(R.id.txtKeywordAlert);
        txtAddKeywordAlert = findViewById(R.id.txtAddKeywordAlert);
        txtError = findViewById(R.id.txtError);

        ActivityLayout = findViewById(R.id.ActivityLayout);
        KeywordAlertLayout = findViewById(R.id.KeywordAlertLayout);
        ResponseLayout = findViewById(R.id.ResponseLayout);
        NoResponseLayout = findViewById(R.id.NoResponseLayout);
        NoResponseKeywordAlertLayout = findViewById(R.id.NoResponseKeywordAlertLayout);

        NotificationView = findViewById(R.id.NotificationView);
        KeywordAlertView = findViewById(R.id.KeywordAlertView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
        ActivityLayout.setOnClickListener(this);
        KeywordAlertLayout.setOnClickListener(this);
        txtAddKeywordAlert.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.ActivityLayout:
                Type = "1";
                GetNotificationListApi(Type, offSet);
                break;
            case R.id.KeywordAlertLayout:
                Type = "2";
                GetNotificationListApi(Type, offSet);
                break;
            case R.id.imgEdit:
                Intent KeywordAlert = new Intent(mContext, KeywordAlertActivity.class);
                startActivity(KeywordAlert);
                break;
            case R.id.imgDelete:
                if (imgEdit.getVisibility() == View.VISIBLE) {
                    Type = "2";
                } else {
                    Type = "1";
                }
                ClearNotificationApi(Type, offSet);
                break;
            case R.id.txtAddKeywordAlert:
                KeywordAlert = new Intent(mContext, KeywordAlertActivity.class);
                startActivity(KeywordAlert);
                break;
        }
    }

    private void GetNotificationListApi(final String Type, final String offSet) {
        if (Type.equals("1")) {
            imgEdit.setVisibility(View.GONE);
            KeywordAlertView.setVisibility(View.GONE);
            NotificationView.setVisibility(View.VISIBLE);

            txtActivity.setTextColor(getResources().getColor(R.color.black));
            ActivityView.setBackgroundColor(getResources().getColor(R.color.black));
            txtKeywordAlert.setTextColor(getResources().getColor(R.color.grey));
            KeywordAlertsView.setBackgroundColor(getResources().getColor(R.color.grey));
        } else {
            imgEdit.setVisibility(View.VISIBLE);
            NotificationView.setVisibility(View.GONE);
            KeywordAlertView.setVisibility(View.VISIBLE);

            txtKeywordAlert.setTextColor(getResources().getColor(R.color.black));
            KeywordAlertsView.setBackgroundColor(getResources().getColor(R.color.black));
            txtActivity.setTextColor(getResources().getColor(R.color.grey));
            ActivityView.setBackgroundColor(getResources().getColor(R.color.grey));
        }
        String req = "req";
        notificationList.clear();
        keywordAlertList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().NOTIFICATION_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().NOTIFICATION_LIST + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            NotificationModel notificationModel = new NotificationModel();
                            notificationModel.setID(arrayData.getJSONObject(i).getString("id"));
                            notificationModel.setTitle(arrayData.getJSONObject(i).getString("title"));
                            notificationModel.setDetail(arrayData.getJSONObject(i).getString("detail"));
                            notificationModel.setUserID(arrayData.getJSONObject(i).getString("user_id"));
                            notificationModel.setCreatedBy(arrayData.getJSONObject(i).getString("created_by"));
                            notificationModel.setNotificationType(arrayData.getJSONObject(i).getString("notification_type"));
                            notificationModel.setReferenceID(arrayData.getJSONObject(i).getString("reference_id"));
                            notificationModel.setReferenceType(arrayData.getJSONObject(i).getString("reference_type"));
                            notificationModel.setCreatedAt(arrayData.getJSONObject(i).getString("created_at"));
                            notificationModel.setType(arrayData.getJSONObject(i).getString("type"));
                            notificationModel.setUpdatedAt(arrayData.getJSONObject(i).getString("updated_at"));
                            notificationModel.setDeletedAt(arrayData.getJSONObject(i).getString("deleted_at"));
                            notificationModel.setProductName(arrayData.getJSONObject(i).getString("product_name"));
                            notificationModel.setProductID(arrayData.getJSONObject(i).getString("product_id"));
                            notificationModel.setCategoryID(arrayData.getJSONObject(i).getString("category_id"));
                            notificationModel.setImages(arrayData.getJSONObject(i).getString("images"));
                            if (Type.equals("1")) {
                                notificationList.add(notificationModel);
                            } else {
                                keywordAlertList.add(notificationModel);
                            }
                        }
                        if (notificationList.size() > 0 || keywordAlertList.size() > 0) {
                            NoResponseLayout.setVisibility(View.GONE);
                            ResponseLayout.setVisibility(View.VISIBLE);
                            if (Type.equals("1")) {
                                notificationAdapter = new NotificationAdapter(mContext, notificationList);
                            } else {
                                notificationAdapter = new NotificationAdapter(mContext, keywordAlertList);
                            }
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            if (Type.equals("1")) {
                                KeywordAlertView.setVisibility(View.GONE);
                                NotificationView.setVisibility(View.VISIBLE);
                                NotificationView.setLayoutManager(mLayoutManager);
                                NotificationView.setItemAnimator(new DefaultItemAnimator());
                                NotificationView.setAdapter(notificationAdapter);
                            } else {
                                NotificationView.setVisibility(View.GONE);
                                KeywordAlertView.setVisibility(View.VISIBLE);
                                KeywordAlertView.setLayoutManager(mLayoutManager);
                                KeywordAlertView.setItemAnimator(new DefaultItemAnimator());
                                KeywordAlertView.setAdapter(notificationAdapter);
                            }
                            notificationAdapter.notifyDataSetChanged();
                        } else {
                            ResponseLayout.setVisibility(View.GONE);
                            imgDelete.setVisibility(View.GONE);
                            NoResponseLayout.setVisibility(View.VISIBLE);
                            if (Type.equals("1")) {
                                NoResponseKeywordAlertLayout.setVisibility(View.GONE);
                                txtError.setVisibility(View.VISIBLE);
                            } else {
                                NoResponseKeywordAlertLayout.setVisibility(View.VISIBLE);
                                txtError.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        String msg = JsonMain.getString("msg");
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                        ResponseLayout.setVisibility(View.GONE);
                        imgDelete.setVisibility(View.GONE);
                        NoResponseLayout.setVisibility(View.VISIBLE);
                        if (Type.equals("1")) {
                            NoResponseKeywordAlertLayout.setVisibility(View.GONE);
                            txtError.setVisibility(View.VISIBLE);
                            txtError.setText(msg);
                        } else {
                            NoResponseKeywordAlertLayout.setVisibility(View.VISIBLE);
                            txtError.setVisibility(View.GONE);
                        }
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
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Transfer-Encoding", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().NOTIFICATION_LIST + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", Type);
                params.put("offset", offSet);
                Log.e("PARAMETER", "" + APIConstant.getInstance().NOTIFICATION_LIST + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().NOTIFICATION_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<NotificationModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtNotificationTitle;
            ImageView imgProduct;

            MyViewHolder(View view) {
                super(view);

                txtNotificationTitle = view.findViewById(R.id.txtNotificationTitle);

                imgProduct = view.findViewById(R.id.imgProduct);
            }
        }

        public NotificationAdapter(Context mContext, ArrayList<NotificationModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final NotificationModel notificationModel = arrayList.get(position);

            Glide.with(mContext).load(notificationModel.getImages()).into(holder.imgProduct);

            holder.txtNotificationTitle.setText(notificationModel.getTitle());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public void removeItem(int position) {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }

        public void restoreItem(NotificationModel notificationModel, int position) {
            arrayList.add(position, notificationModel);
            notifyItemInserted(position);
        }

        public ArrayList<NotificationModel> getData() {
            return arrayList;
        }
    }

    private void EnableSwipeToDeleteAndUndo(final RecyclerView recyclerView) {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(mContext) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final NotificationModel notificationModel = notificationAdapter.getData().get(position);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setTitle("Confirm Delete..!!!");
                alertDialogBuilder.setIcon(R.drawable.delete);
                alertDialogBuilder.setMessage("Are you sure, You want to delete");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        notificationAdapter.removeItem(position);
                        DeleteNotificationApi(notificationModel.getID());
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        notificationAdapter.notifyItemChanged(position);
                    }
                });
                alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        notificationAdapter.notifyItemChanged(position);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void DeleteNotificationApi(final String ID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().DELETE_NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().DELETE_NOTIFICATION + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String msg = JsonMain.getString("msg");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                        if (imgEdit.getVisibility() == View.VISIBLE) {
                            Type = "2";
                        } else {
                            Type = "1";
                        }
                        GetNotificationListApi(Type, offSet);
                    } else {
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
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Transfer-Encoding", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().DELETE_NOTIFICATION + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().DELETE_NOTIFICATION + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().DELETE_NOTIFICATION);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void ClearNotificationApi(final String Type, final String offSet) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().CLEAR_NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().CLEAR_NOTIFICATION + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String msg = JsonMain.getString("msg");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                        GetNotificationListApi(Type, offSet);
                    } else {
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
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Transfer-Encoding", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().CLEAR_NOTIFICATION + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", Type);
                params.put("offset", offSet);
                Log.e("PARAMETER", "" + APIConstant.getInstance().CLEAR_NOTIFICATION + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().CLEAR_NOTIFICATION);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}