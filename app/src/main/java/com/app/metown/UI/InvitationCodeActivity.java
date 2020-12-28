package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.metown.AppConstants.APIConstant;
import com.app.metown.R;

public class InvitationCodeActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    Button btnSkip, btnDone;
    EditText edtTypeInvitationCodeHere;

    String InvitationCode = "", Email = "", MobileNumber = "", Otp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_code);

        Log.e("Activity","InvitationCodeActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        edtTypeInvitationCodeHere = findViewById(R.id.edtTypeInvitationCodeHere);

        btnSkip = findViewById(R.id.btnSkip);
        btnDone = findViewById(R.id.btnDone);

        imgBack.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        MobileNumber = getIntent().getStringExtra("MobileNumber");
        Otp = getIntent().getStringExtra("Otp");
        Email = getIntent().getStringExtra("Email");
        // Log.e("MobileNumber", "" + MobileNumber);
        // Log.e("Otp", "" + Otp);
        // Log.e("Email", "" + Email);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnSkip:
                InvitationCode = edtTypeInvitationCodeHere.getText().toString().trim();
                if (InvitationCode.equals("")) {
                    Intent NickName = new Intent(mContext, NickNameActivity.class);
                    NickName.putExtra("MobileNumber", MobileNumber);
                    NickName.putExtra("Otp", Otp);
                    NickName.putExtra("Email", Email);
                    startActivity(NickName);
                    finish();
                } else {
                    GoToNickNameActivity();
                }
                break;
            case R.id.btnDone:
                InvitationCode = edtTypeInvitationCodeHere.getText().toString().trim();
                if (InvitationCode.equals("")) {
                    Toast.makeText(mContext, "Please Enter Invitation Code", Toast.LENGTH_LONG).show();
                } else {
                    GoToNickNameActivity();
                }
                break;
        }
    }

    public void GoToNickNameActivity() {
        Intent NickName = new Intent(mContext, NickNameActivity.class);
        NickName.putExtra("MobileNumber", MobileNumber);
        NickName.putExtra("Otp", Otp);
        NickName.putExtra("InvitationCode", InvitationCode);
        NickName.putExtra("Email", Email);
        startActivity(NickName);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
