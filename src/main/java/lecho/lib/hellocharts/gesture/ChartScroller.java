package lecho.lib.hellocharts.gesture;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.widget.ScrollerCompat;
import lecho.lib.hellocharts.computator.ChartComputator;
import lecho.lib.hellocharts.model.Viewport;

public class ChartScroller {
    private ScrollerCompat scroller;
    private Viewport scrollerStartViewport = new Viewport();
    private Point surfaceSizeBuffer = new Point();

    public static class ScrollResult {
        public boolean canScrollX;
        public boolean canScrollY;
    }

    public ChartScroller(Context context) {
        this.scroller = ScrollerCompat.create(context);
    }

    public boolean startScroll(ChartComputator computator) {
        this.scroller.abortAnimation();
        this.scrollerStartViewport.set(computator.getCurrentViewport());
        return true;
    }

    public boolean scroll(ChartComputator computator, float distanceX, float distanceY, ScrollResult scrollResult) {
        Viewport maxViewport = computator.getMaximumViewport();
        Viewport visibleViewport = computator.getVisibleViewport();
        Viewport currentViewport = computator.getCurrentViewport();
        Rect contentRect = computator.getContentRectMinusAllMargins();
        boolean canScrollLeft = currentViewport.left > maxViewport.left;
        boolean canScrollRight = currentViewport.right < maxViewport.right;
        boolean canScrollTop = currentViewport.top < maxViewport.top;
        boolean canScrollBottom = currentViewport.bottom > maxViewport.bottom;
        boolean canScrollX = false;
        boolean canScrollY = false;
        if (canScrollLeft && distanceX <= 0.0f) {
            canScrollX = true;
        } else if (canScrollRight && distanceX >= 0.0f) {
            canScrollX = true;
        }
        if (canScrollTop && distanceY <= 0.0f) {
            canScrollY = true;
        } else if (canScrollBottom && distanceY >= 0.0f) {
            canScrollY = true;
        }
        if (canScrollX || canScrollY) {
            computator.computeScrollSurfaceSize(this.surfaceSizeBuffer);
            computator.setViewportTopLeft(currentViewport.left + ((visibleViewport.width() * distanceX) / ((float) contentRect.width())), currentViewport.top + (((-distanceY) * visibleViewport.height()) / ((float) contentRect.height())));
        }
        scrollResult.canScrollX = canScrollX;
        scrollResult.canScrollY = canScrollY;
        if (canScrollX || canScrollY) {
            return true;
        }
        return false;
    }

    public boolean computeScrollOffset(ChartComputator computator) {
        if (!this.scroller.computeScrollOffset()) {
            return false;
        }
        Viewport maxViewport = computator.getMaximumViewport();
        computator.computeScrollSurfaceSize(this.surfaceSizeBuffer);
        computator.setViewportTopLeft(maxViewport.left + ((maxViewport.width() * ((float) this.scroller.getCurrX())) / ((float) this.surfaceSizeBuffer.x)), maxViewport.top - ((maxViewport.height() * ((float) this.scroller.getCurrY())) / ((float) this.surfaceSizeBuffer.y)));
        return true;
    }

    public boolean fling(int velocityX, int velocityY, ChartComputator computator) {
        computator.computeScrollSurfaceSize(this.surfaceSizeBuffer);
        this.scrollerStartViewport.set(computator.getCurrentViewport());
        int startX = (int) ((((float) this.surfaceSizeBuffer.x) * (this.scrollerStartViewport.left - computator.getMaximumViewport().left)) / computator.getMaximumViewport().width());
        int startY = (int) ((((float) this.surfaceSizeBuffer.y) * (computator.getMaximumViewport().top - this.scrollerStartViewport.top)) / computator.getMaximumViewport().height());
        this.scroller.abortAnimation();
        int width = computator.getContentRectMinusAllMargins().width();
        int height = computator.getContentRectMinusAllMargins().height();
        this.scroller.fling(startX, startY, velocityX, velocityY, 0, (this.surfaceSizeBuffer.x - width) + 1, 0, (this.surfaceSizeBuffer.y - height) + 1);
        return true;
    }
}
