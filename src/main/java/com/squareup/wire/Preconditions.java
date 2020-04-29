package com.squareup.wire;

final class Preconditions {
    private Preconditions() {
    }

    static void checkNotNull(Object o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
    }
}
