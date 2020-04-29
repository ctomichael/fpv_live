package dji.component.playback.result;

import android.support.annotation.NonNull;
import dji.component.playback.model.PlaybackFileInfo;

public class PlaybackFileItem {
    private PlaybackFileInfo info;
    private String path;

    public void setPath(@NonNull String path2) {
        this.path = path2;
    }

    @NonNull
    public String getPath() {
        return this.path;
    }

    public void setInfo(@NonNull PlaybackFileInfo info2) {
        this.info = info2;
    }

    @NonNull
    public PlaybackFileInfo getInfo() {
        return this.info;
    }
}
