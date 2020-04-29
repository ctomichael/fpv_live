package dji.logic.album.manager;

import android.support.annotation.Nullable;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.album.model.DJIAlbumDirInfo;
import dji.logic.album.model.DJIAlbumFile;
import dji.logic.album.model.DJIAlbumFileInfo;
import dji.logic.album.model.DJIAlbumPullErrorType;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import java.util.ArrayList;

@EXClassNullAway
public interface DJIAlbumInterface {

    public interface DJIAlbumPullListener<E> {
        void onFailure(DJIAlbumPullErrorType dJIAlbumPullErrorType);

        void onProgress(long j, long j2);

        void onRateUpdate(long j, long j2, long j3);

        void onStart();

        void onSuccess(E e);
    }

    void cancelTask();

    void downBokeh(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downBokehScreenNail(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downBokehThumbNail(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downCustomInfo(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downExif(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downFileList(ArrayList<DJIAlbumFileInfo> arrayList, DJIAlbumPullListener<ArrayList<DJIAlbumFile>> dJIAlbumPullListener);

    void downFileOne(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downPanoSubimgOne(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downPanoSubimgScreennail(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downPanoSubimgThumbnail(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downPanorama(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downScreennail(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downThumbnail(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downTimelapsePreviewImage(DJIAlbumFileInfo dJIAlbumFileInfo, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void downloadFile(@Nullable DJIAlbumFileInfo dJIAlbumFileInfo, DataConfig.SubType subType, DJIAlbumPullListener<DJIAlbumFile> dJIAlbumPullListener);

    void getAudioDirectoryInfo(DJIAlbumPullListener<DJIAlbumDirInfo> dJIAlbumPullListener, DataCameraSetStorageInfo.Storage storage);

    void getAudioInfoIncrementally(DJIAlbumPullListener<DJIAlbumDirInfo> dJIAlbumPullListener, int i, DataCameraSetStorageInfo.Storage storage);

    void getDirectoryInfo(DJIAlbumPullListener<DJIAlbumDirInfo> dJIAlbumPullListener);

    void getDirectoryInfo(DJIAlbumPullListener<DJIAlbumDirInfo> dJIAlbumPullListener, DataCameraSetStorageInfo.Storage storage);

    void getDirectoryInfoIncrementally(DJIAlbumPullListener<DJIAlbumDirInfo> dJIAlbumPullListener, int i);

    void getDirectoryInfoIncrementally(DJIAlbumPullListener<DJIAlbumDirInfo> dJIAlbumPullListener, int i, DataCameraSetStorageInfo.Storage storage);
}
