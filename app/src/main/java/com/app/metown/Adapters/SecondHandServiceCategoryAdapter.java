package com.app.metown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.CategoryModel;
import com.app.metown.R;
import com.app.metown.UI.HiringHelperActivity;
import com.app.metown.UI.StoreAndServiceSearchActivity;
import com.app.metown.UI.UserItemReferenceActivity;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SecondHandServiceCategoryAdapter extends RecyclerView.Adapter<SecondHandServiceCategoryAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CategoryModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory;
        TextView txtCategoryTitle;

        MyViewHolder(View view) {
            super(view);

            imgCategory = view.findViewById(R.id.imgCategory);

            txtCategoryTitle = view.findViewById(R.id.txtCategoryTitle);
        }
    }

    public SecondHandServiceCategoryAdapter(Context mContext, ArrayList<CategoryModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_hand_service_category_adapter, parent, false);
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
