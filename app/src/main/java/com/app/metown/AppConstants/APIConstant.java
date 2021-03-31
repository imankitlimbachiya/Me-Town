package com.app.metown.AppConstants;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.VolleySupport.AppController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIConstant {

    private static APIConstant apiConstant;

    public synchronized static APIConstant getInstance() {
        if (apiConstant == null)
            apiConstant = new APIConstant();
        return apiConstant;
    }

    // private final String BASE_URL = "http://metown.ap-southeast-1.elasticbeanstalk.com/";
    private final String BASE_URL = "http://comma-app.ap-southeast-1.elasticbeanstalk.com/";

    // Without Login Api
    public final String OTP = BASE_URL + "api/otp";
    public final String SIGN_UP = BASE_URL + "api/signup";
    public final String LOG_IN = BASE_URL + "api/login";
    public final String GET_CATEGORY = BASE_URL + "api/get-category?";
    public final String FILTER = BASE_URL + "api/get-filter?";
    public final String FAQ_LIST = BASE_URL + "api/faq-list";
    public final String FAQ_DETAIL = BASE_URL + "api/faq-detail/";
    public final String GET_BARANGAY = BASE_URL + "api/get-barangay?";

    // With Login Api
    public final String LOGOUT = BASE_URL + "api/logout";
    public final String EDIT_PROFILE = BASE_URL + "api/edit-profile";
    public final String CHANGE_MOBILE = BASE_URL + "api/change-mobile";
    public final String SEND_EMAIL_VERIFICATION_CODE = BASE_URL + "api/email/send-verification-code";
    public final String EMAIL_VERIFY = BASE_URL + "api/email/verify";
    public final String POST_SALE = BASE_URL + "api/post-sale";
    public final String POST_STORE_SERVICE = BASE_URL + "api/post-store-services";
    public final String MY_PURCHASE = BASE_URL + "api/mypurchase";
    public final String MY_ACTIVE_SALES = BASE_URL + "api/myActiveSales";
    public final String MY_SOLD_SALES = BASE_URL + "api/mySoldSales";
    public final String SOLD_ITEM = BASE_URL + "api/sold-item";
    public final String MY_HIDDEN_SALES = BASE_URL + "api/myHiddenSales";
    public final String MY_SALE_UPDATE_STATUS = BASE_URL + "api/mysales/update-status";
    public final String MY_SALE_DELETE = BASE_URL + "api/mySalesDelete";
    public final String ADD_EDIT_FAVORITE = BASE_URL + "api/add-edit-favorite";
    public final String GET_FAVORITE_LIST = BASE_URL + "api/get-favorite-list";
    public final String GET_SALE_LIST = BASE_URL + "api/getSalesList";
    public final String GET_USER_LIST = BASE_URL + "api/getUserList";
    public final String SAVE_LOCATION = BASE_URL + "api/save-location";
    public final String GET_LOCATION = BASE_URL + "api/get-location";
    public final String MY_ALL_SALE = BASE_URL + "api/myAllSales";
    public final String SET_KEYWORD = BASE_URL + "api/set-keywords";
    public final String SET_JOB_KEYWORD = BASE_URL + "api/set-job-keywords";
    public final String GET_KEYWORD = BASE_URL + "api/get-keywords";
    public final String GET_JOB_KEYWORD = BASE_URL + "api/get-job-keywords";
    public final String REGISTER_MERCHANT = BASE_URL + "api/register-merchant";
    public final String REGISTER_BUSINESS = BASE_URL + "api/register-business";
    public final String GET_PHRASES = BASE_URL + "api/get-phrases";
    public final String SET_PHRASES = BASE_URL + "api/set-phrase";
    public final String DELETE_PHRASES = BASE_URL + "api/delete-phrase/";
    public final String GET_BUSINESS_ITEM_PRICE_LIST = BASE_URL + "api/get-businessitemprices-list";
    public final String ADD_EDIT_BUSINESS_ITEM_PRICE = BASE_URL + "api/add-edit-businessitemprices";
    public final String ADD_SERVICE_KIND = BASE_URL + "api/add-servicekind";
    public final String GET_SERVICE_KIND = BASE_URL + "api/get-servicekind";
    public final String USER_SETTING = BASE_URL + "api/user-setting";
    public final String DELETE_SERVICE_KIND = BASE_URL + "api/delete-servicekind";
    public final String DELETE_BUSINESS_ITEM_PRICE = BASE_URL + "api/delete-businessitemprices";
    public final String ADD_EDIT_BLOCK_HIDDEN_USER = BASE_URL + "api/add-edit-blockhidden-user?";
    public final String GET_BLOCK_HIDDEN_USER = BASE_URL + "api/get-blockhidden-user?";
    public final String DELETE_BLOCK_HIDDEN_USER = BASE_URL + "api/delete-blockhidden-user?";
    public final String GET_TOPIC = BASE_URL + "api/get-topic";
    public final String ADD_COMMUNITY = BASE_URL + "api/add-community";
    public final String ADD_POST_HIRE_HELPER = BASE_URL + "api/add-posthire-helper";
    public final String POST_HIRE_HELPER_LIST = BASE_URL + "api/posthire-helper-list";
    public final String GET_ORGANISE_MEET_UP = BASE_URL + "api/get-organise-meetup";
    public final String ADD_ORGANISE_MEET_UP = BASE_URL + "api/add-organise-meetup";
    public final String APPLY_COMMUNITY = BASE_URL + "api/apply-community";
    public final String FOLLOW_USER = BASE_URL + "api/follow-user";
    public final String GET_FOLLOW_USER = BASE_URL + "api/get-follow-user";
    public final String TOPIC_FOLLOW = BASE_URL + "api/topic-follow";
    public final String ADD_TOPIC = BASE_URL + "api/add-topic";
    public final String GET_TOWN_WITH_USER_POST_SALE_COUNT = BASE_URL + "api/getTownWithUserPostsaleCount";
    public final String POST_SALE_CHANGE_TOWN = BASE_URL + "api/postsaleChangeTown";
    public final String GET_SEARCH_SECOND_HAND = BASE_URL + "api/get-search-second-hand";
    public final String ADD_EDIT_COMMENT = BASE_URL + "api/add-edit-comment";
    public final String GET_COMMUNITY = BASE_URL + "api/get-community";
    public final String TOWN_STATUS = BASE_URL + "api/town-status";
    public final String OPEN_TOWN_LIST = BASE_URL + "api/open-town-list";
    public final String STORE_PRODUCT_SEARCH = BASE_URL + "api/storeproductsearch";
    public final String MY_COMMUNITY = BASE_URL + "api/mycommunity";
    public final String GET_COMMENT_LIST = BASE_URL + "api/get-comment-list";
    public final String DELETE_USER = BASE_URL + "api/deleteUser";
    public final String GET_PROFILE = BASE_URL + "api/getprofile";
    public final String POPULAR_KEYWORD = BASE_URL + "api/getpopulerkeyword";
    public final String NOTIFICATION_LIST = BASE_URL + "api/getNotificationList";
    public final String DELETE_NOTIFICATION = BASE_URL + "api/deleteNotification";
    public final String CLEAR_NOTIFICATION = BASE_URL + "api/clearNotification";
    public final String SEND_MESSAGE = BASE_URL + "api/chat/send-message";
    public final String GET_CONVERSATION_LIST = BASE_URL + "api/chat/get-conversation-list";
    public final String GET_CONVERSATION_MESSAGE_LIST = BASE_URL + "api/chat/get-conversation-message-list";
    public final String LEAVE_CHAT = BASE_URL + "api/chat/leave-chat";
    public final String BLOCK_UNBLOCK = BASE_URL + "api/chat/block-unblock";
    public final String GET_QUE_ANS_LIST = BASE_URL + "api/getquestionAnsList";
    public final String SAVE_QUE_ANS = BASE_URL + "api/savequestionanswer";
    public final String SUPER_ACHIEVEMENT = BASE_URL + "api/getSuperAchivement";
    public final String NORMAL_ACHIEVEMENT = BASE_URL + "api/getNormalAchivement";
    public final String ALL_BUYER_SELLER_REVIEW = BASE_URL + "api/getAllBuyerSellerReview";
    public final String STORE_MERCHANT_PROFILE = BASE_URL + "api/getstoremerchantprofile";
    public final String GET_SERVICE_NEARBY = BASE_URL + "api/get-service-nearby";
    public final String MY_COMMUNITY_COMMENT = BASE_URL + "api/myCommunityComment";
    public final String ADD_COUPON_FOR_PRODUCT = BASE_URL + "api/add-coupons-for-product";
    public final String MERCHANT_COUPON_LIST = BASE_URL + "api/merchantCouponList";
    public final String ADD_ADVERTISING = BASE_URL + "api/add-advertising";
    public final String GET_ANNOUNCEMENT_LIST = BASE_URL + "api/getAnnoucementList";
    public final String ADD_INQUIRY = BASE_URL + "api/add-inquiry";
    public final String GET_ADVERTISING_LIST = BASE_URL + "api/get-advertising-list";
    public final String GET_STORE_MERCHANT_WITH_HELPER_PROFILE = BASE_URL + "api/getstoremerchantwithhelperprofile";
    public final String ADD_STORE_REVIEW = BASE_URL + "api/add-store-review";
    public final String DELETE_MERCHANT = BASE_URL + "api/deleteMerchant";
    public final String USER_DOWNLOAD_COUPON_LIST = BASE_URL + "api/userdownloadCouponList";
    public final String MY_SALE_SOLD_REVIEW = BASE_URL + "api/getMySaleSoldReview";

    public final String GET_ADVERTISING_WITH_COUPON_LIST = BASE_URL + "api/get-advertisingwithcoupons-list";
    public final String ADD_COUPON = BASE_URL + "api/add-coupons";
    public final String GET_TOPIC_TYPE_KEYWORD = BASE_URL + "api/get-topictype-keyword";
    public final String GET_SERVICE_LIST = BASE_URL + "api/getServiceList";
    public final String COMMUNITY_TOWN_STATUS = BASE_URL + "api/community-town-status";
    public final String ADD_INBOX_COUPON_DOWNLOAD = BASE_URL + "api/add-inbox-coupon-download";
    public final String GET_INBOX_LIST_USER = BASE_URL + "api/get-inbox-list-user";
    public final String GET_INBOX_LIST_MERCHANT = BASE_URL + "api/get-inbox-list-merchant";
    public final String ADD_UPDATE_PRODUCT_VIEW_BY_USER = BASE_URL + "api/add-update-product-view-byuser";
    public final String GET_PRODUCT_VIEW_BY_USER = BASE_URL + "api/get-product-view-byuser";
    public final String INITIATE_CHAT = BASE_URL + "api/InitiateChat";
    public final String GET_SALE_LIST_BY_LAT_LONG = BASE_URL + "api/getSalesListbylatlong";
    public final String GET_SERVICE_LIST_BY_LAT_LONG = BASE_URL + "api/getServiceListbylatlong";
    public final String GET_BUSINESS_LIST_SEARCH = BASE_URL + "api/get-business-list-search";

    /*private void GetSaleListByLatLongApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_SALE_LIST_BY_LAT_LONG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_SALE_LIST_BY_LAT_LONG + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
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
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                // params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_SALE_LIST_BY_LAT_LONG + params);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String LocationLatitude = sharedPreferences.getString("LocationLatitude", "");
                String LocationLongitude = sharedPreferences.getString("LocationLongitude", "");
                String params = "{\"lats\":\"" + LocationLatitude + "\",\"longs\":\"" + LocationLongitude + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SALE_LIST_BY_LAT_LONG + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_SALE_LIST_BY_LAT_LONG);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }*/

    /*private void GetServiceListByLatLongApi(final String Latitude, final String Longitude) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_SERVICE_LIST_BY_LAT_LONG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_SERVICE_LIST_BY_LAT_LONG + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
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
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                // params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_SERVICE_LIST_BY_LAT_LONG + params);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"lats\":\"" + Latitude + "\",\"longs\":\"" + Longitude + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SERVICE_LIST_BY_LAT_LONG + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_SERVICE_LIST_BY_LAT_LONG);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }*/

    /*private void CommunityTownStatus() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().COMMUNITY_TOWN_STATUS,
                new Response.Listener<String>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().COMMUNITY_TOWN_STATUS + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray jsonArray = JsonMain.getJSONArray("data");
                                for (int i = 0; jsonArray.length() > i; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("name");
                                    String is_community_open = jsonObject.getString("is_community_open");
                                    String peoples = jsonObject.getString("peoples");
                                }
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
                Log.e("HEADER", "" + APIConstant.getInstance().COMMUNITY_TOWN_STATUS + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String LocationName = sharedPreferences.getString("LocationName", "");
                String params = "{\"name\":\"" + LocationName + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().TOWN_STATUS + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().COMMUNITY_TOWN_STATUS);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }*/

    /*private void communityOpenTownList() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().COMMUNITY_OPEN_TOWN_LIST,
                new Response.Listener<String>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().COMMUNITY_OPEN_TOWN_LIST + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray jsonArray = JsonMain.getJSONArray("data");
                                for (int i = 0; jsonArray.length() > i; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    OpenTownLIstModel openTownLIstModel = new OpenTownLIstModel();
                                    openTownLIstModel.setId(jsonObject.getString("id"));
                                    openTownLIstModel.setName(jsonObject.getString("name"));
                                    opentownList.add(openTownLIstModel);

                                }
                                if (opentownList.size() > 0) {
                                    OpenTownListAdapter openTownListAdapter = new OpenTownListAdapter(mContext, opentownList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    rc_open_town_list.setLayoutManager(mLayoutManager);
                                    rc_open_town_list.setItemAnimator(new DefaultItemAnimator());
                                    rc_open_town_list.setAdapter(openTownListAdapter);
                                    openTownListAdapter.notifyDataSetChanged();
                                }


                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
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
                Log.e("HEADER", "" + APIConstant.getInstance().COMMUNITY_OPEN_TOWN_LIST + params);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().COMMUNITY_OPEN_TOWN_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }*/

    /*private void GetAdvertisingWithCouponsListApi(final String AdvertiseID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_ADVERTISING_WITH_COUPON_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_ADVERTISING_WITH_COUPON_LIST + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                JSONArray arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; arrayData.length() > i; i++) {

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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_ADVERTISING_WITH_COUPON_LIST + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("advertise_id", AdvertiseID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_ADVERTISING_WITH_COUPON_LIST + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_ADVERTISING_WITH_COUPON_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }*/

    /*private void SetLocationApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().SAVE_LOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().SAVE_LOCATION + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                Toast.makeText(mContext, "Location Saved", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                String msg = JsonMain.getString("msg");
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().SAVE_LOCATION + params);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"location_name\":\"" + LocationName + "\",\"lats\":\"" + lat +
                        "\",\"longs\":\"" + lng + "\",\"user_range\":\"" + Range + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().SAVE_LOCATION + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().SAVE_LOCATION);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }*/
}