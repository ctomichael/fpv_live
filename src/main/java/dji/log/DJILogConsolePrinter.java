package dji.log;

import android.content.Context;
import android.util.Log;
import dji.log.DJILogConsoleConfig;

class DJILogConsolePrinter {
    private DJILogConsoleConfig config;

    DJILogConsolePrinter() {
    }

    public void init(Context context, DJILogConsoleConfig config2) {
        if (config2 == null) {
            config2 = new DJILogConsoleConfig.Builder(context).build();
        }
        this.config = config2;
    }

    private String formatTag(int priority, String tag) {
        if (this.config != null) {
            return this.config.consoleFormat.formatTag(priority, tag);
        }
        return tag;
    }

    private String formatMsg(int priority, String tag, String msg) {
        if (this.config != null) {
            return this.config.consoleFormat.formatMsg(priority, tag, msg);
        }
        return msg;
    }

    /* access modifiers changed from: package-private */
    public void println(int priority, String tag, String msg) {
        Log.println(priority, formatTag(priority, tag), formatMsg(priority, tag, msg));
    }
}
