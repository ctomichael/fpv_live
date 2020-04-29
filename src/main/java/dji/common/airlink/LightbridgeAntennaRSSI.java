package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class LightbridgeAntennaRSSI {
    private final int antenna1;
    private final int antenna2;

    public LightbridgeAntennaRSSI(int antenna12, int antenna22) {
        this.antenna1 = antenna12;
        this.antenna2 = antenna22;
    }

    public int getAntenna1() {
        return this.antenna1;
    }

    public int getAntenna2() {
        return this.antenna2;
    }

    public int hashCode() {
        return (this.antenna1 * 31) + this.antenna2;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof LightbridgeAntennaRSSI)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.antenna1 == ((LightbridgeAntennaRSSI) o).antenna1 && this.antenna2 == ((LightbridgeAntennaRSSI) o).antenna2;
    }
}
