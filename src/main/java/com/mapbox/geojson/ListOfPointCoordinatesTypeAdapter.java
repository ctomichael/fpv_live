package com.mapbox.geojson;

import android.support.annotation.Keep;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.exception.GeoJsonException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Keep
class ListOfPointCoordinatesTypeAdapter extends BaseCoordinatesTypeAdapter<List<Point>> {
    public /* bridge */ /* synthetic */ void write(JsonWriter jsonWriter, Object obj) throws IOException {
        write(jsonWriter, (List<Point>) ((List) obj));
    }

    ListOfPointCoordinatesTypeAdapter() {
    }

    public void write(JsonWriter out, List<Point> points) throws IOException {
        if (points == null) {
            out.nullValue();
            return;
        }
        out.beginArray();
        for (Point point : points) {
            writePoint(out, point);
        }
        out.endArray();
    }

    public List<Point> read(JsonReader in2) throws IOException {
        if (in2.peek() == JsonToken.NULL) {
            throw new NullPointerException();
        } else if (in2.peek() == JsonToken.BEGIN_ARRAY) {
            List<Point> points = new ArrayList<>();
            in2.beginArray();
            while (in2.peek() == JsonToken.BEGIN_ARRAY) {
                points.add(readPoint(in2));
            }
            in2.endArray();
            return points;
        } else {
            throw new GeoJsonException("coordinates should be non-null array of array of double");
        }
    }
}
