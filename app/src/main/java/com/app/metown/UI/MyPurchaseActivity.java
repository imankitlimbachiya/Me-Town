package com.app.metown.UI;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class MyPurchaseActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar, progress;
    ImageView imgBack;
    EasyPopup mQQPop;
    TextView txtError;
    RelativeLayout NoResponseLayout;
    MyPurchasesAdapter myPurchasesAdapter;
    RecyclerView MyPurchasesView;
    ArrayList<ItemModel> myPurchasesList = new ArrayList<>();
    JSONArray arrayData;
    Dialog dialog;
    String offSet = "0", ItemID = "";
    EditText edtAddComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchases);

        Log.e("Activity", "MyPurchasesActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        OpenEasyPopup();

        SetAdapter();

        MyPurchaseApi(offSet);

        RecyclerViewScrollListener(MyPurchasesView);
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);
        progress = findViewById(R.id.progress);

        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        imgBack = findViewById(R.id.imgBack);

        txtError = findViewById(R.id.txtError);

        MyPurchasesView = findViewById(R.id.MyPurchasesView);
    }

    public void SetAdapter() {
        myPurchasesAdapter = new MyPurchasesAdapter(mContext, myPurchasesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        MyPurchasesView.setLayoutManager(mLayoutManager);
        MyPurchasesView.setItemAnimator(new DefaultItemAnimator());
        MyPurchasesView.setAdapter(myPurchasesAdapter);
        // myPurchasesAdapter.notifyDataSetChanged();
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    public void OpenEasyPopup() {
        mQQPop = EasyPopup.create()
                .setContext(mContext)
                .setContentView(R.layout.option_menu)
                // .setAnimationStyle(R.style.RightTop2PopAnim)
                .setOnViewListener(new EasyPopup.OnViewListener() {
                    @Override
                    public void initViews(View view, EasyPopup basePopup) {
                        TextView txtEdit = view.findViewById(R.id.txtEdit);
                        TextView txtHide = view.findViewById(R.id.txtHide);
                        TextView txtUnHide = view.findViewById(R.id.txtUnHide);
                        TextView txtDeletePost = view.findViewById(R.id.txtDeletePost);

                        txtEdit.setVisibility(View.GONE);
                        txtHide.setVisibility(View.GONE);
                        txtUnHide.setVisibility(View.GONE);

                        txtDeletePost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MySaleDeleteApi(ItemID);
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

    private void MySaleDeleteApi(final String ID) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().MY_SALE_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    mQQPop.dismiss();
                    Log.e("RESPONSE", "" + APIConstant.getInstance().MY_SALE_DELETE + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    String Message = JsonMain.getString("msg");
                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                        MyPurchaseApi(offSet);
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
                // params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().MY_SALE_DELETE + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ID);
                Log.e("PARAMETER", "" + APIConstant.getInstance().MY_SALE_DELETE + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_SALE_DELETE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void MyPurchaseApi(final String offSet) {
        String req = "req";
        if (offSet.equals("0")) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.VISIBLE);
        }
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().MY_PURCHASE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (offSet.equals("0")) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progress.setVisibility(View.GONE);
                    }
                    Log.e("RESPONSE", "" + APIConstant.getInstance().MY_PURCHASE + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        arrayData = JsonMain.getJSONArray("data");
                        SetApiAdapter(arrayData);
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                        NoResponseLayout.setVisibility(View.VISIBLE);
                        txtError.setText(ErrorMessage);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (offSet.equals("0")) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progress.setVisibility(View.GONE);
                }
            }
        }) {

            // Header data passing
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().MY_PURCHASE + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"offset\":\"" + offSet + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().MY_PURCHASE + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_PURCHASE);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void SetApiAdapter(JSONArray arrayData) {
        myPurchasesList.clear();
        try {
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
                itemModel.setItemFavouriteCount(arrayData.getJSONObject(i).getString("favourite_count"));
                itemModel.setItemCommentCount(arrayData.getJSONObject(i).getString("commnet_count"));
                myPurchasesList.add(itemModel);
            }

            if (myPurchasesList.size() > 0) {
                /*MyPurchasesAdapter myPurchasesAdapter = new MyPurchasesAdapter(mContext, myPurchasesList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                MyPurchasesView.setLayoutManager(mLayoutManager);
                MyPurchasesView.setItemAnimator(new DefaultItemAnimator());
                MyPurchasesView.setAdapter(myPurchasesAdapter);*/
                myPurchasesAdapter.notifyDataSetChanged();
            } else {
                NoResponseLayout.setVisibility(View.VISIBLE);
                txtError.setText("No Data Available...");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public class MyPurchasesAdapter extends RecyclerView.Adapter<MyPurchasesAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemModel> arrayList;
        String rupee;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgItem;
            TextView txtItemName, txtItemPrice, txtCommentCount, txtLikeCount;
            RelativeLayout GoReviewLayout, OptionLayout;
            LinearLayout CommentLayout, FavouriteLayout;

            MyViewHolder(View view) {
                super(view);

                imgItem = view.findViewById(R.id.imgItem);

                txtItemName = view.findViewById(R.id.txtItemName);
                txtItemPrice = view.findViewById(R.id.txtItemPrice);
                txtCommentCount = view.findViewById(R.id.txtCommentCount);
                txtLikeCount = view.findViewById(R.id.txtLikeCount);

                OptionLayout = view.findViewById(R.id.OptionLayout);

                CommentLayout = view.findViewById(R.id.CommentLayout);
                FavouriteLayout = view.findViewById(R.id.FavouriteLayout);

                GoReviewLayout = view.findViewById(R.id.GoReviewLayout);
            }
        }

        public MyPurchasesAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
            this.rupee = mContext.getString(R.string.rupee);
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_purchases_item_adapter, parent, false);
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
            holder.txtItemPrice.setText(rupee + " " + itemModel.getItemPrice());
            holder.txtCommentCount.setText(itemModel.getItemCommentCount());
            holder.txtLikeCount.setText(itemModel.getItemFavouriteCount());

            holder.OptionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemID = "";
                    ItemID = itemModel.getItemID();
                    OpenEasyPopup();
                    mQQPop.showAtAnchorView(view, YGravity.BELOW, XGravity.LEFT, 0, 0);
                }
            });

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

            holder.GoReviewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String Images = itemModel.getItemImages();
                    String[] separated = Images.split(",");
                    Intent SelectBuyer = new Intent(mContext, SelectBuyerActivity.class);
                    SelectBuyer.putExtra("ItemID", itemModel.getItemID());
                    SelectBuyer.putExtra("ItemName", itemModel.getItemName());
                    SelectBuyer.putExtra("ItemImage", separated[0]);
                    mContext.startActivity(SelectBuyer);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent SecondHandPost = new Intent(mContext, SecondHandPostActivity.class);
                    SecondHandPost.putExtra("ID", itemModel.getItemID());
                    SecondHandPost.putExtra("SellerID", itemModel.getItemSellerID());
                    mContext.startActivity(SecondHandPost);
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
                        MyPurchaseApi(offSet);
                    } else {
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
                        MyPurchaseApi(offSet);
                    } else {
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

    public void RecyclerViewScrollListener(RecyclerView recyclerView) {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (myPurchasesList.size() >= 30) {
                    if (isLastItemDisplaying(recyclerView)) {
                        offSet = String.valueOf(myPurchasesList.size());
                        MyPurchaseApi(offSet);
                    }
                }
            }

            private boolean isLastItemDisplaying(RecyclerView recyclerView) {
                // Check if the adapter item count is greater than 0
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    //get the last visible item on screen using the layout manager
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}