package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataAppUIOperation extends DataBase {
    public AppUIOperateSubType mAppUIOperateSubType = AppUIOperateSubType.AppSetHomePoint;
    public DataAppUIOperateSetHome mDataAppUIOperateSetHome = null;

    public DataAppUIOperation(boolean isRegist) {
        super(isRegist);
    }

    public DataAppUIOperation setSubType(AppUIOperateSubType subType) {
        this.mAppUIOperateSubType = subType;
        return this;
    }

    public DataAppUIOperation setHomeInfo(DataAppUIOperateSetHome homeInfo) {
        if (AppUIOperateSubType.AppSetHomePoint == this.mAppUIOperateSubType) {
            this.mDataAppUIOperateSetHome = homeInfo;
        }
        return this;
    }

    public byte[] getRecData() {
        if (this.mAppUIOperateSubType == null) {
            return null;
        }
        byte[] buffer = null;
        switch (this.mAppUIOperateSubType) {
            case AppSetHomePoint:
                if (this.mDataAppUIOperateSetHome == null) {
                    buffer = null;
                    break;
                } else {
                    buffer = this.mDataAppUIOperateSetHome.getRecData();
                    break;
                }
        }
        return buffer;
    }

    public void setRecData(byte[] data) {
        this.mAppUIOperateSubType = AppUIOperateSubType.find(((Integer) get(0, 1, Integer.class)).intValue());
        switch (this.mAppUIOperateSubType) {
            case AppSetHomePoint:
                this.mDataAppUIOperateSetHome = new DataAppUIOperateSetHome(false);
                this.mDataAppUIOperateSetHome.setRecData(data);
                return;
            default:
                return;
        }
    }

    public String toString() {
        if (this.mAppUIOperateSubType == null) {
            return super.toString();
        }
        if (AppUIOperateSubType.AppSetHomePoint != this.mAppUIOperateSubType || this.mDataAppUIOperateSetHome == null) {
            return super.toString();
        }
        return this.mDataAppUIOperateSetHome.toString();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum AppUIOperateSubType {
        AppSetHomePoint(0),
        OTHER(100);
        
        private static AppUIOperateSubType[] _values = null;
        private final int data;

        private AppUIOperateSubType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static AppUIOperateSubType find(int b) {
            AppUIOperateSubType result = AppSetHomePoint;
            if (_values == null) {
                _values = values();
            }
            AppUIOperateSubType[] appUIOperateSubTypeArr = _values;
            for (AppUIOperateSubType tmp : appUIOperateSubTypeArr) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}
