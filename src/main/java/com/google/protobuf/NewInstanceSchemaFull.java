package com.google.protobuf;

import com.google.protobuf.GeneratedMessageV3;

final class NewInstanceSchemaFull implements NewInstanceSchema {
    NewInstanceSchemaFull() {
    }

    public Object newInstance(Object defaultInstance) {
        return ((GeneratedMessageV3) defaultInstance).newInstance(GeneratedMessageV3.UnusedPrivateParameter.INSTANCE);
    }
}
