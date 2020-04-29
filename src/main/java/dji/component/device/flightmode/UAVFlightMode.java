package dji.component.device.flightmode;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.EnvironmentCompat;
import dji.publics.protocol.ResponseBase;
import java.util.Objects;

public class UAVFlightMode implements Parcelable {
    public static final UAVFlightMode CINEMATIC = new UAVFlightMode("cinematic", 7);
    public static final UAVFlightMode COURSE_LOCK = new UAVFlightMode("course_lock", 5);
    public static final Parcelable.Creator<UAVFlightMode> CREATOR = new Parcelable.Creator<UAVFlightMode>() {
        /* class dji.component.device.flightmode.UAVFlightMode.AnonymousClass1 */

        public UAVFlightMode createFromParcel(Parcel in2) {
            return new UAVFlightMode(in2);
        }

        public UAVFlightMode[] newArray(int size) {
            return new UAVFlightMode[size];
        }
    };
    public static final UAVFlightMode DISCONNECT = new UAVFlightMode("disconnect", -2);
    public static final UAVFlightMode DRAW = new UAVFlightMode("draw", 21);
    public static final UAVFlightMode FOLLOW_ME = new UAVFlightMode("follow_me", 4);
    public static final UAVFlightMode HOME_LOCK = new UAVFlightMode("home_lock", 6);
    public static final UAVFlightMode HYPERLAPSE = new UAVFlightMode("hyperlapse", 28);
    public static final UAVFlightMode JOYSTICK = new UAVFlightMode("joystick", 2);
    public static final UAVFlightMode MULTI_QUICK_SHOT = new UAVFlightMode("multi_quick_shot", 27);
    public static final UAVFlightMode MULTI_TRACKING = new UAVFlightMode("multi_tracking", 26);
    public static final UAVFlightMode NORMAL = new UAVFlightMode(ResponseBase.STRING_NORMAL, 0);
    public static final UAVFlightMode PANO = new UAVFlightMode("pano", 30);
    public static final UAVFlightMode POI = new UAVFlightMode("poi", 11);
    public static final UAVFlightMode POI_TRACKING = new UAVFlightMode("poi_tracking", 29);
    public static final UAVFlightMode QUICK_MOVIE = new UAVFlightMode("quick_movie", 25);
    public static final UAVFlightMode SELFIE = new UAVFlightMode("selfie", 24);
    public static final UAVFlightMode SPORT = new UAVFlightMode("sport", 1);
    public static final UAVFlightMode TAPFLY = new UAVFlightMode("tap_fly", 22);
    public static final UAVFlightMode TERRAIN_TRACKING = new UAVFlightMode("terrain_tracking", 8);
    public static final UAVFlightMode TRACKING = new UAVFlightMode("tacking", 23);
    public static final UAVFlightMode TRIPOD = new UAVFlightMode("tripod", 3);
    public static final UAVFlightMode UNKNOWN = new UAVFlightMode(EnvironmentCompat.MEDIA_UNKNOWN, -1);
    public static final UAVFlightMode WAY_POINT = new UAVFlightMode("waypoint", 9);
    public static final UAVFlightMode WAY_POINT_2 = new UAVFlightMode("waypoint_2", 10);
    private final String name;
    private final int value;

    public static class FlightModeChangeSource {
        public static final int APP_FOLLOW = -2;
        public static final int FLYC = 3;
        public static final int LOST_CONNECT = -3;
        public static final int NAVIGATION = 10;
        public static final int NOT_INIT = -1;
        public static final int RC = 5;
        public static final int UNKNOWN = 255;
    }

    protected UAVFlightMode(Parcel in2) {
        this.name = in2.readString();
        this.value = in2.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.value);
    }

    private UAVFlightMode(String name2, int value2) {
        this.name = name2;
        this.value = value2;
    }

    public String toString() {
        return "UAVFlightMode{name='" + this.name + '\'' + ", value=" + this.value + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UAVFlightMode)) {
            return false;
        }
        UAVFlightMode that = (UAVFlightMode) o;
        if (this.value != that.value || !Objects.equals(this.name, that.name)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.name, Integer.valueOf(this.value));
    }
}
