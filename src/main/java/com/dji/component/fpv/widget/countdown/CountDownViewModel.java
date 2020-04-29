package com.dji.component.fpv.widget.countdown;

import android.support.annotation.NonNull;
import com.dji.component.fpv.base.AbstractViewModel;
import com.dji.component.fpv.base.BulletinBoardKey;
import com.dji.component.fpv.base.BulletinBoardProvider;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import java.util.concurrent.TimeUnit;

public class CountDownViewModel extends AbstractViewModel {
    private Disposable mCountDownDisposable;
    private Disposable mDismissDisposable;
    private PublishSubject<String> mSubjectCountDownContent = PublishSubject.create();
    private PublishSubject<Boolean> mSubjectCountDownVisibility = PublishSubject.create();

    public CountDownViewModel(BulletinBoardProvider bridge) {
        super(bridge);
    }

    public void onCreateView() {
        this.mCountDownDisposable = getBulletinBoard().getObservable(BulletinBoardKey.Preview.COUNT_DOWN).subscribe(new CountDownViewModel$$Lambda$0(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onCreateView$1$CountDownViewModel(Object o) throws Exception {
        if (o instanceof BulletinBoardKey.Preview.CountDownEvent) {
            if (this.mDismissDisposable != null && !this.mDismissDisposable.isDisposed()) {
                this.mDismissDisposable.dispose();
            }
            BulletinBoardKey.Preview.CountDownEvent event = (BulletinBoardKey.Preview.CountDownEvent) o;
            String content = event.getContent();
            int duration = event.getDuration();
            this.mSubjectCountDownContent.onNext(content);
            this.mSubjectCountDownVisibility.onNext(true);
            if (duration > 0) {
                this.mDismissDisposable = Observable.timer((long) duration, TimeUnit.MILLISECONDS).subscribe(new CountDownViewModel$$Lambda$1(this));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$null$0$CountDownViewModel(Long empty) throws Exception {
        this.mSubjectCountDownVisibility.onNext(false);
    }

    public void onDestroyView() {
        this.mCountDownDisposable.dispose();
        if (this.mDismissDisposable != null && !this.mDismissDisposable.isDisposed()) {
            this.mDismissDisposable.dispose();
        }
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public Observable<String> getObservableCountDownDescription() {
        return this.mSubjectCountDownContent.hide();
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public Observable<Boolean> getObservableCountDownVisible() {
        return this.mSubjectCountDownVisibility.hide();
    }
}
