package dji.pilot2.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import dji.frame.widget.R;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreUIHandler;

public class DJILoadMoreDefaultFooterView extends RelativeLayout implements LoadMoreUIHandler {
    private AnimationDrawable mAnimationDrawable;
    private ImageView mImageView;

    public DJILoadMoreDefaultFooterView(Context context) {
        this(context, null);
    }

    public DJILoadMoreDefaultFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DJILoadMoreDefaultFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.load_more_default_footer, this);
        this.mImageView = (ImageView) findViewById(R.id.v2_coupon_point_loading);
        this.mAnimationDrawable = (AnimationDrawable) this.mImageView.getDrawable();
    }

    public void onLoading(LoadMoreContainer container) {
        setVisibility(0);
        this.mAnimationDrawable.start();
    }

    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {
        setVisibility(4);
    }

    public void onWaitToLoadMore(LoadMoreContainer container) {
        setVisibility(0);
    }

    public void onLoadError(LoadMoreContainer container, int errorCode, String errorMessage) {
        setVisibility(4);
    }
}
