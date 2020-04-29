package dji.log;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import dji.midware.usb.P3.AoaReportHelper;

public class DJILog {
    private static final int DEFAULT_ALL_STACK = 100;
    private static final int DEFAULT_START_STACK = 2;
    private static int intervalIndex = 999;
    private static SparseArray<Long> startTimes = new SparseArray<>(5);

    public interface Callback {
        void doMethod();
    }

    private static DJILogHelper getLogHelper() {
        return DJILogHelper.getInstance();
    }

    public static void init(Context context) {
        getLogHelper().init(context);
    }

    public static void init(Context context, DJILogFileConfig fileConfig, DJILogConsoleConfig consoleConfig) {
        getLogHelper().init(context, fileConfig, consoleConfig);
    }

    public static Controller controller() {
        return getLogHelper().controller();
    }

    public static void setController(Controller controller) {
        getLogHelper().setController(controller);
    }

    public static Controller newController() {
        return getLogHelper().newController();
    }

    public static void v(String msg, Object... args) {
        v(DJILogUtils.obtainStackElementTag(), msg, args);
    }

    public static void v(String tag, String msg, Object... args) {
        getLogHelper().v(tag, msg, args);
    }

    public static void v(String tag, String msg, Throwable throwable, Object... args) {
        getLogHelper().v(tag, msg, throwable, args);
    }

    public static void d(String msg, Object... args) {
        d(DJILogUtils.obtainStackElementTag(), msg, args);
    }

    public static void d(String tag, String msg, Object... args) {
        getLogHelper().d(tag, msg, args);
    }

    public static void d(String tag, String msg, Throwable throwable, Object... args) {
        getLogHelper().d(tag, msg, throwable, args);
    }

    public static void i(String msg, Object... args) {
        i(DJILogUtils.obtainStackElementTag(), msg, args);
    }

    public static void i(String tag, String msg, Object... args) {
        getLogHelper().i(tag, msg, args);
    }

    public static void i(String tag, String msg, Throwable throwable, Object... args) {
        getLogHelper().i(tag, msg, throwable, args);
    }

    public static void w(String msg, Object... args) {
        w(DJILogUtils.obtainStackElementTag(), msg, args);
    }

    public static void w(String tag, String msg, Object... args) {
        getLogHelper().w(tag, msg, args);
    }

    public static void w(String tag, String msg, Throwable throwable, Object... args) {
        getLogHelper().w(tag, msg, throwable, args);
    }

    public static void e(String msg, Object... args) {
        e(DJILogUtils.obtainStackElementTag(), msg, args);
    }

    public static void e(String tag, String msg, Object... args) {
        getLogHelper().e(tag, msg, args);
    }

    public static void e(String tag, String msg, Throwable throwable, Object... args) {
        getLogHelper().e(tag, msg, throwable, args);
    }

    private static long[] getPriorityRatio() {
        return getLogHelper().getPriorityRatio();
    }

    public static void saveLog(String msg) {
        getLogHelper().saveLog(msg);
    }

    public static void saveLog(String msg, String extraDir) {
        getLogHelper().saveLog(msg, extraDir);
    }

    private static void saveLog(String tag, String msg, String extraDir) {
        getLogHelper().saveLog(tag, msg, extraDir);
    }

    private static void saveLog(int level, String tag, String msg, String extraDir) {
        getLogHelper().saveLog(level, tag, msg, extraDir);
    }

    @Deprecated
    public static void saveExtraLog(String msg, String path) {
        getLogHelper().saveExtraLog(msg, path);
    }

    private static void saveExtraLog(String tag, String msg, String path) {
        getLogHelper().saveExtraLog(tag, msg, path);
    }

    public static void showView(String msg) {
        getLogHelper().showView(msg);
    }

    public static void flush() {
        getLogHelper().flush();
    }

    public static void addInterceptor(Interceptor interceptor) {
        getLogHelper().addInterceptors(interceptor);
    }

    public static void removeInterceptor(Interceptor interceptor) {
        getLogHelper().removeInterceptors(interceptor);
    }

    public static String getRootDirectory() {
        return getLogHelper().getRootDirectory();
    }

    public static void object(Object object) {
        object(DJILogUtils.obtainStackElementTag(), object);
    }

    public static void object(String tag, Object object) {
        object(3, tag, object);
    }

    public static void object(int priority, String tag, Object object) {
        getLogHelper().println(priority, tag, DJILogUtils.formatObject(object));
    }

    public static void json(String json) {
        json(DJILogUtils.obtainStackElementTag(), json);
    }

    public static void json(String tag, String json) {
        json(3, tag, json);
    }

    public static void json(int priority, String tag, String json) {
        getLogHelper().println(priority, tag, DJILogUtils.formatJson(json));
    }

    public static void xml(String xml) {
        xml(DJILogUtils.obtainStackElementTag(), xml);
    }

    public static void xml(String tag, String xml) {
        xml(3, tag, xml);
    }

    public static void xml(int priority, String tag, String xml) {
        getLogHelper().println(priority, tag, DJILogUtils.formatXml(xml));
    }

