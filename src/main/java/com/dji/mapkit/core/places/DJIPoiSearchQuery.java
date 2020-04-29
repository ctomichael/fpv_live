package com.dji.mapkit.core.places;

import android.support.annotation.NonNull;

public class DJIPoiSearchQuery {
    private final String keyWord;

    public DJIPoiSearchQuery(Builder builder) {
        this.keyWord = builder.keyWord;
    }

    public String keyWord() {
        return this.keyWord;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public String keyWord = "";

        public Builder keyWord(@NonNull String keyWord2) {
            if (keyWord2 == null) {
                throw new IllegalArgumentException("key word can not be set to null");
            }
            this.keyWord = keyWord2;
            return this;
        }

        public DJIPoiSearchQuery build() {
            return new DJIPoiSearchQuery(this);
        }
    }
}
