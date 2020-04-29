package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdSimulator;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataSimulatorSimulateFlightCommend extends DataBase implements DJIDataSyncListener {
    private static DataSimulatorSimulateFlightCommend instance;
    private double altitude;
    private int byte1st = 128;
    private int byte2nd = 64;
    private int byte3rd = 32;
    private int byte4th = 16;
    private int byte5th = 8;
    private int byte6th = 4;
    private int byte7th = 2;
    private int byte8th = 1;
    private int gpsCount;
    private int hz;
    private double latitude;
    private double longitude;
    private int remoteControl;
    private int simulatorSwitch;
    private int status1;
    private int status2;
    private int status3;
    private int status4;

    public static synchronized DataSimulatorSimulateFlightCommend getInstance() {
        DataSimulatorSimulateFlightCommend dataSimulatorSimulateFlightCommend;
        synchronized (DataSimulatorSimulateFlightCommend.class) {
            if (instance == null) {
                instance = new DataSimulatorSimulateFlightCommend();
            }
            dataSimulatorSimulateFlightCommend = instance;
        }
        return dataSimulatorSimulateFlightCommend;
    }

    public DataSimulatorSimulateFlightCommend openSimulator() {
        this.simulatorSwitch = 0;
        return this;
    }

    public DataSimulatorSimulateFlightCommend closeSimulator() {
        this.simulatorSwitch = 2;
        return this;
    }

    public DataSimulatorSimulateFlightCommend setUseRC(boolean bool) {
        if (bool) {
            this.remoteControl |= 1;
        } else {
            this.remoteControl |= 0;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setUseThird(boolean bool) {
        if (bool) {
            this.remoteControl |= 2;
        } else {
            this.remoteControl |= 0;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setBatterySim(boolean bool) {
        if (bool) {
            this.remoteControl |= 4;
        } else {
            this.remoteControl |= 0;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setLatitude(double latitude2) {
        this.latitude = Radian(latitude2);
        return this;
    }

    public DataSimulatorSimulateFlightCommend setLongitude(double longitude2) {
        this.longitude = Radian(longitude2);
        return this;
    }

    public DataSimulatorSimulateFlightCommend setAltitude(double altitude2) {
        this.altitude = 1.0d * altitude2;
        return this;
    }

    public DataSimulatorSimulateFlightCommend setGpsCount(int gpsCount2) {
        this.gpsCount = gpsCount2;
        return this;
    }

    public DataSimulatorSimulateFlightCommend setHz(int hz2) {
        this.hz = hz2;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataSimulatorSimulateFlightCommend setRoll(boolean bool) {
        if (bool) {
            this.status1 |= this.byte1st;
        } else {
            this.status1 = (this.status1 | this.byte1st) - this.byte1st;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setPitch(boolean bool) {
        if (bool) {
            this.status1 |= this.byte2nd;
        } else {
            this.status1 = (this.status1 | this.byte2nd) - this.byte2nd;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setYaw(boolean bool) {
        if (bool) {
            this.status1 |= this.byte3rd;
        } else {
            this.status1 = (this.status1 | this.byte3rd) - this.byte3rd;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setPositionX(boolean bool) {
        if (bool) {
            this.status1 |= this.byte4th;
        } else {
            this.status1 = (this.status1 | this.byte4th) - this.byte4th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setPositionY(boolean bool) {
        if (bool) {
            this.status1 |= this.byte5th;
        } else {
            this.status1 = (this.status1 | this.byte5th) - this.byte5th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setPositionZ(boolean bool) {
        if (bool) {
            this.status1 |= this.byte6th;
        } else {
            this.status1 = (this.status1 | this.byte6th) - this.byte6th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setLatitude(boolean bool) {
        if (bool) {
            this.status1 |= this.byte7th;
        } else {
            this.status1 = (this.status1 | this.byte7th) - this.byte7th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setLongitude(boolean bool) {
        if (bool) {
            this.status1 |= this.byte8th;
        } else {
            this.status1 = (this.status1 | this.byte8th) - this.byte8th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setVelocityX(boolean bool) {
        if (bool) {
            this.status2 |= this.byte1st;
        } else {
            this.status2 = (this.status2 | this.byte1st) - this.byte1st;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setVelocityY(boolean bool) {
        if (bool) {
            this.status2 |= this.byte2nd;
        } else {
            this.status2 = (this.status2 | this.byte2nd) - this.byte2nd;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setVelocityZ(boolean bool) {
        if (bool) {
            this.status2 |= this.byte3rd;
        } else {
            this.status2 = (this.status2 | this.byte3rd) - this.byte3rd;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setAccelerateX(boolean bool) {
        if (bool) {
            this.status2 |= this.byte4th;
        } else {
            this.status2 = (this.status2 | this.byte4th) - this.byte4th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setAccelerateY(boolean bool) {
        if (bool) {
            this.status2 |= this.byte5th;
        } else {
            this.status2 = (this.status2 | this.byte5th) - this.byte5th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setAccelerateZ(boolean bool) {
        if (bool) {
            this.status2 |= this.byte6th;
        } else {
            this.status2 = (this.status2 | this.byte6th) - this.byte6th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setGyroX(boolean bool) {
        if (bool) {
            this.status2 |= this.byte7th;
        } else {
            this.status2 = (this.status2 | this.byte7th) - this.byte7th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setGyroY(boolean bool) {
        if (bool) {
            this.status2 |= this.byte8th;
        } else {
            this.status2 = (this.status2 | this.byte8th) - this.byte8th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setGyroZ(boolean bool) {
        if (bool) {
            this.status3 |= this.byte1st;
        } else {
            this.status3 = (this.status3 | this.byte1st) - this.byte1st;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setRpm1(boolean bool) {
        if (bool) {
            this.status3 |= this.byte2nd;
        } else {
            this.status3 = (this.status3 | this.byte2nd) - this.byte2nd;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setRpm2(boolean bool) {
        if (bool) {
            this.status3 |= this.byte3rd;
        } else {
            this.status3 = (this.status3 | this.byte3rd) - this.byte3rd;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setRpm3(boolean bool) {
        if (bool) {
            this.status3 |= this.byte4th;
        } else {
            this.status3 = (this.status3 | this.byte4th) - this.byte4th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setRpm4(boolean bool) {
        if (bool) {
            this.status3 |= this.byte5th;
        } else {
            this.status3 = (this.status3 | this.byte5th) - this.byte5th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setRpm5(boolean bool) {
        if (bool) {
            this.status3 |= this.byte6th;
        } else {
            this.status3 = (this.status3 | this.byte6th) - this.byte6th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setRpm6(boolean bool) {
        if (bool) {
            this.status3 |= this.byte7th;
        } else {
            this.status3 = (this.status3 | this.byte7th) - this.byte7th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setRpm7(boolean bool) {
        if (bool) {
            this.status3 |= this.byte8th;
        } else {
            this.status3 = (this.status3 | this.byte8th) - this.byte8th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setRpm8(boolean bool) {
        if (bool) {
            this.status4 |= this.byte1st;
        } else {
            this.status4 = (this.status4 | this.byte1st) - this.byte1st;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setSimulateTime(boolean bool) {
        if (bool) {
            this.status4 |= this.byte2nd;
        } else {
            this.status4 = (this.status4 | this.byte2nd) - this.byte2nd;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setLEDColor(boolean bool) {
        if (bool) {
            this.status4 |= this.byte3rd;
        } else {
            this.status4 = (this.status4 | this.byte3rd) - this.byte3rd;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setGearState(boolean bool) {
        if (bool) {
            this.status4 |= this.byte4th;
        } else {
            this.status4 = (this.status4 | this.byte4th) - this.byte4th;
        }
        return this;
    }

    public DataSimulatorSimulateFlightCommend setQuaternion(boolean bool) {
        if (bool) {
            this.status4 |= this.byte5th;
        } else {
            this.status4 = (this.status4 | this.byte5th) - this.byte5th;
        }
        return this;
    }

    private double Radian(double x) {
        return (3.141592653589793d * x) / 180.0d;
    }

    private double Degree(double x) {
        return (180.0d * x) / 3.141592653589793d;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SIMULATOR.value();
        pack.cmdId = CmdIdSimulator.CmdIdType.SimulateFlightCommend.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[36];
        this._sendData[0] = (byte) this.simulatorSwitch;
        this._sendData[1] = (byte) this.remoteControl;
        this._sendData[2] = (byte) this.hz;
        this._sendData[3] = (byte) this.gpsCount;
        System.arraycopy(BytesUtil.getBytes(this.longitude), 0, this._sendData, 4, 8);
        System.arraycopy(BytesUtil.getBytes(this.latitude), 0, this._sendData, 12, 8);
        System.arraycopy(BytesUtil.getBytes(this.altitude), 0, this._sendData, 20, 8);
        this._sendData[28] = (byte) this.status1;
        this._sendData[29] = (byte) this.status2;
        this._sendData[30] = (byte) this.status3;
        this._sendData[31] = (byte) this.status4;
        System.arraycopy(BytesUtil.getBytes(0), 0, this._sendData, 32, 4);
    }
}
