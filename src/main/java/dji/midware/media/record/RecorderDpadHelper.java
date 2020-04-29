package dji.midware.media.record;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.util.ContextUtil;
import dji.midware.util.DjiSharedPreferencesManager;

@EXClassNullAway
@Deprecated
public class RecorderDpadHelper {
    private static final String KEY_FAN_MANUAL = "video_cache_fan";
    public static final boolean OPEN = false;
    private static final String ServiceName = "android.dji.server";
    private static String TAG = "RecorderDpadHelper";
    private static IBinder mBinder;
    private static int mCheckCnt = 0;
    @Deprecated
    private static BroadcastReceiver mTmp = new BroadcastReceiver() {
        /* class dji.midware.media.record.RecorderDpadHelper.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra("temperature", 0) > 40) {
            }
        }
    };

    public static void setFanManual(boolean isClose) {
        DjiSharedPreferencesManager.putBoolean(ServiceManager.getContext(), KEY_FAN_MANUAL, isClose);
    }

    public static boolean getFanManual() {
        return DjiSharedPreferencesManager.getBoolean(ServiceManager.getContext(), KEY_FAN_MANUAL, false);
    }

    public static void start() {
        if (!DpadProductManager.getInstance().isCrystalSky() || getFanManual()) {
        }
    }

    public static void stop() {
        if (DpadProductManager.getInstance().isCrystalSky()) {
        }
    }

    private static boolean checkBinder() {
        if (mBinder == null) {
            mBinder = ContextUtil.getService(ServiceName);
            mCheckCnt++;
            LOG(" checkBinder, mCheckCnt=" + mCheckCnt);
        }
        if (mBinder != null) {
            return true;
        }
        LOG(" null == mBinder = getService(android.dji.server);mCheckCnt=" + mCheckCnt);
        return false;
    }

    public static int getFanState() {
        if (checkBinder()) {
            Parcel reply = Parcel.obtain();
            try {
                mBinder.transact(2001, Parcel.obtain(), reply, 0);
                int state = reply.readInt();
                LOG("getFanState,state=" + state);
                return state;
            } catch (Exception ex) {
                Log.e(TAG, "getFanState,ex=" + ex);
            }
        }
        return -1;
    }

    private static void setFanState(int state) {
        if (checkBinder()) {
            Parcel data = Parcel.obtain();
            data.writeInt(state);
            try {
                mBinder.transact(DJIVideoDecoder.connectLosedelay, data, null, 0);
                LOG("setFanStateInner,state=" + state);
            } catch (Exception ex) {
                LOG("ex=" + ex);
            }
        }
    }

    public static void LOG(String log) {
        Log.e(TAG, log);
    }

    private static void registerTemp() {
        new IntentFilter("dji.temperature.alarm");
    }

    private static void unregisterTemp() {
    }

    public static void postErrorModel(int titleId, int msgId, int type) {
        try {
            Class c = Class.forName("dji.pilot.fpv.model.ErrorModel");
            c.getMethod("postErrorModel", Integer.class, Integer.class, Integer.class).invoke(c, Integer.valueOf(titleId), Integer.valueOf(msgId), Integer.valueOf(type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
