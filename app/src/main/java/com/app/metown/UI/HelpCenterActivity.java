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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.CategoryModel;
import com.app.metown.Models.ItemModel;
import com.app.metown.R;
import com.app.metown.VolleySupport.AppController;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelpCenterActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    TextView txtInquire;
    RecyclerView AttributeView, FAQView;
    ArrayList<ItemModel> attributeList = new ArrayList<>();
    ArrayList<CategoryModel> faqList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);

        Log.e("Activity", "HelpCenterActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();

        ViewOnClick();

        AddAttributeItems();

        FaqListApi();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        txtInquire = findViewById(R.id.txtInquire);

        AttributeView = findViewById(R.id.AttributeView);
        FAQView = findViewById(R.id.FAQView);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        txtInquire.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtInquire:
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String Verified = sharedPreferences.getString("Verified", "");
                Intent intent;
                if (Verified.equals("1")) {
                    intent = new Intent(mContext, InquireActivity.class);
                } else {
                    intent = new Intent(mContext, InquireInformationActivity.class);
                }
                startActivity(intent);
                break;
        }
    }

    public void AddAttributeItems() {
        attributeList.clear();
        ItemModel itemModel;

        itemModel = new ItemModel("1", "Operation\nPolicy");
        attributeList.add(itemModel);

        itemModel = new ItemModel("2", "Account\n/ Verification");
        attributeList.add(itemModel);

        itemModel = new ItemModel("3", "Buy / Sell");
        attributeList.add(itemModel);

        itemModel = new ItemModel("4", "Items");
        attributeList.add(itemModel);

        itemModel = new ItemModel("5", "Trade Manners");
        attributeList.add(itemModel);

        itemModel = new ItemModel("6", "Event\n/ Invitation");
        attributeList.add(itemModel);

        itemModel = new ItemModel("7", "Sanction");
        attributeList.add(itemModel);

        itemModel = new ItemModel("8", "Area\nadvertisement");
        attributeList.add(itemModel);

        itemModel = new ItemModel("9", "ETC");
        attributeList.add(itemModel);

        itemModel = new ItemModel("10", "Commercial\nBusiness");
        attributeList.add(itemModel);

        itemModel = new ItemModel("11", "");
        attributeList.add(itemModel);

        itemModel = new ItemModel("12", "");
        attributeList.add(itemModel);

        if (attributeList.size() > 0) {
            AttributeAdapter attributeAdapter = new AttributeAdapter(mContext, attributeList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 3, RecyclerView.VERTICAL, false);
            AttributeView.setLayoutManager(mLayoutManager);
            AttributeView.setItemAnimator(new DefaultItemAnimator());
            AttributeView.setAdapter(attributeAdapter);
            attributeAdapter.notifyDataSetChanged();
        }
    }

    public static class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtAttributeName;

            MyViewHolder(View view) {
                super(view);

                txtAttributeName = view.findViewById(R.id.txtAttributeName);
            }
        }

        public AttributeAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attribute_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            ItemModel itemModel = arrayList.get(position);

            String AttributeName = itemModel.getItemName();
            if (AttributeName.equals("") || AttributeName.equals("null") || AttributeName.equals(null) || AttributeName == null) {
                holder.txtAttributeName.setText("");
            } else {
                holder.txtAttributeName.setText(itemModel.getItemName());
            }

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

    private void FaqListApi() {
        String req = "req";
        faqList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().FAQ_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().FAQ_LIST + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONArray arrayData = JsonMain.getJSONArray("data");
                        for (int i = 0; i < arrayData.length(); i++) {
                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setCategoryID(arrayData.getJSONObject(i).getString("id"));
                            categoryModel.setCategoryName(arrayData.getJSONObject(i).getString("category_name"));
                            categoryModel.setCategoryTitle(arrayData.getJSONObject(i).getString("title"));
                            faqList.add(categoryModel);
                        }
                        if (faqList.size() > 0) {
                            FAQAdapter faqAdapter = new FAQAdapter(mContext, faqList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            FAQView.setLayoutManager(mLayoutManager);
                            FAQView.setItemAnimator(new DefaultItemAnimator());
                            FAQView.setAdapter(faqAdapter);
                            faqAdapter.notifyDataSetChanged();
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", MODE_PRIVATE);
                String Token = sharedPreferences.getString("Token", "");
                String Type = sharedPreferences.getString("Type", "");
                params.put("Content-Type", "application/json");
                // params.put("Authorization", Type + " " + Token);
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().FAQ_LIST + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().FAQ_LIST);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CategoryModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtQuestion;

            MyViewHolder(View view) {
                super(view);

                txtQuestion = view.findViewById(R.id.txtQuestion);
            }
        }

        public FAQAdapter(Context mContext, ArrayList<CategoryModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final CategoryModel categoryModel = arrayList.get(position);

            holder.txtQuestion.setText(categoryModel.getCategoryTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent FAQDetail = new Intent(mContext, FAQDetailActivity.class);
                    FAQDetail.putExtra("CategoryID", categoryModel.getCategoryID());
                    mContext.startActivity(FAQDetail);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}