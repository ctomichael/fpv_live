package dji.logic.album.manager;

import android.content.Context;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIAlbumManagerFactory {
    public static DJIAlbumManager buildAlbumManager(Context context, String renameTo) {
        DJIAlbumCacheManager.getInstance(context).setRenameTo(renameTo);
        return new DJIAlbumManagerlitchiS();
    }
}
