package lecho.lib.hellocharts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.animation.ChartDataAnimator;
import lecho.lib.hellocharts.animation.ChartDataAnimatorV14;
import lecho.lib.hellocharts.animation.ChartDataAnimatorV8;
import lecho.lib.hellocharts.animation.ChartViewportAnimator;
import lecho.lib.hellocharts.animation.ChartViewportAnimatorV14;
import lecho.lib.hellocharts.animation.ChartViewportAnimatorV8;
import lecho.lib.hellocharts.computator.ChartComputator;
import lecho.lib.hellocharts.gesture.ChartTouchHandler;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.renderer.AxesRenderer;
import lecho.lib.hellocharts.renderer.ChartRenderer;
import lecho.lib.hellocharts.util.ChartUtils;

public abstract class AbstractChartView extends View implements Chart {
    protected AxesRenderer axesRenderer;
    protected ChartComputator chartComputator;
    protected ChartRenderer chartRenderer;
    protected ContainerScrollType containerScrollType;
    protected ChartDataAnimator dataAnimator;
    protected boolean isContainerScrollEnabled;
    protected boolean isInteractive;
    protected ChartTouchHandler touchHandler;
    protected ChartViewportAnimator viewportAnimator;

    public AbstractChartView(Context context) {
        this(context, null, 0);
    }

