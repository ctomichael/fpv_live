package dji.logic.album.manager;

import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.album.model.DJIAlbumDirInfo;
import dji.logic.album.model.DJIAlbumFileInfo;

@EXClassNullAway
public interface DJIAlbumCacheInterface {
    void clearDir();

    void clearFile();

    void getDirInfo(DJIAlbumDirInfo dJIAlbumDirInfo);

    void getFile(DJIAlbumFileInfo dJIAlbumFileInfo);
}
