package dji.component.playback.interfaces;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.component.playback.interfaces.IDownloadTask;
import dji.component.playback.model.PlaybackDownloadType;
import dji.component.playback.model.PlaybackFileInfo;
import dji.component.playback.model.PlaybackStorage;
import dji.component.playback.result.PlaybackFileItem;
import dji.component.playback.result.PlaybackListItem;

public interface IDJIPlaybackService {
    public static final String COMPONENT_NAME = "DJIPlaybackService";

    @NonNull
    IDownloadTask buildDownloadTask(@NonNull IDownloadTask.Priority priority, @NonNull PlaybackDownloadType playbackDownloadType, @NonNull PlaybackFileInfo playbackFileInfo);

    @NonNull
    IDeviceMediaPlayer buildMediaPlayer();

    void init(Context context);

    @NonNull
    IFileInfoListSynchronizeHelper obtainFileInfoListHelper();

    void removeAllTask(boolean z);

    void removeFileTask(@NonNull IDownloadTask.Priority priority, @NonNull PlaybackDownloadType playbackDownloadType, @NonNull PlaybackFileInfo playbackFileInfo);

    void removeListTask();

    void submitFileDownloadTask(@NonNull IDownloadTask iDownloadTask, @Nullable IDownloadCallback<PlaybackFileItem> iDownloadCallback);

    void submitListDownloadTask(@NonNull PlaybackStorage playbackStorage, int i, @Nullable IDownloadCallback<PlaybackListItem> iDownloadCallback);

    void submitListDownloadTask(@NonNull PlaybackStorage playbackStorage, @Nullable IDownloadCallback<PlaybackListItem> iDownloadCallback);
}
