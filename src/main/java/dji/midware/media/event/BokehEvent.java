package dji.midware.media.event;

import android.graphics.Bitmap;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class BokehEvent {
    public static final int RESULT_DOWNLOAD_FAIL = -2;
    public Bitmap bokeh = null;
    public Bitmap depth = null;

    /* renamed from: org  reason: collision with root package name */
    public Bitmap f22org = null;
    public String path;
    public int progress = 0;
    public int result = -2;
    public long time = 0;
    public BokehType type = BokehType.Other;

    public enum BokehType {
        Start,
        Finish,
        Failure,
        PreprocessStart,
        PreprocessOver,
        PreprocessTimeout,
        Refresh,
        Progress,
        DownloadStart,
        DownloadFailure,
        DownloadSuccess,
        Other
    }
}
