package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.app.metown.Fragment.Home;
import com.app.metown.Fragment.Inbox;
import com.app.metown.Fragment.MyPage;
import com.app.metown.Fragment.ServiceFilter;
import com.app.metown.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BaseSecondActivity extends AppCompatActivity {

    Context mContext;
    FrameLayout SecondFrameLayout;
    RelativeLayout mainSecondBaseRelativeLayout;
    BottomNavigationView SecondBottomNavigationView;

    @Override
    public void setContentView(int layoutResID) {
        mainSecondBaseRelativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_base_second, null);
        SecondFrameLayout = mainSecondBaseRelativeLayout.findViewById(R.id.SecondFrameLayout);
        getLayoutInflater().inflate(layoutResID, SecondFrameLayout, true);
        super.setContentView(mainSecondBaseRelativeLayout);

        Log.e("Activity", "BaseActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        // loadFragment(new Home());

        SecondBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.Negotiate:

                        break;
                    case R.id.FollowUser:

                        break;
                    case R.id.Favorite:

                        break;
                    case R.id.Chat:

                        break;
                }

                return loadFragment(fragment);
            }
        });
    }

    public void ViewInitialization() {
        SecondFrameLayout = findViewById(R.id.SecondFrameLayout);
        mainSecondBaseRelativeLayout = findViewById(R.id.mainSecondBaseRelativeLayout);
        SecondBottomNavigationView = findViewById(R.id.SecondBottomNavigationView);
        SecondBottomNavigationView.setItemIconTintList(null);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
