package com.app.metown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.PostHireHelperModel;
import com.app.metown.Models.TownModel;
import com.app.metown.R;
import com.app.metown.UI.CheckSiteMapActivity;
import com.app.metown.UI.ProfileOfStoreActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OpenTownAdapter extends RecyclerView.Adapter<OpenTownAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<TownModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtOpenTownName;

        MyViewHolder(View view) {
            super(view);

            txtOpenTownName = view.findViewById(R.id.txtOpenTownName);
        }
    }

    public OpenTownAdapter(Context mContext, ArrayList<TownModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.open_town_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        TownModel townModel = arrayList.get(position);

        holder.txtOpenTownName.setText(townModel.getTownName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
