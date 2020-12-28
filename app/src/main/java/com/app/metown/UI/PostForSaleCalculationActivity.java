package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.metown.R;

public class PostForSaleCalculationActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtSubmit, txtEnterAmount, txtReadBeforeMakingOffer;
    Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnZero,
            btnDoubleZero, btnDot, btnBS, btnCLR;
    LinearLayout BottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_for_sale_calculation);

        Log.e("Activity","PostForSaleCalculationActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        BottomLayout = findViewById(R.id.BottomLayout);

        btnOne = findViewById(R.id.btnOne);
        btnTwo = findViewById(R.id.btnTwo);
        btnThree = findViewById(R.id.btnThree);
        btnFour = findViewById(R.id.btnFour);
        btnFive = findViewById(R.id.btnFive);
        btnSix = findViewById(R.id.btnSix);
        btnSeven = findViewById(R.id.btnSeven);
        btnEight = findViewById(R.id.btnEight);
        btnNine = findViewById(R.id.btnNine);
        btnZero = findViewById(R.id.btnZero);
        btnDoubleZero = findViewById(R.id.btnDoubleZero);
        btnDot = findViewById(R.id.btnDot);
        btnBS = findViewById(R.id.btnBS);
        btnCLR = findViewById(R.id.btnCLR);

        txtEnterAmount = findViewById(R.id.txtEnterAmount);
        txtReadBeforeMakingOffer = findViewById(R.id.txtReadBeforeMakingOffer);

        txtReadBeforeMakingOffer.setPaintFlags(txtReadBeforeMakingOffer.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtSubmit = findViewById(R.id.txtSubmit);

        imgBack.setOnClickListener(this);

        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNine.setOnClickListener(this);
        btnZero.setOnClickListener(this);
        btnDoubleZero.setOnClickListener(this);
        btnDot.setOnClickListener(this);
        btnBS.setOnClickListener(this);
        btnCLR.setOnClickListener(this);

        txtSubmit.setOnClickListener(this);
        txtEnterAmount.setOnClickListener(this);
    }

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtSubmit:
                Intent HiringHelper = new Intent(mContext, HiringHelperActivity.class);
                startActivity(HiringHelper);
                break;
            case R.id.txtEnterAmount:
                BottomLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btnOne:
                AddAmount("1");
                break;
            case R.id.btnTwo:
                AddAmount("2");
                break;
            case R.id.btnThree:
                AddAmount("3");
                break;
            case R.id.btnFour:
                AddAmount("4");
                break;
            case R.id.btnFive:
                AddAmount("5");
                break;
            case R.id.btnSix:
                AddAmount("6");
                break;
            case R.id.btnSeven:
                AddAmount("7");
                break;
            case R.id.btnEight:
                AddAmount("8");
                break;
            case R.id.btnNine:
                AddAmount("9");
                break;
            case R.id.btnZero:
                AddAmount("0");
                break;
            case R.id.btnDoubleZero:
                AddAmount("00");
                break;
            case R.id.btnDot:
                AddAmount(".");
                break;
            case R.id.btnBS:
                BottomLayout.setVisibility(View.GONE);
                break;
            case R.id.btnCLR:
                txtEnterAmount.setText("");
                break;
        }
    }

    public void AddAmount(String Number) {
        String OldString = txtEnterAmount.getText().toString().trim();
        OldString = OldString + Number;
        txtEnterAmount.setText(OldString);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (BottomLayout.getVisibility() == View.VISIBLE) {
            BottomLayout.setVisibility(View.GONE);
        } else {
            finish();
        }
    }
}
