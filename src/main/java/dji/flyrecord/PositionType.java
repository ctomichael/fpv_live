package dji.flyrecord;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.util.List;
import okio.ByteString;

public final class PositionType extends Message<PositionType, Builder> {
    public static final ProtoAdapter<PositionType> ADAPTER = new ProtoAdapter_PositionType();
    public static final Double DEFAULT_BATTERY = Double.valueOf(0.0d);
    public static final Double DEFAULT_COMBINESPEED = Double.valueOf(0.0d);
    public static final Double DEFAULT_DIRECTION = Double.valueOf(0.0d);
    public static final Double DEFAULT_DISTANCE = Double.valueOf(0.0d);
    public static final Double DEFAULT_FLIGHTTIME = Double.valueOf(0.0d);
    public static final Double DEFAULT_GIMBALANGEL = Double.valueOf(0.0d);
    public static final Double DEFAULT_GIMBALDIRECTION = Double.valueOf(0.0d);
    public static final Double DEFAULT_GPSLEVEL = Double.valueOf(0.0d);
    public static final Double DEFAULT_HEIGHT = Double.valueOf(0.0d);
    public static final Double DEFAULT_HOMEDISTANCE = Double.valueOf(0.0d);
    public static final Double DEFAULT_HORIZONTALSPEED = Double.valueOf(0.0d);
    public static final Double DEFAULT_RC = Double.valueOf(0.0d);
    public static final Double DEFAULT_RESTTIME = Double.valueOf(0.0d);
    public static final Integer DEFAULT_SATELLITENUM = 0;
    public static final String DEFAULT_STALL = "";
    public static final Double DEFAULT_VERTICALSPEED = Double.valueOf(0.0d);
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 2)
    public final Double battery;
    @WireField(adapter = "dji.flyrecord.FPVCheckListItem#ADAPTER", label = WireField.Label.REPEATED, tag = 22)
    public final List<FPVCheckListItem> checklistArray;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 20)
    public final Double combineSpeed;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 7)
    public final Double direction;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 8)
    public final Double distance;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 10)
    public final Double flightTime;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 17)
    public final Double gimbalAngel;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 16)
    public final Double gimbalDirection;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 9)
    public final Double gpsLevel;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 4)
    public final Double height;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 13)
    public final Double homeDistance;
    @WireField(adapter = "dji.flyrecord.LatLng#ADAPTER", tag = 12)
    public final LatLng homeLatLng;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 5)
    public final Double horizontalSpeed;
    @WireField(adapter = "dji.flyrecord.LatLng#ADAPTER", tag = 11)
    public final LatLng latLng;
    @WireField(adapter = "dji.flyrecord.JoyStickType#ADAPTER", tag = 14)
    public final JoyStickType leftStick;
    @WireField(adapter = "dji.flyrecord.FPVTipsItem#ADAPTER", label = WireField.Label.REPEATED, tag = 23)
    public final List<FPVTipsItem> popupArr;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 3)
    public final Double rc;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 18)
    public final Double restTime;
    @WireField(adapter = "dji.flyrecord.JoyStickType#ADAPTER", tag = 15)
    public final JoyStickType rightStick;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 24)
    public final Integer satelliteNum;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String stall;
    @WireField(adapter = "dji.flyrecord.FPVStatusBarItem#ADAPTER", tag = 21)
    public final FPVStatusBarItem topbarItem;
    @WireField(adapter = "dji.flyrecord.LatLng#ADAPTER", tag = 19)
    public final LatLng userLatLng;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 6)
    public final Double verticalSpeed;

    public PositionType(String stall2, Double battery2, Double rc2, Double height2, Double horizontalSpeed2, Double verticalSpeed2, Double direction2, Double distance2, Double gpsLevel2, Double flightTime2, LatLng latLng2, LatLng homeLatLng2, Double homeDistance2, JoyStickType leftStick2, JoyStickType rightStick2, Double gimbalDirection2, Double gimbalAngel2, Double restTime2, LatLng userLatLng2, Double combineSpeed2, FPVStatusBarItem topbarItem2, List<FPVCheckListItem> checklistArray2, List<FPVTipsItem> popupArr2, Integer satelliteNum2) {
        this(stall2, battery2, rc2, height2, horizontalSpeed2, verticalSpeed2, direction2, distance2, gpsLevel2, flightTime2, latLng2, homeLatLng2, homeDistance2, leftStick2, rightStick2, gimbalDirection2, gimbalAngel2, restTime2, userLatLng2, combineSpeed2, topbarItem2, checklistArray2, popupArr2, satelliteNum2, ByteString.EMPTY);
    }

    public PositionType(String stall2, Double battery2, Double rc2, Double height2, Double horizontalSpeed2, Double verticalSpeed2, Double direction2, Double distance2, Double gpsLevel2, Double flightTime2, LatLng latLng2, LatLng homeLatLng2, Double homeDistance2, JoyStickType leftStick2, JoyStickType rightStick2, Double gimbalDirection2, Double gimbalAngel2, Double restTime2, LatLng userLatLng2, Double combineSpeed2, FPVStatusBarItem topbarItem2, List<FPVCheckListItem> checklistArray2, List<FPVTipsItem> popupArr2, Integer satelliteNum2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.stall = stall2;
        this.battery = battery2;
        this.rc = rc2;
        this.height = height2;
        this.horizontalSpeed = horizontalSpeed2;
        this.verticalSpeed = verticalSpeed2;
        this.direction = direction2;
        this.distance = distance2;
        this.gpsLevel = gpsLevel2;
        this.flightTime = flightTime2;
        this.latLng = latLng2;
        this.homeLatLng = homeLatLng2;
        this.homeDistance = homeDistance2;
        this.leftStick = leftStick2;
        this.rightStick = rightStick2;
        this.gimbalDirection = gimbalDirection2;
        this.gimbalAngel = gimbalAngel2;
        this.restTime = restTime2;
        this.userLatLng = userLatLng2;
        this.combineSpeed = combineSpeed2;
        this.topbarItem = topbarItem2;
        this.checklistArray = Internal.immutableCopyOf("checklistArray", checklistArray2);
        this.popupArr = Internal.immutableCopyOf("popupArr", popupArr2);
        this.satelliteNum = satelliteNum2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.stall = this.stall;
        builder.battery = this.battery;
        builder.rc = this.rc;
        builder.height = this.height;
        builder.horizontalSpeed = this.horizontalSpeed;
        builder.verticalSpeed = this.verticalSpeed;
        builder.direction = this.direction;
        builder.distance = this.distance;
        builder.gpsLevel = this.gpsLevel;
        builder.flightTime = this.flightTime;
        builder.latLng = this.latLng;
        builder.homeLatLng = this.homeLatLng;
        builder.homeDistance = this.homeDistance;
        builder.leftStick = this.leftStick;
        builder.rightStick = this.rightStick;
        builder.gimbalDirection = this.gimbalDirection;
        builder.gimbalAngel = this.gimbalAngel;
        builder.restTime = this.restTime;
        builder.userLatLng = this.userLatLng;
        builder.combineSpeed = this.combineSpeed;
        builder.topbarItem = this.topbarItem;
        builder.checklistArray = Internal.copyOf("checklistArray", this.checklistArray);
        builder.popupArr = Internal.copyOf("popupArr", this.popupArr);
        builder.satelliteNum = this.satelliteNum;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PositionType)) {
            return false;
        }
        PositionType o = (PositionType) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.stall, o.stall) || !Internal.equals(this.battery, o.battery) || !Internal.equals(this.rc, o.rc) || !Internal.equals(this.height, o.height) || !Internal.equals(this.horizontalSpeed, o.horizontalSpeed) || !Internal.equals(this.verticalSpeed, o.verticalSpeed) || !Internal.equals(this.direction, o.direction) || !Internal.equals(this.distance, o.distance) || !Internal.equals(this.gpsLevel, o.gpsLevel) || !Internal.equals(this.flightTime, o.flightTime) || !Internal.equals(this.latLng, o.latLng) || !Internal.equals(this.homeLatLng, o.homeLatLng) || !Internal.equals(this.homeDistance, o.homeDistance) || !Internal.equals(this.leftStick, o.leftStick) || !Internal.equals(this.rightStick, o.rightStick) || !Internal.equals(this.gimbalDirection, o.gimbalDirection) || !Internal.equals(this.gimbalAngel, o.gimbalAngel) || !Internal.equals(this.restTime, o.restTime) || !Internal.equals(this.userLatLng, o.userLatLng) || !Internal.equals(this.combineSpeed, o.combineSpeed) || !Internal.equals(this.topbarItem, o.topbarItem) || !this.checklistArray.equals(o.checklistArray) || !this.popupArr.equals(o.popupArr) || !Internal.equals(this.satelliteNum, o.satelliteNum)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
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
        int i21 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.stall != null ? this.stall.hashCode() : 0)) * 37;
        if (this.battery != null) {
            i = this.battery.hashCode();
        } else {
            i = 0;
        }
        int i22 = (hashCode + i) * 37;
        if (this.rc != null) {
            i2 = this.rc.hashCode();
        } else {
            i2 = 0;
        }
        int i23 = (i22 + i2) * 37;
        if (this.height != null) {
            i3 = this.height.hashCode();
        } else {
            i3 = 0;
        }
        int i24 = (i23 + i3) * 37;
        if (this.horizontalSpeed != null) {
            i4 = this.horizontalSpeed.hashCode();
        } else {
            i4 = 0;
        }
        int i25 = (i24 + i4) * 37;
        if (this.verticalSpeed != null) {
            i5 = this.verticalSpeed.hashCode();
        } else {
            i5 = 0;
        }
        int i26 = (i25 + i5) * 37;
        if (this.direction != null) {
            i6 = this.direction.hashCode();
        } else {
            i6 = 0;
        }
        int i27 = (i26 + i6) * 37;
        if (this.distance != null) {
            i7 = this.distance.hashCode();
        } else {
            i7 = 0;
        }
        int i28 = (i27 + i7) * 37;
        if (this.gpsLevel != null) {
            i8 = this.gpsLevel.hashCode();
        } else {
            i8 = 0;
        }
        int i29 = (i28 + i8) * 37;
        if (this.flightTime != null) {
            i9 = this.flightTime.hashCode();
        } else {
            i9 = 0;
        }
        int i30 = (i29 + i9) * 37;
        if (this.latLng != null) {
            i10 = this.latLng.hashCode();
        } else {
            i10 = 0;
        }
        int i31 = (i30 + i10) * 37;
        if (this.homeLatLng != null) {
            i11 = this.homeLatLng.hashCode();
        } else {
            i11 = 0;
        }
        int i32 = (i31 + i11) * 37;
        if (this.homeDistance != null) {
            i12 = this.homeDistance.hashCode();
        } else {
            i12 = 0;
        }
        int i33 = (i32 + i12) * 37;
        if (this.leftStick != null) {
            i13 = this.leftStick.hashCode();
        } else {
            i13 = 0;
        }
        int i34 = (i33 + i13) * 37;
        if (this.rightStick != null) {
            i14 = this.rightStick.hashCode();
        } else {
            i14 = 0;
        }
        int i35 = (i34 + i14) * 37;
        if (this.gimbalDirection != null) {
            i15 = this.gimbalDirection.hashCode();
        } else {
            i15 = 0;
        }
        int i36 = (i35 + i15) * 37;
        if (this.gimbalAngel != null) {
            i16 = this.gimbalAngel.hashCode();
        } else {
            i16 = 0;
        }
        int i37 = (i36 + i16) * 37;
        if (this.restTime != null) {
            i17 = this.restTime.hashCode();
        } else {
            i17 = 0;
        }
        int i38 = (i37 + i17) * 37;
        if (this.userLatLng != null) {
            i18 = this.userLatLng.hashCode();
        } else {
            i18 = 0;
        }
        int i39 = (i38 + i18) * 37;
        if (this.combineSpeed != null) {
            i19 = this.combineSpeed.hashCode();
        } else {
            i19 = 0;
        }
        int i40 = (i39 + i19) * 37;
        if (this.topbarItem != null) {
            i20 = this.topbarItem.hashCode();
        } else {
            i20 = 0;
        }
        int hashCode2 = (((((i40 + i20) * 37) + this.checklistArray.hashCode()) * 37) + this.popupArr.hashCode()) * 37;
        if (this.satelliteNum != null) {
            i21 = this.satelliteNum.hashCode();
        }
        int result2 = hashCode2 + i21;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.stall != null) {
            builder.append(", stall=").append(this.stall);
        }
        if (this.battery != null) {
            builder.append(", battery=").append(this.battery);
        }
        if (this.rc != null) {
            builder.append(", rc=").append(this.rc);
        }
        if (this.height != null) {
            builder.append(", height=").append(this.height);
        }
        if (this.horizontalSpeed != null) {
            builder.append(", horizontalSpeed=").append(this.horizontalSpeed);
        }
        if (this.verticalSpeed != null) {
            builder.append(", verticalSpeed=").append(this.verticalSpeed);
        }
        if (this.direction != null) {
            builder.append(", direction=").append(this.direction);
        }
        if (this.distance != null) {
            builder.append(", distance=").append(this.distance);
        }
        if (this.gpsLevel != null) {
            builder.append(", gpsLevel=").append(this.gpsLevel);
        }
        if (this.flightTime != null) {
            builder.append(", flightTime=").append(this.flightTime);
        }
        if (this.latLng != null) {
            builder.append(", latLng=").append(this.latLng);
        }
        if (this.homeLatLng != null) {
            builder.append(", homeLatLng=").append(this.homeLatLng);
        }
        if (this.homeDistance != null) {
            builder.append(", homeDistance=").append(this.homeDistance);
        }
        if (this.leftStick != null) {
            builder.append(", leftStick=").append(this.leftStick);
        }
        if (this.rightStick != null) {
            builder.append(", rightStick=").append(this.rightStick);
        }
        if (this.gimbalDirection != null) {
            builder.append(", gimbalDirection=").append(this.gimbalDirection);
        }
        if (this.gimbalAngel != null) {
            builder.append(", gimbalAngel=").append(this.gimbalAngel);
        }
        if (this.restTime != null) {
            builder.append(", restTime=").append(this.restTime);
        }
        if (this.userLatLng != null) {
            builder.append(", userLatLng=").append(this.userLatLng);
        }
        if (this.combineSpeed != null) {
            builder.append(", combineSpeed=").append(this.combineSpeed);
        }
        if (this.topbarItem != null) {
            builder.append(", topbarItem=").append(this.topbarItem);
        }
        if (!this.checklistArray.isEmpty()) {
            builder.append(", checklistArray=").append(this.checklistArray);
        }
        if (!this.popupArr.isEmpty()) {
            builder.append(", popupArr=").append(this.popupArr);
        }
        if (this.satelliteNum != null) {
            builder.append(", satelliteNum=").append(this.satelliteNum);
        }
        return builder.replace(0, 2, "PositionType{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<PositionType, Builder> {
        public Double battery;
        public List<FPVCheckListItem> checklistArray = Internal.newMutableList();
        public Double combineSpeed;
        public Double direction;
        public Double distance;
        public Double flightTime;
        public Double gimbalAngel;
        public Double gimbalDirection;
        public Double gpsLevel;
        public Double height;
        public Double homeDistance;
        public LatLng homeLatLng;
        public Double horizontalSpeed;
        public LatLng latLng;
        public JoyStickType leftStick;
        public List<FPVTipsItem> popupArr = Internal.newMutableList();
        public Double rc;
        public Double restTime;
        public JoyStickType rightStick;
        public Integer satelliteNum;
        public String stall;
        public FPVStatusBarItem topbarItem;
        public LatLng userLatLng;
        public Double verticalSpeed;

        public Builder stall(String stall2) {
            this.stall = stall2;
            return this;
        }

        public Builder battery(Double battery2) {
            this.battery = battery2;
            return this;
        }

        public Builder rc(Double rc2) {
            this.rc = rc2;
            return this;
        }

        public Builder height(Double height2) {
            this.height = height2;
            return this;
        }

        public Builder horizontalSpeed(Double horizontalSpeed2) {
            this.horizontalSpeed = horizontalSpeed2;
            return this;
        }

        public Builder verticalSpeed(Double verticalSpeed2) {
            this.verticalSpeed = verticalSpeed2;
            return this;
        }

        public Builder direction(Double direction2) {
            this.direction = direction2;
            return this;
        }

        public Builder distance(Double distance2) {
            this.distance = distance2;
            return this;
        }

        public Builder gpsLevel(Double gpsLevel2) {
            this.gpsLevel = gpsLevel2;
            return this;
        }

        public Builder flightTime(Double flightTime2) {
            this.flightTime = flightTime2;
            return this;
        }

        public Builder latLng(LatLng latLng2) {
            this.latLng = latLng2;
            return this;
        }

        public Builder homeLatLng(LatLng homeLatLng2) {
            this.homeLatLng = homeLatLng2;
            return this;
        }

        public Builder homeDistance(Double homeDistance2) {
            this.homeDistance = homeDistance2;
            return this;
        }

        public Builder leftStick(JoyStickType leftStick2) {
            this.leftStick = leftStick2;
            return this;
        }

        public Builder rightStick(JoyStickType rightStick2) {
            this.rightStick = rightStick2;
            return this;
        }

        public Builder gimbalDirection(Double gimbalDirection2) {
            this.gimbalDirection = gimbalDirection2;
            return this;
        }

        public Builder gimbalAngel(Double gimbalAngel2) {
            this.gimbalAngel = gimbalAngel2;
            return this;
        }

        public Builder restTime(Double restTime2) {
            this.restTime = restTime2;
            return this;
        }

        public Builder userLatLng(LatLng userLatLng2) {
            this.userLatLng = userLatLng2;
            return this;
        }

        public Builder combineSpeed(Double combineSpeed2) {
            this.combineSpeed = combineSpeed2;
            return this;
        }

        public Builder topbarItem(FPVStatusBarItem topbarItem2) {
            this.topbarItem = topbarItem2;
            return this;
        }

        public Builder checklistArray(List<FPVCheckListItem> checklistArray2) {
            Internal.checkElementsNotNull(checklistArray2);
            this.checklistArray = checklistArray2;
            return this;
        }

        public Builder popupArr(List<FPVTipsItem> popupArr2) {
            Internal.checkElementsNotNull(popupArr2);
            this.popupArr = popupArr2;
            return this;
        }

        public Builder satelliteNum(Integer satelliteNum2) {
            this.satelliteNum = satelliteNum2;
            return this;
        }

        public PositionType build() {
            return new PositionType(this.stall, this.battery, this.rc, this.height, this.horizontalSpeed, this.verticalSpeed, this.direction, this.distance, this.gpsLevel, this.flightTime, this.latLng, this.homeLatLng, this.homeDistance, this.leftStick, this.rightStick, this.gimbalDirection, this.gimbalAngel, this.restTime, this.userLatLng, this.combineSpeed, this.topbarItem, this.checklistArray, this.popupArr, this.satelliteNum, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_PositionType extends ProtoAdapter<PositionType> {
        ProtoAdapter_PositionType() {
            super(FieldEncoding.LENGTH_DELIMITED, PositionType.class);
        }

        public int encodedSize(PositionType value) {
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
            int i21 = 0;
            int encodedSizeWithTag = value.stall != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.stall) : 0;
            if (value.battery != null) {
                i = ProtoAdapter.DOUBLE.encodedSizeWithTag(2, value.battery);
            } else {
                i = 0;
            }
            int i22 = i + encodedSizeWithTag;
            if (value.rc != null) {
                i2 = ProtoAdapter.DOUBLE.encodedSizeWithTag(3, value.rc);
            } else {
                i2 = 0;
            }
            int i23 = i22 + i2;
            if (value.height != null) {
                i3 = ProtoAdapter.DOUBLE.encodedSizeWithTag(4, value.height);
            } else {
                i3 = 0;
            }
            int i24 = i23 + i3;
            if (value.horizontalSpeed != null) {
                i4 = ProtoAdapter.DOUBLE.encodedSizeWithTag(5, value.horizontalSpeed);
            } else {
                i4 = 0;
            }
            int i25 = i24 + i4;
            if (value.verticalSpeed != null) {
                i5 = ProtoAdapter.DOUBLE.encodedSizeWithTag(6, value.verticalSpeed);
            } else {
                i5 = 0;
            }
            int i26 = i25 + i5;
            if (value.direction != null) {
                i6 = ProtoAdapter.DOUBLE.encodedSizeWithTag(7, value.direction);
            } else {
                i6 = 0;
            }
            int i27 = i26 + i6;
            if (value.distance != null) {
                i7 = ProtoAdapter.DOUBLE.encodedSizeWithTag(8, value.distance);
            } else {
                i7 = 0;
            }
            int i28 = i27 + i7;
            if (value.gpsLevel != null) {
                i8 = ProtoAdapter.DOUBLE.encodedSizeWithTag(9, value.gpsLevel);
            } else {
                i8 = 0;
            }
            int i29 = i28 + i8;
            if (value.flightTime != null) {
                i9 = ProtoAdapter.DOUBLE.encodedSizeWithTag(10, value.flightTime);
            } else {
                i9 = 0;
            }
            int i30 = i29 + i9;
            if (value.latLng != null) {
                i10 = LatLng.ADAPTER.encodedSizeWithTag(11, value.latLng);
            } else {
                i10 = 0;
            }
            int i31 = i30 + i10;
            if (value.homeLatLng != null) {
                i11 = LatLng.ADAPTER.encodedSizeWithTag(12, value.homeLatLng);
            } else {
                i11 = 0;
            }
            int i32 = i31 + i11;
            if (value.homeDistance != null) {
                i12 = ProtoAdapter.DOUBLE.encodedSizeWithTag(13, value.homeDistance);
            } else {
                i12 = 0;
            }
            int i33 = i32 + i12;
            if (value.leftStick != null) {
                i13 = JoyStickType.ADAPTER.encodedSizeWithTag(14, value.leftStick);
            } else {
                i13 = 0;
            }
            int i34 = i33 + i13;
            if (value.rightStick != null) {
                i14 = JoyStickType.ADAPTER.encodedSizeWithTag(15, value.rightStick);
            } else {
                i14 = 0;
            }
            int i35 = i34 + i14;
            if (value.gimbalDirection != null) {
                i15 = ProtoAdapter.DOUBLE.encodedSizeWithTag(16, value.gimbalDirection);
            } else {
                i15 = 0;
            }
            int i36 = i35 + i15;
            if (value.gimbalAngel != null) {
                i16 = ProtoAdapter.DOUBLE.encodedSizeWithTag(17, value.gimbalAngel);
            } else {
                i16 = 0;
            }
            int i37 = i36 + i16;
            if (value.restTime != null) {
                i17 = ProtoAdapter.DOUBLE.encodedSizeWithTag(18, value.restTime);
            } else {
                i17 = 0;
            }
            int i38 = i37 + i17;
            if (value.userLatLng != null) {
                i18 = LatLng.ADAPTER.encodedSizeWithTag(19, value.userLatLng);
            } else {
                i18 = 0;
            }
            int i39 = i38 + i18;
            if (value.combineSpeed != null) {
                i19 = ProtoAdapter.DOUBLE.encodedSizeWithTag(20, value.combineSpeed);
            } else {
                i19 = 0;
            }
            int i40 = i39 + i19;
            if (value.topbarItem != null) {
                i20 = FPVStatusBarItem.ADAPTER.encodedSizeWithTag(21, value.topbarItem);
            } else {
                i20 = 0;
            }
            int encodedSizeWithTag2 = i20 + i40 + FPVCheckListItem.ADAPTER.asRepeated().encodedSizeWithTag(22, value.checklistArray) + FPVTipsItem.ADAPTER.asRepeated().encodedSizeWithTag(23, value.popupArr);
            if (value.satelliteNum != null) {
                i21 = ProtoAdapter.INT32.encodedSizeWithTag(24, value.satelliteNum);
            }
            return encodedSizeWithTag2 + i21 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, PositionType value) throws IOException {
            if (value.stall != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.stall);
            }
            if (value.battery != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 2, value.battery);
            }
            if (value.rc != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 3, value.rc);
            }
            if (value.height != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 4, value.height);
            }
            if (value.horizontalSpeed != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 5, value.horizontalSpeed);
            }
            if (value.verticalSpeed != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 6, value.verticalSpeed);
            }
            if (value.direction != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 7, value.direction);
            }
            if (value.distance != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 8, value.distance);
            }
            if (value.gpsLevel != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 9, value.gpsLevel);
            }
            if (value.flightTime != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 10, value.flightTime);
            }
            if (value.latLng != null) {
                LatLng.ADAPTER.encodeWithTag(writer, 11, value.latLng);
            }
            if (value.homeLatLng != null) {
                LatLng.ADAPTER.encodeWithTag(writer, 12, value.homeLatLng);
            }
            if (value.homeDistance != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 13, value.homeDistance);
            }
            if (value.leftStick != null) {
                JoyStickType.ADAPTER.encodeWithTag(writer, 14, value.leftStick);
            }
            if (value.rightStick != null) {
                JoyStickType.ADAPTER.encodeWithTag(writer, 15, value.rightStick);
            }
            if (value.gimbalDirection != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 16, value.gimbalDirection);
            }
            if (value.gimbalAngel != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 17, value.gimbalAngel);
            }
            if (value.restTime != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 18, value.restTime);
            }
            if (value.userLatLng != null) {
                LatLng.ADAPTER.encodeWithTag(writer, 19, value.userLatLng);
            }
            if (value.combineSpeed != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 20, value.combineSpeed);
            }
            if (value.topbarItem != null) {
                FPVStatusBarItem.ADAPTER.encodeWithTag(writer, 21, value.topbarItem);
            }
            FPVCheckListItem.ADAPTER.asRepeated().encodeWithTag(writer, 22, value.checklistArray);
            FPVTipsItem.ADAPTER.asRepeated().encodeWithTag(writer, 23, value.popupArr);
            if (value.satelliteNum != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 24, value.satelliteNum);
            }
            writer.writeBytes(value.unknownFields());
        }

        public PositionType decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.stall(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.battery(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 3:
                            builder.rc(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 4:
                            builder.height(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 5:
                            builder.horizontalSpeed(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 6:
                            builder.verticalSpeed(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 7:
                            builder.direction(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 8:
                            builder.distance(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 9:
                            builder.gpsLevel(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 10:
                            builder.flightTime(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 11:
                            builder.latLng(LatLng.ADAPTER.decode(reader));
                            break;
                        case 12:
                            builder.homeLatLng(LatLng.ADAPTER.decode(reader));
                            break;
                        case 13:
                            builder.homeDistance(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 14:
                            builder.leftStick(JoyStickType.ADAPTER.decode(reader));
                            break;
                        case 15:
                            builder.rightStick(JoyStickType.ADAPTER.decode(reader));
                            break;
                        case 16:
                            builder.gimbalDirection(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 17:
                            builder.gimbalAngel(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 18:
                            builder.restTime(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 19:
                            builder.userLatLng(LatLng.ADAPTER.decode(reader));
                            break;
                        case 20:
                            builder.combineSpeed(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 21:
                            builder.topbarItem(FPVStatusBarItem.ADAPTER.decode(reader));
                            break;
                        case 22:
                            builder.checklistArray.add(FPVCheckListItem.ADAPTER.decode(reader));
                            break;
                        case 23:
                            builder.popupArr.add(FPVTipsItem.ADAPTER.decode(reader));
                            break;
                        case 24:
                            builder.satelliteNum(ProtoAdapter.INT32.decode(reader));
                            break;
                        default:
                            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
                            builder.addUnknownField(tag, fieldEncoding, fieldEncoding.rawProtoAdapter().decode(reader));
                            break;
                    }
                } else {
                    reader.endMessage(token);
                    return builder.build();
                }
            }
        }

        public PositionType redact(PositionType value) {
            Builder builder = value.newBuilder();
            if (builder.latLng != null) {
                builder.latLng = LatLng.ADAPTER.redact(builder.latLng);
            }
            if (builder.homeLatLng != null) {
                builder.homeLatLng = LatLng.ADAPTER.redact(builder.homeLatLng);
            }
            if (builder.leftStick != null) {
                builder.leftStick = JoyStickType.ADAPTER.redact(builder.leftStick);
            }
            if (builder.rightStick != null) {
                builder.rightStick = JoyStickType.ADAPTER.redact(builder.rightStick);
            }
            if (builder.userLatLng != null) {
                builder.userLatLng = LatLng.ADAPTER.redact(builder.userLatLng);
            }
            if (builder.topbarItem != null) {
                builder.topbarItem = FPVStatusBarItem.ADAPTER.redact(builder.topbarItem);
            }
            Internal.redactElements(builder.checklistArray, FPVCheckListItem.ADAPTER);
            Internal.redactElements(builder.popupArr, FPVTipsItem.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
