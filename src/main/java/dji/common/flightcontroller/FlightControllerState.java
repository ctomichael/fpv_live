package dji.common.flightcontroller;

import android.support.annotation.NonNull;
import com.billy.cc.core.component.CCUtil;
import dji.common.flightcontroller.GoHomeAssessment;
import dji.common.model.LocationCoordinate2D;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FlightControllerState {
    private int aircraftHeadDirection;
    private LocationCoordinate3D aircraftLocation = new LocationCoordinate3D(Double.NaN, Double.NaN, Float.NaN);
    private Attitude attitude = new Attitude(Double.NaN, Double.NaN, Double.NaN);
    private BatteryThresholdBehavior batteryThresholdBehavior = BatteryThresholdBehavior.UNKNOWN;
    private boolean doesUltrasonicHaveError;
    private FlightMode flightMode = FlightMode.UNKNOWN;
    private String flightModeString = CCUtil.PROCESS_UNKNOWN;
    private int flightTimeInSeconds;
    private FlightWindWarning flightWindWarning = FlightWindWarning.UNKNOWN;
    private GoHomeAssessment goHomeAssessment;
    private GoHomeExecutionState goHomeExecutionState = GoHomeExecutionState.UNKNOWN;
    private int goHomeHeight;
    private GPSSignalLevel gpsSignalLevel = GPSSignalLevel.NONE;
    private boolean hasReachedMaxFlightHeight;
    private boolean hasReachedMaxFlightRadius;
    private LocationCoordinate2D homeLocation = new LocationCoordinate2D(Double.NaN, Double.NaN);
    private boolean isFailsafeEnabled;
    private boolean isFlying;
    private boolean isGoingHome;
    private boolean isHomeLocationSet;
    private boolean isIMUPreheating;
    private boolean isLowerThanBatteryWarningThreshold;
    private boolean isLowerThanSeriousBatteryWarningThreshold;
    private boolean isMultipModeOpen;
    private boolean isUltrasonicBeingUsed;
    private boolean isVisionPositioningSensorBeingUsed;
    private boolean islandingConfirmationNeeded;
    private boolean motorsOn;
    private FlightOrientationMode orientationMode = FlightOrientationMode.AIRCRAFT_HEADING;
    private int satelliteCount;
    private float takeoffLocationAltitude = Float.NaN;
    private float ultrasonicHeightInMeters = Float.NaN;
    private float velocityX = Float.NaN;
    private float velocityY = Float.NaN;
    private float velocityZ = Float.NaN;

    public interface Callback {
        void onUpdate(@NonNull FlightControllerState flightControllerState);
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        int i24;
        int i25;
        int i26;
        int i27;
        int i28;
        int i29;
        int i30 = 1;
        if (this.orientationMode != null) {
            result = this.orientationMode.hashCode();
        } else {
            result = 0;
        }
        int i31 = result * 31;
        if (this.attitude != null) {
            i = this.attitude.hashCode();
        } else {
            i = 0;
        }
        int i32 = (i31 + i) * 31;
        if (this.goHomeExecutionState != null) {
            i2 = this.goHomeExecutionState.hashCode();
        } else {
            i2 = 0;
        }
        int i33 = (i32 + i2) * 31;
        if (this.gpsSignalLevel != null) {
            i3 = this.gpsSignalLevel.hashCode();
        } else {
            i3 = 0;
        }
        int i34 = (i33 + i3) * 31;
        if (this.homeLocation != null) {
            i4 = this.homeLocation.hashCode();
        } else {
            i4 = 0;
        }
        int i35 = (i34 + i4) * 31;
        if (this.aircraftLocation != null) {
            i5 = this.aircraftLocation.hashCode();
        } else {
            i5 = 0;
        }
        int i36 = (i35 + i5) * 31;
        if (this.flightMode != null) {
            i6 = this.flightMode.hashCode();
        } else {
            i6 = 0;
        }
        int i37 = (i36 + i6) * 31;
        if (this.goHomeAssessment != null) {
            i7 = this.goHomeAssessment.hashCode();
        } else {
            i7 = 0;
        }
        int i38 = (i37 + i7) * 31;
        if (this.isGoingHome) {
            i8 = 1;
        } else {
            i8 = 0;
        }
        int i39 = (i38 + i8) * 31;
        if (this.isMultipModeOpen) {
            i9 = 1;
        } else {
            i9 = 0;
        }
        int i40 = (i39 + i9) * 31;
        if (this.hasReachedMaxFlightHeight) {
            i10 = 1;
        } else {
            i10 = 0;
        }
        int i41 = (i40 + i10) * 31;
        if (this.hasReachedMaxFlightRadius) {
            i11 = 1;
        } else {
            i11 = 0;
        }
        int i42 = (i41 + i11) * 31;
        if (this.isHomeLocationSet) {
            i12 = 1;
        } else {
            i12 = 0;
        }
        int i43 = (i42 + i12) * 31;
        if (this.isFailsafeEnabled) {
            i13 = 1;
        } else {
            i13 = 0;
        }
        int i44 = (i43 + i13) * 31;
        if (this.motorsOn) {
            i14 = 1;
        } else {
            i14 = 0;
        }
        int i45 = (i44 + i14) * 31;
        if (this.isUltrasonicBeingUsed) {
            i15 = 1;
        } else {
            i15 = 0;
        }
        int i46 = (i45 + i15) * 31;
        if (this.isIMUPreheating) {
            i16 = 1;
        } else {
            i16 = 0;
        }
        int i47 = (i46 + i16) * 31;
        if (this.isVisionPositioningSensorBeingUsed) {
            i17 = 1;
        } else {
            i17 = 0;
        }
        int i48 = (i47 + i17) * 31;
        if (this.doesUltrasonicHaveError) {
            i18 = 1;
        } else {
            i18 = 0;
        }
        int i49 = (i48 + i18) * 31;
        if (this.isFlying) {
            i19 = 1;
        } else {
            i19 = 0;
        }
        int i50 = (((((i49 + i19) * 31) + this.goHomeHeight) * 31) + this.aircraftHeadDirection) * 31;
        if (this.ultrasonicHeightInMeters != 0.0f) {
            i20 = Float.floatToIntBits(this.ultrasonicHeightInMeters);
        } else {
            i20 = 0;
        }
        int i51 = (i50 + i20) * 31;
        if (this.takeoffLocationAltitude != 0.0f) {
            i21 = Float.floatToIntBits(this.takeoffLocationAltitude);
        } else {
            i21 = 0;
        }
        int i52 = (((i51 + i21) * 31) + this.satelliteCount) * 31;
        if (this.flightModeString != null) {
            i22 = this.flightModeString.hashCode();
        } else {
            i22 = 0;
        }
        int i53 = (i52 + i22) * 31;
        if (this.batteryThresholdBehavior != null) {
            i23 = this.batteryThresholdBehavior.hashCode();
        } else {
            i23 = 0;
        }
        int i54 = (i53 + i23) * 31;
        if (this.velocityX != 0.0f) {
            i24 = Float.floatToIntBits(this.velocityX);
        } else {
            i24 = 0;
        }
        int i55 = (i54 + i24) * 31;
        if (this.velocityY != 0.0f) {
            i25 = Float.floatToIntBits(this.velocityY);
        } else {
            i25 = 0;
        }
        int i56 = (i55 + i25) * 31;
        if (this.velocityZ != 0.0f) {
            i26 = Float.floatToIntBits(this.velocityZ);
        } else {
            i26 = 0;
        }
        int i57 = (((i56 + i26) * 31) + this.flightTimeInSeconds) * 31;
        if (this.isLowerThanBatteryWarningThreshold) {
            i27 = 1;
        } else {
            i27 = 0;
        }
        int i58 = (i57 + i27) * 31;
        if (this.isLowerThanSeriousBatteryWarningThreshold) {
            i28 = 1;
        } else {
            i28 = 0;
        }
        int i59 = (i58 + i28) * 31;
        if (this.flightWindWarning != null) {
            i29 = this.flightWindWarning.hashCode();
        } else {
            i29 = 0;
        }
        int i60 = (i59 + i29) * 31;
        if (!this.islandingConfirmationNeeded) {
            i30 = 0;
        }
        return i60 + i30;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlightControllerState that = (FlightControllerState) o;
        if (this.isGoingHome != that.isGoingHome || this.isMultipModeOpen != that.isMultipModeOpen || this.hasReachedMaxFlightHeight != that.hasReachedMaxFlightHeight || this.hasReachedMaxFlightRadius != that.hasReachedMaxFlightRadius || this.isHomeLocationSet != that.isHomeLocationSet || this.isFailsafeEnabled != that.isFailsafeEnabled || this.motorsOn != that.motorsOn || this.isUltrasonicBeingUsed != that.isUltrasonicBeingUsed || this.isIMUPreheating != that.isIMUPreheating || this.isVisionPositioningSensorBeingUsed != that.isVisionPositioningSensorBeingUsed || this.doesUltrasonicHaveError != that.doesUltrasonicHaveError || this.isFlying != that.isFlying || this.goHomeHeight != that.goHomeHeight || this.aircraftHeadDirection != that.aircraftHeadDirection || Float.compare(that.ultrasonicHeightInMeters, this.ultrasonicHeightInMeters) != 0 || Float.compare(that.takeoffLocationAltitude, this.takeoffLocationAltitude) != 0 || this.satelliteCount != that.satelliteCount || Float.compare(that.velocityX, this.velocityX) != 0 || Float.compare(that.velocityY, this.velocityY) != 0 || Float.compare(that.velocityZ, this.velocityZ) != 0 || this.flightTimeInSeconds != that.flightTimeInSeconds || this.isLowerThanBatteryWarningThreshold != that.isLowerThanBatteryWarningThreshold || this.isLowerThanSeriousBatteryWarningThreshold != that.isLowerThanSeriousBatteryWarningThreshold || this.islandingConfirmationNeeded != that.islandingConfirmationNeeded || this.orientationMode != that.orientationMode) {
            return false;
        }
        if (this.attitude != null) {
            if (!this.attitude.equals(that.attitude)) {
                return false;
            }
        } else if (that.attitude != null) {
            return false;
        }
        if (this.goHomeExecutionState != that.goHomeExecutionState || this.gpsSignalLevel != that.gpsSignalLevel) {
            return false;
        }
        if (this.homeLocation != null) {
            if (!this.homeLocation.equals(that.homeLocation)) {
                return false;
            }
        } else if (that.homeLocation != null) {
            return false;
        }
        if (this.aircraftLocation != null) {
            if (!this.aircraftLocation.equals(that.aircraftLocation)) {
                return false;
            }
        } else if (that.aircraftLocation != null) {
            return false;
        }
        if (this.flightMode != that.flightMode) {
            return false;
        }
        if (this.goHomeAssessment != null) {
            if (!this.goHomeAssessment.equals(that.goHomeAssessment)) {
                return false;
            }
        } else if (that.goHomeAssessment != null) {
            return false;
        }
        if (this.flightModeString != null) {
            if (!this.flightModeString.equals(that.flightModeString)) {
                return false;
            }
        } else if (that.flightModeString != null) {
            return false;
        }
        if (this.batteryThresholdBehavior != that.batteryThresholdBehavior) {
            return false;
        }
        if (this.flightWindWarning != that.flightWindWarning) {
            z = false;
        }
        return z;
    }

    public float getVelocityX() {
        return this.velocityX;
    }

    public void setVelocityX(float velocityX2) {
        this.velocityX = velocityX2;
    }

    public float getVelocityY() {
        return this.velocityY;
    }

    public void setVelocityY(float velocityY2) {
        this.velocityY = velocityY2;
    }

    public float getVelocityZ() {
        return this.velocityZ;
    }

    public void setVelocityZ(float velocityZ2) {
        this.velocityZ = velocityZ2;
    }

    public BatteryThresholdBehavior getBatteryThresholdBehavior() {
        return this.batteryThresholdBehavior;
    }

    public void setBatteryThresholdBehavior(BatteryThresholdBehavior batteryThresholdBehavior2) {
        this.batteryThresholdBehavior = batteryThresholdBehavior2;
    }

    public LocationCoordinate2D getHomeLocation() {
        return this.homeLocation;
    }

    public void setHomeLocation(LocationCoordinate2D homeLocation2) {
        this.homeLocation = homeLocation2;
    }

    public float getTakeoffLocationAltitude() {
        return this.takeoffLocationAltitude;
    }

    public void setTakeoffLocationAltitude(float takeoffLocationAltitude2) {
        this.takeoffLocationAltitude = takeoffLocationAltitude2;
    }

    public FlightOrientationMode getOrientationMode() {
        return this.orientationMode;
    }

    public void setOrientationMode(FlightOrientationMode orientationMode2) {
        this.orientationMode = orientationMode2;
    }

    public boolean isGoingHome() {
        return this.isGoingHome;
    }

    public void setGoingHome(boolean isGoingHome2) {
        this.isGoingHome = isGoingHome2;
    }

    public GoHomeExecutionState getGoHomeExecutionState() {
        return this.goHomeExecutionState;
    }

    public void setGoHomeExecutionState(GoHomeExecutionState goHomeExecutionState2) {
        this.goHomeExecutionState = goHomeExecutionState2;
    }

    public boolean isMultipleModeOpen() {
        return this.isMultipModeOpen;
    }

    public void setMultipModeOpen(boolean isMultipModeOpen2) {
        this.isMultipModeOpen = isMultipModeOpen2;
    }

    public boolean hasReachedMaxFlightHeight() {
        return this.hasReachedMaxFlightHeight;
    }

    public void setHasReachedMaxFlightHeight(boolean hasReachedMaxFlightHeight2) {
        this.hasReachedMaxFlightHeight = hasReachedMaxFlightHeight2;
    }

    public boolean hasReachedMaxFlightRadius() {
        return this.hasReachedMaxFlightRadius;
    }

    public void setHasReachedMaxFlightRadius(boolean hasReachedMaxFlightRadius2) {
        this.hasReachedMaxFlightRadius = hasReachedMaxFlightRadius2;
    }

    public int getAircraftHeadDirection() {
        return this.aircraftHeadDirection;
    }

    public void setAircraftHeadDirection(int aircraftHeadDirection2) {
        this.aircraftHeadDirection = aircraftHeadDirection2;
    }

    public boolean isHomeLocationSet() {
        return this.isHomeLocationSet;
    }

    public void setHomeLocationSet(boolean isHomeLocationSet2) {
        this.isHomeLocationSet = isHomeLocationSet2;
    }

    public int getGoHomeHeight() {
        return this.goHomeHeight;
    }

    public void setGoHomeHeight(int goHomeHeight2) {
        this.goHomeHeight = goHomeHeight2;
    }

    public int getSatelliteCount() {
        return this.satelliteCount;
    }

    public void setSatelliteCount(int satelliteCount2) {
        this.satelliteCount = satelliteCount2;
    }

    public LocationCoordinate3D getAircraftLocation() {
        return this.aircraftLocation;
    }

    public void setAircraftLocation(LocationCoordinate3D aircraftLocation2) {
        this.aircraftLocation = aircraftLocation2;
    }

    public Attitude getAttitude() {
        return this.attitude;
    }

    public void setAttitude(Attitude attitude2) {
        this.attitude = attitude2;
    }

    public boolean isFailsafeEnabled() {
        return this.isFailsafeEnabled;
    }

    public void setFailsafeEnabled(boolean isFailsafeEnabled2) {
        this.isFailsafeEnabled = isFailsafeEnabled2;
    }

    public FlightMode getFlightMode() {
        return this.flightMode;
    }

    public void setFlightMode(FlightMode flightMode2) {
        this.flightMode = flightMode2;
    }

    public boolean isFlying() {
        return this.isFlying;
    }

    public void setFlying(boolean isFlying2) {
        this.isFlying = isFlying2;
    }

    public boolean areMotorsOn() {
        return this.motorsOn;
    }

    public void setMotorsOn(boolean motorsOn2) {
        this.motorsOn = motorsOn2;
    }

    public boolean isUltrasonicBeingUsed() {
        return this.isUltrasonicBeingUsed;
    }

    public void setUltrasonicBeingUsed(boolean isUltrasonicBeingUsed2) {
        this.isUltrasonicBeingUsed = isUltrasonicBeingUsed2;
    }

    public boolean isIMUPreheating() {
        return this.isIMUPreheating;
    }

    public void setIMUPreheating(boolean isIMUPreheating2) {
        this.isIMUPreheating = isIMUPreheating2;
    }

    public boolean isVisionPositioningSensorBeingUsed() {
        return this.isVisionPositioningSensorBeingUsed;
    }

    public void setVisionPositioningSensorBeingUsed(boolean isVisionSensorBeingUsed) {
        this.isVisionPositioningSensorBeingUsed = isVisionSensorBeingUsed;
    }

    public boolean doesUltrasonicHaveError() {
        return this.doesUltrasonicHaveError;
    }

    public void setDoesUltrasonicHaveError(boolean doesUltrasonicHaveError2) {
        this.doesUltrasonicHaveError = doesUltrasonicHaveError2;
    }

    public GPSSignalLevel getGPSSignalLevel() {
        return this.gpsSignalLevel;
    }

    public void setGPSSignalLevel(GPSSignalLevel gpsSignalLevel2) {
        this.gpsSignalLevel = gpsSignalLevel2;
    }

    public float getUltrasonicHeightInMeters() {
        return this.ultrasonicHeightInMeters;
    }

    public void setUltrasonicHeightInMeters(float ultrasonicHeightInMeters2) {
        this.ultrasonicHeightInMeters = ultrasonicHeightInMeters2;
    }

    public String getFlightModeString() {
        return this.flightModeString;
    }

    public void setFlightModeString(String flightModeString2) {
        this.flightModeString = flightModeString2;
    }

    public GoHomeAssessment getGoHomeAssessment() {
        if (this.goHomeAssessment == null) {
            return new GoHomeAssessment.Builder().build();
        }
        return this.goHomeAssessment;
    }

    public void setGoHomeAssessment(GoHomeAssessment goHomeAssessment2) {
        this.goHomeAssessment = goHomeAssessment2;
    }

    public int getFlightTimeInSeconds() {
        return this.flightTimeInSeconds;
    }

    public void setFlightTimeInSeconds(int flightTimeInSeconds2) {
        this.flightTimeInSeconds = flightTimeInSeconds2;
    }

    public boolean isLandingConfirmationNeeded() {
        return this.islandingConfirmationNeeded;
    }

    public void setIslandingConfirmationNeeded(boolean islandingConfirmationNeeded2) {
        this.islandingConfirmationNeeded = islandingConfirmationNeeded2;
    }

    public boolean isLowerThanBatteryWarningThreshold() {
        return this.isLowerThanBatteryWarningThreshold;
    }

    public void setLowerThanBatteryWarningThreshold(boolean lowerThanBatteryWarningThreshold) {
        this.isLowerThanBatteryWarningThreshold = lowerThanBatteryWarningThreshold;
    }

    public boolean isLowerThanSeriousBatteryWarningThreshold() {
        return this.isLowerThanSeriousBatteryWarningThreshold;
    }

    public void setLowerThanSeriousBatteryWarningThreshold(boolean lowerThanSeriousBatteryWarningThreshold) {
        this.isLowerThanSeriousBatteryWarningThreshold = lowerThanSeriousBatteryWarningThreshold;
    }

    public FlightWindWarning getFlightWindWarning() {
        return this.flightWindWarning;
    }

    public void setFlightWindWarning(FlightWindWarning flightWindWarning2) {
        this.flightWindWarning = flightWindWarning2;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("}");
        sb.append("\r\n");
        return sb.toString();
    }
}
