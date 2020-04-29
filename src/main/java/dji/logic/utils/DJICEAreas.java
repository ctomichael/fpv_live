package dji.logic.utils;

import android.location.Location;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataWifiSetPowerMode;
import dji.midware.interfaces.DJIDataCallBack;
import java.util.ArrayList;
import java.util.Iterator;

@EXClassNullAway
public class DJICEAreas {
    private static ArrayList<CEArea> areas = new ArrayList<>();

    private static class CEArea {
        protected double latitude;
        protected double longtitude;
        protected int radius;

        public CEArea(double latitude2, double longtitude2, int radius2) {
            this.latitude = latitude2;
            this.longtitude = longtitude2;
            this.radius = radius2;
        }
    }

    static {
        areas.add(new CEArea(55.529627d, 15.702531d, 2824707));
        areas.add(new CEArea(-29.63077d, 24.718358d, 1038280));
        areas.add(new CEArea(18.20013d, 78.10576d, 1585814));
        areas.add(new CEArea(24.41194d, 93.17285d, 1044521));
        areas.add(new CEArea(-23.950101d, 135.518546d, 4285467));
        areas.add(new CEArea(35.628727d, 129.950682d, 643505));
    }

    public static boolean isInCEArea(double latitude, double longtitude) {
        float[] results = new float[3];
        Iterator<CEArea> it2 = areas.iterator();
        while (it2.hasNext()) {
            CEArea area = it2.next();
            Location.distanceBetween(area.latitude, area.longtitude, latitude, longtitude, results);
            if (results[0] <= ((float) area.radius)) {
                return true;
            }
        }
        return false;
    }

    public static void startSetArea(boolean isInCEArea, DJIDataCallBack callBack) {
        DataWifiSetPowerMode.DJIWifiPowerMode mode;
        if (isInCEArea) {
            mode = DataWifiSetPowerMode.DJIWifiPowerMode.CE;
        } else {
            mode = DataWifiSetPowerMode.DJIWifiPowerMode.FCC;
        }
        DataWifiSetPowerMode.getInstance().setMode(mode).start(callBack);
    }
}
