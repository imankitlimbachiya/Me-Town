package com.app.metown.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.CategoryModel;
import com.app.metown.Models.ItemModel;
import com.app.metown.Models.ManageUserModel;
import com.app.metown.R;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondHandSearchUserAdapter extends RecyclerView.Adapter<SecondHandSearchUserAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ManageUserModel> arrayList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgUser;
        TextView txtNickName;

        MyViewHolder(View view) {
            super(view);

            imgUser = view.findViewById(R.id.imgUser);

            txtNickName = view.findViewById(R.id.txtNickName);
        }
    }

    public SecondHandSearchUserAdapter(Context mContext, ArrayList<ManageUserModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_hand_new_search_user_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        final ManageUserModel manageUserModel = arrayList.get(position);

        String ProfilePicture = manageUserModel.getProfilePicture();
        if (ProfilePicture.equals("") || ProfilePicture.equals("null") || ProfilePicture.equals(null) || ProfilePicture == null) {

        } else {
            Glide.with(mContext).load(ProfilePicture).into(holder.imgUser);
        }

        holder.txtNickName.setText(manageUserModel.getNickName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
