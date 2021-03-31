package com.app.metown.AppConstants;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LoadMore {

    public static int getLastVisiblePosition(RecyclerView recyclerView) {
        if (recyclerView != null) {
            final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
        }
        return 0;
    }
}