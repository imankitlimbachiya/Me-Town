package com.app.metown.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.ItemMainModel;
import com.app.metown.Models.ItemMainModel1;
import com.app.metown.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SecondHandItemAdapter extends RecyclerView.Adapter<SecondHandItemAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ItemMainModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        // RadioButton btnSelect;
        TextView item_name;
        MyViewHolder(View view) {
            super(view);

            item_name = view.findViewById(R.id.item_name);
        }
    }

    public SecondHandItemAdapter(Context mContext, ArrayList<ItemMainModel> arrayList) {
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
        ItemMainModel itemMainModel = arrayList.get(position);

       holder.item_name.setText("  " + itemMainModel.getItemName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
