package com.dji.component.fpv.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dji.component.fpv.base.providers.FpvScopeProvider;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java.util.Map;

public abstract class AbstractViewGate implements LifecycleObserver {
    protected BulletinBoardProvider mBridge;
    protected CompositeDisposable mCompositeDisposable;
    protected Context mContext;
    protected IFpvContainer mFPVContainer;
    protected Map<String, ? super FpvScopeProvider> mFpvScopeProviderMap;

    /* access modifiers changed from: protected */
    public abstract void inflateShell(@NonNull Context context, @NonNull BulletinBoardProvider bulletinBoardProvider);

    /* access modifiers changed from: protected */
    public abstract void onShellFinishInflate();

    public AbstractViewGate(Context context, IFpvContainer fpvContainer, BulletinBoardProvider bridge) {
        this(context, fpvContainer, bridge, null);
    }

    public AbstractViewGate(Context context, IFpvContainer fpvContainer, BulletinBoardProvider bridge, Map<String, ? super FpvScopeProvider> fpvScopeProviderMap) {
        this.mCompositeDisposable = new CompositeDisposable();
        this.mContext = context;
        this.mFPVContainer = fpvContainer;
        this.mBridge = bridge;
        this.mFpvScopeProviderMap = fpvScopeProviderMap;
    }

    public void onInflatePermit() {
        this.mCompositeDisposable.add(Observable.create(new AbstractViewGate$$Lambda$0(this)).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new AbstractViewGate$$Lambda$1(this)));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onInflatePermit$0$AbstractViewGate(ObservableEmitter emitter) throws Exception {
        inflateShell(this.mContext, this.mBridge);
        emitter.onNext(new Object());
        emitter.onComplete();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onInflatePermit$1$AbstractViewGate(Object o) throws Exception {
        onShellFinishInflate();
    }

    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @CallSuper
    public void onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @CallSuper
    public void onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    @CallSuper
    public void onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    @CallSuper
    public void onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @CallSuper
    public void onDestroy() {
        this.mCompositeDisposable.clear();
    }

    @CallSuper
    public void onLowMemory() {
    }

    @CallSuper
    public void onSaveInstanceState(Bundle outState) {
    }

    public void onInitDevice() {
    }
}
