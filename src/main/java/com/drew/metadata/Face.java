package com.drew.metadata;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;

public class Face {
    @Nullable
    private final Age _age;
    private final int _height;
    @Nullable
    private final String _name;
    private final int _width;
    private final int _x;
    private final int _y;

    public Face(int x, int y, int width, int height, @Nullable String name, @Nullable Age age) {
        this._x = x;
        this._y = y;
        this._width = width;
        this._height = height;
        this._name = name;
        this._age = age;
    }

    public int getX() {
        return this._x;
    }

    public int getY() {
        return this._y;
    }

    public int getWidth() {
        return this._width;
    }

    public int getHeight() {
        return this._height;
    }

    @Nullable
    public String getName() {
        return this._name;
    }

    @Nullable
    public Age getAge() {
        return this._age;
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Face face = (Face) o;
        if (this._height != face._height) {
            return false;
        }
        if (this._width != face._width) {
            return false;
        }
        if (this._x != face._x) {
            return false;
        }
        if (this._y != face._y) {
            return false;
        }
        if (this._age == null ? face._age != null : !this._age.equals(face._age)) {
            return false;
        }
        if (this._name != null) {
            if (this._name.equals(face._name)) {
                return true;
            }
        } else if (face._name == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        int i3 = ((((((this._x * 31) + this._y) * 31) + this._width) * 31) + this._height) * 31;
        if (this._name != null) {
            i = this._name.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this._age != null) {
            i2 = this._age.hashCode();
        }
        return i4 + i2;
    }

    @NotNull
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("x: ").append(this._x);
        result.append(" y: ").append(this._y);
        result.append(" width: ").append(this._width);
        result.append(" height: ").append(this._height);
        if (this._name != null) {
            result.append(" name: ").append(this._name);
        }
        if (this._age != null) {
            result.append(" age: ").append(this._age.toFriendlyString());
        }
        return result.toString();
    }
}
