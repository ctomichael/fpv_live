package dji.log;

import android.content.Context;
import dji.log.impl.SimpleConsoleFormat;

public class DJILogConsoleConfig {
    public IConsoleFormat consoleFormat;
    /* access modifiers changed from: private */
    public int debuggablePriority;
    /* access modifiers changed from: private */
    public int factoryPriority;
    /* access modifiers changed from: private */
    public int innerPriority;
    public boolean open;
    /* access modifiers changed from: private */
    public int releasePriority;

    public static class Builder {
        DJILogConsoleConfig config = new DJILogConsoleConfig();

        public Builder(Context context) {
            setOpen(true);
            setDebuggablePriority(2);
            setInnerPriority(3);
            setFactoryPriority(3);
            setReleasePriority(6);
            setConsoleFormat(new SimpleConsoleFormat());
        }

        public Builder setOpen(boolean open) {
            this.config.open = open;
            return this;
        }

        public Builder setDebuggablePriority(int debuggablePriority) {
            int unused = this.config.debuggablePriority = debuggablePriority;
            return this;
        }

        public Builder setInnerPriority(int innerPriority) {
            int unused = this.config.innerPriority = innerPriority;
            return this;
        }

        public Builder setFactoryPriority(int factoryPriority) {
            int unused = this.config.factoryPriority = factoryPriority;
            return this;
        }

        public Builder setReleasePriority(int releasePriority) {
            int unused = this.config.releasePriority = releasePriority;
            return this;
        }

        public Builder setConsoleFormat(IConsoleFormat consoleFormat) {
            this.config.consoleFormat = consoleFormat;
            return this;
        }

        public DJILogConsoleConfig build() {
            return this.config;
        }
    }
}
