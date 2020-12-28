package com.app.metown.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.metown.R;
import com.app.metown.UI.AnnouncementActivity;
import com.app.metown.UI.FavouriteActivity;
import com.app.metown.UI.FollowerActivity;
import com.app.metown.UI.HelpCenterActivity;
import com.app.metown.UI.InviteFriendActivity;
import com.app.metown.UI.KeywordAlertActivity;
import com.app.metown.UI.LocationVerifyActivity;
import com.app.metown.UI.MyCommunityActivity;
import com.app.metown.UI.MyPurchaseActivity;
import com.app.metown.UI.MySaleActivity;
import com.app.metown.UI.ProfileActivity;
import com.app.metown.UI.RegisterMerchantActivity;
import com.app.metown.UI.SetRangeActivity;
import com.app.metown.UI.SettingActivity;
import com.app.metown.UI.TopicListActivity;

public class MyPage extends Fragment implements View.OnClickListener {

    public MyPage() {
        // Required empty public constructor
    }

    Context mContext;
    ProgressBar progressBar;
    TextView txtSeeProfile;
    LinearLayout MySaleLayout, MyPurchaseLayout, FavouriteLayout;
    RelativeLayout KeywordAlertLayout, MyCommunityPostCommentLayout, FollowedCommunityKeywordLayout, InviteFriendLayout,
            AnnouncementLayout, SettingLayout, MerchantMenuLayout, FollowedUserLayout, SetRangeLayout, HelpCenterLayout,
            VerifyLocationLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_page, container, false);

        Log.e("Fragment","MyPage");

        mContext = getActivity();

        progressBar = view.findViewById(R.id.progressBar);

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

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSeeProfile:
                Intent Profile = new Intent(mContext, ProfileActivity.class);
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
                Intent RegisterMerchant = new Intent(mContext, RegisterMerchantActivity.class);
                startActivity(RegisterMerchant);
                break;
            case R.id.FollowedUserLayout:
                Intent Follower = new Intent(mContext, FollowerActivity.class);
                startActivity(Follower);
                break;
            case R.id.SetRangeLayout:
                Intent SetRange = new Intent(mContext, SetRangeActivity.class);
                startActivity(SetRange);
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
