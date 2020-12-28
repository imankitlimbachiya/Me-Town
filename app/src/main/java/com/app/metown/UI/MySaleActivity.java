package com.app.metown.UI;

import android.annotation.SuppressLint;
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
import java.util.HashMap;
import java.util.Map;

public class MySaleActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    EasyPopup mQQPop;
    RelativeLayout NoResponseLayout;
    LinearLayout ActiveLayout, SoldLayout, HiddenLayout, ActiveResponseLayout, SoldResponseLayout, HiddenResponseLayout;
    TextView txtActive, txtSold, txtHidden, txtError;
    View ActiveView, SoldView, HiddenView;
    RecyclerView ActiveItemView, SoldItemView, HiddenItemView;
    ArrayList<ItemModel> activeItemList = new ArrayList<>();
    ArrayList<ItemModel> soldItemList = new ArrayList<>();
    ArrayList<ItemModel> hiddenItemList = new ArrayList<>();
    JSONArray arrayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sale);

        Log.e("Activity", "MySaleActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        ActiveView = findViewById(R.id.ActiveView);
        SoldView = findViewById(R.id.SoldView);
        HiddenView = findViewById(R.id.HiddenView);

        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        ActiveLayout = findViewById(R.id.ActiveLayout);
        SoldLayout = findViewById(R.id.SoldLayout);
        HiddenLayout = findViewById(R.id.HiddenLayout);
        ActiveResponseLayout = findViewById(R.id.ActiveResponseLayout);
        SoldResponseLayout = findViewById(R.id.SoldResponseLayout);
        HiddenResponseLayout = findViewById(R.id.HiddenResponseLayout);

        txtError = findViewById(R.id.txtError);

        txtActive = findViewById(R.id.txtActive);
        txtSold = findViewById(R.id.txtSold);
        txtHidden = findViewById(R.id.txtHidden);

        txtActive.setTextColor(getResources().getColor(R.color.black));
        ActiveView.setBackgroundColor(getResources().getColor(R.color.black));
        txtSold.setTextColor(getResources().getColor(R.color.grey));
        SoldView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtHidden.setTextColor(getResources().getColor(R.color.grey));
        HiddenView.setBackgroundColor(getResources().getColor(R.color.grey));

        ActiveItemView = findViewById(R.id.ActiveItemView);
        SoldItemView = findViewById(R.id.SoldItemView);
        HiddenItemView = findViewById(R.id.HiddenItemView);

        MyActiveSaleApi();

        imgBack.setOnClickListener(this);
        ActiveLayout.setOnClickListener(this);
        SoldLayout.setOnClickListener(this);
        HiddenLayout.setOnClickListener(this);

        mQQPop = EasyPopup.create()
                .setContext(mContext)
                .setContentView(R.layout.option_menu)
                // .setAnimationStyle(R.style.RightTop2PopAnim)
                .setOnViewListener(new EasyPopup.OnViewListener() {
                    @Override
                    public void initViews(View view, EasyPopup basePopup) {
                        // View arrowView = view.findViewById(R.id.v_arrow);
                        // arrowView.setBackground(new TriangleDrawable(TriangleDrawable.TOP, Color.parseColor("#88FF88")));
                    }
                })
                .setFocusAndOutsideEnable(true)
                // .setBackgroundDimEnable(true)
                // .setDimValue(0.5f)
                // .setDimColor(Color.RED)
                // .setDimView(mTitleBar)
                .apply();
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        ActiveLayout.setOnClickListener(this);
        SoldLayout.setOnClickListener(this);
        HiddenLayout.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.ActiveLayout:
                MyActiveSaleApi();
                break;
            case R.id.SoldLayout:
                MySoldSaleApi();
                break;
            case R.id.HiddenLayout:
                MyHiddenSaleApi();
                break;
        }
    }

    private void MyActiveSaleApi() {
        txtActive.setTextColor(getResources().getColor(R.color.black));
        ActiveView.setBackgroundColor(getResources().getColor(R.color.black));
        txtSold.setTextColor(getResources().getColor(R.color.grey));
        SoldView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtHidden.setTextColor(getResources().getColor(R.color.grey));
        HiddenView.setBackgroundColor(getResources().getColor(R.color.grey));

        String req = "req";
        activeItemList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().MY_ACTIVE_SALES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().MY_ACTIVE_SALES + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    ItemModel itemModel = new ItemModel();
                                    itemModel.setItemID(arrayData.getJSONObject(i).getString("id"));
                                    itemModel.setItemSellerID(arrayData.getJSONObject(i).getString("seller_id"));
                                    itemModel.setItemBuyerID(arrayData.getJSONObject(i).getString("buyer_id"));
                                    itemModel.setItemCategoryID(arrayData.getJSONObject(i).getString("category_id"));
                                    itemModel.setItemCategoryTitle(arrayData.getJSONObject(i).getString("category_title"));
                                    itemModel.setItemName(arrayData.getJSONObject(i).getString("name"));
                                    itemModel.setItemDescription(arrayData.getJSONObject(i).getString("description"));
                                    itemModel.setItemStatus(arrayData.getJSONObject(i).getString("status"));
                                    itemModel.setItemType(arrayData.getJSONObject(i).getString("type"));
                                    itemModel.setItemPrice(arrayData.getJSONObject(i).getString("price"));
                                    itemModel.setItemLatitude(arrayData.getJSONObject(i).getString("lats"));
                                    itemModel.setItemLongitude(arrayData.getJSONObject(i).getString("longs"));
                                    itemModel.setItemUpdatedAt(arrayData.getJSONObject(i).getString("updated_at"));
                                    itemModel.setItemIsNegotiable(arrayData.getJSONObject(i).getString("is_negotiable"));
                                    itemModel.setItemImages(arrayData.getJSONObject(i).getString("images"));
                                    itemModel.setItemStatusTitle(arrayData.getJSONObject(i).getString("status_title"));
                                    itemModel.setItemTypeTitle(arrayData.getJSONObject(i).getString("type_title"));
                                    itemModel.setItemFavouriteCount(arrayData.getJSONObject(i).getString("favourite_count"));
                                    activeItemList.add(itemModel);
                                }

                                if (activeItemList.size() > 0) {
                                    NoResponseLayout.setVisibility(View.GONE);

                                    ActiveResponseLayout.setVisibility(View.VISIBLE);
                                    SoldResponseLayout.setVisibility(View.GONE);
                                    HiddenResponseLayout.setVisibility(View.GONE);

                                    ActiveItemAdapter activeItemAdapter = new ActiveItemAdapter(mContext, activeItemList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    ActiveItemView.setLayoutManager(mLayoutManager);
                                    ActiveItemView.setItemAnimator(new DefaultItemAnimator());
                                    ActiveItemView.setAdapter(activeItemAdapter);
                                    activeItemAdapter.notifyDataSetChanged();
                                } else {
                                    ActiveResponseLayout.setVisibility(View.GONE);
                                    HiddenResponseLayout.setVisibility(View.GONE);
                                    SoldResponseLayout.setVisibility(View.GONE);

                                    NoResponseLayout.setVisibility(View.VISIBLE);
                                    txtError.setText("No Data Available...");
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();

                                ActiveResponseLayout.setVisibility(View.GONE);
                                HiddenResponseLayout.setVisibility(View.GONE);
                                SoldResponseLayout.setVisibility(View.GONE);

                                NoResponseLayout.setVisibility(View.VISIBLE);
                                txtError.setText(ErrorMessage);
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
                        Log.e("ERROR", "" + error.getMessage());
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
                Log.e("HEADER", "" + APIConstant.getInstance().MY_ACTIVE_SALES + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_ACTIVE_SALES);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class ActiveItemAdapter extends RecyclerView.Adapter<ActiveItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemModel> arrayList;
        private String rupee;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgItem;
            TextView txtItemName, txtAddressOrTimePosted, txtItemPrice, txtChangeToReserved, txtChangeToSold;
            RelativeLayout OptionLayout;
            LinearLayout FavouriteLayout;

            MyViewHolder(View view) {
                super(view);

                imgItem = view.findViewById(R.id.imgItem);

                txtItemName = view.findViewById(R.id.txtItemName);
                txtAddressOrTimePosted = view.findViewById(R.id.txtAddressOrTimePosted);
                txtItemPrice = view.findViewById(R.id.txtItemPrice);
                txtChangeToReserved = view.findViewById(R.id.txtChangeToReserved);
                txtChangeToSold = view.findViewById(R.id.txtChangeToSold);

                OptionLayout = view.findViewById(R.id.OptionLayout);

                FavouriteLayout = view.findViewById(R.id.FavouriteLayout);
            }
        }

        public ActiveItemAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
            this.rupee = mContext.getString(R.string.rupee);
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final ItemModel itemModel = arrayList.get(position);

            String Images = itemModel.getItemImages();
            String[] separated = Images.split(",");
            Glide.with(mContext).load(separated[0]).into(holder.imgItem);

            holder.txtItemName.setText(itemModel.getItemName());
            holder.txtItemPrice.setText(rupee + " " + itemModel.getItemPrice());

            holder.txtChangeToReserved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MySaleUpdateStatusApi(itemModel.getItemID(), "2", "Active");
                }
            });

            holder.txtChangeToSold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MySaleUpdateStatusApi(itemModel.getItemID(), "3", "Active");
                }
            });

            holder.OptionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mQQPop.showAtAnchorView(view, YGravity.BELOW, XGravity.LEFT, 0, 0);
                }
            });

            holder.FavouriteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddEditFavoriteApi(itemModel.getItemID(), "1");
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void MySoldSaleApi() {
        txtSold.setTextColor(getResources().getColor(R.color.black));
        SoldView.setBackgroundColor(getResources().getColor(R.color.black));
        txtHidden.setTextColor(getResources().getColor(R.color.grey));
        HiddenView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtActive.setTextColor(getResources().getColor(R.color.grey));
        ActiveView.setBackgroundColor(getResources().getColor(R.color.grey));

        String req = "req";
        soldItemList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().MY_SOLD_SALES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().MY_SOLD_SALES + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    ItemModel itemModel = new ItemModel();
                                    itemModel.setItemID(arrayData.getJSONObject(i).getString("id"));
                                    itemModel.setItemSellerID(arrayData.getJSONObject(i).getString("seller_id"));
                                    itemModel.setItemBuyerID(arrayData.getJSONObject(i).getString("buyer_id"));
                                    itemModel.setItemCategoryID(arrayData.getJSONObject(i).getString("category_id"));
                                    itemModel.setItemCategoryTitle(arrayData.getJSONObject(i).getString("category_title"));
                                    itemModel.setItemName(arrayData.getJSONObject(i).getString("name"));
                                    itemModel.setItemDescription(arrayData.getJSONObject(i).getString("description"));
                                    itemModel.setItemStatus(arrayData.getJSONObject(i).getString("status"));
                                    itemModel.setItemType(arrayData.getJSONObject(i).getString("type"));
                                    itemModel.setItemPrice(arrayData.getJSONObject(i).getString("price"));
                                    itemModel.setItemLatitude(arrayData.getJSONObject(i).getString("lats"));
                                    itemModel.setItemLongitude(arrayData.getJSONObject(i).getString("longs"));
                                    itemModel.setItemUpdatedAt(arrayData.getJSONObject(i).getString("updated_at"));
                                    itemModel.setItemIsNegotiable(arrayData.getJSONObject(i).getString("is_negotiable"));
                                    itemModel.setItemImages(arrayData.getJSONObject(i).getString("images"));
                                    itemModel.setItemStatusTitle(arrayData.getJSONObject(i).getString("status_title"));
                                    itemModel.setItemTypeTitle(arrayData.getJSONObject(i).getString("type_title"));
                                    itemModel.setItemFavouriteCount(arrayData.getJSONObject(i).getString("favourite_count"));
                                    soldItemList.add(itemModel);
                                }

                                if (soldItemList.size() > 0) {
                                    NoResponseLayout.setVisibility(View.GONE);

                                    ActiveResponseLayout.setVisibility(View.GONE);
                                    HiddenResponseLayout.setVisibility(View.GONE);
                                    SoldResponseLayout.setVisibility(View.VISIBLE);

                                    SoldItemAdapter soldItemAdapter = new SoldItemAdapter(mContext, soldItemList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    SoldItemView.setLayoutManager(mLayoutManager);
                                    SoldItemView.setItemAnimator(new DefaultItemAnimator());
                                    SoldItemView.setAdapter(soldItemAdapter);
                                    soldItemAdapter.notifyDataSetChanged();
                                } else {
                                    ActiveResponseLayout.setVisibility(View.GONE);
                                    HiddenResponseLayout.setVisibility(View.GONE);
                                    SoldResponseLayout.setVisibility(View.GONE);

                                    NoResponseLayout.setVisibility(View.VISIBLE);
                                    txtError.setText("No Data Available...");
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();

                                ActiveResponseLayout.setVisibility(View.GONE);
                                HiddenResponseLayout.setVisibility(View.GONE);
                                SoldResponseLayout.setVisibility(View.GONE);

                                NoResponseLayout.setVisibility(View.VISIBLE);
                                txtError.setText(ErrorMessage);
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
                        Log.e("ERROR", "" + error.getMessage());
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
                Log.e("HEADER", "" + APIConstant.getInstance().MY_SOLD_SALES + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_SOLD_SALES);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class SoldItemAdapter extends RecyclerView.Adapter<SoldItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            RelativeLayout ReviewLayout;

            MyViewHolder(View view) {
                super(view);

                ReviewLayout = view.findViewById(R.id.ReviewLayout);
            }
        }

        public SoldItemAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }


        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sold_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            ItemModel itemModel = arrayList.get(position);

            holder.ReviewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SelectBuyerActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void MyHiddenSaleApi() {
        txtHidden.setTextColor(getResources().getColor(R.color.black));
        HiddenView.setBackgroundColor(getResources().getColor(R.color.black));
        txtActive.setTextColor(getResources().getColor(R.color.grey));
        ActiveView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtSold.setTextColor(getResources().getColor(R.color.grey));
        SoldView.setBackgroundColor(getResources().getColor(R.color.grey));

        String req = "req";
        hiddenItemList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().MY_HIDDEN_SALES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().MY_HIDDEN_SALES + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equalsIgnoreCase("false")) {
                                arrayData = JsonMain.getJSONArray("data");
                                for (int i = 0; i < arrayData.length(); i++) {
                                    ItemModel itemModel = new ItemModel();
                                    itemModel.setItemID(arrayData.getJSONObject(i).getString("id"));
                                    itemModel.setItemSellerID(arrayData.getJSONObject(i).getString("seller_id"));
                                    itemModel.setItemBuyerID(arrayData.getJSONObject(i).getString("buyer_id"));
                                    itemModel.setItemCategoryID(arrayData.getJSONObject(i).getString("category_id"));
                                    itemModel.setItemCategoryTitle(arrayData.getJSONObject(i).getString("category_title"));
                                    itemModel.setItemName(arrayData.getJSONObject(i).getString("name"));
                                    itemModel.setItemDescription(arrayData.getJSONObject(i).getString("description"));
                                    itemModel.setItemStatus(arrayData.getJSONObject(i).getString("status"));
                                    itemModel.setItemType(arrayData.getJSONObject(i).getString("type"));
                                    itemModel.setItemPrice(arrayData.getJSONObject(i).getString("price"));
                                    itemModel.setItemLatitude(arrayData.getJSONObject(i).getString("lats"));
                                    itemModel.setItemLongitude(arrayData.getJSONObject(i).getString("longs"));
                                    itemModel.setItemUpdatedAt(arrayData.getJSONObject(i).getString("updated_at"));
                                    itemModel.setItemIsNegotiable(arrayData.getJSONObject(i).getString("is_negotiable"));
                                    itemModel.setItemImages(arrayData.getJSONObject(i).getString("images"));
                                    itemModel.setItemStatusTitle(arrayData.getJSONObject(i).getString("status_title"));
                                    itemModel.setItemTypeTitle(arrayData.getJSONObject(i).getString("type_title"));
                                    itemModel.setItemFavouriteCount(arrayData.getJSONObject(i).getString("favourite_count"));
                                    hiddenItemList.add(itemModel);
                                }

                                if (hiddenItemList.size() > 0) {
                                    NoResponseLayout.setVisibility(View.GONE);

                                    ActiveResponseLayout.setVisibility(View.GONE);
                                    SoldResponseLayout.setVisibility(View.GONE);
                                    HiddenResponseLayout.setVisibility(View.VISIBLE);

                                    HiddenItemAdapter hiddenItemAdapter = new HiddenItemAdapter(mContext, hiddenItemList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    HiddenItemView.setLayoutManager(mLayoutManager);
                                    HiddenItemView.setItemAnimator(new DefaultItemAnimator());
                                    HiddenItemView.setAdapter(hiddenItemAdapter);
                                    hiddenItemAdapter.notifyDataSetChanged();
                                } else {
                                    ActiveResponseLayout.setVisibility(View.GONE);
                                    HiddenResponseLayout.setVisibility(View.GONE);
                                    SoldResponseLayout.setVisibility(View.GONE);

                                    NoResponseLayout.setVisibility(View.VISIBLE);
                                    txtError.setText("No Data Available...");
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();

                                ActiveResponseLayout.setVisibility(View.GONE);
                                HiddenResponseLayout.setVisibility(View.GONE);
                                SoldResponseLayout.setVisibility(View.GONE);

                                NoResponseLayout.setVisibility(View.VISIBLE);
                                txtError.setText(ErrorMessage);
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
                        Log.e("ERROR", "" + error.getMessage());
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
                Log.e("HEADER", "" + APIConstant.getInstance().MY_HIDDEN_SALES + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_HIDDEN_SALES);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class HiddenItemAdapter extends RecyclerView.Adapter<HiddenItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            // RadioButton btnSelect;

            MyViewHolder(View view) {
                super(view);

                // btnSelect = view.findViewById(R.id.btnSelect);
            }
        }

        public HiddenItemAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }


        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hidden_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            ItemModel itemModel = arrayList.get(position);

            // holder.btnSelect.setText("  " + categoryModel.getCategoryName());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void MySaleUpdateStatusApi(final String ID, final String Status, final String From) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().MY_SALE_UPDATE_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().MY_SALE_UPDATE_STATUS + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            String Message = JsonMain.getString("msg");
                            if (HAS_ERROR.equals("false")) {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                                if (From.equals("Active")) {
                                    MyActiveSaleApi();
                                } else if (From.equals("Sold")) {
                                    MySoldSaleApi();
                                } else if (From.equals("Hidden")) {
                                    MyHiddenSaleApi();
                                }
                            } else {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
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
                        Log.e("ERROR", "" + error.getMessage());
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

            // Form data passing
            /*protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("email", edtEmail.getText().toString());
                // params.put("password", edtPassword.getText().toString());
                // params.put("secretkey", APIConstant.getInstance().ApiSecretsKey);
                Log.e("PARAMETER", "" + APIConstant.getInstance().MY_SALE_UPDATE_STATUS + params);
                return params;
            }*/

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"id\":\"" + ID + "\",\"status\":\"" + Status + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().MY_SALE_UPDATE_STATUS + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_SALE_UPDATE_STATUS);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void AddEditFavoriteApi(final String ProductID, final String ISFavorite) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ADD_EDIT_FAVORITE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().ADD_EDIT_FAVORITE + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            String Message = JsonMain.getString("msg");
                            if (HAS_ERROR.equals("false")) {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
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
                        Log.e("ERROR", "" + error.getMessage());
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
                String params = "{\"product_id\":\"" + ProductID + "\",\"is_favorite\":\"" + ISFavorite + "\"}";
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
