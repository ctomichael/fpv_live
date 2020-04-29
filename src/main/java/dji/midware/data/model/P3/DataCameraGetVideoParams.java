package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;
import com.drew.metadata.photoshop.PhotoshopDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraGetVideoParams extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetVideoParams instance = null;

    public static synchronized DataCameraGetVideoParams getInstance() {
        DataCameraGetVideoParams dataCameraGetVideoParams;
        synchronized (DataCameraGetVideoParams.class) {
            if (instance == null) {
                instance = new DataCameraGetVideoParams();
            }
            dataCameraGetVideoParams = instance;
        }
        return dataCameraGetVideoParams;
    }

    @Keep
    public enum Resolution_Drone {
        R0(0, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE, 480, "p"),
        R1(1, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE, 480, "i"),
        R2(2, 1280, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE, "p"),
        R3(3, 1280, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE, "i"),
        R4(4, 1280, 720, "p"),
        R5(5, 1280, 720, "i"),
        R6(6, 1280, 960, "p"),
        R7(7, 1280, 960, "i"),
        R8(8, 1920, 960, "p"),
        R9(9, 1920, 960, "i"),
        R10(10, 1920, PhotoshopDirectory.TAG_COUNT_INFORMATION, "p"),
        R11(11, 1920, PhotoshopDirectory.TAG_COUNT_INFORMATION, "i"),
        R12(12, 1920, 1440, "p"),
        R13(13, 1920, 1440, "i"),
        R14(14, 3840, 1920, "p"),
        R15(15, 3840, 1920, "i"),
        R16(16, 3840, 2160, "p"),
        R17(17, 3840, 2160, "i"),
        R18(18, 3840, 2880, "p"),
        R19(19, 3840, 2880, "i"),
        R20(20, 4096, 2048, "p"),
        R21(21, 4096, 2048, "i"),
        R22(22, 4096, 2160, "p"),
        R23(23, 4096, 2160, "i"),
        OTHER(-1, 0, 0, "-");
        
        private int height;
        private String suffix;
        private int type;
        private int width;

        private Resolution_Drone(int type2, int width2, int height2, String suffix2) {
            this.type = type2;
            this.width = width2;
            this.height = height2;
            this.suffix = suffix2;
        }

        public int type() {
            return this.type;
        }

        public int height() {
            return this.height;
        }

        public int width() {
            return this.width;
        }

        public String suffix() {
            return this.suffix;
        }

        public boolean _equals(int b) {
            return this.type == b;
        }

        public static Resolution_Drone find(int b) {
            Resolution_Drone result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum FPS_Drone {
        FPS0(0, 15),
        FPS1(1, 24),
        FPS2(2, 25),
        FPS3(3, 30),
        FPS4(4, 48),
        FPS5(5, 50),
        FPS6(6, 60),
        FPS7(7, 120),
        FPS8(8, 240),
        FPS9(9, 480),
        OTHER(-1, 0);
        
        private int fps;
        private int type;

        private FPS_Drone(int type2, int fps2) {
            this.type = type2;
            this.fps = fps2;
        }

        public int type() {
            return this.type;
        }

        public int fps() {
            return this.fps;
        }

        public boolean _equals(int b) {
            return this.type == b;
        }

        public static FPS_Drone find(int b) {
            FPS_Drone result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public int getFolderId() {
        return ((Integer) get(0, 2, Integer.class)).intValue();
    }

    public int getFileId() {
        return ((Integer) get(2, 2, Integer.class)).intValue();
    }

    public long getUuid() {
        return ((Long) get(4, 4, Long.class)).longValue();
    }

    public int getRatio() {
        return ((Integer) get(8, 1, Integer.class)).intValue();
    }

    public int getFps() {
        return ((Integer) get(9, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetVideoParams.value();
        start(pack, callBack);
    }
}
