package lecho.lib.hellocharts.gesture;

import android.content.Context;
import android.graphics.RectF;
import android.support.v4.widget.ScrollerCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartTouchHandler extends ChartTouchHandler {
    public static final int FLING_VELOCITY_DOWNSCALE = 4;
    /* access modifiers changed from: private */
    public boolean isRotationEnabled = true;
    protected PieChartView pieChart;
    protected ScrollerCompat scroller;

    public PieChartTouchHandler(Context context, PieChartView chart) {
        super(context, chart);
        this.pieChart = chart;
        this.scroller = ScrollerCompat.create(context);
        this.gestureDetector = new GestureDetector(context, new ChartGestureListener());
        this.scaleGestureDetector = new ScaleGestureDetector(context, new ChartScaleGestureListener());
        this.isZoomEnabled = false;
    }

    public boolean computeScroll() {
        if (this.isRotationEnabled && this.scroller.computeScrollOffset()) {
            this.pieChart.setChartRotation(this.scroller.getCurrY(), false);
        }
        return false;
    }

    public boolean handleTouchEvent(MotionEvent event) {
        boolean needInvalidate = super.handleTouchEvent(event);
        if (this.isRotationEnabled) {
            return this.gestureDetector.onTouchEvent(event) || needInvalidate;
        }
        return needInvalidate;
    }

    public boolean isRotationEnabled() {
        return this.isRotationEnabled;
    }

    public void setRotationEnabled(boolean isRotationEnabled2) {
        this.isRotationEnabled = isRotationEnabled2;
    }

    private class ChartScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ChartScaleGestureListener() {
        }

        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }
    }

    private class ChartGestureListener extends GestureDetector.SimpleOnGestureListener {
        private ChartGestureListener() {
        }

        public boolean onDown(MotionEvent e) {
            if (!PieChartTouchHandler.this.isRotationEnabled) {
                return false;
            }
            PieChartTouchHandler.this.scroller.abortAnimation();
            return true;
        }

        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!PieChartTouchHandler.this.isRotationEnabled) {
                return false;
            }
            RectF circleOval = PieChartTouchHandler.this.pieChart.getCircleOval();
            PieChartTouchHandler.this.pieChart.setChartRotation(PieChartTouchHandler.this.pieChart.getChartRotation() - (((int) vectorToScalarScroll(distanceX, distanceY, e2.getX() - circleOval.centerX(), e2.getY() - circleOval.centerY())) / 4), false);
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!PieChartTouchHandler.this.isRotationEnabled) {
                return false;
            }
            RectF circleOval = PieChartTouchHandler.this.pieChart.getCircleOval();
            float f = velocityX;
            float f2 = velocityY;
            float scrollTheta = vectorToScalarScroll(f, f2, e2.getX() - circleOval.centerX(), e2.getY() - circleOval.centerY());
            PieChartTouchHandler.this.scroller.abortAnimation();
            PieChartTouchHandler.this.scroller.fling(0, PieChartTouchHandler.this.pieChart.getChartRotation(), 0, ((int) scrollTheta) / 4, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            return true;
        }

        private float vectorToScalarScroll(float dx, float dy, float x, float y) {
            return ((float) Math.sqrt((double) ((dx * dx) + (dy * dy)))) * Math.signum(((-y) * dx) + (x * dy));
        }
    }
}
