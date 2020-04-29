package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.forbid.DJIFlightLimitAreaModel;
import dji.midware.data.forbid.NewNfzDesc;
import dji.midware.data.manager.P3.DataBase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bouncycastle.asn1.eac.CertificateBody;

@Keep
@EXClassNullAway
public class DataFlycGetPushForbidStatus extends DataBase {
    private static final int DYNAMIC_DATA_START_INDEX = 7;
    private static DataFlycGetPushForbidStatus instance = null;
    private boolean isDataRealChanged = false;
    private ArrayList<DJIFlightLimitAreaModel> limitAreaModels = new ArrayList<>();
    private int mCurNewStartIndex = 0;
    private int mCurVersion = 0;
    private List<NewNfzDesc> mNewNfzDescs = new ArrayList();

    public static synchronized DataFlycGetPushForbidStatus getInstance() {
        DataFlycGetPushForbidStatus dataFlycGetPushForbidStatus;
        synchronized (DataFlycGetPushForbidStatus.class) {
            if (instance == null) {
                instance = new DataFlycGetPushForbidStatus();
            }
            dataFlycGetPushForbidStatus = instance;
        }
        return dataFlycGetPushForbidStatus;
    }

    private DataFlycGetPushForbidStatus() {
        this.isNeedPushLosed = true;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        this.isDataRealChanged = !Arrays.equals(this._recData, data);
        return true;
    }

    public void clear() {
        this.limitAreaModels.clear();
        super.clear();
    }

    public DJIFlightLimitAreaState getFlightLimitAreaState() {
        return DJIFlightLimitAreaState.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public DJIFlightLimitActionEvent getDJIFlightLimitActionEvent() {
        return DJIFlightLimitActionEvent.find(((Integer) get(1, 1, Integer.class)).intValue());
    }

    public int getVersion() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public boolean isSupportDetectionV3() {
        return getVersion() > 0;
    }

    public int getlimitSpaceNum() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public boolean has1860Db() {
        return (((Integer) get(3, 1, Integer.class)).intValue() & 128) == 128;
    }

    public int getLandingCountdown() {
        return ((Integer) get(3, 1, Integer.class)).intValue() & CertificateBody.profileType;
    }

    public boolean isSupportLicenseUnlock() {
        return (((Integer) get(4, 1, Integer.class)).intValue() & 32) == 32;
    }

    public int getLicenseUnlockVer() {
        return (((Integer) get(4, 1, Integer.class)).intValue() >> 6) & 3;
    }

    public GohomeFrbAreaState getGohomeFrbAreaState() {
        int value = (((Integer) get(4, 1, Integer.class)).intValue() >> 3) & 3;
        if (value == 2) {
            value = 1;
        }
        return GohomeFrbAreaState.find(value);
    }

    public boolean isStuckMovingSlowly() {
        return (((Integer) get(4, 1, Integer.class)).intValue() & 4) == 4;
    }

    public boolean isStuckStopMoving() {
        return (((Integer) get(4, 1, Integer.class)).intValue() & 2) == 2;
    }

    public int getLimitMaxHeight() {
        return ((Integer) get(5, 2, Integer.class)).intValue();
    }

    public NewFlyfrbState getNewState() {
        return NewFlyfrbState.find(((Integer) get(this.mCurNewStartIndex + 7, 1, Integer.class)).intValue());
    }

    public int getNfzNum() {
        return ((Integer) get(this.mCurNewStartIndex + 7 + 1, 1, Integer.class)).intValue();
    }

    private void generateNewStartIndex() {
        this.mCurVersion = getVersion();
        if (this.mCurVersion >= 1) {
            this.mCurNewStartIndex = getlimitSpaceNum() * 17;
        }
    }

    public List<NewNfzDesc> getNewNfzDescs() {
        return new ArrayList(this.mNewNfzDescs);
    }

    public ArrayList<DJIFlightLimitAreaModel> getFlightLimitAreaModels() {
        return this.limitAreaModels;
    }

    /* access modifiers changed from: protected */
    public void post() {
        generateNewStartIndex();
        extractLimitArea();
        extractNewNfzDesc();
        super.post();
    }

    private void extractNewNfzDesc() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        if (this.isDataRealChanged) {
            this.mNewNfzDescs.clear();
            int num = getNfzNum();
            int dataIndex = this.mCurNewStartIndex + 7 + 2;
            for (int i = 0; i < num; i++) {
                NewNfzDesc tmpDesc = new NewNfzDesc();
                tmpDesc.mAreaId = ((Integer) get(dataIndex, 4, Integer.class)).intValue();
                int dataIndex2 = dataIndex + 4;
                tmpDesc.mSubId = ((Integer) get(dataIndex2, 1, Integer.class)).intValue();
                int dataIndex3 = dataIndex2 + 1;
                int tmpData = ((Integer) get(dataIndex3, 1, Integer.class)).intValue();
                int dataIndex4 = dataIndex3 + 1;
                if ((tmpData & 1) == 1) {
                    z = true;
                } else {
                    z = false;
                }
                tmpDesc.isInAvailableLicense = z;
                if (((tmpData >> 1) & 1) == 1) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                tmpDesc.isDrone = z2;
                tmpDesc.mPosState = NewNfzDesc.NfzPositionState.find((tmpData >> 2) & 3);
                if (((tmpData >> 4) & 1) == 1) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                tmpDesc.mHasElevation = z3;
                if (((tmpData >> 5) & 1) == 1) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                tmpDesc.isAuthZone = z4;
                tmpDesc.mHeight = ((Integer) get(dataIndex4, 2, Integer.class)).intValue();
                int dataIndex5 = dataIndex4 + 2;
                tmpDesc.mDistance = ((Float) get(dataIndex5, 4, Float.class)).floatValue();
                dataIndex = dataIndex5 + 4;
                this.mNewNfzDescs.add(tmpDesc);
            }
        }
    }

