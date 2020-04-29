package dji.log;

import android.os.Handler;
import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.packages.P3.Pack;
import dji.midware.data.packages.P3.RecvPack;
import dji.midware.data.packages.P3.SendPack;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Keep
@EXClassNullAway
public class DJISendPackWatcher {
    private static final boolean DEBUG_TEST = false;
    private static final int MSG_DUMPFPS = 2;
    private static final int MSG_DUMPINFO = 1;
    private static final int MSG_DUMPPACK = 0;
    private static final String TAG = "PackWatcher";
    private volatile int mBeforeDisplayCodec;
    private volatile int mBeforeQueneOutCodec;
    private volatile int mBeforeQueneToCodec;
    /* access modifiers changed from: private */
    public volatile long mBeforeRecvLength;
    /* access modifiers changed from: private */
    public volatile long mBeforeSendLength;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private volatile int mTotalDisplayCodec;
    private volatile int mTotalQueneOutCodec;
    private volatile int mTotalQueneToCodec;
    /* access modifiers changed from: private */
    public volatile long mTotalReceviceLength;
    /* access modifiers changed from: private */
    public volatile long mTotalSendLength;

    public static DJISendPackWatcher getInstance() {
        return SingletonHolder.mInstance;
    }

    public void watchPack(Pack pack) {
        if (pack instanceof SendPack) {
            this.mTotalSendLength += (long) pack.getLength();
        } else if (pack instanceof RecvPack) {
            this.mTotalReceviceLength += (long) pack.getLength();
        }
    }

    public void watchPack(boolean beSend, int byteLength) {
        if (beSend) {
            this.mTotalSendLength += (long) byteLength;
        } else {
            this.mTotalReceviceLength += (long) byteLength;
        }
    }

    public void loge(String msg) {
        Log.e(TAG, msg);
    }

    public void watchCodecIn(int frame) {
        this.mTotalQueneToCodec += frame;
        Log.e(TAG, "watchCodecIn-" + frame + ";" + System.currentTimeMillis());
    }

    public void watchDisplay(int frame) {
        this.mTotalDisplayCodec += frame;
    }

    public void watchCodecOut(int frame) {
        this.mTotalQueneOutCodec += frame;
        Log.e(TAG, "watchCodecOut-" + frame + ";" + System.currentTimeMillis());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
    }

    public void printfFrame(int frameNum, long frameIndex) {
    }

    public void watchCmd(SendPack pack) {
    }

    private DJISendPackWatcher() {
        this.mBeforeSendLength = 0;
        this.mTotalSendLength = 0;
        this.mBeforeRecvLength = 0;
        this.mTotalReceviceLength = 0;
        this.mBeforeQueneToCodec = 0;
        this.mTotalQueneToCodec = 0;
        this.mBeforeQueneOutCodec = 0;
        this.mTotalQueneOutCodec = 0;
        this.mBeforeDisplayCodec = 0;
        this.mTotalDisplayCodec = 0;
        this.mHandler = null;
    }

    @Keep
    private static final class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJISendPackWatcher mInstance = new DJISendPackWatcher();

        private SingletonHolder() {
        }
    }
}
