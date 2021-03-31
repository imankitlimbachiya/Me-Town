package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.app.metown.AppConstants.DateUtil;
import com.app.metown.Models.ConversationModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatCommercialActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgCalendar, imgEnter, imgProductOrStore;
    TextView txtUserName, txtProductOrStoreName;
    EditText edtMessage;
    RelativeLayout CheckCouponLayout, SeeReviewLayout, OptionLayout;
    EasyPopup mQQPop;
    ConversationAdapter adapter;
    String ToUserID = "", ToUserName = "", ConversationID = "", ProductID = "", ProductName = "", ProductImages = "", ProductPrice = "",
            ToUserProfilePicture = "", ISBlock;
    RecyclerView ConversationMessageView;
    ArrayList<ConversationModel> conversationMessageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_commercial);

        Log.e("Activity", "ChatCommercialActivity");

        mContext = this;

        getSupportActionBar().hide();

        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        GetIntentData();

        ViewInitialization();

        ViewOnClick();

        ViewSetText();

        OpenEasyPopup();

        GetConversationMessageListApi(ToUserID, ConversationID, "FirstTime");
    }

    public void GetIntentData() {
        ConversationID = getIntent().getStringExtra("ConversationID");
        ToUserID = getIntent().getStringExtra("ToUserID");
        ToUserName = getIntent().getStringExtra("ToUserName");
        ToUserProfilePicture = getIntent().getStringExtra("ToUserProfilePicture");
        ProductID = getIntent().getStringExtra("ProductID");
        ProductName = getIntent().getStringExtra("ProductName");
        ProductImages = getIntent().getStringExtra("ProductImages");
        ProductPrice = getIntent().getStringExtra("ProductPrice");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgCalendar = findViewById(R.id.imgCalendar);
        imgEnter = findViewById(R.id.imgEnter);
        imgProductOrStore = findViewById(R.id.imgProductOrStore);

        edtMessage = findViewById(R.id.edtMessage);

        txtUserName = findViewById(R.id.txtUserName);
        txtProductOrStoreName = findViewById(R.id.txtProductOrStoreName);

        CheckCouponLayout = findViewById(R.id.CheckCouponLayout);
        SeeReviewLayout = findViewById(R.id.SeeReviewLayout);
        OptionLayout = findViewById(R.id.OptionLayout);

        ConversationMessageView = findViewById(R.id.ConversationMessageView);

        SetAdapter();
    }

    public void SetAdapter() {
        adapter = new ConversationAdapter(mContext, conversationMessageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        ConversationMessageView.setLayoutManager(mLayoutManager);
        ConversationMessageView.setItemAnimator(new DefaultItemAnimator());
        ConversationMessageView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        imgCalendar.setOnClickListener(this);
        imgEnter.setOnClickListener(this);
        txtUserName.setOnClickListener(this);
        SeeReviewLayout.setOnClickListener(this);
        OptionLayout.setOnClickListener(this);
    }

    public void ViewSetText() {
        txtUserName.setText(ToUserName);
        txtProductOrStoreName.setText(ProductName);
        Glide.with(mContext).load(ProductImages).into(imgProductOrStore);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgCalendar:
                GoToNextActivity("OrganiseMeet");
                break;
            case R.id.imgEnter:
                String Message = edtMessage.getText().toString().trim();
                if (Message.equals("")) {
                    Toast.makeText(mContext, "Please enter message.", Toast.LENGTH_LONG).show();
                } else {
                    SendMessageApi(ToUserID, Message, ProductID, ConversationID);
                }
                break;
            case R.id.SeeReviewLayout:
                GoToNextActivity("Review");
                break;
            case R.id.OptionLayout:
                mQQPop.showAtAnchorView(view, YGravity.ABOVE, XGravity.RIGHT, 0, 0);
                break;
        }
    }

    public void OpenEasyPopup() {
        mQQPop = EasyPopup.create()
                .setContext(mContext)
                .setContentView(R.layout.chat_option_menu)
                // .setAnimationStyle(R.style.RightTop2PopAnim)
                .setOnViewListener(new EasyPopup.OnViewListener() {
                    @Override
                    public void initViews(View view, EasyPopup basePopup) {
                        TextView txtReport = view.findViewById(R.id.txtReport);
                        txtReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                GoToNextActivity("Report");
                            }
                        });
                        TextView txtSold = view.findViewById(R.id.txtSold);
                        txtSold.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                SoldItemApi(ToUserID, ProductID);
                            }
                        });
                        TextView txtComplement = view.findViewById(R.id.txtComplement);
                        txtComplement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                GoToNextActivity("Compliment");
                            }
                        });
                        TextView txtDisappointment = view.findViewById(R.id.txtDisappointment);
                        txtDisappointment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                GoToNextActivity("Disappointment");
                            }
                        });
                        TextView txtBlock = view.findViewById(R.id.txtBlock);
                        txtBlock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                ISBlock = "1";
                                BlockUnblockApi(ConversationID, ISBlock);
                            }
                        });
                        TextView txtLeaveChat = view.findViewById(R.id.txtLeaveChat);
                        txtLeaveChat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                LeaveChatApi(ConversationID);
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

    public void GoToNextActivity(final String Where) {
        Intent intent = null;
        switch (Where) {
            case "Report":
                intent = new Intent(mContext, ReportActivity.class);
                break;
            case "Compliment":
                intent = new Intent(mContext, ComplimentActivity.class);
                break;
            case "Disappointment":
                intent = new Intent(mContext, DisappointmentActivity.class);
                break;
            case "OrganiseMeet":
                intent = new Intent(mContext, OrganiseMeetUpActivity.class);
                break;
            case "Review":
                intent = new Intent(mContext, ReviewActivity.class);
                break;
        }
        intent.putExtra("ConversationID", ConversationID);
        intent.putExtra("ToUserID", ToUserID);
        intent.putExtra("ToUserName", ToUserName);
        intent.putExtra("ToUserProfilePicture", ToUserProfilePicture);
        intent.putExtra("ProductID", ProductID);
        startActivity(intent);
    }

    private void GetConversationMessageListApi(final String ToUserID, final String ConversationID, final String Time) {
        String req = "req";
        conversationMessageList.clear();
        if (Time.equals("FirstTime")) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_CONVERSATION_MESSAGE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_CONVERSATION_MESSAGE_LIST + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONObject jsonObject = JsonMain.getJSONObject("data");
                                JSONArray arrayMessageList = jsonObject.getJSONArray("message_list");
                                for (int i = 0; i < arrayMessageList.length(); i++) {
                                    ConversationModel conversationModel = new ConversationModel();
                                    conversationModel.setLastMessageType(arrayMessageList.getJSONObject(i).getString("type"));
                                    conversationModel.setLastMessageBody(arrayMessageList.getJSONObject(i).getString("body"));
                                    conversationModel.setLastMessageCreatedAt(arrayMessageList.getJSONObject(i).getString("created_at"));
                                    conversationModel.setSenderUserID(arrayMessageList.getJSONObject(i).getJSONObject("sender").getString("user_id"));
                                    conversationModel.setSenderProfilePicture(arrayMessageList.getJSONObject(i).getJSONObject("sender").getString("profile_pic"));
                                    conversationMessageList.add(conversationModel);
                                }
                                if (conversationMessageList.size() > 0) {
                                    // adapter.notifyDataSetChanged();
                                    ConversationMessageView.scrollToPosition(conversationMessageList.size() - 1);
                                    /*new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ConversationMessageView.scrollToPosition(conversationMessageList.size() - 1);
                                        }
                                    }, 1000);*/
                                }
                                /*JSONObject ObjectOtherUserDetail = jsonObject.getJSONObject("other_user_detail");
                                String UserID = ObjectOtherUserDetail.getString("user_id");
                                String Name = ObjectOtherUserDetail.getString("name");
                                String profilePicture = ObjectOtherUserDetail.getString("profile_pic");
                                String UserRating = ObjectOtherUserDetail.getString("user_rating");
                                txtUserName.setText(Name);*/
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
                // params.put("Content-Type", "application/json");
                params.put("Content-Transfer-Encoding", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_CONVERSATION_MESSAGE_LIST + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("conversation_id", ConversationID);
                params.put("touser_id", ToUserID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_CONVERSATION_MESSAGE_LIST + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_CONVERSATION_MESSAGE_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ConversationModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            CircleImageView imgReceiverText;
            TextView txtReceiverText, txtReceiverTime, txtSenderText, txtSenderTime;
            LinearLayout CouponLayout, ContactLayout;
            RelativeLayout TextLayout, ReceiverTextLayout, SenderTextLayout;

            MyViewHolder(View view) {
                super(view);

                imgReceiverText = view.findViewById(R.id.imgReceiverText);

                txtReceiverText = view.findViewById(R.id.txtReceiverText);
                txtReceiverTime = view.findViewById(R.id.txtReceiverTime);
                txtSenderText = view.findViewById(R.id.txtSenderText);
                txtSenderTime = view.findViewById(R.id.txtSenderTime);

                CouponLayout = view.findViewById(R.id.CouponLayout);
                ContactLayout = view.findViewById(R.id.ContactLayout);

                TextLayout = view.findViewById(R.id.TextLayout);
                ReceiverTextLayout = view.findViewById(R.id.ReceiverTextLayout);
                SenderTextLayout = view.findViewById(R.id.SenderTextLayout);
            }
        }

        public ConversationAdapter(Context mContext, ArrayList<ConversationModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final ConversationModel conversationModel = arrayList.get(position);

            SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
            String UserID = sharedPreferences.getString("UserID", "");
            String LastMessageType = conversationModel.getLastMessageType();
            if (LastMessageType.equals("text")) {
                holder.TextLayout.setVisibility(View.VISIBLE);
                String SenderUserID = conversationModel.getSenderUserID();
                if (SenderUserID.equals(UserID)) {
                    holder.ReceiverTextLayout.setVisibility(View.GONE);
                    holder.SenderTextLayout.setVisibility(View.VISIBLE);
                    holder.txtSenderText.setText(conversationModel.getLastMessageBody());
                    holder.txtSenderTime.setText(DateUtil.getTodayTime(conversationModel.getLastMessageCreatedAt()));
                } else {
                    holder.SenderTextLayout.setVisibility(View.GONE);
                    holder.ReceiverTextLayout.setVisibility(View.VISIBLE);
                    String SenderProfilePicture = conversationModel.getSenderProfilePicture();
                    if (SenderProfilePicture.equals("") || SenderProfilePicture.equals("null") ||
                            SenderProfilePicture.equals(null) || SenderProfilePicture == null) {
                    } else {
                        Glide.with(mContext).load(SenderProfilePicture).into(holder.imgReceiverText);
                    }
                    holder.txtReceiverText.setText(conversationModel.getLastMessageBody());
                    holder.txtReceiverTime.setText(DateUtil.getTodayTime(conversationModel.getLastMessageCreatedAt()));
                }
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void SendMessageApi(final String ToUserID, final String Body, final String ProductID, final String ConversationID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().SEND_MESSAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().SEND_MESSAGE + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        // String SuccessMessage = JsonMain.getString("msg");
                        // Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
                        GetConversationMessageListApi(ToUserID, ConversationID, "SecondTime");
                        edtMessage.setText("");
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
                // params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().SEND_MESSAGE + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("touser_id", ToUserID);
                params.put("type", "text");
                params.put("body", Body);
                params.put("product_id", ProductID);
                params.put("conversation_id", ConversationID);
                params.put("image", "");
                Log.e("PARAMETER", "" + APIConstant.getInstance().SEND_MESSAGE + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().SEND_MESSAGE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void BlockUnblockApi(final String ConversationID, final String ISBlock) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().BLOCK_UNBLOCK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().BLOCK_UNBLOCK + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                String SuccessMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Transfer-Encoding", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().BLOCK_UNBLOCK + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("is_block", ISBlock);
                params.put("conversation_id", ConversationID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().BLOCK_UNBLOCK + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().BLOCK_UNBLOCK);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void LeaveChatApi(final String ConversationID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().LEAVE_CHAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().LEAVE_CHAT + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                String SuccessMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
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
                params.put("Content-Transfer-Encoding", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().LEAVE_CHAT + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("conversation_id", ConversationID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().LEAVE_CHAT + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().LEAVE_CHAT);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void SoldItemApi(final String BuyerID, final String ProductID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().SOLD_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().SOLD_ITEM + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
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
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().SOLD_ITEM + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"buyer_id\":\"" + BuyerID + "\",\"product_id\":\"" + ProductID + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().SOLD_ITEM + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().SOLD_ITEM);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    /*Handler handler = new Handler();
    Runnable runnable;
    int delay = 2 * 1000;

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                //do something
                GetConversationMessageListApi(ToUserID, ConversationID, "SecondTime");
                handler.postDelayed(runnable, delay);
            }
        }, delay);
        super.onResume();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}