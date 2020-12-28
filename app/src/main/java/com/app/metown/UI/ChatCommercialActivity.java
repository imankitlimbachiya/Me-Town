package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.metown.Adapters.ReviewOptionAdapter;
import com.app.metown.R;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

public class ChatCommercialActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack, imgCalendar;
    RelativeLayout CheckCouponLayout, SeeReviewLayout, OptionLayout;
    EasyPopup mQQPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_commercial);

        Log.e("Activity", "ChatCommercialActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgCalendar = findViewById(R.id.imgCalendar);

        CheckCouponLayout = findViewById(R.id.CheckCouponLayout);
        SeeReviewLayout = findViewById(R.id.SeeReviewLayout);
        OptionLayout = findViewById(R.id.OptionLayout);

        imgBack.setOnClickListener(this);
        imgCalendar.setOnClickListener(this);
        CheckCouponLayout.setOnClickListener(this);
        SeeReviewLayout.setOnClickListener(this);
        OptionLayout.setOnClickListener(this);

        mQQPop = EasyPopup.create()
                .setContext(mContext)
                .setContentView(R.layout.chat_option_menu)
                // .setAnimationStyle(R.style.RightTop2PopAnim)
                .setOnViewListener(new EasyPopup.OnViewListener() {
                    @Override
                    public void initViews(View view, EasyPopup basePopup) {
                        TextView txtReport = view.findViewById(R.id.txtReport);
                        txtReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                Intent Report = new Intent(mContext, ReportActivity.class);
                                startActivity(Report);
                            }
                        });
                        TextView txtComplement = view.findViewById(R.id.txtComplement);
                        txtComplement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                Intent Compliment = new Intent(mContext, ComplimentActivity.class);
                                startActivity(Compliment);
                            }
                        });
                        TextView txtDisappointment = view.findViewById(R.id.txtDisappointment);
                        txtDisappointment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                Intent Disappointment = new Intent(mContext, DisappointmentActivity.class);
                                startActivity(Disappointment);
                            }
                        });
                    }
                })
                .setFocusAndOutsideEnable(true)
                // .setBackgroundDimEnable(true)
                // .setDimValue(0.5f)
                // .setDimColor(Color.RED)
                // .setDimView(mTitleBar)
                .apply();
    }

    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgCalendar:
                Intent OrganiseMeetUp = new Intent(mContext, OrganiseMeetUpActivity.class);
                startActivity(OrganiseMeetUp);
                break;
            case R.id.CheckCouponLayout:
                Intent ReceivedCoupon = new Intent(mContext, ReceivedCouponActivity.class);
                startActivity(ReceivedCoupon);
                break;
            case R.id.SeeReviewLayout:
                Intent Review = new Intent(mContext, ReviewActivity.class);
                startActivity(Review);
                break;
            case R.id.OptionLayout:
                mQQPop.showAtAnchorView(view, YGravity.BELOW, XGravity.LEFT, 50, -55);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
