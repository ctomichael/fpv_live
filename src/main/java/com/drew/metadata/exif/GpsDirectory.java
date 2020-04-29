package com.drew.metadata.exif;

import com.drew.lang.GeoLocation;
import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class GpsDirectory extends ExifDirectoryBase {
    public static final int TAG_ALTITUDE = 6;
    public static final int TAG_ALTITUDE_REF = 5;
    public static final int TAG_AREA_INFORMATION = 28;
    public static final int TAG_DATE_STAMP = 29;
    public static final int TAG_DEST_BEARING = 24;
    public static final int TAG_DEST_BEARING_REF = 23;
    public static final int TAG_DEST_DISTANCE = 26;
    public static final int TAG_DEST_DISTANCE_REF = 25;
    public static final int TAG_DEST_LATITUDE = 20;
    public static final int TAG_DEST_LATITUDE_REF = 19;
    public static final int TAG_DEST_LONGITUDE = 22;
    public static final int TAG_DEST_LONGITUDE_REF = 21;
    public static final int TAG_DIFFERENTIAL = 30;
    public static final int TAG_DOP = 11;
    public static final int TAG_IMG_DIRECTION = 17;
    public static final int TAG_IMG_DIRECTION_REF = 16;
    public static final int TAG_LATITUDE = 2;
    public static final int TAG_LATITUDE_REF = 1;
    public static final int TAG_LONGITUDE = 4;
    public static final int TAG_LONGITUDE_REF = 3;
    public static final int TAG_MAP_DATUM = 18;
    public static final int TAG_MEASURE_MODE = 10;
    public static final int TAG_PROCESSING_METHOD = 27;
    public static final int TAG_SATELLITES = 8;
    public static final int TAG_SPEED = 13;
    public static final int TAG_SPEED_REF = 12;
    public static final int TAG_STATUS = 9;
    public static final int TAG_TIME_STAMP = 7;
    public static final int TAG_TRACK = 15;
    public static final int TAG_TRACK_REF = 14;
    public static final int TAG_VERSION_ID = 0;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        addExifTagNames(_tagNameMap);
        _tagNameMap.put(0, "GPS Version ID");
        _tagNameMap.put(1, "GPS Latitude Ref");
        _tagNameMap.put(2, "GPS Latitude");
        _tagNameMap.put(3, "GPS Longitude Ref");
        _tagNameMap.put(4, "GPS Longitude");
        _tagNameMap.put(5, "GPS Altitude Ref");
        _tagNameMap.put(6, "GPS Altitude");
        _tagNameMap.put(7, "GPS Time-Stamp");
        _tagNameMap.put(8, "GPS Satellites");
        _tagNameMap.put(9, "GPS Status");
        _tagNameMap.put(10, "GPS Measure Mode");
        _tagNameMap.put(11, "GPS DOP");
        _tagNameMap.put(12, "GPS Speed Ref");
        _tagNameMap.put(13, "GPS Speed");
        _tagNameMap.put(14, "GPS Track Ref");
        _tagNameMap.put(15, "GPS Track");
        _tagNameMap.put(16, "GPS Img Direction Ref");
        _tagNameMap.put(17, "GPS Img Direction");
        _tagNameMap.put(18, "GPS Map Datum");
        _tagNameMap.put(19, "GPS Dest Latitude Ref");
        _tagNameMap.put(20, "GPS Dest Latitude");
        _tagNameMap.put(21, "GPS Dest Longitude Ref");
        _tagNameMap.put(22, "GPS Dest Longitude");
        _tagNameMap.put(23, "GPS Dest Bearing Ref");
        _tagNameMap.put(24, "GPS Dest Bearing");
        _tagNameMap.put(25, "GPS Dest Distance Ref");
        _tagNameMap.put(26, "GPS Dest Distance");
        _tagNameMap.put(27, "GPS Processing Method");
        _tagNameMap.put(28, "GPS Area Information");
        _tagNameMap.put(29, "GPS Date Stamp");
        _tagNameMap.put(30, "GPS Differential");
    }

    public GpsDirectory() {
        setDescriptor(new GpsDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "GPS";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }

    @Nullable
    public GeoLocation getGeoLocation() {
        Rational[] latitudes = getRationalArray(2);
        Rational[] longitudes = getRationalArray(4);
        String latitudeRef = getString(1);
        String longitudeRef = getString(3);
        if (latitudes == null || latitudes.length != 3 || longitudes == null || longitudes.length != 3 || latitudeRef == null || longitudeRef == null) {
            return null;
        }
        Double lat = GeoLocation.degreesMinutesSecondsToDecimal(latitudes[0], latitudes[1], latitudes[2], latitudeRef.equalsIgnoreCase("S"));
        Double lon = GeoLocation.degreesMinutesSecondsToDecimal(longitudes[0], longitudes[1], longitudes[2], longitudeRef.equalsIgnoreCase("W"));
        if (lat == null || lon == null) {
            return null;
        }
        return new GeoLocation(lat.doubleValue(), lon.doubleValue());
    }

    @Nullable
    public Date getGpsDate() {
        String date = getString(29);
        Rational[] timeComponents = getRationalArray(7);
        if (date == null || timeComponents == null || timeComponents.length != 3) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy:MM:dd HH:mm:ss.S z").parse(String.format(Locale.US, "%s %02d:%02d:%02.3f UTC", date, Integer.valueOf(timeComponents[0].intValue()), Integer.valueOf(timeComponents[1].intValue()), Double.valueOf(timeComponents[2].doubleValue())));
        } catch (ParseException e) {
            return null;
        }
    }
}
