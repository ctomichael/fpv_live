package dji.midware.data.manager.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.natives.GroudStation;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DJIEncryManager {
    private static final int CMD_TYPE_POSITION = 8;
    private static final int NEW_ENCRYPT_FLAG = 3;
    private static DJIEncryManager instance;
    private boolean isOldFirmware = true;

    public static synchronized DJIEncryManager getInstance() {
        DJIEncryManager dJIEncryManager;
        synchronized (DJIEncryManager.class) {
            if (instance == null) {
                instance = new DJIEncryManager();
            }
            dJIEncryManager = instance;
        }
        return dJIEncryManager;
    }

    public byte[] simpleEncrypt(byte[] data, int seq) {
        if (data == null) {
            return null;
        }
        int encryptLength = (data.length - 9) - 2;
        byte[] preEcryptData = new byte[encryptLength];
        System.arraycopy(data, 9, preEcryptData, 0, encryptLength);
        System.arraycopy(GroudStation.native_rcDataDeal(preEcryptData, seq), 0, data, 9, encryptLength);
        return data;
    }

    public byte[] simpleDecrypt(byte[] data, int seq) {
        if (data == null) {
            return null;
        }
        int encryptLength = (data.length - 9) - 2;
        byte[] preDecryptData = new byte[encryptLength];
        System.arraycopy(data, 9, preDecryptData, 0, encryptLength);
        System.arraycopy(GroudStation.native_rcDataDeal(preDecryptData, seq), 0, data, 9, encryptLength);
        return data;
    }

    public boolean isOldFirmware() {
        return this.isOldFirmware;
    }

    public void setIsOldFirmware(boolean isOldFirmware2) {
        this.isOldFirmware = isOldFirmware2;
    }

    public boolean isEncryptData(byte[] data) {
        if ((BytesUtil.getInt(data[8]) & 7) == 3) {
            return true;
        }
        return false;
    }

    public boolean isNeedEncrypt(byte[] buffer) {
        int cmdSet = BytesUtil.getInt(buffer[9]);
        int cmdId = BytesUtil.getInt(buffer[10]);
        if (cmdSet == CmdSet.COMMON.value() || (cmdSet == CmdSet.CAMERA.value() && (cmdSet != CmdSet.CAMERA.value() || cmdId == 16 || cmdId == 17 || cmdId == 112 || cmdId == 113))) {
            return false;
        }
        return true;
    }

    public void encryptData(byte[] buffer) {
        buffer[8] = (byte) (buffer[8] | 3);
    }
}
