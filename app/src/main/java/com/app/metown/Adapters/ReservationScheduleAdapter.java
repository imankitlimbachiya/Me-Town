package com.app.metown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.OrganiseMeetUpModel;
import com.app.metown.R;
import com.app.metown.UI.ReportActivity;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReservationScheduleAdapter extends RecyclerView.Adapter<ReservationScheduleAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<OrganiseMeetUpModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtDateAndTime, txtDay, txtAddress, txtDescription, txtDone;

        MyViewHolder(View view) {
            super(view);

            txtDateAndTime = view.findViewById(R.id.txtDateAndTime);
            txtDay = view.findViewById(R.id.txtDay);
            txtAddress = view.findViewById(R.id.txtAddress);
            txtDescription = view.findViewById(R.id.txtDescription);
            txtDone = view.findViewById(R.id.txtDone);
        }
    }

    public ReservationScheduleAdapter(Context mContext, ArrayList<OrganiseMeetUpModel> arrayList) {
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
        OrganiseMeetUpModel organiseMeetUpModel = arrayList.get(position);

        holder.txtDateAndTime.setText(ChangeDateFormat(organiseMeetUpModel.getAppointmentTime()));
        holder.txtDay.setText(GetDay(organiseMeetUpModel.getAppointmentTime()));
        holder.txtAddress.setText(organiseMeetUpModel.getMeetUpAddress());
        holder.txtDescription.setText(organiseMeetUpModel.getDetails());

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

    public String ChangeDateFormat(String DateTime) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "MMM. MM. hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(DateTime);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String GetDay(String DateTime) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "E";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(DateTime);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
