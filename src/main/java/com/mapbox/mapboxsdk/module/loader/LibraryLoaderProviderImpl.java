package com.mapbox.mapboxsdk.module.loader;

import com.facebook.soloader.SoLoader;
import com.mapbox.mapboxsdk.LibraryLoader;
import com.mapbox.mapboxsdk.LibraryLoaderProvider;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.exceptions.MapboxConfigurationException;
import com.mapbox.mapboxsdk.log.Logger;

public class LibraryLoaderProviderImpl implements LibraryLoaderProvider {
    public LibraryLoader getDefaultLibraryLoader() {
        return new SoLibraryLoader();
    }

    private static class SoLibraryLoader extends LibraryLoader {
        private static final String TAG = "SoLibraryLoader";

        private SoLibraryLoader() {
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.facebook.soloader.SoLoader.init(android.content.Context, boolean):void
         arg types: [android.content.Context, int]
         candidates:
          com.facebook.soloader.SoLoader.init(android.content.Context, int):void
          com.facebook.soloader.SoLoader.init(android.content.Context, boolean):void */
        public void load(String name) {
            try {
                SoLoader.init(Mapbox.getApplicationContext(), false);
                SoLoader.loadLibrary(name);
            } catch (MapboxConfigurationException e) {
                Logger.e(TAG, "Couldn't load so file with relinker, application context missing, call Mapbox.getInstance(Context context, String accessToken) first");
            }
        }
    }
}
