package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ObjectTypeAdapter extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        /* class com.google.gson.internal.bind.ObjectTypeAdapter.AnonymousClass1 */

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() == Object.class) {
                return new ObjectTypeAdapter(gson);
            }
            return null;
        }
    };
    private final Gson gson;

    ObjectTypeAdapter(Gson gson2) {
        this.gson = gson2;
    }

    public Object read(JsonReader in2) throws IOException {
        switch (in2.peek()) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<>();
                in2.beginArray();
                while (in2.hasNext()) {
                    list.add(read(in2));
                }
                in2.endArray();
                return list;
            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<>();
                in2.beginObject();
                while (in2.hasNext()) {
                    map.put(in2.nextName(), read(in2));
                }
                in2.endObject();
                return map;
            case STRING:
                return in2.nextString();
            case NUMBER:
                return Double.valueOf(in2.nextDouble());
            case BOOLEAN:
                return Boolean.valueOf(in2.nextBoolean());
            case NULL:
                in2.nextNull();
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    public void write(JsonWriter out, Object value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        TypeAdapter<Object> typeAdapter = this.gson.getAdapter(value.getClass());
        if (typeAdapter instanceof ObjectTypeAdapter) {
            out.beginObject();
            out.endObject();
            return;
        }
        typeAdapter.write(out, value);
    }
}
