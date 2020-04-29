package com.google.protobuf;

import java.util.Arrays;

public final class TextFormatParseLocation {
    public static final TextFormatParseLocation EMPTY = new TextFormatParseLocation(-1, -1);
    private final int column;
    private final int line;

    static TextFormatParseLocation create(int line2, int column2) {
        if (line2 == -1 && column2 == -1) {
            return EMPTY;
        }
        if (line2 >= 0 && column2 >= 0) {
            return new TextFormatParseLocation(line2, column2);
        }
        throw new IllegalArgumentException(String.format("line and column values must be >= 0: line %d, column: %d", Integer.valueOf(line2), Integer.valueOf(column2)));
    }

    private TextFormatParseLocation(int line2, int column2) {
        this.line = line2;
        this.column = column2;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

    public String toString() {
        return String.format("ParseLocation{line=%d, column=%d}", Integer.valueOf(this.line), Integer.valueOf(this.column));
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TextFormatParseLocation)) {
            return false;
        }
        TextFormatParseLocation that = (TextFormatParseLocation) o;
        if (this.line == that.getLine() && this.column == that.getColumn()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(new int[]{this.line, this.column});
    }
}
