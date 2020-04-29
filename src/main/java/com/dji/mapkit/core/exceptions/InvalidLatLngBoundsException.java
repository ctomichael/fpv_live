package com.dji.mapkit.core.exceptions;

public class InvalidLatLngBoundsException extends RuntimeException {
    public InvalidLatLngBoundsException(int latLngsListSize) {
        super("Cannot create a DJILatLngBounds from " + latLngsListSize + " items");
    }
}
