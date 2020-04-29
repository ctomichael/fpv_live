package dji.component.playback.interfaces;

import android.support.annotation.NonNull;
import dji.component.playback.model.PlaybackFileInfo;
import dji.component.playback.model.PlaybackStorage;
import java.util.List;

public interface IFileInfoListUpdateListener {
    void onUpdate(@NonNull PlaybackStorage playbackStorage, @NonNull List<PlaybackFileInfo> list);
}
