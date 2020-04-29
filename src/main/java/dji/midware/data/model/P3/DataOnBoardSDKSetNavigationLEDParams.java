package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.model.P3.DataOnBoardSDKSetAccessoryCommonParams;
import dji.midware.util.BytesUtil;

@Keep
public class DataOnBoardSDKSetNavigationLEDParams extends DataOnBoardSDKSetAccessoryCommonParams {
    private int param;

    public DataOnBoardSDKSetNavigationLEDParams() {
        super(DataOnBoardSDKSetAccessoryCommonParams.AccessoryType.NAVIGATION_LED);
    }

    public DataOnBoardSDKSetNavigationLEDParams setParam(int param2) {
        this.param = param2;
        return this;
    }

    public DataOnBoardSDKSetNavigationLEDParams setParamType(int type) {
        this.paramType = type;
        return this;
    }

    /* access modifiers changed from: protected */
    public byte[] getParamData() {
        return new byte[]{BytesUtil.getByte(this.param)};
    }

    @Keep
    public enum ParamType {
        POWER(1),
        ON_OFF(2),
        FLICKER_FREQUENCY(3);
        
        private final int mValue;

        private ParamType(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }
    }
}
