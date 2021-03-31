package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Fragment.Home;
import com.app.metown.Fragment.Inbox;
import com.app.metown.Fragment.MyPage;
import com.app.metown.Fragment.ServiceFilter;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    Context mContext;
    FrameLayout frameLayout;
    RelativeLayout mainBaseRelativeLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    public void setContentView(int layoutResID) {
        mainBaseRelativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        frameLayout = mainBaseRelativeLayout.findViewById(R.id.frameLayout);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(mainBaseRelativeLayout);

        Log.e("Activity", "BaseActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        loadFragment(new Home());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.Home:
                        fragment = new Home();
                        break;
                    case R.id.Categories:
                        fragment = new ServiceFilter();
                        break;
                    case R.id.Post:
                        ShowBottomSheetDialog();
                        break;
                    case R.id.Inbox:
                        fragment = new Inbox();
                        break;
                    case R.id.MyPage:
                        fragment = new MyPage();
                        break;
                }

                return loadFragment(fragment);
            }
        });
    }

    public void ViewInitialization() {
        frameLayout = findViewById(R.id.frameLayout);
        mainBaseRelativeLayout = findViewById(R.id.mainBaseRelativeLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);
    }

    public void ShowBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.bottom_dialog, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(view);
        dialog.findViewById(R.id.ForSaleLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Intent PostForSale = new Intent(mContext, PostForSaleActivity.class);
                startActivity(PostForSale);
            }
        });
        dialog.findViewById(R.id.MyCommunityLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Intent PostInMyCommunity = new Intent(mContext, PostInMyCommunityActivity.class);
                startActivity(PostInMyCommunity);
            }
        });
        dialog.findViewById(R.id.ForStoreServiceLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Intent PostForStoreAndService = new Intent(mContext, PostForStoreAndServiceActivity.class);
                startActivity(PostForStoreAndService);
            }
        });
        dialog.findViewById(R.id.HiringHelperLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Intent PostHiringHelper = new Intent(mContext, PostHiringHelperActivity.class);
                startActivity(PostHiringHelper);
            }
        });
        dialog.show();
    }

    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    // .addToBackStack(null)
                    // .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            return true;
        }
        return false;
    }

    private void GetProfileApi(final String UserID) {
        String req = "req";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_PROFILE + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONObject objectData = JsonMain.getJSONObject("data");
                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString("UserID", objectData.getString("id"));
                        sharedPreferencesEditor.putString("UniqueID", objectData.getString("unique_id"));
                        sharedPreferencesEditor.putString("SocialID", objectData.getString("social_id"));
                        sharedPreferencesEditor.putString("FullName", objectData.getString("full_name"));
                        sharedPreferencesEditor.putString("NickName", objectData.getString("nick_name"));
                        sharedPreferencesEditor.putString("Email", objectData.getString("email"));
                        sharedPreferencesEditor.putString("VerificationCode", objectData.getString("verification_code"));
                        sharedPreferencesEditor.putString("Verified", objectData.getString("verified"));
                        sharedPreferencesEditor.putString("PhoneNumber", objectData.getString("phone_number"));
                        sharedPreferencesEditor.putString("ProfilePicture", objectData.getString("profile_pic"));
                        sharedPreferencesEditor.putString("Otp", objectData.getString("otp"));
                        sharedPreferencesEditor.putString("InvitationCode", objectData.getString("invitation_code"));
                        sharedPreferencesEditor.putString("ReferralCode", objectData.getString("referral_code"));
                        sharedPreferencesEditor.putString("Status", objectData.getString("status"));
                        sharedPreferencesEditor.putString("PrimaryNotification", objectData.getString("primary_notification"));
                        sharedPreferencesEditor.putString("OtherNotification", objectData.getString("other_notification"));
                        sharedPreferencesEditor.putString("DoNotDisturb", objectData.getString("do_not_disturb"));
                        sharedPreferencesEditor.putString("ApproveSearchEngine", objectData.getString("approve_search_engine"));
                        sharedPreferencesEditor.putString("Vibration", objectData.getString("vibration"));
                        sharedPreferencesEditor.putString("UserRange", objectData.getString("user_range"));
                        sharedPreferencesEditor.putString("CreatedAt", objectData.getString("created_at"));
                        sharedPreferencesEditor.putString("UpdatedAt", objectData.getString("updated_at"));
                        sharedPreferencesEditor.putString("DeletedAt", objectData.getString("deleted_at"));
                        sharedPreferencesEditor.putString("AchievementCount", objectData.getString("achivementcount"));
                        sharedPreferencesEditor.putString("SecondaryCount", objectData.getString("secondarycount"));
                        sharedPreferencesEditor.putString("IsMerchant", objectData.getString("is_merchant"));
                        JSONObject objectMerchantDetail = objectData.getJSONObject("merchant_detail");
                        sharedPreferencesEditor.putString("MerchantID", objectMerchantDetail.getString("id"));
                        sharedPreferencesEditor.putString("MerchantCategoryID", objectMerchantDetail.getString("category_id"));
                        sharedPreferencesEditor.putString("MerchantStoreName", objectMerchantDetail.getString("name"));
                        sharedPreferencesEditor.putString("MerchantStoreAddress", objectMerchantDetail.getString("address"));
                        sharedPreferencesEditor.putString("MerchantStoreDetailAddress", objectMerchantDetail.getString("detailed_addr"));
                        sharedPreferencesEditor.putString("MerchantStoreImages", objectMerchantDetail.getString("images"));
                        sharedPreferencesEditor.putString("MerchantStoreDescription", objectMerchantDetail.getString("images"));
                        sharedPreferencesEditor.putString("MerchantStoreBusinessStartTime", objectMerchantDetail.getString("business_start"));
                        sharedPreferencesEditor.putString("MerchantStoreBusinessEndTime", objectMerchantDetail.getString("business_end"));
                        sharedPreferencesEditor.putString("MerchantStoreDayOff", objectMerchantDetail.getString("day_off"));
                        sharedPreferencesEditor.putString("MerchantStoreBenefits", objectMerchantDetail.getString("benefits"));
                        sharedPreferencesEditor.putString("MerchantStoreWebPage", objectMerchantDetail.getString("webpage"));
                        sharedPreferencesEditor.putString("MerchantStorePermit", objectMerchantDetail.getString("permit"));
                        sharedPreferencesEditor.putString("MerchantStoreLatitude", objectMerchantDetail.getString("lats"));
                        sharedPreferencesEditor.putString("MerchantStoreLongitude", objectMerchantDetail.getString("longs"));
                        sharedPreferencesEditor.putString("MerchantStoreLocationName", objectMerchantDetail.getString("location_name"));
                        sharedPreferencesEditor.putString("MerchantStoreCreatedAt", objectMerchantDetail.getString("created_at"));
                        sharedPreferencesEditor.putString("MerchantStoreUpdatedAt", objectMerchantDetail.getString("updated_at"));
                        sharedPreferencesEditor.putString("MerchantStoreDeletedAt", objectMerchantDetail.getString("deleted_at"));
                        sharedPreferencesEditor.apply();
                        sharedPreferencesEditor.commit();
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
            }
        }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_PROFILE + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", UserID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_PROFILE + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_PROFILE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String UserID = sharedPreferences.getString("UserID", "");
        GetProfileApi(UserID);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String UserID = sharedPreferences.getString("UserID", "");
        GetProfileApi(UserID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}