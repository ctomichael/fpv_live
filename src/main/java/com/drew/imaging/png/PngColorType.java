package com.drew.imaging.png;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;

public enum PngColorType {
    Greyscale(0, "Greyscale", 1, 2, 4, 8, 16),
    TrueColor(2, "True Color", 8, 16),
    IndexedColor(3, "Indexed Color", 1, 2, 4, 8),
    GreyscaleWithAlpha(4, "Greyscale with Alpha", 8, 16),
    TrueColorWithAlpha(6, "True Color with Alpha", 8, 16);
    
    @NotNull
    private final int[] _allowedBitDepths;
    @NotNull
    private final String _description;
    private final int _numericValue;

    @Nullable
    public static PngColorType fromNumericValue(int numericValue) {
        PngColorType[] arr$ = (PngColorType[]) PngColorType.class.getEnumConstants();
        for (PngColorType colorType : arr$) {
            if (colorType.getNumericValue() == numericValue) {
                return colorType;
            }
        }
        return null;
    }

    private PngColorType(int numericValue, @NotNull String description, @NotNull int... allowedBitDepths) {
        this._numericValue = numericValue;
        this._description = description;
        this._allowedBitDepths = allowedBitDepths;
    }

    public int getNumericValue() {
        return this._numericValue;
    }

    @NotNull
    public String getDescription() {
        return this._description;
    }

    @NotNull
    public int[] getAllowedBitDepths() {
        return this._allowedBitDepths;
    }
}
