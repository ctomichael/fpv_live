package dji.common.flightcontroller.virtualfence;

public class VirtualFenceArea {
    private int areaId;
    private AreaShape areaShape;
    private VirtualCircleFenceArea circleFenceArea;
    private int flightHeight;
    private FlightHeightType flightHeightType;
    private boolean isFlightAllowed;
    private VirtualPolygonFenceArea polygonFenceArea;

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