    private void extractLimitArea() {
        if (this.isDataRealChanged) {
            this.limitAreaModels.clear();
            int size = getlimitSpaceNum();
            int dataIndex = 7;
            for (int i = 0; i < size; i++) {
                DJIFlightLimitAreaModel limitAreaModel = new DJIFlightLimitAreaModel();
                limitAreaModel.latitude = ((Integer) get(dataIndex, 4, Integer.class)).intValue();
                int dataIndex2 = dataIndex + 4;
                limitAreaModel.longitude = ((Integer) get(dataIndex2, 4, Integer.class)).intValue();
                int dataIndex3 = dataIndex2 + 4;
                limitAreaModel.innerRadius = ((Integer) get(dataIndex3, 2, Integer.class)).intValue();
                int dataIndex4 = dataIndex3 + 2;
                limitAreaModel.outerRadius = ((Integer) get(dataIndex4, 2, Integer.class)).intValue();
                int dataIndex5 = dataIndex4 + 2;
                limitAreaModel.type = ((Integer) get(dataIndex5, 1, Integer.class)).intValue();
                dataIndex = dataIndex5 + 1;
                if (limitAreaModel.type == 255 || this.mCurVersion > 1) {
                    limitAreaModel.area_id = ((Integer) get(dataIndex, 4, Integer.class)).intValue();
                    dataIndex += 4;
                }
                this.limitAreaModels.add(limitAreaModel);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum NewFlyfrbState {
        OUTSIDE_LIMIT(0),
        LOCATION_UNKNOWN(1),
        SEEM_IN_LIMIT(2),
        PHONE_IN_LIMIT(3),
        UAV_IN_LIMIT(4),
        SEEM_IN_LIMIT_HEIGHT(5),
        PHONE_IN_LIMIT_HEIGHT(6),
        UAV_IN_LIMIT_HEIGHT(7),
        IN_WHITE_AREA(8),
        OTHER(100);
        
        private int data;

        private NewFlyfrbState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static NewFlyfrbState find(int b) {
            NewFlyfrbState result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum DJIFlightLimitAreaState {
        None(0),
        NearLimit(1),
        InHalfLimit(2),
        InSlowDownArea(3),
        InnerLimit(4),
        InnerUnLimit(5),
        OTHER(100);
        
        private int data;

        private DJIFlightLimitAreaState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJIFlightLimitAreaState find(int b) {
            DJIFlightLimitAreaState result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum DJIFlightLimitActionEvent {
        None(0),
        ExitLanding(1),
        Collision(2),
        StartLanding(3),
        StopMotor(4),
        OTHER(100);
        
        private int data;

        private DJIFlightLimitActionEvent(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJIFlightLimitActionEvent find(int b) {
            DJIFlightLimitActionEvent result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum GohomeFrbAreaState {
        NORMAL(0),
        TANGENT_AREA(1),
        CROSS_AREA(3),
        OTHER(255);
        
        private int data;

        private GohomeFrbAreaState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static GohomeFrbAreaState find(int b) {
            GohomeFrbAreaState result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
