package com.mapbox.android.telemetry;

import okhttp3.MediaType;

class FileData {
    private final String filePath;
    private final MediaType type;

    FileData(String filePath2, MediaType type2) {
        this.filePath = filePath2;
        this.type = type2;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public MediaType getType() {
        return this.type;
    }
}
