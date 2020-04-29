package com.dji.component.fpv.widget.histogram;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.dji.component.fpv.base.ViewModelBinder;
import com.dji.component.fpv.widget.preview.R;
import dji.utils.Optional;
import io.reactivex.disposables.CompositeDisposable;

public class HistogramShell extends ConstraintLayout implements ViewModelBinder<HistogramShellViewModel> {
    private static final int INDEX_END = 3;
    private static final int INDEX_START = 3;
    private HistogramChartView mChartView = null;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Context mContext = null;
    private final float[] mData = new float[58];
    private Animation mHideAnim = null;
    private HistogramShellViewModel mHistogramShellViewModel;
    private boolean mPushEnable = false;
    private Animation mShowAnim = null;

    public HistogramShell(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void bind(HistogramShellViewModel histogramShellViewModel) {
        this.mHistogramShellViewModel = histogramShellViewModel;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mCompositeDisposable.add(this.mHistogramShellViewModel.getHistogramEnableObservable().subscribe(new HistogramShell$$Lambda$0(this)));
        this.mCompositeDisposable.add(this.mHistogramShellViewModel.getHistogramLightValueObservable().subscribe(new HistogramShell$$Lambda$1(this)));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$0$HistogramShell(Optional enable) throws Exception {
        this.mPushEnable = ((Boolean) enable.get()).booleanValue();
        if (this.mPushEnable) {
            setVisibility(0);
            showChart();
            return;
        }
        setVisibility(4);
        hideChart();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$1$HistogramShell(Optional values) throws Exception {
        if (this.mPushEnable && values.isPresent()) {
            copyData(this.mData, (short[]) values.get());
            this.mChartView.setData(this.mData);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mCompositeDisposable.clear();
    }

    public void showChart() {
        if (this.mPushEnable && getVisibility() != 0) {
            setVisibility(0);
            startAnimation(this.mShowAnim);
        }
    }

    public void hideChart() {
        if (getVisibility() != 8) {
            setVisibility(8);
            startAnimation(this.mHideAnim);
        }
    }

    private void copyData(float[] data, short[] cache) {
        for (int i = 3; i < 61; i++) {
            data[i - 3] = (float) cache[i];
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            this.mChartView = (HistogramChartView) findViewById(R.id.histogram_chart);
            ((ImageView) findViewById(R.id.histogram_close)).setOnClickListener(new HistogramShell$$Lambda$2(this));
            this.mShowAnim = AnimationUtils.loadAnimation(this.mContext, R.anim.show_middle);
            this.mHideAnim = AnimationUtils.loadAnimation(this.mContext, R.anim.hide_middel);
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onFinishInflate$2$HistogramShell(View v) {
        this.mHistogramShellViewModel.setObservableHistogramEnable(false);
    }
}
