package com.amap.location.common.model;

import android.location.Location;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import dji.publics.LogReport.base.Fields;
import dji.publics.protocol.ResponseBase;
import org.json.JSONObject;

public class AmapLoc {
    public static final int COORD_GD = 1;
    public static final int COORD_UNKNOWN = -1;
    public static final int COORD_WGS84 = 0;
    public static final int LOC_TYPE_INT_CACHE = 3;
    public static final int LOC_TYPE_INT_LAST = 2;
    public static final int LOC_TYPE_INT_NEW = 1;
    public static final int LOC_TYPE_INT_OFFLINE = 4;
    public static final int LOC_TYPE_INT_UNKNOW = 0;
    public static final String RESULT_TYPE_AMAP_INDOOR = "-1";
    public static final String RESULT_TYPE_CAS_INDOOR = "-3";
    public static final String RESULT_TYPE_CELL_ONLY = "3";
    public static final String RESULT_TYPE_CELL_WITHIN_SAME_ADDRESS = "9";
    public static final String RESULT_TYPE_CELL_WITH_NEIGHBORS = "4";
    public static final String RESULT_TYPE_FAIL = "8";
    public static final String RESULT_TYPE_FUSED = "2";
    public static final String RESULT_TYPE_GOOGLE = "-2";
    public static final String RESULT_TYPE_GPS = "0";
    public static final String RESULT_TYPE_NEW_FUSED = "24";
    public static final String RESULT_TYPE_NEW_WIFI_ONLY = "14";
    @Deprecated
    public static final String RESULT_TYPE_NO_LONGER_USED = "6";
    public static final String RESULT_TYPE_SELF_LAT_LON = "5";
    public static final String RESULT_TYPE_SKYHOOK = "-4";
    public static final String RESULT_TYPE_STANDARD = "-5";
    public static final String RESULT_TYPE_WIFI_ONLY = "1";
    private static final int SERVER_PARSE_REQUEST_ERROR = 1;
    public static final String TYPE_CACHE = "mem";
    public static final String TYPE_NEW = "new";
    public static final String TYPE_OFFLINE_CELL = "file";
    public static final String TYPE_OFFLINE_WIFI = "wifioff";
    public static final String TYPE_OFFLINE_WIFI_V3 = "wifioffv3";
    public static String sCxtFromServer = null;
    private float accuracy = 0.0f;
    private String adcode = "";
    private double altitude = 0.0d;
    private String aoiname = "";
    private float bearing = 0.0f;
    private String cens = null;
    private String city = "";
    private String citycode = "";
    private int coord = -1;
    private String country = "";
    private String desc = "";
    private String district = "";
    private JSONObject extra = null;
    private String floor = "";
    private boolean isError = false;
    private boolean isLast;
    private double lat = 0.0d;
    private double lon = 0.0d;
    private int mServerError;
    private String mcell = "";
    private String number = "";
    private float optimizedAccuracy = 0.0f;
    private String poiid = "";
    private String poiname = "";
    private String provider = "";
    private String province = "";
    private String rdesc = "";
    private String resubtype = "";
    private String retype = "";
    private String road = "";
    private int scenarioConfidence = -1;
    private byte[] serverFilterContext = null;
    private float speed = 0.0f;
    private String street = "";
    private long time = 0;
    private String type = TYPE_NEW;

    public AmapLoc() {
    }

