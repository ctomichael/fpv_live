package lecho.lib.hellocharts.gesture;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import lecho.lib.hellocharts.gesture.ChartTouchHandler;
import lecho.lib.hellocharts.view.Chart;

public class PreviewChartTouchHandler extends ChartTouchHandler {
    public PreviewChartTouchHandler(Context context, Chart chart) {
        super(context, chart);
        this.gestureDetector = new GestureDetector(context, new PreviewChartGestureListener());
        this.scaleGestureDetector = new ScaleGestureDetector(context, new ChartScaleGestureListener());
        this.isValueTouchEnabled = false;
        this.isValueSelectionEnabled = false;
    }

    protected class ChartScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        protected ChartScaleGestureListener() {
        }

        public boolean onScale(ScaleGestureDetector detector) {
            if (!PreviewChartTouchHandler.this.isZoomEnabled) {
                return false;
            }
            float scale = detector.getCurrentSpan() / detector.getPreviousSpan();
            if (Float.isInfinite(scale)) {
                scale = 1.0f;
            }
            return PreviewChartTouchHandler.this.chartZoomer.scale(PreviewChartTouchHandler.this.computator, detector.getFocusX(), detector.getFocusY(), scale);
        }
    }

    protected class PreviewChartGestureListener extends ChartTouchHandler.ChartGestureListener {
        protected PreviewChartGestureListener() {
            super();
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, -distanceX, -distanceY);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, -velocityX, -velocityY);
        }
    }
}
