package com.app.metown.notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.R;
import com.app.metown.UI.BaseActivity;
import com.app.metown.UI.HomeActivity;
import com.app.metown.VolleySupport.AppController;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFireBaseMessagingService.class.getSimpleName();
    private NotificationUtil notificationUtils;

    @Override
    public void onNewToken(@NonNull String string) {
        super.onNewToken(string);
        Log.e("NEW_TOKEN", "" + string);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage == null)
            return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e("Remote Message Data", "" + remoteMessage.getData());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e("On Message Received", "" + e.getMessage());
            }
        } else {
            Log.e(TAG, "Data Payload Empty");
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e("Notification Body", "" + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtil.isAppIsInBackground(getApplicationContext())) {
            Log.e("MFMS", "## handleNotification if");

            Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
            resultIntent.putExtra("message", message);
            // check for image attachment
            showNotificationMessage(getApplicationContext(), getResources().getString(R.string.app_name), message, "", resultIntent);

            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(NotificationConfig.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtil notificationUtils = new NotificationUtil(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            Log.e("MFMS", "## handleNotification else");
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e("Json", "" + json);
        try {
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("body");
            Log.e("Json Title", "" + title);
            Log.e("Json Message", "" + message);
            if (!NotificationUtil.isAppIsInBackground(getApplicationContext())) {
                Log.e(TAG, "Handle Data Message In Background");
                // play notification sound
                NotificationUtil notificationUtils = new NotificationUtil(this);
                notificationUtils.playNotificationSound();
                Intent resultIntent = new Intent(getApplicationContext(), BaseActivity.class);
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("message", message);
                // check for image attachment
                showNotificationMessage(getApplicationContext(), title, message, "", resultIntent);
                Intent pushNotification = new Intent(NotificationConfig.PUSH_NOTIFICATION);
                pushNotification.putExtra("title", title);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            } else {
                NotificationUtil notificationUtils = new NotificationUtil(this);
                notificationUtils.playNotificationSound();
                Log.e(TAG, "Handle Data Message In Foreground");
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("message", message);
                showNotificationMessage(getApplicationContext(), title, message, "", resultIntent);
            }

            // NotificationCountApi();

        } catch (JSONException e) {
            Log.e("Json Exception", "" + e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtil(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtil(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    /*private void NotificationCountApi() {
        String req = "req";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().NOTIFICATION_COUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    Log.e("RESPONSE", "" + APIConstant.getInstance().NOTIFICATION_COUNT + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String STATUS = JsonMain.getString("status");
                    if (STATUS.equalsIgnoreCase("true")) {
                        // String SuccessMessage = JsonMain.getString("success_msg");
                        // Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
                        String BadgeCount = JsonMain.getString("badge_count");
                        APIConstant.ISPush.postValue(BadgeCount);
                    } else {
                        // String ErrorMessage = JsonMain.getString("error_msg");
                        // Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String UserID = sharedPreferences.getString("UserID", "");
                params.put("user_id", UserID);
                params.put("secretkey", APIConstant.getInstance().ApiSecretsKey);
                Log.e("PARAMETER", "" + APIConstant.getInstance().NOTIFICATION_COUNT + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().NOTIFICATION_COUNT);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }*/
}
