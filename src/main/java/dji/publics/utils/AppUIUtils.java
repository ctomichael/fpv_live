package dji.publics.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Window;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import dji.fieldAnnotation.EXClassNullAway;
import java.lang.reflect.Method;

@EXClassNullAway
public class AppUIUtils {
    private static final int MAX_BITMAP_CACHE = 31457280;

    public static void add(Activity activity) {
        try {
            Class c = Class.forName("com.android.debug.hv.ViewServer");
            Method getMethod = c.getMethod("get", Context.class);
            c.getMethod("addWindow", Activity.class).invoke(getMethod.invoke(c, activity), activity);
        } catch (Exception e) {
        }
    }

    public static void remove(Activity activity) {
        try {
            Class c = Class.forName("com.android.debug.hv.ViewServer");
            Method getMethod = c.getMethod("get", Context.class);
            c.getMethod("removeWindow", Activity.class).invoke(getMethod.invoke(c, activity), activity);
        } catch (Exception e) {
        }
    }

    public static void focus(Activity activity) {
        try {
            Class c = Class.forName("com.android.debug.hv.ViewServer");
            Method getMethod = c.getMethod("get", Context.class);
            c.getMethod("setFocusedWindow", Activity.class).invoke(getMethod.invoke(c, activity), activity);
        } catch (Exception e) {
        }
    }

    public static void initImageLoader(Context context) {
        if (!ImageLoader.getInstance().isInited()) {
            DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).displayer(new FadeInBitmapDisplayer(200)).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
            int cache = (int) (((float) Runtime.getRuntime().maxMemory()) * 0.16f);
            if (cache > MAX_BITMAP_CACHE) {
                cache = MAX_BITMAP_CACHE;
            }
            ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(context).threadPoolSize(3).denyCacheImageMultipleSizesInMemory().discCacheSize(MAX_BITMAP_CACHE).memoryCacheSize(cache).discCacheFileNameGenerator(new Md5FileNameGenerator()).defaultDisplayImageOptions(options).build());
        }
    }

    public static void setFullWindowsMode(Window windows) {
        windows.getDecorView().setSystemUiVisibility(7942);
    }
}
