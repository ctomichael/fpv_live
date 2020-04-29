package com.mapzen.android.lost.api;

import android.support.annotation.NonNull;
import com.mapzen.android.lost.api.Result;

public interface ResultCallback<R extends Result> {
    void onResult(@NonNull R r);
}
