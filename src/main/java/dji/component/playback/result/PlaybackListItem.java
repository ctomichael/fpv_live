package dji.component.playback.result;

import android.support.annotation.NonNull;
import dji.component.playback.model.PlaybackFileInfo;
import java.util.List;

public class PlaybackListItem {
    private List<PlaybackFileInfo> infoList;

    public void setInfoList(@NonNull List<PlaybackFileInfo> infoList2) {
        this.infoList = infoList2;
    }

    @NonNull
    public List<PlaybackFileInfo> getInfoList() {
        return this.infoList;
    }
}
