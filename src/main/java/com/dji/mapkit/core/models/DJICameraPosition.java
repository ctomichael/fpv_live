package com.dji.mapkit.core.models;

public class DJICameraPosition {
    public final float bearing;
    public final DJILatLng target;
    public final float tilt;
    public final float zoom;

    private DJICameraPosition() {
        this.target = null;
        this.zoom = 0.0f;
        this.tilt = 0.0f;
        this.bearing = 0.0f;
    }

    public DJICameraPosition(DJILatLng target2, float zoom2, float tilt2, float bearing2) {
        this.target = target2;
        this.zoom = zoom2;
        this.tilt = tilt2;
        this.bearing = (((double) bearing2) <= 0.0d ? (bearing2 % 360.0f) + 360.0f : bearing2) % 360.0f;
    }

    public DJICameraPosition(DJILatLng position, float zoom2) {
        this(position, zoom2, 0.0f, 0.0f);
    }

    public DJILatLng getPosition() {
        return this.target;
    }

    public float getZoom() {
        return this.zoom;
    }

    public float getTilt() {
        return this.tilt;
    }

    public float getBearing() {
        return this.bearing;
    }

    public static final DJICameraPosition fromLatLngZoom(DJILatLng target2, float zoom2) {
        return new DJICameraPosition(target2, zoom2, 0.0f, 0.0f);
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3 = 0;
        if (this.target != null) {
            result = this.target.hashCode();
        } else {
            result = 0;
        }
        int i4 = result * 31;
        if (this.zoom != 0.0f) {
            i = Float.floatToIntBits(this.zoom);
        } else {
            i = 0;
        }
        int i5 = (i4 + i) * 31;
        if (this.tilt != 0.0f) {
            i2 = Float.floatToIntBits(this.tilt);
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 31;
        if (this.bearing != 0.0f) {
            i3 = Float.floatToIntBits(this.bearing);
        }
        return i6 + i3;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DJICameraPosition)) {
            return false;
        }
        DJICameraPosition o = (DJICameraPosition) obj;
        if (this.target.equals(o.target) && this.zoom == o.zoom && this.tilt == o.tilt && this.bearing == o.bearing) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "latlng: " + this.target + " zoom: " + this.zoom + " tilt: " + this.tilt + " bearing: " + this.bearing;
    }

    public static Builder builder(DJICameraPosition camera) {
        return new Builder(camera);
    }

    public static final class Builder {
        private float bearing = Float.NaN;
        private DJILatLng target = new DJILatLng(0.0d, 0.0d);
        private float tilt = Float.NaN;
        private float zoom = Float.NaN;

        public Builder() {
        }

        public Builder(DJICameraPosition previous) {
            this.target = previous.target;
            this.zoom = previous.zoom;
            this.tilt = previous.tilt;
            this.bearing = previous.bearing;
        }

        public Builder target(DJILatLng location) {
            this.target = location;
            return this;
        }

        public Builder zoom(float zoom2) {
            this.zoom = zoom2;
            return this;
        }

        public Builder tilt(float tilt2) {
            this.tilt = tilt2;
            return this;
        }

        public Builder bearing(float bearing2) {
            this.bearing = bearing2;
            return this;
        }

        public DJICameraPosition build() {
            return new DJICameraPosition(this.target, this.zoom, this.tilt, this.bearing);
        }
    }
}
