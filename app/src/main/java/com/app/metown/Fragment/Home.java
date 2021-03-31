package com.app.metown.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.ItemModel;
import com.app.metown.Models.LocationModel;
import com.app.metown.Models.PostModel;
import com.app.metown.R;
import com.app.metown.UI.FilterActivity;
import com.app.metown.UI.LocationActivity;
import com.app.metown.UI.NotificationActivity;
import com.app.metown.UI.SearchAroundYourLocationActivity;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Home extends Fragment implements View.OnClickListener {

    public Home() {
        // Required empty public constructor
    }

    Context mContext;
    ProgressBar progressBar;
    Button btnApply;
    View SecondHandView, CommunityView;
    ImageView imgSearch, imgFilter, imgAlert, imgOpenLocation;
    TextView txtLocation, txtSecondHand, txtCommunity, txtWonderCommunity, txtError;
    LinearLayout SecondHandLayout, CommunityLayout, CommunityTabLayout, CommunityApplyLayout, PrepareOpenLocationLayout, CommunityPostLayout, ResponseLayout;
    RelativeLayout NoResponseLayout;
    Dialog dialog;
    EditText edtAddComment;
    RecyclerView LocationView, SecondHandItemView, CommunityPostView;
    ArrayList<PostModel> postList = new ArrayList<>();
    ArrayList<ItemModel> itemList = new ArrayList<>();
    ArrayList<LocationModel> locationList = new ArrayList<>();
    String LocationName = "", offSet = "0", UserRange = "1000";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        Log.e("Fragment", "Home");

        mContext = getActivity();

        ViewInitialization(view);

        ViewOnClick();

        ViewSetText("Second Hand");

        GetUserDefault();

        GetSaleListApi();

        return view;
    }

    public void ViewInitialization(View view) {
        progressBar = view.findViewById(R.id.progressBar);

        btnApply = view.findViewById(R.id.btnApply);

        imgSearch = view.findViewById(R.id.imgSearch);
        imgFilter = view.findViewById(R.id.imgFilter);
        imgAlert = view.findViewById(R.id.imgAlert);
        imgOpenLocation = view.findViewById(R.id.imgOpenLocation);

        SecondHandView = view.findViewById(R.id.SecondHandView);
        CommunityView = view.findViewById(R.id.CommunityView);

        txtLocation = view.findViewById(R.id.txtLocation);
        txtError = view.findViewById(R.id.txtError);
        txtSecondHand = view.findViewById(R.id.txtSecondHand);
        txtCommunity = view.findViewById(R.id.txtCommunity);
        txtWonderCommunity = view.findViewById(R.id.txtWonderCommunity);
        txtWonderCommunity.setPaintFlags(txtWonderCommunity.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        SecondHandLayout = view.findViewById(R.id.SecondHandLayout);
        CommunityLayout = view.findViewById(R.id.CommunityLayout);
        CommunityTabLayout = view.findViewById(R.id.CommunityTabLayout);
        CommunityApplyLayout = view.findViewById(R.id.CommunityApplyLayout);
        PrepareOpenLocationLayout = view.findViewById(R.id.PrepareOpenLocationLayout);
        CommunityPostLayout = view.findViewById(R.id.CommunityPostLayout);

        ResponseLayout = view.findViewById(R.id.ResponseLayout);
        NoResponseLayout = view.findViewById(R.id.NoResponseLayout);

        LocationView = view.findViewById(R.id.LocationView);
        SecondHandItemView = view.findViewById(R.id.SecondHandItemView);
        CommunityPostView = view.findViewById(R.id.CommunityPostView);
    }

    public void ViewOnClick() {
        SecondHandLayout.setOnClickListener(this);
        CommunityLayout.setOnClickListener(this);
        PrepareOpenLocationLayout.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgFilter.setOnClickListener(this);
        imgAlert.setOnClickListener(this);
        imgOpenLocation.setOnClickListener(this);
        btnApply.setOnClickListener(this);
    }

    public void ViewSetText(final String From) {
        if (From.equals("Second Hand")) {
            txtSecondHand.setTextColor(getResources().getColor(R.color.black));
            SecondHandView.setBackgroundColor(getResources().getColor(R.color.black));
            txtCommunity.setTextColor(getResources().getColor(R.color.grey));
            CommunityView.setBackgroundColor(getResources().getColor(R.color.grey));
        } else {
            txtCommunity.setTextColor(getResources().getColor(R.color.black));
            CommunityView.setBackgroundColor(getResources().getColor(R.color.black));
            txtSecondHand.setTextColor(getResources().getColor(R.color.grey));
            SecondHandView.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }

    public void GetUserDefault() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
        String LocationName = sharedPreferences.getString("LocationName", "");
        String Latitude = sharedPreferences.getString("Latitude", "");
        String Longitude = sharedPreferences.getString("Longitude", "");
        txtLocation.setText(LocationName);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgSearch:
                Intent SearchAroundYourLocation = new Intent(mContext, SearchAroundYourLocationActivity.class);
                startActivity(SearchAroundYourLocation);
                break;
            case R.id.imgFilter:
                Intent Filter = new Intent(mContext, FilterActivity.class);
                startActivity(Filter);
                break;
            case R.id.imgAlert:
                Intent Notification = new Intent(mContext, NotificationActivity.class);
                startActivity(Notification);
                break;
            case R.id.SecondHandLayout:
                ViewSetText("Second Hand");
                GetSaleListApi();
                break;
            case R.id.CommunityLayout:
                ViewSetText("Community");
                TownStatusApi();
                break;
            case R.id.btnApply:
                ApplyCommunityApi();
                break;
            case R.id.PrepareOpenLocationLayout:
                GetCommunityApi(offSet, UserRange);
                break;
            case R.id.imgOpenLocation:
                // GetLocationApi();
                break;
        }
    }

    private void GetLocationApi() {
        String req = "req";
        locationList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().GET_LOCATION, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_LOCATION + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            LocationModel locationModel = new LocationModel();
                            locationModel.setID(arrayData.getJSONObject(i).getString("id"));
                            locationModel.setLocationName(arrayData.getJSONObject(i).getString("location_name"));
                            locationModel.setLatitude(arrayData.getJSONObject(i).getString("lats"));
                            locationModel.setLongitude(arrayData.getJSONObject(i).getString("longs"));
                            locationModel.setUserRange(arrayData.getJSONObject(i).getString("user_range"));
                            locationList.add(locationModel);
                        }
                        if (locationList.size() > 0) {
                            LocationView.setVisibility(View.VISIBLE);
                            LocationAdapter locationAdapter = new LocationAdapter(mContext, locationList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            LocationView.setLayoutManager(mLayoutManager);
                            LocationView.setItemAnimator(new DefaultItemAnimator());
                            LocationView.setAdapter(locationAdapter);
                            locationAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_LOCATION + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_LOCATION);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<LocationModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtLocation;

            MyViewHolder(View view) {
                super(view);

                txtLocation = view.findViewById(R.id.txtLocation);
            }
        }

        public LocationAdapter(Context mContext, ArrayList<LocationModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final LocationModel locationModel = arrayList.get(position);

            holder.txtLocation.setText(locationModel.getLocationName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocationName = locationModel.getLocationName();
                    txtLocation.setText(LocationName);
                    LocationView.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void GetSaleListApi() {
        String req = "req";
        itemList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_SALE_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_SALE_LIST + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            ItemModel itemModel = new ItemModel();
                            itemModel.setItemID(arrayData.getJSONObject(i).getString("id"));
                            itemModel.setItemName(arrayData.getJSONObject(i).getString("name"));
                            itemModel.setAddress(arrayData.getJSONObject(i).getString("address"));
                            itemModel.setItemImages(arrayData.getJSONObject(i).getString("images"));
                            itemModel.setItemDescription(arrayData.getJSONObject(i).getString("description"));
                            itemModel.setItemSellerID(arrayData.getJSONObject(i).getString("seller_id"));
                            itemModel.setDistance(arrayData.getJSONObject(i).getString("distance"));
                            itemModel.setItemFavouriteCount(arrayData.getJSONObject(i).getString("favourite_count"));
                            itemModel.setItemCommentCount(arrayData.getJSONObject(i).getString("commnet_count"));
                            itemModel.setIsFavourite(arrayData.getJSONObject(i).getString("is_favourite"));
                            itemList.add(itemModel);
                        }
                        if (itemList.size() > 0) {
                            CommunityTabLayout.setVisibility(View.GONE);
                            SecondHandItemView.setVisibility(View.VISIBLE);
                            SecondHandItemAdapter secondHandItemAdapter = new SecondHandItemAdapter(mContext, itemList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            SecondHandItemView.setLayoutManager(mLayoutManager);
                            SecondHandItemView.setItemAnimator(new DefaultItemAnimator());
                            SecondHandItemView.setAdapter(secondHandItemAdapter);
                            secondHandItemAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                        ResponseLayout.setVisibility(View.GONE);
                        NoResponseLayout.setVisibility(View.VISIBLE);
                        txtError.setText(ErrorMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_SALE_LIST + params);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String LocationID = sharedPreferences.getString("LocationID", "");
                String params = "{\"location_id\":\"" + LocationID + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SALE_LIST + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_SALE_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class SecondHandItemAdapter extends RecyclerView.Adapter<SecondHandItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgItem;
            TextView txtItemName, txtAddress, txtItemPrice;
            LinearLayout CommentLayout, FavouriteLayout;

            MyViewHolder(View view) {
                super(view);

                imgItem = view.findViewById(R.id.imgItem);

                txtItemName = view.findViewById(R.id.txtItemName);
                txtAddress = view.findViewById(R.id.txtAddress);
                txtItemPrice = view.findViewById(R.id.txtItemPrice);

                CommentLayout = view.findViewById(R.id.CommentLayout);
                FavouriteLayout = view.findViewById(R.id.FavouriteLayout);
            }
        }

        public SecondHandItemAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_hand_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final ItemModel itemModel = arrayList.get(position);

            String Images = itemModel.getItemImages();
            if (Images.equals("") || Images.equals("null") || Images.equals(null) || Images == null) {

            } else {
                String[] separated = Images.split(",");
                Glide.with(mContext).load(separated[0]).into(holder.imgItem);
            }

            holder.txtItemName.setText(itemModel.getItemName());
            holder.txtAddress.setText(itemModel.getAddress());

            holder.CommentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.add_comment_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    edtAddComment = dialog.findViewById(R.id.edtAddComment);
                    dialog.findViewById(R.id.txtAdd).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String Comment = edtAddComment.getText().toString().trim();
                            if (Comment.equals("")) {
                                Toast.makeText(mContext, "Please Enter a comment.", Toast.LENGTH_LONG).show();
                            } else {
                                AddEditCommentApi(itemModel.getItemID(), edtAddComment.getText().toString().trim());
                            }
                        }
                    });
                    dialog.show();
                }
            });

            holder.FavouriteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddEditFavoriteApi(itemModel.getItemID());
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void TownStatusApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().TOWN_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().TOWN_STATUS + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONObject objectData = JsonMain.getJSONObject("data");
                        String TownID = objectData.getString("id");
                        String TownName = objectData.getString("name");
                        String TownIsOpen = objectData.getString("is_open");
                        String TownRequirePeople = objectData.getString("requiredPeoples");
                        String TownPeople = objectData.getString("peoples");
                        if (TownIsOpen.equals("1")) {
                            GetCommunityApi(offSet, UserRange);
                        } else {
                            NoResponseLayout.setVisibility(View.GONE);
                            ResponseLayout.setVisibility(View.VISIBLE);
                            SecondHandItemView.setVisibility(View.GONE);
                            CommunityTabLayout.setVisibility(View.VISIBLE);
                            CommunityPostLayout.setVisibility(View.GONE);
                            CommunityApplyLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                        NoResponseLayout.setVisibility(View.GONE);
                        ResponseLayout.setVisibility(View.VISIBLE);
                        SecondHandItemView.setVisibility(View.GONE);
                        CommunityTabLayout.setVisibility(View.VISIBLE);
                        CommunityPostLayout.setVisibility(View.GONE);
                        CommunityApplyLayout.setVisibility(View.VISIBLE);
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().TOWN_STATUS + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String LocationName = sharedPreferences.getString("LocationName", "");
                String params = "{\"name\":\"" + LocationName + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().TOWN_STATUS + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().TOWN_STATUS);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void ApplyCommunityApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().APPLY_COMMUNITY, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().APPLY_COMMUNITY + response);
                    JSONObject JsonMain = new JSONObject(response);
                    // String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                    Intent Location = new Intent(mContext, LocationActivity.class);
                    startActivity(Location);
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().APPLY_COMMUNITY + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String LocationName = sharedPreferences.getString("LocationName", "");
                String params = "{\"name\":\"" + LocationName + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().APPLY_COMMUNITY + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().APPLY_COMMUNITY);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void GetCommunityApi(final String offSet, final String UserRange) {
        String req = "req";
        postList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_COMMUNITY, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().GET_COMMUNITY + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            PostModel postModel = new PostModel();
                            postModel.setID(arrayData.getJSONObject(i).getString("id"));
                            postModel.setKeyword(arrayData.getJSONObject(i).getString("keyword"));
                            postModel.setDescription(arrayData.getJSONObject(i).getString("description"));
                            postModel.setImage(arrayData.getJSONObject(i).getString("image"));
                            postModel.setDistance(arrayData.getJSONObject(i).getString("distance"));
                            postModel.setTopic(arrayData.getJSONObject(i).getString("topic"));
                            postModel.setCreatedAt(arrayData.getJSONObject(i).getString("created_at"));
                            JSONObject objectUserDetail = arrayData.getJSONObject(i).getJSONObject("user_detail");
                            postModel.setNickName(objectUserDetail.getString("nick_name"));
                            postModel.setProfilePicture(objectUserDetail.getString("profile_pic"));
                            postList.add(postModel);
                        }
                        if (postList.size() > 0) {
                            NoResponseLayout.setVisibility(View.GONE);
                            ResponseLayout.setVisibility(View.VISIBLE);
                            SecondHandItemView.setVisibility(View.GONE);
                            CommunityTabLayout.setVisibility(View.VISIBLE);
                            CommunityApplyLayout.setVisibility(View.GONE);
                            CommunityPostLayout.setVisibility(View.VISIBLE);
                            CommunityPostAdapter communityPostAdapter = new CommunityPostAdapter(mContext, postList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            CommunityPostView.setLayoutManager(mLayoutManager);
                            CommunityPostView.setItemAnimator(new DefaultItemAnimator());
                            CommunityPostView.setAdapter(communityPostAdapter);
                            communityPostAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                    }
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
                Log.e("HEADER", "" + APIConstant.getInstance().GET_COMMUNITY + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Latitude = sharedPreferences.getString("Latitude", "");
                String Longitude = sharedPreferences.getString("Longitude", "");
                String params = "{\"lats\":\"" + Latitude + "\",\"longs\":\"" + Longitude + "\",\"offset\":\"" + offSet +
                        "\",\"userRange\":\"" + UserRange + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_COMMUNITY + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_COMMUNITY);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<PostModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgUser, imgPost;
            TextView txtTopic, txtNickName, txtPostDescription;

            MyViewHolder(View view) {
                super(view);

                imgUser = view.findViewById(R.id.imgUser);
                imgPost = view.findViewById(R.id.imgPost);

                txtTopic = view.findViewById(R.id.txtTopic);
                txtNickName = view.findViewById(R.id.txtNickName);
                txtPostDescription = view.findViewById(R.id.txtPostDescription);
            }
        }

        public CommunityPostAdapter(Context mContext, ArrayList<PostModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_post_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final PostModel postModel = arrayList.get(position);

            String ProfilePicture = postModel.getProfilePicture();
            if (ProfilePicture.equals("") || ProfilePicture.equals("null") || ProfilePicture.equals(null) || ProfilePicture == null) {

            } else {
                Glide.with(mContext).load(postModel.getProfilePicture()).into(holder.imgUser);
            }

            String PostImage = postModel.getImage();
            if (PostImage.equals("") || PostImage.equals("null") || PostImage.equals(null) || PostImage == null) {

            } else {
                Glide.with(mContext).load(postModel.getImage()).into(holder.imgPost);
            }

            holder.txtTopic.setText(postModel.getTopic());
            holder.txtNickName.setText(postModel.getNickName());
            holder.txtPostDescription.setText(postModel.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
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
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                        GetSaleListApi();
                    } else {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                    }
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

    private void AddEditCommentApi(final String ProductID, final String Comment) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ADD_EDIT_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                    Log.e("RESPONSE", "" + APIConstant.getInstance().ADD_EDIT_COMMENT + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        GetSaleListApi();
                    } else {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                    }
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
                Log.e("HEADER", "" + APIConstant.getInstance().ADD_EDIT_COMMENT + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"product_id\":\"" + ProductID + "\",\"type\":\"" + "1" + "\",\"comment\":\"" + Comment + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().ADD_EDIT_COMMENT + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().ADD_EDIT_COMMENT);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }
}