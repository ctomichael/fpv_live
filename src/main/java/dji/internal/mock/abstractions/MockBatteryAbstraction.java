package dji.internal.mock.abstractions;

import dji.common.battery.ConnectionState;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mock.MockHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.battery.BatteryAbstraction;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class MockBatteryAbstraction extends BatteryAbstraction {
    public static int remainingPercentage;

    public MockBatteryAbstraction() {
        remainingPercentage = 100;
        generateFakeRemainingPercentage();
    }

    private void generateFakeRemainingPercentage() {
        Observable.timer(200, TimeUnit.MILLISECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockBatteryAbstraction.AnonymousClass1 */

            public Observable<Boolean> call(Long aLong) {
                MockBatteryAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockBatteryAbstraction.remainingPercentage), BatteryKeys.FULL_CHARGE_CAPACITY);
                MockBatteryAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockBatteryAbstraction.remainingPercentage), "ChargeRemaining");
                MockBatteryAbstraction.this.notifyValueChangeForKeyPath(20, BatteryKeys.VOLTAGE);
                MockBatteryAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockBatteryAbstraction.remainingPercentage * 100), BatteryKeys.CURRENT);
                MockBatteryAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockBatteryAbstraction.remainingPercentage), BatteryKeys.LIFETIME_REMAINING);
                MockBatteryAbstraction.this.notifyValueChangeForKeyPath(Float.valueOf((273.15f / ((float) (MockBatteryAbstraction.remainingPercentage + 1))) - 273.15f), BatteryKeys.TEMPERATURE);
                MockBatteryAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockBatteryAbstraction.remainingPercentage), BatteryKeys.NUMBER_OF_DISCHARGES);
                MockBatteryAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(MockBatteryAbstraction.remainingPercentage), BatteryKeys.CHARGE_REMAINING_IN_PERCENT);
                MockBatteryAbstraction.remainingPercentage--;
                if (MockBatteryAbstraction.remainingPercentage < 0) {
                    MockBatteryAbstraction.remainingPercentage = 100;
                }
                if (MockBatteryAbstraction.remainingPercentage <= 60 || MockBatteryAbstraction.remainingPercentage >= 80) {
                    MockBatteryAbstraction.this.notifyValueChangeForKeyPath(ConnectionState.NORMAL, BatteryKeys.CONNECTION_STATE);
                } else {
                    MockBatteryAbstraction.this.notifyValueChangeForKeyPathFromSetter(ConnectionState.INVALID, BatteryKeys.CONNECTION_STATE);
                }
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }

    @Getter(BatteryKeys.LIFETIME_REMAINING)
    public void getLifetimeRemaining(DJISDKCacheHWAbstraction.InnerCallback callback) {
        MockHelper.doOnResultForCallback(callback, Integer.valueOf(remainingPercentage));
    }

    @Getter(BatteryKeys.NUMBER_OF_DISCHARGES)
    public void getNumberOfDischarges(DJISDKCacheHWAbstraction.InnerCallback callback) {
        MockHelper.doOnResultForCallback(callback, Integer.valueOf(remainingPercentage));
    }

    @Getter(BatteryKeys.FULL_CHARGE_CAPACITY)
    public void getFullChargeCapacity(DJISDKCacheHWAbstraction.InnerCallback callback) {
        MockHelper.doOnResultForCallback(callback, Integer.valueOf(remainingPercentage));
    }

    @Getter(BatteryKeys.CURRENT)
    public void getCurrent(DJISDKCacheHWAbstraction.InnerCallback callback) {
        MockHelper.doOnResultForCallback(callback, Integer.valueOf(remainingPercentage * 100));
    }

    @Getter("ChargeRemaining")
    public void getChargeRemaining(DJISDKCacheHWAbstraction.InnerCallback callback) {
        MockHelper.doOnResultForCallback(callback, Integer.valueOf(remainingPercentage));
    }

    @Getter(BatteryKeys.VOLTAGE)
    public void getVoltage(DJISDKCacheHWAbstraction.InnerCallback callback) {
        MockHelper.doOnResultForCallback(callback, 20);
    }

    @Getter(BatteryKeys.CHARGE_REMAINING_IN_PERCENT)
    public void getChargeRemainingInPercent(DJISDKCacheHWAbstraction.InnerCallback callback) {
        MockHelper.doOnResultForCallback(callback, Integer.valueOf(remainingPercentage));
    }
}
