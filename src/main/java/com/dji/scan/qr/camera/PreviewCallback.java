package com.dji.scan.qr.camera;

import com.dji.scan.qr.SourceData;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface PreviewCallback {
    void onPreview(SourceData sourceData);

    void onPreviewError(Exception exc);
}
