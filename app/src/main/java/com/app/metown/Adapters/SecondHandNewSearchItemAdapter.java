package com.app.metown.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.ItemModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SecondHandNewSearchItemAdapter extends RecyclerView.Adapter<SecondHandNewSearchItemAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ItemModel> arrayList;
    String rupee;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgItem;
        TextView txtItemName, txtPrice;

        MyViewHolder(View view) {
            super(view);

            imgItem = view.findViewById(R.id.imgItem);

            txtItemName = view.findViewById(R.id.txtItemName);
            txtPrice = view.findViewById(R.id.txtPrice);
        }
    }

    public SecondHandNewSearchItemAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.rupee = mContext.getString(R.string.rupee);
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_hand_new_search_item_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        ItemModel itemModel = arrayList.get(position);

        holder.txtItemName.setText(itemModel.getItemName());
        holder.txtPrice.setText(rupee + itemModel.getItemPrice());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
