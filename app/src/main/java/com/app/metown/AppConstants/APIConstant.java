package com.app.metown.AppConstants;

public class APIConstant {

    private static APIConstant apiConstant;

    public synchronized static APIConstant getInstance() {
        if (apiConstant == null)
            apiConstant = new APIConstant();
        return apiConstant;
    }

    private final String BASE_URL = "http://metown.ap-southeast-1.elasticbeanstalk.com/";

    // Without Login Api
    public final String OTP = BASE_URL + "api/otp";
    public final String SIGN_UP = BASE_URL + "api/signup";
    public final String LOG_IN = BASE_URL + "api/login";
    public final String GET_CATEGORY = BASE_URL + "api/get-category?";
    public final String FAQ_LIST = BASE_URL + "api/faq-list";
    public final String FAQ_DETAIL = BASE_URL + "api/faq-detail/";
    public final String GET_BARANGAY = BASE_URL + "api/get-barangay?";
    public final String GET_SALE_LIST_BY_LAT_LONG = BASE_URL + "api/getSalesListbylatlong";
    public final String GET_SERVICE_LIST_BY_LAT_LONG = BASE_URL + "api/getServiceListbylatlong";

    // With Login Api
    public final String LOGOUT = BASE_URL + "api/logout";
    public final String EDIT_PROFILE = BASE_URL + "api/edit-profile";
    public final String CHANGE_MOBILE = BASE_URL + "api/change-mobile";
    public final String SEND_EMAIL_VERIFICATION_CODE = BASE_URL + "api/email/send-verification-code";
    public final String EMAIL_VERIFY = BASE_URL + "api/email/verify";
    public final String POST_SALE = BASE_URL + "api/post-sale";
    public final String POST_STORE_SERVICE = BASE_URL + "api/post-store-services";
    public final String MY_PURCHASE = BASE_URL + "api/mypurchase/1/1";
    public final String MY_ACTIVE_SALES = BASE_URL + "api/myActiveSales";
    public final String MY_SOLD_SALES = BASE_URL + "api/mySoldSales";
    public final String MY_HIDDEN_SALES = BASE_URL + "api/myHiddenSales";
    public final String MY_SALE_UPDATE_STATUS = BASE_URL + "api/mysales/update-status";
    public final String ADD_EDIT_FAVORITE = BASE_URL + "api/add-edit-favorite";
    public final String GET_FAVORITE_LIST = BASE_URL + "api/get-favorite-list";
    public final String GET_SALE_LIST = BASE_URL + "api/getSalesList";
    public final String GET_SERVICE_LIST = BASE_URL + "api/getServiceList";
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

    public final String GET_SEARCH_SECOND_HAND = BASE_URL + "api/get-search-second-hand";
    public final String ADD_EDIT_COMMENT = BASE_URL + "api/add-edit-comment";
    public final String GET_COMMUNITY = BASE_URL + "api/get-community";
    public final String MY_COMMUNITY = BASE_URL + "api/mycommunity";
    public final String GET_COMMENT_LIST = BASE_URL + "api/get-comment-list";
    public final String DELETE_USER = BASE_URL + "api/deleteUser";

    public final String GET_SERVICE_NEARBY = BASE_URL + "api/get-service-nearby";
    public final String GET_BUSINESS_LIST_SEARCH = BASE_URL + "api/get-business-list-search";

    public final String GET_ADVERTISING_LIST = BASE_URL + "api/get-advertising-list";
    public final String GET_ADVERTISING_WITH_COUPON_LIST = BASE_URL + "api/get-advertisingwithcoupons-list";
    public final String ADD_COUPON = BASE_URL + "api/add-coupons";
    public final String ADD_ADVERTISING = BASE_URL + "api/add-advertising";
    public final String GET_TOPIC_TYPE_KEYWORD = BASE_URL + "api/get-topictype-keyword";

    public final String ADD_INBOX_COUPON_DOWNLOAD = BASE_URL + "api/add-inbox-coupon-download";
    public final String GET_INBOX_LIST_USER = BASE_URL + "api/get-inbox-list-user";
    public final String GET_INBOX_LIST_MERCHANT = BASE_URL + "api/get-inbox-list-merchant";
    public final String ADD_UPDATE_PRODUCT_VIEW_BY_USER = BASE_URL + "api/add-update-product-view-byuser";
    public final String GET_PRODUCT_VIEW_BY_USER = BASE_URL + "api/get-product-view-byuser";
    public final String MY_COMMUNITY_COMMENT = BASE_URL + "api/myCommunityComment";

    public int ISConstant = 1;
}
