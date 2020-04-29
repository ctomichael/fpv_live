package dji.logic.album.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.manager.litchis.DJIFileListLoader;
import dji.logic.album.manager.litchis.DJIFileLoader;
import dji.logic.album.manager.litchis.DJIFileNailLoader;
import dji.logic.album.model.DJIAlbumDirInfo;
import dji.logic.album.model.DJIAlbumFile;
import dji.logic.album.model.DJIAlbumFileInfo;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.litchis.DataConfig;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import java.util.ArrayList;

@EXClassNullAway
class DJIAlbumManagerlitchiS extends DJIAlbumManager {
    DJIFileListLoader fileListLoader;
    DJIFileLoader fileLoader;
    DJIFileNailLoader nailLoader;

    public void Destroy() {
        if (this.fileListLoader != null) {
            this.fileListLoader = null;
        }
        if (this.nailLoader != null) {
            this.nailLoader = null;
        }
        if (this.fileLoader != null) {
            this.fileLoader = null;
        }
    }

    public void downloadFile(@Nullable DJIAlbumFileInfo info, @NonNull DataConfig.SubType subType, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (subType == DataConfig.SubType.ORG) {
            if (this.fileLoader == null) {
                this.fileLoader = new DJIFileLoader();
            }
            this.fileLoader.setListener(info, listener);
            this.fileLoader.setType(subType);
            this.fileLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
            this.fileLoader.start();
            return;
        }
        if (this.nailLoader == null) {
            this.nailLoader = new DJIFileNailLoader();
        }
        this.nailLoader.setListener(info, listener);
        this.nailLoader.setType(subType);
        this.nailLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.nailLoader.start();
    }

    public synchronized void getDirectoryInfo(DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumDirInfo> listener) {
        cancel();
        if (this.fileListLoader == null) {
            this.fileListLoader = new DJIFileListLoader();
        }
        this.fileListLoader.setListener(listener);
        this.fileListLoader.setDeviceType(DeviceType.CAMERA);
        this.fileListLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.fileListLoader.start();
    }

    public synchronized void getDirectoryInfo(DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumDirInfo> listener, DataCameraSetStorageInfo.Storage storage) {
        cancel();
        if (this.fileListLoader == null) {
            this.fileListLoader = new DJIFileListLoader();
        }
        this.fileListLoader.setListener(listener);
        this.fileListLoader.setDeviceType(DeviceType.CAMERA);
        this.fileListLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.fileListLoader.setStorage(storage);
        this.fileListLoader.start();
    }

    public synchronized void getAudioDirectoryInfo(DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumDirInfo> listener, DataCameraSetStorageInfo.Storage storage) {
        cancel();
        if (this.fileListLoader == null) {
            this.fileListLoader = new DJIFileListLoader();
        }
        this.fileListLoader.setListener(listener);
        this.fileListLoader.setStorage(storage);
        this.fileListLoader.setDeviceType(DeviceType.OFDM);
        this.fileListLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.fileListLoader.start();
    }

    public synchronized void getDirectoryInfoIncrementally(DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumDirInfo> listener, int index) {
        cancel();
        if (this.fileListLoader == null) {
            this.fileListLoader = new DJIFileListLoader();
        }
        this.fileListLoader.setListener(listener);
        this.fileListLoader.setDeviceType(DeviceType.CAMERA);
        this.fileListLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.fileListLoader.start(index);
    }

    public synchronized void getAudioInfoIncrementally(DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumDirInfo> listener, int index, DataCameraSetStorageInfo.Storage storage) {
        cancel();
        if (this.fileListLoader == null) {
            this.fileListLoader = new DJIFileListLoader();
        }
        this.fileListLoader.setListener(listener);
        this.fileListLoader.setStorage(storage);
        this.fileListLoader.setDeviceType(DeviceType.OFDM);
        this.fileListLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.fileListLoader.start(index);
    }

    public void getDirectoryInfoIncrementally(DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumDirInfo> listener, int index, DataCameraSetStorageInfo.Storage storage) {
        cancel();
        if (this.fileListLoader == null) {
            this.fileListLoader = new DJIFileListLoader();
        }
        this.fileListLoader.setListener(listener);
        this.fileListLoader.setDeviceType(DeviceType.CAMERA);
        this.fileListLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.fileListLoader.setStorage(storage);
        this.fileListLoader.start(index);
    }

