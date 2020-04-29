package dji.logic.album.model;

import android.graphics.Bitmap;
import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.album.manager.litchis.DJIFileType;

@Keep
@EXClassNullAway
public class DJIAlbumFile {
    public Bitmap bitmap;
    public String cachPath;
    public long duration;
    public DJIFileType fileType;
    public int index;
    public long length;
}
