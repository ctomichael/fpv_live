package dji.flysafe;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum LimitAreaType implements WireEnum {
    DJIAirport(0),
    DJISpecialZone(1),
    MilitaryZone(2),
    CriticalAirport(10),
    AirMapCommercialAirport(11),
    PrivateCommercialAirport(12),
    RecreationalAirport(13),
    PrivateRecreationalAirport(14),
    ClassB(15),
    ClassC(16),
    ClassD(17),
    ClassE(18),
    Heliports(19),
    NationalPark(20),
    NOAA(21),
    Parcel(22),
    PowerPlant(23),
    Prison(24),
    School(25),
    Stadium(26),
    ProhibitedSpecial(27),
    RestrictedSpecial(28),
    TFR(29),
    NuclearPowerPlant(30),
    UnpavedAirports(31),
    DJISpecialZoneNew(32),
    MilitaryZoneNew(33),
    HeliportsNew(34),
    SeaplaneBase(35),
    TFR2(36);
    
    public static final ProtoAdapter<LimitAreaType> ADAPTER = ProtoAdapter.newEnumAdapter(LimitAreaType.class);
    private final int value;

    private LimitAreaType(int value2) {
        this.value = value2;
    }

    public static LimitAreaType fromValue(int value2) {
        switch (value2) {
            case 0:
                return DJIAirport;
            case 1:
                return DJISpecialZone;
            case 2:
                return MilitaryZone;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            default:
                return null;
            case 10:
                return CriticalAirport;
            case 11:
                return AirMapCommercialAirport;
            case 12:
                return PrivateCommercialAirport;
            case 13:
                return RecreationalAirport;
            case 14:
                return PrivateRecreationalAirport;
            case 15:
                return ClassB;
            case 16:
                return ClassC;
            case 17:
                return ClassD;
            case 18:
                return ClassE;
            case 19:
                return Heliports;
            case 20:
                return NationalPark;
            case 21:
                return NOAA;
            case 22:
                return Parcel;
            case 23:
                return PowerPlant;
            case 24:
                return Prison;
            case 25:
                return School;
            case 26:
                return Stadium;
            case 27:
                return ProhibitedSpecial;
            case 28:
                return RestrictedSpecial;
            case 29:
                return TFR;
            case 30:
                return NuclearPowerPlant;
            case 31:
                return UnpavedAirports;
            case 32:
                return DJISpecialZoneNew;
            case 33:
                return MilitaryZoneNew;
            case 34:
                return HeliportsNew;
            case 35:
                return SeaplaneBase;
            case 36:
                return TFR2;
        }
    }

    public int getValue() {
        return this.value;
    }
}
