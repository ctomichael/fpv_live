package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdFlight;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataFlightSetVFenceData;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.LocationCoordinate;
import java.util.ArrayList;
import java.util.List;

public class DataFlightGetVFenceData extends DataBase implements DJIDataSyncListener {
    private static final int FLIGHT_HEIGHT_TYPE = 96;
    private static final int IS_ALLOWABLE = 128;
    private static final int SHAPE = 28;
    private static DataFlightGetVFenceData mInstance = null;
    private boolean shouldGet = true;

    public static synchronized DataFlightGetVFenceData getInstance() {
        DataFlightGetVFenceData dataFlightGetVFenceData;
        synchronized (DataFlightGetVFenceData.class) {
            if (mInstance == null) {
                mInstance = new DataFlightGetVFenceData();
            }
            dataFlightGetVFenceData = mInstance;
        }
        return dataFlightGetVFenceData;
    }

    public DataFlightGetVFenceData setGet(boolean shouldGet2) {
        this.shouldGet = shouldGet2;
        return this;
    }

    public boolean getRspResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 0;
    }

    public int getVersion() {
        return ((Integer) get(1, 1, Integer.class)).intValue() >> 5;
    }

    public boolean isVFenceEnabled() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 16) == 1;
    }

    public int getVirtualFenceAreaCount() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public List<VirtualFenceArea> getVirtualFenceArea() {
        ArrayList<VirtualFenceArea> virtualFenceAreas = new ArrayList<>();
        int recIndex = 3;
        while (recIndex < this._recData.length) {
            VirtualFenceArea.Builder areaBuilder = new VirtualFenceArea.Builder().areaId(((Integer) get(recIndex, 1, Integer.class)).intValue());
            int recIndex2 = recIndex + 1;
            areaBuilder.isFlightAllowed(((((Integer) get(recIndex2, 1, Integer.class)).intValue() & 128) >> 7) == 1);
            areaBuilder.flightHeightType(DataFlightSetVFenceData.FlightHeightType.find((((Integer) get(recIndex2, 1, Integer.class)).intValue() & 96) >> 5));
            DataFlightSetVFenceData.AreaShape shape = DataFlightSetVFenceData.AreaShape.find((((Integer) get(recIndex2, 1, Integer.class)).intValue() & 28) >> 2);
            areaBuilder.areaShape(shape);
            int recIndex3 = recIndex2 + 1;
            areaBuilder.flightHeight(((Integer) get(recIndex3, 2, Integer.class)).intValue());
            if (shape == DataFlightSetVFenceData.AreaShape.POLYGON) {
                recIndex3 += 2;
                int totalPoints = ((Integer) get(recIndex3, 1, Integer.class)).intValue();
                ArrayList<LocationCoordinate> points = new ArrayList<>();
                for (int pointIndex = totalPoints; pointIndex > 0; pointIndex--) {
                    int recIndex4 = recIndex3 + 1;
                    double longitude = ((double) ((Integer) get(recIndex4, 4, Integer.class)).intValue()) / Math.pow(10.0d, 7.0d);
                    int recIndex5 = recIndex4 + 4;
                    points.add(new LocationCoordinate(((double) ((Integer) get(recIndex5, 4, Integer.class)).intValue()) / Math.pow(10.0d, 7.0d), longitude));
                    recIndex3 = recIndex5 + 3;
                }
                areaBuilder.polygonFenceArea(new DataFlightSetVFenceData.VirtualPolygonFenceArea(totalPoints, points));
                virtualFenceAreas.add(areaBuilder.build());
            } else if (shape == DataFlightSetVFenceData.AreaShape.CIRCLE) {
                int recIndex6 = recIndex3 + 2;
                double longitude2 = ((double) ((Integer) get(recIndex6, 4, Integer.class)).intValue()) / Math.pow(10.0d, 7.0d);
                int recIndex7 = recIndex6 + 4;
                int recIndex8 = recIndex7 + 4;
                recIndex3 = recIndex8 + 3;
                areaBuilder.circleFenceArea(new DataFlightSetVFenceData.VirtualCircleFenceArea(((double) ((Integer) get(recIndex7, 4, Integer.class)).intValue()) / Math.pow(10.0d, 7.0d), longitude2, ((Float) get(recIndex8, 4, Float.class)).floatValue()));
                virtualFenceAreas.add(areaBuilder.build());
            }
            recIndex = recIndex3 + 1;
        }
        return virtualFenceAreas;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        if (this.shouldGet) {
            this._sendData[0] = Byte.MIN_VALUE;
        } else {
            this._sendData[0] = 0;
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
        pack.cmdId = CmdIdFlight.CmdIdType.GetVFenceData.value();
        super.start(pack, callBack);
    }

    public static final class VirtualFenceArea {
        private int areaId;
        private DataFlightSetVFenceData.AreaShape areaShape;
        private DataFlightSetVFenceData.VirtualCircleFenceArea circleFenceArea;
        private int flightHeight;
        private DataFlightSetVFenceData.FlightHeightType flightHeightType;
        private boolean isFlightAllowed;
        private DataFlightSetVFenceData.VirtualPolygonFenceArea polygonFenceArea;

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

        public DataFlightSetVFenceData.FlightHeightType getFlightHeightType() {
            return this.flightHeightType;
        }

        public int getFlightHeight() {
            return this.flightHeight;
        }

        public DataFlightSetVFenceData.VirtualPolygonFenceArea getPolygonFenceArea() {
            return this.polygonFenceArea;
        }

        public DataFlightSetVFenceData.VirtualCircleFenceArea getCircleFenceArea() {
            return this.circleFenceArea;
        }

        public DataFlightSetVFenceData.AreaShape getAreaShape() {
            return this.areaShape;
        }

        public static final class Builder {
            /* access modifiers changed from: private */
            public int areaId;
            /* access modifiers changed from: private */
            public DataFlightSetVFenceData.AreaShape areaShape;
            /* access modifiers changed from: private */
            public DataFlightSetVFenceData.VirtualCircleFenceArea circleFenceArea;
            /* access modifiers changed from: private */
            public int flightHeight;
            /* access modifiers changed from: private */
            public DataFlightSetVFenceData.FlightHeightType flightHeightType;
            /* access modifiers changed from: private */
            public boolean isFlightAllowed;
            /* access modifiers changed from: private */
            public DataFlightSetVFenceData.VirtualPolygonFenceArea polygonFenceArea;

            public Builder areaId(int areaId2) {
                this.areaId = areaId2;
                return this;
            }

            public Builder isFlightAllowed(boolean isFlightAllowed2) {
                this.isFlightAllowed = isFlightAllowed2;
                return this;
            }

            public Builder flightHeightType(DataFlightSetVFenceData.FlightHeightType type) {
                this.flightHeightType = type;
                return this;
            }

            public Builder flightHeight(int flightHeight2) {
                this.flightHeight = flightHeight2;
                return this;
            }

            public Builder areaShape(DataFlightSetVFenceData.AreaShape areaShape2) {
                this.areaShape = areaShape2;
                return this;
            }

            public Builder polygonFenceArea(DataFlightSetVFenceData.VirtualPolygonFenceArea polygonFenceArea2) {
                this.polygonFenceArea = polygonFenceArea2;
                return this;
            }

            public Builder circleFenceArea(DataFlightSetVFenceData.VirtualCircleFenceArea circleFenceArea2) {
                this.circleFenceArea = circleFenceArea2;
                return this;
            }

            public VirtualFenceArea build() {
                return new VirtualFenceArea(this);
            }
        }
    }
}
