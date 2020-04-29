package dji.dbox.upgrade.p4.interfaces;

import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;
import java.util.Iterator;

@EXClassNullAway
public class DJIUpDownloadArrayListeners extends ArrayList<DJIUpDownloadListener> implements DJIUpDownloadListener {
    public void onDownloadStart() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            ((DJIUpDownloadListener) it2.next()).onDownloadStart();
        }
    }

    public void onDownloadProgress(int progress) {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            ((DJIUpDownloadListener) it2.next()).onDownloadProgress(progress);
        }
    }

    public void onDownloadComplete() {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            ((DJIUpDownloadListener) it2.next()).onDownloadComplete();
        }
    }

    public void onDownloadFail(String reason) {
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            ((DJIUpDownloadListener) it2.next()).onDownloadFail(reason);
        }
    }
}
