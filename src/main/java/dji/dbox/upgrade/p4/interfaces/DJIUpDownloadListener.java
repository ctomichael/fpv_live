package dji.dbox.upgrade.p4.interfaces;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface DJIUpDownloadListener {
    void onDownloadComplete();

    void onDownloadFail(String str);

    void onDownloadProgress(int i);

    void onDownloadStart();
}
