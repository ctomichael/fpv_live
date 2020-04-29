package dji.sdksharedlib.util;

import android.app.Application;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.model.LocationCoordinate2D;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.component.DJIComponentManager;
import dji.midware.util.LocationCoordinate;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

@EXClassNullAway
public class Util {
    public static Application getApplication() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication", new Class[0]).invoke(null, null);
    }

    public static boolean needUpdateCameraByPlatformChanged(DJIComponentManager.PlatformType platformType) {
        return DJIComponentManager.PlatformType.OSMO == platformType || DJIComponentManager.PlatformType.P3s == platformType || DJIComponentManager.PlatformType.P3x == platformType || DJIComponentManager.PlatformType.P3c == platformType || DJIComponentManager.PlatformType.P3w == platformType || DJIComponentManager.PlatformType.P4 == platformType || DJIComponentManager.PlatformType.P4P == platformType || DJIComponentManager.PlatformType.P4PSDR == platformType || DJIComponentManager.PlatformType.P4RTK == platformType || DJIComponentManager.PlatformType.P4A == platformType;
    }

    public static ArrayList<LocationCoordinate> wrapLocationCoordinate(ArrayList<LocationCoordinate2D> coordinates) {
        ArrayList<LocationCoordinate> locationCoordinates = new ArrayList<>();
        Iterator<LocationCoordinate2D> it2 = coordinates.iterator();
        while (it2.hasNext()) {
            LocationCoordinate2D coordinate2D = it2.next();
            locationCoordinates.add(new LocationCoordinate(coordinate2D.getLatitude(), coordinate2D.getLongitude()));
        }
        return locationCoordinates;
    }

    public static ArrayList<LocationCoordinate2D> transformLocationCoordinate2D(ArrayList<LocationCoordinate> coordinates) {
        ArrayList<LocationCoordinate2D> locationCoordinates = new ArrayList<>();
        Iterator<LocationCoordinate> it2 = coordinates.iterator();
        while (it2.hasNext()) {
            LocationCoordinate coordinate = it2.next();
            locationCoordinates.add(new LocationCoordinate2D(coordinate.latitude, coordinate.longitude));
        }
        return locationCoordinates;
    }

    public static LocationCoordinate wrapLocationCoordinate(LocationCoordinate3D coordinate3D) {
        return new LocationCoordinate(coordinate3D.getLatitude(), coordinate3D.getLongitude(), coordinate3D.getAltitude());
    }
}
