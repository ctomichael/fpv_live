package dji.common.mission.waypoint;

import dji.common.model.LocationCoordinate2D;

public class WaypointMissionInterruption {
    private float altitude;
    private LocationCoordinate2D coordinate;
    private int index;
    private int missionID;

    private WaypointMissionInterruption(Builder builder) {
        this.coordinate = builder.coordinate;
        this.index = builder.index;
        this.altitude = builder.altitude;
        this.missionID = builder.missionID;
    }

    public LocationCoordinate2D getCoordinate() {
        return this.coordinate;
    }

    public int getIndex() {
        return this.index;
    }

    public float getAltitude() {
        return this.altitude;
    }

    public int getMissionID() {
        return this.missionID;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public float altitude;
        /* access modifiers changed from: private */
        public LocationCoordinate2D coordinate;
        /* access modifiers changed from: private */
        public int index;
        /* access modifiers changed from: private */
        public int missionID;

        private Builder() {
        }

        public Builder coordinate(LocationCoordinate2D coordinate2) {
            this.coordinate = coordinate2;
            return this;
        }

        public Builder index(int index2) {
            this.index = index2;
            return this;
        }

        public Builder altitude(float altitude2) {
            this.altitude = altitude2;
            return this;
        }

        public Builder missionID(int missionID2) {
            this.missionID = missionID2;
            return this;
        }

        public WaypointMissionInterruption build() {
            return new WaypointMissionInterruption(this);
        }
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WaypointMissionInterruption that = (WaypointMissionInterruption) o;
        if (this.index != that.index || Float.compare(that.altitude, this.altitude) != 0 || this.missionID != that.missionID) {
            return false;
        }
        if (this.coordinate != null) {
            z = this.coordinate.equals(that.coordinate);
        } else if (that.coordinate != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.coordinate != null) {
            result = this.coordinate.hashCode();
        } else {
            result = 0;
        }
        int i2 = ((result * 31) + this.index) * 31;
        if (this.altitude != 0.0f) {
            i = Float.floatToIntBits(this.altitude);
        }
        return ((i2 + i) * 31) + this.missionID;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("WaypointMissionInterruption{");
        sb.append("coordinate=").append(this.coordinate);
        sb.append(", index=").append(this.index);
        sb.append(", altitude=").append(this.altitude);
        sb.append(", missionID=").append(this.missionID);
        sb.append('}');
        return sb.toString();
    }
}
