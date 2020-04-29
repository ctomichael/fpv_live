package dji.logic.album.manager;

import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.album.model.DJIAlbumDirInfo;

@EXClassNullAway
public abstract class DJIAlbumManager implements DJIAlbumInterface, DJIAlbumCacheInterface {
    protected DJIAlbumDirInfo dirinfo;
    protected boolean isSupportMultiThread = false;
    protected int receiverIdInProtocol = -1;

    public abstract void Destroy();

    public DJIAlbumManager setReceiverIdInProtocol(int receiverIdInProtocol2) {
        this.receiverIdInProtocol = receiverIdInProtocol2;
        return this;
    }
}
