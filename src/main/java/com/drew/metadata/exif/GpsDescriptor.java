package com.drew.metadata.exif;

import com.drew.lang.GeoLocation;
import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;
import com.mapbox.turf.TurfConstants;
import dji.thirdparty.sanselan.formats.tiff.constants.GPSTagConstants;
import java.text.DecimalFormat;

public class GpsDescriptor extends TagDescriptor<GpsDirectory> {
    public GpsDescriptor(@NotNull GpsDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 0:
                return getGpsVersionIdDescription();
            case 1:
            case 3:
            case 8:
            case 11:
            case 13:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 26:
            case 27:
            case 28:
            case 29:
            default:
                return super.getDescription(tagType);
            case 2:
                return getGpsLatitudeDescription();
            case 4:
                return getGpsLongitudeDescription();
            case 5:
                return getGpsAltitudeRefDescription();
            case 6:
                return getGpsAltitudeDescription();
            case 7:
                return getGpsTimeStampDescription();
            case 9:
                return getGpsStatusDescription();
            case 10:
                return getGpsMeasureModeDescription();
            case 12:
                return getGpsSpeedRefDescription();
            case 14:
            case 16:
            case 23:
                return getGpsDirectionReferenceDescription(tagType);
            case 15:
            case 17:
            case 24:
                return getGpsDirectionDescription(tagType);
            case 25:
                return getGpsDestinationReferenceDescription();
            case 30:
                return getGpsDifferentialDescription();
        }
    }

    @Nullable
    private String getGpsVersionIdDescription() {
        return getVersionBytesDescription(0, 1);
    }

    @Nullable
    public String getGpsLatitudeDescription() {
        GeoLocation location = ((GpsDirectory) this._directory).getGeoLocation();
        if (location == null) {
            return null;
        }
        return GeoLocation.decimalToDegreesMinutesSecondsString(location.getLatitude());
    }

    @Nullable
    public String getGpsLongitudeDescription() {
        GeoLocation location = ((GpsDirectory) this._directory).getGeoLocation();
        if (location == null) {
            return null;
        }
        return GeoLocation.decimalToDegreesMinutesSecondsString(location.getLongitude());
    }

    @Nullable
    public String getGpsTimeStampDescription() {
        Rational[] timeComponents = ((GpsDirectory) this._directory).getRationalArray(7);
        DecimalFormat df = new DecimalFormat("00.000");
        if (timeComponents == null) {
            return null;
        }
        return String.format("%02d:%02d:%s UTC", Integer.valueOf(timeComponents[0].intValue()), Integer.valueOf(timeComponents[1].intValue()), df.format(timeComponents[2].doubleValue()));
    }

    @Nullable
    public String getGpsDestinationReferenceDescription() {
        String value = ((GpsDirectory) this._directory).getString(25);
        if (value == null) {
            return null;
        }
        String distanceRef = value.trim();
        if ("K".equalsIgnoreCase(distanceRef)) {
            return "kilometers";
        }
        if ("M".equalsIgnoreCase(distanceRef)) {
            return TurfConstants.UNIT_MILES;
        }
        if ("N".equalsIgnoreCase(distanceRef)) {
            return "knots";
        }
        return "Unknown (" + distanceRef + ")";
    }

    @Nullable
    public String getGpsDirectionDescription(int tagType) {
        Rational angle = ((GpsDirectory) this._directory).getRational(tagType);
        String value = angle != null ? new DecimalFormat("0.##").format(angle.doubleValue()) : ((GpsDirectory) this._directory).getString(tagType);
        if (value == null || value.trim().length() == 0) {
            return null;
        }
        return value.trim() + " degrees";
    }

    @Nullable
    public String getGpsDirectionReferenceDescription(int tagType) {
        String value = ((GpsDirectory) this._directory).getString(tagType);
        if (value == null) {
            return null;
        }
        String gpsDistRef = value.trim();
        if ("T".equalsIgnoreCase(gpsDistRef)) {
            return "True direction";
        }
        if ("M".equalsIgnoreCase(gpsDistRef)) {
            return "Magnetic direction";
        }
        return "Unknown (" + gpsDistRef + ")";
    }

    @Nullable
    public String getGpsSpeedRefDescription() {
        String value = ((GpsDirectory) this._directory).getString(12);
        if (value == null) {
            return null;
        }
        String gpsSpeedRef = value.trim();
        if ("K".equalsIgnoreCase(gpsSpeedRef)) {
            return "kph";
        }
        if ("M".equalsIgnoreCase(gpsSpeedRef)) {
            return "mph";
        }
        if ("N".equalsIgnoreCase(gpsSpeedRef)) {
            return "knots";
        }
        return "Unknown (" + gpsSpeedRef + ")";
    }

    @Nullable
    public String getGpsMeasureModeDescription() {
        String value = ((GpsDirectory) this._directory).getString(10);
        if (value == null) {
            return null;
        }
        String gpsSpeedMeasureMode = value.trim();
        if ("2".equalsIgnoreCase(gpsSpeedMeasureMode)) {
            return "2-dimensional measurement";
        }
        if ("3".equalsIgnoreCase(gpsSpeedMeasureMode)) {
            return "3-dimensional measurement";
        }
        return "Unknown (" + gpsSpeedMeasureMode + ")";
    }

    @Nullable
    public String getGpsStatusDescription() {
        String value = ((GpsDirectory) this._directory).getString(9);
        if (value == null) {
            return null;
        }
        String gpsStatus = value.trim();
        if (GPSTagConstants.GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_IN_PROGRESS.equalsIgnoreCase(gpsStatus)) {
            return "Active (Measurement in progress)";
        }
        if (GPSTagConstants.GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_INTEROPERABILITY.equalsIgnoreCase(gpsStatus)) {
            return "Void (Measurement Interoperability)";
        }
        return "Unknown (" + gpsStatus + ")";
    }

    @Nullable
    public String getGpsAltitudeRefDescription() {
        return getIndexedDescription(5, "Sea level", "Below sea level");
    }

    @Nullable
    public String getGpsAltitudeDescription() {
        Rational value = ((GpsDirectory) this._directory).getRational(6);
        if (value == null) {
            return null;
        }
        return value.intValue() + " metres";
    }

    @Nullable
    public String getGpsDifferentialDescription() {
        return getIndexedDescription(30, "No Correction", "Differential Corrected");
    }

    @Nullable
    public String getDegreesMinutesSecondsDescription() {
        GeoLocation location = ((GpsDirectory) this._directory).getGeoLocation();
        if (location == null) {
            return null;
        }
        return location.toDMSString();
    }
}
