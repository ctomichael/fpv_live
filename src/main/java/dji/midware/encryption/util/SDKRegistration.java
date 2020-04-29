package dji.midware.encryption.util;

import android.util.Base64;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class SDKRegistration {
    private static SDKRegistration instance;

    public static synchronized SDKRegistration getInstance() {
        SDKRegistration sDKRegistration;
        synchronized (SDKRegistration.class) {
            if (instance == null) {
                instance = new SDKRegistration();
            }
            sDKRegistration = instance;
        }
        return sDKRegistration;
    }

    public String getLocalFileContent(String appId, String fileContent) {
        try {
            byte[] fileContentDataByte = Base64.decode(fileContent, 2);
            String byte2hex = BytesUtil.byte2hex(fileContentDataByte);
            return new String(AES256Encryption.decrypt(fileContentDataByte, "Android@" + appId));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getFileContentforServer(String Data, String requestKey) {
        try {
            byte[] bytes = Data.getBytes();
            return new String(Base64.encode(AES256Encryption.encrypt(Data.getBytes(), requestKey), 2));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
