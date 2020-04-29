package com.mapbox.mapboxsdk;

import com.mapbox.mapboxsdk.log.Logger;

public abstract class LibraryLoader {
    private static final LibraryLoader DEFAULT = Mapbox.getModuleProvider().createLibraryLoaderProvider().getDefaultLibraryLoader();
    private static final String TAG = "Mbgl-LibraryLoader";
    private static boolean loaded;
    private static volatile LibraryLoader loader = DEFAULT;

    public abstract void load(String str);

    public static void setLibraryLoader(LibraryLoader libraryLoader) {
        loader = libraryLoader;
    }

    public static void load() {
        try {
            if (!loaded) {
                loaded = true;
                loader.load("mapbox-gl");
            }
        } catch (UnsatisfiedLinkError error) {
            loaded = false;
            Logger.e(TAG, "Failed to load native shared library.", error);
            MapStrictMode.strictModeViolation("Failed to load native shared library.", error);
        }
    }
}