    public static void logWriteV(String tag, String msg, Object... args) {
        logWriteV(tag, msg, null, args);
    }

    public static void logWriteV(String tag, String msg, String extraDir, Object... args) {
        v(tag, msg, args);
        saveLog(2, tag, DJILogUtils.formatMessage(msg, args), extraDir);
    }

    public static void logWriteD(String tag, String msg, Object... args) {
        logWriteD(tag, msg, null, args);
    }

    public static void logWriteD(String tag, String msg, String extraDir, Object... args) {
        d(tag, msg, args);
        saveLog(3, tag, DJILogUtils.formatMessage(msg, args), extraDir);
    }

    public static void logWriteW(String tag, String msg, Object... args) {
        logWriteW(tag, msg, null, args);
    }

    public static void logWriteW(String tag, String msg, String extraDir, Object... args) {
        w(tag, msg, args);
        saveLog(5, tag, DJILogUtils.formatMessage(msg, args), extraDir);
    }

    public static void logWriteI(String tag, String msg, Object... args) {
        logWriteI(tag, msg, null, args);
    }

    public static void logWriteI(String tag, String msg, String extraDir, Object... args) {
        i(tag, msg, args);
        saveLog(4, tag, DJILogUtils.formatMessage(msg, args), extraDir);
    }

    public static void logWriteE(String tag, String msg, Object... args) {
        logWriteE(tag, msg, null, args);
    }

    public static void logWriteE(String tag, String msg, String extraDir, Object... args) {
        e(tag, msg, args);
        saveLog(6, tag, DJILogUtils.formatMessage(msg, args), extraDir);
    }

    public static void logStack() {
        logStack(100);
    }

    public static void logStack(int count) {
        logStack(DJILogUtils.obtainStackElementTag(), count);
    }

    public static void logStack(String tag, int count) {
        d(tag, DJILogUtils.getCurrentStack(2, count), new Object[0]);
    }

    public static void saveStack(String dir) {
        saveStack(dir, 100);
    }

    public static void saveStack(String dir, int count) {
        saveStack("DJIGo", dir, count);
    }

    private static void saveStack(String tag, String dir, int count) {
        saveLog(tag, DJILogUtils.getCurrentStack(2, count), dir);
    }

    public static void logSaveStack(String tag, Thread thread, String dir) {
        d(tag, DJILogUtils.getThreadStack(thread), new Object[0]);
        saveLog(tag, DJILogUtils.getThreadStack(thread), dir);
    }

    public static void countTime(String tag, Callback callback) {
        long start = System.currentTimeMillis();
        callback.doMethod();
        e(tag, "execution time=" + (((float) (System.currentTimeMillis() - start)) / 1000.0f) + "s", new Object[0]);
        d(tag, DJILogUtils.getCurrentStack(2, 100), new Object[0]);
    }

    public static void startTime(int index) {
        startTimes.put(index, Long.valueOf(System.currentTimeMillis()));
    }

    public static void endTimes(int index) {
        Log.d("CostTime", "CostTime index : " + index + ", time : " + (System.currentTimeMillis() - startTimes.get(index).longValue()));
    }

    public static void endTimesFilter(int index) {
        long delta = System.currentTimeMillis() - startTimes.get(index).longValue();
        if (delta > 100) {
            Log.d("CostTime", "CostTime index : " + index + ", time : " + delta);
            Log.d("CostTime", "CostTime index : " + getCurrentStack());
        }
    }

    public static void interval() {
        if (startTimes.get(intervalIndex) == null) {
            startTime(intervalIndex);
            return;
        }
        endTimesFilter(intervalIndex);
        startTime(intervalIndex);
    }

    public static void saveAsync(String tag, String log) {
        logWriteI(tag, log, tag, new Object[0]);
    }

    public static void d(String tag, String log, boolean saveFile, boolean showInScreen) {
        DJILogHelper.getInstance().LOGD("DJIGo", tag + " : " + log, saveFile, showInScreen);
    }

    public static void e(String tag, String log, boolean saveFile, boolean showInScreen) {
        DJILogHelper.getInstance().LOGE(tag, log, saveFile, showInScreen);
    }

    public static void i(String tag, String log, boolean saveFile, boolean showInScreen) {
        DJILogHelper.getInstance().LOGI(tag, log, saveFile, showInScreen);
    }

    public static void save(String tag, String log) {
        logWriteI(tag, log, tag, new Object[0]);
    }

    public static void saveConnectDebug(String log) {
        logWriteI(AoaReportHelper.TAG_CONNECT_DEBUG, "(" + System.currentTimeMillis() + ") " + log, AoaReportHelper.TAG_CONNECT_DEBUG, new Object[0]);
    }

    public static String exceptionToString(Throwable e) {
        return DJILogUtils.exceptionToString(e);
    }

    public static String getCurrentStack() {
        return DJILogUtils.getCurrentStack();
    }

    public static String formatObject(Object object) {
        return DJILogUtils.formatObject(object);
    }
}
