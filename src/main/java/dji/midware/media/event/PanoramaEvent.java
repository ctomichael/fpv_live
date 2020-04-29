package dji.midware.media.event;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class PanoramaEvent {
    public String path;
    public int progress;
    public PanoramaType type;

    public enum PanoramaType {
        Generate,
        Finish,
        Failure,
        Progress,
        DownloadSuccess,
        DownloadStart,
        DownloadFailure,
        SpherePreview,
        CameraDownloadSuccess,
        CameraDownloadFailure,
        Other
    }
}
