package dji.internal.mission.abstraction;

import android.graphics.PointF;
import com.dji.mapkit.core.maps.DJIProjection;
import com.dji.mapkit.core.models.DJILatLng;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.mission.waypoint.WaypointTurnMode;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.LocationUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MultiInterestPointsMissionBuilder extends WaypointMission.Builder {
    private static final int MUTI = 1;
    private boolean bezierCurePath = true;
    private boolean enableAutoPitch = true;
    private HashMap<Waypoint, LocationCoordinate3D> interestMap = new HashMap<>();
    private boolean isUseCustomDirection = false;
    private boolean isUseCustomGimbalPitch = false;
    private DJIProjection projection;

    public MultiInterestPointsMissionBuilder() {
        this.isMultiInterestPointMission = true;
        this.headingMode = WaypointMissionHeadingMode.USING_WAYPOINT_HEADING;
    }

    public boolean isBezierCurePath() {
        return this.bezierCurePath;
    }

    public MultiInterestPointsMissionBuilder enableBezierCurePath() {
        this.bezierCurePath = true;
        return this;
    }

    public MultiInterestPointsMissionBuilder disableBezierCurePath() {
        this.bezierCurePath = false;
        return this;
    }

    public MultiInterestPointsMissionBuilder setUseCustomDirection(boolean ignore) {
        this.isUseCustomDirection = ignore;
        return this;
    }

    public MultiInterestPointsMissionBuilder setUseCustomGimbalPitch(boolean ignore) {
        this.isUseCustomGimbalPitch = ignore;
        return this;
    }

    public boolean isUseCustomDirection() {
        return this.isUseCustomDirection;
    }

    public MultiInterestPointsMissionBuilder setPorjection(DJIProjection projection2) {
        this.projection = projection2;
        return this;
    }

    public MultiInterestPointsMissionBuilder(MultiInterestPointsMissionBuilder builder) {
        this.isMultiInterestPointMission = true;
        this.missionID = builder.getMissionID();
        this.waypointCount = builder.getWaypointCount();
        this.maxFlightSpeed = builder.getMaxFlightSpeed();
        this.autoFlightSpeed = builder.getAutoFlightSpeed();
        this.finishedAction = builder.getFinishedAction();
        this.headingMode = builder.getHeadingMode();
        this.flightPathMode = builder.getFlightPathMode();
        this.gotoFirstWaypointMode = builder.getGotoFirstWaypointMode();
        this.isExitMissionOnRCSignalLostEnabled = builder.isExitMissionOnRCSignalLostEnabled();
        this.pointOfInterest = builder.getPointOfInterest();
        this.isGimbalPitchRotationEnabled = builder.isGimbalPitchRotationEnabled();
        this.repeatTimes = builder.getRepeatTimes();
        this.waypointList = new ArrayList(builder.waypointList);
        this.waypointCount = this.waypointList.size();
        this.interestMap = new HashMap<>(builder.interestMap);
        this.isUseCustomDirection = builder.isUseCustomDirection();
        if (builder.getHeadingMode() == WaypointMissionHeadingMode.TOWARD_POINT_OF_INTEREST) {
            this.isMultiInterestPointMission = false;
        }
        if (!builder.isGimbalPitchRotationEnabled()) {
            this.enableAutoPitch = false;
        }
    }

    public MultiInterestPointsMissionBuilder(WaypointMission waypointMissionToClone) {
        super(waypointMissionToClone);
        this.isMultiInterestPointMission = true;
        if (waypointMissionToClone.getHeadingMode() == WaypointMissionHeadingMode.TOWARD_POINT_OF_INTEREST) {
            this.isMultiInterestPointMission = false;
        }
        if (!waypointMissionToClone.isGimbalPitchRotationEnabled()) {
            this.enableAutoPitch = false;
        }
    }

    public DJILatLng getEndPointOfLine(Waypoint wp) {
        DJILatLng latLng = null;
        if (wp != null && this.waypointList.contains(wp)) {
            int index = this.waypointList.indexOf(wp);
            if (!this.bezierCurePath || index == this.waypointList.size() - 1) {
                return new DJILatLng(wp.coordinate.getLatitude(), wp.coordinate.getLongitude());
            }
            if (index > 0 && index < this.waypointList.size()) {
                Waypoint preOne = (Waypoint) this.waypointList.get(index - 1);
                if (this.flightPathMode == WaypointMissionFlightPathMode.NORMAL) {
                    return new DJILatLng(wp.coordinate.getLatitude(), wp.coordinate.getLongitude());
                }
                float bezierRadius = 1.0f * wp.cornerRadiusInMeters;
                float realDistance = (float) LocationUtils.gps2m(wp.coordinate, preOne.coordinate);
                if (bezierRadius > realDistance) {
                    return new DJILatLng(wp.coordinate.getLatitude(), wp.coordinate.getLongitude());
                }
                latLng = converPointToGPS(getCutPoint(converGPSToPoint(preOne.coordinate), converGPSToPoint(wp.coordinate), bezierRadius / realDistance));
            }
        }
        return latLng;
    }

    public DJILatLng getEndPointOfLine(double lat, double longitude) {
        LocationCoordinate2D locationCoordinate2D = new LocationCoordinate2D(lat, longitude);
        Waypoint wp = null;
        Iterator it2 = this.waypointList.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            Waypoint w = (Waypoint) it2.next();
            if (w.coordinate.isEqualPosition(locationCoordinate2D)) {
                wp = w;
                break;
            }
        }
        if (wp != null) {
            return getEndPointOfLine(wp);
        }
        return null;
    }

    public DJILatLng getStartPointOfLine(Waypoint wp) {
        int index;
        DJILatLng latLng = null;
        if (wp != null && this.waypointList.contains(wp) && (index = this.waypointList.indexOf(wp)) > 0) {
            Waypoint preOne = (Waypoint) this.waypointList.get(index - 1);
            if (index - 1 == 0 || this.flightPathMode == WaypointMissionFlightPathMode.NORMAL || !this.bezierCurePath) {
                return new DJILatLng(preOne.coordinate.getLatitude(), preOne.coordinate.getLongitude());
            }
            float bezierRadius = 1.0f * preOne.cornerRadiusInMeters;
            float realDistance = (float) LocationUtils.gps2m(wp.coordinate, preOne.coordinate);
            if (bezierRadius > realDistance) {
                return new DJILatLng(preOne.coordinate.getLatitude(), preOne.coordinate.getLongitude());
            }
            latLng = converPointToGPS(getCutPoint(converGPSToPoint(wp.coordinate), converGPSToPoint(preOne.coordinate), bezierRadius / realDistance));
        }
        return latLng;
    }

    public DJILatLng getStartPointOfLine(double lat, double longitude) {
        LocationCoordinate2D locationCoordinate2D = new LocationCoordinate2D(lat, longitude);
        Waypoint wp = null;
        Iterator it2 = this.waypointList.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            Waypoint w = (Waypoint) it2.next();
            if (w.coordinate.isEqualPosition(locationCoordinate2D)) {
                wp = w;
                break;
            }
        }
        if (wp == null || !this.waypointList.contains(wp)) {
            return null;
        }
        return getStartPointOfLine(wp);
    }

    public List<DJILatLng> getBezierLinePoints(Waypoint wp, int counts) {
        List<DJILatLng> points = new ArrayList<>(2);
        if (wp != null && this.waypointList.contains(wp)) {
            if (!this.bezierCurePath || this.flightPathMode == WaypointMissionFlightPathMode.NORMAL) {
                points.add(new DJILatLng(wp.coordinate.getLatitude(), wp.coordinate.getLongitude()));
            } else {
                int index = this.waypointList.indexOf(wp);
                if (index == 0 || index == this.waypointList.size() - 1) {
                    points.add(new DJILatLng(wp.coordinate.getLatitude(), wp.coordinate.getLongitude()));
                } else {
                    Waypoint preOne = (Waypoint) this.waypointList.get(index - 1);
                    Waypoint laterOne = (Waypoint) this.waypointList.get(index + 1);
                    if (Math.abs(getDirectDiff(preOne, wp, laterOne)) < 3.0d) {
                        counts = 2;
                    }
                    float bezierRadius = 1.0f * wp.cornerRadiusInMeters;
                    float realDistance = (float) LocationUtils.gps2m(wp.coordinate, preOne.coordinate);
                    if (bezierRadius <= realDistance) {
                        PointF converGPSToPoint = converGPSToPoint(preOne.coordinate);
                        PointF pre = getCutPoint(converGPSToPoint, converGPSToPoint(wp.coordinate), bezierRadius / realDistance);
                        float realDistance2 = (float) LocationUtils.gps2m(wp.coordinate, laterOne.coordinate);
                        float bezierRadius2 = 1.0f * wp.cornerRadiusInMeters;
                        if (bezierRadius2 <= realDistance2) {
                            PointF converGPSToPoint2 = converGPSToPoint(laterOne.coordinate);
                            for (PointF p : getAllPoints(pre, getCutPoint(converGPSToPoint2, converGPSToPoint(wp.coordinate), bezierRadius2 / realDistance2), converGPSToPoint(wp.coordinate), counts)) {
                                points.add(converPointToGPS(p));
                            }
                        }
                    }
                }
            }
        }
        return points;
    }

    public List<DJILatLng> getBezierLinePoints(double lat, double longitude, int counts) {
        LocationCoordinate2D locationCoordinate2D = new LocationCoordinate2D(lat, longitude);
        Waypoint wp = null;
        Iterator it2 = this.waypointList.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            Waypoint w = (Waypoint) it2.next();
            if (w.coordinate.isEqualPosition(locationCoordinate2D)) {
                wp = w;
                break;
            }
        }
        List<DJILatLng> points = new ArrayList<>(2);
        if (wp != null) {
            return getBezierLinePoints(wp, counts);
        }
        return points;
    }

    private double getDirectDiff(Waypoint start, Waypoint mid, Waypoint end) {
        return getDirect(mid, end) - getDirect(start, mid);
    }

    private PointF converGPSToPoint(LocationCoordinate2D gps) {
        return new PointF((float) gps.getLongitude(), (float) gps.getLatitude());
    }

    private DJILatLng converPointToGPS(PointF pointF) {
        return new DJILatLng((double) pointF.y, (double) pointF.x);
    }

    private List<PointF> getAllPoints(PointF start, PointF end, PointF control, int sum) {
        int sum2 = sum - 1;
        List<PointF> list = new ArrayList<>();
        for (int i = 0; i <= sum2; i++) {
            list.add(getBezierPoint(start, end, control, ((float) i) / ((float) sum2)));
        }
        return list;
    }

    private PointF getBezierPoint(PointF start, PointF end, PointF control, float t) {
        PointF bezierPoint = new PointF();
        bezierPoint.x = ((1.0f - t) * (1.0f - t) * start.x) + (2.0f * t * (1.0f - t) * control.x) + (t * t * end.x);
        bezierPoint.y = ((1.0f - t) * (1.0f - t) * start.y) + (2.0f * t * (1.0f - t) * control.y) + (t * t * end.y);
        return bezierPoint;
    }

    private PointF getCutPoint(PointF start, PointF end, float distanceToEndRatio) {
        float distanceToEndRatio2 = Math.abs(distanceToEndRatio);
        double distance = Math.sqrt((double) (((start.x - end.x) * (start.x - end.x)) + ((start.y - end.y) * (start.y - end.y))));
        float t = (float) ((((double) distanceToEndRatio2) * distance) / distance);
        float middle = t * Math.abs(start.x - end.x);
        PointF point = new PointF();
        if (start.x > end.x) {
            point.x = end.x + middle;
        } else {
            point.x = end.x - middle;
        }
        float middle2 = t * Math.abs(start.y - end.y);
        if (start.y > end.y) {
            point.y = end.y + middle2;
        } else {
            point.y = end.y - middle2;
        }
        return point;
    }

    public MultiInterestPointsMissionBuilder disableMultiPOIFeature() {
        this.isMultiInterestPointMission = false;
        return this;
    }

    public MultiInterestPointsMissionBuilder enableMultiPOIFeature() {
        this.isMultiInterestPointMission = true;
        return this;
    }

    public MultiInterestPointsMissionBuilder enableAutoPitchFeature() {
        this.enableAutoPitch = true;
        this.isGimbalPitchRotationEnabled = true;
        return this;
    }

    public MultiInterestPointsMissionBuilder disableAutoPitchFeature() {
        this.enableAutoPitch = false;
        return this;
    }

    public boolean isAutoPitch() {
        return this.enableAutoPitch;
    }

    public boolean isMultiPOIMission() {
        return this.isMultiInterestPointMission;
    }

    public MultiInterestPointsMissionBuilder setPOI(Waypoint wp, LocationCoordinate3D poi) {
        if (poi != null) {
            if (this.waypointList == null) {
                this.waypointList = new ArrayList();
                addWaypoint(wp);
            } else if (!this.waypointList.contains(wp)) {
                addWaypoint(wp);
            }
            this.interestMap.put(wp, poi);
        }
        return this;
    }

    public MultiInterestPointsMissionBuilder setPOIs(Collection<Waypoint> wpList, LocationCoordinate3D poi) {
        for (Waypoint wp : wpList) {
            setPOI(wp, poi);
        }
        return this;
    }

    public MultiInterestPointsMissionBuilder removeInterestPointForWaypoint(Waypoint wp) {
        this.interestMap.remove(wp);
        return this;
    }

    public MultiInterestPointsMissionBuilder removeInterestPointForWaypoints(Collection<Waypoint> wpList) {
        for (Waypoint wp : wpList) {
            removeInterestPointForWaypoint(wp);
        }
        return this;
    }

    public LocationCoordinate3D getInterestPoint(Waypoint wp) {
        return this.interestMap.get(wp);
    }

    public Collection<LocationCoordinate3D> getInterestPoints() {
        return this.interestMap.values();
    }

    public WaypointMission build() {
        if (!this.isMultiInterestPointMission) {
            return super.build();
        }
        if (this.interestMap.size() != 0 || this.isUseCustomDirection) {
            this.headingMode = WaypointMissionHeadingMode.USING_WAYPOINT_HEADING;
        } else {
            this.headingMode = WaypointMissionHeadingMode.AUTO;
        }
        Waypoint wpLast = null;
        Waypoint wpLastSecond = null;
        float lastPitch = 0.0f;
        boolean pitchSet = false;
        for (Waypoint wp : this.waypointList) {
            if (wpLast != null && this.interestMap.get(wpLast) == null && !wpLast.isUseCustomDirection) {
                wpLast.heading = getAircraftHeadingToInterestPoint(wpLast, wp);
            }
            LocationCoordinate3D interestPoint = this.interestMap.get(wp);
            if (interestPoint != null) {
                if (this.enableAutoPitch) {
                    wp.gimbalPitch = getGimbalPitch(wp, interestPoint);
                    lastPitch = wp.gimbalPitch;
                    pitchSet = true;
                }
                wp.heading = getAircraftHeadingToInterestPoint(wp, interestPoint);
            } else if (wpLast != null) {
                if (pitchSet && this.enableAutoPitch) {
                    if (!this.isUseCustomGimbalPitch) {
                        wp.gimbalPitch = lastPitch;
                    }
                    lastPitch = wp.gimbalPitch;
                }
                if (!this.isUseCustomDirection) {
                    wp.heading = wpLast.heading;
                }
            }
            if (wpLast != null) {
                wpLast.turnMode = getTurnModeFromSourceToDest(wpLast.heading, wp.heading);
            }
            if (wpLastSecond != null) {
                wpLastSecond.turnMode = getTurnModeFromSourceToDest(wpLastSecond.heading, wpLast.heading);
            }
            wpLastSecond = wpLast;
            wpLast = wp;
        }
        return new WaypointMission(this);
    }

    private float getGimbalPitch(Waypoint wp, LocationCoordinate3D interestPoint) {
        if (interestPoint.getAltitude() > wp.altitude) {
            return 0.0f;
        }
        double distance = Math.abs(LocationUtils.getDistanceInMeterFromTwoGPSLocations(wp.coordinate.getLongitude(), wp.coordinate.getLatitude(), interestPoint.getLongitude(), interestPoint.getLatitude()));
        double diffOfHeight = (double) (wp.altitude - interestPoint.getAltitude());
        if (distance < Double.MIN_VALUE) {
            return -90.0f;
        }
        return (float) (Math.toDegrees(Math.atan(diffOfHeight / distance)) * -1.0d);
    }

    private int getAircraftHeadingToInterestPoint(Waypoint waypoint, Waypoint interestPoint) {
        return getAircraftHeadingToInterestPoint(waypoint, new LocationCoordinate3D(interestPoint.coordinate.getLatitude(), interestPoint.coordinate.getLongitude(), interestPoint.altitude));
    }

    private int getAircraftHeadingToInterestPoint(Waypoint waypoint, LocationCoordinate3D interestPoint) {
        return (int) getDirect(waypoint, interestPoint);
    }

    private double getDirect(Waypoint from, Waypoint to) {
        return getDirect(from, new LocationCoordinate3D(to.coordinate.getLatitude(), to.coordinate.getLongitude(), 0.0f));
    }

    private double getDirect(Waypoint from, LocationCoordinate3D to) {
        if (to == null || from == null) {
            return 0.0d;
        }
        double fromLat = Math.toRadians(from.coordinate.getLatitude());
        double fromLng = Math.toRadians(from.coordinate.getLongitude());
        double toLat = Math.toRadians(to.getLatitude());
        double dLng = Math.toRadians(to.getLongitude()) - fromLng;
        return wrap(Math.toDegrees(Math.atan2(Math.sin(dLng) * Math.cos(toLat), (Math.cos(fromLat) * Math.sin(toLat)) - ((Math.sin(fromLat) * Math.cos(toLat)) * Math.cos(dLng)))), -180.0d, 180.0d);
    }

    private static double wrap(double n, double min, double max) {
        return (n < min || n >= max) ? mod(n - min, max - min) + min : n;
    }

    private static double mod(double x, double m) {
        return ((x % m) + m) % m;
    }

    private WaypointTurnMode getTurnModeFromSourceToDest(int sourceHeading, int destHeading) {
        if (sourceHeading < 0) {
            sourceHeading += 360;
        }
        if (destHeading < 0) {
            destHeading += 360;
        }
        int differenceOfHeading = destHeading - sourceHeading;
        if (differenceOfHeading > 0 && differenceOfHeading < 180) {
            return WaypointTurnMode.CLOCKWISE;
        }
        if (differenceOfHeading < 0 && differenceOfHeading > -180) {
            return WaypointTurnMode.COUNTER_CLOCKWISE;
        }
        if (differenceOfHeading > 180) {
            return WaypointTurnMode.COUNTER_CLOCKWISE;
        }
        return WaypointTurnMode.CLOCKWISE;
    }

    public void clearALl() {
        this.interestMap.clear();
        this.waypointList.clear();
        this.waypointCount = 0;
    }
}
