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
class ListOfListOfPointCoordinatesTypeAdapter extends BaseCoordinatesTypeAdapter<List<List<Point>>> {
    public /* bridge */ /* synthetic */ void write(JsonWriter jsonWriter, Object obj) throws IOException {
        write(jsonWriter, (List<List<Point>>) ((List) obj));
    }

    ListOfListOfPointCoordinatesTypeAdapter() {
    }

    public void write(JsonWriter out, List<List<Point>> points) throws IOException {
        if (points == null) {
            out.nullValue();
            return;
        }
        out.beginArray();
        for (List<Point> listOfPoints : points) {
            out.beginArray();
            for (Point point : listOfPoints) {
                writePoint(out, point);
            }
            out.endArray();
        }
        out.endArray();
    }

    public List<List<Point>> read(JsonReader in2) throws IOException {
        if (in2.peek() == JsonToken.NULL) {
            throw new NullPointerException();
        } else if (in2.peek() == JsonToken.BEGIN_ARRAY) {
            in2.beginArray();
            List<List<Point>> points = new ArrayList<>();
            while (in2.peek() == JsonToken.BEGIN_ARRAY) {
                in2.beginArray();
                List<Point> listOfPoints = new ArrayList<>();
                while (in2.peek() == JsonToken.BEGIN_ARRAY) {
                    listOfPoints.add(readPoint(in2));
                }
                in2.endArray();
                points.add(listOfPoints);
            }
            in2.endArray();
            return points;
        } else {
            throw new GeoJsonException("coordinates should be array of array of array of double");
        }
    }
}
