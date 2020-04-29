package lecho.lib.hellocharts.gesture;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewParent;
import lecho.lib.hellocharts.computator.ChartComputator;
import lecho.lib.hellocharts.gesture.ChartScroller;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.renderer.ChartRenderer;
import lecho.lib.hellocharts.view.Chart;

public class ChartTouchHandler {
    protected Chart chart;
    protected ChartScroller chartScroller;
    protected ChartZoomer chartZoomer;
    protected ChartComputator computator;
    protected ContainerScrollType containerScrollType;
    protected GestureDetector gestureDetector;
    protected boolean isScrollEnabled = true;
    protected boolean isValueSelectionEnabled = false;
    protected boolean isValueTouchEnabled = true;
    protected boolean isZoomEnabled = true;
    protected SelectedValue oldSelectedValue = new SelectedValue();
    protected ChartRenderer renderer;
    protected ScaleGestureDetector scaleGestureDetector;
    protected SelectedValue selectedValue = new SelectedValue();
    protected SelectedValue selectionModeOldValue = new SelectedValue();
    protected ViewParent viewParent;

    public ChartTouchHandler(Context context, Chart chart2) {
        this.chart = chart2;
        this.computator = chart2.getChartComputator();
        this.renderer = chart2.getChartRenderer();
        this.gestureDetector = new GestureDetector(context, new ChartGestureListener());
        this.scaleGestureDetector = new ScaleGestureDetector(context, new ChartScaleGestureListener());
        this.chartScroller = new ChartScroller(context);
        this.chartZoomer = new ChartZoomer(context, ZoomType.HORIZONTAL_AND_VERTICAL);
    }

    public void resetTouchHandler() {
        this.computator = this.chart.getChartComputator();
        this.renderer = this.chart.getChartRenderer();
    }

    public boolean computeScroll() {
        boolean needInvalidate = false;
        if (this.isScrollEnabled && this.chartScroller.computeScrollOffset(this.computator)) {
            needInvalidate = true;
        }
        if (!this.isZoomEnabled || !this.chartZoomer.computeZoom(this.computator)) {
            return needInvalidate;
        }
        return true;
    }

    public boolean handleTouchEvent(MotionEvent event) {
        boolean needInvalidate = this.scaleGestureDetector.onTouchEvent(event) || this.gestureDetector.onTouchEvent(event);
        if (this.isZoomEnabled && this.scaleGestureDetector.isInProgress()) {
            disallowParentInterceptTouchEvent();
        }
        if (!this.isValueTouchEnabled) {
            return needInvalidate;
        }
        if (computeTouch(event) || needInvalidate) {
            return true;
        }
        return false;
    }

    public boolean handleTouchEvent(MotionEvent event, ViewParent viewParent2, ContainerScrollType containerScrollType2) {
        this.viewParent = viewParent2;
        this.containerScrollType = containerScrollType2;
        return handleTouchEvent(event);
    }

    /* access modifiers changed from: private */
    public void disallowParentInterceptTouchEvent() {
        if (this.viewParent != null) {
            this.viewParent.requestDisallowInterceptTouchEvent(true);
        }
    }

    /* access modifiers changed from: private */
    public void allowParentInterceptTouchEvent(ChartScroller.ScrollResult scrollResult) {
        if (this.viewParent == null) {
            return;
        }
        if (ContainerScrollType.HORIZONTAL == this.containerScrollType && !scrollResult.canScrollX && !this.scaleGestureDetector.isInProgress()) {
            this.viewParent.requestDisallowInterceptTouchEvent(false);
        } else if (ContainerScrollType.VERTICAL == this.containerScrollType && !scrollResult.canScrollY && !this.scaleGestureDetector.isInProgress()) {
            this.viewParent.requestDisallowInterceptTouchEvent(false);
        }
    }

