package lecho.lib.hellocharts.gesture;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import lecho.lib.hellocharts.computator.ChartComputator;
import lecho.lib.hellocharts.model.Viewport;

public class ChartZoomer {
    public static final float ZOOM_AMOUNT = 0.25f;
    private Viewport scrollerStartViewport = new Viewport();
    private PointF viewportFocus = new PointF();
    private PointF zoomFocalPoint = new PointF();
    private ZoomType zoomType;
    private ZoomerCompat zoomer;

    public ChartZoomer(Context context, ZoomType zoomType2) {
        this.zoomer = new ZoomerCompat(context);
        this.zoomType = zoomType2;
    }

    public boolean startZoom(MotionEvent e, ChartComputator computator) {
        this.zoomer.forceFinished(true);
        this.scrollerStartViewport.set(computator.getCurrentViewport());
        if (!computator.rawPixelsToDataPoint(e.getX(), e.getY(), this.zoomFocalPoint)) {
            return false;
        }
        this.zoomer.startZoom(0.25f);
        return true;
    }

    public boolean computeZoom(ChartComputator computator) {
        if (!this.zoomer.computeZoom()) {
            return false;
        }
        float newWidth = (1.0f - this.zoomer.getCurrZoom()) * this.scrollerStartViewport.width();
        float newHeight = (1.0f - this.zoomer.getCurrZoom()) * this.scrollerStartViewport.height();
        float pointWithinViewportX = (this.zoomFocalPoint.x - this.scrollerStartViewport.left) / this.scrollerStartViewport.width();
        float pointWithinViewportY = (this.zoomFocalPoint.y - this.scrollerStartViewport.bottom) / this.scrollerStartViewport.height();
        setCurrentViewport(computator, this.zoomFocalPoint.x - (newWidth * pointWithinViewportX), this.zoomFocalPoint.y + ((1.0f - pointWithinViewportY) * newHeight), this.zoomFocalPoint.x + ((1.0f - pointWithinViewportX) * newWidth), this.zoomFocalPoint.y - (newHeight * pointWithinViewportY));
        return true;
    }

    public boolean scale(ChartComputator computator, float focusX, float focusY, float scale) {
        float newWidth = scale * computator.getCurrentViewport().width();
        float newHeight = scale * computator.getCurrentViewport().height();
        if (!computator.rawPixelsToDataPoint(focusX, focusY, this.viewportFocus)) {
            return false;
        }
        float left = this.viewportFocus.x - ((focusX - ((float) computator.getContentRectMinusAllMargins().left)) * (newWidth / ((float) computator.getContentRectMinusAllMargins().width())));
        float top = this.viewportFocus.y + ((focusY - ((float) computator.getContentRectMinusAllMargins().top)) * (newHeight / ((float) computator.getContentRectMinusAllMargins().height())));
        setCurrentViewport(computator, left, top, left + newWidth, top - newHeight);
        return true;
    }

    private void setCurrentViewport(ChartComputator computator, float left, float top, float right, float bottom) {
        Viewport currentViewport = computator.getCurrentViewport();
        if (ZoomType.HORIZONTAL_AND_VERTICAL == this.zoomType) {
            computator.setCurrentViewport(left, top, right, bottom);
        } else if (ZoomType.HORIZONTAL == this.zoomType) {
            computator.setCurrentViewport(left, currentViewport.top, right, currentViewport.bottom);
        } else if (ZoomType.VERTICAL == this.zoomType) {
            computator.setCurrentViewport(currentViewport.left, top, currentViewport.right, bottom);
        }
    }

    public ZoomType getZoomType() {
        return this.zoomType;
    }

    public void setZoomType(ZoomType zoomType2) {
        this.zoomType = zoomType2;
    }
}
