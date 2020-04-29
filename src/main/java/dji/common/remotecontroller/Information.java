package dji.common.remotecontroller;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Information {
    private boolean hasGimbalControlPermission;
    private int id;
    private String name;
    private String password;
    private short signalQuality;

    public interface ControlRequestCallback {
        void onReceive(@NonNull Information information);
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean hasGimbalControlPermission;
        /* access modifiers changed from: private */
        public int id;
        /* access modifiers changed from: private */
        public String name;
        /* access modifiers changed from: private */
        public String password;
        /* access modifiers changed from: private */
        public short signalQuality;

        public Builder identifier(int identifier) {
            this.id = identifier;
            return this;
        }

        public Builder name(String name2) {
            this.name = name2;
            return this;
        }

        public Builder password(String password2) {
            this.password = password2;
            return this;
        }

        public Builder signalQuality(short signalQuality2) {
            this.signalQuality = signalQuality2;
            return this;
        }

        public Builder hasGimbalControlPermission(boolean hasGimbalControlPermission2) {
            this.hasGimbalControlPermission = hasGimbalControlPermission2;
            return this;
        }

        public Information build() {
            return new Information(this);
        }
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public short getSignalQuality() {
        return this.signalQuality;
    }

    public boolean hasGimbalControlPermission() {
        return this.hasGimbalControlPermission;
    }

    private Information(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.password = builder.password;
        this.signalQuality = builder.signalQuality;
        this.hasGimbalControlPermission = builder.hasGimbalControlPermission;
    }
}
