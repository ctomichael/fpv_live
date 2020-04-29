package dji.midware.media.demuxer;

import android.media.MediaFormat;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public abstract class DemuxerBase {
    protected int audioTrack = -1;
    protected int videoTrack = -1;

    public abstract int getTrackCount();

    public abstract MediaFormat getTrackFormat(int i);

    public int getTrackIndex(String expected_mime) {
        for (int i = 0; i < getTrackCount(); i++) {
            String ext_mime = getTrackFormat(i).getString("mime");
            if (ext_mime != null && ext_mime.equals(expected_mime)) {
                return i;
            }
        }
        return -1;
    }

    public int getAudioTrack() {
        String type;
        if (this.audioTrack < 0) {
            int i = 0;
            while (true) {
                if (i >= getTrackCount()) {
                    break;
                }
                String ext_mime = getTrackFormat(i).getString("mime");
                if (ext_mime != null && (type = ext_mime.split(IMemberProtocol.PARAM_SEPERATOR)[0]) != null && type.equalsIgnoreCase("audio")) {
                    this.audioTrack = i;
                    break;
                }
                i++;
            }
        }
        return this.audioTrack;
    }

    public int getVideoTrack() {
        String type;
        if (this.videoTrack < 0) {
            int i = 0;
            while (true) {
                if (i >= getTrackCount()) {
                    break;
                }
                String ext_mime = getTrackFormat(i).getString("mime");
                if (ext_mime != null && (type = ext_mime.split(IMemberProtocol.PARAM_SEPERATOR)[0]) != null && type.equalsIgnoreCase("video")) {
                    this.videoTrack = i;
                    break;
                }
                i++;
            }
        }
        return this.videoTrack;
    }
}
