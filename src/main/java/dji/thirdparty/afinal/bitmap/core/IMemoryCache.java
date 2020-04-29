package dji.thirdparty.afinal.bitmap.core;

import android.graphics.Bitmap;

public interface IMemoryCache {
    void evictAll();

    Bitmap get(String str);

    void put(String str, Bitmap bitmap);

    void remove(String str);
}
