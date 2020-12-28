package com.app.metown.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.CategoryModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CategoryModel> arrayList;
    // int pos = -1;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        RadioButton btnSelect;

        MyViewHolder(View view) {
            super(view);

            btnSelect = view.findViewById(R.id.btnSelect);
        }
    }

    public CategoryAdapter(Context mContext, ArrayList<CategoryModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, final int position) {
        CategoryModel categoryModel = arrayList.get(position);

        holder.btnSelect.setText("  " + categoryModel.getCategoryTitle());

        /*if (pos == position) {
            holder.btnSelect.setSelected(true);
        } else {
            holder.btnSelect.setSelected(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = position;
                notifyDataSetChanged();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
