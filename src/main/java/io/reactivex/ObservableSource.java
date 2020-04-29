package io.reactivex;

import dji.common.camera.CameraRecordingState;
import dji.utils.Optional;
import io.reactivex.annotations.NonNull;

public interface ObservableSource<T> {
    void subscribe(@NonNull Observer<? super Optional<CameraRecordingState>> observer);
}
