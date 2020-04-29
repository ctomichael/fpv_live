package com.mapbox.geojson;

import android.support.annotation.Keep;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.List;

@Keep
class ListOfDoublesCoordinatesTypeAdapter extends BaseCoordinatesTypeAdapter<List<Double>> {
    public /* bridge */ /* synthetic */ void write(JsonWriter jsonWriter, Object obj) throws IOException {
        write(jsonWriter, (List<Double>) ((List) obj));
    }

    ListOfDoublesCoordinatesTypeAdapter() {
    }

    public void write(JsonWriter out, List<Double> value) throws IOException {
        writePointList(out, value);
    }

    public List<Double> read(JsonReader in2) throws IOException {
        return readPointList(in2);
    }
}
