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
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.Models.ItemMainModel;
import com.app.metown.R;
import com.app.metown.UI.FilterActivity;
import com.app.metown.UI.LocationActivity;
import com.app.metown.UI.NotificationActivity;
import com.app.metown.UI.SearchAroundYourLocationActivity;
import com.app.metown.VolleySupport.AppController;

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
    TextView txtSecondHand, txtCommunity, txtWonderCommunity;
    View SecondHandView, CommunityView;
    Button btnApply;
    ImageView imgSearch, imgFilter, imgAlert;
    LinearLayout SecondHandLayout, CommunityLayout, CommunityTabView;
    RecyclerView SecondHandItemView, CommunityPostView;
    ArrayList<ItemMainModel> secondHandItemList = new ArrayList<>();
    ArrayList<StaticCategoryModel> communityPostList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        Log.e("Fragment", "Home");

        mContext = getActivity();

        ViewInitialization(view);

        ViewOnClick();

        ViewSetText();

        GetLocationAPI();

        MyAllSaleApi();

        AddSecondHandItems();

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

        txtSecondHand = view.findViewById(R.id.txtSecondHand);
        txtCommunity = view.findViewById(R.id.txtCommunity);
        txtWonderCommunity = view.findViewById(R.id.txtWonderCommunity);
        txtWonderCommunity.setPaintFlags(txtWonderCommunity.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        SecondHandLayout = view.findViewById(R.id.SecondHandLayout);
        CommunityLayout = view.findViewById(R.id.CommunityLayout);
        CommunityTabView = view.findViewById(R.id.CommunityTabView);

        SecondHandItemView = view.findViewById(R.id.SecondHandItemView);
        CommunityPostView = view.findViewById(R.id.CommunityPostView);
    }

    public void ViewOnClick() {
        SecondHandLayout.setOnClickListener(this);
        CommunityLayout.setOnClickListener(this);
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
                GetSaleListApi();
                txtSecondHand.setTextColor(getResources().getColor(R.color.black));
                SecondHandView.setBackgroundColor(getResources().getColor(R.color.black));
                txtCommunity.setTextColor(getResources().getColor(R.color.grey));
                CommunityView.setBackgroundColor(getResources().getColor(R.color.grey));
                AddSecondHandItems();
                break;
            case R.id.CommunityLayout:
                txtCommunity.setTextColor(getResources().getColor(R.color.black));
                CommunityView.setBackgroundColor(getResources().getColor(R.color.black));
                txtSecondHand.setTextColor(getResources().getColor(R.color.grey));
                SecondHandView.setBackgroundColor(getResources().getColor(R.color.grey));
                SecondHandItemView.setVisibility(View.GONE);
                CommunityTabView.setVisibility(View.VISIBLE);
                AddCommunityPostItems();
                break;
            case R.id.btnApply:
                Intent Location = new Intent(mContext, LocationActivity.class);
                startActivity(Location);
                break;
        }
    }

    private void GetLocationAPI() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().GET_LOCATION,
                new Response.Listener<String>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_LOCATION + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                              /*  Toast.makeText(mContext, "Keyword Added", Toast.LENGTH_LONG).show();
                                finish();*/
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
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_LOCATION + params);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_LOCATION);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void MyAllSaleApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().MY_ALL_SALE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().MY_ALL_SALE + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
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
                Log.e("PARAMETER", "" + APIConstant.getInstance().MY_ALL_SALE + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_ALL_SALE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void GetSaleListApi() {
        String req = "req";
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
                                /*JSONArray SalesAraay = JsonMain.getJSONArray("data");
                                SecondHandItemAdapter secondHandItemAdapter = new SecondHandItemAdapter(mContext, secondHandItemList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                SecondHandItemView.setLayoutManager(mLayoutManager);
                                SecondHandItemView.setItemAnimator(new DefaultItemAnimator());
                                SecondHandItemView.setAdapter(secondHandItemAdapter);
                                secondHandItemAdapter.notifyDataSetChanged();*/
                            } else {
                                String msg = JsonMain.getString("msg");
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
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
                String params = "{\"location_id\":\"" + "1" + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SALE_LIST + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_SALE_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void GetSaleListByLatLongApi(final String Latitude, final String Longitude) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_SALE_LIST_BY_LAT_LONG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_SALE_LIST_BY_LAT_LONG + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                /*JSONArray SalesAraay = JsonMain.getJSONArray("data");
                                SecondHandItemAdapter secondHandItemAdapter = new SecondHandItemAdapter(mContext, secondHandItemList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                SecondHandItemView.setLayoutManager(mLayoutManager);
                                SecondHandItemView.setItemAnimator(new DefaultItemAnimator());
                                SecondHandItemView.setAdapter(secondHandItemAdapter);
                                secondHandItemAdapter.notifyDataSetChanged();*/
                            } else {
                                String msg = JsonMain.getString("msg");
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
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
                // params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_SALE_LIST_BY_LAT_LONG + params);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"lats\":\"" + Latitude + "\",\"longs\":\"" + Longitude + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SALE_LIST_BY_LAT_LONG + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_SALE_LIST_BY_LAT_LONG);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void GetServiceListByLatLongApi(final String Latitude, final String Longitude) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().GET_SERVICE_LIST_BY_LAT_LONG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GET_SERVICE_LIST_BY_LAT_LONG + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                /*JSONArray SalesAraay = JsonMain.getJSONArray("data");
                                SecondHandItemAdapter secondHandItemAdapter = new SecondHandItemAdapter(mContext, secondHandItemList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                SecondHandItemView.setLayoutManager(mLayoutManager);
                                SecondHandItemView.setItemAnimator(new DefaultItemAnimator());
                                SecondHandItemView.setAdapter(secondHandItemAdapter);
                                secondHandItemAdapter.notifyDataSetChanged();*/
                            } else {
                                String msg = JsonMain.getString("msg");
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
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
                // params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().GET_SERVICE_LIST_BY_LAT_LONG + params);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"lats\":\"" + Latitude + "\",\"longs\":\"" + Longitude + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().GET_SERVICE_LIST_BY_LAT_LONG + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().GET_SERVICE_LIST_BY_LAT_LONG);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public void AddSecondHandItems() {
        CommunityTabView.setVisibility(View.GONE);
        SecondHandItemView.setVisibility(View.VISIBLE);

        secondHandItemList.clear();

        for (int i = 1; i <= 4; i++) {
            ItemMainModel itemMainModel = new ItemMainModel(String.valueOf(i), "name");
            secondHandItemList.add(itemMainModel);
        }

        if (secondHandItemList.size() > 0) {
            SecondHandItemAdapter secondHandItemAdapter = new SecondHandItemAdapter(mContext, secondHandItemList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            SecondHandItemView.setLayoutManager(mLayoutManager);
            SecondHandItemView.setItemAnimator(new DefaultItemAnimator());
            SecondHandItemView.setAdapter(secondHandItemAdapter);
            secondHandItemAdapter.notifyDataSetChanged();
        }
    }

    public void AddCommunityPostItems() {
        SecondHandItemView.setVisibility(View.GONE);
        CommunityTabView.setVisibility(View.VISIBLE);

        communityPostList.clear();
        for (int i = 1; i <= 5; i++) {
            StaticCategoryModel staticCategoryModel = new StaticCategoryModel(String.valueOf(i), "Item name");
            communityPostList.add(staticCategoryModel);
        }

        if (communityPostList.size() > 0) {
            CommunityPostAdapter communityPostAdapter = new CommunityPostAdapter(mContext, communityPostList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            CommunityPostView.setLayoutManager(mLayoutManager);
            CommunityPostView.setItemAnimator(new DefaultItemAnimator());
            CommunityPostView.setAdapter(communityPostAdapter);
            communityPostAdapter.notifyDataSetChanged();
        }
    }

    public static class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<StaticCategoryModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            // RelativeLayout OptionLayout;

            MyViewHolder(View view) {
                super(view);

                // OptionLayout = view.findViewById(R.id.OptionLayout);
            }
        }

        public CommunityPostAdapter(Context mContext, ArrayList<StaticCategoryModel> arrayList) {
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
            StaticCategoryModel staticCategoryModel = arrayList.get(position);

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

    @Override
    public void onResume() {
        super.onResume();
        GetSaleListByLatLongApi("23.112659", "72.547752");
        GetServiceListByLatLongApi("23.112659", "72.547752");
    }
}
