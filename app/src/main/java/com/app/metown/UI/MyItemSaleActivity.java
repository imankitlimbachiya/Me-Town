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

public class MyItemSaleActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    EasyPopup mQQPop;
    RelativeLayout NoResponseLayout;
    LinearLayout AllLayout, ActiveLayout, SoldLayout, ActiveResponseLayout, SoldResponseLayout, AllResponseLayout;
    TextView txtAll, txtActive, txtSold, txtError;
    EditText edtAddComment;
    View AllView, ActiveView, SoldView;
    RecyclerView AllItemView, ActiveItemView, SoldItemView;
    ArrayList<ItemModel> allItemList = new ArrayList<>();
    ArrayList<ItemModel> activeItemList = new ArrayList<>();
    ArrayList<ItemModel> soldItemList = new ArrayList<>();
    JSONArray arrayData;
    Dialog dialog;
    String ItemID = "", From = "Active", offSetAll = "0", offSetActive = "0", offSetSold = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item_sale);

        Log.e("Activity", "MySaleActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        ViewSetText();

        OpenEasyPopup();

        MyAllSaleApi(offSetAll);

        RecyclerViewScrollListener(AllItemView);
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        AllView = findViewById(R.id.AllView);
        ActiveView = findViewById(R.id.ActiveView);
        SoldView = findViewById(R.id.SoldView);

        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        AllLayout = findViewById(R.id.AllLayout);
        ActiveLayout = findViewById(R.id.ActiveLayout);
        SoldLayout = findViewById(R.id.SoldLayout);
        AllResponseLayout = findViewById(R.id.AllResponseLayout);
        ActiveResponseLayout = findViewById(R.id.ActiveResponseLayout);
        SoldResponseLayout = findViewById(R.id.SoldResponseLayout);

        txtAll = findViewById(R.id.txtAll);
        txtActive = findViewById(R.id.txtActive);
        txtSold = findViewById(R.id.txtSold);
        txtError = findViewById(R.id.txtError);

        AllItemView = findViewById(R.id.AllItemView);
        ActiveItemView = findViewById(R.id.ActiveItemView);
        SoldItemView = findViewById(R.id.SoldItemView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        AllLayout.setOnClickListener(this);
        ActiveLayout.setOnClickListener(this);
        SoldLayout.setOnClickListener(this);
    }

    public void ViewSetText() {
        txtAll.setTextColor(getResources().getColor(R.color.black));
        AllView.setBackgroundColor(getResources().getColor(R.color.black));
        txtActive.setTextColor(getResources().getColor(R.color.grey));
        ActiveView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtSold.setTextColor(getResources().getColor(R.color.grey));
        SoldView.setBackgroundColor(getResources().getColor(R.color.grey));
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

                        if (From.equals("Active")) {
                            txtUnHide.setVisibility(View.GONE);
                        } else if (From.equals("Sold")) {
                            txtHide.setVisibility(View.GONE);
                            txtUnHide.setVisibility(View.GONE);
                        } else if (From.equals("Hidden")) {

                        }

                        txtEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                        txtHide.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MySaleUpdateStatusApi(ItemID, "4", From);
                            }
                        });

                        txtUnHide.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                        txtDeletePost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MySaleDeleteApi(ItemID, From);
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

    public void RecyclerViewScrollListener(RecyclerView recyclerView) {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (activeItemList.size() >= 30) {
                    if (isLastItemDisplaying(recyclerView)) {
                        offSetActive = String.valueOf(activeItemList.size());
                        MyActiveSaleApi(offSetActive);
                    }
                }

                /*if (isLastItemDisplaying(recyclerView)) {
                    offSetActive = String.valueOf(activeItemList.size());
                    MyActiveSaleApi(offSetActive);
                }*/
            }

            private boolean isLastItemDisplaying(RecyclerView recyclerView) {
                // Check if the adapter item count is greater than 0
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    //get the last visible item on screen using the layout manager
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    return lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.AllLayout:
                RecyclerViewScrollListener(AllItemView);
                MyAllSaleApi(offSetAll);
                break;
            case R.id.ActiveLayout:
                RecyclerViewScrollListener(ActiveItemView);
                MyActiveSaleApi(offSetActive);
                break;
            case R.id.SoldLayout:
                RecyclerViewScrollListener(SoldItemView);
                MySoldSaleApi(offSetSold);
                break;
        }
    }

    private void MyAllSaleApi(final String offSetAll) {
        txtAll.setTextColor(getResources().getColor(R.color.black));
        AllView.setBackgroundColor(getResources().getColor(R.color.black));
        txtActive.setTextColor(getResources().getColor(R.color.grey));
        ActiveView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtSold.setTextColor(getResources().getColor(R.color.grey));
        SoldView.setBackgroundColor(getResources().getColor(R.color.grey));

        String req = "req";
        allItemList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().MY_ALL_SALE + "/" + offSetAll,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().MY_ALL_SALE + "/" + offSetAll + response);
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
                                    itemModel.setItemCommentCount(arrayData.getJSONObject(i).getString("commnet_count"));
                                    allItemList.add(itemModel);
                                }
                                if (allItemList.size() > 0) {
                                    NoResponseLayout.setVisibility(View.GONE);

                                    AllResponseLayout.setVisibility(View.VISIBLE);
                                    ActiveResponseLayout.setVisibility(View.GONE);
                                    SoldResponseLayout.setVisibility(View.GONE);

                                    AllItemAdapter hiddenItemAdapter = new AllItemAdapter(mContext, allItemList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    AllItemView.setLayoutManager(mLayoutManager);
                                    AllItemView.setItemAnimator(new DefaultItemAnimator());
                                    AllItemView.setAdapter(hiddenItemAdapter);
                                    hiddenItemAdapter.notifyDataSetChanged();
                                } else {
                                    AllResponseLayout.setVisibility(View.GONE);
                                    ActiveResponseLayout.setVisibility(View.GONE);
                                    SoldResponseLayout.setVisibility(View.GONE);

                                    NoResponseLayout.setVisibility(View.VISIBLE);
                                    txtError.setText("No Data Available...");
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
                Log.e("PARAMETER", "" + APIConstant.getInstance().MY_ALL_SALE + "/" + offSetAll + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_ALL_SALE + "/" + offSetAll);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class AllItemAdapter extends RecyclerView.Adapter<AllItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemModel> arrayList;
        String rupee;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgItem;
            TextView txtItemName, txtItemPrice, txtCommentCount, txtLikeCount;
            RelativeLayout UnHidePostLayout, OptionLayout;
            LinearLayout CommentLayout, FavouriteLayout;

            MyViewHolder(View view) {
                super(view);

                imgItem = view.findViewById(R.id.imgItem);

                txtItemName = view.findViewById(R.id.txtItemName);
                txtItemPrice = view.findViewById(R.id.txtItemPrice);
                txtCommentCount = view.findViewById(R.id.txtCommentCount);
                txtLikeCount = view.findViewById(R.id.txtLikeCount);

                OptionLayout = view.findViewById(R.id.OptionLayout);
                UnHidePostLayout = view.findViewById(R.id.UnHidePostLayout);

                CommentLayout = view.findViewById(R.id.CommentLayout);
                FavouriteLayout = view.findViewById(R.id.FavouriteLayout);
            }
        }

        public AllItemAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
            this.rupee = mContext.getString(R.string.rupee);
        }


        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final ItemModel itemModel = arrayList.get(position);

            String Images = itemModel.getItemImages();
            String[] separated = Images.split(",");
            Glide.with(mContext).load(separated[0]).into(holder.imgItem);

            holder.txtItemName.setText(itemModel.getItemName());
            holder.txtItemPrice.setText(rupee + " " + itemModel.getItemPrice());
            holder.txtCommentCount.setText(itemModel.getItemCommentCount());
            holder.txtLikeCount.setText(itemModel.getItemFavouriteCount());

            holder.OptionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemID = "";
                    From = "";
                    ItemID = itemModel.getItemID();
                    From = "All";
                    Log.e("ItemID", "" + ItemID);
                    Log.e("From", "" + From);
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
                                AddEditCommentApi(itemModel.getItemID(), edtAddComment.getText().toString().trim(), "All");
                            }
                        }
                    });
                    dialog.show();
                }
            });

            holder.FavouriteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddEditFavoriteApi(itemModel.getItemID(), "All");
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void MyActiveSaleApi(final String offSetActive) {
        txtAll.setTextColor(getResources().getColor(R.color.grey));
        AllView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtActive.setTextColor(getResources().getColor(R.color.black));
        ActiveView.setBackgroundColor(getResources().getColor(R.color.black));
        txtSold.setTextColor(getResources().getColor(R.color.grey));
        SoldView.setBackgroundColor(getResources().getColor(R.color.grey));

        String req = "req";
        activeItemList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().MY_ACTIVE_SALES + "/" + offSetActive,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().MY_ACTIVE_SALES + "/" + offSetActive + response);
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
                                    itemModel.setItemCommentCount(arrayData.getJSONObject(i).getString("commnet_count"));
                                    activeItemList.add(itemModel);
                                }

                                if (activeItemList.size() > 0) {
                                    NoResponseLayout.setVisibility(View.GONE);

                                    AllResponseLayout.setVisibility(View.GONE);
                                    ActiveResponseLayout.setVisibility(View.VISIBLE);
                                    SoldResponseLayout.setVisibility(View.GONE);

                                    ActiveItemAdapter activeItemAdapter = new ActiveItemAdapter(mContext, activeItemList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    ActiveItemView.setLayoutManager(mLayoutManager);
                                    ActiveItemView.setItemAnimator(new DefaultItemAnimator());
                                    ActiveItemView.setAdapter(activeItemAdapter);
                                    activeItemAdapter.notifyDataSetChanged();
                                } else {
                                    AllResponseLayout.setVisibility(View.GONE);
                                    ActiveResponseLayout.setVisibility(View.GONE);
                                    SoldResponseLayout.setVisibility(View.GONE);

                                    NoResponseLayout.setVisibility(View.VISIBLE);
                                    txtError.setText("No Data Available...");
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();

                                AllResponseLayout.setVisibility(View.GONE);
                                ActiveResponseLayout.setVisibility(View.GONE);
                                SoldResponseLayout.setVisibility(View.GONE);

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
                Log.e("HEADER", "" + APIConstant.getInstance().MY_ACTIVE_SALES + "/" + offSetActive + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_ACTIVE_SALES + "/" + offSetActive);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class ActiveItemAdapter extends RecyclerView.Adapter<ActiveItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemModel> arrayList;
        String rupee;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgItem;
            TextView txtItemName, txtAddressOrTimePosted, txtItemPrice, txtChangeToReserved, txtChangeToSold,
                    txtCommentCount, txtLikeCount;
            RelativeLayout OptionLayout;
            LinearLayout CommentLayout, FavouriteLayout;

            MyViewHolder(View view) {
                super(view);

                imgItem = view.findViewById(R.id.imgItem);

                txtItemName = view.findViewById(R.id.txtItemName);
                txtAddressOrTimePosted = view.findViewById(R.id.txtAddressOrTimePosted);
                txtItemPrice = view.findViewById(R.id.txtItemPrice);
                txtChangeToReserved = view.findViewById(R.id.txtChangeToReserved);
                txtChangeToSold = view.findViewById(R.id.txtChangeToSold);
                txtCommentCount = view.findViewById(R.id.txtCommentCount);
                txtLikeCount = view.findViewById(R.id.txtLikeCount);

                OptionLayout = view.findViewById(R.id.OptionLayout);

                CommentLayout = view.findViewById(R.id.CommentLayout);
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

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final ItemModel itemModel = arrayList.get(position);

            String Images = itemModel.getItemImages();
            String[] separated = Images.split(",");
            Glide.with(mContext).load(separated[0]).into(holder.imgItem);

            holder.txtCommentCount.setText(itemModel.getItemCommentCount());
            holder.txtLikeCount.setText(itemModel.getItemFavouriteCount());

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
                    ItemID = "";
                    From = "";
                    ItemID = itemModel.getItemID();
                    From = "Active";
                    Log.e("ItemID", "" + ItemID);
                    Log.e("From", "" + From);
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
                                AddEditCommentApi(itemModel.getItemID(), edtAddComment.getText().toString().trim(), "Active");
                            }
                        }
                    });
                    dialog.show();
                }
            });

            holder.FavouriteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddEditFavoriteApi(itemModel.getItemID(), "Active");
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private void MySoldSaleApi(final String offSetSold) {
        txtAll.setTextColor(getResources().getColor(R.color.grey));
        AllView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtActive.setTextColor(getResources().getColor(R.color.grey));
        ActiveView.setBackgroundColor(getResources().getColor(R.color.grey));
        txtSold.setTextColor(getResources().getColor(R.color.black));
        SoldView.setBackgroundColor(getResources().getColor(R.color.black));

        String req = "req";
        soldItemList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().MY_SOLD_SALES + "/" + offSetSold,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().MY_SOLD_SALES + "/" + offSetSold + response);
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
                                    itemModel.setItemCommentCount(arrayData.getJSONObject(i).getString("commnet_count"));
                                    soldItemList.add(itemModel);
                                }

                                if (soldItemList.size() > 0) {
                                    NoResponseLayout.setVisibility(View.GONE);

                                    AllResponseLayout.setVisibility(View.GONE);
                                    ActiveResponseLayout.setVisibility(View.GONE);
                                    SoldResponseLayout.setVisibility(View.VISIBLE);

                                    SoldItemAdapter soldItemAdapter = new SoldItemAdapter(mContext, soldItemList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                                    SoldItemView.setLayoutManager(mLayoutManager);
                                    SoldItemView.setItemAnimator(new DefaultItemAnimator());
                                    SoldItemView.setAdapter(soldItemAdapter);
                                    soldItemAdapter.notifyDataSetChanged();
                                } else {
                                    AllResponseLayout.setVisibility(View.GONE);
                                    ActiveResponseLayout.setVisibility(View.GONE);
                                    SoldResponseLayout.setVisibility(View.GONE);

                                    NoResponseLayout.setVisibility(View.VISIBLE);
                                    txtError.setText("No Data Available...");
                                }
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();

                                AllResponseLayout.setVisibility(View.GONE);
                                ActiveResponseLayout.setVisibility(View.GONE);
                                SoldResponseLayout.setVisibility(View.GONE);

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
                        // Log.e("ERROR", "" + error.getMessage());
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
                Log.e("HEADER", "" + APIConstant.getInstance().MY_SOLD_SALES + "/" + offSetSold + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().MY_SOLD_SALES + "/" + offSetSold);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class SoldItemAdapter extends RecyclerView.Adapter<SoldItemAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemModel> arrayList;
        String rupee;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgItem;
            TextView txtItemName, txtItemPrice, txtCommentCount, txtLikeCount;
            RelativeLayout ReviewLayout, OptionLayout;
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

                ReviewLayout = view.findViewById(R.id.ReviewLayout);
            }
        }

        public SoldItemAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
            this.rupee = mContext.getString(R.string.rupee);
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sold_item_adapter, parent, false);
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
            holder.txtCommentCount.setText(itemModel.getItemCommentCount());
            holder.txtLikeCount.setText(itemModel.getItemFavouriteCount());

            holder.OptionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemID = "";
                    From = "";
                    ItemID = itemModel.getItemID();
                    From = "Sold";
                    Log.e("ItemID", "" + ItemID);
                    Log.e("From", "" + From);
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
                                AddEditCommentApi(itemModel.getItemID(), edtAddComment.getText().toString().trim(), "Sold");
                            }
                        }
                    });
                    dialog.show();
                }
            });

            holder.FavouriteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddEditFavoriteApi(itemModel.getItemID(), "Sold");
                }
            });

            holder.ReviewLayout.setOnClickListener(new View.OnClickListener() {
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
                            mQQPop.dismiss();
                            Log.e("RESPONSE", "" + APIConstant.getInstance().MY_SALE_UPDATE_STATUS + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            String Message = JsonMain.getString("msg");
                            if (HAS_ERROR.equals("false")) {
                                switch (From) {
                                    case "All":
                                        MyAllSaleApi(offSetAll);
                                        break;
                                    case "Active":
                                        MyActiveSaleApi(offSetActive);
                                        break;
                                    case "Sold":
                                        MySoldSaleApi(offSetSold);
                                        break;
                                }
                            } else {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        // Log.e("ERROR", "" + error.getMessage());
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

    private void MySaleDeleteApi(final String ID, final String From) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().MY_SALE_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            mQQPop.dismiss();
                            Log.e("RESPONSE", "" + APIConstant.getInstance().MY_SALE_DELETE + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            String Message = JsonMain.getString("msg");
                            if (HAS_ERROR.equals("false")) {
                                switch (From) {
                                    case "All":
                                        MyAllSaleApi(offSetAll);
                                        break;
                                    case "Active":
                                        MyActiveSaleApi(offSetActive);
                                        break;
                                    case "Sold":
                                        MySoldSaleApi(offSetSold);
                                        break;
                                }
                            } else {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        // Log.e("ERROR", "" + error.getMessage());
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

    private void AddEditFavoriteApi(final String ProductID, final String From) {
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
                                switch (From) {
                                    case "All":
                                        MyAllSaleApi(offSetAll);
                                        break;
                                    case "Active":
                                        MyActiveSaleApi(offSetActive);
                                        break;
                                    case "Sold":
                                        MySoldSaleApi(offSetSold);
                                        break;
                                }
                            } else {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        // Log.e("ERROR", "" + error.getMessage());
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

    private void AddEditCommentApi(final String ProductID, final String Comment, final String From) {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().ADD_EDIT_COMMENT,
                new Response.Listener<String>() {
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
                                switch (From) {
                                    case "All":
                                        MyAllSaleApi(offSetAll);
                                        break;
                                    case "Active":
                                        MyActiveSaleApi(offSetActive);
                                        break;
                                    case "Sold":
                                        MySoldSaleApi(offSetSold);
                                        break;
                                }
                            } else {
                                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
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
