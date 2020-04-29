package com.mapbox.mapboxsdk.module.telemetry;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.support.annotation.FloatRange;

@SuppressLint({"ParcelCreator"})
public class OfflineDownloadEndEvent extends MapBaseEvent {
    private static final String EVENT_NAME = "map.offlineDownload.end";
    private final Double maxZoom;
    private final Double minZoom;
    private long numberOfTilesCompleted;
    private final String shapeForOfflineRegion;
    private long sizeOfResourcesCompleted;
    private String state;
    private String styleURL;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    OfflineDownloadEndEvent(PhoneState phoneState, String shapeForOfflineRegion2, @FloatRange(from = 0.0d, to = 25.5d) Double minZoom2, @FloatRange(from = 0.0d, to = 25.5d) Double maxZoom2) {
        super(phoneState);
        this.shapeForOfflineRegion = shapeForOfflineRegion2;
        this.minZoom = minZoom2;
        this.maxZoom = maxZoom2;
    }

    /* access modifiers changed from: package-private */
    public String getEventName() {
        return EVENT_NAME;
    }

    /* access modifiers changed from: package-private */
    public Double getMinZoom() {
        return this.minZoom;
    }

    /* access modifiers changed from: package-private */
    public Double getMaxZoom() {
        return this.maxZoom;
    }

    /* access modifiers changed from: package-private */
    public String getShapeForOfflineRegion() {
        return this.shapeForOfflineRegion;
    }

    /* access modifiers changed from: package-private */
    public String getStyleURL() {
        return this.styleURL;
    }

    /* access modifiers changed from: package-private */
    public void setStyleURL(String styleURL2) {
        this.styleURL = styleURL2;
    }

    /* access modifiers changed from: package-private */
    public long getSizeOfResourcesCompleted() {
        return this.sizeOfResourcesCompleted;
    }

    /* access modifiers changed from: package-private */
    public void setSizeOfResourcesCompleted(long sizeOfResourcesCompleted2) {
        this.sizeOfResourcesCompleted = sizeOfResourcesCompleted2;
    }

    /* access modifiers changed from: package-private */
    public long getNumberOfTilesCompleted() {
        return this.numberOfTilesCompleted;
    }

    /* access modifiers changed from: package-private */
    public void setNumberOfTilesCompleted(long numberOfTilesCompleted2) {
        this.numberOfTilesCompleted = numberOfTilesCompleted2;
    }

    /* access modifiers changed from: package-private */
    public String getState() {
        return this.state;
    }

    /* access modifiers changed from: package-private */
    public void setState(int state2) {
        this.state = String.valueOf(state2);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OfflineDownloadEndEvent that = (OfflineDownloadEndEvent) o;
        if (this.sizeOfResourcesCompleted != that.sizeOfResourcesCompleted || this.numberOfTilesCompleted != that.numberOfTilesCompleted) {
            return false;
        }
        if (this.minZoom != null) {
            if (!this.minZoom.equals(that.minZoom)) {
                return false;
            }
        } else if (that.minZoom != null) {
            return false;
        }
        if (this.maxZoom != null) {
            if (!this.maxZoom.equals(that.maxZoom)) {
                return false;
            }
        } else if (that.maxZoom != null) {
            return false;
        }
        if (this.shapeForOfflineRegion != null) {
            if (!this.shapeForOfflineRegion.equals(that.shapeForOfflineRegion)) {
                return false;
            }
        } else if (that.shapeForOfflineRegion != null) {
            return false;
        }
        if (this.styleURL != null) {
            if (!this.styleURL.equals(that.styleURL)) {
                return false;
            }
        } else if (that.styleURL != null) {
            return false;
        }
        if (this.state != null) {
            z = this.state.equals(that.state);
        } else if (that.state != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4 = 0;
        if (this.minZoom != null) {
            result = this.minZoom.hashCode();
        } else {
            result = 0;
        }
        int i5 = result * 31;
        if (this.maxZoom != null) {
            i = this.maxZoom.hashCode();
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (this.shapeForOfflineRegion != null) {
            i2 = this.shapeForOfflineRegion.hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 31;
        if (this.styleURL != null) {
            i3 = this.styleURL.hashCode();
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (this.state != null) {
            i4 = this.state.hashCode();
        }
        return ((((i8 + i4) * 31) + ((int) (this.sizeOfResourcesCompleted ^ (this.sizeOfResourcesCompleted >>> 32)))) * 31) + ((int) (this.numberOfTilesCompleted ^ (this.numberOfTilesCompleted >>> 32)));
    }

    public String toString() {
        return "OfflineDownloadEndEvent{, minZoom=" + this.minZoom + ", maxZoom=" + this.maxZoom + ", shapeForOfflineRegion='" + this.shapeForOfflineRegion + '\'' + ", styleURL='" + this.styleURL + '\'' + ", sizeOfResourcesCompleted=" + this.sizeOfResourcesCompleted + ", numberOfTilesCompleted=" + this.numberOfTilesCompleted + ", state=" + this.state + '}';
    }
}
