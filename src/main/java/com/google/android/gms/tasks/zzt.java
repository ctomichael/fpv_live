package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

final class zzt implements Executor {
    zzt() {
    }

    public final void execute(@NonNull Runnable runnable) {
        runnable.run();
    }
}
