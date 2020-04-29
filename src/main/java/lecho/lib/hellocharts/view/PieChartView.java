package lecho.lib.hellocharts.view;

import android.content.Context;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import lecho.lib.hellocharts.BuildConfig;
import lecho.lib.hellocharts.animation.PieChartRotationAnimator;
import lecho.lib.hellocharts.animation.PieChartRotationAnimatorV14;
import lecho.lib.hellocharts.animation.PieChartRotationAnimatorV8;
import lecho.lib.hellocharts.gesture.PieChartTouchHandler;
import lecho.lib.hellocharts.listener.DummyPieChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.provider.PieChartDataProvider;
import lecho.lib.hellocharts.renderer.PieChartRenderer;

public class PieChartView extends AbstractChartView implements PieChartDataProvider {
    private static final String TAG = "PieChartView";
    protected PieChartData data;
    protected PieChartOnValueSelectListener onValueTouchListener;
    protected PieChartRenderer pieChartRenderer;
    protected PieChartRotationAnimator rotationAnimator;

    public PieChartView(Context context) {
        this(context, null, 0);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.onValueTouchListener = new DummyPieChartOnValueSelectListener();
        this.pieChartRenderer = new PieChartRenderer(context, this, this);
        this.touchHandler = new PieChartTouchHandler(context, this);
        setChartRenderer(this.pieChartRenderer);
        if (Build.VERSION.SDK_INT < 14) {
            this.rotationAnimator = new PieChartRotationAnimatorV8(this);
        } else {
            this.rotationAnimator = new PieChartRotationAnimatorV14(this);
        }
        setPieChartData(PieChartData.generateDummyData());
    }

    public PieChartData getPieChartData() {
        return this.data;
    }

    public void setPieChartData(PieChartData data2) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Setting data for ColumnChartView");
        }
        if (data2 == null) {
            this.data = PieChartData.generateDummyData();
        } else {
            this.data = data2;
        }
        super.onChartDataChange();
    }

    public ChartData getChartData() {
        return this.data;
    }

    public void callTouchListener() {
        SelectedValue selectedValue = this.chartRenderer.getSelectedValue();
        if (selectedValue.isSet()) {
            this.onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), this.data.getValues().get(selectedValue.getFirstIndex()));
            return;
        }
        this.onValueTouchListener.onValueDeselected();
    }

    public PieChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(PieChartOnValueSelectListener touchListener) {
        if (touchListener != null) {
            this.onValueTouchListener = touchListener;
        }
    }

    public RectF getCircleOval() {
        return this.pieChartRenderer.getCircleOval();
    }

    public void setCircleOval(RectF orginCircleOval) {
        this.pieChartRenderer.setCircleOval(orginCircleOval);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public int getChartRotation() {
        return this.pieChartRenderer.getChartRotation();
    }

    public void setChartRotation(int rotation, boolean isAnimated) {
        if (isAnimated) {
            this.rotationAnimator.cancelAnimation();
            this.rotationAnimator.startAnimation((float) this.pieChartRenderer.getChartRotation(), (float) rotation);
        } else {
            this.pieChartRenderer.setChartRotation(rotation);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public boolean isChartRotationEnabled() {
        if (this.touchHandler instanceof PieChartTouchHandler) {
            return ((PieChartTouchHandler) this.touchHandler).isRotationEnabled();
        }
        return false;
    }

    public void setChartRotationEnabled(boolean isRotationEnabled) {
        if (this.touchHandler instanceof PieChartTouchHandler) {
            ((PieChartTouchHandler) this.touchHandler).setRotationEnabled(isRotationEnabled);
        }
    }

    public SliceValue getValueForAngle(int angle, SelectedValue selectedValue) {
        return this.pieChartRenderer.getValueForAngle(angle, selectedValue);
    }

    public float getCircleFillRatio() {
        return this.pieChartRenderer.getCircleFillRatio();
    }

    public void setCircleFillRatio(float fillRatio) {
        this.pieChartRenderer.setCircleFillRatio(fillRatio);
        ViewCompat.postInvalidateOnAnimation(this);
    }
}
