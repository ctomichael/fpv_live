package dji.common.remotecontroller;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class MasterSlaveState {
    private final String authorizationCode;
    private final String freqPoint;
    private final boolean hasGimbalControl;
    private final boolean isConnected;
    private final String masterId;
    private final RCMode rcMode;
    private final String receiveFreq;
    private final String rssi;
    private final String sendFreq;
    private final String slaveId;

    public interface Callback {
        void onUpdate(@NonNull MasterSlaveState masterSlaveState);
    }

    private MasterSlaveState(Builder builder) {
        this.rcMode = builder.rcMode;
        this.masterId = builder.masterId;
        this.slaveId = builder.slaveId;
        this.isConnected = builder.isConnected;
        this.hasGimbalControl = builder.isGimbalControlled;
        this.freqPoint = builder.freqPoint;
        this.sendFreq = builder.sendFreq;
        this.receiveFreq = builder.receiveFreq;
        this.authorizationCode = builder.authorizationCode;
        this.rssi = builder.rssi;
    }

    public String getRssi() {
        return this.rssi;
    }

    public RCMode getRcMode() {
        return this.rcMode;
    }

    public String getMasterId() {
        return this.masterId;
    }

    public String getSlaveId() {
        return this.slaveId;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public String getFreqPoint() {
        return this.freqPoint;
    }

    public String getSendFreq() {
        return this.sendFreq;
    }

    public String getReceiveFreq() {
        return this.receiveFreq;
    }

    public String getAuthorizationCode() {
        return this.authorizationCode;
    }

    public boolean hasGimbalControl() {
        return this.hasGimbalControl;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof MasterSlaveState)) {
            return false;
        }
        MasterSlaveState that = (MasterSlaveState) o;
        if (this.isConnected != that.isConnected || this.hasGimbalControl != that.hasGimbalControl || this.rcMode != that.rcMode) {
            return false;
        }
        if (this.masterId != null) {
            if (!this.masterId.equals(that.masterId)) {
                return false;
            }
        } else if (that.masterId != null) {
            return false;
        }
        if (this.slaveId != null) {
            if (!this.slaveId.equals(that.slaveId)) {
                return false;
            }
        } else if (that.slaveId != null) {
            return false;
        }
        if (this.freqPoint != null) {
            if (!this.freqPoint.equals(that.freqPoint)) {
                return false;
            }
        } else if (that.freqPoint != null) {
            return false;
        }
        if (this.sendFreq != null) {
            if (!this.sendFreq.equals(that.sendFreq)) {
                return false;
            }
        } else if (that.sendFreq != null) {
            return false;
        }
        if (this.receiveFreq != null) {
            if (!this.receiveFreq.equals(that.receiveFreq)) {
                return false;
            }
        } else if (that.receiveFreq != null) {
            return false;
        }
        if (this.authorizationCode != null) {
            if (!this.authorizationCode.equals(that.authorizationCode)) {
                return false;
            }
        } else if (that.authorizationCode != null) {
            return false;
        }
        if (this.rssi != null) {
            z = this.rssi.equals(that.rssi);
        } else if (that.rssi != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8 = 1;
        int i9 = 0;
        if (this.rcMode != null) {
            result = this.rcMode.hashCode();
        } else {
            result = 0;
        }
        int i10 = result * 31;
        if (this.masterId != null) {
            i = this.masterId.hashCode();
        } else {
            i = 0;
        }
        int i11 = (i10 + i) * 31;
        if (this.slaveId != null) {
            i2 = this.slaveId.hashCode();
        } else {
            i2 = 0;
        }
        int i12 = (i11 + i2) * 31;
        if (this.isConnected) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i13 = (i12 + i3) * 31;
        if (!this.hasGimbalControl) {
            i8 = 0;
        }
        int i14 = (i13 + i8) * 31;
        if (this.freqPoint != null) {
            i4 = this.freqPoint.hashCode();
        } else {
            i4 = 0;
        }
        int i15 = (i14 + i4) * 31;
        if (this.sendFreq != null) {
            i5 = this.sendFreq.hashCode();
        } else {
            i5 = 0;
        }
        int i16 = (i15 + i5) * 31;
        if (this.receiveFreq != null) {
            i6 = this.receiveFreq.hashCode();
        } else {
            i6 = 0;
        }
        int i17 = (i16 + i6) * 31;
        if (this.authorizationCode != null) {
            i7 = this.authorizationCode.hashCode();
        } else {
            i7 = 0;
        }
        int i18 = (i17 + i7) * 31;
        if (this.rssi != null) {
            i9 = this.rssi.hashCode();
        }
        return i18 + i9;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public String authorizationCode;
        /* access modifiers changed from: private */
        public String freqPoint;
        /* access modifiers changed from: private */
        public boolean isConnected;
        /* access modifiers changed from: private */
        public boolean isGimbalControlled;
        /* access modifiers changed from: private */
        public String masterId;
        /* access modifiers changed from: private */
        public RCMode rcMode;
        /* access modifiers changed from: private */
        public String receiveFreq;
        /* access modifiers changed from: private */
        public String rssi;
        /* access modifiers changed from: private */
        public String sendFreq;
        /* access modifiers changed from: private */
        public String slaveId;

        public Builder rcMode(RCMode rcMode2) {
            this.rcMode = rcMode2;
            return this;
        }

        public Builder masterId(String masterId2) {
            this.masterId = masterId2;
            return this;
        }

        public Builder slaveId(String slaveId2) {
            this.slaveId = slaveId2;
            return this;
        }

        public Builder isGimbalControlled(boolean isGimbalControlled2) {
            this.isGimbalControlled = isGimbalControlled2;
            return this;
        }

        public Builder isConnected(boolean isConnected2) {
            this.isConnected = isConnected2;
            return this;
        }

        public Builder freqPoint(String freqPoint2) {
            this.freqPoint = freqPoint2;
            return this;
        }

        public Builder sendFreq(String sendFreq2) {
            this.sendFreq = sendFreq2;
            return this;
        }

        public Builder receiveFreq(String receiveFreq2) {
            this.receiveFreq = receiveFreq2;
            return this;
        }

        public Builder authorizationCode(String authorizationCode2) {
            this.authorizationCode = authorizationCode2;
            return this;
        }

        public Builder rssi(String rssi2) {
            this.rssi = rssi2;
            return this;
        }

        public MasterSlaveState build() {
            return new MasterSlaveState(this);
        }
    }
}
