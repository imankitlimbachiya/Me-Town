package com.app.metown.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.ItemMainModel;
import com.app.metown.Models.ItemModel;
import com.app.metown.Models.SecondHandSearchItemMainModel;
import com.app.metown.R;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SecondHandSearchItemAdapter extends RecyclerView.Adapter<SecondHandSearchItemAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ItemModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgItem;
        TextView txtItemTitle, txtItemPrice;

        MyViewHolder(View view) {
            super(view);

            imgItem = view.findViewById(R.id.imgItem);

            txtItemTitle = view.findViewById(R.id.txtItemTitle);
            txtItemPrice = view.findViewById(R.id.txtItemPrice);
        }
    }

    public SecondHandSearchItemAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_hand_search_item_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        ItemModel itemModel = arrayList.get(position);

        String Images = itemModel.getItemImages();
        String[] separated = Images.split(",");
        Glide.with(mContext).load(separated[0]).into(holder.imgItem);

        holder.txtItemTitle.setText(itemModel.getItemName());
        holder.txtItemPrice.setText(itemModel.getItemPrice());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
