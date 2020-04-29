package com.mapbox.mapboxsdk.style.types;

import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import java.util.Arrays;

@Keep
public class Formatted {
    private final FormattedSection[] formattedSections;

    public Formatted(FormattedSection... formattedSections2) {
        this.formattedSections = formattedSections2;
    }

    public FormattedSection[] getFormattedSections() {
        return this.formattedSections;
    }

    public Object[] toArray() {
        Object[] sections = new Object[this.formattedSections.length];
        for (int i = 0; i < this.formattedSections.length; i++) {
            sections[i] = this.formattedSections[i].toArray();
        }
        return sections;
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Arrays.equals(this.formattedSections, ((Formatted) o).formattedSections);
    }

    public int hashCode() {
        return Arrays.hashCode(this.formattedSections);
    }

    public String toString() {
        return "Formatted{formattedSections=" + Arrays.toString(this.formattedSections) + '}';
    }
}
