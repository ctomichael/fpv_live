package dji.thirdparty.plogger;

public final class Settings {
    private LogAdapter logAdapter;
    private LogLevel logLevel = LogLevel.FULL;
    private int methodCount = 2;
    private int methodOffset = 0;
    private boolean showThreadInfo = true;

    public Settings hideThreadInfo() {
        this.showThreadInfo = false;
        return this;
    }

    public Settings methodCount(int methodCount2) {
        if (methodCount2 < 0) {
            methodCount2 = 0;
        }
        this.methodCount = methodCount2;
        return this;
    }

    public Settings logLevel(LogLevel logLevel2) {
        this.logLevel = logLevel2;
        return this;
    }

    public Settings methodOffset(int offset) {
        this.methodOffset = offset;
        return this;
    }

    public Settings logAdapter(LogAdapter logAdapter2) {
        this.logAdapter = logAdapter2;
        return this;
    }

    public int getMethodCount() {
        return this.methodCount;
    }

    public boolean isShowThreadInfo() {
        return this.showThreadInfo;
    }

    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    public int getMethodOffset() {
        return this.methodOffset;
    }

    public LogAdapter getLogAdapter() {
        if (this.logAdapter == null) {
            this.logAdapter = new AndroidLogAdapter();
        }
        return this.logAdapter;
    }

    public void reset() {
        this.methodCount = 2;
        this.methodOffset = 0;
        this.showThreadInfo = true;
        this.logLevel = LogLevel.FULL;
    }
}
