package dji.sdksharedlib.util;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import dji.common.airlink.LteSignalStatus;
import dji.common.util.LteOperatorHelper;
import dji.midware.data.model.P3.DataModule4GCardType;
import dji.midware.data.model.P3.DataModule4GGetPushOperator;
import dji.midware.data.model.P3.DataModule4GGetPushSignal;
import dji.midware.util.BackgroundLooper;

public class LteSignalHelper {
    private static final int LTE_TIME_OUT = 2000;
    private static final int MSG_COUNTDOWN = 1;
    private Handler mHandler = new Handler(BackgroundLooper.getLooper()) {
        /* class dji.sdksharedlib.util.LteSignalHelper.AnonymousClass1 */

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    LteSignalStatus unused = LteSignalHelper.this.mLteStatus = LteSignalStatus.NONE;
                    if (LteSignalHelper.this.mKeyChanger != null) {
                        LteSignalHelper.this.mKeyChanger.notifyValueChangeForKeyPath(LteSignalHelper.this.mLteStatus);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public KeyValueChanger mKeyChanger;
    /* access modifiers changed from: private */
    public LteSignalStatus mLteStatus;
    private int mSenderType;

    public interface KeyValueChanger {
        void notifyValueChangeForKeyPath(Object obj);
    }

    public LteSignalHelper(KeyValueChanger keyChanger, int senderType) {
        this.mKeyChanger = keyChanger;
        this.mSenderType = senderType;
    }

    public void operatorChange(@NonNull DataModule4GGetPushOperator operator) {
        if ((operator.getPack() == null || operator.getPack().senderType == this.mSenderType) && operator.getCardType() != DataModule4GCardType.UNKNOWN) {
            LteSignalStatus status = getOldStatus();
            status.setOperatorName(LteOperatorHelper.getInstance().getName(operator.getMCC(), operator.getMNC()));
            if (this.mKeyChanger != null) {
                this.mKeyChanger.notifyValueChangeForKeyPath(status);
                this.mLteStatus = status;
            }
            lteHeartBeat();
        }
    }

    public void signalChange(@NonNull DataModule4GGetPushSignal signal) {
        if ((signal.getPack() == null || signal.getPack().senderType == this.mSenderType) && signal.getCardType() != DataModule4GCardType.UNKNOWN) {
            LteSignalStatus status = getOldStatus();
            status.setNetworkType(signal.getNetworkType());
            status.setSignal(signal.getSignal());
            if (this.mKeyChanger != null) {
                this.mKeyChanger.notifyValueChangeForKeyPath(status);
                this.mLteStatus = status;
            }
            lteHeartBeat();
        }
    }

    private LteSignalStatus getOldStatus() {
        return this.mLteStatus == null ? new LteSignalStatus() : this.mLteStatus.clone();
    }

    private void lteHeartBeat() {
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessageDelayed(1, 2000);
    }
}
