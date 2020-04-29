package dji.midware.media.newframing;

import com.dji.video.framing.VideoLog;
import io.reactivex.functions.Consumer;

final /* synthetic */ class DJIVideoHevcControl$$Lambda$1 implements Consumer {
    static final Consumer $instance = new DJIVideoHevcControl$$Lambda$1();

    private DJIVideoHevcControl$$Lambda$1() {
    }

    public void accept(Object obj) {
        VideoLog.e(DJIVideoHevcControl.TAG, "sendChangeVideoTransferFormatToAir() failed:", (Throwable) obj, new Object[0]);
    }
}
