package com.dji.mapkit.core.models;

import android.graphics.Bitmap;
import android.view.View;
import com.dji.mapkit.core.models.DJIBitmapDescriptor;

public class DJIBitmapDescriptorFactory {
    private static final DJIBitmapDescriptor DEFAULT_MARKER = new DJIBitmapDescriptor((Bitmap) null);

    private DJIBitmapDescriptorFactory() {
    }

    public static DJIBitmapDescriptor defaultMarker() {
        return DEFAULT_MARKER;
    }

    public static DJIBitmapDescriptor fromAsset(String assetName) {
        return new DJIBitmapDescriptor(assetName, DJIBitmapDescriptor.Type.PATH_ASSET);
    }

    public static DJIBitmapDescriptor fromBitmap(Bitmap image) {
        return new DJIBitmapDescriptor(image);
    }

    public static DJIBitmapDescriptor fromFile(String fileName) {
        return new DJIBitmapDescriptor(fileName, DJIBitmapDescriptor.Type.PATH_FILEINPUT);
    }

    public static DJIBitmapDescriptor fromPath(String path) {
        return new DJIBitmapDescriptor(path, DJIBitmapDescriptor.Type.PATH_ABSOLUTE);
    }

    public static DJIBitmapDescriptor fromResource(int resourceId) {
        return new DJIBitmapDescriptor(resourceId);
    }

    public static DJIBitmapDescriptor fromView(View view) {
        return new DJIBitmapDescriptor(view);
    }
}
