package dji.component.playback.model;

import java.io.Serializable;

public class PlaybackExif implements Serializable {
    private byte[] mData;

    public PlaybackExif(byte[] data) {
        this.mData = data;
    }

    public void setData(byte[] data) {
        this.mData = data;
    }

    public byte[] getData() {
        return this.mData;
    }
}