    public AmapLoc(AmapLoc amapLoc) {
        if (amapLoc != null) {
            try {
                this.provider = amapLoc.provider;
                this.lon = amapLoc.lon;
                this.lat = amapLoc.lat;
                this.altitude = amapLoc.altitude;
                this.accuracy = amapLoc.accuracy;
                this.speed = amapLoc.speed;
                this.bearing = amapLoc.bearing;
                this.time = amapLoc.time;
                this.type = amapLoc.type;
                this.retype = amapLoc.retype;
                this.rdesc = amapLoc.rdesc;
                this.citycode = amapLoc.citycode;
                this.desc = amapLoc.desc;
                this.adcode = amapLoc.adcode;
                this.country = amapLoc.country;
                this.province = amapLoc.province;
                this.city = amapLoc.city;
                this.district = amapLoc.district;
                this.road = amapLoc.road;
                this.street = amapLoc.street;
                this.number = amapLoc.number;
                this.aoiname = amapLoc.aoiname;
                this.poiname = amapLoc.poiname;
                this.cens = amapLoc.cens;
                this.poiid = amapLoc.poiid;
                this.floor = amapLoc.floor;
                this.isError = amapLoc.isError;
                this.coord = amapLoc.coord;
                this.mcell = amapLoc.mcell;
                this.extra = amapLoc.extra;
                this.scenarioConfidence = amapLoc.scenarioConfidence;
                this.resubtype = amapLoc.resubtype;
                this.isLast = amapLoc.isLast;
            } catch (Exception e) {
            }
        }
    }

    public AmapLoc(JSONObject jSONObject) {
        if (jSONObject != null) {
            try {
                setProvider(jSONObject.getString(ResponseBase.STRING_PROVIDER));
                setLon(jSONObject.getDouble("lon"));
                setLat(jSONObject.getDouble(ResponseBase.STRING_LAT));
                if (jSONObject.has("altitude")) {
                    setAltitude(jSONObject.getDouble("altitude"));
                }
                setAccuracy((float) jSONObject.getLong("accuracy"));
                setSpeed((float) jSONObject.getLong(Fields.Dgo_poi_2_running_click_go.speed));
                setBearing((float) jSONObject.getLong("bearing"));
                setType(jSONObject.getString("type"));
                setRetype(jSONObject.getString("retype"));
                setRdesc(jSONObject.getString("rdesc"));
                setCitycode(jSONObject.getString("citycode"));
                setDesc(jSONObject.getString("desc"));
                setAdcode(jSONObject.getString("adcode"));
                setCountry(jSONObject.getString("country"));
                setProvince(jSONObject.getString(ResponseBase.STRING_PROVINCE));
                setCity(jSONObject.getString(ResponseBase.STRING_CITY));
                setRoad(jSONObject.getString("road"));
                setStreet(jSONObject.getString("street"));
                setNumber(jSONObject.getString("number"));
                setAoiname(jSONObject.getString("aoiname"));
                setPoiname(jSONObject.getString("poiname"));
                if (jSONObject.has("cens")) {
                    setCens(jSONObject.getString("cens"));
                }
                if (jSONObject.has("poiid")) {
                    setPoiid(jSONObject.getString("poiid"));
                }
                if (jSONObject.has("floor")) {
                    setFloor(jSONObject.getString("floor"));
                }
                if (jSONObject.has("coord")) {
                    setCoord(jSONObject.getString("coord"));
                }
                if (jSONObject.has("mcell")) {
                    setMcell(jSONObject.getString("mcell"));
                }
                if (jSONObject.has("time")) {
                    setTime(jSONObject.getLong("time"));
                }
                if (jSONObject.has("district")) {
                    setDistrict(jSONObject.getString("district"));
                }
                if (jSONObject.has("scenarioConfidence")) {
                    setScenarioConfidence(jSONObject.optInt("scenarioConfidence"));
                }
                if (jSONObject.has("resubtype")) {
                    setSubType(jSONObject.optString("resubtype"));
                }
                if (jSONObject.has("isLast")) {
                    setIsLast(jSONObject.optBoolean("isLast"));
                }
            } catch (Exception e) {
            }
        }
    }

    public static int getLocType(AmapLoc amapLoc) {
        if (amapLoc == null) {
            return 0;
        }
        if (amapLoc.getIsLast()) {
            return 2;
        }
        if (TYPE_NEW.equals(amapLoc.getType())) {
            return 1;
        }
        if (TYPE_CACHE.equals(amapLoc.getType())) {
            return 3;
        }
        return (TYPE_OFFLINE_CELL.equals(amapLoc.getType()) || TYPE_OFFLINE_WIFI.equals(amapLoc.getType())) ? 4 : 0;
    }

