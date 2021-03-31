package com.app.metown.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.CategoryModel;
import com.app.metown.Models.StaticCategoryModel;
import com.app.metown.R;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CategorySearchAdapter extends RecyclerView.Adapter<CategorySearchAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CategoryModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory;
        TextView txtCategoryName;

        MyViewHolder(View view) {
            super(view);

            imgCategory = view.findViewById(R.id.imgCategory);

            txtCategoryName = view.findViewById(R.id.txtCategoryName);
        }
    }

    public CategorySearchAdapter(Context mContext, ArrayList<CategoryModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_search_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        CategoryModel categoryModel = arrayList.get(position);

        String CategoryImage = categoryModel.getCategoryImage();
        if (CategoryImage.equals("") || CategoryImage.equals(null)) {

        } else {
            Glide.with(mContext).load(CategoryImage).into(holder.imgCategory);
        }
        holder.txtCategoryName.setText(categoryModel.getCategoryTitle());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}