package com.drew.metadata;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;

public class Age {
    private final int _days;
    private final int _hours;
    private final int _minutes;
    private final int _months;
    private final int _seconds;
    private final int _years;

    @Nullable
    public static Age fromPanasonicString(@NotNull String s) {
        if (s.length() != 19 || s.startsWith("9999:99:99")) {
            return null;
        }
        try {
            return new Age(Integer.parseInt(s.substring(0, 4)), Integer.parseInt(s.substring(5, 7)), Integer.parseInt(s.substring(8, 10)), Integer.parseInt(s.substring(11, 13)), Integer.parseInt(s.substring(14, 16)), Integer.parseInt(s.substring(17, 19)));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Age(int years, int months, int days, int hours, int minutes, int seconds) {
        this._years = years;
        this._months = months;
        this._days = days;
        this._hours = hours;
        this._minutes = minutes;
        this._seconds = seconds;
    }

    public int getYears() {
        return this._years;
    }

    public int getMonths() {
        return this._months;
    }

    public int getDays() {
        return this._days;
    }

    public int getHours() {
        return this._hours;
    }

    public int getMinutes() {
        return this._minutes;
    }

    public int getSeconds() {
        return this._seconds;
    }

    public String toString() {
        return String.format("%04d:%02d:%02d %02d:%02d:%02d", Integer.valueOf(this._years), Integer.valueOf(this._months), Integer.valueOf(this._days), Integer.valueOf(this._hours), Integer.valueOf(this._minutes), Integer.valueOf(this._seconds));
    }

    public String toFriendlyString() {
        StringBuilder result = new StringBuilder();
        appendAgePart(result, this._years, "year");
        appendAgePart(result, this._months, "month");
        appendAgePart(result, this._days, "day");
        appendAgePart(result, this._hours, "hour");
        appendAgePart(result, this._minutes, "minute");
        appendAgePart(result, this._seconds, "second");
        return result.toString();
    }

    private static void appendAgePart(StringBuilder result, int num, String singularName) {
        if (num != 0) {
            if (result.length() != 0) {
                result.append(' ');
            }
            result.append(num).append(' ').append(singularName);
            if (num != 1) {
                result.append('s');
            }
        }
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Age age = (Age) o;
        if (this._days != age._days) {
            return false;
        }
        if (this._hours != age._hours) {
            return false;
        }
        if (this._minutes != age._minutes) {
            return false;
        }
        if (this._months != age._months) {
            return false;
        }
        if (this._seconds != age._seconds) {
            return false;
        }
        if (this._years != age._years) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((((((this._years * 31) + this._months) * 31) + this._days) * 31) + this._hours) * 31) + this._minutes) * 31) + this._seconds;
    }
}
