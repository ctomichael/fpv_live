package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataCameraTauExterParams extends DataCameraTauParamer {
    private final ArrayList<ExterParamInfo> mCmdParams = new ArrayList<>(1);
    private int mDataLength = 0;

    public DataCameraTauExterParams() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.EXTER_PARAMS;
    }

    public DataCameraTauExterParams addParam(ExterParamId id, short value) {
        ExterParamInfo pi = new ExterParamInfo(id, value);
        this.mDataLength += pi.dataLength();
        this.mCmdParams.add(pi);
        return this;
    }

    @Keep
    private static final class ExterParamInfo {
        private ExterParamId mParamId;
        private short mValue;

        private ExterParamInfo(ExterParamId id, short value) {
            this.mParamId = ExterParamId.DEFAULT;
            this.mValue = 0;
            this.mParamId = id;
            this.mValue = value;
        }

        /* access modifiers changed from: private */
        public int dataLength() {
            if (ExterParamId.DEFAULT == this.mParamId) {
            }
            return 4;
        }

        /* access modifiers changed from: private */
        public byte[] toBytes() {
            byte[] data = new byte[dataLength()];
            data[0] = (byte) this.mParamId.value();
            if (ExterParamId.DEFAULT == this.mParamId) {
                data[1] = 2;
                System.arraycopy(BytesUtil.getBytes(this.mValue), 0, data, 2, 2);
            } else {
                data[1] = 2;
                System.arraycopy(BytesUtil.getBytes(this.mValue), 0, data, 2, 2);
            }
            return data;
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this.mParams = new byte[this.mDataLength];
        int size = this.mCmdParams.size();
        int offset = 0;
        for (int i = 0; i < size; i++) {
            ExterParamInfo epi = this.mCmdParams.get(i);
            int length = epi.dataLength();
            System.arraycopy(epi.toBytes(), 0, this.mParams, offset, length);
            offset += length;
        }
        super.doPack();
    }

    @Keep
    public enum ExterParamId {
        DEFAULT(0),
        TARGET_EMISSIVITY(1),
        BACKGROUND_TEMP(4),
        ATMOSPHERE_TRANSMISSION(2),
        ATMOSPHERE_TEMP(3),
        WINDOW_TRANSMISSION(5),
        WINDOW_REFLECTION(7),
        WINDOW_TEMP(6),
        WINDOW_REFLECTED_TEMP(8),
        OTHER(99);
        
        private final int _value;

        private ExterParamId(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return value == this._value;
        }

        public static ExterParamId find(int value) {
            ExterParamId[] values = values();
            for (ExterParamId tmp : values) {
                if (tmp._equals(value)) {
                    return tmp;
                }
            }
            return DEFAULT;
        }
    }
}
