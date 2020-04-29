package dji.dbox.upgrade.p4.interfaces;

import dji.dbox.upgrade.p4.interfaces.DJIUpgradeListener;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCommonGetPushUpgradeStatus;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

@EXClassNullAway
public class DJIUpgradeArrayListeners extends CopyOnWriteArrayList<DJIUpgradeListener> implements DJIUpgradeListener {
    private static final long serialVersionUID = 919186262593058534L;

    public void onCollectVersionStart() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onCollectVersionStart();
            }
        }
    }

    public void onCollectVersionComplete() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onCollectVersionComplete();
            }
        }
    }

    public void onCollectFail(String reason) {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onCollectFail(reason);
            }
        }
    }

    public void onZipStart() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onZipStart();
            }
        }
    }

    public void onZipProgress(int progress) {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onZipProgress(progress);
            }
        }
    }

    public void onZipComplete() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onZipComplete();
            }
        }
    }

    public void onZipFail() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onZipFail();
            }
        }
    }

    public void onUploadStart() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onUploadStart();
            }
        }
    }

    public void onUploadProgress(int progress) {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onUploadProgress(progress);
            }
        }
    }

    public void onUploadFail(DJIUpgradeListener.UploadFailReason reason, Ccode ccode) {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onUploadFail(reason, ccode);
            }
        }
    }

    public void onUploadComplete() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onUploadComplete();
            }
        }
    }

    public void onUpgradeProgress(String detail, int progress) {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onUpgradeProgress(detail, progress);
            }
        }
    }

    public void onUpgradeComplete() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onUpgradeComplete();
            }
        }
    }

    public void onUpgradeFail(DataCommonGetPushUpgradeStatus.DJIUpgradeCompleteReason reason) {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onUpgradeFail(reason);
            }
        }
    }

    public void onCollectDeviceCfgComplete() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onCollectDeviceCfgComplete();
            }
        }
    }

    public void onCollectProductTypeStart() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onCollectProductTypeStart();
            }
        }
    }

    public void onCollectProductTypeComplete() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onCollectProductTypeComplete();
            }
        }
    }

    public void onWaitTimeout() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            DJIUpgradeListener listener = (DJIUpgradeListener) it2.next();
            if (listener != null) {
                listener.onWaitTimeout();
            }
        }
    }
}
