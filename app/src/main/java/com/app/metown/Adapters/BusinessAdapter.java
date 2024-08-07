package com.app.metown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.PostHireHelperModel;
import com.app.metown.R;
import com.app.metown.UI.BusinessStoreActivity;
import com.app.metown.UI.CheckSiteMapActivity;
import com.app.metown.UI.ProfileOfStoreActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<PostHireHelperModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtBusinessName, txtSalary, txtWorkingTime, txtCheckTheSiteOnMap;

        MyViewHolder(View view) {
            super(view);

            txtBusinessName = view.findViewById(R.id.txtBusinessName);
            txtSalary = view.findViewById(R.id.txtSalary);
            txtWorkingTime = view.findViewById(R.id.txtWorkingTime);
            txtCheckTheSiteOnMap = view.findViewById(R.id.txtCheckTheSiteOnMap);
        }
    }

    public BusinessAdapter(Context mContext, ArrayList<PostHireHelperModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        final PostHireHelperModel postHireHelperModel = arrayList.get(position);

        holder.txtBusinessName.setText(postHireHelperModel.getName());
        holder.txtSalary.setText(postHireHelperModel.getSalary());
        holder.txtWorkingTime.setText(postHireHelperModel.getStartWorkingTime());

        holder.txtCheckTheSiteOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Report = new Intent(mContext, CheckSiteMapActivity.class);
                Report.putExtra("Latitude", postHireHelperModel.getLatitude());
                Report.putExtra("Longitude", postHireHelperModel.getLongitude());
                mContext.startActivity(Report);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent BusinessStore = new Intent(mContext, BusinessStoreActivity.class);
                BusinessStore.putExtra("UserID", postHireHelperModel.getUserID());
                BusinessStore.putExtra("HelperID", postHireHelperModel.getID());
                mContext.startActivity(BusinessStore);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}