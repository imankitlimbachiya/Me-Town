package com.app.metown.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.metown.Adapters.InboxAdapter;
import com.app.metown.Models.CategoryMainModel;
import com.app.metown.R;

import java.util.ArrayList;

public class Inbox extends Fragment implements View.OnClickListener {

    public Inbox() {
        // Required empty public constructor
    }

    Context mContext;
    ProgressBar progressBar;
    RecyclerView InboxView;
    ArrayList<CategoryMainModel> inboxList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inbox, container, false);

        Log.e("Fragment","Inbox");

        mContext = getActivity();

        progressBar = view.findViewById(R.id.progressBar);

        InboxView = view.findViewById(R.id.InboxView);

        AddInboxItems();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.imgAlert:
                Intent notification = new Intent(mContext, NotificationActivity.class);
                startActivity(notification);
                break;*/
        }
    }

    public void AddInboxItems() {
        inboxList.clear();
        for (int i = 1; i <= 12; i++) {
            CategoryMainModel categoryMainModel = new CategoryMainModel(String.valueOf(i), "Item name");
            inboxList.add(categoryMainModel);
        }

        if (inboxList.size() > 0) {
            InboxAdapter inboxAdapter = new InboxAdapter(mContext, inboxList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            InboxView.setLayoutManager(mLayoutManager);
            InboxView.setItemAnimator(new DefaultItemAnimator());
            InboxView.setAdapter(inboxAdapter);
            inboxAdapter.notifyDataSetChanged();
        }
    }
}
