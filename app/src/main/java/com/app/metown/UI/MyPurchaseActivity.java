package com.app.metown.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.zyyoona7.popup.EasyPopup;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPurchaseActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ProgressBar progressBar;
    ImageView imgBack;
    EasyPopup mQQPop;
    TextView txtError;
    RelativeLayout NoResponseLayout;
    RecyclerView MyPurchasesView;
    ArrayList<ItemModel> myPurchasesList = new ArrayList<>();
    JSONArray arrayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchases);

        Log.e("Activity","MyPurchasesActivity");

        mContext = this;

        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewInitialization();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        NoResponseLayout = findViewById(R.id.NoResponseLayout);

        imgBack = findViewById(R.id.imgBack);

        txtError = findViewById(R.id.txtError);

        imgBack.setOnClickListener(this);

        MyPurchasesView = findViewById(R.id.MyPurchasesView);

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

        MyPurchaseApi();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        /*switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }*/
        if (view.getId() == R.id.imgBack) {
            finish();
        }
    }

    private void MyPurchaseApi() {
        String req = "req";
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, APIConstant.getInstance().MY_PURCHASE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().MY_PURCHASE + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String HAS_ERROR = JsonMain.getString("has_error");
                            if (HAS_ERROR.equals("false")) {
                                arrayData = JsonMain.getJSONArray("data");
                                SetApiAdapter(arrayData);
                            } else {
                                String ErrorMessage = JsonMain.getString("msg");
                                Toast.makeText(mContext, ErrorMessage, Toast.LENGTH_LONG).show();
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
                params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbWV0b3duLmFwLXNvdXRoZWFzdC0xLmVsYXN0aWNiZWFuc3RhbGsuY29tL2FwaS9sb2dpbiIsImlhdCI6MTYwNjQxMzY1NywiZXhwIjoxNjA3NjIzMjU3LCJuYmYiOjE2MDY0MTM2NTcsImp0aSI6IjdkaHlrb1hzbWhCaWdxdU4iLCJzdWIiOjEsInBydiI6Ijg3ZTBhZjFlZjlmZDE1ODEyZmRlYzk3MTUzYTE0ZTBiMDQ3NTQ2YWEifQ.D_NZt_zTqC23_75avlVI54SY_UqbG1zHG6lSrcLgy58");
                params.put("Accept", "application/json");
                Log.e("HEADER", "" + APIConstant.getInstance().MY_PURCHASE + params);
                return params;
            }

            // Form data passing
            /*protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("email", edtEmail.getText().toString());
                // params.put("fcm_token", edtPassword.getText().toString());
                // params.put("device_type", APIConstant.getInstance().ApiSecretsKey);
                Log.e("PARAMETER", "" + APIConstant.getInstance().SIGN_UP + params);
                return params;
            }*/

            // Raw data passing
            /*@Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"nick_name\":\"" + NickName + "\",\"email\":\"" + Email + "\",\"phone_number\":\"" + PhoneNumber +
                        "\",\"device_type\":\"" + "A" + "\",\"social_id\":\"" + "" + "\",\"referral_code\":\"" + ReferralCode +
                        "\",\"fcm_token\":\"" + FCMToken + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().MY_SALES + params);
                return params.getBytes();
            }*/
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
                myPurchasesList.add(itemModel);
            }

            if (myPurchasesList.size() > 0) {
                MyPurchasesAdapter myPurchasesAdapter = new MyPurchasesAdapter(mContext, myPurchasesList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                MyPurchasesView.setLayoutManager(mLayoutManager);
                MyPurchasesView.setItemAnimator(new DefaultItemAnimator());
                MyPurchasesView.setAdapter(myPurchasesAdapter);
                myPurchasesAdapter.notifyDataSetChanged();
            } else {
                NoResponseLayout.setVisibility(View.VISIBLE);
                txtError.setText("No Data Available...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
        }
    }

    public static class MyPurchasesAdapter extends RecyclerView.Adapter<MyPurchasesAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<ItemModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtItemName;
            RelativeLayout OptionLayout, GoReviewLayout;

            MyViewHolder(View view) {
                super(view);

                txtItemName = view.findViewById(R.id.txtItemName);

                OptionLayout = view.findViewById(R.id.OptionLayout);
                GoReviewLayout = view.findViewById(R.id.GoReviewLayout);
            }
        }

        public MyPurchasesAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_purchases_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            ItemModel itemModel = arrayList.get(position);

            holder.txtItemName.setText(itemModel.getItemName());

            holder.OptionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // mQQPop.showAtAnchorView(view, YGravity.BELOW, XGravity.LEFT, 50, -55);
                }
            });

            holder.GoReviewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent Review = new Intent(mContext, ReviewActivity.class);
                    mContext.startActivity(Review);
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
