package com.app.metown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;
import com.app.metown.UI.ReportActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReservationScheduleAdapter extends RecyclerView.Adapter<ReservationScheduleAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CategoryMainModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtDone;

        MyViewHolder(View view) {
            super(view);

            txtDone = view.findViewById(R.id.txtDone);
        }
    }

    public ReservationScheduleAdapter(Context mContext, ArrayList<CategoryMainModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_schedule_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        CategoryMainModel categoryMainModel = arrayList.get(position);

        // holder.btnSelect.setText("  " + categoryModel.getCategoryName());

        holder.txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Report = new Intent(mContext, ReportActivity.class);
                mContext.startActivity(Report);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
