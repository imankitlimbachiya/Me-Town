package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.app.metown.AppConstants.LoadMore;
import com.app.metown.Models.StoreSearchProductModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreAndServiceSearchActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    TextView txtGoCheck, txtRelatedKeyword, txtKeywordTitle, txtKeywordAndCount;
    ImageView imgBack;
    EditText edtKeyword, edtAddComment;
    RecyclerView KeywordSearchResultView;
    ArrayList<StoreSearchProductModel> keywordSearchResultList = new ArrayList<>();
    String Keyword = "", offSet = "0";
    Dialog dialog;
    KeywordSearchResultAdapter secondHandSearchItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_and_service_search);

        Log.e("Activity", "StoreAndServiceSearchActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GetIntentData();

        ViewInitialization();

        ViewOnClick();

        ViewSetText();

        // LoadMore(KeywordSearchResultView);

        SetAdapter();

        StoreProductSearchApi(Keyword, offSet);
    }

    public void GetIntentData() {
        Keyword = getIntent().getStringExtra("Keyword");
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        edtKeyword = findViewById(R.id.edtKeyword);

        txtKeywordAndCount = findViewById(R.id.txtKeywordAndCount);
        txtRelatedKeyword = findViewById(R.id.txtRelatedKeyword);
        txtKeywordTitle = findViewById(R.id.txtKeywordTitle);
        txtGoCheck = findViewById(R.id.txtGoCheck);
        txtGoCheck.setPaintFlags(txtGoCheck.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        KeywordSearchResultView = findViewById(R.id.KeywordSearchResultView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);

        edtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Keyword = charSequence.toString().trim();
                // StoreProductSearchApi(Keyword, offSet);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void ViewSetText() {
        txtRelatedKeyword.setText(Keyword);
        edtKeyword.setText(Keyword);
        txtKeywordTitle.setText(Keyword + " near you");
    }

    public void SetAdapter() {
        secondHandSearchItemAdapter = new KeywordSearchResultAdapter(mContext, keywordSearchResultList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        KeywordSearchResultView.setLayoutManager(mLayoutManager);
        KeywordSearchResultView.setItemAnimator(new DefaultItemAnimator());
        KeywordSearchResultView.setAdapter(secondHandSearchItemAdapter);
        // secondHandSearchItemAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    private void StoreProductSearchApi(final String Keyword, final String offSet) {
        String req = "req";
        keywordSearchResultList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().STORE_PRODUCT_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().STORE_PRODUCT_SEARCH + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; arrayData.length() > i; i++) {
                            StoreSearchProductModel storeSearchProductModel = new StoreSearchProductModel();
                            storeSearchProductModel.setID(arrayData.getJSONObject(i).getString("id"));
                            storeSearchProductModel.setSellerID(arrayData.getJSONObject(i).getString("seller_id"));
                            storeSearchProductModel.setBuyerID(arrayData.getJSONObject(i).getString("buyer_id"));
                            storeSearchProductModel.setCategoryID(arrayData.getJSONObject(i).getString("category_id"));
                            storeSearchProductModel.setCategoryTitle(arrayData.getJSONObject(i).getString("category_title"));
                            storeSearchProductModel.setName(arrayData.getJSONObject(i).getString("name"));
                            storeSearchProductModel.setDescription(arrayData.getJSONObject(i).getString("description"));
                            storeSearchProductModel.setPrice(arrayData.getJSONObject(i).getString("price"));
                            storeSearchProductModel.setImages(arrayData.getJSONObject(i).getString("images"));
                            storeSearchProductModel.setDataFrom(arrayData.getJSONObject(i).getString("data_from"));
                            storeSearchProductModel.setLocationName(arrayData.getJSONObject(i).getString("location_name"));
                            storeSearchProductModel.setType(arrayData.getJSONObject(i).getString("type"));
                            storeSearchProductModel.setIsFavourite(arrayData.getJSONObject(i).getString("is_favourite"));
                            storeSearchProductModel.setFavouriteCount(arrayData.getJSONObject(i).getString("favourite_count"));
                            storeSearchProductModel.setCommentCount(arrayData.getJSONObject(i).getString("commnet_count"));
                            keywordSearchResultList.add(storeSearchProductModel);
                        }
                        if (keywordSearchResultList.size() > 0) {
                            txtKeywordAndCount.setText("Result of searching " + Keyword + " (" + keywordSearchResultList.size() + ")");
                            secondHandSearchItemAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String ErrorMessage = JsonMain.getString("msg");
                        Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
                        txtKeywordAndCount.setText("Result of searching " + Keyword + " (" + 0 + ")");
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
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().STORE_PRODUCT_SEARCH + params);
                return params;
            }

            // Form data passing
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("keyword", Keyword);
                params.put("offset", offSet);
                Log.e("PARAMETER", "" + APIConstant.getInstance().STORE_PRODUCT_SEARCH + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().STORE_PRODUCT_SEARCH);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public class KeywordSearchResultAdapter extends RecyclerView.Adapter<KeywordSearchResultAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<StoreSearchProductModel> arrayList;
        String rupee;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgProductOrStore;
            TextView txtStoreName, txtAddress, txtPrice, txtReviewCount, txtLikeCount;
            LinearLayout CommentLayout, FavoriteLayout;

            MyViewHolder(View view) {
                super(view);

                imgProductOrStore = view.findViewById(R.id.imgProductOrStore);

                txtStoreName = view.findViewById(R.id.txtStoreName);
                txtAddress = view.findViewById(R.id.txtAddress);
                txtPrice = view.findViewById(R.id.txtPrice);
                txtReviewCount = view.findViewById(R.id.txtReviewCount);
                txtLikeCount = view.findViewById(R.id.txtLikeCount);

                CommentLayout = view.findViewById(R.id.CommentLayout);
                FavoriteLayout = view.findViewById(R.id.FavoriteLayout);
            }
        }

        public KeywordSearchResultAdapter(Context mContext, ArrayList<StoreSearchProductModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
            this.rupee = mContext.getString(R.string.rupee);
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.keyword_search_result_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final StoreSearchProductModel storeSearchProductModel = arrayList.get(position);

            String Images = storeSearchProductModel.getImages();
            if (Images.equals("") || Images.equals("null") || Images.equals(null) || Images == null) {

            } else {
                String[] separated = Images.split(",");
                Glide.with(mContext).load(separated[0]).into(holder.imgProductOrStore);
            }

            holder.txtStoreName.setText(storeSearchProductModel.getName());
            holder.txtAddress.setText(storeSearchProductModel.getLocationName());
            holder.txtPrice.setText(rupee + storeSearchProductModel.getPrice());
            holder.txtReviewCount.setText(storeSearchProductModel.getCommentCount());
            holder.txtLikeCount.setText(storeSearchProductModel.getFavouriteCount());

            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, BusinessStoreProfileActivity.class);
                    mContext.startActivity(intent);
                }
            });*/

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
                                AddEditCommentApi(storeSearchProductModel.getID(), edtAddComment.getText().toString().trim());
                            }
                        }
                    });
                    dialog.show();
                }
            });

            holder.FavoriteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddEditFavoriteApi(storeSearchProductModel.getID());
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
                        StoreProductSearchApi(Keyword, offSet);
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
                        StoreProductSearchApi(Keyword, offSet);
                    } else {
                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
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

    private void LoadMore(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.e("LoadMore", "" + LoadMore.getLastVisiblePosition(recyclerView));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}