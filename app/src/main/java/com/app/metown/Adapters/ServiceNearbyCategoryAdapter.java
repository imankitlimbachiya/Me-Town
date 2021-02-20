package com.app.metown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.CategoryModel;
import com.app.metown.R;
import com.app.metown.UI.UserItemReferenceActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ServiceNearbyCategoryAdapter extends RecyclerView.Adapter<ServiceNearbyCategoryAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CategoryModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtServiceName;

        MyViewHolder(View view) {
            super(view);

            txtServiceName = view.findViewById(R.id.txtServiceName);
        }
    }

    public ServiceNearbyCategoryAdapter(Context mContext, ArrayList<CategoryModel> arrayList) {
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
        CategoryModel categoryModel = arrayList.get(position);

        holder.txtServiceName.setText(categoryModel.getCategoryName());

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
