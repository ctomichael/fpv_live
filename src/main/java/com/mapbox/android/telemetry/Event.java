package com.mapbox.android.telemetry;

import android.os.Parcelable;
import java.util.EnumSet;

public abstract class Event implements Parcelable {
    static EnumSet<Type> visionEventTypes = EnumSet.of(Type.VIS_GENERAL, Type.VIS_ATTACHMENT, Type.VIS_OBJ_DETECTION);

    @Deprecated
    public enum Type {
        TURNSTILE,
        MAP_LOAD,
        MAP_CLICK,
        MAP_DRAGEND,
        OFFLINE_DOWNLOAD_START,
        OFFLINE_DOWNLOAD_COMPLETE,
        LOCATION,
        NAV_DEPART,
        NAV_ARRIVE,
        NAV_CANCEL,
        NAV_REROUTE,
        NAV_FEEDBACK,
        NAV_FASTER_ROUTE,
        VIS_GENERAL,
        VIS_ATTACHMENT,
        VIS_OBJ_DETECTION,
        NO_OP,
        CRASH
    }

    /* access modifiers changed from: package-private */
    public Type obtainType() {
        return Type.NO_OP;
    }
}
