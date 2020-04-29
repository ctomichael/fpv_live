package com.mapbox.android.telemetry;

import com.google.gson.annotations.SerializedName;
import dji.publics.protocol.ResponseBase;

class TelemetryResponse {
    @SerializedName(ResponseBase.STRING_MESSAGE)
    private final String message;

    TelemetryResponse(String message2) {
        this.message = message2;
    }

    public boolean equals(Object response) {
        if (this == response) {
            return true;
        }
        if (response == null || getClass() != response.getClass()) {
            return false;
        }
        TelemetryResponse otherResponse = (TelemetryResponse) response;
        if (this.message != null) {
            return this.message.equals(otherResponse.message);
        }
        if (otherResponse.message != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.message != null) {
            return this.message.hashCode();
        }
        return 0;
    }
}
