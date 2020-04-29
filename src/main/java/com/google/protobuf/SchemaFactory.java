package com.google.protobuf;

interface SchemaFactory {
    <T> Schema<T> createSchema(Class<T> cls);
}
