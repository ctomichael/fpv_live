package dji.dbox.upgrade.logics;

import android.content.Context;
import android.os.Looper;
import com.dji.fieldAnnotation.EXClassNullAway;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.dbox.upgrade.p4.model.DJIUpStatus;
import dji.dbox.upgrade.p4.utils.DJIRequestDeviceCfg;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.p4.utils.DJIUpgradeBaseUtils;
import dji.publics.DJIExecutor;

@EXClassNullAway
public class DJIFirVerComparator {
    private static final String TAG = "DJIFirVerComparator";
    private DJIRequestDeviceCfg.DJIRequestCfgListener listener = new DJIRequestDeviceCfg.DJIRequestCfgListener() {
        /* class dji.dbox.upgrade.logics.DJIFirVerComparator.AnonymousClass1 */

        public void onSuccess(DJIUpCfgModel deviceCfgModel) {
            if (DJIFirVerComparator.this.mCallback == null) {
                return;
            }
            if (deviceCfgModel.isNull()) {
                DJIFirVerComparator.this.mCallback.onFailed();
                DJIUpConstants.LOGD(DJIFirVerComparator.TAG, "Comparator deviceCfgModel is null");
                return;
            }
            DJIUpListElement element = DJIFirVerComparator.this.status.getChoiceElement();
            if (element == null) {
                DJIUpConstants.LOGD(DJIFirVerComparator.TAG, "Comparator element is null");
                DJIFirVerComparator.this.mCallback.onFailed();
                return;
            }
            DJIUpConstants.LOGD(DJIFirVerComparator.TAG, "Comparator releaseVersion=" + deviceCfgModel.releaseVersion + " product_version=" + element.product_version);
            if (DJIUpgradeBaseUtils.isSameFirVer(deviceCfgModel.releaseVersion, element.product_version)) {
                DJIFirVerComparator.this.mCallback.onCompare(true);
            } else {
                DJIFirVerComparator.this.mCallback.onCompare(false);
            }
        }

        public void onFailed() {
            if (DJIFirVerComparator.this.retryTimes > 0) {
                DJIFirVerComparator.this.start();
                DJIFirVerComparator.access$210(DJIFirVerComparator.this);
            }
        }
    };
    /* access modifiers changed from: private */
    public Callback mCallback;
    /* access modifiers changed from: private */
    public DJIRequestDeviceCfg requestDeviceCfg;
    /* access modifiers changed from: private */
    public int retryTimes = 3;
    /* access modifiers changed from: private */
    public DJIUpStatus status = DJIUpStatusHelper.getUpgradingStatus();

    public interface Callback {
        void onCompare(boolean z);

        void onFailed();
    }

    static /* synthetic */ int access$210(DJIFirVerComparator x0) {
        int i = x0.retryTimes;
        x0.retryTimes = i - 1;
        return i;
    }

    public DJIFirVerComparator(Context context, Callback callback) {
        this.mCallback = callback;
        if (this.status != null && context != null) {
            this.requestDeviceCfg = new DJIRequestDeviceCfg(context, this.status.getDeviceType(), this.status.getDeviceId(), this.status.getProductId(), this.listener);
        }
    }

    public void start() {
        if (this.requestDeviceCfg == null) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            DJIExecutor.getExecutorFor(DJIExecutor.Purpose.NORMAL).execute(new Runnable() {
                /* class dji.dbox.upgrade.logics.DJIFirVerComparator.AnonymousClass2 */

                public void run() {
                    DJIFirVerComparator.this.requestDeviceCfg.startGetDeviceCfg();
                }
            });
        } else {
            this.requestDeviceCfg.startGetDeviceCfg();
        }
    }
}
