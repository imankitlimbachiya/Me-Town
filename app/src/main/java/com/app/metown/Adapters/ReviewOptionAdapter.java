package com.app.metown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;
import com.app.metown.UI.ReceivedCouponActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewOptionAdapter extends RecyclerView.Adapter<ReviewOptionAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<StaticCategoryModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtOptionName;

        MyViewHolder(View view) {
            super(view);

            txtOptionName = view.findViewById(R.id.txtOptionName);
        }
    }

    public ReviewOptionAdapter(Context mContext, ArrayList<StaticCategoryModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_option_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        StaticCategoryModel staticCategoryModel = arrayList.get(position);

        holder.txtOptionName.setText(staticCategoryModel.getCategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ReceivedCouponActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
