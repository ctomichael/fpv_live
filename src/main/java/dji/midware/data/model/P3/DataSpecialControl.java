package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataFlycSetJoyStickParams;
import dji.midware.data.model.P3.DataGimbalControl;
import dji.midware.interfaces.DJIDataCallBack;

@Keep
@EXClassNullAway
@Deprecated
public abstract class DataSpecialControl extends DataBase {
    /* access modifiers changed from: protected */
    public abstract void _reset();

    /* access modifiers changed from: protected */
    public abstract void doPack();

    public abstract DataSpecialControl init();

    /* access modifiers changed from: protected */
    public abstract DataSpecialControl reset();

    public abstract DataSpecialControl resetGimbal();

    public abstract DataSpecialControl selfieGimbal();

    public abstract DataSpecialControl setFlyGoHomeStatus(FlyGoHomeStaus flyGoHomeStaus);

    public abstract DataSpecialControl setFlycMode(DataFlycSetJoyStickParams.FlycMode flycMode);

    public abstract DataSpecialControl setGimbalMode(DataGimbalControl.MODE mode);

    public abstract DataSpecialControl setGimbalMode(DataGimbalControl.MODE mode, boolean z);

    public abstract DataSpecialControl setPhotoType(DataCameraSetPhoto.TYPE type);

    public abstract DataSpecialControl setPhotoType(DataCameraSetPhoto.TYPE type, int i, int i2);

    public abstract DataSpecialControl setPlayBackBrowserScaleType(short s);

    public abstract DataSpecialControl setPlayBackBrowserType(PlayBrowseType playBrowseType, byte b, byte b2);

    public abstract DataSpecialControl setPlayBackPlayCtr(PlayCtrType playCtrType, byte b);

    public abstract DataSpecialControl setPlayBackType(boolean z);

    public abstract DataSpecialControl setRecordType(boolean z);

    public abstract DataSpecialControl setRecordType(boolean z, int i, int i2);

    public abstract void start(long j);

    public abstract void start(DJIDataCallBack dJIDataCallBack);

    public abstract void stop();

    public static final boolean useNewControl() {
        ProductType type = DJIProductManager.getInstance().getType();
        return ProductType.Pomato == type || ProductType.Orange2 == type || ProductType.M200 == type || ProductType.M210 == type || ProductType.M210RTK == type || ProductType.PM420 == type || ProductType.PM420PRO == type || ProductType.PM420PRO_RTK == type || ProductType.Potato == type;
    }

    @Deprecated
    public static synchronized DataSpecialControl getInstance() {
        DataSpecialControl instance;
        synchronized (DataSpecialControl.class) {
            if (useNewControl()) {
                instance = DataNewSpecialControl.getInstance();
            } else {
                instance = DataOldSpecialControl.getInstance();
            }
        }
        return instance;
    }

    @Keep
    public enum PlayCtrType {
        START(0),
        STOP(1),
        PAUSE(2),
        FastForward(3),
        FastRewind(4),
        TouchProgress(5),
        OTHER(100);
        
        private int data;

        private PlayCtrType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static PlayCtrType find(int b) {
            PlayCtrType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum PlayBrowseType {
        CANCEL(0),
        ENTER(1),
        DELETE(2),
        PAGEDOWN(3),
        PAGEUP(4),
        RIGHT(5),
        LEFT(6),
        DOWN(7),
        UP(8),
        ZOOMOUT(9),
        ZOOMIN(10),
        MULTIPLY(11),
        SINGLE(12),
        MULTIPLY_DEL(13),
        SCALE(14),
        DRAG(15),
        OTHER(100);
        
        private int data;

        private PlayBrowseType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static PlayBrowseType find(int b) {
            PlayBrowseType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum MulDelValue {
        ALL_CANCEL((byte) -4),
        ALL_SELECT((byte) -3),
        PAGE_CANCEL((byte) -2),
        PAGE_SELECT((byte) -1),
        INVALID((byte) 0);
        
        private byte data;

        private MulDelValue(byte _data) {
            this.data = _data;
        }

        public byte value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static MulDelValue find(byte b) {
            MulDelValue result = INVALID;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum FlyCtrlCmd {
        INIT(1),
        TAKEOFF(2),
        LAND(3),
        OTHER(100);
        
        private final int data;

        private FlyCtrlCmd(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FlyCtrlCmd find(int b) {
            FlyCtrlCmd result = INIT;
            FlyCtrlCmd[] values = values();
            for (FlyCtrlCmd tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum FlyGoHomeStaus {
        INIT((byte) 1),
        START((byte) 2),
        EXIT((byte) 3);
        
        private byte mData = 1;

        private FlyGoHomeStaus(byte data) {
            this.mData = data;
        }

        public byte value() {
            return this.mData;
        }

        public boolean _equals(int b) {
            return this.mData == b;
        }

        public static FlyGoHomeStaus find(int b) {
            FlyGoHomeStaus result = INIT;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
