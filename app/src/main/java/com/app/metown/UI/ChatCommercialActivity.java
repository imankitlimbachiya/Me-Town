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
import com.app.metown.Adapters.InboxAdapter;
import com.app.metown.Adapters.ReviewOptionAdapter;
import com.app.metown.AppConstants.APIConstant;
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
    ImageView imgBack, imgCalendar;
    TextView txtUserName;
    RelativeLayout CheckCouponLayout, SeeReviewLayout, OptionLayout;
    EasyPopup mQQPop;
    String ToUserID = "", ConversationID = "";
    RecyclerView ConversationMessageView;
    ArrayList<ConversationModel> conversationMessageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_commercial);

        Log.e("Activity", "ChatCommercialActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GetIntentData();

        ViewInitialization();

        ViewOnClick();

        OpenEasyPopup();

        GetConversationMessageListApi(ToUserID, ConversationID);
    }

    public void GetIntentData() {
        ConversationID = getIntent().getStringExtra("ConversationID");
        ToUserID = getIntent().getStringExtra("ToUserID");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgCalendar = findViewById(R.id.imgCalendar);

        txtUserName = findViewById(R.id.txtUserName);

        CheckCouponLayout = findViewById(R.id.CheckCouponLayout);
        SeeReviewLayout = findViewById(R.id.SeeReviewLayout);
        OptionLayout = findViewById(R.id.OptionLayout);

        ConversationMessageView = findViewById(R.id.ConversationMessageView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        imgCalendar.setOnClickListener(this);
        txtUserName.setOnClickListener(this);
        SeeReviewLayout.setOnClickListener(this);
        OptionLayout.setOnClickListener(this);
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
                                Intent Report = new Intent(mContext, ReportActivity.class);
                                startActivity(Report);
                            }
                        });
                        TextView txtComplement = view.findViewById(R.id.txtComplement);
                        txtComplement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                Intent Compliment = new Intent(mContext, ComplimentActivity.class);
                                startActivity(Compliment);
                            }
                        });
                        TextView txtDisappointment = view.findViewById(R.id.txtDisappointment);
                        txtDisappointment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                Intent Disappointment = new Intent(mContext, DisappointmentActivity.class);
                                startActivity(Disappointment);
                            }
                        });
                        TextView txtBlock = view.findViewById(R.id.txtBlock);
                        txtBlock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                BlockUnblockApi(ConversationID, "1");
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
                params.put("Content-Type", "application/json");
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgCalendar:
                Intent OrganiseMeetUp = new Intent(mContext, OrganiseMeetUpActivity.class);
                startActivity(OrganiseMeetUp);
                break;
            /*case R.id.CheckCouponLayout:
                Intent ReceivedCoupon = new Intent(mContext, ReceivedCouponActivity.class);
                startActivity(ReceivedCoupon);
                break;*/
            case R.id.SeeReviewLayout:
                Intent Review = new Intent(mContext, ReviewActivity.class);
                startActivity(Review);
                break;
            case R.id.OptionLayout:
                mQQPop.showAtAnchorView(view, YGravity.BELOW, XGravity.LEFT, 50, -55);
                break;
        }
    }

    private void GetConversationMessageListApi(final String ToUserID, final String ConversationID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
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
                                    conversationMessageList.add(conversationModel);
                                }
                                if (conversationMessageList.size() > 0) {
                                    ConversationAdapter adapter = new ConversationAdapter(mContext, conversationMessageList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    ConversationMessageView.setLayoutManager(mLayoutManager);
                                    ConversationMessageView.setItemAnimator(new DefaultItemAnimator());
                                    ConversationMessageView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                                JSONObject ObjectOtherUserDetail = jsonObject.getJSONObject("other_user_detail");
                                String UserID = ObjectOtherUserDetail.getString("user_id");
                                String Name = ObjectOtherUserDetail.getString("name");
                                String profilePicture = ObjectOtherUserDetail.getString("profile_pic");
                                String UserRating = ObjectOtherUserDetail.getString("user_rating");
                                txtUserName.setText(Name);
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
                params.put("Content-Type", "application/json");
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

            // CircleImageView imgSender;
            // TextView txtSenderName, txtLastMessage;
            LinearLayout CouponLayout, ContactLayout, TextLayout;

            MyViewHolder(View view) {
                super(view);

                // imgSender = view.findViewById(R.id.imgSender);

                // txtSenderName = view.findViewById(R.id.txtSenderName);
                // txtLastMessage = view.findViewById(R.id.txtLastMessage);

                CouponLayout = view.findViewById(R.id.CouponLayout);
                ContactLayout = view.findViewById(R.id.ContactLayout);
                TextLayout = view.findViewById(R.id.TextLayout);
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

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final ConversationModel conversationModel = arrayList.get(position);

            Log.e("LastMessageType","" + conversationModel.getLastMessageType());

            if (conversationModel.getLastMessageType().equals("text")) {
                holder.TextLayout.setVisibility(View.VISIBLE);
            } else {

            }

            /*String SenderProfilePicture = conversationModel.getSenderProfilePicture();

            if (SenderProfilePicture.equals("") || SenderProfilePicture.equals("null") ||
                    SenderProfilePicture.equals(null) || SenderProfilePicture == null) {

            } else {
                Glide.with(mContext).load(SenderProfilePicture).into(holder.imgSender);
            }

            holder.txtSenderName.setText(conversationModel.getSenderName());
            holder.txtLastMessage.setText(conversationModel.getLastMessageBody());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ChatCommercial = new Intent(mContext, ChatCommercialActivity.class);
                    ChatCommercial.putExtra("ConversationID", conversationModel.getConversationID());
                    mContext.startActivity(ChatCommercial);
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}