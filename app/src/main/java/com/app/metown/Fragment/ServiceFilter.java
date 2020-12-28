package com.app.metown.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Adapters.SecondHandServiceCategoryAdapter;
import com.app.metown.Adapters.ServiceNearbyCategoryAdapter;
import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;
import com.app.metown.UI.HiringHelperActivity;
import com.app.metown.UI.NotificationActivity;
import com.app.metown.UI.StoreAndServiceNearYouActivity;
import com.app.metown.UI.UserItemReferenceActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ServiceFilter extends Fragment implements View.OnClickListener {

    public ServiceFilter() {
        // Required empty public constructor
    }

    Context mContext;
    ProgressBar progressBar;
    ImageView imgAlert;
    TextView txtFindMoreService;
    RecyclerView MyTownServiceView, SecondHandServiceView, StoreServiceView;
    ArrayList<CategoryMainModel> myTownServiceList = new ArrayList<>();
    ArrayList<CategoryMainModel> secondHandServiceList = new ArrayList<>();
    ArrayList<CategoryMainModel> storeServiceList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_filter, container, false);

        Log.e("Fragment", "ServiceFilter");

        mContext = getActivity();

        progressBar = view.findViewById(R.id.progressBar);

        txtFindMoreService = view.findViewById(R.id.txtFindMoreService);

        imgAlert = view.findViewById(R.id.imgAlert);

        imgAlert.setOnClickListener(this);
        txtFindMoreService.setOnClickListener(this);

        MyTownServiceView = view.findViewById(R.id.MyTownServiceView);
        SecondHandServiceView = view.findViewById(R.id.SecondHandServiceView);
        StoreServiceView = view.findViewById(R.id.StoreServiceView);

        AddMyTownServiceItems();

        AddSecondHandServiceItems();

        AddStoreServiceItems();

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgAlert:
                Intent notification = new Intent(mContext, NotificationActivity.class);
                startActivity(notification);
                break;
            case R.id.txtFindMoreService:
                Intent StoreAndServiceNearYou = new Intent(mContext, StoreAndServiceNearYouActivity.class);
                startActivity(StoreAndServiceNearYou);
                break;
        }
    }

    public void AddMyTownServiceItems() {
        myTownServiceList.clear();
        for (int i = 1; i <= 12; i++) {
            CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(i), "Item name");
            myTownServiceList.add(categoryMainModel);
        }

        if (myTownServiceList.size() > 0) {
            ServiceNearbyCategoryAdapter serviceNearbyCategoryAdapter = new ServiceNearbyCategoryAdapter(mContext, myTownServiceList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false);
            MyTownServiceView.setLayoutManager(mLayoutManager);
            MyTownServiceView.setItemAnimator(new DefaultItemAnimator());
            MyTownServiceView.setAdapter(serviceNearbyCategoryAdapter);
            serviceNearbyCategoryAdapter.notifyDataSetChanged();
        }
    }

    public void AddSecondHandServiceItems() {
        secondHandServiceList.clear();
        CategoryMainModel categoryMainModel;

        categoryMainModel = new CategoryMainModel("1", "Popular");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("2", "Digital/Gadget");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("3", "Furniture");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("4", "Baby&Kids");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("5", "Living");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("6", "Sports");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("7", "Women's");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("8", "Men's");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("9", "Game&Hobby");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("10", "Beauty");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("11", "Pets");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("12", "Books");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("13", "Etc.");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("14", "Request to buy");
        secondHandServiceList.add(categoryMainModel);

        if (secondHandServiceList.size() > 0) {
            SecondHandServiceCategoryAdapter secondHandServiceCategoryAdapter = new SecondHandServiceCategoryAdapter(mContext, secondHandServiceList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
            SecondHandServiceView.setLayoutManager(mLayoutManager);
            SecondHandServiceView.setItemAnimator(new DefaultItemAnimator());
            SecondHandServiceView.setAdapter(secondHandServiceCategoryAdapter);
            secondHandServiceCategoryAdapter.notifyDataSetChanged();
        }
    }

    public void AddStoreServiceItems() {
        storeServiceList.clear();
        CategoryMainModel categoryMainModel;

        categoryMainModel = new CategoryMainModel("1", "All");
        storeServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("2", "Car & Motor");
        storeServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("3", "Groceries");
        storeServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("4", "Water Dispenser");
        secondHandServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("5", "Laundry, Cleaning, Pest control");
        storeServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("6", "Furniture, Kitchen");
        storeServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("7", "Exhibit, Event, Concert");
        storeServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("8", "Hiring helper");
        storeServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("9", "Real estate");
        storeServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("10", "Restaurant");
        storeServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("11", "Hardware, key, Plumbing");
        storeServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("12", "Gadget, Computer");
        storeServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("13", "Massage");
        storeServiceList.add(categoryMainModel);

        categoryMainModel = new CategoryMainModel("14", "Etc.");
        storeServiceList.add(categoryMainModel);

        if (storeServiceList.size() > 0) {
            StoreServiceAdapter storeServiceAdapter = new StoreServiceAdapter(mContext, storeServiceList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            StoreServiceView.setLayoutManager(mLayoutManager);
            StoreServiceView.setItemAnimator(new DefaultItemAnimator());
            StoreServiceView.setAdapter(storeServiceAdapter);
            storeServiceAdapter.notifyDataSetChanged();
        }
    }

    public static class StoreServiceAdapter extends RecyclerView.Adapter<StoreServiceAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CategoryMainModel> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtStoreService;

            MyViewHolder(View view) {
                super(view);

                txtStoreService = view.findViewById(R.id.txtStoreService);
            }
        }

        public StoreServiceAdapter(Context mContext, ArrayList<CategoryMainModel> arrayList) {
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
            final CategoryMainModel categoryMainModel = arrayList.get(position);

            holder.txtStoreService.setText(categoryMainModel.getCategoryName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (categoryMainModel.getCategoryName().equals("Hiring helper")) {
                        Intent intent = new Intent(mContext, HiringHelperActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, UserItemReferenceActivity.class);
                        mContext.startActivity(intent);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}
