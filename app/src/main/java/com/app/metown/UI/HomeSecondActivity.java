package com.app.metown.UI;

import android.os.Bundle;
import android.util.Log;

import com.app.metown.R;

public class HomeSecondActivity extends BaseSecondActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_second);

        Log.e("Activity","HomeSecondActivity");
    }
}
