package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.Iterator;

@Keep
@EXClassNullAway
public class DataEyeSetQuickMovieParams extends DataBase implements DJIDataSyncListener {
    private static final int REASON_STRUCTURE_SIZE = 2;
    private static DataEyeSetQuickMovieParams instance;
    private ArrayList<ActionParam> actionParams;

    public interface FailedReason {
        public static final int FAILED = 1;
        public static final int NONE = 0;
        public static final int UNKNOWN = 255;
    }

    public interface ActionType {
        public static final int CIRCLE = 1;
        public static final int COMET = 6;
        public static final int DIAGONAL = 2;
        public static final int DOLLY_ZOOM = 10;
        public static final int NONE = 0;
        public static final int PLANET = 8;
        public static final int ROCKY = 4;
        public static final int SPIRAL = 3;
    }

    @Keep
    public enum ActionParamIndex {
        ACTION_TYPE(0, 1),
        IS_START(1, 1),
        VELOCITY_X(2, 4),
        VELOCITY_Y(3, 4),
        VELOCITY_Z(4, 4),
        DISTANCE(5, 4),
        TIME(6, 4),
        PROGRESS(7, 1),
        END_OF_PARAMS(255, 1);
        
        private int size;
        private int value;

        private ActionParamIndex(int value2, int size2) {
            this.value = value2;
            this.size = size2;
        }

        public int getValue() {
            return this.value;
        }

        public int getSize() {
            return this.size;
        }

        public static int getTotalPackageSize() {
            int result = 0;
            for (ActionParamIndex index : values()) {
                result = result + 1 + 1 + index.getSize();
            }
            return result;
        }

        public static ActionParamIndex find(int value2) {
            ActionParamIndex[] values = values();
            for (ActionParamIndex candidate : values) {
                if (candidate.getValue() == value2) {
                    return candidate;
                }
            }
            return END_OF_PARAMS;
        }
    }

    @Keep
    public static class FailedReasonStruct {
        public int index;
        public int reason;

        public FailedReasonStruct() {
        }

        public FailedReasonStruct(int index2, int reason2) {
            this.index = index2;
            this.reason = reason2;
        }
    }

    @Keep
    public static class ActionParam {
        public Number data;
        public ActionParamIndex index;

        public ActionParam(ActionParamIndex index2, Number data2) {
            this.index = index2;
            this.data = data2;
        }

        public ActionParam(ActionParamIndex endFlag) {
            this.index = ActionParamIndex.END_OF_PARAMS;
            this.data = 1;
        }

        public String toString() {
            return "ActionParam{index=" + this.index + ", data=" + this.data + '}';
        }
    }

    public static synchronized DataEyeSetQuickMovieParams getInstance() {
        DataEyeSetQuickMovieParams dataEyeSetQuickMovieParams;
        synchronized (DataEyeSetQuickMovieParams.class) {
            if (instance == null) {
                instance = new DataEyeSetQuickMovieParams();
            }
            dataEyeSetQuickMovieParams = instance;
        }
        return dataEyeSetQuickMovieParams;
    }

    public DataEyeSetQuickMovieParams setActionParams(ArrayList<ActionParam> actionParams2) {
        this.actionParams = actionParams2;
        return this;
    }

    public ArrayList<FailedReasonStruct> getFailedReasons() {
        ArrayList<FailedReasonStruct> reasons = null;
        int failedCount = ((Integer) get(0, 1, Integer.class)).intValue();
        if (!(failedCount == 0 || this._recData == null || this._recData.length - 1 != failedCount * 2)) {
            reasons = new ArrayList<>();
            for (int i = 0; i < failedCount; i++) {
                reasons.add(new FailedReasonStruct(((Integer) get((i * 2) + 1, 1, Integer.class)).intValue(), ((Integer) get((i * 2) + 2, 1, Integer.class)).intValue()));
            }
        }
        return reasons;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int totalSize = 0;
        Iterator<ActionParam> it2 = this.actionParams.iterator();
        while (it2.hasNext()) {
            totalSize += it2.next().index.getSize() + 2;
        }
        this._sendData = new byte[(totalSize + 1)];
        this._sendData[0] = (byte) this.actionParams.size();
        if (this.actionParams != null) {
            int offset = 1;
            Iterator<ActionParam> it3 = this.actionParams.iterator();
            while (it3.hasNext()) {
                ActionParam actionParam = it3.next();
                int offset2 = offset + 1;
                this._sendData[offset] = (byte) actionParam.index.getValue();
                int offset3 = offset2 + 1;
                this._sendData[offset2] = (byte) actionParam.index.getSize();
                if (actionParam.index.getSize() == 4) {
                    System.arraycopy(BytesUtil.getBytes(actionParam.data.floatValue()), 0, this._sendData, offset3, actionParam.index.getSize());
                } else {
                    System.arraycopy(BytesUtil.getBytes(actionParam.data.intValue()), 0, this._sendData, offset3, actionParam.index.getSize());
                }
                offset = offset3 + actionParam.index.getSize();
            }
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.SetQuickMovieParams.value();
        start(pack, callBack);
    }
}
