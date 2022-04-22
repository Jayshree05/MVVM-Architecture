package com.example.mvvmarchitechre.utility;

import android.os.Handler;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;



public class AdvanceLoadMoreHelper {
    private OnLoadMoreListener onLoadMoreListener;
    private int visibleThreshold = 1;
    private int pastVisibleItems;

    public int getVisibleThreshold() {
        return visibleThreshold;
    }

    public void setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public AdvanceLoadMoreHelper(RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        if (recyclerView != null) {
            setOnScrollListener(recyclerView);
        }
    }

    private void setOnScrollListener(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int lastVibleItem = gridLayoutManager.findLastVisibleItemPosition();
                    if (totalItemCount <= lastVibleItem + visibleThreshold) {
                        onLoadMoreListener.onLoadMore();
                    }
                }
            });
        } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (totalItemCount <= lastVibleItem + visibleThreshold) {
                        onLoadMoreListener.onLoadMore();
                    }
                }
            });
        }

        else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager gridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int[] firstVisibleItems = gridLayoutManager.findFirstVisibleItemPositions(null);
                    if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                        pastVisibleItems = firstVisibleItems[0];
                    }
                    if ((visibleItemCount + pastVisibleItems+visibleThreshold) >= totalItemCount) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onLoadMoreListener.onLoadMore();
                            }
                        }, 200);
                    }
                }
            });
        }

    }
}
