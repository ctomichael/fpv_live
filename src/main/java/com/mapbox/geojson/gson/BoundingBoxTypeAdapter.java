package com.mapbox.geojson.gson;

import android.support.annotation.Keep;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.exception.GeoJsonException;
import com.mapbox.geojson.shifter.CoordinateShifterManager;
import com.mapbox.geojson.utils.GeoJsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Keep
public class BoundingBoxTypeAdapter extends TypeAdapter<BoundingBox> {
    public void write(JsonWriter out, BoundingBox value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginArray();
        Point point = value.southwest();
        List<Double> unshiftedCoordinates = CoordinateShifterManager.getCoordinateShifter().unshiftPoint(point);
        out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(0).doubleValue()));
        out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(1).doubleValue()));
        if (point.hasAltitude()) {
            out.value(unshiftedCoordinates.get(2));
        }
        Point point2 = value.northeast();
        List<Double> unshiftedCoordinates2 = CoordinateShifterManager.getCoordinateShifter().unshiftPoint(point2);
        out.value(GeoJsonUtils.trim(unshiftedCoordinates2.get(0).doubleValue()));
        out.value(GeoJsonUtils.trim(unshiftedCoordinates2.get(1).doubleValue()));
        if (point2.hasAltitude()) {
            out.value(unshiftedCoordinates2.get(2));
        }
        out.endArray();
    }

    public BoundingBox read(JsonReader in2) throws IOException {
        List<Double> rawCoordinates = new ArrayList<>();
        in2.beginArray();
        while (in2.hasNext()) {
            rawCoordinates.add(Double.valueOf(in2.nextDouble()));
        }
        in2.endArray();
        if (rawCoordinates.size() == 6) {
            return BoundingBox.fromLngLats(((Double) rawCoordinates.get(0)).doubleValue(), ((Double) rawCoordinates.get(1)).doubleValue(), ((Double) rawCoordinates.get(2)).doubleValue(), ((Double) rawCoordinates.get(3)).doubleValue(), ((Double) rawCoordinates.get(4)).doubleValue(), ((Double) rawCoordinates.get(5)).doubleValue());
        }
        if (rawCoordinates.size() == 4) {
            return BoundingBox.fromLngLats(((Double) rawCoordinates.get(0)).doubleValue(), ((Double) rawCoordinates.get(1)).doubleValue(), ((Double) rawCoordinates.get(2)).doubleValue(), ((Double) rawCoordinates.get(3)).doubleValue());
        }
        throw new GeoJsonException("The value of the bbox member MUST be an array of length 2*n where n is the number of dimensions represented in the contained geometries,with all axes of the most southwesterly point followed  by all axes of the more northeasterly point. The axes order of a bbox follows the axes order of geometries.");
    }
}
