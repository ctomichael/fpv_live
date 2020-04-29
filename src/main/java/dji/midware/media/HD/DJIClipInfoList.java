package dji.midware.media.HD;

import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;
import java.util.Locale;

@EXClassNullAway
public class DJIClipInfoList {
    public int clipCount;
    public ArrayList<DJIClipInfo> clipList = new ArrayList<>();
    public int commitNo;
    public int dataLength;

    public String toString() {
        return String.format(Locale.US, "clipCount=%d dataLength=%d commitNo=%d", Integer.valueOf(this.clipCount), Integer.valueOf(this.dataLength), Integer.valueOf(this.commitNo)) + ". " + this.clipList.toString();
    }
}
