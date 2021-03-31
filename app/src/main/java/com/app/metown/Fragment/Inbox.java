package com.app.metown.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.ConversationModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inbox extends Fragment implements View.OnClickListener {

    public Inbox() {
        // Required empty public constructor
    }

    Context mContext;
    ProgressBar progressBar;
    RecyclerView InboxView;
    ArrayList<ConversationModel> inboxList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inbox, container, false);

        Log.e("Fragment", "Inbox");

        mContext = getActivity();

        ViewInitialization(view);

        ViewOnClick();

        GetConversationListApi();

        return view;
    }

    public void ViewInitialization(View view) {
        progressBar = view.findViewById(R.id.progressBar);

        InboxView = view.findViewById(R.id.InboxView);
    }

    public void ViewOnClick() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.imgAlert:
                Intent notification = new Intent(mContext, NotificationActivity.class);
                startActivity(notification);
                break;*/
        }
    }

    private void GetConversationListApi() {
        String req = "req";
        inboxList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_CONVERSATION_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_CONVERSATION_LIST + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONObject jsonObject = JsonMain.getJSONObject("data");
                        JSONArray arrayConversationList = jsonObject.getJSONArray("conversation_list");
                        for (int i = 0; i < arrayConversationList.length(); i++) {
                            ConversationModel conversationModel = new ConversationModel();
                            conversationModel.setConversationID(arrayConversationList.getJSONObject(i).getString("conversation_id"));
                            conversationModel.setSenderUserID(arrayConversationList.getJSONObject(i).getJSONObject("sender").getString("user_id"));
                            conversationModel.setSenderProfilePicture(arrayConversationList.getJSONObject(i).getJSONObject("sender").getString("profile_pic"));
                            conversationModel.setSenderName(arrayConversationList.getJSONObject(i).getJSONObject("sender").getString("name"));
                            conversationModel.setProductID(arrayConversationList.getJSONObject(i).getJSONObject("product").getString("id"));
                            conversationModel.setProductName(arrayConversationList.getJSONObject(i).getJSONObject("product").getString("name"));
                            conversationModel.setProductImages(arrayConversationList.getJSONObject(i).getJSONObject("product").getString("images"));
                            conversationModel.setProductPrice(arrayConversationList.getJSONObject(i).getJSONObject("product").getString("price"));
                            conversationModel.setLastMessageBody(arrayConversationList.getJSONObject(i).getJSONObject("last_message").getString("body"));
                            conversationModel.setLastMessageCreatedAt(arrayConversationList.getJSONObject(i).getJSONObject("last_message").getString("created_at"));
                            inboxList.add(conversationModel);
                        }
                        if (inboxList.size() > 0) {
                            InboxAdapter inboxAdapter = new InboxAdapter(mContext, inboxList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            InboxView.setLayoutManager(mLayoutManager);
                            InboxView.setItemAnimator(new DefaultItemAnimator());
                            InboxView.setAdapter(inboxAdapter);
                            inboxAdapter.notifyDataSetChanged();
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", mContext.MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Transfer-Encoding", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_CONVERSATION_LIST + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_CONVERSATION_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }
}