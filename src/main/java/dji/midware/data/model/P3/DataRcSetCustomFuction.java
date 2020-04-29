package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import org.bouncycastle.crypto.tls.CipherSuite;

@Keep
@EXClassNullAway
public class DataRcSetCustomFuction extends DataBase implements DJIDataSyncListener {
    private static DataRcSetCustomFuction instance = null;
    private int c1value;
    private int c2value;
    private int c3value = -1;
    private byte[] customButtonValue;

    public enum C3SDRExtCustomButton {
        POWER,
        LJS,
        RJS,
        PAUSE,
        RET,
        PHOTO,
        RECORD,
        FOCUS,
        FLYMODE,
        DIAL,
        DIAL_KEY
    }

    public static synchronized DataRcSetCustomFuction getInstance() {
        DataRcSetCustomFuction dataRcSetCustomFuction;
        synchronized (DataRcSetCustomFuction.class) {
            if (instance == null) {
                instance = new DataRcSetCustomFuction();
            }
            dataRcSetCustomFuction = instance;
        }
        return dataRcSetCustomFuction;
    }

    public DataRcSetCustomFuction setC1(int c1value2) {
        this.c1value = c1value2;
        return this;
    }

    public DataRcSetCustomFuction setC2(int c2value2) {
        this.c2value = c2value2;
        return this;
    }

    public DataRcSetCustomFuction setC3(int c3value2) {
        this.c3value = c3value2;
        return this;
    }

    public DataRcSetCustomFuction setCustomButton(ProCustomButton btn, int value) {
        ProCustomButton[] buttons = ProCustomButton.values();
        byte[] values = new byte[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == btn) {
                values[i] = (byte) value;
            } else {
                values[i] = -1;
            }
        }
        this.customButtonValue = values;
        ensureLWRWDefined(btn);
        return this;
    }

    public DataRcSetCustomFuction setCustomSingleBtnUndefined(ProCustomButton btn) {
        ProCustomButton[] buttons = ProCustomButton.values();
        int i = 0;
        while (true) {
            if (i >= buttons.length) {
                break;
            } else if (buttons[i] == btn) {
                this.customButtonValue[i] = (byte) DJICustomType.OTHER.value();
                break;
            } else {
                i++;
            }
        }
        return this;
    }

    private void ensureLWRWDefined(ProCustomButton btn) {
        if (btn == ProCustomButton.LW || btn == ProCustomButton.RW) {
            int indexLw = -1;
            int indexRw = -1;
            ProCustomButton[] buttons = ProCustomButton.values();
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i] == ProCustomButton.LW) {
                    indexLw = i;
                } else if (buttons[i] == ProCustomButton.RW) {
                    indexRw = i;
                }
            }
            if (indexLw != -1 && indexRw != -1) {
                if (btn == ProCustomButton.LW) {
                    this.customButtonValue[indexRw] = (byte) ((DJICustomType.GimbalPitch.value() + DJICustomType.GimbalYaw.value()) - this.customButtonValue[indexLw]);
                } else if (btn == ProCustomButton.RW) {
                    this.customButtonValue[indexLw] = (byte) ((DJICustomType.GimbalPitch.value() + DJICustomType.GimbalYaw.value()) - this.customButtonValue[indexRw]);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.customButtonValue != null) {
            this._sendData = this.customButtonValue;
        } else if (this.c3value != -1) {
            this._sendData = new byte[3];
            this._sendData[0] = (byte) this.c1value;
            this._sendData[1] = (byte) this.c2value;
            this._sendData[2] = (byte) this.c3value;
        } else {
            this._sendData = new byte[2];
            this._sendData[0] = (byte) this.c1value;
            this._sendData[1] = (byte) this.c2value;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetCustomFuction.value();
        pack.data = getSendData();
        pack.timeOut = 1000;
        start(pack, callBack);
    }

    @Keep
    public enum DJICustomType {
        CameraSetting(0),
        GimbalCenter(1),
        SwitchGimbalMode(2),
        MapSwitch(3),
        ClearRoute(4),
        Battery(5),
        GimbalDirec(6),
        CenterMetering(7),
        AeLock(8),
        ForeArm(9),
        Vision1(10),
        Vision2(11),
        GimbalRecenter(12),
        LiveViewExpand(13),
        QuickCircle(14),
        Navigation(15),
        PlayBack(16),
        CenterFocus(17),
        Navigation1(18),
        Navigation2(19),
        IndexShutterISOSwitch(20),
        FixWing(21),
        OneKeyTakeOffLanding(22),
        GetGimbalControl(23),
        GimbalMode(24),
        QuickSetting(25),
        AFMF(26),
        CloseTips(27),
        SmartGear(28),
        ReviewWarning(29),
        SwitchFrequence(30),
        UpdateHomeDrone(31),
        UpdateHomeRC(32),
        UpdateHomeStartFly(33),
        FlyExp(34),
        GimbalExp(35),
        GimbalRoll(36),
        Focus(37),
        Apeture(38),
        CameraZoom(39),
        FpvPitch(40),
        CompositionMode(41),
        Spotlight(42),
        FocusPeaking(43),
        GridLine(44),
        Histogram(45),
        AEMFSwitch(46),
        OverExposure(47),
        ISO(48),
        SHUT(49),
        Transformation(54),
        CloseRadarMap(59),
        CloseDownVPS(60),
        CloseFrontAss(61),
        SwitchGimbalFpv(62),
        FullScreen(63),
        EnterGSMode(64),
        ExitGSMode(65),
        ForceMapSwitch(66),
        ExitFixWing(67),
        MenuSetting(70),
        GimbalPitch(71),
        GimbalYaw(73),
        TorsGyro(77),
        VertVelocity(78),
        GimbalPitchYawCenter(79),
        ColorOscilloScope(80),
        GimbalDownCenter(81),
        MasterSlaveGroup(82),
        OnBoardInterfaceZero(86),
        OnBoardInterfaceOne(87),
        OnBoardInterfaceTwo(88),
        OnBoardInterfaceThree(89),
        OnBoardInterfaceFour(90),
        CUSTOM100(100),
        CUSTOM101(101),
        CUSTOM102(102),
        CUSTOM103(103),
        CUSTOM104(104),
        CUSTOM105(105),
        CUSTOM106(106),
        CUSTOM107(107),
        CUSTOM108(108),
        CUSTOM109(109),
        OTHER(110),
        AutoHighLight(CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256),
        ClearTipsForConsume(198),
        DownVisionLight(199),
        VisionSwitch(200),
        NOT_DEFINED(255);
        
        private int data;

        private DJICustomType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJICustomType find(int b) {
            DJICustomType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }

        public static DJICustomType find(String functionName) {
            DJICustomType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i].toString().equals(functionName)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum ProCustomButton {
        C1,
        C2,
        C3,
        C4,
        BA,
        BB,
        BC,
        BD,
        BE,
        BF,
        BG,
        BH,
        SET,
        SHUT,
        APER,
        ISO,
        Menu,
        TD,
        LW,
        RW,
        LD,
        RD;

        public static int findIndex(ProCustomButton btn) {
            ProCustomButton[] list = values();
            for (int i = 0; i < list.length; i++) {
                if (list[i] == btn) {
                    return i;
                }
            }
            return -1;
        }

        public static ProCustomButton find(String str) {
            ProCustomButton[] list = values();
            for (int i = 0; i < list.length; i++) {
                if (list[i].toString().equals(str)) {
                    return list[i];
                }
            }
            return null;
        }
    }
}
