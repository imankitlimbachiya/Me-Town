package com.app.metown.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.StoreModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondHandPostActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    CircleImageView imgUser;
    TextView txtSecondHandPostTitle, txtNickName, txtAddress, txtDetail;
    TextView txtReport, txtHideThisUser, txtMute, txtComplement, txtDisappointment, txtBlock, txtLeaveChat;
    LinearLayout NegotiateLayout, FollowUserLayout, FavoriteLayout, ChatLayout;
    RelativeLayout OptionLayout;
    RecyclerView PostView, OtherItemByNicknameView, OtherItemView;
    ArrayList<String> postList = new ArrayList<>();
    ArrayList<StoreModel> itemList = new ArrayList<>();
    ArrayList<StoreModel> otherItemList = new ArrayList<>();
    String ID, SellerID, UserID, Images;
    EasyPopup mQQPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_hand_post);

        Log.e("Activity", "SecondHandPostActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GetUserDefault();

        GetIntentData();

        ViewInitialization();

        ViewOnClick();

        OpenEasyPopup();

        GetStoreMerchantProfileApi(UserID, ID);
    }

    public void GetUserDefault() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        UserID = sharedPreferences.getString("UserID", "");
    }

    public void GetIntentData() {
        ID = getIntent().getStringExtra("ID");
        SellerID = getIntent().getStringExtra("SellerID");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);
        imgUser = findViewById(R.id.imgUser);

        txtSecondHandPostTitle = findViewById(R.id.txtSecondHandPostTitle);
        txtNickName = findViewById(R.id.txtNickName);
        txtAddress = findViewById(R.id.txtAddress);
        txtDetail = findViewById(R.id.txtDetail);

        NegotiateLayout = findViewById(R.id.NegotiateLayout);
        FollowUserLayout = findViewById(R.id.FollowUserLayout);
        FavoriteLayout = findViewById(R.id.FavoriteLayout);
        ChatLayout = findViewById(R.id.ChatLayout);

        OptionLayout = findViewById(R.id.OptionLayout);

        PostView = findViewById(R.id.PostView);
        OtherItemByNicknameView = findViewById(R.id.OtherItemByNicknameView);
        OtherItemView = findViewById(R.id.OtherItemView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        txtNickName.setOnClickListener(this);
        NegotiateLayout.setOnClickListener(this);
        FollowUserLayout.setOnClickListener(this);
        FavoriteLayout.setOnClickListener(this);
        ChatLayout.setOnClickListener(this);
        OptionLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtNickName:
                Intent Profile = new Intent(mContext, ProfileActivity.class);
                Profile.putExtra("UserID", UserID);
                startActivity(Profile);
                break;
            case R.id.NegotiateLayout:
                Intent PostForSaleCalculation = new Intent(mContext, PostForSaleCalculationActivity.class);
                startActivity(PostForSaleCalculation);
                break;
            case R.id.FollowUserLayout:
                FollowUserApi(UserID);
                break;
            case R.id.FavoriteLayout:
                AddEditFavoriteApi(ID);
                break;
            case R.id.ChatLayout:
                Intent ChatCommercial = new Intent(mContext, ChatCommercialActivity.class);
                ChatCommercial.putExtra("ConversationID", "");
                ChatCommercial.putExtra("ToUserID", SellerID);
                ChatCommercial.putExtra("ToUserName", "");
                ChatCommercial.putExtra("ProductID", ID);
                ChatCommercial.putExtra("ProductName", "");
                ChatCommercial.putExtra("ProductImages", Images);
                ChatCommercial.putExtra("ProductPrice", "");
                startActivity(ChatCommercial);
                break;
            case R.id.OptionLayout:
                mQQPop.showAtAnchorView(view, YGravity.ABOVE, XGravity.RIGHT, 0, 0);
                break;
        }
    }

    public void OpenEasyPopup() {
        mQQPop = EasyPopup.create()
                .setContext(mContext)
                .setContentView(R.layout.chat_option_menu)
                // .setAnimationStyle(R.style.RightTop2PopAnim)
                .setOnViewListener(new EasyPopup.OnViewListener() {
                    @Override
                    public void initViews(View view, EasyPopup basePopup) {
                        txtComplement = view.findViewById(R.id.txtComplement);
                        txtComplement.setVisibility(View.GONE);
                        txtDisappointment = view.findViewById(R.id.txtDisappointment);
                        txtDisappointment.setVisibility(View.GONE);
                        txtBlock = view.findViewById(R.id.txtBlock);
                        txtBlock.setVisibility(View.GONE);
                        txtMute = view.findViewById(R.id.txtMute);
                        txtMute.setVisibility(View.GONE);
                        txtLeaveChat = view.findViewById(R.id.txtLeaveChat);
                        txtLeaveChat.setVisibility(View.GONE);
                        txtReport = view.findViewById(R.id.txtReport);
                        txtReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mQQPop.dismiss();
                                Intent Report = new Intent(mContext, ReportActivity.class);
                                startActivity(Report);
                            }
                        });
                        txtHideThisUser = view.findViewById(R.id.txtHideThisUser);
                        txtHideThisUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MySaleUpdateStatusApi(ID);
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

    private void GetStoreMerchantProfileApi(final String UserID, final String ProductID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().STORE_MERCHANT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().STORE_MERCHANT_PROFILE + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONObject objectData = JsonMain.getJSONObject("data");
                        String Name = objectData.getString("name");
                        txtSecondHandPostTitle.setText(Name);
                        String Address = objectData.getString("address");
                        txtAddress.setText(Address);
                        String Description = objectData.getString("description");
                        txtDetail.setText(Description);
                        String CreatedAt = objectData.getString("created_at");
                        String Images = objectData.getString("images");
                        String[] separated = Images.split(",");
                        postList.addAll(Arrays.asList(separated));
                        if (postList.size() > 0) {
                            PostAdapter postAdapter = new PostAdapter(mContext, postList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
                            PostView.setLayoutManager(mLayoutManager);
                            PostView.setItemAnimator(new DefaultItemAnimator());
                            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
                            pagerSnapHelper.attachToRecyclerView(PostView);
                            PostView.setAdapter(postAdapter);
                            postAdapter.notifyDataSetChanged();
                        }
                        JSONObject UserDetail = objectData.getJSONObject("user_detail");
                        String NickName = UserDetail.getString("nick_name");
                        txtNickName.setText(NickName);
                        String ProfilePicture = UserDetail.getString("profile_pic");
                        Glide.with(mContext).load(ProfilePicture).into(imgUser);

                        JSONArray arrayStoreNickItem = objectData.getJSONArray("storeNickItem");
                        for (int i = 0; i < arrayStoreNickItem.length(); i++) {
                            StoreModel storeModel = new StoreModel();
                            storeModel.setID(arrayStoreNickItem.getJSONObject(i).getString("id"));
                            storeModel.setName(arrayStoreNickItem.getJSONObject(i).getString("name"));
                            storeModel.setAddress(arrayStoreNickItem.getJSONObject(i).getString("address"));
                            storeModel.setImages(arrayStoreNickItem.getJSONObject(i).getString("images"));
                            storeModel.setDescription(arrayStoreNickItem.getJSONObject(i).getString("description"));
                            storeModel.setType(arrayStoreNickItem.getJSONObject(i).getString("type"));
                            storeModel.setSellerID(arrayStoreNickItem.getJSONObject(i).getString("seller_id"));
                            otherItemList.add(storeModel);
                        }
                        if (itemList.size() > 0) {
                            StoreItemByNickNameAdapter itemByNickNameAdapter = new StoreItemByNickNameAdapter(mContext, itemList);
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
                            OtherItemByNicknameView.setLayoutManager(layoutManager);
                            OtherItemByNicknameView.setItemAnimator(new DefaultItemAnimator());
                            OtherItemByNicknameView.setAdapter(itemByNickNameAdapter);
                            itemByNickNameAdapter.notifyDataSetChanged();
                        }

                        JSONArray arrayStoreItem = objectData.getJSONArray("storeItem");
                        for (int i = 0; i < arrayStoreItem.length(); i++) {
                            StoreModel storeModel = new StoreModel();
                            storeModel.setID(arrayStoreItem.getJSONObject(i).getString("id"));
                            storeModel.setName(arrayStoreItem.getJSONObject(i).getString("name"));
                            storeModel.setAddress(arrayStoreItem.getJSONObject(i).getString("address"));
                            storeModel.setImages(arrayStoreItem.getJSONObject(i).getString("images"));
                            storeModel.setDescription(arrayStoreItem.getJSONObject(i).getString("description"));
                            storeModel.setType(arrayStoreItem.getJSONObject(i).getString("type"));
                            storeModel.setSellerID(arrayStoreItem.getJSONObject(i).getString("seller_id"));
                            otherItemList.add(storeModel);
                        }
                        if (otherItemList.size() > 0) {
                            StoreItemByNickNameAdapter storeItemByNickNameAdapter = new StoreItemByNickNameAdapter(mContext, otherItemList);
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
                            OtherItemView.setLayoutManager(mLayoutManager);
                            OtherItemView.setItemAnimator(new DefaultItemAnimator());
                            OtherItemView.setAdapter(storeItemByNickNameAdapter);
                            storeItemByNickNameAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String Message = JsonMain.getString("msg");
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Transfer-Encoding", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().STORE_MERCHANT_PROFILE + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", UserID);
                params.put("product_id", ProductID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().STORE_MERCHANT_PROFILE + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().STORE_MERCHANT_PROFILE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<String> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgPost;

            MyViewHolder(View view) {
                super(view);

                imgPost = view.findViewById(R.id.imgPost);
            }
        }

        public PostAdapter(Context mContext, ArrayList<String> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            String string = arrayList.get(position);

            Glide.with(mContext).load(string).into(holder.imgPost);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public static class StoreItemByNickNameAdapter extends RecyclerView.Adapter<StoreItemByNickNameAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<StoreModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgItem;
            TextView txtItemName, txtItemPrice;

            MyViewHolder(View view) {
                super(view);

                imgItem = view.findViewById(R.id.imgItem);

                txtItemName = view.findViewById(R.id.txtItemName);
                txtItemPrice = view.findViewById(R.id.txtItemPrice);
            }
        }

        public StoreItemByNickNameAdapter(Context mContext, ArrayList<StoreModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_item_by_nickname_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final StoreModel storeModel = arrayList.get(position);

            String Images = storeModel.getImages();
            if (Images.equals("") || Images.equals("null") || Images.equals(null) || Images == null) {

            } else {
                Glide.with(mContext).load(storeModel.getImages()).into(holder.imgItem);
            }

            holder.txtItemName.setText(storeModel.getName());
            // holder.txtItemPrice.setText(storeModel.getPrice());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SetRangeActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void MySaleUpdateStatusApi(final String ID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().MY_SALE_UPDATE_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    mQQPop.dismiss();
                    Log.e("RESPONSE", "" + APIConstant.getInstance().MY_SALE_UPDATE_STATUS + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        mQQPop.dismiss();
                    }
                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                // params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().MY_SALE_UPDATE_STATUS + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"id\":\"" + ID + "\",\"status\":\"" + "4" + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().MY_SALE_UPDATE_STATUS + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_SALE_UPDATE_STATUS);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void FollowUserApi(final String ID) {
        String req = "req";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().FOLLOW_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    Log.e("RESPONSE", "" + APIConstant.getInstance().FOLLOW_USER + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        String SuccessMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, SuccessMessage, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Authorization", Type + " " + Token);
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().FOLLOW_USER + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"following\":\"" + ID + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().FOLLOW_USER + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().FOLLOW_USER);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void AddEditFavoriteApi(final String ProductID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ADD_EDIT_FAVORITE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().ADD_EDIT_FAVORITE + response);
                    JSONObject JsonMain = new JSONObject(response);
                    // String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_EDIT_FAVORITE + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"product_id\":\"" + ProductID + "\",\"is_favorite\":\"" + "1" + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_EDIT_FAVORITE + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().ADD_EDIT_FAVORITE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}