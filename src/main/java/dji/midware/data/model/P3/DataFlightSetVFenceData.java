package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdFlight;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import dji.midware.util.LocationCoordinate;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class DataFlightSetVFenceData extends DataBase implements DJIDataSyncListener {
    private static final int AREA_COMMON_ATTRIBUTE_PACK_LENGTH = 4;
    private static final int FLIGHT_HEIGHT_TYPE = 96;
    private static final int IS_ALLOWABLE = 128;
    private static final int SHAPE = 28;
    private static final int VERSION = 1;
    private static DataFlightSetVFenceData mInstance = null;
    private int areaCount = 0;
    private int packLength = 2;
    private ArrayList<VirtualFenceArea> virtualFenceAreas = new ArrayList<>();

    public static synchronized DataFlightSetVFenceData getInstance() {
        DataFlightSetVFenceData dataFlightSetVFenceData;
        synchronized (DataFlightSetVFenceData.class) {
            if (mInstance == null) {
                mInstance = new DataFlightSetVFenceData();
            }
            dataFlightSetVFenceData = mInstance;
        }
        return dataFlightSetVFenceData;
    }

    public DataFlightSetVFenceData resetPack() {
        this.areaCount = 0;
        this.virtualFenceAreas = new ArrayList<>();
        this.packLength = 2;
        return this;
    }

    public DataFlightSetVFenceData setAreaCount(int areaCount2) {
        this.areaCount = areaCount2;
        return this;
    }

    public DataFlightSetVFenceData addVirtualAreaData(VirtualFenceArea areaData) {
        this.virtualFenceAreas.add(areaData);
        if (areaData.areaShape == AreaShape.CIRCLE) {
            this.packLength += 4;
            this.packLength += areaData.areaShape.length;
        } else if (areaData.areaShape == AreaShape.POLYGON) {
            this.packLength += 5;
            this.packLength += areaData.areaShape.length * areaData.polygonFenceArea.totalPointsCount;
        }
        return this;
    }

    public boolean getRspResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 0;
    }

    public int getRspErrorReason() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[this.packLength];
        this._sendData[0] = 32;
        this._sendData[1] = (byte) this.areaCount;
        int index = 1;
        Iterator<VirtualFenceArea> it2 = this.virtualFenceAreas.iterator();
        while (it2.hasNext()) {
            VirtualFenceArea area = it2.next();
            int index2 = index + 1;
            this._sendData[index2] = (byte) area.areaId;
            int index3 = index2 + 1;
            this._sendData[index3] = (byte) ((((area.isFlightAllowed ? 1 : 0) << 7) & 128) | ((area.flightHeightType.data << 5) & 96) | ((area.areaShape.value << 2) & 28));
            index = index3 + 1;
            System.arraycopy(BytesUtil.getBytes(area.flightHeight), 0, this._sendData, index, 2);
            if (area.areaShape == AreaShape.CIRCLE) {
                int longitude = truncate(area.circleFenceArea.centerLongitude * Math.pow(10.0d, 7.0d));
                int latitude = truncate(area.circleFenceArea.centerLatitude * Math.pow(10.0d, 7.0d));
                int index4 = index + 2;
                System.arraycopy(BytesUtil.getBytes(longitude), 0, this._sendData, index4, 4);
                int index5 = index4 + 4;
                System.arraycopy(BytesUtil.getBytes(latitude), 0, this._sendData, index5, 4);
                int index6 = index5 + 4;
                System.arraycopy(BytesUtil.getBytes(area.circleFenceArea.radius), 0, this._sendData, index6, 4);
                index = index6 + 3;
            } else if (area.areaShape == AreaShape.POLYGON) {
                index += 2;
                this._sendData[index] = (byte) area.polygonFenceArea.totalPointsCount;
                Iterator it3 = area.polygonFenceArea.coordinatePoints.iterator();
                while (it3.hasNext()) {
                    LocationCoordinate latLng = (LocationCoordinate) it3.next();
                    int longitude2 = truncate(latLng.longitude * Math.pow(10.0d, 7.0d));
                    int latitude2 = truncate(latLng.latitude * Math.pow(10.0d, 7.0d));
                    int index7 = index + 1;
                    System.arraycopy(BytesUtil.getBytes(longitude2), 0, this._sendData, index7, 4);
                    int index8 = index7 + 4;
                    System.arraycopy(BytesUtil.getBytes(latitude2), 0, this._sendData, index8, 4);
                    index = index8 + 3;
                }
            }
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 5;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.Flight.value();
        pack.cmdId = CmdIdFlight.CmdIdType.SetVFenceData.value();
        super.start(pack, callBack);
    }

    public static final class VirtualFenceArea {
        /* access modifiers changed from: private */
        public int areaId;
        /* access modifiers changed from: private */
        public AreaShape areaShape;
        /* access modifiers changed from: private */
        public VirtualCircleFenceArea circleFenceArea;
        /* access modifiers changed from: private */
        public int flightHeight;
        /* access modifiers changed from: private */
        public FlightHeightType flightHeightType;
        /* access modifiers changed from: private */
        public boolean isFlightAllowed;
        /* access modifiers changed from: private */
        public VirtualPolygonFenceArea polygonFenceArea;

        private VirtualFenceArea(Builder builder) {
            this.areaId = builder.areaId;
            this.isFlightAllowed = builder.isFlightAllowed;
            this.flightHeightType = builder.flightHeightType;
            this.flightHeight = builder.flightHeight;
            this.polygonFenceArea = builder.polygonFenceArea;
            this.circleFenceArea = builder.circleFenceArea;
            this.areaShape = builder.areaShape;
        }

        public int getAreaId() {
            return this.areaId;
        }

        public boolean isFlightAllowed() {
            return this.isFlightAllowed;
        }

        public FlightHeightType getFlightHeightType() {
            return this.flightHeightType;
        }

        public int getFlightHeight() {
            return this.flightHeight;
        }

        public VirtualPolygonFenceArea getPolygonFenceArea() {
            return this.polygonFenceArea;
        }

        public VirtualCircleFenceArea getCircleFenceArea() {
            return this.circleFenceArea;
        }

        public AreaShape getAreaShape() {
            return this.areaShape;
        }

        public static final class Builder {
            /* access modifiers changed from: private */
            public int areaId;
            /* access modifiers changed from: private */
            public AreaShape areaShape;
            /* access modifiers changed from: private */
            public VirtualCircleFenceArea circleFenceArea;
            /* access modifiers changed from: private */
            public int flightHeight;
            /* access modifiers changed from: private */
            public FlightHeightType flightHeightType;
            /* access modifiers changed from: private */
            public boolean isFlightAllowed;
            /* access modifiers changed from: private */
            public VirtualPolygonFenceArea polygonFenceArea;

            public Builder areaId(int areaId2) {
                this.areaId = areaId2;
                return this;
            }

            public Builder isFlightAllowed(boolean isFlightAllowed2) {
                this.isFlightAllowed = isFlightAllowed2;
                return this;
            }

            public Builder flightHeightType(FlightHeightType type) {
                this.flightHeightType = type;
                return this;
            }

            public Builder flightHeight(int flightHeight2) {
                this.flightHeight = flightHeight2;
                return this;
            }

            public Builder areaShape(AreaShape areaShape2) {
                this.areaShape = areaShape2;
                return this;
            }

            public Builder polygonFenceArea(VirtualPolygonFenceArea polygonFenceArea2) {
                this.polygonFenceArea = polygonFenceArea2;
                return this;
            }

            public Builder circleFenceArea(VirtualCircleFenceArea circleFenceArea2) {
                this.circleFenceArea = circleFenceArea2;
                return this;
            }

            public VirtualFenceArea build() {
                return new VirtualFenceArea(this);
            }
        }
    }

    public enum FlightHeightType {
        NOT_SET(0),
        RELATIVE(1),
        ABSOLUTE(2);
        
        /* access modifiers changed from: private */
        public final int data;

        private FlightHeightType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static FlightHeightType find(int value) {
            FlightHeightType result = NOT_SET;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public static final class VirtualPolygonFenceArea {
        /* access modifiers changed from: private */
        public ArrayList<LocationCoordinate> coordinatePoints;
        /* access modifiers changed from: private */
        public int totalPointsCount = 0;

        public VirtualPolygonFenceArea(int totalPointsCount2, ArrayList<LocationCoordinate> coordinatePoints2) {
            this.totalPointsCount = totalPointsCount2;
            this.coordinatePoints = coordinatePoints2;
        }

        public int getTotalPointsCount() {
            return this.totalPointsCount;
        }

        public ArrayList<LocationCoordinate> getCoordinatePoints() {
            return this.coordinatePoints;
        }
    }

    public static final class VirtualCircleFenceArea {
        /* access modifiers changed from: private */
        public double centerLatitude = 0.0d;
        /* access modifiers changed from: private */
        public double centerLongitude = 0.0d;
        /* access modifiers changed from: private */
        public float radius = 0.0f;

        public VirtualCircleFenceArea(double centerLatitude2, double centerLongitude2, float radius2) {
            this.centerLatitude = centerLatitude2;
            this.centerLongitude = centerLongitude2;
            this.radius = radius2;
        }

        public double getCenterLongitude() {
            return this.centerLongitude;
        }

        public double getCenterLatitude() {
            return this.centerLatitude;
        }

        public float getRadius() {
            return this.radius;
        }
    }

    public enum AreaShape {
        CIRCLE(12, 0),
        POLYGON(8, 1),
        UNKNOWN(0, 255);
        
        /* access modifiers changed from: private */
        public final int length;
        /* access modifiers changed from: private */
        public final int value;

        private AreaShape(int length2, int value2) {
            this.length = length2;
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static AreaShape find(int value2) {
            AreaShape result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum SetVFenceError {
        NO_ERROR(0),
        DATA_REGULAR_CHECK_FAILED(1),
        NO_FLY_ZONE_OVERLAPPINT(2),
        NO_FLY_ZONE_INCLUSION(3),
        FLY_ZONE_OVERLAPPING(4),
        FLY_ZONE_INCLUSION(5),
        DIFFERENT_FLY_ZONE_OVERLAPPING(6),
        DIFFERENT_FLY_ZONES_INCLUSION(7),
        UNKNOWN(255);
        
        private final int value;

        private SetVFenceError(int value2) {
            this.value = value2;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static SetVFenceError find(int value2) {
            SetVFenceError result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public static int truncate(double value) {
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(0);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        return Integer.parseInt(formater.format(value));
    }

    public byte[] getTestSendData() {
        return this._sendData;
    }
}
