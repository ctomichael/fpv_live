package android.arch.lifecycle;

import android.arch.core.util.Function;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Transformations {
    private Transformations() {
    }

    @MainThread
    public static <X, Y> LiveData<Y> map(@NonNull LiveData<X> source, @NonNull final Function<X, Y> func) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<X>() {
            /* class android.arch.lifecycle.Transformations.AnonymousClass1 */

            public void onChanged(@Nullable X x) {
                result.setValue(func.apply(x));
            }
        });
        return result;
    }

    @MainThread
    public static <X, Y> LiveData<Y> switchMap(@NonNull LiveData<X> trigger, @NonNull final Function<X, LiveData<Y>> func) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(trigger, new Observer<X>() {
            /* class android.arch.lifecycle.Transformations.AnonymousClass2 */
            LiveData<Y> mSource;

            public void onChanged(@Nullable X x) {
                LiveData<Y> newLiveData = (LiveData) func.apply(x);
                if (this.mSource != newLiveData) {
                    if (this.mSource != null) {
                        result.removeSource(this.mSource);
                    }
                    this.mSource = newLiveData;
                    if (this.mSource != null) {
                        result.addSource(this.mSource, new Observer<Y>() {
                            /* class android.arch.lifecycle.Transformations.AnonymousClass2.AnonymousClass1 */

                            public void onChanged(@Nullable Y y) {
                                result.setValue(y);
                            }
                        });
                    }
                }
            }
        });
        return result;
    }
}
