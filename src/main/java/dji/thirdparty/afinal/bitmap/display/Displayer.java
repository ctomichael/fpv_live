package dji.thirdparty.afinal.bitmap.display;

import android.graphics.Bitmap;
import android.view.View;
import dji.thirdparty.afinal.bitmap.core.BitmapDisplayConfig;

public interface Displayer {
    void loadCompletedisplay(View view, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig);

    void loadFailDisplay(View view, Bitmap bitmap);
}
