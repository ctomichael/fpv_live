package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FrequencyInterference {
    public float frequencyFrom;
    public float frequencyTo;
    public byte rssi;

    public FrequencyInterference(float from, float to, byte rssi2) {
        this.frequencyFrom = from;
        this.frequencyTo = to;
        this.rssi = rssi2;
    }

    public int hashCode() {
        return (((Float.floatToIntBits(this.frequencyFrom) * 31) + Float.floatToIntBits(this.frequencyTo)) * 31) + this.rssi;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof FrequencyInterference)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.frequencyFrom == ((FrequencyInterference) o).frequencyFrom && this.frequencyTo == ((FrequencyInterference) o).frequencyTo && this.rssi == ((FrequencyInterference) o).rssi;
    }
}
