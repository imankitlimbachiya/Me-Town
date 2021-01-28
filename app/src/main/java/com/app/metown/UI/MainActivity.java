package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.AppConstants.ConstantFunction;
import com.app.metown.BuildConfig;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback {

    Context mContext;
    ProgressBar progressBar;
    Button btnSignUp, btnLogin;
    TextView txtLookAround;
    CircleImageView imgFacebook, imgGoogle, imgTalk, imgLine;

    CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        //initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }

        setContentView(R.layout.activity_main);

        Log.e("Activity", "MainActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        txtLookAround = findViewById(R.id.txtLookAround);

        imgFacebook = findViewById(R.id.imgFacebook);
        imgGoogle = findViewById(R.id.imgGoogle);
        imgTalk = findViewById(R.id.imgTalk);
        imgLine = findViewById(R.id.imgLine);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        txtLookAround.setOnClickListener(this);
        imgGoogle.setOnClickListener(this);
        imgFacebook.setOnClickListener(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("649646092496-bfequa4129rn10qo0il5egbl566895qt.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addScope(new Scope(Scopes.PROFILE))
                .build();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgFacebook:
                FacebookLogin();
                break;
            case R.id.imgGoogle:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.imgTalk:

                break;
            case R.id.imgLine:
                // GoToPhoneVerifyActivity();
                break;
            case R.id.btnSignUp:
                GoToPhoneVerifyActivity("SignUp");
                break;
            case R.id.btnLogin:
                GoToPhoneVerifyActivity("Login");
                break;
            case R.id.txtLookAround:
                Intent Home = new Intent(mContext, HomeActivity.class);
                startActivity(Home);
                break;
        }
    }

    public void GoToPhoneVerifyActivity(String Type) {
        Intent PhoneVerify = new Intent(mContext, PhoneVerifyActivity.class);
        PhoneVerify.putExtra("SocialID", "");
        PhoneVerify.putExtra("Email", "");
        PhoneVerify.putExtra("Type", Type);
        PhoneVerify.putExtra("NickName", "");
        startActivity(PhoneVerify);
    }

    private void FacebookLogin() {
        if (ConstantFunction.isNetworkAvailable(mContext)) {
            callbackManager = CallbackManager.Factory.create();
            LoginManager manager = LoginManager.getInstance();
            manager.setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
            manager.logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult result) {
                    // TODO Auto-generated method stub
                    // System.out.println("onSuccess");
                    ProgressDialog progressDialog = new ProgressDialog(mContext);
                    progressDialog.setMessage("Processing data...");
                    progressDialog.show();
                    String accessToken = result.getAccessToken().getToken();
                    GraphRequest request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(org.json.JSONObject object, GraphResponse response) {
                            try {
                                String facebookUserID = object.getString("id");
                                String facebookUsername = object.getString("name");
                                String facebookEmail = object.getString("email");
                                String facebookFirstName = object.getString("first_name");
                                String facebookLastName = object.getString("last_name");
                                /*Log.e("id", "" + facebookUserID);
                                Log.e("name", "" + facebookUsername);
                                Log.e("email", "" + facebookEmail);
                                Log.e("fb_firstName", "" + facebookFirstName);
                                Log.e("fb_lastName", "" + facebookLastName);*/

                                /*Toast.makeText(mContext, facebookUserID + "\n" + facebookUsername +
                                        "\n" + facebookEmail, Toast.LENGTH_LONG).show();*/

                                Intent PhoneVerify = new Intent(mContext, PhoneVerifyActivity.class);
                                PhoneVerify.putExtra("SocialID", facebookUserID);
                                PhoneVerify.putExtra("Email", facebookEmail);
                                PhoneVerify.putExtra("Type", "Social");
                                PhoneVerify.putExtra("NickName", facebookFirstName);
                                startActivity(PhoneVerify);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // facebookImage = "http://graph.facebook.com/" + facebookUserID + "/picture?type=large";
                            // String facebookImage = "http://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=large";
                            // Log.e("facebookImage", "" + facebookImage);
                        }
                    });
                    progressDialog.dismiss();
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,first_name,last_name,gender");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
        } else {
            Toast.makeText(mContext, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        // Log.e("google", "" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Uri image = acct.getPhotoUrl();

            String GoogleID = acct.getId();
            String GoogleEmail = acct.getEmail();
            String GoogleUsername = acct.getDisplayName();
            String GoogleUserImage = String.valueOf(image);

            Log.e("GoogleID", "" + GoogleID);
            Log.e("GoogleUsername", "" + GoogleUsername);
            Log.e("GoogleEmail", "" + GoogleEmail);
            Log.e("GoogleUserImage", "" + GoogleUserImage);

            /*Toast.makeText(mContext, GoogleID + "\n" + GoogleUsername +
                    "\n" + GoogleEmail + "\n" + GoogleUserImage, Toast.LENGTH_LONG).show();*/

            Intent PhoneVerify = new Intent(mContext, PhoneVerifyActivity.class);
            PhoneVerify.putExtra("SocialID", GoogleID);
            PhoneVerify.putExtra("Email", GoogleEmail);
            PhoneVerify.putExtra("Type", "Social");
            PhoneVerify.putExtra("NickName", GoogleUsername);
            startActivity(PhoneVerify);
        } else {
            Log.e("Not isSuccess", "Not isSuccess");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // Log.e("idToken", "" + result.getSignInAccount());
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onResult(@NonNull Result result) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
