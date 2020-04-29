package com.google.gson.typeadapters;

import android.support.annotation.Keep;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Keep
public final class RuntimeTypeAdapterFactory<T> implements TypeAdapterFactory {
    /* access modifiers changed from: private */
    public final Class<?> baseType;
    private final Map<String, Class<?>> labelToSubtype = new LinkedHashMap();
    /* access modifiers changed from: private */
    public final boolean maintainType;
    /* access modifiers changed from: private */
    public final Map<Class<?>, String> subtypeToLabel = new LinkedHashMap();
    /* access modifiers changed from: private */
    public final String typeFieldName;

    private RuntimeTypeAdapterFactory(Class<?> baseType2, String typeFieldName2, boolean maintainType2) {
        if (typeFieldName2 == null || baseType2 == null) {
            throw new NullPointerException();
        }
        this.baseType = baseType2;
        this.typeFieldName = typeFieldName2;
        this.maintainType = maintainType2;
    }

    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType2, String typeFieldName2, boolean maintainType2) {
        return new RuntimeTypeAdapterFactory<>(baseType2, typeFieldName2, maintainType2);
    }

    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType2, String typeFieldName2) {
        return new RuntimeTypeAdapterFactory<>(baseType2, typeFieldName2, false);
    }

    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType2) {
        return new RuntimeTypeAdapterFactory<>(baseType2, "type", false);
    }

    public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> type, String label) {
        if (type == null || label == null) {
            throw new NullPointerException();
        } else if (this.subtypeToLabel.containsKey(type) || this.labelToSubtype.containsKey(label)) {
            throw new IllegalArgumentException("types and labels must be unique");
        } else {
            this.labelToSubtype.put(label, type);
            this.subtypeToLabel.put(type, label);
            return this;
        }
    }

    public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> type) {
        return registerSubtype(type, type.getSimpleName());
    }

    public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {
        if (type.getRawType() != this.baseType) {
            return null;
        }
        final Map<String, TypeAdapter<?>> labelToDelegate = new LinkedHashMap<>();
        final Map<Class<?>, TypeAdapter<?>> subtypeToDelegate = new LinkedHashMap<>();
        for (Map.Entry<String, Class<?>> entry : this.labelToSubtype.entrySet()) {
            TypeAdapter<?> delegate = gson.getDelegateAdapter(this, TypeToken.get((Class) entry.getValue()));
            labelToDelegate.put(entry.getKey(), delegate);
            subtypeToDelegate.put(entry.getValue(), delegate);
        }
        return new TypeAdapter<R>() {
            /* class com.google.gson.typeadapters.RuntimeTypeAdapterFactory.AnonymousClass1 */

            public R read(JsonReader in2) throws IOException {
                JsonElement labelJsonElement;
                JsonElement jsonElement = Streams.parse(in2);
                if (RuntimeTypeAdapterFactory.this.maintainType) {
                    labelJsonElement = jsonElement.getAsJsonObject().get(RuntimeTypeAdapterFactory.this.typeFieldName);
                } else {
                    labelJsonElement = jsonElement.getAsJsonObject().remove(RuntimeTypeAdapterFactory.this.typeFieldName);
                }
                if (labelJsonElement == null) {
                    throw new JsonParseException("cannot deserialize " + RuntimeTypeAdapterFactory.this.baseType + " because it does not define a field named " + RuntimeTypeAdapterFactory.this.typeFieldName);
                }
                String label = labelJsonElement.getAsString();
                TypeAdapter<R> delegate = (TypeAdapter) labelToDelegate.get(label);
                if (delegate != null) {
                    return delegate.fromJsonTree(jsonElement);
                }
                throw new JsonParseException("cannot deserialize " + RuntimeTypeAdapterFactory.this.baseType + " subtype named " + label + "; did you forget to register a subtype?");
            }

            public void write(JsonWriter out, R value) throws IOException {
                Class<?> srcType = value.getClass();
                TypeAdapter<R> delegate = (TypeAdapter) subtypeToDelegate.get(srcType);
                if (delegate == null) {
                    throw new JsonParseException("cannot serialize " + srcType.getName() + "; did you forget to register a subtype?");
                }
                JsonObject jsonObject = delegate.toJsonTree(value).getAsJsonObject();
                if (RuntimeTypeAdapterFactory.this.maintainType) {
                    Streams.write(jsonObject, out);
                    return;
                }
                JsonObject clone = new JsonObject();
                if (jsonObject.has(RuntimeTypeAdapterFactory.this.typeFieldName)) {
                    throw new JsonParseException("cannot serialize " + srcType.getName() + " because it already defines a field named " + RuntimeTypeAdapterFactory.this.typeFieldName);
                }
                clone.add(RuntimeTypeAdapterFactory.this.typeFieldName, new JsonPrimitive((String) RuntimeTypeAdapterFactory.this.subtypeToLabel.get(srcType)));
                for (Map.Entry<String, JsonElement> e : jsonObject.entrySet()) {
                    clone.add((String) e.getKey(), (JsonElement) e.getValue());
                }
                Streams.write(clone, out);
            }
        }.nullSafe();
    }
}
