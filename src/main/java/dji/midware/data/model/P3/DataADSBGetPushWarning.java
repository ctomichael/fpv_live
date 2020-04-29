package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataADSBGetPushWarning extends DataBase {
    private static DataADSBGetPushWarning mInstance = null;
    private boolean isConnectAdsb = false;
    private ArrayList<FlightItem> list = new ArrayList<>();
    private DJIWarningType warningType = DJIWarningType.None;

    public static DataADSBGetPushWarning getInstance() {
        if (mInstance == null) {
            mInstance = new DataADSBGetPushWarning();
        }
        return mInstance;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public boolean isConnectAdsb() {
        return isGetted() && this.isConnectAdsb;
    }

    public DJIWarningType getWarningType() {
        return this.warningType;
    }

    public ArrayList<FlightItem> getList() {
        return this.list;
    }

    @Keep
    public static class FlightItem {
        public String ICAOAddress;
        public float altitude;
        public int distance;
        public float heading;
        public double latitude;
        public double longitude;
        public int remainTime;
        public float speed;
        public DJIWarningType warningLevel;

        public String getTreatedIcao() {
            if (this.ICAOAddress == null || this.ICAOAddress.equals("")) {
                return this.ICAOAddress;
            }
            String result1 = this.ICAOAddress.substring(0, 1);
            return result1 + "***" + this.ICAOAddress.substring(4);
        }
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        boolean z;
        if (isProductSupportADSBWarning(DJIProductManager.getInstance().getType())) {
            super.setPushRecData(data);
            this.list.clear();
            if (data != null) {
                if ((this._recData[0] & 1) == 1) {
                    z = true;
                } else {
                    z = false;
                }
                this.isConnectAdsb = z;
                this.warningType = DJIWarningType.find((this._recData[0] & 14) >> 1);
                byte b = this._recData[1];
                for (int i = 0; i < b; i++) {
                    int append = i * 26;
                    FlightItem item = new FlightItem();
                    item.ICAOAddress = getUTF8(append + 2, 7);
                    item.warningLevel = DJIWarningType.find(this._recData[append + 9]);
                    item.latitude = (((double) ((Integer) get(append + 10, 4, Integer.class)).intValue()) * 1.0d) / 1000000.0d;
                    item.longitude = (((double) ((Integer) get(append + 14, 4, Integer.class)).intValue()) * 1.0d) / 1000000.0d;
                    item.heading = (((float) ((Integer) get(append + 18, 2, Integer.class)).intValue()) * 1.0f) / 10.0f;
                    item.altitude = (float) ((Short) get(append + 20, 2, Short.class)).shortValue();
                    item.speed = (float) ((Integer) get(append + 22, 2, Integer.class)).intValue();
                    item.remainTime = ((Integer) get(append + 24, 2, Integer.class)).intValue();
                    item.distance = ((Integer) get(append + 26, 2, Integer.class)).intValue();
                    this.list.add(item);
                }
            }
        }
    }

    private boolean isProductSupportADSBWarning(ProductType type) {
        return type == ProductType.M200 || type == ProductType.M210 || type == ProductType.M210RTK || type == ProductType.WM245 || type == ProductType.PM420 || type == ProductType.PM420PRO || type == ProductType.PM420PRO_RTK;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum DJIWarningType {
        None(0),
        First(1),
        Second(2),
        Three(3),
        Four(4),
        OTHER(100);
        
        private int data;

        private DJIWarningType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJIWarningType find(int b) {
            DJIWarningType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
