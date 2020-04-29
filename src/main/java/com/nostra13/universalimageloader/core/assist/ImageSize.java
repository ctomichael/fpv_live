package com.nostra13.universalimageloader.core.assist;

public class ImageSize {
    private static final String SEPARATOR = "x";
    private static final int TO_STRING_MAX_LENGHT = 9;
    private final int height;
    private final int width;

    public ImageSize(int width2, int height2) {
        this.width = width2;
        this.height = height2;
    }

    public ImageSize(int width2, int height2, int rotation) {
        if (rotation % 180 == 0) {
            this.width = width2;
            this.height = height2;
            return;
        }
        this.width = height2;
        this.height = width2;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public ImageSize scaleDown(int sampleSize) {
        return new ImageSize(this.width / sampleSize, this.height / sampleSize);
    }

    public ImageSize scale(float scale) {
        return new ImageSize((int) (((float) this.width) * scale), (int) (((float) this.height) * scale));
    }

    public String toString() {
        return new StringBuilder(9).append(this.width).append(SEPARATOR).append(this.height).toString();
    }
}
