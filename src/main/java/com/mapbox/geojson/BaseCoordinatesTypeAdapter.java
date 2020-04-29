package com.mapbox.geojson;

import android.support.annotation.Keep;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.exception.GeoJsonException;
import com.mapbox.geojson.shifter.CoordinateShifterManager;
import com.mapbox.geojson.utils.GeoJsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Keep
abstract class BaseCoordinatesTypeAdapter<T> extends TypeAdapter<T> {
    BaseCoordinatesTypeAdapter() {
    }

    /* access modifiers changed from: protected */
    public void writePoint(JsonWriter out, Point value) throws IOException {
        writePointList(out, value.coordinates());
    }

    /* access modifiers changed from: protected */
    public Point readPoint(JsonReader in2) throws IOException {
        List<Double> coordinates = readPointList(in2);
        if (coordinates != null && coordinates.size() > 1) {
            return new Point("Point", null, coordinates);
        }
        throw new GeoJsonException(" Point coordinates should be non-null double array");
    }

    /* access modifiers changed from: protected */
    public void writePointList(JsonWriter out, List<Double> value) throws IOException {
        if (value != null) {
            out.beginArray();
            List<Double> unshiftedCoordinates = CoordinateShifterManager.getCoordinateShifter().unshiftPoint(value);
            out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(0).doubleValue()));
            out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(1).doubleValue()));
            if (value.size() > 2) {
                out.value(unshiftedCoordinates.get(2));
            }
            out.endArray();
        }
    }

    /* access modifiers changed from: protected */
    public List<Double> readPointList(JsonReader in2) throws IOException {
        if (in2.peek() == JsonToken.NULL) {
            throw new NullPointerException();
        }
        List<Double> coordinates = new ArrayList<>();
        in2.beginArray();
        while (in2.hasNext()) {
            coordinates.add(Double.valueOf(in2.nextDouble()));
        }
        in2.endArray();
        if (coordinates.size() > 2) {
            return CoordinateShifterManager.getCoordinateShifter().shiftLonLatAlt(((Double) coordinates.get(0)).doubleValue(), ((Double) coordinates.get(1)).doubleValue(), ((Double) coordinates.get(2)).doubleValue());
        }
        return CoordinateShifterManager.getCoordinateShifter().shiftLonLat(((Double) coordinates.get(0)).doubleValue(), ((Double) coordinates.get(1)).doubleValue());
    }
}
