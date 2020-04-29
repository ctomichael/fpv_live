package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataAppUIOperation;
import dji.midware.data.model.P3.DataFlycSetHomePoint;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataAppUIOperateSetHome extends DataBase {
    public HomeSourceFrom mHomeSourceFrom = HomeSourceFrom.From_Settings;
    public DataFlycSetHomePoint.HOMETYPE mHomeType = DataFlycSetHomePoint.HOMETYPE.AIRCRAFT;
    public byte mInterval = 0;
    public double mLantitude = 0.0d;
    public double mLongtitude = 0.0d;

    public DataAppUIOperateSetHome(boolean isRegist) {
        super(isRegist);
    }

    public DataAppUIOperateSetHome setSourceFrom(HomeSourceFrom from) {
        this.mHomeSourceFrom = from;
        return this;
    }

    public DataAppUIOperateSetHome setHomeType(DataFlycSetHomePoint.HOMETYPE type) {
        this.mHomeType = type;
        return this;
    }

    public DataAppUIOperateSetHome setGpsInfo(double lantitude, double longtitude) {
        this.mLantitude = lantitude;
        this.mLongtitude = longtitude;
        return this;
    }

    public DataAppUIOperateSetHome setInterval(byte interval) {
        this.mInterval = interval;
        return this;
    }

    public byte[] getRecData() {
        byte[] buffer = new byte[20];
        buffer[0] = (byte) DataAppUIOperation.AppUIOperateSubType.AppSetHomePoint.value();
        buffer[1] = (byte) this.mHomeSourceFrom.value();
        buffer[2] = this.mHomeType.value();
        System.arraycopy(BytesUtil.getBytes(this.mLantitude), 0, buffer, 3, 8);
        System.arraycopy(BytesUtil.getBytes(this.mLongtitude), 0, buffer, 11, 8);
        buffer[19] = this.mInterval;
        return buffer;
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        this.mHomeSourceFrom = HomeSourceFrom.find(((Integer) get(1, 1, Integer.class)).intValue());
        this.mHomeType = DataFlycSetHomePoint.HOMETYPE.ofValue(((Byte) get(2, 1, Byte.class)).byteValue());
        this.mLantitude = ((Double) get(3, 8, Double.class)).doubleValue();
        this.mLongtitude = ((Double) get(11, 8, Double.class)).doubleValue();
        this.mInterval = ((Byte) get(19, 1, Byte.class)).byteValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DataAppUIOperation.AppUIOperateSubType.AppSetHomePoint).append(";");
        sb.append(this.mHomeSourceFrom).append(";");
        sb.append(this.mHomeType).append(";");
        sb.append(this.mLantitude).append(";");
        sb.append(this.mLongtitude).append(";");
        sb.append((int) this.mInterval).append(";");
        return sb.toString();
    }

    @Keep
    public enum HomeSourceFrom {
        From_Settings(0),
        From_LeftMenu(1),
        From_RTHDialog(2),
        OTHER(100);
        
        private static HomeSourceFrom[] _values = null;
        private final int data;

        private HomeSourceFrom(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static HomeSourceFrom find(int b) {
            HomeSourceFrom result = From_Settings;
            if (_values == null) {
                _values = values();
            }
            HomeSourceFrom[] homeSourceFromArr = _values;
            for (HomeSourceFrom tmp : homeSourceFromArr) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