    public static boolean isLocationCorrect(Location location) {
        if (location == null) {
            return false;
        }
        boolean z = true;
        try {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            boolean equals = "gps".equals(location.getProvider());
            boolean hasAccuracy = location.hasAccuracy();
            if ((longitude == 0.0d && latitude == 0.0d) || longitude > 180.0d || latitude > 90.0d || longitude < -180.0d || latitude < -90.0d) {
                return false;
            }
            if (equals && hasAccuracy && location.getAccuracy() < -1.0E-8f) {
                return false;
            }
            if (!equals && hasAccuracy && location.getAccuracy() <= 0.0f) {
                return false;
            }
            if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
                z = false;
            }
            return z;
        } catch (Throwable th) {
            return false;
        }
    }

    private void setAccuracy(String str) {
        this.accuracy = Float.parseFloat(str);
    }

    public float getAccuracy() {
        return this.accuracy;
    }

    public String getAdcode() {
        return this.adcode;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public String getAoiname() {
        return this.aoiname;
    }

    public float getBearing() {
        return this.bearing;
    }

    public String getCens() {
        return this.cens;
    }

    public String getCity() {
        return this.city;
    }

    public String getCitycode() {
        return this.citycode;
    }

    public int getCoord() {
        return this.coord;
    }

    public String getCountry() {
        return this.country;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getDistrict() {
        return this.district;
    }

    public JSONObject getExtra() {
        return this.extra;
    }

    public String getFloor() {
        return this.floor;
    }

    public boolean getIsError() {
        return this.isError;
    }

    public boolean getIsLast() {
        return this.isLast;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    public String getMcell() {
        return this.mcell;
    }

    public AmapLoc getMcellLoc() {
        String mcell2 = getMcell();
        if (TextUtils.isEmpty(mcell2)) {
            return null;
        }
        String[] split = mcell2.split(",");
        if (split.length != 3) {
            return null;
        }
        AmapLoc amapLoc = new AmapLoc();
        amapLoc.setProvider(getProvider());
        amapLoc.setLon(split[0]);
        amapLoc.setLat(split[1]);
        amapLoc.setAccuracy(Float.parseFloat(split[2]));
        amapLoc.setCitycode(getCitycode());
        amapLoc.setAdcode(getAdcode());
        amapLoc.setCountry(getCountry());
        amapLoc.setProvince(getProvince());
        amapLoc.setCity(getCity());
        amapLoc.setDistrict(getDistrict());
        amapLoc.setTime(getTime());
        amapLoc.setType(getType());
        amapLoc.setCoord(String.valueOf(getCoord()));
        if (amapLoc.isLocationCorrect()) {
            return amapLoc;
        }
        return null;
    }

    public String getNumber() {
        return this.number;
    }

    public float getOptimizedAccuracy() {
        return this.optimizedAccuracy;
    }

    public String getPoiid() {
        return this.poiid;
    }

    public String getPoiname() {
        return this.poiname;
    }

    public String getProvider() {
        return this.provider;
    }

    public String getProvince() {
        return this.province;
    }

    public String getRdesc() {
        return this.rdesc;
    }

    public String getRetype() {
        return this.retype;
    }

    public String getRoad() {
        return this.road;
    }

    public int getScenarioConfidence() {
        return this.scenarioConfidence;
    }

    public byte[] getServerFilterContext() {
        return this.serverFilterContext;
    }

    public float getSpeed() {
        return this.speed;
    }

    public String getStreet() {
        return this.street;
    }

    public String getSubType() {
        return this.resubtype;
    }

    public long getTime() {
        return this.time;
    }

    public String getType() {
        return this.type;
    }

    public boolean hasAccuracy() {
        return this.accuracy > 0.0f;
    }

    public boolean hasAltitude() {
        return this.altitude > 0.0d;
    }

    public boolean hasBearing() {
        return this.bearing > 0.0f;
    }

    public boolean hasSpeed() {
        return this.speed > 0.0f;
    }

    public boolean isLocationCorrect() {
        if (!getRetype().equals("8") && !getRetype().equals("5") && !getRetype().equals("6")) {
            double lon2 = getLon();
            double lat2 = getLat();
            float accuracy2 = getAccuracy();
            if (!(lon2 == 0.0d && lat2 == 0.0d && ((double) accuracy2) == 0.0d)) {
                if (lon2 > 180.0d || lat2 > 90.0d) {
                    return false;
                }
                if (lon2 >= -180.0d && lat2 >= -90.0d) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isServerParseRequestError() {
        return this.mServerError == 1;
    }

    public void resetServerFilterContext() {
        this.serverFilterContext = null;
    }

    public void setAccuracy(float f) {
        setAccuracy(String.valueOf(Math.round(f)));
    }

    public void setAdcode(String str) {
        this.adcode = str;
    }

    public void setAltitude(double d) {
        this.altitude = d;
    }

    public void setAoiname(String str) {
        this.aoiname = str;
    }

    public void setBearing(float f) {
        this.bearing = (f * 10.0f) / 10.0f;
    }

    public void setBuiltInLocationAdjust(Location location) {
        if (isLocationCorrect(location)) {
            this.lat = location.getLatitude();
            this.lon = location.getLongitude();
            this.altitude = location.getAltitude();
            this.speed = location.getSpeed();
            this.bearing = location.getBearing();
            this.accuracy = location.getAccuracy();
        }
    }

    public void setCens(String str) {
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("\\*");
            int length = split.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String str2 = split[i];
                if (!TextUtils.isEmpty(str2)) {
                    String[] split2 = str2.split(",");
                    setLon(Double.parseDouble(split2[0]));
                    setLat(Double.parseDouble(split2[1]));
                    setAccuracy((float) Integer.parseInt(split2[2]));
                    break;
                }
                i++;
            }
            this.cens = str;
        }
    }

    public void setCity(String str) {
        this.city = str;
    }

    public void setCitycode(String str) {
        this.citycode = str;
    }

    public void setCoord(int i) {
        setCoord(String.valueOf(i));
    }

    public void setCoord(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (this.provider.equals("gps")) {
                this.coord = 0;
                return;
            } else if (str.equals("0")) {
                this.coord = 0;
                return;
            } else if (str.equals("1")) {
                this.coord = 1;
                return;
            }
        }
        this.coord = -1;
    }

    public void setCountry(String str) {
        this.country = str;
    }

    public void setDesc(String str) {
        this.desc = str;
    }

    public void setDistrict(String str) {
        this.district = str;
    }

    public void setExtra(JSONObject jSONObject) {
        this.extra = jSONObject;
    }

    public void setFloor(String str) {
        if (!TextUtils.isEmpty(str)) {
            str = str.replace("F", "");
            try {
                Integer.parseInt(str);
            } catch (Exception e) {
                str = null;
            }
        }
        this.floor = str;
    }

    public void setIsError(boolean z) {
        this.isError = z;
    }

    public void setIsLast(boolean z) {
        this.isLast = z;
    }

    public void setLat(double d) {
        if (d > 90.0d || d < -90.0d) {
            this.lat = 0.0d;
            this.isError = true;
            return;
        }
        this.lat = ((double) Math.round(d * 1000000.0d)) / 1000000.0d;
    }

    public void setLat(String str) {
        setLat(Double.parseDouble(str));
    }

    public void setLon(double d) {
        if (d > 180.0d || d < -180.0d) {
            this.lon = 0.0d;
            this.isError = true;
            return;
        }
        this.lon = ((double) Math.round(d * 1000000.0d)) / 1000000.0d;
    }

    public void setLon(String str) {
        setLon(Double.parseDouble(str));
    }

    public void setMcell(String str) {
        this.mcell = str;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public void setOptimizedAccuracy(float f) {
        this.optimizedAccuracy = f;
    }

    public void setPoiid(String str) {
        this.poiid = str;
    }

    public void setPoiname(String str) {
        this.poiname = str;
    }

    public void setProvider(String str) {
        this.provider = str;
    }

    public void setProvince(String str) {
        this.province = str;
    }

    public void setRdesc(String str) {
        this.rdesc = str;
    }

    public void setRetype(String str) {
        this.retype = str;
    }

    public void setRoad(String str) {
        this.road = str;
    }

    public void setScenarioConfidence(int i) {
        this.scenarioConfidence = i;
    }

    public void setServerError(int i) {
        this.mServerError = i;
    }

    public void setServerFilterContext(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                this.serverFilterContext = Base64.decode(str, 0);
            }
        } catch (Exception e) {
        }
    }

    public void setSpeed(float f) {
        if (f > 100.0f) {
            this.speed = 0.0f;
        } else {
            this.speed = (f * 10.0f) / 10.0f;
        }
    }

    public void setStreet(String str) {
        this.street = str;
    }

    public void setSubType(String str) {
        this.resubtype = str;
    }

    public void setTime(long j) {
        this.time = j;
    }

    public void setType(String str) {
        this.type = str;
    }

    public Location toBuiltInLocation() {
        Location location = new Location(getProvider());
        location.setTime(this.time);
        if (Build.VERSION.SDK_INT >= 17) {
            location.setElapsedRealtimeNanos(this.time);
        }
        location.setLatitude(this.lat);
        location.setLongitude(this.lon);
        location.setAltitude(this.altitude);
        location.setSpeed(this.speed);
        location.setBearing(this.bearing);
        location.setAccuracy(this.accuracy);
        return location;
    }

    public JSONObject toJSONObject(int i) {
        try {
            JSONObject jSONObject = new JSONObject();
            switch (i) {
                case 1:
                    jSONObject.put("altitude", this.altitude);
                    jSONObject.put(Fields.Dgo_poi_2_running_click_go.speed, (double) this.speed);
                    jSONObject.put("bearing", (double) this.bearing);
                    jSONObject.put("retype", this.retype);
                    jSONObject.put("rdesc", this.rdesc);
                    jSONObject.put("citycode", this.citycode);
                    jSONObject.put("desc", this.desc);
                    jSONObject.put("adcode", this.adcode);
                    jSONObject.put("country", this.country);
                    jSONObject.put(ResponseBase.STRING_PROVINCE, this.province);
                    jSONObject.put(ResponseBase.STRING_CITY, this.city);
                    jSONObject.put("district", this.district);
                    jSONObject.put("road", this.road);
                    jSONObject.put("street", this.street);
                    jSONObject.put("number", this.number);
                    jSONObject.put("aoiname", this.aoiname);
                    jSONObject.put("poiname", this.poiname);
                    jSONObject.put("cens", this.cens);
                    jSONObject.put("poiid", this.poiid);
                    jSONObject.put("floor", this.floor);
                    jSONObject.put("coord", this.coord);
                    jSONObject.put("mcell", this.mcell);
                    jSONObject.put("scenarioConfidence", this.scenarioConfidence);
                    jSONObject.put("resubtype", this.resubtype);
                    jSONObject.put("isLast", this.isLast);
                    if (this.extra != null && jSONObject.has("offpct")) {
                        jSONObject.put("offpct", this.extra.getString("offpct"));
                    }
                case 2:
                    jSONObject.put("time", this.time);
                    break;
                case 3:
                    break;
                default:
                    return jSONObject;
            }
            jSONObject.put(ResponseBase.STRING_PROVIDER, this.provider);
            jSONObject.put("lon", this.lon);
            jSONObject.put(ResponseBase.STRING_LAT, this.lat);
            jSONObject.put("accuracy", (double) this.accuracy);
            jSONObject.put("type", this.type);
            return jSONObject;
        } catch (Exception e) {
            return null;
        }
    }

    public String toJSONStr(int i) {
        JSONObject jSONObject = toJSONObject(i);
        if (jSONObject == null) {
            return null;
        }
        return jSONObject.toString();
    }

    public String toStr() {
        return toJSONStr(1);
    }
}
