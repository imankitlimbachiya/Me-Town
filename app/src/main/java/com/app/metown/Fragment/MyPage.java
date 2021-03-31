package com.app.metown.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.BuildConfig;
import com.app.metown.R;
import com.app.metown.UI.AnnouncementActivity;
import com.app.metown.UI.FavouriteActivity;
import com.app.metown.UI.FollowerActivity;
import com.app.metown.UI.HelpCenterActivity;
import com.app.metown.UI.InviteFriendActivity;
import com.app.metown.UI.KeywordAlertActivity;
import com.app.metown.UI.LocationVerifyActivity;
import com.app.metown.UI.MerchantMenuActivity;
import com.app.metown.UI.MyCommunityActivity;
import com.app.metown.UI.MyPurchaseActivity;
import com.app.metown.UI.MySaleActivity;
import com.app.metown.UI.ProfileActivity;
import com.app.metown.UI.ProfileEditActivity;
import com.app.metown.UI.RegisterMerchantActivity;
import com.app.metown.UI.SetRangeActivity;
import com.app.metown.UI.SettingActivity;
import com.app.metown.UI.TopicListActivity;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MyPage extends Fragment implements View.OnClickListener {

    public MyPage() {
        // Required empty public constructor
    }

    Context mContext;
    ProgressBar progressBar;
    CircleImageView imgUser, imgCamera;
    TextView txtNickName, txtSeeProfile;
    LinearLayout MySaleLayout, MyPurchaseLayout, FavouriteLayout;
    RelativeLayout KeywordAlertLayout, MyCommunityPostCommentLayout, FollowedCommunityKeywordLayout, InviteFriendLayout, ShareMeTownLayout,
            AnnouncementLayout, SettingLayout, MerchantMenuLayout, FollowedUserLayout, SetRangeLayout, HelpCenterLayout,
            VerifyLocationLayout;
    String IsMerchant = "", UserID = "";
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_page, container, false);

        Log.e("Fragment", "MyPage");

        mContext = getActivity();

        ViewInitialization(view);

        ViewOnClick();

        GetUserDefault();

        return view;
    }

    public void ViewInitialization(View view) {
        progressBar = view.findViewById(R.id.progressBar);

        imgUser = view.findViewById(R.id.imgUser);
        imgCamera = view.findViewById(R.id.imgCamera);

        txtNickName = view.findViewById(R.id.txtNickName);
        txtSeeProfile = view.findViewById(R.id.txtSeeProfile);

        MySaleLayout = view.findViewById(R.id.MySaleLayout);
        MyPurchaseLayout = view.findViewById(R.id.MyPurchaseLayout);
        FavouriteLayout = view.findViewById(R.id.FavouriteLayout);

        KeywordAlertLayout = view.findViewById(R.id.KeywordAlertLayout);
        MyCommunityPostCommentLayout = view.findViewById(R.id.MyCommunityPostCommentLayout);
        FollowedCommunityKeywordLayout = view.findViewById(R.id.FollowedCommunityKeywordLayout);
        InviteFriendLayout = view.findViewById(R.id.InviteFriendLayout);
        AnnouncementLayout = view.findViewById(R.id.AnnouncementLayout);
        SettingLayout = view.findViewById(R.id.SettingLayout);
        MerchantMenuLayout = view.findViewById(R.id.MerchantMenuLayout);
        FollowedUserLayout = view.findViewById(R.id.FollowedUserLayout);
        SetRangeLayout = view.findViewById(R.id.SetRangeLayout);
        HelpCenterLayout = view.findViewById(R.id.HelpCenterLayout);
        VerifyLocationLayout = view.findViewById(R.id.VerifyLocationLayout);
        ShareMeTownLayout = view.findViewById(R.id.ShareMeTownLayout);
    }

    public void ViewOnClick() {
        imgCamera.setOnClickListener(this);
        txtSeeProfile.setOnClickListener(this);
        MySaleLayout.setOnClickListener(this);
        MyPurchaseLayout.setOnClickListener(this);
        FavouriteLayout.setOnClickListener(this);
        KeywordAlertLayout.setOnClickListener(this);
        MyCommunityPostCommentLayout.setOnClickListener(this);
        FollowedCommunityKeywordLayout.setOnClickListener(this);
        InviteFriendLayout.setOnClickListener(this);
        AnnouncementLayout.setOnClickListener(this);
        SettingLayout.setOnClickListener(this);
        MerchantMenuLayout.setOnClickListener(this);
        FollowedUserLayout.setOnClickListener(this);
        SetRangeLayout.setOnClickListener(this);
        HelpCenterLayout.setOnClickListener(this);
        ShareMeTownLayout.setOnClickListener(this);
    }

    public void GetUserDefault() {
        sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
        IsMerchant = sharedPreferences.getString("IsMerchant", "");
        UserID = sharedPreferences.getString("UserID", "");
        String NickName = sharedPreferences.getString("NickName", "");
        String ProfilePicture = sharedPreferences.getString("ProfilePicture", "");
        txtNickName.setText(NickName);
        Glide.with(mContext).load(ProfilePicture).placeholder(R.drawable.profile_default).into(imgUser);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgCamera:
                Intent ProfileEdit = new Intent(mContext, ProfileEditActivity.class);
                startActivity(ProfileEdit);
                break;
            case R.id.txtSeeProfile:
                Intent Profile = new Intent(mContext, ProfileActivity.class);
                Profile.putExtra("UserID", UserID);
                startActivity(Profile);
                break;
            case R.id.MySaleLayout:
                Intent MySale = new Intent(mContext, MySaleActivity.class);
                startActivity(MySale);
                break;
            case R.id.MyPurchaseLayout:
                Intent MyPurchase = new Intent(mContext, MyPurchaseActivity.class);
                startActivity(MyPurchase);
                break;
            case R.id.FavouriteLayout:
                Intent Favourite = new Intent(mContext, FavouriteActivity.class);
                startActivity(Favourite);
                break;
            case R.id.KeywordAlertLayout:
                Intent KeywordAlert = new Intent(mContext, KeywordAlertActivity.class);
                startActivity(KeywordAlert);
                break;
            case R.id.MyCommunityPostCommentLayout:
                Intent MyCommunity = new Intent(mContext, MyCommunityActivity.class);
                startActivity(MyCommunity);
                break;
            case R.id.FollowedCommunityKeywordLayout:
                Intent Topic = new Intent(mContext, TopicListActivity.class);
                startActivity(Topic);
                break;
            case R.id.InviteFriendLayout:
                Intent InviteFriend = new Intent(mContext, InviteFriendActivity.class);
                startActivity(InviteFriend);
                break;
            case R.id.AnnouncementLayout:
                Intent Announcement = new Intent(mContext, AnnouncementActivity.class);
                startActivity(Announcement);
                break;
            case R.id.SettingLayout:
                Intent Setting = new Intent(mContext, SettingActivity.class);
                startActivity(Setting);
                break;
            case R.id.MerchantMenuLayout:
                if (IsMerchant.equals("0")) {
                    Intent RegisterMerchant = new Intent(mContext, RegisterMerchantActivity.class);
                    startActivity(RegisterMerchant);
                } else {
                    Intent MerchantMenu = new Intent(mContext, MerchantMenuActivity.class);
                    startActivity(MerchantMenu);
                }
                break;
            case R.id.FollowedUserLayout:
                Intent Follower = new Intent(mContext, FollowerActivity.class);
                startActivity(Follower);
                break;
            case R.id.SetRangeLayout:
                Intent SetRange = new Intent(mContext, SetRangeActivity.class);
                startActivity(SetRange);
                break;
            case R.id.ShareMeTownLayout:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: https://play.google.com/store/apps/details?id="
                        + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.HelpCenterLayout:
                Intent HelpCenter = new Intent(mContext, HelpCenterActivity.class);
                startActivity(HelpCenter);
                break;
            case R.id.VerifyLocationLayout:
                Intent LocationVerify = new Intent(mContext, LocationVerifyActivity.class);
                startActivity(LocationVerify);
                break;
        }
    }
}