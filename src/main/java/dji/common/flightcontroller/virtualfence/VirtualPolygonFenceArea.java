package dji.common.flightcontroller.virtualfence;

import dji.common.model.LocationCoordinate2D;
import java.util.ArrayList;

public class VirtualPolygonFenceArea {
    private ArrayList<LocationCoordinate2D> coordinatePoints;
    private int totalPointsCount = 0;

    public VirtualPolygonFenceArea(int totalPointsCount2, ArrayList<LocationCoordinate2D> coordinatePoints2) {
        this.totalPointsCount = totalPointsCount2;
        this.coordinatePoints = coordinatePoints2;
    }

    public int getTotalPointsCount() {
        return this.totalPointsCount;
    }

    public ArrayList<LocationCoordinate2D> getCoordinatePoints() {
        return this.coordinatePoints;
    }
}