    public AbstractChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbstractChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.isInteractive = true;
        this.isContainerScrollEnabled = false;
        this.chartComputator = new ChartComputator();
        this.touchHandler = new ChartTouchHandler(context, this);
        this.axesRenderer = new AxesRenderer(context, this);
        if (Build.VERSION.SDK_INT < 14) {
            this.dataAnimator = new ChartDataAnimatorV8(this);
            this.viewportAnimator = new ChartViewportAnimatorV8(this);
            return;
        }
        this.viewportAnimator = new ChartViewportAnimatorV14(this);
        this.dataAnimator = new ChartDataAnimatorV14(this);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        this.chartComputator.setContentRect(getWidth(), getHeight(), getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        this.chartRenderer.onChartSizeChanged();
        this.axesRenderer.onChartSizeChanged();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isEnabled()) {
            this.axesRenderer.drawInBackground(canvas);
            int clipRestoreCount = canvas.save();
            canvas.clipRect(this.chartComputator.getContentRectMinusAllMargins());
            this.chartRenderer.draw(canvas);
            canvas.restoreToCount(clipRestoreCount);
            this.chartRenderer.drawUnclipped(canvas);
            this.axesRenderer.drawInForeground(canvas);
            return;
        }
        canvas.drawColor(ChartUtils.DEFAULT_COLOR);
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean needInvalidate;
        super.onTouchEvent(event);
        if (!this.isInteractive) {
            return false;
        }
        if (this.isContainerScrollEnabled) {
            needInvalidate = this.touchHandler.handleTouchEvent(event, getParent(), this.containerScrollType);
        } else {
            needInvalidate = this.touchHandler.handleTouchEvent(event);
        }
        if (needInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        return true;
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.isInteractive && this.touchHandler.computeScroll()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void startDataAnimation() {
        this.dataAnimator.startAnimation(Long.MIN_VALUE);
    }

    public void startDataAnimation(long duration) {
        this.dataAnimator.startAnimation(duration);
    }

    public void cancelDataAnimation() {
        this.dataAnimator.cancelAnimation();
    }

    public void animationDataUpdate(float scale) {
        getChartData().update(scale);
        this.chartRenderer.onChartViewportChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void animationDataFinished() {
        getChartData().finish();
        this.chartRenderer.onChartViewportChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setDataAnimationListener(ChartAnimationListener animationListener) {
        this.dataAnimator.setChartAnimationListener(animationListener);
    }

    public void setViewportAnimationListener(ChartAnimationListener animationListener) {
        this.viewportAnimator.setChartAnimationListener(animationListener);
    }

    public void setViewportChangeListener(ViewportChangeListener viewportChangeListener) {
        this.chartComputator.setViewportChangeListener(viewportChangeListener);
    }

    public ChartRenderer getChartRenderer() {
        return this.chartRenderer;
    }

    public void setChartRenderer(ChartRenderer renderer) {
        this.chartRenderer = renderer;
        resetRendererAndTouchHandler();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public AxesRenderer getAxesRenderer() {
        return this.axesRenderer;
    }

    public ChartComputator getChartComputator() {
        return this.chartComputator;
    }

    public ChartTouchHandler getTouchHandler() {
        return this.touchHandler;
    }

    public boolean isInteractive() {
        return this.isInteractive;
    }

    public void setInteractive(boolean isInteractive2) {
        this.isInteractive = isInteractive2;
    }

    public boolean isZoomEnabled() {
        return this.touchHandler.isZoomEnabled();
    }

    public void setZoomEnabled(boolean isZoomEnabled) {
        this.touchHandler.setZoomEnabled(isZoomEnabled);
    }

    public boolean isScrollEnabled() {
        return this.touchHandler.isScrollEnabled();
    }

    public void setScrollEnabled(boolean isScrollEnabled) {
        this.touchHandler.setScrollEnabled(isScrollEnabled);
    }

    public void moveTo(float x, float y) {
        setCurrentViewport(computeScrollViewport(x, y));
    }

    public void moveToWithAnimation(float x, float y) {
        setCurrentViewportWithAnimation(computeScrollViewport(x, y));
    }

    private Viewport computeScrollViewport(float x, float y) {
        Viewport maxViewport = getMaximumViewport();
        Viewport currentViewport = getCurrentViewport();
        Viewport scrollViewport = new Viewport(currentViewport);
        if (maxViewport.contains(x, y)) {
            float width = currentViewport.width();
            float height = currentViewport.height();
            float left = Math.max(maxViewport.left, Math.min(x - (width / 2.0f), maxViewport.right - width));
            float top = Math.max(maxViewport.bottom + height, Math.min(y + (height / 2.0f), maxViewport.top));
            scrollViewport.set(left, top, left + width, top - height);
        }
        return scrollViewport;
    }

    public boolean isValueTouchEnabled() {
        return this.touchHandler.isValueTouchEnabled();
    }

    public void setValueTouchEnabled(boolean isValueTouchEnabled) {
        this.touchHandler.setValueTouchEnabled(isValueTouchEnabled);
    }

    public ZoomType getZoomType() {
        return this.touchHandler.getZoomType();
    }

    public void setZoomType(ZoomType zoomType) {
        this.touchHandler.setZoomType(zoomType);
    }

    public float getMaxZoom() {
        return this.chartComputator.getMaxZoom();
    }

    public void setMaxZoom(float maxZoom) {
        this.chartComputator.setMaxZoom(maxZoom);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public float getZoomLevel() {
        Viewport maxViewport = getMaximumViewport();
        Viewport currentViewport = getCurrentViewport();
        return Math.max(maxViewport.width() / currentViewport.width(), maxViewport.height() / currentViewport.height());
    }

    public void setZoomLevel(float x, float y, float zoomLevel) {
        setCurrentViewport(computeZoomViewport(x, y, zoomLevel));
    }

    public void setZoomLevelWithAnimation(float x, float y, float zoomLevel) {
        setCurrentViewportWithAnimation(computeZoomViewport(x, y, zoomLevel));
    }

    private Viewport computeZoomViewport(float x, float y, float zoomLevel) {
        Viewport maxViewport = getMaximumViewport();
        Viewport zoomViewport = new Viewport(getMaximumViewport());
        if (maxViewport.contains(x, y)) {
            if (zoomLevel < 1.0f) {
                zoomLevel = 1.0f;
            } else if (zoomLevel > getMaxZoom()) {
                zoomLevel = getMaxZoom();
            }
            float newWidth = zoomViewport.width() / zoomLevel;
            float newHeight = zoomViewport.height() / zoomLevel;
            float halfWidth = newWidth / 2.0f;
            float halfHeight = newHeight / 2.0f;
            float left = x - halfWidth;
            float right = x + halfWidth;
            float top = y + halfHeight;
            float bottom = y - halfHeight;
            if (left < maxViewport.left) {
                left = maxViewport.left;
                right = left + newWidth;
            } else if (right > maxViewport.right) {
                right = maxViewport.right;
                left = right - newWidth;
            }
            if (top > maxViewport.top) {
                top = maxViewport.top;
                bottom = top - newHeight;
            } else if (bottom < maxViewport.bottom) {
                bottom = maxViewport.bottom;
                top = bottom + newHeight;
            }
            ZoomType zoomType = getZoomType();
            if (ZoomType.HORIZONTAL_AND_VERTICAL == zoomType) {
                zoomViewport.set(left, top, right, bottom);
            } else if (ZoomType.HORIZONTAL == zoomType) {
                zoomViewport.left = left;
                zoomViewport.right = right;
            } else if (ZoomType.VERTICAL == zoomType) {
                zoomViewport.top = top;
                zoomViewport.bottom = bottom;
            }
        }
        return zoomViewport;
    }

    public Viewport getMaximumViewport() {
        return this.chartRenderer.getMaximumViewport();
    }

    public void setMaximumViewport(Viewport maxViewport) {
        this.chartRenderer.setMaximumViewport(maxViewport);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setCurrentViewportWithAnimation(Viewport targetViewport) {
        if (targetViewport != null) {
            this.viewportAnimator.cancelAnimation();
            this.viewportAnimator.startAnimation(getCurrentViewport(), targetViewport);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setCurrentViewportWithAnimation(Viewport targetViewport, long duration) {
        if (targetViewport != null) {
            this.viewportAnimator.cancelAnimation();
            this.viewportAnimator.startAnimation(getCurrentViewport(), targetViewport, duration);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public Viewport getCurrentViewport() {
        return getChartRenderer().getCurrentViewport();
    }

    public void setCurrentViewport(Viewport targetViewport) {
        if (targetViewport != null) {
            this.chartRenderer.setCurrentViewport(targetViewport);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void resetViewports() {
        this.chartRenderer.setMaximumViewport(null);
        this.chartRenderer.setCurrentViewport(null);
    }

    public boolean isViewportCalculationEnabled() {
        return this.chartRenderer.isViewportCalculationEnabled();
    }

    public void setViewportCalculationEnabled(boolean isEnabled) {
        this.chartRenderer.setViewportCalculationEnabled(isEnabled);
    }

    public boolean isValueSelectionEnabled() {
        return this.touchHandler.isValueSelectionEnabled();
    }

    public void setValueSelectionEnabled(boolean isValueSelectionEnabled) {
        this.touchHandler.setValueSelectionEnabled(isValueSelectionEnabled);
    }

    public void selectValue(SelectedValue selectedValue) {
        this.chartRenderer.selectValue(selectedValue);
        callTouchListener();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public SelectedValue getSelectedValue() {
        return this.chartRenderer.getSelectedValue();
    }

    public boolean isContainerScrollEnabled() {
        return this.isContainerScrollEnabled;
    }

    public void setContainerScrollEnabled(boolean isContainerScrollEnabled2, ContainerScrollType containerScrollType2) {
        this.isContainerScrollEnabled = isContainerScrollEnabled2;
        this.containerScrollType = containerScrollType2;
    }

    /* access modifiers changed from: protected */
    public void onChartDataChange() {
        this.chartComputator.resetContentRect();
        this.chartRenderer.onChartDataChanged();
        this.axesRenderer.onChartDataChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /* access modifiers changed from: protected */
    public void resetRendererAndTouchHandler() {
        this.chartRenderer.resetRenderer();
        this.axesRenderer.resetRenderer();
        this.touchHandler.resetTouchHandler();
    }

    public boolean canScrollHorizontally(int direction) {
        if (((double) getZoomLevel()) <= 1.0d) {
            return false;
        }
        Viewport currentViewport = getCurrentViewport();
        Viewport maximumViewport = getMaximumViewport();
        if (direction < 0) {
            if (currentViewport.left <= maximumViewport.left) {
                return false;
            }
            return true;
        } else if (currentViewport.right >= maximumViewport.right) {
            return false;
        } else {
            return true;
        }
    }
}
