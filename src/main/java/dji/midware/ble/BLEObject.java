package dji.midware.ble;

import dji.midware.ble.BLE;

public class BLEObject implements Comparable {
    private static final String TAG = "BLEObject";
    public String address;
    public BLE.BLEEvent event;
    public boolean isBonded;
    public boolean isOnGimbal;
    public String name;
    public int rssi;

    public boolean equals(Object o) {
        if (o instanceof String) {
            return this.address.equals(o);
        }
        if (o instanceof BLEObject) {
            return this.address.equals(((BLEObject) o).address);
        }
        return false;
    }

    public int hashCode() {
        return this.address.hashCode();
    }

    public int compareTo(Object another) {
        if (!(another instanceof BLEObject)) {
            return 0;
        }
        return Integer.valueOf(((BLEObject) another).rssi).compareTo(Integer.valueOf(this.rssi));
    }

    public String toString() {
        return "BLEObject{address='" + this.address + '\'' + ", name='" + this.name + '\'' + ", rssi=" + this.rssi + ", isOnGimbal=" + this.isOnGimbal + ", isBonded=" + this.isBonded + ", event=" + this.event + '}';
    }
}
