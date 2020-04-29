package com.dji.rx.sharedlib.diagnostics;

import android.util.SparseArray;
import dji.diagnostics.DiagnosticsInformationListener;
import dji.diagnostics.model.DJIDiagnostics;
import dji.internal.diagnostics.DJIDiagnosticsManagerImpl;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiCodeObservable extends DiagnosticsObservable {
    private List<Integer> mDiagnosticsCodeArr;

    public MultiCodeObservable(List<Integer> diagnosticsCodeArr) {
        this.mDiagnosticsCodeArr = diagnosticsCodeArr;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super DiagnosticsEvent> observer) {
        DiagnosticsDispatcher dispatcher = new DiagnosticsDispatcher(observer, this.mDiagnosticsCodeArr);
        observer.onSubscribe(dispatcher);
        DJIDiagnosticsManagerImpl.getInstance().addDiagnosticsInformationListener(dispatcher);
    }

    static final class DiagnosticsDispatcher implements Disposable, DiagnosticsInformationListener {
        private List<Integer> mDiagnosticsCodeArr;
        private SparseArrayList<DJIDiagnostics> mDiagnosticsMapHolder = new SparseArrayList<>();
        final Observer<? super DiagnosticsEvent> observer;
        private final AtomicBoolean unsubscribed = new AtomicBoolean();

        public DiagnosticsDispatcher(Observer<? super DiagnosticsEvent> observer2, List<Integer> diagnosticsCodeArr) {
            this.observer = observer2;
            this.mDiagnosticsCodeArr = diagnosticsCodeArr;
        }

        public void dispose() {
            if (this.unsubscribed.compareAndSet(false, true)) {
                DJIDiagnosticsManagerImpl.getInstance().removeDiagnosticsInformationListener(this);
                this.mDiagnosticsMapHolder.clear();
            }
        }

        public boolean isDisposed() {
            return this.unsubscribed.get();
        }

        public void onUpdate(List<DJIDiagnostics> diagnosticsList) {
            if (!isDisposed()) {
                SparseArrayList<DJIDiagnostics> hits = new SparseArrayList<>();
                for (DJIDiagnostics djiDiagnostic : diagnosticsList) {
                    if (this.mDiagnosticsCodeArr.contains(Integer.valueOf(djiDiagnostic.getCode()))) {
                        hits.putValue(djiDiagnostic.getCode(), djiDiagnostic);
                    }
                }
                for (Integer code : this.mDiagnosticsCodeArr) {
                    List<DJIDiagnostics> diagnosticList = (List) hits.get(code.intValue(), Collections.emptyList());
                    if (!diagnosticList.equals((List) this.mDiagnosticsMapHolder.get(code.intValue()))) {
                        this.mDiagnosticsMapHolder.put(code.intValue(), diagnosticList);
                        this.observer.onNext(new DiagnosticsEvent(code.intValue(), diagnosticList));
                    }
                }
            }
        }
    }

    static final class SparseArrayList<T> extends SparseArray<List<T>> {
        SparseArrayList() {
        }

        /* access modifiers changed from: package-private */
        public void putValue(int key, T value) {
            List<T> list = (List) get(key);
            if (list == null) {
                list = new ArrayList<>();
                put(key, list);
            }
            list.add(value);
        }
    }
}
