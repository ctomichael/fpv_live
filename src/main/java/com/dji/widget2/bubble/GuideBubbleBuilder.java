package com.dji.widget2.bubble;

import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.dji.widget2.bubble.GuideBubbleViewKt;

public class GuideBubbleBuilder {
    public static final int BOTTOM = 2;
    public static final int CENTER = 1;
    public static final int FLAG_INTERCEPT_ALL_EVENT = 1;
    public static final int FLAG_TARGET_VIEW_CLICKABLE = 0;
    public static final int LEFT = 0;
    public static final int RIGHT = 2;
    public static final int TOP = 0;
    private int align = 1;
    private ViewGroup anchor;
    private int arrowOffset = 0;
    private int flag = 0;
    @GuideBubbleViewKt.Gravity
    protected long gravity = 4;
    private int marginHorizontal;
    private int marginVertical;
    private boolean needShow = true;
    private Runnable onEndAction = GuideBubbleBuilder$$Lambda$1.$instance;
    private Runnable onShowAction = GuideBubbleBuilder$$Lambda$0.$instance;
    private View targetView;
    private String text = "";

    static final /* synthetic */ void lambda$new$0$GuideBubbleBuilder() {
    }

    static final /* synthetic */ void lambda$new$1$GuideBubbleBuilder() {
    }

    private GuideBubbleBuilder(ViewGroup anchor2, View targetView2) {
        this.targetView = targetView2;
        this.anchor = anchor2;
    }

    public static GuideBubbleBuilder bind(ViewGroup anchor2, View target) {
        return new GuideBubbleBuilder(anchor2, target);
    }

    public GuideBubbleBuilder gravity(@GuideBubbleViewKt.Gravity long gravity2) {
        this.gravity = gravity2;
        return this;
    }

    public GuideBubbleBuilder arrowOffset(int arrowOffset2) {
        this.arrowOffset = arrowOffset2;
        return this;
    }

    public GuideBubbleBuilder marginHorizontal(int marginHorizontal2) {
        this.marginHorizontal = marginHorizontal2;
        return this;
    }

    public GuideBubbleBuilder marginVertical(int marginVertical2) {
        this.marginVertical = marginVertical2;
        return this;
    }

    public GuideBubbleBuilder alignToTarget(int align2) {
        this.align = align2;
        return this;
    }

    public GuideBubbleBuilder text(String text2) {
        this.text = text2;
        return this;
    }

    public GuideBubbleBuilder flag(int flag2) {
        this.flag = flag2;
        return this;
    }

    public GuideBubbleBuilder onShowAction(Runnable onShowAction2) {
        this.onShowAction = onShowAction2;
        return this;
    }

    public GuideBubbleBuilder onEndAction(Runnable onEndAction2) {
        this.onEndAction = onEndAction2;
        return this;
    }

    public GuideBubbleBuilder needShow(boolean needShow2) {
        this.needShow = needShow2;
        return this;
    }

    public void born() {
        if (!this.needShow) {
            this.onEndAction.run();
        } else {
            this.targetView.post(new GuideBubbleBuilder$$Lambda$2(this));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$born$5$GuideBubbleBuilder() {
        MaskView maskView = new MaskView(this.anchor.getContext(), calculate(), this.flag == 0);
        this.anchor.addView(maskView, new ViewGroup.LayoutParams(-1, -1));
        View.OnLayoutChangeListener listener = new GuideBubbleBuilder$$Lambda$4(this, new GuideBubbleBuilder$$Lambda$3(this, maskView));
        this.targetView.addOnLayoutChangeListener(listener);
        maskView.setOnClickListener(new GuideBubbleBuilder$$Lambda$5(this, maskView, listener));
        this.onShowAction.run();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$null$2$GuideBubbleBuilder(MaskView maskView) {
        maskView.updateBubble(calculate());
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$null$3$GuideBubbleBuilder(Runnable runnable, View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        this.targetView.removeCallbacks(runnable);
        this.targetView.postDelayed(runnable, 100);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$null$4$GuideBubbleBuilder(MaskView maskView, View.OnLayoutChangeListener listener, View view) {
        this.onEndAction.run();
        this.anchor.removeView(maskView);
        this.targetView.removeOnLayoutChangeListener(listener);
    }

    public GuideBubbleInfo bornInfo() {
        return calculate();
    }

    private GuideBubbleInfo calculate() {
        GuideBubbleViewKt bubble = new GuideBubbleViewKt(this.anchor.getContext(), this.gravity);
        bubble.setText(this.text);
        int measuredWidth = bubble.getRealWidth();
        int measuredHeight = bubble.getRealHeight();
        RectF mTargetRect = ViewUtilKt.getLocationInView(this.anchor, this.targetView);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
        int multiple = this.align;
        if (multiple > 2) {
            multiple = 1;
        } else if (multiple < 0) {
            multiple = 1;
        }
        int delta = 0;
        if (this.gravity == 4) {
            delta = (int) ((mTargetRect.width() - ((float) measuredWidth)) / 2.0f);
            lp.topMargin = ((int) mTargetRect.bottom) + this.marginVertical;
            lp.leftMargin = ((int) (mTargetRect.left + ((float) (multiple * delta)))) + this.marginHorizontal;
        } else if (this.gravity == 3) {
            delta = (int) ((mTargetRect.width() - ((float) measuredWidth)) / 2.0f);
            lp.topMargin = (int) ((mTargetRect.top - ((float) measuredHeight)) + ((float) this.marginVertical));
            lp.leftMargin = (int) (mTargetRect.left + ((float) (multiple * delta)) + ((float) this.marginHorizontal));
        } else if (this.gravity == 1) {
            delta = ((int) (mTargetRect.height() - ((float) measuredHeight))) / 2;
            lp.topMargin = (int) (mTargetRect.top + ((float) (multiple * delta)) + ((float) this.marginVertical));
            lp.leftMargin = (int) ((mTargetRect.left - ((float) measuredWidth)) + ((float) this.marginHorizontal));
        } else if (this.gravity == 2) {
            delta = ((int) (mTargetRect.height() - ((float) measuredHeight))) / 2;
            lp.topMargin = (int) (mTargetRect.top + ((float) (multiple * delta)) + ((float) this.marginVertical));
            lp.leftMargin = ((int) mTargetRect.right) + this.marginHorizontal;
        }
        bubble.setArrowOffset(this.arrowOffset - ((multiple - 1) * delta));
        return new GuideBubbleInfo(bubble, lp, mTargetRect);
    }

    public static class GuideBubbleInfo {
        public final ViewGroup.LayoutParams mLayoutParams;
        public final RectF mPositionInAnchor;
        public final GuideBubbleViewKt mView;

        public GuideBubbleInfo(GuideBubbleViewKt view, ViewGroup.LayoutParams layoutParams, RectF positionInAnchor) {
            this.mView = view;
            this.mLayoutParams = layoutParams;
            this.mPositionInAnchor = positionInAnchor;
        }
    }
}
