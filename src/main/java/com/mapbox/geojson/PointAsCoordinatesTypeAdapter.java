package com.mapbox.geojson;

import android.support.annotation.Keep;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

@Keep
public class PointAsCoordinatesTypeAdapter extends BaseCoordinatesTypeAdapter<Point> {
    public void write(JsonWriter out, Point value) throws IOException {
        writePoint(out, value);
    }

    public Point read(JsonReader in2) throws IOException {
        return readPoint(in2);
    }
}
