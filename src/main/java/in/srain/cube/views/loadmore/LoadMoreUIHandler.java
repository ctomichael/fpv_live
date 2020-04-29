package in.srain.cube.views.loadmore;

public interface LoadMoreUIHandler {
    void onLoadError(LoadMoreContainer loadMoreContainer, int i, String str);

    void onLoadFinish(LoadMoreContainer loadMoreContainer, boolean z, boolean z2);

    void onLoading(LoadMoreContainer loadMoreContainer);

    void onWaitToLoadMore(LoadMoreContainer loadMoreContainer);
}
