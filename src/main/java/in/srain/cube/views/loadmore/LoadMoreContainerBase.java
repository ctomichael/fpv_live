package in.srain.cube.views.loadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import dji.pilot2.widget.DJILoadMoreDefaultFooterView;

public abstract class LoadMoreContainerBase extends LinearLayout implements LoadMoreContainer {
    private AbsListView mAbsListView;
    private boolean mAutoLoadMore = true;
    private View mFooterView;
    private boolean mHasMore = false;
    private boolean mIsLoading;
    private boolean mListEmpty = true;
    private boolean mLoadError = false;
    private LoadMoreHandler mLoadMoreHandler;
    private LoadMoreUIHandler mLoadMoreUIHandler;
    /* access modifiers changed from: private */
    public AbsListView.OnScrollListener mOnScrollListener;
    private boolean mShowLoadingForFirstPage = false;

    /* access modifiers changed from: protected */
    public abstract void addFooterView(View view);

    /* access modifiers changed from: protected */
    public abstract void removeFooterView(View view);

    /* access modifiers changed from: protected */
    public abstract AbsListView retrieveAbsListView();

    public LoadMoreContainerBase(Context context) {
        super(context);
    }

    public LoadMoreContainerBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mAbsListView = retrieveAbsListView();
        init();
    }

    @Deprecated
    public void useDefaultHeader() {
        useDefaultFooter();
    }

    public void useDefaultFooter() {
        DJILoadMoreDefaultFooterView footerView = new DJILoadMoreDefaultFooterView(getContext());
        footerView.setVisibility(8);
        setLoadMoreView(footerView);
        setLoadMoreUIHandler(footerView);
    }

    private void init() {
        if (this.mFooterView != null) {
            addFooterView(this.mFooterView);
        }
        this.mAbsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            /* class in.srain.cube.views.loadmore.LoadMoreContainerBase.AnonymousClass1 */
            private boolean mIsEnd = false;

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (LoadMoreContainerBase.this.mOnScrollListener != null) {
                    LoadMoreContainerBase.this.mOnScrollListener.onScrollStateChanged(view, scrollState);
                }
                if (scrollState == 0 && this.mIsEnd) {
                    LoadMoreContainerBase.this.onReachBottom();
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (LoadMoreContainerBase.this.mOnScrollListener != null) {
                    LoadMoreContainerBase.this.mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                if (firstVisibleItem + visibleItemCount >= totalItemCount - 1) {
                    this.mIsEnd = true;
                } else {
                    this.mIsEnd = false;
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void tryToPerformLoadMore() {
        if (!this.mIsLoading) {
            if (this.mHasMore || (this.mListEmpty && this.mShowLoadingForFirstPage)) {
                this.mIsLoading = true;
                if (this.mLoadMoreUIHandler != null) {
                    this.mLoadMoreUIHandler.onLoading(this);
                }
                if (this.mLoadMoreHandler != null) {
                    this.mLoadMoreHandler.onLoadMore(this);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onReachBottom() {
        if (!this.mLoadError) {
            if (this.mAutoLoadMore) {
                tryToPerformLoadMore();
            } else if (this.mHasMore) {
                this.mLoadMoreUIHandler.onWaitToLoadMore(this);
            }
        }
    }

    public void setShowLoadingForFirstPage(boolean showLoading) {
        this.mShowLoadingForFirstPage = showLoading;
    }

    public void setAutoLoadMore(boolean autoLoadMore) {
        this.mAutoLoadMore = autoLoadMore;
    }

    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        this.mOnScrollListener = l;
    }

    public void setLoadMoreView(View view) {
        if (this.mAbsListView == null) {
            this.mFooterView = view;
            return;
        }
        if (!(this.mFooterView == null || this.mFooterView == view)) {
            removeFooterView(view);
        }
        this.mFooterView = view;
        this.mFooterView.setOnClickListener(new View.OnClickListener() {
            /* class in.srain.cube.views.loadmore.LoadMoreContainerBase.AnonymousClass2 */

            public void onClick(View view) {
                LoadMoreContainerBase.this.tryToPerformLoadMore();
            }
        });
        addFooterView(view);
    }

    public void setLoadMoreUIHandler(LoadMoreUIHandler handler) {
        this.mLoadMoreUIHandler = handler;
    }

    public void setLoadMoreHandler(LoadMoreHandler handler) {
        this.mLoadMoreHandler = handler;
    }

    public void loadMoreFinish(boolean emptyResult, boolean hasMore) {
        this.mLoadError = false;
        this.mListEmpty = emptyResult;
        this.mIsLoading = false;
        this.mHasMore = hasMore;
        if (this.mLoadMoreUIHandler != null) {
            this.mLoadMoreUIHandler.onLoadFinish(this, emptyResult, hasMore);
        }
    }

    public void loadMoreError(int errorCode, String errorMessage) {
        this.mIsLoading = false;
        this.mLoadError = true;
        if (this.mLoadMoreUIHandler != null) {
            this.mLoadMoreUIHandler.onLoadError(this, errorCode, errorMessage);
        }
    }
}