    public synchronized void downBokeh(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.nailLoader == null) {
            this.nailLoader = new DJIFileNailLoader();
        }
        this.nailLoader.setListener(fileInfo, listener);
        this.nailLoader.setType(DataConfig.SubType.BOKEH);
        this.nailLoader.start();
    }

    public synchronized void downBokehThumbNail(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.nailLoader == null) {
            this.nailLoader = new DJIFileNailLoader();
        }
        this.nailLoader.setListener(fileInfo, listener);
        this.nailLoader.setType(DataConfig.SubType.BOKEH_THM);
        this.nailLoader.start();
    }

    public synchronized void downBokehScreenNail(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.nailLoader == null) {
            this.nailLoader = new DJIFileNailLoader();
        }
        this.nailLoader.setListener(fileInfo, listener);
        this.nailLoader.setType(DataConfig.SubType.BOKEH_SCR);
        this.nailLoader.start();
    }

    public void downPanorama(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.nailLoader == null) {
            this.nailLoader = new DJIFileNailLoader();
        }
        this.nailLoader.setListener(fileInfo, listener);
        this.nailLoader.setType(DataConfig.SubType.Pano);
        this.nailLoader.start();
    }

    public void downExif(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.nailLoader == null) {
            this.nailLoader = new DJIFileNailLoader();
        }
        this.nailLoader.setListener(fileInfo, listener);
        this.nailLoader.setType(DataConfig.SubType.EXIF_NEW);
        this.nailLoader.start();
    }

    public void downCustomInfo(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.nailLoader == null) {
            this.nailLoader = new DJIFileNailLoader();
        }
        this.nailLoader.setListener(fileInfo, listener);
        this.nailLoader.setType(DataConfig.SubType.EXIF);
        this.nailLoader.start();
    }

    public synchronized void downFileOne(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.fileLoader == null) {
            this.fileLoader = new DJIFileLoader();
        }
        this.fileLoader.setListener(fileInfo, listener);
        this.fileLoader.setType(DataConfig.SubType.ORG);
        this.fileLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.fileLoader.start();
    }

    public void downFileList(ArrayList<DJIAlbumFileInfo> arrayList, DJIAlbumInterface.DJIAlbumPullListener<ArrayList<DJIAlbumFile>> dJIAlbumPullListener) {
    }

    public synchronized void downThumbnail(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.nailLoader == null) {
            this.nailLoader = new DJIFileNailLoader();
        }
        this.nailLoader.setListener(fileInfo, listener);
        this.nailLoader.setType(DataConfig.SubType.THM);
        this.nailLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.nailLoader.start();
    }

    public synchronized void downScreennail(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.nailLoader == null) {
            this.nailLoader = new DJIFileNailLoader();
        }
        this.nailLoader.setListener(fileInfo, listener);
        this.nailLoader.setType(DataConfig.SubType.SCR);
        this.nailLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.nailLoader.start();
    }

    private void cancel() {
        if (this.fileListLoader != null) {
            this.fileListLoader.abort(false);
        }
        if (this.nailLoader != null) {
            this.nailLoader.abort(false);
        }
        if (this.fileLoader != null) {
            this.fileLoader.abort(false);
        }
    }

    public synchronized void cancelTask() {
        cancel();
    }

    public void clearDir() {
    }

    public void clearFile() {
    }

    public void getDirInfo(DJIAlbumDirInfo dirInfo) {
    }

    public void getFile(DJIAlbumFileInfo fileInfo) {
    }

    public synchronized void downPanoSubimgOne(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.fileLoader == null) {
            this.fileLoader = new DJIFileLoader();
        }
        this.fileLoader.setListener(fileInfo, listener);
        this.fileLoader.setType(DataConfig.SubType.Pano);
        this.fileLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.fileLoader.start();
    }

    public synchronized void downPanoSubimgThumbnail(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.nailLoader == null) {
            this.nailLoader = new DJIFileNailLoader();
        }
        this.nailLoader.setListener(fileInfo, listener);
        this.nailLoader.setType(DataConfig.SubType.Pano_THM);
        this.nailLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.nailLoader.start();
    }

    public synchronized void downPanoSubimgScreennail(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.nailLoader == null) {
            this.nailLoader = new DJIFileNailLoader();
        }
        this.nailLoader.setListener(fileInfo, listener);
        this.nailLoader.setType(DataConfig.SubType.Pano_SCR);
        this.nailLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.nailLoader.start();
    }

    public void downTimelapsePreviewImage(DJIAlbumFileInfo fileInfo, DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener) {
        cancel();
        if (this.nailLoader == null) {
            this.nailLoader = new DJIFileNailLoader();
        }
        this.nailLoader.setListener(fileInfo, listener);
        this.nailLoader.setType(DataConfig.SubType.TIMELAPSE);
        this.nailLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.nailLoader.start();
    }
}
