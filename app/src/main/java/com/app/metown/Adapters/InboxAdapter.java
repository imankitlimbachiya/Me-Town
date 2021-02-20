package com.app.metown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Models.ConversationModel;
import com.app.metown.R;
import com.app.metown.UI.ChatCommercialActivity;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ConversationModel> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgSender;
        TextView txtSenderName, txtLastMessage;

        MyViewHolder(View view) {
            super(view);

            imgSender = view.findViewById(R.id.imgSender);

            txtSenderName = view.findViewById(R.id.txtSenderName);
            txtLastMessage = view.findViewById(R.id.txtLastMessage);
        }
    }

    public InboxAdapter(Context mContext, ArrayList<ConversationModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        final ConversationModel conversationModel = arrayList.get(position);

        String SenderProfilePicture = conversationModel.getSenderProfilePicture();

        if (SenderProfilePicture.equals("") || SenderProfilePicture.equals("null") ||
                SenderProfilePicture.equals(null) || SenderProfilePicture == null) {

        } else {
            Glide.with(mContext).load(SenderProfilePicture).into(holder.imgSender);
        }

        holder.txtSenderName.setText(conversationModel.getSenderName());
        holder.txtLastMessage.setText(conversationModel.getLastMessageBody());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ChatCommercial = new Intent(mContext, ChatCommercialActivity.class);
                ChatCommercial.putExtra("ConversationID", conversationModel.getConversationID());
                ChatCommercial.putExtra("ToUserID", conversationModel.getSenderUserID());
                mContext.startActivity(ChatCommercial);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}