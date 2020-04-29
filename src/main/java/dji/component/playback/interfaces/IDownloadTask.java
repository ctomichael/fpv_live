package dji.component.playback.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.component.playback.model.PlaybackFileInfo;

public interface IDownloadTask {
    public static final int ERROR_CODE_DEVICE_NOT_CONNECT = 6;
    public static final int ERROR_CODE_EXCLUSIVE_EXIST = 5;
    public static final int ERROR_CODE_INVALID_PARAMS = 2;
    public static final int ERROR_CODE_INVALID_STORAGE = 7;
    public static final int ERROR_CODE_TASK_EXIST = 4;
    public static final int ERROR_CODE_TIMEOUT = 1;
    public static final int ERROR_CODE_UNKNOWN = 255;
    public static final int ERROR_CODE_USER_CANCEL = 3;

    public enum Priority {
        BACKGROUND,
        FOREGROUND,
        EXCLUSIVE
    }

    @Nullable
    PlaybackFileInfo getFileInfo();

    @NonNull
    Priority getTaskPriority();

    @NonNull
    IDownloadTask setPath(@NonNull String str);
}
