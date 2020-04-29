package dji.internal.diagnostics;

import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import dji.diagnostics.DiagnosticsInformationListener;
import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsType;
import dji.log.DJILog;
import dji.thirdparty.io.reactivex.android.schedulers.AndroidSchedulers;
import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.subjects.BehaviorSubject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class DJIDiagnosticsUpdateObserver implements DiagnosticsInformationListener {
    private static final DJIDiagnosticsImpl DEFAULT = new DJIDiagnosticsImpl(DJIDiagnosticsType.OTHER, 0);
    private static final long PROTECTION_TIME = 500;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private DJIDiagnosticsManagerImpl mDJIDiagnosticsManager;
    private final SparseArrayCompat<BehaviorSubject<DJIDiagnostics>> mDiagnosticsSubjectMap = new SparseArrayCompat<>();
    private final List<BehaviorSubject<DJIDiagnostics>> mSubjectList = new ArrayList();
    private List<DiagnosticsUpdateListener> mUpdateListenerList = new CopyOnWriteArrayList();

    public interface DiagnosticsUpdateListener {
        void onChange(@Nullable DJIDiagnostics dJIDiagnostics);
    }

    public DJIDiagnosticsUpdateObserver(int... errorCodeList) {
        for (int errorCode : errorCodeList) {
            BehaviorSubject<DJIDiagnostics> subject = BehaviorSubject.createDefault(DEFAULT);
            this.mDiagnosticsSubjectMap.put(errorCode, subject);
            this.mSubjectList.add(subject);
        }
    }

    public void addUpdateListener(DiagnosticsUpdateListener listener) {
        if (listener != null && !this.mUpdateListenerList.contains(listener)) {
            this.mUpdateListenerList.add(listener);
        }
    }

    public void removeUpdateListener(DiagnosticsUpdateListener listener) {
        if (listener != null) {
            this.mUpdateListenerList.remove(listener);
        }
    }

    public void start() {
        this.mDJIDiagnosticsManager = DJIDiagnosticsManagerImpl.getInstance();
        this.mDJIDiagnosticsManager.addDiagnosticsInformationListener(this);
        for (int i = 0; i < this.mDiagnosticsSubjectMap.size(); i++) {
            this.mCompositeDisposable.add(this.mDiagnosticsSubjectMap.valueAt(i).skip(1).throttleLast(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged().subscribe(new DJIDiagnosticsUpdateObserver$$Lambda$0(this)));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$start$0$DJIDiagnosticsUpdateObserver(DJIDiagnostics d) throws Exception {
        DJILog.i("diag update", "diag update code: " + d.getCode() + " all: " + d.toString(), new Object[0]);
        if (d.equals(DEFAULT)) {
            notifyUpdate(null);
        } else {
            notifyUpdate(d);
        }
    }

    public void stop() {
        this.mCompositeDisposable.clear();
        if (this.mDJIDiagnosticsManager != null) {
            this.mDJIDiagnosticsManager.removeDiagnosticsInformationListener(this);
        }
    }

    private void notifyUpdate(DJIDiagnostics diagnostics) {
        for (DiagnosticsUpdateListener listener : this.mUpdateListenerList) {
            listener.onChange(diagnostics);
        }
    }

    public void onUpdate(List<DJIDiagnostics> diagnosticsList) {
        if (diagnosticsList != null && !diagnosticsList.isEmpty()) {
            HashSet<Integer> currentSet = new HashSet<>();
            for (int i = 0; i < this.mDiagnosticsSubjectMap.size(); i++) {
                if (((DJIDiagnostics) this.mDiagnosticsSubjectMap.valueAt(i).getValue()) != DEFAULT) {
                    currentSet.add(Integer.valueOf(this.mDiagnosticsSubjectMap.keyAt(i)));
                }
            }
            HashSet<Integer> updateSet = new HashSet<>();
            for (DJIDiagnostics item : diagnosticsList) {
                BehaviorSubject<DJIDiagnostics> subject = this.mDiagnosticsSubjectMap.get(item.getCode(), null);
                if (subject != null) {
                    subject.onNext(item);
                    updateSet.add(Integer.valueOf(item.getCode()));
                }
            }
            currentSet.removeAll(updateSet);
            Iterator it2 = currentSet.iterator();
            while (it2.hasNext()) {
                this.mDiagnosticsSubjectMap.get(((Integer) it2.next()).intValue()).onNext(DEFAULT);
            }
        }
    }
}
