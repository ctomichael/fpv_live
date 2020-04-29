package com.mapbox.mapboxsdk.utils;

import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import com.mapbox.mapboxsdk.MapStrictMode;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.log.Logger;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FontUtils {
    private static final List<String> DEFAULT_FONT_STACKS = new ArrayList();
    private static final String TAG = "Mbgl-FontUtils";
    private static final String TYPEFACE_FONTMAP_FIELD_NAME = "sSystemFontMap";

    static {
        DEFAULT_FONT_STACKS.add(MapboxConstants.DEFAULT_FONT);
        DEFAULT_FONT_STACKS.add("serif");
        DEFAULT_FONT_STACKS.add("monospace");
    }

    private FontUtils() {
    }

    public static String extractValidFont(String... fontNames) {
        List<String> validFonts;
        if (fontNames == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            validFonts = getDeviceFonts();
        } else {
            validFonts = DEFAULT_FONT_STACKS;
        }
        for (String fontName : fontNames) {
            if (validFonts.contains(fontName)) {
                return fontName;
            }
        }
        Logger.i(TAG, String.format("Couldn't map font family for local ideograph, using %s instead", MapboxConstants.DEFAULT_FONT));
        return MapboxConstants.DEFAULT_FONT;
    }

    @RequiresApi(21)
    private static List<String> getDeviceFonts() {
        List<String> fonts = new ArrayList<>();
        try {
            Typeface typeface = Typeface.create(Typeface.DEFAULT, 0);
            Field field = Typeface.class.getDeclaredField(TYPEFACE_FONTMAP_FIELD_NAME);
            field.setAccessible(true);
            fonts.addAll(((Map) field.get(typeface)).keySet());
        } catch (Exception exception) {
            Logger.e(TAG, "Couldn't load fonts from Typeface", exception);
            MapStrictMode.strictModeViolation("Couldn't load fonts from Typeface", exception);
        }
        return fonts;
    }
}
