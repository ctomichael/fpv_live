package dji.log;

import android.support.annotation.Keep;
import android.util.Log;
import com.dji.megatronking.stringfog.lib.annotation.DJIStringFog;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.BuildConfig;

@Keep
@EXClassNullAway
public class GlobalConfig {
    public static final String BRIDGE_HOST_IP = "";
    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final boolean FLYSAFE = false;
    public static final boolean IVT_RUNNING = false;
    public static final boolean LOG_DEBUGGABLE = (BuildConfig.DEBUG || Log.isLoggable(TAG, 3));
    public static final boolean MOCK = false;
    @DJIStringFog
    public static final String TAG = "KDSIJD";
    public static final boolean VIDEO_LOG_DEBUGGABLE = Log.isLoggable(VIDEO_TAG, 3);
    @DJIStringFog
    public static final String VIDEO_TAG = "GOLKDSIJD";
}
