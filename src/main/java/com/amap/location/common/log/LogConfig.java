package com.amap.location.common.log;

public class LogConfig {
    /* access modifiers changed from: private */
    public boolean mFileLogEnable;
    /* access modifiers changed from: private */
    public a mILogToServerImpl;
    /* access modifiers changed from: private */
    public boolean mIsTraceUpToServer;
    /* access modifiers changed from: private */
    public boolean mIsTraceWriteToFile;
    /* access modifiers changed from: private */
    public String mLogFileDir;
    /* access modifiers changed from: private */
    public int mLogFileMaxCount;
    /* access modifiers changed from: private */
    public int mLogMemoryBufferSize;
    /* access modifiers changed from: private */
    public boolean mLogcatEnable;
    /* access modifiers changed from: private */
    public Product mProduct;
    /* access modifiers changed from: private */
    public boolean mServerLogEnable;
    /* access modifiers changed from: private */
    public int mSignalLogFileLimit;

    public static class Builder {
        private LogConfig logConfig = new LogConfig();

        public LogConfig build(Product product, String str) {
            if (product == null) {
                throw new IllegalArgumentException("product 不能为 null ");
            }
            if (this.logConfig.mFileLogEnable && (str == null || str.trim().length() == 0)) {
                boolean unused = this.logConfig.mFileLogEnable = false;
                str = null;
            }
            Product unused2 = this.logConfig.mProduct = product;
            String unused3 = this.logConfig.mLogFileDir = str;
            return this.logConfig;
        }

        public Builder setFileLogEnable(boolean z) {
            boolean unused = this.logConfig.mFileLogEnable = z;
            return this;
        }

        public Builder setLogFileMaxCount(int i) {
            int unused = this.logConfig.mLogFileMaxCount = i;
            return this;
        }

        public Builder setLogMemoryBufferSize(int i) {
            int unused = this.logConfig.mLogMemoryBufferSize = i;
            return this;
        }

        public Builder setLogToServer(a aVar) {
            a unused = this.logConfig.mILogToServerImpl = aVar;
            return this;
        }

        public Builder setLogcatEnable(boolean z) {
            boolean unused = this.logConfig.mLogcatEnable = z;
            return this;
        }

        public Builder setServerLogEnable(boolean z) {
            boolean unused = this.logConfig.mServerLogEnable = z;
            return this;
        }

        public Builder setSignalLogFileLimit(int i) {
            int unused = this.logConfig.mSignalLogFileLimit = i;
            return this;
        }

        public Builder setTraceUpToServer(boolean z) {
            boolean unused = this.logConfig.mIsTraceUpToServer = z;
            return this;
        }

        public Builder setTraceWriteToFile(boolean z) {
            boolean unused = this.logConfig.mIsTraceWriteToFile = z;
            return this;
        }
    }

    public enum Product {
        FLP,
        NLP,
        SDK
    }

    public interface a {
        void a(String str);

        boolean a();
    }

    private LogConfig() {
        this.mLogcatEnable = false;
        this.mFileLogEnable = false;
        this.mServerLogEnable = false;
        this.mLogFileDir = "";
        this.mProduct = Product.SDK;
        this.mIsTraceWriteToFile = false;
        this.mIsTraceUpToServer = true;
        this.mLogMemoryBufferSize = 204800;
        this.mSignalLogFileLimit = 1048576;
        this.mLogFileMaxCount = 20;
    }

    public String getLogFileDir() {
        return this.mLogFileDir;
    }

    public int getLogFileMaxCount() {
        return this.mLogFileMaxCount;
    }

    public int getLogMemoryBufferSize() {
        return this.mLogMemoryBufferSize;
    }

    public a getLogToServerImpl() {
        return this.mILogToServerImpl;
    }

    public Product getProduct() {
        return this.mProduct;
    }

    public int getSignalLogFileLimit() {
        return this.mSignalLogFileLimit;
    }

    public boolean isFileLogEnable() {
        return this.mFileLogEnable;
    }

    public boolean isLogcatEnable() {
        return this.mLogcatEnable;
    }

    public boolean isServerLogEnable() {
        return this.mServerLogEnable;
    }

    public boolean isTraceUpToServer() {
        return this.mIsTraceUpToServer;
    }

    public boolean isTraceWriteToFile() {
        return this.mIsTraceWriteToFile;
    }
}
