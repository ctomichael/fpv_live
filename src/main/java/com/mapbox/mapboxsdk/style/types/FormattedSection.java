package com.mapbox.mapboxsdk.style.types;

import android.support.annotation.ColorInt;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Keep
public class FormattedSection {
    private Number fontScale;
    private String[] fontStack;
    private String text;
    private String textColor;

    public FormattedSection(@NonNull String text2) {
        this(text2, null, null, null);
    }

    public FormattedSection(@NonNull String text2, @Nullable Number fontScale2, @Nullable String[] fontStack2, @Nullable String textColor2) {
        this.text = text2;
        this.fontScale = fontScale2;
        this.fontStack = fontStack2;
        this.textColor = textColor2;
    }

    @Deprecated
    public FormattedSection(@NonNull String text2, @Nullable Number fontScale2, @Nullable String[] fontStack2) {
        this(text2, fontScale2, fontStack2, null);
    }

    @Deprecated
    public FormattedSection(@NonNull String text2, @Nullable Number fontScale2) {
        this(text2, fontScale2, null, null);
    }

    @Deprecated
    public FormattedSection(@NonNull String text2, @Nullable String[] fontStack2) {
        this(text2, null, fontStack2, null);
    }

    @NonNull
    public String getText() {
        return this.text;
    }

    @Nullable
    public Number getFontScale() {
        return this.fontScale;
    }

    @Nullable
    public String[] getFontStack() {
        return this.fontStack;
    }

    public String getTextColor() {
        return this.textColor;
    }

    public void setFontScale(@Nullable Number fontScale2) {
        this.fontScale = fontScale2;
    }

    public void setFontStack(@Nullable String[] fontStack2) {
        this.fontStack = fontStack2;
    }

    public void setTextColor(@Nullable String textColor2) {
        this.textColor = textColor2;
    }

    public void setTextColor(@ColorInt int textColor2) {
        this.textColor = ColorUtils.colorToRgbaString(textColor2);
    }

    /* access modifiers changed from: package-private */
    public void setTextColor(@NonNull Object textColor2) {
        setTextColor((String) textColor2);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormattedSection that = (FormattedSection) o;
        if (this.text != null) {
            if (!this.text.equals(that.text)) {
                return false;
            }
        } else if (that.text != null) {
            return false;
        }
        if (this.fontScale != null) {
            if (!this.fontScale.equals(that.fontScale)) {
                return false;
            }
        } else if (that.fontScale != null) {
            return false;
        }
        if (!Arrays.equals(this.fontStack, that.fontStack)) {
            return false;
        }
        if (this.textColor != null) {
            z = this.textColor.equals(that.textColor);
        } else if (that.textColor != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.text != null) {
            result = this.text.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.fontScale != null) {
            i = this.fontScale.hashCode();
        } else {
            i = 0;
        }
        int hashCode = (((i3 + i) * 31) + Arrays.hashCode(this.fontStack)) * 31;
        if (this.textColor != null) {
            i2 = this.textColor.hashCode();
        }
        return hashCode + i2;
    }

    /* access modifiers changed from: package-private */
    public Object[] toArray() {
        Map<String, Object> params = new HashMap<>();
        params.put("font-scale", this.fontScale);
        params.put("text-font", this.fontStack);
        params.put("text-color", this.textColor);
        return new Object[]{this.text, params};
    }

    public String toString() {
        return "FormattedSection{text='" + this.text + '\'' + ", fontScale=" + this.fontScale + ", fontStack=" + Arrays.toString(this.fontStack) + ", textColor='" + this.textColor + '\'' + '}';
    }
}
