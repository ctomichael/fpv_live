package in.srain.cube.views.loadmore;

import android.view.View;
import android.widget.AbsListView;

public interface LoadMoreContainer {
    void loadMoreError(int i, String str);

    void loadMoreFinish(boolean z, boolean z2);

    void setAutoLoadMore(boolean z);

    void setLoadMoreHandler(LoadMoreHandler loadMoreHandler);

    void setLoadMoreUIHandler(LoadMoreUIHandler loadMoreUIHandler);

    void setLoadMoreView(View view);

    void setOnScrollListener(AbsListView.OnScrollListener onScrollListener);

    void setShowLoadingForFirstPage(boolean z);
}
