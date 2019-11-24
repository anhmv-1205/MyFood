package com.sun_asterisk.myfood.base.recyclerview;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EndlessRecyclerOnScrollListener
        extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_THRESHOLD = 5;

    private int currentPage = 1;

    private RecyclerView.LayoutManager layoutManager;

    private boolean loading = true;

    private int previousTotalItemCount = 0;

    private int startingPageIndex = 1;

    protected EndlessRecyclerOnScrollListener(@Nullable RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public abstract void onLoadMore(int currentPage);

    @Override
    public void onScrolled(@NotNull RecyclerView view, int dx, int dy) {
        super.onScrolled(view, dx, dy);
        if (layoutManager == null) {
            return;
        }
        int totalItemCount = this.layoutManager.getItemCount();
        int lastVisibleItemPosition = this.getLastVisibleItemPosition();

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < this.previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (this.loading && (totalItemCount > this.previousTotalItemCount)) {
            this.loading = false;
            this.previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!this.loading && (lastVisibleItemPosition + VISIBLE_THRESHOLD)
                > totalItemCount) {
            this.currentPage++;
            onLoadMore(this.currentPage);
            this.loading = true;
        }
    }

    public void reset() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;

        if (layoutManager != null) {
            layoutManager.scrollToPosition(0);
        }
    }

    public void resetNoScroll() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
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

    private int getLastVisibleItemPosition() {
        int lastVisibleItemPosition = 0;
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions =
                    ((StaggeredGridLayoutManager) layoutManager)
                            .findLastVisibleItemPositions(
                                    null);
            lastVisibleItemPosition = getLastVisibleItem(
                    lastVisibleItemPositions);
        } else if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition =
                    ((GridLayoutManager) layoutManager)
                            .findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition =
                    ((LinearLayoutManager) layoutManager)
                            .findLastVisibleItemPosition();
        }
        return lastVisibleItemPosition;
    }
}
