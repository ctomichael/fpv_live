package dji.sdksharedlib.hardware.abstractions.battery;

import dji.common.battery.PairingState;
import dji.common.error.DJIBatteryError;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataSmartBatteryGetBarCode;
import dji.midware.data.model.P3.DataSmartBatteryGetPair;
import dji.midware.data.model.P3.DataSmartBatterySetPair;
import dji.midware.util.MultipleDataBase;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import java.util.Arrays;

@EXClassNullAway
public class BatteryInspire2Abstraction extends BatteryM600Abstraction {
    public final String TAG = "DJISDKCacheInspire2BatteryAbstraction";

    @Getter("PairingState")
    public void getPairingState(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSmartBatteryGetPair getter0 = new DataSmartBatteryGetPair();
        getter0.setIndex(1);
        final DataSmartBatteryGetPair getter1 = new DataSmartBatteryGetPair();
        getter1.setIndex(2);
        new MultipleDataBase(getter0, getter1).start(new MultipleDataBase.Callback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryInspire2Abstraction.AnonymousClass1 */

            public void onSuccess() {
                PairingState status;
                byte[] checkSum0 = getter0.getCheckSum();
                byte[] checkSum1 = getter1.getCheckSum();
                if (checkSum0 == null || checkSum1 == null || !Arrays.equals(checkSum0, checkSum1)) {
                    status = PairingState.UNPAIRED;
                } else {
                    status = PairingState.PAIRED;
                }
                if (callback != null) {
                    callback.onSuccess(status);
                }
            }

            public void onFails(int index, Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Action(BatteryKeys.PAIR_BATTERIES)
    public void pairBatteries(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSmartBatteryGetBarCode getter0 = new DataSmartBatteryGetBarCode();
        final DataSmartBatteryGetBarCode getter1 = new DataSmartBatteryGetBarCode();
        getter0.setIndex(1);
        getter1.setIndex(2);
        new MultipleDataBase(getter0, getter1).start(new MultipleDataBase.Callback() {
            /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryInspire2Abstraction.AnonymousClass2 */

            public void onSuccess() {
                byte[] barCode0 = getter0.getBarCodeBytes();
                byte[] barCode1 = getter1.getBarCodeBytes();
                String barCode = getter0.getBarCode();
                String barCode2 = getter1.getBarCode();
                if (barCode0 != null && barCode1 != null) {
                    int len = Math.min(barCode1.length, barCode0.length);
                    byte[] checkSum = new byte[len];
                    for (int i = 0; i < len; i++) {
                        checkSum[i] = (byte) (barCode0[i] ^ barCode1[i]);
                    }
                    DataSmartBatterySetPair setter0 = new DataSmartBatterySetPair();
                    setter0.setIndex(1);
                    setter0.setCheckSum(checkSum);
                    DataSmartBatterySetPair setter1 = new DataSmartBatterySetPair();
                    setter1.setIndex(2);
                    setter1.setCheckSum(checkSum);
                    new MultipleDataBase(setter0, setter1).start(new MultipleDataBase.Callback() {
                        /* class dji.sdksharedlib.hardware.abstractions.battery.BatteryInspire2Abstraction.AnonymousClass2.AnonymousClass1 */

                        public void onSuccess() {
                            if (callback != null) {
                                callback.onSuccess(PairingState.PAIRED);
                                BatteryInspire2Abstraction.this.notifyValueChangeForKeyPathFromGetter(PairingState.PAIRED, KeyHelper.getBatteryKey(BatteryInspire2Abstraction.this.index, "PairingState"), null);
                            }
                        }

                        public void onFails(int index, Ccode ccode) {
                            if (callback != null) {
                                callback.onFails(DJIBatteryError.BATTERY_PAIR_FAILED);
                            }
                        }
                    });
                } else if (callback != null) {
                    callback.onFails(DJIBatteryError.BATTERY_PAIR_FAILED);
                }
            }

            public void onFails(int index, Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }
}
