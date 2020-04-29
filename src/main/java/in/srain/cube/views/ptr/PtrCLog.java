package in.srain.cube.views.ptr;

import android.util.Log;
import java.util.Locale;

class PtrCLog {
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_WARNING = 3;
    private static int sLevel = 0;

    PtrCLog() {
    }

    public static void setLogLevel(int level) {
        sLevel = level;
    }

    public static void v(String tag, String msg) {
        if (sLevel <= 0) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable throwable) {
        if (sLevel <= 0) {
            Log.v(tag, msg, throwable);
        }
    }

    public static void v(String tag, String msg, Object... args) {
        if (sLevel <= 0) {
            if (args.length > 0) {
                msg = String.format(Locale.US, msg, args);
            }
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sLevel <= 1) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Object... args) {
        if (sLevel <= 1) {
            if (args.length > 0) {
                msg = String.format(Locale.US, msg, args);
            }
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (sLevel <= 1) {
            Log.d(tag, msg, throwable);
        }
    }

    public static void i(String tag, String msg) {
        if (sLevel <= 2) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Object... args) {
        if (sLevel <= 2) {
            if (args.length > 0) {
                msg = String.format(Locale.US, msg, args);
            }
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable throwable) {
        if (sLevel <= 2) {
            Log.i(tag, msg, throwable);
        }
    }

    public static void w(String tag, String msg) {
        if (sLevel <= 3) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Object... args) {
        if (sLevel <= 3) {
            if (args.length > 0) {
                msg = String.format(Locale.US, msg, args);
            }
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (sLevel <= 3) {
            Log.w(tag, msg, throwable);
        }
    }

    public static void e(String tag, String msg) {
        if (sLevel <= 4) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Object... args) {
        if (sLevel <= 4) {
            if (args.length > 0) {
                msg = String.format(Locale.US, msg, args);
            }
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (sLevel <= 4) {
            Log.e(tag, msg, throwable);
        }
    }

    public static void f(String tag, String msg) {
        if (sLevel <= 5) {
            Log.wtf(tag, msg);
        }
    }

    public static void f(String tag, String msg, Object... args) {
        if (sLevel <= 5) {
            if (args.length > 0) {
                msg = String.format(Locale.US, msg, args);
            }
            Log.wtf(tag, msg);
        }
    }

    public static void f(String tag, String msg, Throwable throwable) {
        if (sLevel <= 5) {
            Log.wtf(tag, msg, throwable);
        }
    }
}
