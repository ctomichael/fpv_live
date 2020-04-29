package com.dji.findmydrone.ui.view.right.camera;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.dji.component.fpv.widget.preview.PreviewHelper;
import com.dji.component.fpv.widget.preview.VideoSurfaceView;
import dji.fieldAnnotation.EXClassNullAway;
import io.reactivex.disposables.CompositeDisposable;

@Keep
@EXClassNullAway
public class CameraLiveView extends RelativeLayout implements PreviewHelper.SizeProvider {
    public final String TAG;
    private CompositeDisposable mCompositeDisposable;
    private PreviewHelper mPreviewHelper;
    private VideoSurfaceView mSurfaceView;

    public CameraLiveView(Context context) {
        super(context);
        this.TAG = "CameraLiveView";
    }

    public CameraLiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TAG = "CameraLiveView";
        this.mCompositeDisposable = new CompositeDisposable();
        this.mSurfaceView = new VideoSurfaceView(getContext());
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mPreviewHelper = new PreviewHelper(getContext(), this.mSurfaceView, this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(1, 1);
            lp.addRule(13);
            addView(this.mSurfaceView, lp);
            this.mSurfaceView.setRenderer();
            this.mCompositeDisposable.add(this.mPreviewHelper.liveviewSizeObservable().subscribe(new CameraLiveView$$Lambda$0(this)));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$0$CameraLiveView(Rect rect) throws Exception {
        ViewGroup.LayoutParams lp2 = this.mSurfaceView.getLayoutParams();
        lp2.width = rect.width();
        lp2.height = rect.height();
        this.mSurfaceView.setLayoutParams(lp2);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeView(this.mSurfaceView);
        this.mCompositeDisposable.clear();
        this.mPreviewHelper.onDetachedFromWindow();
        this.mPreviewHelper.stopDecoder();
        this.mPreviewHelper = null;
    }

    public void reset() {
        removeView(this.mSurfaceView);
        this.mCompositeDisposable.clear();
        this.mPreviewHelper.onDetachedFromWindow();
        this.mPreviewHelper.stopDecoder();
        this.mPreviewHelper = null;
        this.mPreviewHelper = new PreviewHelper(getContext(), this.mSurfaceView, this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(1, 1);
        lp.addRule(13);
        addView(this.mSurfaceView, lp);
        this.mSurfaceView.setRenderer();
        this.mCompositeDisposable.add(this.mPreviewHelper.liveviewSizeObservable().subscribe(new CameraLiveView$$Lambda$1(this)));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$reset$1$CameraLiveView(Rect rect) throws Exception {
        ViewGroup.LayoutParams lp2 = this.mSurfaceView.getLayoutParams();
        lp2.width = rect.width();
        lp2.height = rect.height();
        this.mSurfaceView.setLayoutParams(lp2);
    }

    public int getShowWidth() {
        if (this.mSurfaceView == null || this.mSurfaceView.getParent() == null) {
            return 0;
        }
        return ((View) this.mSurfaceView.getParent()).getWidth();
    }

    public int getShowHeight() {
        if (this.mSurfaceView == null || this.mSurfaceView.getParent() == null) {
            return 0;
        }
        return ((View) this.mSurfaceView.getParent()).getHeight();
    }
}
