package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataRcMultiPairing extends DataBase implements DJIDataSyncListener {
    private PairAction action;
    private PairMode mode;
    private int param;
    private PairTarget target;

    public DataRcMultiPairing setAction(PairAction action2) {
        this.action = action2;
        return this;
    }

    public DataRcMultiPairing setMode(PairMode mode2) {
        this.mode = mode2;
        return this;
    }

    public DataRcMultiPairing setTarget(PairTarget target2) {
        this.target = target2;
        return this;
    }

    public DataRcMultiPairing eraseAllPairingInformation() {
        this.param = 255;
        return this;
    }

    /* Debug info: failed to restart local var, previous not found, register: 1 */
    public DataRcMultiPairing erasePairingInformation(int number) {
        if (number > 9) {
            return eraseAllPairingInformation();
        }
        this.param = (number * 16) - 1;
        return this;
    }

    public PairState getPairState() {
        return PairState.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public SDRPairState getSdrPairState() {
        return SDRPairState.find(((Integer) get(1, 1, Integer.class)).intValue());
    }

    public DataRcMultiPairing setNumber(int number) {
        this.param = number;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.MultiRcPairing.value();
        pack.timeOut = 3000;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = (byte) this.action.value();
        this._sendData[1] = (byte) this.mode.value();
        this._sendData[2] = (byte) this.target.value();
        this._sendData[3] = (byte) this.param;
    }

    public enum PairAction {
        GET_PAIR_STATE(0),
        ENTER_PAIRING(1),
        EXIT_PAIRING(2),
        GET_CP_STATE(3),
        UNKNOWN(255);
        
        private int value;

        private PairAction(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean equals(int value2) {
            return this.value == value2;
        }

        public static PairAction find(int value2) {
            PairAction[] values = values();
            for (PairAction arg : values) {
                if (arg.equals(value2)) {
                    return arg;
                }
            }
            return UNKNOWN;
        }
    }

    public enum PairMode {
        AGRICULTURE(0),
        OTHER(1);
        
        private int value;

        private PairMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean equals(int value2) {
            return this.value == value2;
        }

        public static PairMode find(int value2) {
            PairMode[] values = values();
            for (PairMode arg : values) {
                if (arg.equals(value2)) {
                    return arg;
                }
            }
            return OTHER;
        }
    }

    public enum PairTarget {
        AIRCRAFT(0),
        BS(1),
        GS(4),
        UNKONWN(255);
        
        private int value;

        private PairTarget(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean equals(int value2) {
            return this.value == value2;
        }

        public static PairTarget find(int value2) {
            PairTarget[] values = values();
            for (PairTarget arg : values) {
                if (arg.equals(value2)) {
                    return arg;
                }
            }
            return UNKONWN;
        }
    }

    public enum PairState {
        UNPAIRED(0),
        PAIRING(1),
        PAIRED(2),
        UNKNOWN(255);
        
        private int value;

        private PairState(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean equals(int value2) {
            return this.value == value2;
        }

        public static PairState find(int value2) {
            PairState[] values = values();
            for (PairState arg : values) {
                if (arg.equals(value2)) {
                    return arg;
                }
            }
            return UNKNOWN;
        }
    }

    public enum SDRPairState {
        UNPAIRED(3),
        PAIRED(4),
        PAIRING(5),
        LOST(6),
        AMT(7),
        ERROR(8),
        UNKNOWN(255);
        
        private int value;

        private SDRPairState(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean equals(int value2) {
            return this.value == value2;
        }

        public static SDRPairState find(int value2) {
            SDRPairState[] values = values();
            for (SDRPairState arg : values) {
                if (arg.equals(value2)) {
                    return arg;
                }
            }
            return UNKNOWN;
        }
    }
}
