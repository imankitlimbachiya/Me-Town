package com.app.metown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;
import com.app.metown.UI.UserItemReferenceActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ServiceNearbyCategoryAdapter extends RecyclerView.Adapter<ServiceNearbyCategoryAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CategoryMainModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView service_item_name;

        MyViewHolder(View view) {
            super(view);

            service_item_name = view.findViewById(R.id.service_item_name);
        }
    }

    public ServiceNearbyCategoryAdapter(Context mContext, ArrayList<CategoryMainModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_nearby_category_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        CategoryMainModel categoryMainModel = arrayList.get(position);

        holder.service_item_name.setText("  " + categoryMainModel.getCategoryName());
        Log.e("name",categoryMainModel.getCategoryName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserItemReferenceActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
