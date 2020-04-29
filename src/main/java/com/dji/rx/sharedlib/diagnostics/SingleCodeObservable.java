package com.dji.rx.sharedlib.diagnostics;

import dji.diagnostics.DiagnosticsInformationListener;
import dji.diagnostics.model.DJIDiagnostics;
import dji.internal.diagnostics.DJIDiagnosticsManagerImpl;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SingleCodeObservable extends DiagnosticsObservable {
    private int mDiagnosticsCode;

    public SingleCodeObservable(int diagnosticsCode) {
        this.mDiagnosticsCode = diagnosticsCode;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super DiagnosticsEvent> observer) {
        DiagnosticsDispatcher dispatcher = new DiagnosticsDispatcher(observer, this.mDiagnosticsCode);
        observer.onSubscribe(dispatcher);
        DJIDiagnosticsManagerImpl.getInstance().addDiagnosticsInformationListener(dispatcher);
    }

    static final class DiagnosticsDispatcher implements Disposable, DiagnosticsInformationListener {
        private int mDiagnosticsCode;
        private List<DJIDiagnostics> mDiagnosticsListHolder;
        final Observer<? super DiagnosticsEvent> observer;
        private final AtomicBoolean unsubscribed = new AtomicBoolean();

        public DiagnosticsDispatcher(Observer<? super DiagnosticsEvent> observer2, int diagnosticsCode) {
            this.observer = observer2;
            this.mDiagnosticsCode = diagnosticsCode;
        }

        public void dispose() {
            if (this.unsubscribed.compareAndSet(false, true)) {
                DJIDiagnosticsManagerImpl.getInstance().removeDiagnosticsInformationListener(this);
                this.mDiagnosticsListHolder.clear();
            }
        }

        public boolean isDisposed() {
            return this.unsubscribed.get();
        }

        public void onUpdate(List<DJIDiagnostics> diagnosticsList) {
            if (!isDisposed()) {
                List<DJIDiagnostics> hits = new ArrayList<>();
                for (DJIDiagnostics djiDiagnostics : diagnosticsList) {
                    if (djiDiagnostics.getCode() == this.mDiagnosticsCode) {
                        hits.add(djiDiagnostics);
                    }
                }
                if (!hits.equals(this.mDiagnosticsListHolder)) {
                    this.mDiagnosticsListHolder = hits;
                    this.observer.onNext(new DiagnosticsEvent(this.mDiagnosticsCode, hits));
                }
            }
        }
    }
}
