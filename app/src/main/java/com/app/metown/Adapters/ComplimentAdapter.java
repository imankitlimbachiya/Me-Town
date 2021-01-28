package com.app.metown.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ComplimentAdapter extends RecyclerView.Adapter<ComplimentAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<StaticCategoryModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        // RadioButton btnSelect;

        MyViewHolder(View view) {
            super(view);

            // btnSelect = view.findViewById(R.id.btnSelect);
        }
    }

    public ComplimentAdapter(Context mContext, ArrayList<StaticCategoryModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.compliment_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        StaticCategoryModel staticCategoryModel = arrayList.get(position);

        // holder.btnSelect.setText("  " + categoryModel.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
