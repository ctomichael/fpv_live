package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import java.util.concurrent.atomic.AtomicBoolean;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class ComputableLiveData<T> {
    /* access modifiers changed from: private */
    public AtomicBoolean mComputing = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public AtomicBoolean mInvalid = new AtomicBoolean(true);
    @VisibleForTesting
    final Runnable mInvalidationRunnable = new Runnable() {
        /* class android.arch.lifecycle.ComputableLiveData.AnonymousClass3 */

        @MainThread
        public void run() {
            boolean isActive = ComputableLiveData.this.mLiveData.hasActiveObservers();
            if (ComputableLiveData.this.mInvalid.compareAndSet(false, true) && isActive) {
                ArchTaskExecutor.getInstance().executeOnDiskIO(ComputableLiveData.this.mRefreshRunnable);
            }
        }
    };
    /* access modifiers changed from: private */
    public final LiveData<T> mLiveData = new LiveData<T>() {
        /* class android.arch.lifecycle.ComputableLiveData.AnonymousClass1 */

        /* access modifiers changed from: protected */
        public void onActive() {
            ArchTaskExecutor.getInstance().executeOnDiskIO(ComputableLiveData.this.mRefreshRunnable);
        }
    };
    @VisibleForTesting
    final Runnable mRefreshRunnable = new Runnable() {
        /* class android.arch.lifecycle.ComputableLiveData.AnonymousClass2 */

        @WorkerThread
        public void run() {
            do {
                boolean computed = false;
                if (ComputableLiveData.this.mComputing.compareAndSet(false, true)) {
                    Object obj = null;
                    while (ComputableLiveData.this.mInvalid.compareAndSet(true, false)) {
                        try {
                            computed = true;
                            obj = ComputableLiveData.this.compute();
                        } finally {
                            ComputableLiveData.this.mComputing.set(false);
                        }
                    }
                    if (computed) {
                        ComputableLiveData.this.mLiveData.postValue(obj);
                    }
                }
                if (!computed) {
                    return;
                }
            } while (ComputableLiveData.this.mInvalid.get());
        }
    };

    /* access modifiers changed from: protected */
    @WorkerThread
    public abstract T compute();

    @NonNull
    public LiveData<T> getLiveData() {
        return this.mLiveData;
    }

    public void invalidate() {
        ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
    }
}
