package lecho.lib.hellocharts.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import lecho.lib.hellocharts.BuildConfig;
import lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.DummyBubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.provider.BubbleChartDataProvider;
import lecho.lib.hellocharts.renderer.BubbleChartRenderer;

public class BubbleChartView extends AbstractChartView implements BubbleChartDataProvider {
    private static final String TAG = "BubbleChartView";
    protected BubbleChartRenderer bubbleChartRenderer;
    protected BubbleChartData data;
    protected BubbleChartOnValueSelectListener onValueTouchListener;

    public BubbleChartView(Context context) {
        this(context, null, 0);
    }

    public BubbleChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.onValueTouchListener = new DummyBubbleChartOnValueSelectListener();
        this.bubbleChartRenderer = new BubbleChartRenderer(context, this, this);
        setChartRenderer(this.bubbleChartRenderer);
        setBubbleChartData(BubbleChartData.generateDummyData());
    }

    public BubbleChartData getBubbleChartData() {
        return this.data;
    }

    public void setBubbleChartData(BubbleChartData data2) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Setting data for BubbleChartView");
        }
        if (data2 == null) {
            this.data = BubbleChartData.generateDummyData();
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

    public BubbleChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(BubbleChartOnValueSelectListener touchListener) {
        if (touchListener != null) {
            this.onValueTouchListener = touchListener;
        }
    }

    public void removeMargins() {
        this.bubbleChartRenderer.removeMargins();
        ViewCompat.postInvalidateOnAnimation(this);
    }
}