    private boolean computeTouch(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                boolean wasTouched = this.renderer.isTouched();
                if (wasTouched == checkTouch(event.getX(), event.getY())) {
                    return false;
                }
                if (!this.isValueSelectionEnabled) {
                    return true;
                }
                this.selectionModeOldValue.clear();
                if (!wasTouched || this.renderer.isTouched()) {
                    return true;
                }
                this.chart.callTouchListener();
                return true;
            case 1:
                if (!this.renderer.isTouched()) {
                    return false;
                }
                if (!checkTouch(event.getX(), event.getY())) {
                    this.renderer.clearTouch();
                } else if (!this.isValueSelectionEnabled) {
                    this.chart.callTouchListener();
                    this.renderer.clearTouch();
                } else if (!this.selectionModeOldValue.equals(this.selectedValue)) {
                    this.selectionModeOldValue.set(this.selectedValue);
                    this.chart.callTouchListener();
                }
                return true;
            case 2:
                if (!this.renderer.isTouched() || checkTouch(event.getX(), event.getY())) {
                    return false;
                }
                this.renderer.clearTouch();
                return true;
            case 3:
                if (!this.renderer.isTouched()) {
                    return false;
                }
                this.renderer.clearTouch();
                return true;
            default:
                return false;
        }
    }

    private boolean checkTouch(float touchX, float touchY) {
        this.oldSelectedValue.set(this.selectedValue);
        this.selectedValue.clear();
        if (this.renderer.checkTouch(touchX, touchY)) {
            this.selectedValue.set(this.renderer.getSelectedValue());
        }
        if (!this.oldSelectedValue.isSet() || !this.selectedValue.isSet() || this.oldSelectedValue.equals(this.selectedValue)) {
            return this.renderer.isTouched();
        }
        return false;
    }

    public boolean isZoomEnabled() {
        return this.isZoomEnabled;
    }

    public void setZoomEnabled(boolean isZoomEnabled2) {
        this.isZoomEnabled = isZoomEnabled2;
    }

    public boolean isScrollEnabled() {
        return this.isScrollEnabled;
    }

    public void setScrollEnabled(boolean isScrollEnabled2) {
        this.isScrollEnabled = isScrollEnabled2;
    }

    public ZoomType getZoomType() {
        return this.chartZoomer.getZoomType();
    }

    public void setZoomType(ZoomType zoomType) {
        this.chartZoomer.setZoomType(zoomType);
    }

    public boolean isValueTouchEnabled() {
        return this.isValueTouchEnabled;
    }

    public void setValueTouchEnabled(boolean isValueTouchEnabled2) {
        this.isValueTouchEnabled = isValueTouchEnabled2;
    }

    public boolean isValueSelectionEnabled() {
        return this.isValueSelectionEnabled;
    }

    public void setValueSelectionEnabled(boolean isValueSelectionEnabled2) {
        this.isValueSelectionEnabled = isValueSelectionEnabled2;
    }

    protected class ChartScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        protected ChartScaleGestureListener() {
        }

        public boolean onScale(ScaleGestureDetector detector) {
            if (!ChartTouchHandler.this.isZoomEnabled) {
                return false;
            }
            float scale = 2.0f - detector.getScaleFactor();
            if (Float.isInfinite(scale)) {
                scale = 1.0f;
            }
            return ChartTouchHandler.this.chartZoomer.scale(ChartTouchHandler.this.computator, detector.getFocusX(), detector.getFocusY(), scale);
        }
    }

    protected class ChartGestureListener extends GestureDetector.SimpleOnGestureListener {
        protected ChartScroller.ScrollResult scrollResult = new ChartScroller.ScrollResult();

        protected ChartGestureListener() {
        }

        public boolean onDown(MotionEvent e) {
            if (!ChartTouchHandler.this.isScrollEnabled) {
                return false;
            }
            ChartTouchHandler.this.disallowParentInterceptTouchEvent();
            return ChartTouchHandler.this.chartScroller.startScroll(ChartTouchHandler.this.computator);
        }

        public boolean onDoubleTap(MotionEvent e) {
            if (ChartTouchHandler.this.isZoomEnabled) {
                return ChartTouchHandler.this.chartZoomer.startZoom(e, ChartTouchHandler.this.computator);
            }
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!ChartTouchHandler.this.isScrollEnabled) {
                return false;
            }
            boolean canScroll = ChartTouchHandler.this.chartScroller.scroll(ChartTouchHandler.this.computator, distanceX, distanceY, this.scrollResult);
            ChartTouchHandler.this.allowParentInterceptTouchEvent(this.scrollResult);
            return canScroll;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (ChartTouchHandler.this.isScrollEnabled) {
                return ChartTouchHandler.this.chartScroller.fling((int) (-velocityX), (int) (-velocityY), ChartTouchHandler.this.computator);
            }
            return false;
        }
    }
}
