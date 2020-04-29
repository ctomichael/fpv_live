package io.reactivex.schedulers;

import io.reactivex.annotations.NonNull;

public interface SchedulerRunnableIntrospection {
    @NonNull
    Runnable getWrappedRunnable();
}
