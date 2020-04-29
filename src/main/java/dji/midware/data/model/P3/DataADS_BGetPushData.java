package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataADS_BGetPushData extends DataBase {
    private static DataADS_BGetPushData mInstance = null;
    private ArrayList<ADS_BItem> list = new ArrayList<>();

    @Keep
    public static class ADS_BItem {
        public String ICAOAddress;
        public float altitude;
        public String callsign;
        public float hSpeed;
        public float heading;
        public ADS_B_HeightFrom heightFrom;
        public ADS_B_InfoFrom infoFrom;
        public double latitude;
        public double longitude;
        public float vSpeed;
    }

    public static DataADS_BGetPushData getInstance() {
        if (mInstance == null) {
            mInstance = new DataADS_BGetPushData();
        }
        return mInstance;
    }

    public ArrayList<ADS_BItem> getList() {
        return this.list;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
        this.list.clear();
        if (data != null) {
            int count = data.length / 41;
            for (int i = 0; i < count; i++) {
                int append = i * 41;
                ADS_BItem item = new ADS_BItem();
                item.heightFrom = ADS_B_HeightFrom.find((((Integer) get(append, 1, Integer.class)).intValue() >> 3) & 3);
                item.infoFrom = ADS_B_InfoFrom.find(((Integer) get(append, 1, Integer.class)).intValue() & 7);
                item.ICAOAddress = getUTF8(append + 1, 7);
                item.callsign = getUTF8(append + 8, 9).replace("_", "");
                item.latitude = (((double) ((Integer) get(append + 17, 4, Integer.class)).intValue()) * 1.0d) / 1000000.0d;
                item.longitude = (((double) ((Integer) get(append + 21, 4, Integer.class)).intValue()) * 1.0d) / 1000000.0d;
                item.altitude = (float) ((((double) ((Integer) get(append + 25, 4, Integer.class)).intValue()) * 1.0d) / 1000.0d);
                item.hSpeed = (float) ((((double) ((Integer) get(append + 29, 4, Integer.class)).intValue()) * 1.0d) / 1000.0d);
                item.vSpeed = (float) ((((double) ((Integer) get(append + 33, 4, Integer.class)).intValue()) * 1.0d) / 1000.0d);
                item.heading = (float) ((((double) ((Integer) get(append + 37, 4, Integer.class)).intValue()) * 1.0d) / 1000.0d);
                this.list.add(item);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum ADS_B_HeightFrom {
        Pressure(0),
        GNSS(1),
        OTHER(100);
        
        private int data;

        private ADS_B_HeightFrom(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ADS_B_HeightFrom find(int b) {
            ADS_B_HeightFrom result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum ADS_B_InfoFrom {
        ES1090(0),
        UAT(1),
        OTHER(100);
        
        private int data;

        private ADS_B_InfoFrom(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ADS_B_InfoFrom find(int b) {
            ADS_B_InfoFrom result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
