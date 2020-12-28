package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
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

    /*@Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
        Log.e("UserID","" + sharedPreferences.getString("UserID", ""));
        Log.e("UniqueID","" + sharedPreferences.getString("UniqueID", ""));
        Log.e("NickName","" + sharedPreferences.getString("NickName", ""));
        Log.e("Email","" + sharedPreferences.getString("Email", ""));
        Log.e("SocialID","" + sharedPreferences.getString("SocialID", ""));
        Log.e("PhoneNumber","" + sharedPreferences.getString("PhoneNumber", ""));
        Log.e("InvitationCode","" + sharedPreferences.getString("InvitationCode", ""));
        Log.e("Status","" + sharedPreferences.getString("Status", ""));
        Log.e("EmailVerify","" + sharedPreferences.getString("EmailVerify", ""));
        Log.e("ProfilePicture","" + sharedPreferences.getString("ProfilePicture", ""));
        Log.e("Token","" + sharedPreferences.getString("Token", ""));
        Log.e("Type","" + sharedPreferences.getString("Type", ""));
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
