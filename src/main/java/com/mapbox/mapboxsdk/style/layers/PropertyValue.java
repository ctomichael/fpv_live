package com.mapbox.mapboxsdk.style.layers;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.JsonArray;
import com.mapbox.mapboxsdk.MapStrictMode;
import com.mapbox.mapboxsdk.exceptions.ConversionException;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.utils.ColorUtils;

public class PropertyValue<T> {
    private static final String TAG = "Mbgl-PropertyValue";
    @NonNull
    public final String name;
    public final T value;

    public PropertyValue(@NonNull String name2, T value2) {
        this.name = name2;
        this.value = value2;
    }

    public boolean isNull() {
        return this.value == null;
    }

    public boolean isExpression() {
        return !isNull() && ((this.value instanceof JsonArray) || (this.value instanceof Expression));
    }

    @Nullable
    public Expression getExpression() {
        if (isExpression()) {
            return this.value instanceof JsonArray ? Expression.Converter.convert((JsonArray) this.value) : (Expression) this.value;
        }
        Logger.w(TAG, String.format("%s not an expression, try PropertyValue#getValue()", this.name));
        return null;
    }

    public boolean isValue() {
        return !isNull() && !isExpression();
    }

    @Nullable
    public T getValue() {
        if (isValue()) {
            return this.value;
        }
        Logger.w(TAG, String.format("%s not a value, try PropertyValue#getExpression()", this.name));
        return null;
    }

    @ColorInt
    @Nullable
    public Integer getColorInt() {
        if (!isValue() || !(this.value instanceof String)) {
            Logger.e(TAG, String.format("%s is not a String value and can not be converted to a color it", this.name));
            return null;
        }
        try {
            return Integer.valueOf(ColorUtils.rgbaToColor((String) this.value));
        } catch (ConversionException ex) {
            Logger.e(TAG, String.format("%s could not be converted to a Color int: %s", this.name, ex.getMessage()));
            MapStrictMode.strictModeViolation(ex);
            return null;
        }
    }

    public String toString() {
        return String.format("%s: %s", this.name, this.value);
    }
}
