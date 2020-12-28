package com.app.metown.AppConstants;

public class APIConstant {

    private static APIConstant apiConstant;

    public synchronized static APIConstant getInstance() {
        if (apiConstant == null)
            apiConstant = new APIConstant();
        return apiConstant;
    }

    private final String BASE_URL = "http://metown.ap-southeast-1.elasticbeanstalk.com/";

    public final String SIGN_UP = BASE_URL + "api/signup";
    public final String LOG_IN = BASE_URL + "api/login";
    public final String OTP = BASE_URL + "api/otp";
    public final String LOGOUT = BASE_URL + "api/logout";
    public final String EDIT_PROFILE = BASE_URL + "api/edit-profile";
    public final String GET_CATEGORY = BASE_URL + "api/get-category?";
    public final String GET_BARANGAY = BASE_URL + "api/get-barangay?";
    public final String MY_ACTIVE_SALES = BASE_URL + "api/myActiveSales";
    public final String MY_SOLD_SALES = BASE_URL + "api/mySoldSales";
    public final String MY_HIDDEN_SALES = BASE_URL + "api/myHiddenSales";
    public final String MY_PURCHASE = BASE_URL + "api/mypurchase/1/1";
    public final String POST_SALE = BASE_URL + "api/post-sale";
    public final String MY_SALE_UPDATE_STATUS = BASE_URL + "api/mysales/update-status";
    public final String CHANGE_MOBILE = BASE_URL + "api/change-mobile";
    public final String SEND_EMAIL_VERIFICATION_CODE = BASE_URL + "api/email/send-verification-code";
    public final String EMAIL_VERIFY = BASE_URL + "api/email/verify";
    public final String ADD_EDIT_FAVORITE = BASE_URL + "api/add-edit-favorite";
    public final String GET_FAVORITE_LIST = BASE_URL + "api/get-favorite-list";
    public final String GET_SALE_LIST = BASE_URL + "api/getSalesList";
    public final String GET_SERVICE_LIST = BASE_URL + "api/getServiceList";
    public final String GET_USER_LIST = BASE_URL + "api/getUserList";
    public final String SAVE_LOCATION = BASE_URL + "api/save-location";
    public final String SET_KEYWORD = BASE_URL + "api/set-keywords";
    public final String GET_LOCATION = BASE_URL + "api/get-location";
    public final String GET_JOB_KEYWORD = BASE_URL + "api/get-job-keywords";
    public final String SET_JOB_KEYWORD = BASE_URL + "api/set-job-keywords";
    public final String GET_PHRASES = BASE_URL + "api/get-phrases";
    public final String SET_PHRASES = BASE_URL + "api/set-phrase";
    public final String DELETE_PHRASES = BASE_URL + "api/delete-phrase/";
    public final String FAQ_LIST = BASE_URL + "api/faq-list";
    public final String FAQ_DETAIL = BASE_URL + "api/faq-detail/";
    public final String REGISTER_MERCHANT = BASE_URL + "api/register-merchant";
    public final String REGISTER_BUSINESS = BASE_URL + "api/register-business";

    public int ISConstant = 1;
}
