package com.dji.component.fpv.widget.countdown;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.dji.component.fpv.base.ViewModelBinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class CountDownView extends AppCompatTextView implements ViewModelBinder<CountDownViewModel> {
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private CountDownViewModel mViewModel;

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(@NonNull CountDownViewModel countDownShellModel) {
        this.mViewModel = countDownShellModel;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mViewModel.onCreateView();
        addDisposable();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mViewModel.onDestroyView();
        removeDisposable();
    }

    private void addDisposable() {
        this.mDisposable.add(this.mViewModel.getObservableCountDownDescription().observeOn(AndroidSchedulers.mainThread()).subscribe(new CountDownView$$Lambda$0(this)));
        this.mDisposable.add(this.mViewModel.getObservableCountDownVisible().observeOn(AndroidSchedulers.mainThread()).subscribe(new CountDownView$$Lambda$1(this)));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$addDisposable$0$CountDownView(Boolean visible) throws Exception {
        setVisibility(visible.booleanValue() ? 0 : 4);
    }

    private void removeDisposable() {
        this.mDisposable.clear();
    }
}
