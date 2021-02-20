package com.app.metown.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.app.metown.Adapters.SecondHandItemAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.ItemModel;
import com.app.metown.Models.PostModel;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.Models.ItemMainModel;
import com.app.metown.R;
import com.app.metown.UI.FilterActivity;
import com.app.metown.UI.LocationActivity;
import com.app.metown.UI.MyCommunityActivity;
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
    TextView txtSecondHand, txtCommunity, txtWonderCommunity, txtApplyCommunityDescription, txtLookingForwardDescription,
            txtPrepareOpenLocation, txtPrepareOpenLocationDescription, txtError;
    View SecondHandView, CommunityView, btnApplyView;
    Button btnApply;
    ImageView imgSearch, imgFilter, imgAlert;
    LinearLayout SecondHandLayout, CommunityLayout, CommunityTabView, PrepareOpenLocationLayout,
            CommunityPostLayout, ResponseLayout;
    RelativeLayout NoResponseLayout;
    RecyclerView SecondHandItemView, CommunityPostView;
    ArrayList<PostModel> postList = new ArrayList<>();
    ArrayList<ItemModel> itemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        Log.e("Fragment", "Home");

        mContext = getActivity();

        ViewInitialization(view);

        ViewOnClick();

        ViewSetText();

        GetLocationAPI();

        return view;
    }

    public void ViewInitialization(View view) {
        progressBar = view.findViewById(R.id.progressBar);

        btnApply = view.findViewById(R.id.btnApply);

        imgSearch = view.findViewById(R.id.imgSearch);
        imgFilter = view.findViewById(R.id.imgFilter);
        imgAlert = view.findViewById(R.id.imgAlert);

        SecondHandView = view.findViewById(R.id.SecondHandView);
        CommunityView = view.findViewById(R.id.CommunityView);
        btnApplyView = view.findViewById(R.id.btnApplyView);

        txtError = view.findViewById(R.id.txtError);
        txtSecondHand = view.findViewById(R.id.txtSecondHand);
        txtCommunity = view.findViewById(R.id.txtCommunity);
        txtApplyCommunityDescription = view.findViewById(R.id.txtApplyCommunityDescription);
        txtLookingForwardDescription = view.findViewById(R.id.txtLookingForwardDescription);
        txtPrepareOpenLocation = view.findViewById(R.id.txtPrepareOpenLocation);
        txtPrepareOpenLocationDescription = view.findViewById(R.id.txtPrepareOpenLocationDescription);
        txtWonderCommunity = view.findViewById(R.id.txtWonderCommunity);
        txtWonderCommunity.setPaintFlags(txtWonderCommunity.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        SecondHandLayout = view.findViewById(R.id.SecondHandLayout);
        CommunityLayout = view.findViewById(R.id.CommunityLayout);
        CommunityTabView = view.findViewById(R.id.CommunityTabView);
        PrepareOpenLocationLayout = view.findViewById(R.id.PrepareOpenLocationLayout);
        CommunityPostLayout = view.findViewById(R.id.CommunityPostLayout);

        ResponseLayout = view.findViewById(R.id.ResponseLayout);
        NoResponseLayout = view.findViewById(R.id.NoResponseLayout);

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
        btnApply.setOnClickListener(this);
    }

    public void ViewSetText() {
        txtSecondHand.setTextColor(getResources().getColor(R.color.black));
        SecondHandView.setBackgroundColor(getResources().getColor(R.color.black));
        txtCommunity.setTextColor(getResources().getColor(R.color.grey));
        CommunityView.setBackgroundColor(getResources().getColor(R.color.grey));
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
                txtSecondHand.setTextColor(getResources().getColor(R.color.black));
                SecondHandView.setBackgroundColor(getResources().getColor(R.color.black));
                txtCommunity.setTextColor(getResources().getColor(R.color.grey));
                CommunityView.setBackgroundColor(getResources().getColor(R.color.grey));
                GetSaleListApi();
                break;
            case R.id.CommunityLayout:
                txtCommunity.setTextColor(getResources().getColor(R.color.black));
                CommunityView.setBackgroundColor(getResources().getColor(R.color.black));
                txtSecondHand.setTextColor(getResources().getColor(R.color.grey));
                SecondHandView.setBackgroundColor(getResources().getColor(R.color.grey));
                TownStatusApi();
                break;
            case R.id.btnApply:
                ApplyCommunityApi();
                break;
            case R.id.PrepareOpenLocationLayout:
                GetCommunityApi();
                break;
        }
    }

    private void GetLocationAPI() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().GET_LOCATION,
                new Response.Listener<String>() {
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
                                    String LocationID = arrayData.getJSONObject(i).getString("id");
                                    String LocationName = arrayData.getJSONObject(i).getString("location_name");
                                    String LocationLatitude = arrayData.getJSONObject(i).getString("lats");
                                    String LocationLongitude = arrayData.getJSONObject(i).getString("longs");
                                    String LocationUserRange = arrayData.getJSONObject(i).getString("user_range");
                                    SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                    sharedPreferencesEditor.putString("LocationID", LocationID);
                                    sharedPreferencesEditor.putString("LocationName", LocationName);
                                    sharedPreferencesEditor.putString("LocationLatitude", LocationLatitude);
                                    sharedPreferencesEditor.putString("LocationLongitude", LocationLongitude);
                                    sharedPreferencesEditor.putString("LocationUserRange", LocationUserRange);
                                    sharedPreferencesEditor.apply();
                                    sharedPreferencesEditor.commit();

                                    GetSaleListApi();
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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

    private void GetSaleListApi() {
        String req = "req";
        itemList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_SALE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_SALE_LIST + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                ItemModel itemModel = new ItemModel();
                                itemList.add(itemModel);

                                itemModel = new ItemModel();
                                itemList.add(itemModel);

                                itemModel = new ItemModel();
                                itemList.add(itemModel);

                                itemModel = new ItemModel();
                                itemList.add(itemModel);

                                CommunityTabView.setVisibility(View.GONE);
                                SecondHandItemView.setVisibility(View.VISIBLE);
                                SecondHandItemAdapter secondHandItemAdapter = new SecondHandItemAdapter(mContext, itemList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                SecondHandItemView.setLayoutManager(mLayoutManager);
                                SecondHandItemView.setItemAnimator(new DefaultItemAnimator());
                                SecondHandItemView.setAdapter(secondHandItemAdapter);
                                secondHandItemAdapter.notifyDataSetChanged();
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                // Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                                ResponseLayout.setVisibility(View.GONE);
                                NoResponseLayout.setVisibility(View.VISIBLE);
                                txtError.setText(ErrorMessage);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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

    private void TownStatusApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().TOWN_STATUS,
                new Response.Listener<String>() {
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
                                    GetCommunityApi();
                                } else {
                                    NoResponseLayout.setVisibility(View.GONE);
                                    ResponseLayout.setVisibility(View.VISIBLE);
                                    SecondHandItemView.setVisibility(View.GONE);
                                    CommunityTabView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                                NoResponseLayout.setVisibility(View.GONE);
                                ResponseLayout.setVisibility(View.VISIBLE);
                                SecondHandItemView.setVisibility(View.GONE);
                                CommunityTabView.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().APPLY_COMMUNITY,
                new Response.Listener<String>() {
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
                },
                new Response.ErrorListener() {
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

    private void GetCommunityApi() {
        String req = "req";
        postList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_COMMUNITY,
                new Response.Listener<String>() {
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
                                    JSONObject objectUserDetail = arrayData.getJSONObject(i).getJSONObject("user_detail");
                                    postModel.setNickName(objectUserDetail.getString("nick_name"));
                                    postModel.setProfilePicture(objectUserDetail.getString("profile_pic"));
                                    postList.add(postModel);
                                }
                                if (postList.size() > 0) {
                                    txtPrepareOpenLocation.setVisibility(View.GONE);
                                    txtPrepareOpenLocationDescription.setVisibility(View.GONE);
                                    txtWonderCommunity.setVisibility(View.GONE);
                                    btnApplyView.setVisibility(View.GONE);
                                    btnApply.setVisibility(View.GONE);
                                    txtApplyCommunityDescription.setVisibility(View.GONE);
                                    txtLookingForwardDescription.setVisibility(View.GONE);
                                    CommunityPostLayout.setVisibility(View.VISIBLE);

                                    SecondHandItemView.setVisibility(View.GONE);
                                    CommunityTabView.setVisibility(View.VISIBLE);
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
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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

            @Override
            public byte[] getBody() throws AuthFailureError {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String LocationLatitude = sharedPreferences.getString("LocationLatitude", "");
                String LocationLongitude = sharedPreferences.getString("LocationLongitude", "");
                String LocationUserRange = sharedPreferences.getString("LocationUserRange", "");
                String params = "{\"lats\":\"" + LocationLatitude + "\",\"longs\":\"" + LocationLongitude +
                        "\",\"userRange\":\"" + LocationUserRange + "\"}";
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
            TextView txtNickName, txtPostDescription;

            MyViewHolder(View view) {
                super(view);

                imgUser = view.findViewById(R.id.imgUser);
                imgPost = view.findViewById(R.id.imgPost);

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
            PostModel postModel = arrayList.get(position);

            Glide.with(mContext).load(postModel.getProfilePicture()).into(holder.imgUser);
            Glide.with(mContext).load(postModel.getImage()).into(holder.imgPost);

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
}
