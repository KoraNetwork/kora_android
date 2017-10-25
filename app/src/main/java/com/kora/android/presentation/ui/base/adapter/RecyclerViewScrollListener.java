package com.kora.android.presentation.ui.base.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 2;
    private int currentPage = 1;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 1;

    private RecyclerView.LayoutManager mLayoutManager;

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    private void initLayoutManager(final RecyclerView recyclerView) {
        if (mLayoutManager != null) return;
        mLayoutManager = recyclerView.getLayoutManager();

        if (mLayoutManager instanceof StaggeredGridLayoutManager)
            visibleThreshold = visibleThreshold * ((StaggeredGridLayoutManager) mLayoutManager).getSpanCount();
        if (mLayoutManager instanceof GridLayoutManager)
            visibleThreshold = visibleThreshold * ((GridLayoutManager) mLayoutManager).getSpanCount();
    }

    public void resetParams() {
        visibleThreshold = 2;
        currentPage = 1;
        previousTotalItemCount = 0;
        loading = true;
        startingPageIndex = 1;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        //avoid loading more items when vertical scroll was not detected
        if (dy == 0) return;

        initLayoutManager(recyclerView);

        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }
//
//        if (recyclerView.getAdapter() instanceof JobsAdapter) {
//            JobsAdapter adapter = (JobsAdapter) recyclerView.getAdapter();
//            if (adapter.getItem(totalItemCount - 1) == null) {
//                --totalItemCount;
//                --visibleThreshold;
//            }
//        }

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
//            if (recyclerView.getAdapter() instanceof JobsAdapter) {
//                ((JobsAdapter) recyclerView.getAdapter()).add(null);
//            }

//            onLoadMore(currentPage, totalItemCount);
            onLoadMore(totalItemCount);
            loading = true;
        }

    }

//    public abstract void onLoadMore(int page, int totalItemsCount);
    public abstract void onLoadMore(int totalItemCount);
}