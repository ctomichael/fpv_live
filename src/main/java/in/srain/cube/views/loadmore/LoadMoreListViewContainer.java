package in.srain.cube.views.loadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class LoadMoreListViewContainer extends LoadMoreContainerBase {
    private ListView mListView;

    public LoadMoreListViewContainer(Context context) {
        super(context);
    }

    public LoadMoreListViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void addFooterView(View view) {
        this.mListView.addFooterView(view);
    }

    /* access modifiers changed from: protected */
    public void removeFooterView(View view) {
        this.mListView.removeFooterView(view);
    }

    /* access modifiers changed from: protected */
    public AbsListView retrieveAbsListView() {
        this.mListView = (ListView) getChildAt(0);
        return this.mListView;
    }
}
