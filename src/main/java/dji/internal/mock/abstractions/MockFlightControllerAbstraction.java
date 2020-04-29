package dji.internal.mock.abstractions;

import dji.common.bus.EventBus;
import dji.common.bus.LogicEventBus;
import dji.common.flightcontroller.BatteryThresholdBehavior;
import dji.common.flightcontroller.FlightMode;
import dji.common.flightcontroller.GPSSignalLevel;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.CompassLogic;
import dji.internal.logics.ESCLogic;
import dji.internal.logics.FPVTipLogic;
import dji.internal.logics.IMULogic;
import dji.internal.logics.Message;
import dji.internal.logics.RadioChannelQualityLogic;
import dji.internal.logics.VisionLogic;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantAbstraction;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.keycatalog.RTKKeys;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class MockFlightControllerAbstraction extends FlightControllerAbstraction {
    private static final int DEFAULT_FLIGHT_TIME = 2000;
    protected static final int MAX_ALTITUDE = 300;
    protected static final double ONE_METER_OFFSET = 8.99322E-6d;
    /* access modifiers changed from: private */
    public double aircraftLatitude = this.homeLocation.getLatitude();
    /* access modifiers changed from: private */
    public double aircraftLongitude = this.homeLocation.getLongitude();
    /* access modifiers changed from: private */
    public double aircraftPitch = 0.0d;
    /* access modifiers changed from: private */
    public double aircraftRoll = 0.0d;
    /* access modifiers changed from: private */
    public double aircraftYaw = 0.0d;
    /* access modifiers changed from: private */
    public float altitude = 0.0f;
    /* access modifiers changed from: private */
    public String[] binary_statuses = {"Normal", "Abnormal"};
    /* access modifiers changed from: private */
    public int counter = 0;
    /* access modifiers changed from: private */
    public String[] generic_statuses = {"Offline", "Good", "Warning", "Error"};
    /* access modifiers changed from: private */
    public boolean goingUp = true;
    /* access modifiers changed from: private */
    public int gpsSatelliteNumber = 0;
    /* access modifiers changed from: private */
    public GPSSignalLevel gpsSignalStatus = GPSSignalLevel.find(0);
    /* access modifiers changed from: private */
    public LocationCoordinate2D homeLocation = new LocationCoordinate2D(37.421972d, -122.137385d);
    boolean isAutoLanding = false;
    boolean isFlying = false;
    boolean isGoingHome = false;
    boolean isMotorUp = false;
    boolean isTakeOff = false;
    /* access modifiers changed from: private */
    public String[] radio_statuses = {"Unknown", "Good", "Strong Interference. Fly with caution.", "Poor"};
    /* access modifiers changed from: private */
    public int remainingFlightTime = 2000;
    /* access modifiers changed from: private */
    public boolean rtkEnabled = false;
    /* access modifiers changed from: private */
    public float velocityX = 0.0f;
    /* access modifiers changed from: private */
    public float velocityY = 0.0f;
    /* access modifiers changed from: private */
    public float velocityZ = 0.0f;

    static /* synthetic */ int access$010(MockFlightControllerAbstraction x0) {
        int i = x0.remainingFlightTime;
        x0.remainingFlightTime = i - 1;
        return i;
    }

    static /* synthetic */ float access$208(MockFlightControllerAbstraction x0) {
        float f = x0.altitude;
        x0.altitude = 1.0f + f;
        return f;
    }

    static /* synthetic */ float access$210(MockFlightControllerAbstraction x0) {
        float f = x0.altitude;
        x0.altitude = f - 1.0f;
        return f;
    }

    static /* synthetic */ int access$3108(MockFlightControllerAbstraction x0) {
        int i = x0.counter;
        x0.counter = i + 1;
        return i;
    }

    static /* synthetic */ int access$5508(MockFlightControllerAbstraction x0) {
        int i = x0.gpsSatelliteNumber;
        x0.gpsSatelliteNumber = i + 1;
        return i;
    }

    public MockFlightControllerAbstraction() {
        generateVeryHighFrequencyData();
        generateHighFrequencyData();
        generateMediumFrequencyData();
        generateLowFrequencyData();
        generateFakeEvents();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
        addCharacteristics(FlightControllerKeys.class, getClass());
    }

    /* access modifiers changed from: protected */
    public boolean isNewProgressOfActivation() {
        return false;
    }

    /* access modifiers changed from: protected */
    public IntelligentFlightAssistantAbstraction newIntelligentFlightAssistantIfSupport() {
        return new MockIntelligentFlightAssistantAbstraction();
    }

    private void generateVeryHighFrequencyData() {
        Observable.timer(100, TimeUnit.MILLISECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockFlightControllerAbstraction.AnonymousClass1 */

            public Observable<Boolean> call(Long aLong) {
                MockFlightControllerAbstraction.access$010(MockFlightControllerAbstraction.this);
                if (MockFlightControllerAbstraction.this.remainingFlightTime < 0) {
                    int unused = MockFlightControllerAbstraction.this.remainingFlightTime = 2000;
                } else {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(Integer.valueOf(MockFlightControllerAbstraction.this.remainingFlightTime), FlightControllerKeys.REMAINING_FLIGHT_TIME);
                }
                if (MockFlightControllerAbstraction.this.isTakeOff) {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Float.valueOf(MockFlightControllerAbstraction.this.altitude), FlightControllerKeys.ALTITUDE);
                    if (MockFlightControllerAbstraction.this.isAutoLanding || MockFlightControllerAbstraction.this.isGoingHome) {
                        MockFlightControllerAbstraction.access$210(MockFlightControllerAbstraction.this);
                        if (MockFlightControllerAbstraction.this.altitude <= 0.0f) {
                            MockFlightControllerAbstraction.this.isFlying = false;
                            MockFlightControllerAbstraction.this.isMotorUp = false;
                            MockFlightControllerAbstraction.this.isTakeOff = false;
                            MockFlightControllerAbstraction.this.isAutoLanding = false;
                            MockFlightControllerAbstraction.this.isGoingHome = false;
                        }
                    } else {
                        if (MockFlightControllerAbstraction.this.goingUp) {
                            MockFlightControllerAbstraction.access$208(MockFlightControllerAbstraction.this);
                        } else {
                            MockFlightControllerAbstraction.access$210(MockFlightControllerAbstraction.this);
                        }
                        if (MockFlightControllerAbstraction.this.goingUp && MockFlightControllerAbstraction.this.altitude >= 300.0f) {
                            boolean unused2 = MockFlightControllerAbstraction.this.goingUp = false;
                        } else if (!MockFlightControllerAbstraction.this.goingUp && MockFlightControllerAbstraction.this.altitude <= 0.0f) {
                            boolean unused3 = MockFlightControllerAbstraction.this.goingUp = true;
                        }
                    }
                }
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Float.valueOf(MockFlightControllerAbstraction.this.velocityX), FlightControllerKeys.VELOCITY_X);
                float unused4 = MockFlightControllerAbstraction.this.velocityX = MockFlightControllerAbstraction.this.altitude / 10.0f;
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }

    private void generateHighFrequencyData() {
        Observable.timer(200, TimeUnit.MILLISECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockFlightControllerAbstraction.AnonymousClass2 */

            public Observable<Boolean> call(Long aLong) {
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Float.valueOf(MockFlightControllerAbstraction.this.velocityY), FlightControllerKeys.VELOCITY_Y);
                float unused = MockFlightControllerAbstraction.this.velocityY = MockFlightControllerAbstraction.this.altitude / 10.0f;
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }

    /* access modifiers changed from: private */
    public int getGpsLevelForOldFlightController(int gpsNum) {
        if (gpsNum == 0 || gpsNum >= 50) {
            return 0;
        }
        if (gpsNum <= 7) {
            return 1;
        }
        if (gpsNum > 10) {
            return 5;
        }
        return gpsNum - 6;
    }

    private void generateMediumFrequencyData() {
        Observable.timer(300, TimeUnit.MILLISECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockFlightControllerAbstraction.AnonymousClass3 */

            public Observable<Boolean> call(Long aLong) {
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(MockFlightControllerAbstraction.this.homeLocation, FlightControllerKeys.HOME_LOCATION);
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Double.valueOf(MockFlightControllerAbstraction.this.aircraftLatitude), FlightControllerKeys.AIRCRAFT_LOCATION_LATITUDE);
                double unused = MockFlightControllerAbstraction.this.aircraftLatitude = MockFlightControllerAbstraction.this.homeLocation.getLatitude() + (((double) (MockFlightControllerAbstraction.this.altitude * 5.0f)) * 8.99322E-6d);
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Double.valueOf(MockFlightControllerAbstraction.this.aircraftLongitude), FlightControllerKeys.AIRCRAFT_LOCATION_LONGITUDE);
                double unused2 = MockFlightControllerAbstraction.this.aircraftLongitude = MockFlightControllerAbstraction.this.homeLocation.getLongitude() + (((double) (MockFlightControllerAbstraction.this.altitude * 5.0f)) * 8.99322E-6d);
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Double.valueOf(MockFlightControllerAbstraction.this.aircraftPitch), FlightControllerKeys.ATTITUDE_PITCH);
                if (MockFlightControllerAbstraction.this.goingUp) {
                    double unused3 = MockFlightControllerAbstraction.this.aircraftPitch = (double) (((-MockFlightControllerAbstraction.this.altitude) / 300.0f) * 45.0f);
                } else {
                    double unused4 = MockFlightControllerAbstraction.this.aircraftPitch = (double) ((MockFlightControllerAbstraction.this.altitude / 300.0f) * 45.0f);
                }
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Double.valueOf(MockFlightControllerAbstraction.this.aircraftRoll), FlightControllerKeys.ATTITUDE_ROLL);
                if (MockFlightControllerAbstraction.this.goingUp) {
                    double unused5 = MockFlightControllerAbstraction.this.aircraftRoll = (double) (((-MockFlightControllerAbstraction.this.altitude) / 300.0f) * 45.0f);
                } else {
                    double unused6 = MockFlightControllerAbstraction.this.aircraftRoll = (double) ((MockFlightControllerAbstraction.this.altitude / 300.0f) * 45.0f);
                }
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Double.valueOf(MockFlightControllerAbstraction.this.aircraftYaw), FlightControllerKeys.ATTITUDE_YAW);
                if (MockFlightControllerAbstraction.this.goingUp) {
                    double unused7 = MockFlightControllerAbstraction.this.aircraftYaw = (double) (((-MockFlightControllerAbstraction.this.altitude) / 300.0f) * 360.0f);
                } else {
                    double unused8 = MockFlightControllerAbstraction.this.aircraftYaw = (double) ((MockFlightControllerAbstraction.this.altitude / 300.0f) * 360.0f);
                }
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(MockFlightControllerAbstraction.this.isGoingHome), MockFlightControllerAbstraction.this.convertKeyToPath(FlightControllerKeys.IS_GOING_HOME));
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(MockFlightControllerAbstraction.this.isAutoLanding), MockFlightControllerAbstraction.this.convertKeyToPath(FlightControllerKeys.IS_LANDING));
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(MockFlightControllerAbstraction.this.isMotorUp), MockFlightControllerAbstraction.this.convertKeyToPath(FlightControllerKeys.ARE_MOTOR_ON));
                MockFlightControllerAbstraction.this.isFlying = !MockFlightControllerAbstraction.this.isFlying;
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(MockFlightControllerAbstraction.this.isFlying), MockFlightControllerAbstraction.this.convertKeyToPath(FlightControllerKeys.IS_FLYING));
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Float.valueOf(MockFlightControllerAbstraction.this.velocityZ), FlightControllerKeys.VELOCITY_Z);
                float unused9 = MockFlightControllerAbstraction.this.velocityZ = MockFlightControllerAbstraction.this.altitude / 10.0f;
                if (!MockFlightControllerAbstraction.this.goingUp) {
                    float unused10 = MockFlightControllerAbstraction.this.velocityZ = MockFlightControllerAbstraction.this.velocityZ * -1.0f;
                }
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }

    @Action(FlightControllerKeys.TAKE_OFF)
    public void takeOff(DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.isTakeOff = true;
        this.isMotorUp = true;
        this.isFlying = true;
        CallbackUtils.onSuccess(callback, (Object) null);
    }

    @Action(FlightControllerKeys.START_LANDING)
    public void autoLanding(DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.isAutoLanding = true;
        CallbackUtils.onSuccess(callback, (Object) null);
    }

    @Action(FlightControllerKeys.CANCEL_LANDING)
    public void cancelAutoLanding(DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.isAutoLanding = false;
        CallbackUtils.onSuccess(callback, (Object) null);
    }

    @Action(FlightControllerKeys.TURN_ON_MOTORS)
    public void turnOnMotors(DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.isMotorUp = true;
        CallbackUtils.onSuccess(callback, (Object) null);
    }

    @Action(FlightControllerKeys.TURN_OFF_MOTORS)
    public void turnOffMotors(DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.isMotorUp = false;
        CallbackUtils.onSuccess(callback, (Object) null);
    }

    @Action(FlightControllerKeys.START_GO_HOME)
    public void goHome(DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.isGoingHome = true;
        CallbackUtils.onSuccess(callback, (Object) null);
    }

    @Action(FlightControllerKeys.CANCEL_GO_HOME)
    public void cancelGoHome(DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.isGoingHome = false;
        CallbackUtils.onSuccess(callback, (Object) null);
    }

    public void generateLowFrequencyData() {
        Observable.timer(1000, TimeUnit.MILLISECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockFlightControllerAbstraction.AnonymousClass4 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.internal.mock.abstractions.MockFlightControllerAbstraction.access$3200(dji.internal.mock.abstractions.MockFlightControllerAbstraction, java.lang.Object, java.lang.String):void
             arg types: [dji.internal.mock.abstractions.MockFlightControllerAbstraction, int, java.lang.String]
             candidates:
              dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.access$3200(dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
              dji.internal.mock.abstractions.MockFlightControllerAbstraction.access$3200(dji.internal.mock.abstractions.MockFlightControllerAbstraction, java.lang.Object, java.lang.String):void */
            public Observable<Boolean> call(Long aLong) {
                boolean z = false;
                MockFlightControllerAbstraction.access$3108(MockFlightControllerAbstraction.this);
                if (MockBatteryAbstraction.remainingPercentage > 50) {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter((Object) 55, FlightControllerKeys.BATTERY_PERCENTAGE_NEEDED_TO_GO_HOME);
                } else {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(20, FlightControllerKeys.BATTERY_PERCENTAGE_NEEDED_TO_GO_HOME);
                }
                if (MockBatteryAbstraction.remainingPercentage <= 10) {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(BatteryThresholdBehavior.find(2), FlightControllerKeys.BATTERY_THRESHOLD_BEHAVIOR);
                } else if (MockBatteryAbstraction.remainingPercentage <= 30) {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(BatteryThresholdBehavior.find(1), FlightControllerKeys.BATTERY_THRESHOLD_BEHAVIOR);
                } else {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(BatteryThresholdBehavior.find(0), FlightControllerKeys.BATTERY_THRESHOLD_BEHAVIOR);
                }
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(15, FlightControllerKeys.CURRENT_LAND_IMMEDIATELY_BATTERY);
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(10, FlightControllerKeys.SERIOUS_LOW_BATTERY_WARNING_THRESHOLD);
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(30, FlightControllerKeys.LOW_BATTERY_WARNING_THRESHOLD);
                if (MockBatteryAbstraction.remainingPercentage > 30) {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(true, KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.COLLISION_AVOIDANCE_ENABLED));
                } else {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(false, KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.COLLISION_AVOIDANCE_ENABLED));
                }
                if (MockBatteryAbstraction.remainingPercentage <= 40 || MockBatteryAbstraction.remainingPercentage >= 50) {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(true, KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_SENSOR_WORKING));
                } else {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(false, KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_SENSOR_WORKING));
                }
                if (MockFlightControllerAbstraction.this.counter % 2 == 0) {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(true, FlightControllerKeys.IS_VISION_POSITIONING_SENSOR_BEING_USED);
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(true, FlightControllerKeys.IS_VISION_SENSOR_WORK);
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(true, FlightControllerKeys.IS_VISION_SENSOR_ENABLE);
                } else {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(false, FlightControllerKeys.IS_VISION_POSITIONING_SENSOR_BEING_USED);
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(true, FlightControllerKeys.IS_VISION_SENSOR_WORK);
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(true, FlightControllerKeys.IS_VISION_SENSOR_ENABLE);
                }
                if (MockFlightControllerAbstraction.this.altitude <= 3.0f) {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(true, FlightControllerKeys.IS_VISION_POSITIONING_SENSOR_BEING_USED);
                } else {
                    MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(false, FlightControllerKeys.IS_VISION_POSITIONING_SENSOR_BEING_USED);
                }
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPathFromSetter(FlightMode.find((int) ((MockFlightControllerAbstraction.this.altitude + 300.0f) / 20.0f)), FlightControllerKeys.FLIGHT_MODE);
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(MockFlightControllerAbstraction.this.gpsSignalStatus, FlightControllerKeys.GPS_SIGNAL_LEVEL);
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockFlightControllerAbstraction.this.gpsSatelliteNumber), FlightControllerKeys.SATELLITE_COUNT);
                MockFlightControllerAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(MockFlightControllerAbstraction.this.rtkEnabled), RTKKeys.RTK_ENABLED);
                MockFlightControllerAbstraction.access$5508(MockFlightControllerAbstraction.this);
                GPSSignalLevel unused = MockFlightControllerAbstraction.this.gpsSignalStatus = GPSSignalLevel.find(MockFlightControllerAbstraction.this.getGpsLevelForOldFlightController(MockFlightControllerAbstraction.this.gpsSatelliteNumber));
                if (MockFlightControllerAbstraction.this.gpsSatelliteNumber >= 30) {
                    int unused2 = MockFlightControllerAbstraction.this.gpsSatelliteNumber = 0;
                }
                MockFlightControllerAbstraction mockFlightControllerAbstraction = MockFlightControllerAbstraction.this;
                if (MockFlightControllerAbstraction.this.gpsSatelliteNumber > 15) {
                    z = true;
                }
                boolean unused3 = mockFlightControllerAbstraction.rtkEnabled = z;
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }

    private void generateFakeEvents() {
        Observable.timer(1000, TimeUnit.MILLISECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockFlightControllerAbstraction.AnonymousClass5 */

            public Observable<Boolean> call(Long aLong) {
                int quadCount = MockFlightControllerAbstraction.this.counter % 4;
                String binaryTitle = MockFlightControllerAbstraction.this.binary_statuses[MockFlightControllerAbstraction.this.counter % 2];
                Message.Type quadType = Message.Type.values()[quadCount];
                Message binaryMessage = new Message(MockFlightControllerAbstraction.this.counter % 2 == 0 ? Message.Type.GOOD : Message.Type.ERROR, binaryTitle, "");
                EventBus bus = LogicEventBus.getInstance();
                bus.post(new FPVTipLogic.FPVTipEvent(new Message(quadType, MockFlightControllerAbstraction.this.generic_statuses[quadCount], "")));
                bus.post(new RadioChannelQualityLogic.RadioChannelQualityEvent(new Message(quadType, MockFlightControllerAbstraction.this.radio_statuses[quadCount], "")));
                bus.post(new CompassLogic.CompassEvent(binaryMessage));
                bus.post(new IMULogic.IMUEvent(binaryMessage));
                bus.post(new ESCLogic.ESCEvent(binaryMessage));
                bus.post(new VisionLogic.VisionEvent(binaryMessage));
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }
}
