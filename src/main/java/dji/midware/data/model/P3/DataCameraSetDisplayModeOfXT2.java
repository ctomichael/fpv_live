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
import dji.midware.media.DJIVideoDecoder;

@Keep
@EXClassNullAway
public class DataCameraSetDisplayModeOfXT2 extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetDisplayModeOfXT2 instance = null;
    private int blendLevel = 0;
    private CmdID cmdID = CmdID.OTHER;
    private DisplayMode displayMode = DisplayMode.PIP;
    private int horizontalPosition = 0;
    private PIPMode pipMode = PIPMode.IR_RIGHT_BOTTOM;
    private int verticalPosition = 0;

    public static synchronized DataCameraSetDisplayModeOfXT2 getInstance() {
        DataCameraSetDisplayModeOfXT2 dataCameraSetDisplayModeOfXT2;
        synchronized (DataCameraSetDisplayModeOfXT2.class) {
            if (instance == null) {
                instance = new DataCameraSetDisplayModeOfXT2();
            }
            dataCameraSetDisplayModeOfXT2 = instance;
        }
        return dataCameraSetDisplayModeOfXT2;
    }

    private DataCameraSetDisplayModeOfXT2() {
    }

    public void setDisplayMode(DisplayMode displayMode2) {
        this.displayMode = displayMode2;
        this.cmdID = CmdID.DISPLAY_MODE_CMD;
    }

    public void setPipMode(PIPMode pipMode2) {
        this.pipMode = pipMode2;
        this.cmdID = CmdID.PIP_MODE_CMD;
    }

    public void setHorizontalPosition(int horizontalPosition2) {
        this.horizontalPosition = horizontalPosition2;
        this.cmdID = CmdID.HO_POSITION_CMD;
    }

    public void setVerticalPosition(int verticalPosition2) {
        this.verticalPosition = verticalPosition2;
        this.cmdID = CmdID.VE_POSITION_CMD;
    }

    public void setBlendingLevel(int blendLevel2) {
        if (blendLevel2 > 100) {
            this.blendLevel = 100;
        } else if (blendLevel2 < 0) {
            this.blendLevel = 0;
        } else {
            this.blendLevel = blendLevel2;
        }
        this.cmdID = CmdID.BLENDING_CMD;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[3];
        this._sendData[0] = -104;
        this._sendData[1] = (byte) this.cmdID.value();
        if (this.cmdID == CmdID.DISPLAY_MODE_CMD) {
            this._sendData[2] = (byte) this.displayMode.value();
        } else if (this.cmdID == CmdID.PIP_MODE_CMD) {
            this._sendData[2] = (byte) this.pipMode.value();
        } else if (this.cmdID == CmdID.BLENDING_CMD) {
            this._sendData[2] = (byte) this.blendLevel;
        } else if (this.cmdID == CmdID.VE_POSITION_CMD) {
            this._sendData[2] = (byte) this.verticalPosition;
        } else if (this.cmdID == CmdID.HO_POSITION_CMD) {
            this._sendData[2] = (byte) this.horizontalPosition;
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
        pack.cmdId = CmdIdCamera.CmdIdType.TauParam.value();
        pack.timeOut = DJIVideoDecoder.connectLosedelay;
        start(pack, callBack);
    }

    @Keep
    public enum DisplayMode {
        VISIBLE_ONLY(0),
        IR_ONLY(1),
        PIP(2),
        BLENDING(3),
        OTHER(255);
        
        private int data;

        private DisplayMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static DisplayMode find(int b) {
            DisplayMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum PIPMode {
        IR_CENTER(0),
        IR_RIGHT_BOTTOM(1),
        IR_CENTER_RIGHT(2),
        IR_RIGHT_TOP(3),
        IR_CENTER_BOTTOM(4),
        IR_CENTER_TOP(5),
        IR_LEFT_BOTTOM(6),
        IR_CENTER_LEFT(7),
        IR_LEFT_TOP(8),
        SIDE_BY_SIDE(9),
        LINK_UP(10),
        OTHER(255);
        
        private int data;

        private PIPMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static PIPMode find(int b) {
            PIPMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum CmdID {
        DISPLAY_MODE_CMD(0),
        PIP_MODE_CMD(1),
        BLENDING_CMD(2),
        HO_POSITION_CMD(3),
        VE_POSITION_CMD(4),
        OTHER(255);
        
        private int data;

        private CmdID(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static CmdID find(int b) {
            CmdID result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
