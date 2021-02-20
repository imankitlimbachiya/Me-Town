package com.app.metown.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.ItemModel;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SecondHandItemAdapter extends RecyclerView.Adapter<SecondHandItemAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ItemModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName;

        MyViewHolder(View view) {
            super(view);

            txtItemName = view.findViewById(R.id.txtItemName);
        }
    }

    public SecondHandItemAdapter(Context mContext, ArrayList<ItemModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_hand_item_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        ItemModel itemModel = arrayList.get(position);

       // holder.txtItemName.setText(itemModel.getItemName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
