package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectAdvertisingPostActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    Button btnMakeNewPost;
    EditText edtAddComment;
    Dialog dialog;
    RecyclerView ActiveItemView;
    ArrayList<ItemModel> activeItemList = new ArrayList<>();
    String offSet = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_advertising_post);

        Log.e("Activity", "SelectAdvertisingPostActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        MyActiveSaleApi(offSet);
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        btnMakeNewPost = findViewById(R.id.btnMakeNewPost);

        ActiveItemView = findViewById(R.id.ActiveItemView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnMakeNewPost.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnMakeNewPost:
                Intent PostForSale = new Intent(mContext, PostForSaleActivity.class);
                startActivity(PostForSale);
                break;
        }
    }

    private void MyActiveSaleApi(final String offSet) {
        String req = "req";
        activeItemList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().MY_ACTIVE_SALES + "/" + offSet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().MY_ACTIVE_SALES + "/" + offSet + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
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
                            itemModel.setItemCommentCount(arrayData.getJSONObject(i).getString("commnet_count"));
                            activeItemList.add(itemModel);
                        }
                        if (activeItemList.size() > 0) {
                            ActiveItemAdvertiseAdapter activeItemAdvertiseAdapter = new ActiveItemAdvertiseAdapter(mContext, activeItemList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            ActiveItemView.setLayoutManager(mLayoutManager);
                            ActiveItemView.setItemAnimator(new DefaultItemAnimator());
                            ActiveItemView.setAdapter(activeItemAdvertiseAdapter);
                            activeItemAdvertiseAdapter.notifyDataSetChanged();
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
                Log.e("HEADER", "" + APIConstant.getInstance().MY_ACTIVE_SALES + "/" + offSet + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_ACTIVE_SALES + "/" + offSet);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class ActiveItemAdvertiseAdapter extends RecyclerView.Adapter<ActiveItemAdvertiseAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemModel> arrayList;
        String rupee;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgProduct;
            TextView txtItemName, txtItemAddress, txtCommentCount, txtLikeCount;
            LinearLayout CommentLayout, FavouriteLayout, NextLayout;

            MyViewHolder(View view) {
                super(view);

                imgProduct = view.findViewById(R.id.imgProduct);

                txtItemName = view.findViewById(R.id.txtItemName);
                txtItemAddress = view.findViewById(R.id.txtItemAddress);
                txtCommentCount = view.findViewById(R.id.txtCommentCount);
                txtLikeCount = view.findViewById(R.id.txtLikeCount);

                CommentLayout = view.findViewById(R.id.CommentLayout);
                FavouriteLayout = view.findViewById(R.id.FavouriteLayout);
                NextLayout = view.findViewById(R.id.NextLayout);
            }
        }

        public ActiveItemAdvertiseAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
            this.rupee = mContext.getString(R.string.rupee);
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_item_advertise_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final ItemModel itemModel = arrayList.get(position);

            String Images = itemModel.getItemImages();
            String[] separated = Images.split(",");
            Glide.with(mContext).load(separated[0]).into(holder.imgProduct);
            holder.txtCommentCount.setText(itemModel.getItemCommentCount());
            holder.txtLikeCount.setText(itemModel.getItemFavouriteCount());

            holder.txtItemName.setText(itemModel.getItemName());

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

            holder.NextLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent SelectAdvertisingPostDetail = new Intent(mContext, SelectAdvertisingPostDetailActivity.class);
                    SelectAdvertisingPostDetail.putExtra("ItemID", itemModel.getItemID());
                    SelectAdvertisingPostDetail.putExtra("ItemImages", itemModel.getItemImages());
                    SelectAdvertisingPostDetail.putExtra("ItemName", itemModel.getItemName());
                    SelectAdvertisingPostDetail.putExtra("ItemAddress", "");
                    startActivity(SelectAdvertisingPostDetail);
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
                        dialog.dismiss();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}