package dji.midware.component.rc;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.interfaces.DJIDataCallBack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class DM368GBlockRequest {
    public final String TAG = "DM368GBlockRequest";
    private DataCommonGetVersion dm368_GGetter = null;
    /* access modifiers changed from: private */
    public CountDownLatch latch = null;
    /* access modifiers changed from: private */
    public boolean ret = false;

    public DataCommonGetVersion getDM368() {
        this.latch = new CountDownLatch(1);
        this.dm368_GGetter = new DataCommonGetVersion();
        this.dm368_GGetter.setDeviceType(DeviceType.DM368_G);
        this.ret = false;
        this.dm368_GGetter.start(new DJIDataCallBack() {
            /* class dji.midware.component.rc.DM368GBlockRequest.AnonymousClass1 */

            public void onSuccess(Object model) {
                DM368GBlockRequest.this.latch.countDown();
                boolean unused = DM368GBlockRequest.this.ret = true;
            }

            public void onFailure(Ccode ccode) {
                DM368GBlockRequest.this.latch.countDown();
            }
        });
        try {
            this.latch.await(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DJILog.d("DM368GBlockRequest", "getDM368Block()", new Object[0]);
        if (this.ret) {
            return this.dm368_GGetter;
        }
        return null;
    }
}
