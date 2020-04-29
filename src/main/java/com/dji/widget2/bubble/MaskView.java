package com.dji.widget2.bubble;

import android.content.Context;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.dji.widget2.bubble.GuideBubbleBuilder;

public class MaskView extends FrameLayout {
    private GuideBubbleBuilder.GuideBubbleInfo mGuideBubbleInfo;
    private RectF mTargetRect;
    private boolean targetViewClickable;

    public MaskView(@NonNull Context context, GuideBubbleBuilder.GuideBubbleInfo bubbleInfo, boolean targetViewClickable2) {
        super(context);
        addView(bubbleInfo.mView, bubbleInfo.mLayoutParams);
        this.mTargetRect = bubbleInfo.mPositionInAnchor;
        this.targetViewClickable = targetViewClickable2;
        this.mGuideBubbleInfo = bubbleInfo;
    }

    public void updateBubble(GuideBubbleBuilder.GuideBubbleInfo bubbleInfo) {
        if (this.mGuideBubbleInfo != null) {
            removeView(this.mGuideBubbleInfo.mView);
        }
        this.mGuideBubbleInfo = bubbleInfo;
        addView(bubbleInfo.mView, bubbleInfo.mLayoutParams);
        this.mTargetRect = bubbleInfo.mPositionInAnchor;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.targetViewClickable || !isLocationAtTargetArea(event)) {
            return super.onTouchEvent(event);
        }
        performClick();
        return false;
    }

    private boolean isLocationAtTargetArea(MotionEvent ev) {
        return ev.getX() > this.mTargetRect.left && ev.getX() < this.mTargetRect.right && ev.getY() > this.mTargetRect.top && ev.getY() < this.mTargetRect.bottom;
    }
}
