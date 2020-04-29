package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public final class JsonAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor2) {
        this.constructorConstructor = constructorConstructor2;
    }

    /* JADX WARN: Type inference failed for: r5v0, types: [com.google.gson.reflect.TypeToken<T>, com.google.gson.reflect.TypeToken], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> com.google.gson.TypeAdapter<T> create(com.google.gson.Gson r4, com.google.gson.reflect.TypeToken<T> r5) {
        /*
            r3 = this;
            java.lang.Class r1 = r5.getRawType()
            java.lang.Class<com.google.gson.annotations.JsonAdapter> r2 = com.google.gson.annotations.JsonAdapter.class
            java.lang.annotation.Annotation r0 = r1.getAnnotation(r2)
            com.google.gson.annotations.JsonAdapter r0 = (com.google.gson.annotations.JsonAdapter) r0
            if (r0 != 0) goto L_0x0010
            r2 = 0
        L_0x000f:
            return r2
        L_0x0010:
            com.google.gson.internal.ConstructorConstructor r2 = r3.constructorConstructor
            com.google.gson.TypeAdapter r2 = r3.getTypeAdapter(r2, r4, r5, r0)
            goto L_0x000f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.create(com.google.gson.Gson, com.google.gson.reflect.TypeToken):com.google.gson.TypeAdapter");
    }

    /* access modifiers changed from: package-private */
    public TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor2, Gson gson, TypeToken<?> type, JsonAdapter annotation) {
        JsonSerializer<?> serializer;
        JsonDeserializer<?> deserializer;
        TypeAdapter<?> typeAdapter;
        Object instance = constructorConstructor2.get(TypeToken.get((Class) annotation.value())).construct();
        if (instance instanceof TypeAdapter) {
            typeAdapter = (TypeAdapter) instance;
        } else if (instance instanceof TypeAdapterFactory) {
            typeAdapter = ((TypeAdapterFactory) instance).create(gson, type);
        } else if ((instance instanceof JsonSerializer) || (instance instanceof JsonDeserializer)) {
            if (instance instanceof JsonSerializer) {
                serializer = (JsonSerializer) instance;
            } else {
                serializer = null;
            }
            if (instance instanceof JsonDeserializer) {
                deserializer = (JsonDeserializer) instance;
            } else {
                deserializer = null;
            }
            typeAdapter = new TreeTypeAdapter<>(serializer, deserializer, gson, type, null);
        } else {
            throw new IllegalArgumentException("Invalid attempt to bind an instance of " + instance.getClass().getName() + " as a @JsonAdapter for " + type.toString() + ". @JsonAdapter value must be a TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer.");
        }
        if (typeAdapter == null || !annotation.nullSafe()) {
            return typeAdapter;
        }
        return typeAdapter.nullSafe();
    }
}
