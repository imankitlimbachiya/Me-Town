package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import com.app.metown.Models.ComplimentModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    CircleImageView imgUser;
    TextView txtDone, txtUserName;
    RecyclerView ReportQuestionView;
    ArrayList<ComplimentModel> reportList = new ArrayList<>();
    ArrayList<ComplimentModel> optionList = new ArrayList<>();
    String Type = "3", QuestionType = "1", UserType = "1", ToUserID = "", ToUserName = "", ToUserProfilePicture = "",
            ConversationID = "", ProductID = "";
    JSONArray jsonArray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Log.e("Activity", "ReportActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GetIntentData();

        ViewInitialization();

        ViewOnClick();

        GetQuestionAnswerList(Type, QuestionType, UserType);
    }

    public void GetIntentData() {
        ConversationID = getIntent().getStringExtra("ConversationID");
        ToUserID = getIntent().getStringExtra("ToUserID");
        ToUserName = getIntent().getStringExtra("ToUserName");
        ToUserProfilePicture = getIntent().getStringExtra("ToUserProfilePicture");
        ProductID = getIntent().getStringExtra("ProductID");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgUser = findViewById(R.id.imgUser);

        txtDone = findViewById(R.id.txtDone);
        txtUserName = findViewById(R.id.txtUserName);

        ReportQuestionView = findViewById(R.id.ReportQuestionView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
    }

    public void ViewSetText() {
        Glide.with(mContext).load(ToUserProfilePicture).into(imgUser);
        txtUserName.setText(ToUserName);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtDone:
                if (jsonArray.length() == 0) {
                    Toast.makeText(mContext, "Please choose answer.", Toast.LENGTH_LONG).show();
                } else {
                    SaveQuestionAnswerApi(jsonArray.toString());
                }
                break;
        }
    }

    private void GetQuestionAnswerList(final String Type, final String QuestionType, final String UserType) {
        String req = "req";
        reportList.clear();
        optionList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_QUE_ANS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_QUE_ANS_LIST + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            ComplimentModel complimentModel = new ComplimentModel();
                            complimentModel.setID(arrayData.getJSONObject(i).getString("id"));
                            complimentModel.setTitle(arrayData.getJSONObject(i).getString("title"));
                            complimentModel.setType(arrayData.getJSONObject(i).getString("type"));
                            complimentModel.setQuestionType(arrayData.getJSONObject(i).getString("question_type"));
                            complimentModel.setUserType(arrayData.getJSONObject(i).getString("user_type"));
                            complimentModel.setOptions(arrayData.getJSONObject(i).getJSONArray("options"));
                            reportList.add(complimentModel);
                        }
                        if (reportList.size() > 0) {
                            ReportQuestionAdapter reportQuestionAdapter = new ReportQuestionAdapter(mContext, reportList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            ReportQuestionView.setLayoutManager(mLayoutManager);
                            ReportQuestionView.setItemAnimator(new DefaultItemAnimator());
                            ReportQuestionView.setAdapter(reportQuestionAdapter);
                            reportQuestionAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_QUE_ANS_LIST + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"type\":\"" + Type + "\",\"question_type\":\"" + QuestionType + "\",\"user_type\":\"" + UserType + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_QUE_ANS_LIST + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_QUE_ANS_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class ReportQuestionAdapter extends RecyclerView.Adapter<ReportQuestionAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ComplimentModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtQuestion;
            RecyclerView ComplimentOptionView;
            RelativeLayout MainLayout, OtherLayout;

            MyViewHolder(View view) {
                super(view);

                txtQuestion = view.findViewById(R.id.txtQuestion);

                ComplimentOptionView = view.findViewById(R.id.ComplimentOptionView);

                MainLayout = view.findViewById(R.id.MainLayout);
                OtherLayout = view.findViewById(R.id.OtherLayout);
            }
        }

        public ReportQuestionAdapter(Context mContext, ArrayList<ComplimentModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.compliment_question_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            if (arrayList.size() == position) {
                holder.MainLayout.setVisibility(View.GONE);
                holder.OtherLayout.setVisibility(View.VISIBLE);

                holder.OtherLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ReportOtherReasonActivity.class);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                final ComplimentModel complimentModel = arrayList.get(position);

                holder.MainLayout.setVisibility(View.VISIBLE);
                holder.OtherLayout.setVisibility(View.GONE);

                holder.txtQuestion.setText(complimentModel.getTitle());

                try {
                    JSONArray arrayOptions = complimentModel.getOptions();
                    optionList.clear();
                    for (int i = 0; i < arrayOptions.length(); i++) {
                        ComplimentModel model = new ComplimentModel();
                        model.setAnswerID(arrayOptions.getJSONObject(i).getString("answer_id"));
                        model.setAnswer(arrayOptions.getJSONObject(i).getString("answer"));
                        model.setQuestionID(arrayOptions.getJSONObject(i).getString("question_id"));
                        optionList.add(model);
                    }
                    if (optionList.size() > 0) {
                        ReportAdapter reportAdapter = new ReportAdapter(mContext, optionList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                        holder.ComplimentOptionView.setLayoutManager(mLayoutManager);
                        holder.ComplimentOptionView.setItemAnimator(new DefaultItemAnimator());
                        holder.ComplimentOptionView.setAdapter(reportAdapter);
                        reportAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getItemCount() {
            // return arrayList.size();
            return arrayList.size() + 1;
        }
    }

    public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ComplimentModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            CheckBox btnSelect;
            TextView txtOptionName;

            MyViewHolder(View view) {
                super(view);

                btnSelect = view.findViewById(R.id.btnSelect);

                txtOptionName = view.findViewById(R.id.txtOptionName);
            }
        }

        public ReportAdapter(Context mContext, ArrayList<ComplimentModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.compliment_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final ComplimentModel complimentModel = arrayList.get(position);

            holder.btnSelect.setText("   " + complimentModel.getAnswer());

            holder.btnSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject ansObject = new JSONObject();
                        if (isChecked) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (jsonArray.getJSONObject(i).getString("question_id").equals(complimentModel.getQuestionID())) {
                                    ansObject.put("answer_ids", complimentModel.getAnswerID());
                                    jsonArray.getJSONObject(i).put("answer_ids", jsonArray.getJSONObject(i).getString("answer_ids") + ","
                                            + ansObject.getString("answer_ids"));
                                    Log.e("jsonArray", "" + jsonArray.toString());
                                    return;
                                }
                            }
                            jsonObject.put("question_id", complimentModel.getQuestionID());
                            jsonObject.put("answer_ids", complimentModel.getAnswerID());
                            jsonObject.put("type", Type);
                            jsonObject.put("conversation_id", ConversationID);
                            jsonObject.put("to_user_id", ToUserID);
                            jsonObject.put("product_id", ProductID);
                            jsonArray.put(jsonObject);
                            Log.e("jsonArray", "" + jsonArray.toString());
                        } else {
                            for (int j = 0; j < jsonArray.length(); j++) {
                                if (jsonArray.getJSONObject(j).getString("answer_ids").equals(complimentModel.getAnswerID())) {
                                    jsonArray.remove(j);
                                } else {
                                    String answer_ids = jsonArray.getJSONObject(j).getString("answer_ids");
                                    Log.e("answer_ids", "" + answer_ids);
                                    String[] separated = answer_ids.split(",");
                                    String ids = "";
                                    for (String string : separated) {
                                        if (!string.contains(complimentModel.getAnswerID())) {
                                            if (ids.equals("")) {
                                                ids = string;
                                            } else {
                                                ids = ids + "," + string;
                                            }
                                        }
                                    }
                                    Log.e("ids", "" + ids);
                                    jsonArray.getJSONObject(j).put("answer_ids", ids);
                                    Log.e("jsonArray", "" + jsonArray.toString());
                                    return;
                                }
                                Log.e("jsonArray", "" + jsonArray.toString());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void SaveQuestionAnswerApi(final String answer) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().SAVE_QUE_ANS, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().SAVE_QUE_ANS + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String ErrorMessage = JsonMain.getString("msg");
                    Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        finish();
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
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().SAVE_QUE_ANS + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"answer\":" + answer + "}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().SAVE_QUE_ANS + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().SAVE_QUE_ANS);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}