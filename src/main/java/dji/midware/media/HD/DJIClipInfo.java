package dji.midware.media.HD;

import com.dji.frame.util.MD5;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.album.manager.litchis.DJIFileType;
import java.util.Date;
import java.util.Locale;

@EXClassNullAway
public class DJIClipInfo {
    public int clipNo;
    public int commitID;
    private Date downloadTime = null;
    public int endTimeMSec;
    public DJIFileType fileType;
    public long source_uuid;
    public int startTimeMSec;
    public String targetPath;
    public TranscodeState transcodeState;

    public enum TranscodeState {
        SUECCESS(0),
        UNDO(1),
        INVALID_PARAM(2),
        ERR_INCOMPLETE(3),
        ERR_UNSPECIFIED(4),
        UNKNOWN(-1);
        
        private int data;

        private TranscodeState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TranscodeState find(int b) {
            TranscodeState result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    private String getExt() {
        if (this.fileType != null) {
            return "." + this.fileType.toString().toLowerCase(Locale.ENGLISH);
        }
        return "";
    }

    public String getNameKey() {
        return "clip_" + MD5.getMD5For16(this.source_uuid + "_" + this.startTimeMSec + "_" + this.endTimeMSec + "_" + getDownloadTime()) + getExt();
    }

    public String toString() {
        return String.format(Locale.US, "Clip No.%d's state is %s", Integer.valueOf(this.clipNo), this.transcodeState.toString());
    }

    public Date getDownloadTime() {
        if (this.downloadTime == null) {
            this.downloadTime = new Date();
        }
        return this.downloadTime;
    }
}
