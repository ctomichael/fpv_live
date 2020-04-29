package com.mapbox.geojson.shifter;

import com.mapbox.geojson.Point;
import java.util.List;

public interface CoordinateShifter {
    List<Double> shiftLonLat(double d, double d2);

    List<Double> shiftLonLatAlt(double d, double d2, double d3);

    List<Double> unshiftPoint(Point point);

    List<Double> unshiftPoint(List<Double> list);
}
