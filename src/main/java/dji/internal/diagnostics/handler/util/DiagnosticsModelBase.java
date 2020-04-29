package dji.internal.diagnostics.handler.util;

import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DJIDiagnosticsImpl;
import dji.midware.util.BackgroundLooper;
import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.io.reactivex.android.schedulers.AndroidSchedulers;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.utils.Optional;
import dji.utils.function.Consumer;
import dji.utils.function.Function;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public abstract class DiagnosticsModelBase<T> implements DiagnosticsModel<T> {
    protected static final int AUTO_DISAPPEAR = 3000;
    protected static final int DELAY_NOTIFY = 1500;
    protected static final int KEEP_TIME = 3000;
    private static final String TAG = "diagnostics";
    protected static final int UPDATE_DATA_PERIOD = 1000;
    protected Function<T, ?> extraDataFunction = null;
    protected boolean mAutoDisappear = false;
    protected Disposable mAutoDisappearDisposable;
    protected int mAutoDisappearTime = 3000;
    protected boolean mAutoUpdateExtraData = false;
    private Scheduler mBackgroundScheduler = AndroidSchedulers.from(BackgroundLooper.getLooper());
    protected int mDelayNotifyTime = 1500;
    private Integer mDiagnosticsCode;
    protected Object mExtraData;
    protected Disposable mExtraDataDisposable;
    protected Disposable mIgnoreTempChangeDisposable;
    protected boolean mIgnoreTemporaryChange = false;
    private T mInputValue;
    protected boolean mKeepForWhile = false;
    protected Disposable mKeepForWhileDisposable;
    protected int mKeepTime = 3000;
    private Integer mLastRealDiagnosticsCode;
    private Integer mRealDiagnosticsCode;
    protected int mUpdateDataPeriod = 1000;
    protected UpdateInterface mUpdater;

    /* access modifiers changed from: protected */
    public abstract Integer applyToCode(T t);

    public void doIfError(DJIDiagnosticsType dJIDiagnosticsType, Consumer consumer) {
        DiagnosticsModel$$CC.doIfError(this, dJIDiagnosticsType, consumer);
    }

    public final synchronized boolean statusApply(T value) {
        boolean z = false;
        synchronized (this) {
            this.mInputValue = value;
            this.mLastRealDiagnosticsCode = this.mRealDiagnosticsCode;
            this.mRealDiagnosticsCode = applyToCode(value);
            if (Objects.equals(this.mRealDiagnosticsCode, this.mLastRealDiagnosticsCode)) {
                z = onCodeNotChange(value);
            } else if (this.mIgnoreTemporaryChange) {
                delayNotifyChange();
            } else if (!this.mKeepForWhile || this.mRealDiagnosticsCode != null || this.mKeepForWhileDisposable == null || this.mKeepForWhileDisposable.isDisposed()) {
                changeCodeTo(this.mRealDiagnosticsCode);
                if (this.mAutoDisappear) {
                    autoDisappearTimer();
                }
                z = onCodeChange(this.mInputValue);
            } else {
                DiagnosticsLog.logi(TAG, String.format(Locale.US, "code=[%s]处于保护期间", getDiagnosticsCode()));
            }
        }
        return z;
    }

    public void doIfError(DJIDiagnosticsType type, Consumer<DJIDiagnosticsImpl> diagnosticsConsumer, int componentIndex) {
        if (this.mDiagnosticsCode != null) {
            diagnosticsConsumer.accept(new DJIDiagnosticsImpl(type, this.mDiagnosticsCode.intValue(), this.mExtraData, componentIndex));
        }
    }

    /* access modifiers changed from: protected */
    public boolean onCodeNotChange(T t) {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean onCodeChange(T t) {
        return true;
    }

    private void autoDisappearTimer() {
        safeDispose(this.mAutoDisappearDisposable);
        if (this.mDiagnosticsCode != null) {
            this.mAutoDisappearDisposable = Observable.just(this.mDiagnosticsCode).delay((long) this.mAutoDisappearTime, TimeUnit.MILLISECONDS).filter(new DiagnosticsModelBase$$Lambda$0(this)).observeOn(this.mBackgroundScheduler).subscribe(new DiagnosticsModelBase$$Lambda$1(this));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$autoDisappearTimer$0$DiagnosticsModelBase(Integer lastCode) throws Exception {
        return Objects.equals(lastCode, this.mDiagnosticsCode);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$autoDisappearTimer$1$DiagnosticsModelBase(Integer lastCode) throws Exception {
        if (Objects.equals(lastCode, this.mDiagnosticsCode)) {
            onAutoDisappear();
            this.mDiagnosticsCode = null;
            this.mUpdater.onStatusUpdate();
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void onAutoDisappear() {
    }

    private void resetKeepForAWhileTimer() {
        safeDispose(this.mKeepForWhileDisposable);
        if (this.mRealDiagnosticsCode != null) {
            this.mKeepForWhileDisposable = Observable.timer((long) this.mKeepTime, TimeUnit.MILLISECONDS).doOnNext(new DiagnosticsModelBase$$Lambda$2(this)).filter(new DiagnosticsModelBase$$Lambda$3(this)).observeOn(this.mBackgroundScheduler).subscribe(new DiagnosticsModelBase$$Lambda$4(this));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$resetKeepForAWhileTimer$2$DiagnosticsModelBase(Long l) throws Exception {
        DiagnosticsLog.logi(TAG, String.format(Locale.US, "code=[%s]保护期已过, 真实code=[%s]", getDiagnosticsCode(), this.mRealDiagnosticsCode));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$resetKeepForAWhileTimer$3$DiagnosticsModelBase(Long any) throws Exception {
        return this.mRealDiagnosticsCode == null;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$resetKeepForAWhileTimer$4$DiagnosticsModelBase(Long any) throws Exception {
        if (this.mRealDiagnosticsCode == null) {
            DiagnosticsLog.logi(TAG, String.format(Locale.US, "code=[%s]重置", getDiagnosticsCode()));
            this.mDiagnosticsCode = null;
            this.mUpdater.onStatusUpdate();
        }
    }

    private synchronized void changeCodeTo(Integer realDiagnosticsCode) {
        this.mDiagnosticsCode = realDiagnosticsCode;
        if (this.mKeepForWhile) {
            resetKeepForAWhileTimer();
        }
        if (this.mDiagnosticsCode == null) {
            safeDispose(this.mExtraDataDisposable);
        }
        if (!(this.mDiagnosticsCode == null || this.extraDataFunction == null)) {
            if (!this.mAutoUpdateExtraData || this.mUpdater == null) {
                this.mExtraData = this.extraDataFunction.apply(this.mInputValue);
            } else {
                autoUpdateExtraData();
            }
        }
    }

    private void delayNotifyChange() {
        safeDispose(this.mIgnoreTempChangeDisposable);
        if (!Objects.equals(this.mRealDiagnosticsCode, this.mDiagnosticsCode)) {
            this.mIgnoreTempChangeDisposable = Observable.just(Optional.ofNullable(this.mRealDiagnosticsCode)).delay((long) this.mDelayNotifyTime, TimeUnit.MILLISECONDS).observeOn(this.mBackgroundScheduler).subscribe(new DiagnosticsModelBase$$Lambda$5(this));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$delayNotifyChange$5$DiagnosticsModelBase(Optional statusOp) throws Exception {
        Integer status = (Integer) statusOp.orElse(null);
        if (!Objects.equals(this.mDiagnosticsCode, status)) {
            changeCodeTo(status);
            this.mUpdater.onStatusUpdate();
        }
    }

    private void autoUpdateExtraData() {
        this.mExtraDataDisposable = Observable.interval((long) this.mUpdateDataPeriod, TimeUnit.MILLISECONDS).takeWhile(new DiagnosticsModelBase$$Lambda$6(this)).map(new DiagnosticsModelBase$$Lambda$7(this)).observeOn(this.mBackgroundScheduler).subscribe(new DiagnosticsModelBase$$Lambda$8(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$autoUpdateExtraData$6$DiagnosticsModelBase(Long any) throws Exception {
        return this.mDiagnosticsCode != null;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Optional lambda$autoUpdateExtraData$7$DiagnosticsModelBase(Long any) throws Exception {
        return Optional.ofNullable(this.extraDataFunction.apply(this.mInputValue));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$autoUpdateExtraData$8$DiagnosticsModelBase(Optional extraDataOpt) throws Exception {
        Object current = extraDataOpt.orElse(null);
        if (!Objects.equals(current, this.mExtraData)) {
            this.mExtraData = current;
            this.mUpdater.onStatusUpdate();
        }
    }

    private static void safeDispose(Disposable extraDataDisposable) {
        if (extraDataDisposable != null) {
            extraDataDisposable.dispose();
        }
    }

    public DiagnosticsModelBase<T> updateExtraPeriodic(UpdateInterface mAutoUpdater, int mUpdateDataPeriod2) {
        this.mAutoUpdateExtraData = true;
        this.mUpdater = mAutoUpdater;
        this.mUpdateDataPeriod = mUpdateDataPeriod2;
        return this;
    }

    public DiagnosticsModelBase<T> updateExtraPeriodic(UpdateInterface mAutoUpdater) {
        return updateExtraPeriodic(mAutoUpdater, 1000);
    }

    public DiagnosticsModelBase<T> keepForAWhile(UpdateInterface mAutoUpdater, int keepTime) {
        this.mKeepForWhile = true;
        this.mUpdater = mAutoUpdater;
        this.mKeepTime = keepTime;
        return this;
    }

    public DiagnosticsModelBase<T> keepForAWhile(UpdateInterface mAutoUpdater) {
        return keepForAWhile(mAutoUpdater, 3000);
    }

    public DiagnosticsModelBase<T> ignoreTempChange(UpdateInterface mAutoUpdater, int delayNotifyTime) {
        this.mIgnoreTemporaryChange = true;
        this.mUpdater = mAutoUpdater;
        this.mDelayNotifyTime = delayNotifyTime;
        return this;
    }

    public DiagnosticsModelBase<T> ignoreTempChange(UpdateInterface mAutoUpdater) {
        return ignoreTempChange(mAutoUpdater, 1500);
    }

    public DiagnosticsModelBase<T> autoDisappear(UpdateInterface mAutoUpdater, int autoDisappearTime) {
        this.mAutoDisappear = true;
        this.mUpdater = mAutoUpdater;
        this.mAutoDisappearTime = autoDisappearTime;
        return this;
    }

    public DiagnosticsModelBase<T> autoDisappear(UpdateInterface mAutoUpdater) {
        return autoDisappear(mAutoUpdater, 3000);
    }

    public void dispose() {
        safeDispose(this.mExtraDataDisposable);
        safeDispose(this.mIgnoreTempChangeDisposable);
        safeDispose(this.mKeepForWhileDisposable);
        safeDispose(this.mAutoDisappearDisposable);
    }

    public synchronized void reset() {
        safeDispose(this.mExtraDataDisposable);
        safeDispose(this.mIgnoreTempChangeDisposable);
        safeDispose(this.mKeepForWhileDisposable);
        safeDispose(this.mAutoDisappearDisposable);
        this.mDiagnosticsCode = null;
        this.mRealDiagnosticsCode = null;
        this.mLastRealDiagnosticsCode = null;
        this.mExtraData = null;
        this.mInputValue = null;
    }

    public Integer getDiagnosticsCode() {
        return this.mDiagnosticsCode;
    }
}
