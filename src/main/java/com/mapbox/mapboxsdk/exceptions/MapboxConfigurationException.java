package com.mapbox.mapboxsdk.exceptions;

import android.support.annotation.NonNull;

public class MapboxConfigurationException extends RuntimeException {
    public MapboxConfigurationException() {
        super("\nUsing MapView requires calling Mapbox.getInstance(Context context, String accessToken) before inflating or creating the view. The access token parameter is required when using a Mapbox service.\nPlease see https://www.mapbox.com/help/create-api-access-token/ to learn how to create one.\nMore information in this guide https://www.mapbox.com/help/first-steps-android-sdk/#access-tokens.");
    }

    public MapboxConfigurationException(@NonNull String message) {
        super(message);
    }
}
