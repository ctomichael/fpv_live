package dji.pilot2.widget.ptr;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import dji.frame.widget.R;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;

public class DJIPullToRefreshView extends PtrFrameLayout {
    private final PtrUIHandler mPtrHeaderUIHandler = new PtrUIHandler.PtrDefaultUIHandler() {
        /* class dji.pilot2.widget.ptr.DJIPullToRefreshView.AnonymousClass1 */

        public void onUIReset(PtrFrameLayout frame) {
            if (frame.getHeaderView() != null) {
                ((AnimationDrawable) ((ImageView) frame.getHeaderView()).getDrawable()).stop();
            }
        }

        public void onUIRefreshPrepare(PtrFrameLayout frame) {
            if (frame.getHeaderView() != null) {
                ((AnimationDrawable) ((ImageView) frame.getHeaderView()).getDrawable()).start();
            }
        }
    };

    public DJIPullToRefreshView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public DJIPullToRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DJIPullToRefreshView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        disableWhenHorizontalMove(true);
        ImageView loadingIv = new ImageView(context);
        loadingIv.setLayoutParams(new ViewGroup.LayoutParams(-1, context.getResources().getDimensionPixelSize(R.dimen.d_64_dp)));
        loadingIv.setImageResource(R.drawable.v2_coupon_loading);
        loadingIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setHeaderView(loadingIv);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getHeaderView() != null) {
            addPtrUIHandler(this.mPtrHeaderUIHandler);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (getHeaderView() != null) {
            removePtrUIHandler(this.mPtrHeaderUIHandler);
        }
    }
}
