package com.google.protobuf;

public interface OptionOrBuilder extends MessageOrBuilder {
    String getName();

    ByteString getNameBytes();

    Any getValue();

    AnyOrBuilder getValueOrBuilder();

    boolean hasValue();
}
