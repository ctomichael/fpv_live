package dji.component.playback.interfaces;

import android.support.annotation.NonNull;

public interface IDownloadCallback<T> {
    void onFailure(int i);

    void onFinish(@NonNull T t);

    void onProgress(float f);

    void onQueue();

    void onRateUpdate(int i);

    void onStart();
}
