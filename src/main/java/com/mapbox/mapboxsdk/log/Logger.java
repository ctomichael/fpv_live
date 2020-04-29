package com.mapbox.mapboxsdk.log;

import android.support.annotation.Keep;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Keep
public final class Logger {
    public static final int DEBUG = 3;
    private static final LoggerDefinition DEFAULT = new LoggerDefinition() {
        /* class com.mapbox.mapboxsdk.log.Logger.AnonymousClass1 */

        public void v(String tag, String msg) {
            Log.v(tag, msg);
        }

        public void v(String tag, String msg, Throwable tr) {
            Log.v(tag, msg, tr);
        }

        public void d(String tag, String msg) {
            Log.d(tag, msg);
        }

        public void d(String tag, String msg, Throwable tr) {
            Log.d(tag, msg, tr);
        }

        public void i(String tag, String msg) {
            Log.i(tag, msg);
        }

        public void i(String tag, String msg, Throwable tr) {
            Log.i(tag, msg, tr);
        }

        public void w(String tag, String msg) {
            Log.w(tag, msg);
        }

        public void w(String tag, String msg, Throwable tr) {
            Log.w(tag, msg, tr);
        }

        public void e(String tag, String msg) {
            Log.e(tag, msg);
        }

        public void e(String tag, String msg, Throwable tr) {
            Log.e(tag, msg, tr);
        }
    };
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int NONE = 99;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static int logLevel;
    private static volatile LoggerDefinition logger = DEFAULT;

    @Retention(RetentionPolicy.SOURCE)
    public @interface LogLevel {
    }

    public static void setVerbosity(int logLevel2) {
        logLevel = logLevel2;
    }

    public static void setLoggerDefinition(LoggerDefinition loggerDefinition) {
        logger = loggerDefinition;
    }

    public static void v(String tag, String msg) {
        if (logLevel <= 2) {
            logger.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (logLevel <= 2) {
            logger.v(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (logLevel <= 3) {
            logger.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (logLevel <= 3) {
            logger.d(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (logLevel <= 4) {
            logger.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (logLevel <= 4) {
            logger.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (logLevel <= 5) {
            logger.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (logLevel <= 5) {
            logger.w(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (logLevel <= 6) {
            logger.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (logLevel <= 6) {
            logger.e(tag, msg, tr);
        }
    }

    public static void log(int severity, String tag, String message) {
        switch (severity) {
            case 2:
                v(tag, message);
                return;
            case 3:
                d(tag, message);
                return;
            case 4:
                i(tag, message);
                return;
            case 5:
                w(tag, message);
                return;
            case 6:
                e(tag, message);
                return;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
