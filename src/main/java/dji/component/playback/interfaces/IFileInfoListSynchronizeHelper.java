package dji.component.playback.interfaces;

import android.support.annotation.NonNull;
import dji.component.playback.model.PlaybackFileInfo;
import dji.component.playback.result.PlaybackListItem;
import java.util.List;

public interface IFileInfoListSynchronizeHelper {
    void addUpdateListener(@NonNull IFileInfoListUpdateListener iFileInfoListUpdateListener);

    void getList(@NonNull IDownloadCallback<PlaybackListItem> iDownloadCallback);

    @NonNull
    List<PlaybackFileInfo> getListImmediately();

    void notifyListChange();

    void removeUpdateListener(@NonNull IFileInfoListUpdateListener iFileInfoListUpdateListener);
}
