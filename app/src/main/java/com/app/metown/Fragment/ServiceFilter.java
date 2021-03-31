package com.app.metown.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import com.app.metown.Adapters.SecondHandServiceCategoryAdapter;
import com.app.metown.Adapters.ServiceNearbyCategoryAdapter;
import com.app.metown.AppConstants.APIConstant;
import com.app.metown.Models.CategoryModel;
import com.app.metown.R;
import com.app.metown.UI.HiringHelperActivity;
import com.app.metown.UI.NotificationActivity;
import com.app.metown.UI.SearchAroundYourLocationActivity;
import com.app.metown.UI.StoreAndServiceNearYouActivity;
import com.app.metown.UI.StoreAndServiceSearchActivity;
import com.app.metown.VolleySupport.AppController;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ServiceFilter extends Fragment implements View.OnClickListener {

    public ServiceFilter() {
        // Required empty public constructor
    }

    Context mContext;
    ProgressBar progressBar;
    ImageView imgAlert, imgSearch;
    TextView txtFindMoreService;
    RecyclerView MyTownServiceView, SecondHandServiceView, StoreServiceView;
    ArrayList<CategoryModel> myTownServiceList = new ArrayList<>();
    ArrayList<CategoryModel> secondHandServiceList = new ArrayList<>();
    ArrayList<CategoryModel> storeServiceList = new ArrayList<>();
    String CategoryType = "1", ParentID = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_filter, container, false);

        Log.e("Fragment", "ServiceFilter");

        mContext = getActivity();

        ViewInitialization(view);

        ViewOnClick();

        GetFilterApi(CategoryType, ParentID);

        return view;
    }

    public void ViewInitialization(View view) {
        progressBar = view.findViewById(R.id.progressBar);

        txtFindMoreService = view.findViewById(R.id.txtFindMoreService);

        imgAlert = view.findViewById(R.id.imgAlert);
        imgSearch = view.findViewById(R.id.imgSearch);

        MyTownServiceView = view.findViewById(R.id.MyTownServiceView);
        SecondHandServiceView = view.findViewById(R.id.SecondHandServiceView);
        StoreServiceView = view.findViewById(R.id.StoreServiceView);
    }

    public void ViewOnClick() {
        imgAlert.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        txtFindMoreService.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgAlert:
                Intent notification = new Intent(mContext, NotificationActivity.class);
                startActivity(notification);
                break;
            case R.id.imgSearch:
                Intent SearchAroundYourLocation = new Intent(mContext, SearchAroundYourLocationActivity.class);
                startActivity(SearchAroundYourLocation);
                break;
            case R.id.txtFindMoreService:
                Intent StoreAndServiceNearYou = new Intent(mContext, StoreAndServiceNearYouActivity.class);
                startActivity(StoreAndServiceNearYou);
                break;
        }
    }

    private void GetFilterApi(final String CategoryType, final String ParentID) {
        String req = "req";
        myTownServiceList.clear();
        secondHandServiceList.clear();
        storeServiceList.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, APIConstant.getInstance().FILTER, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    Log.e("RESPONSE", "" + APIConstant.getInstance().FILTER + response);
                    JSONObject JsonMain = new JSONObject(response);
                    String HAS_ERROR = JsonMain.getString("has_error");
                    if (HAS_ERROR.equalsIgnoreCase("false")) {
                        JSONObject objectData = JsonMain.getJSONObject("data");
                        JSONArray arrayCategoryList = objectData.getJSONArray("category_list");
                        for (int i = 0; i < arrayCategoryList.length(); i++) {
                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setCategoryID(arrayCategoryList.getJSONObject(i).getString("id"));
                            categoryModel.setCategoryTitle(arrayCategoryList.getJSONObject(i).getString("name"));
                            myTownServiceList.add(categoryModel);
                        }
                        if (myTownServiceList.size() > 0) {
                            ServiceNearbyCategoryAdapter serviceNearbyCategoryAdapter = new ServiceNearbyCategoryAdapter(mContext, myTownServiceList);
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false);
                            MyTownServiceView.setLayoutManager(mLayoutManager);
                            MyTownServiceView.setItemAnimator(new DefaultItemAnimator());
                            MyTownServiceView.setAdapter(serviceNearbyCategoryAdapter);
                            serviceNearbyCategoryAdapter.notifyDataSetChanged();
                        }

                        JSONArray arraySecondCategoryList = objectData.getJSONArray("second_category_list");
                        for (int i = 0; i < arraySecondCategoryList.length(); i++) {
                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setCategoryID(arraySecondCategoryList.getJSONObject(i).getString("id"));
                            categoryModel.setCategoryImage(arraySecondCategoryList.getJSONObject(i).getString("image"));
                            categoryModel.setCategoryTitle(arraySecondCategoryList.getJSONObject(i).getString("category_title"));
                            categoryModel.setCategoryParentID(arraySecondCategoryList.getJSONObject(i).getString("parent_id"));
                            categoryModel.setCategoryParentCategoryTitle(arraySecondCategoryList.getJSONObject(i).getString("parent_category_title"));
                            categoryModel.setCategoryType(arraySecondCategoryList.getJSONObject(i).getString("category_type"));
                            categoryModel.setCategoryStatus(arraySecondCategoryList.getJSONObject(i).getString("status"));
                            secondHandServiceList.add(categoryModel);
                        }
                        if (secondHandServiceList.size() > 0) {
                            SecondHandServiceCategoryAdapter secondHandServiceCategoryAdapter = new SecondHandServiceCategoryAdapter(mContext, secondHandServiceList);
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
                            SecondHandServiceView.setLayoutManager(mLayoutManager);
                            SecondHandServiceView.setItemAnimator(new DefaultItemAnimator());
                            SecondHandServiceView.setAdapter(secondHandServiceCategoryAdapter);
                            secondHandServiceCategoryAdapter.notifyDataSetChanged();
                        }

                        JSONArray arrayStoreServiceList = objectData.getJSONArray("store_service_list");
                        for (int i = 0; i < arrayStoreServiceList.length(); i++) {
                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setCategoryID(arrayStoreServiceList.getJSONObject(i).getString("id"));
                            categoryModel.setCategoryImage(arrayStoreServiceList.getJSONObject(i).getString("image"));
                            categoryModel.setCategoryTitle(arrayStoreServiceList.getJSONObject(i).getString("category_title"));
                            categoryModel.setCategoryParentID(arrayStoreServiceList.getJSONObject(i).getString("parent_id"));
                            categoryModel.setCategoryParentCategoryTitle(arrayStoreServiceList.getJSONObject(i).getString("parent_category_title"));
                            categoryModel.setCategoryType(arrayStoreServiceList.getJSONObject(i).getString("category_type"));
                            categoryModel.setCategoryStatus(arrayStoreServiceList.getJSONObject(i).getString("status"));
                            storeServiceList.add(categoryModel);
                        }
                        if (storeServiceList.size() > 0) {
                            StoreServiceAdapter storeServiceAdapter = new StoreServiceAdapter(mContext, storeServiceList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                            StoreServiceView.setLayoutManager(mLayoutManager);
                            StoreServiceView.setItemAnimator(new DefaultItemAnimator());
                            StoreServiceView.setAdapter(storeServiceAdapter);
                            storeServiceAdapter.notifyDataSetChanged();
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
                Log.e("HEADER", "" + APIConstant.getInstance().MY_HIDDEN_SALES + params);
                return params;
            }

            // Raw data passing
            @Override
            public byte[] getBody() throws AuthFailureError {
                String params = "{\"category_type\":\"" + CategoryType + "\",\"parent_id\":\"" + ParentID + "\"}";
                Log.e("PARAMETER", "" + APIConstant.getInstance().FILTER + params);
                return params.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().FILTER);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    public static class StoreServiceAdapter extends RecyclerView.Adapter<StoreServiceAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CategoryModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgCategory;
            TextView txtCategoryTitle;

            MyViewHolder(View view) {
                super(view);

                imgCategory = view.findViewById(R.id.imgCategory);

                txtCategoryTitle = view.findViewById(R.id.txtCategoryTitle);
            }
        }

        public StoreServiceAdapter(Context mContext, ArrayList<CategoryModel> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_service_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final CategoryModel categoryModel = arrayList.get(position);

            String Image = categoryModel.getCategoryImage();
            if (Image.equals("") || Image.equals("null") || Image.equals(null) || Image == null) {

            } else {
                Glide.with(mContext).load(Image).into(holder.imgCategory);
            }

            holder.txtCategoryTitle.setText(categoryModel.getCategoryTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    if (categoryModel.getCategoryTitle().equals("Hiring helper")) {
                        intent = new Intent(mContext, HiringHelperActivity.class);
                        intent.putExtra("WhereFrom", "Service");
                    } else {
                        intent = new Intent(mContext, StoreAndServiceSearchActivity.class);
                        intent.putExtra("Keyword", categoryModel.getCategoryTitle());
                    }
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}