package com.app.metown.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.R;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Bitmap> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;

        MyViewHolder(View view) {
            super(view);

            imgPhoto = view.findViewById(R.id.imgPhoto);
        }
    }

    public PhotoAdapter(Context mContext, ArrayList<Bitmap> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        final Bitmap bitmap = arrayList.get(position);

        Glide.with(mContext).load(bitmap).into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}