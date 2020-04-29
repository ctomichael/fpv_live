package dji.common.flightcontroller.flightassistant;

public class PoiTargetInformation {
    public float centerX;
    public float centerY;
    public float height;
    public PoiTargetState status;
    public float width;

    public PoiTargetInformation(int status2, float centerX2, float centerY2, float width2, float height2) {
        this.status = PoiTargetState.find(status2);
        this.centerX = centerX2;
        this.centerY = centerY2;
        this.width = width2;
        this.height = height2;
    }
}
