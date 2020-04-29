package com.mapbox.mapboxsdk.utils;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import java.text.Normalizer;

@Keep
class StringUtils {
    StringUtils() {
    }

    @Keep
    @NonNull
    static String unaccent(@NonNull String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("(\\p{InCombiningDiacriticalMarks}|\\p{InCombiningDiacriticalMarksForSymbols}|\\p{InCombiningDiacriticalMarksSupplement})+", "");
    }
}
