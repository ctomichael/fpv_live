package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class SDRHdOffsetParams {
    public int pathLossOffset;
    public int rcLinkOffset;
    public int txPowerOffset;

    public SDRHdOffsetParams(int _pathOff, int _rcOff, int _txPOff) {
        this.pathLossOffset = _pathOff;
        this.rcLinkOffset = _rcOff;
        this.txPowerOffset = _txPOff;
    }

    public int hashCode() {
        return (((this.pathLossOffset * 31) + this.rcLinkOffset) * 31) + this.txPowerOffset;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof SDRHdOffsetParams)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.pathLossOffset == ((SDRHdOffsetParams) o).pathLossOffset && this.rcLinkOffset == ((SDRHdOffsetParams) o).rcLinkOffset && this.txPowerOffset == ((SDRHdOffsetParams) o).txPowerOffset;
    }
}
