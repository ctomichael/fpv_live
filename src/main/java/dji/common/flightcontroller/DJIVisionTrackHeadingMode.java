package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum DJIVisionTrackHeadingMode {
    FOLLOW(0),
    FORWARD(1),
    OTHER(100);
    
    private final int data;

    private DJIVisionTrackHeadingMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static DJIVisionTrackHeadingMode find(int b) {
        DJIVisionTrackHeadingMode result = FOLLOW;
        DJIVisionTrackHeadingMode[] values = values();
        for (DJIVisionTrackHeadingMode tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
