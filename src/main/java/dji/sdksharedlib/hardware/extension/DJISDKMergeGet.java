package dji.sdksharedlib.hardware.extension;

import android.os.Handler;
import android.os.Message;
import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.BackgroundLooper;
import java.util.ArrayList;
import java.util.List;

@EXClassNullAway
public abstract class DJISDKMergeGet {
    protected static final int MSG_ADD = 0;
    protected static final int MSG_EXCUTE = 1;
    protected int DELAY_TIME = LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE;
    private List<Object> commandList = new ArrayList();
    protected Handler handler = new Handler(BackgroundLooper.getLooper()) {
        /* class dji.sdksharedlib.hardware.extension.DJISDKMergeGet.AnonymousClass1 */

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DJISDKMergeGet.this.innerAdd(msg.obj);
                    return;
                case 1:
                    DJISDKMergeGet.this.innerExecute();
                    return;
                default:
                    return;
            }
        }
    };

    /* access modifiers changed from: protected */
    public abstract void execute(List<Object> list);

    /* access modifiers changed from: private */
    public void innerAdd(Object cmd) {
        this.commandList.add(cmd);
        if (!this.handler.hasMessages(1)) {
            this.handler.sendEmptyMessageDelayed(1, getDelayTime());
        }
    }

    /* access modifiers changed from: private */
    public void innerExecute() {
        execute(new ArrayList<>(this.commandList));
        this.commandList.clear();
    }

    /* access modifiers changed from: protected */
    public void addCommand(Object cmd) {
        Message msg = this.handler.obtainMessage(0);
        msg.obj = cmd;
        this.handler.sendMessage(msg);
    }

    /* access modifiers changed from: protected */
    public long getDelayTime() {
        return (long) this.DELAY_TIME;
    }
}
