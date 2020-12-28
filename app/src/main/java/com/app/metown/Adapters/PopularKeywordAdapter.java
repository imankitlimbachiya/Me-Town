package com.app.metown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;
import com.app.metown.UI.StoreAndServiceSearchActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PopularKeywordAdapter extends RecyclerView.Adapter<PopularKeywordAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CategoryMainModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        // RadioButton btnSelect;

        MyViewHolder(View view) {
            super(view);

            // btnSelect = view.findViewById(R.id.btnSelect);
        }
    }

    public PopularKeywordAdapter(Context mContext, ArrayList<CategoryMainModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_keyword_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        CategoryMainModel categoryMainModel = arrayList.get(position);

        // holder.btnSelect.setText("  " + categoryModel.getCategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent StoreAndServiceSearch = new Intent(mContext, StoreAndServiceSearchActivity.class);
                mContext.startActivity(StoreAndServiceSearch);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}