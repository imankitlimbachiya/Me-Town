package com.app.metown.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CategorySearchAdapter extends RecyclerView.Adapter<CategorySearchAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CategoryMainModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtCategoryName;
        LinearLayout MainLayout;

        MyViewHolder(View view) {
            super(view);

            txtCategoryName = view.findViewById(R.id.txtCategoryName);

            MainLayout = view.findViewById(R.id.MainLayout);
        }
    }

    public CategorySearchAdapter(Context mContext, ArrayList<CategoryMainModel> arrayList) {
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
        CategoryMainModel categoryMainModel = arrayList.get(position);

        holder.txtCategoryName.setText(categoryMainModel.getCategoryName());

        /*int i = arrayList.size() - 1;

        if (position == i) {
            setMargins(holder.MainLayout, 0, 30, 25, 0);
        }*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
