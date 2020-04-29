package dji.component.videoframing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.component.playback.model.video.PlaybackVideoSubType;
import io.reactivex.disposables.Disposable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface IDJIVideoFramingService {
    public static final String NAME = "DJIVideoFramingService";
    public static final int STATE_ERROR = 2;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_RECORDING = 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface RecordingState {
    }

    void addPhotoShootListener(@NonNull PhotoShootListener photoShootListener);

    void addVideoRecordListener(@NonNull VideoRecordListener videoRecordListener);

    int getRecordingState();

    void removePhotoShootListener(@Nullable PhotoShootListener photoShootListener);

    void removeVideoRecordListener(@NonNull VideoRecordListener videoRecordListener);

    void startRecord(PlaybackVideoSubType playbackVideoSubType);

    boolean stopRecord();

    Disposable takePhoto();
}
