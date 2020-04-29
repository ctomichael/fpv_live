package com.mapbox.geojson.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.shifter.CoordinateShifterManager;
import com.mapbox.geojson.utils.GeoJsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class CoordinateTypeAdapter extends TypeAdapter<List<Double>> {
    public /* bridge */ /* synthetic */ void write(JsonWriter jsonWriter, Object obj) throws IOException {
        write(jsonWriter, (List<Double>) ((List) obj));
    }

    public void write(JsonWriter out, List<Double> value) throws IOException {
        out.beginArray();
        List<Double> unshiftedCoordinates = CoordinateShifterManager.getCoordinateShifter().unshiftPoint(value);
        out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(0).doubleValue()));
        out.value(GeoJsonUtils.trim(unshiftedCoordinates.get(1).doubleValue()));
        if (value.size() > 2) {
            out.value(unshiftedCoordinates.get(2));
        }
        out.endArray();
    }

    public List<Double> read(JsonReader in2) throws IOException {
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
