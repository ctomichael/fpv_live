package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataCameraControlTransCode extends DataBase implements DJIDataSyncListener {
    private static DataCameraControlTransCode instance = null;
    private int bps;
    private ControlType controlType;
    private int fileId;
    private int folderId;
    private int fragmentId;
    private ArrayList<DJIFragmentModel> fragmentModels;
    private int groupId;
    private ToFps toFps;
    private ToResolution toResolution;
    private int uuid;

    public static synchronized DataCameraControlTransCode getInstance() {
        DataCameraControlTransCode dataCameraControlTransCode;
        synchronized (DataCameraControlTransCode.class) {
            if (instance == null) {
                instance = new DataCameraControlTransCode();
            }
            dataCameraControlTransCode = instance;
        }
        return dataCameraControlTransCode;
    }

    public DataCameraControlTransCode setControlType(ControlType controlType2) {
        this.controlType = controlType2;
        return this;
    }

    public DataCameraControlTransCode setFolderId(int folderId2) {
        this.folderId = folderId2;
        return this;
    }

    public DataCameraControlTransCode setFileId(int fileId2) {
        this.fileId = fileId2;
        return this;
    }

    public DataCameraControlTransCode setUuid(int uuid2) {
        this.uuid = uuid2;
        return this;
    }

    public DataCameraControlTransCode setGroupId(int groupId2) {
        this.groupId = groupId2;
        return this;
    }

    public DataCameraControlTransCode setFragmentId(int fragmentId2) {
        this.fragmentId = fragmentId2;
        return this;
    }

    public DataCameraControlTransCode setResolution(ToResolution toResolution2) {
        this.toResolution = toResolution2;
        return this;
    }

    public DataCameraControlTransCode setFps(ToFps toFps2) {
        this.toFps = toFps2;
        return this;
    }

    public DataCameraControlTransCode setBps(int bps2) {
        this.bps = bps2;
        return this;
    }

    public DataCameraControlTransCode setFragments(ArrayList<DJIFragmentModel> fragmentModels2) {
        this.fragmentModels = fragmentModels2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int size = this.fragmentModels.size();
        this._sendData = new byte[((size * 8) + 19)];
        this._sendData[0] = (byte) this.controlType.value();
        byte[] folderbytes = BytesUtil.getBytes((short) this.folderId);
        System.arraycopy(folderbytes, 0, this._sendData, 0 + 1, folderbytes.length);
        int index = folderbytes.length + 1;
        byte[] filebytes = BytesUtil.getBytes((short) this.fileId);
        System.arraycopy(filebytes, 0, this._sendData, index, filebytes.length);
        int index2 = index + filebytes.length;
        byte[] uuidbytes = BytesUtil.getBytes(this.uuid);
        System.arraycopy(uuidbytes, 0, this._sendData, index2, uuidbytes.length);
        int index3 = index2 + uuidbytes.length;
        byte[] groupbytes = BytesUtil.getBytes((short) this.groupId);
        System.arraycopy(groupbytes, 0, this._sendData, index3, groupbytes.length);
        int index4 = index3 + groupbytes.length;
        byte[] fragbytes = BytesUtil.getBytes((short) this.fragmentId);
        System.arraycopy(fragbytes, 0, this._sendData, index4, fragbytes.length);
        int index5 = index4 + fragbytes.length;
        this._sendData[index5] = (byte) this.toResolution.value();
        int index6 = index5 + 1;
        this._sendData[index6] = (byte) this.toFps.value();
        int index7 = index6 + 1;
        byte[] bpsbytes = BytesUtil.getBytes((short) this.bps);
        System.arraycopy(bpsbytes, 0, this._sendData, index7, bpsbytes.length);
        int index8 = index7 + bpsbytes.length;
        byte[] fragnumbytes = BytesUtil.getBytes((short) size);
        System.arraycopy(fragnumbytes, 0, this._sendData, index8, fragnumbytes.length);
        int index9 = index8 + fragnumbytes.length;
        for (int i = 0; i < size; i++) {
            DJIFragmentModel model = this.fragmentModels.get(i);
            byte[] startbytes = BytesUtil.getBytes(model.startTime);
            System.arraycopy(startbytes, 0, this._sendData, index9, startbytes.length);
            int index10 = index9 + startbytes.length;
            byte[] endbytes = BytesUtil.getBytes(model.endTime);
            System.arraycopy(endbytes, 0, this._sendData, index10, endbytes.length);
            index9 = index10 + endbytes.length;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.ControlTransCode.value();
        start(pack, callBack);
    }

    @Keep
    public class DJIFragmentModel {
        public int endTime;
        public int startTime;

        public DJIFragmentModel() {
        }
    }

    @Keep
    public enum ControlType {
        STOP_All(0),
        START(1),
        STOP_CUR(2),
        PAUSE(3),
        ADD(4),
        OTHER(100);
        
        private int data;

        private ControlType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ControlType find(int b) {
            ControlType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum ToResolution {
        Default(0),
        R1920_1280_16_9(1),
        R1280_720_16_9(2),
        R848_480_16_9(3),
        R432_240_16_9(4),
        OTHER(100);
        
        private int data;

        private ToResolution(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ToResolution find(int b) {
            ToResolution result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum ToFps {
        fps15(0),
        fps24(1),
        fps25(2),
        fps30(3),
        fps48(4),
        fps50(5),
        fps60(6),
        fps120(7),
        fps240(8),
        fps480(9),
        OTHER(100);
        
        private int data;

        private ToFps(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ToFps find(int b) {
            ToFps result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
