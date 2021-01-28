package com.app.metown.UI;

import android.os.Bundle;
import android.util.Log;

import com.app.metown.R;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.e("Activity", "HomeActivity");
    }
}
