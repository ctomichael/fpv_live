package dji.midware.data.manager.Dpad;

import android.os.IBinder;
import android.os.Parcel;
import com.secneo.ProtectMeVmpMethod;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.util.ContextUtil;
import dji.midware.util.DjiSharedPreferencesManager;

@EXClassNullAway
public class DpadBoardManager {
    private final String KEY_FAN_MANUAL = "video_cache_fan";
    public final boolean OPEN = true;
    private final String ServiceName = "android.dji.server";
    private String TAG = "DpadBoardManager";
    private IBinder mBinder;
    private int mCheckCnt = 0;
    private final boolean mEnableCompassDenoise;
    private final boolean mEnableRecordDenoise = false;
    private final boolean mIsExBoard;
    private final int sBestSpeed = DJIVideoDecoder.connectLosedelay;

    private interface FanState {
        public static final int AUTO = 1;
        public static final int MANUAL_0 = 2;
        public static final int Manual_2k = 3;
    }

    public DpadBoardManager() {
        if (!DpadProductManager.getInstance().isCrystalSky()) {
            throw new RuntimeException("only for isCrystalSky!!");
        }
        boolean isExBoard = isExBoard();
        this.mIsExBoard = isExBoard;
        this.mEnableCompassDenoise = isExBoard;
    }

    public void onCreate() {
        if (this.mEnableCompassDenoise) {
            setFanState(3);
        }
    }

    public void onDestroy() {
        if (this.mEnableCompassDenoise) {
            setFanState(1);
        }
    }

    public void handleRecordStart() {
    }

    public void handleOverHeat() {
    }

    public void handleRecordStop() {
    }

    private void setFanState(int fanState) {
        if (fanState == 1) {
            setFanStateInner(-1);
        } else if (fanState == 2) {
            setFanStateInner(0, 0);
        } else if (fanState == 3) {
            setFanStateInner(1, DJIVideoDecoder.connectLosedelay);
        }
    }

    @ProtectMeVmpMethod
    private void setFanStateInner(int state) {
        if (checkBinder()) {
            Parcel data = Parcel.obtain();
            data.writeInt(state);
            try {
                this.mBinder.transact(DJIVideoDecoder.connectLosedelay, data, null, 0);
                LOG("setFanStateInner,state=" + state);
            } catch (Exception ex) {
                LOG("ex=" + ex);
            }
        }
    }

    @ProtectMeVmpMethod
    private void setFanStateInner(int state, int speed) {
        if (checkBinder()) {
            Parcel data = Parcel.obtain();
            data.writeInt(state);
            data.writeInt(speed);
            try {
                this.mBinder.transact(DJIVideoDecoder.connectLosedelay, data, null, 0);
                LOG("setFanStateInner,state=" + state);
            } catch (Exception ex) {
                LOG("ex=" + ex);
            }
        }
    }

    public int getFanState() {
        if (checkBinder()) {
            Parcel reply = Parcel.obtain();
            try {
                this.mBinder.transact(2001, Parcel.obtain(), reply, 0);
                int state = reply.readInt();
                LOG("getFanState,state=" + state);
                return state;
            } catch (Exception ex) {
                LOG("ex=" + ex);
            }
        }
        return -1;
    }

    private void reset() {
        if (isExBoard()) {
            setFanState(3);
        } else {
            setFanState(1);
        }
    }

    @ProtectMeVmpMethod
    private boolean isExBoard() {
        return DJISysPropManager.getBoardVer() <= 1 && DpadProductManager.getInstance().isCrystalSkyB();
    }

    private boolean checkBinder() {
        if (this.mBinder == null) {
            this.mBinder = ContextUtil.getService("android.dji.server");
            this.mCheckCnt++;
            LOG(" checkBinder, mCheckCnt=" + this.mCheckCnt);
        }
        if (this.mBinder != null) {
            return true;
        }
        LOG(" null == mBinder = getService(android.dji.server);mCheckCnt=" + this.mCheckCnt);
        return false;
    }

    private static void LOG(String log) {
    }

    public void setFanManual(boolean isClose) {
        DjiSharedPreferencesManager.putBoolean(ServiceManager.getContext(), "video_cache_fan", isClose);
    }

    public boolean getFanManual() {
        return DjiSharedPreferencesManager.getBoolean(ServiceManager.getContext(), "video_cache_fan", false);
    }
}
