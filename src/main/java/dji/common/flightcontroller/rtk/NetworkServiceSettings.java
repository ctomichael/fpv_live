package dji.common.flightcontroller.rtk;

import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction;

public class NetworkServiceSettings {
    private String mountPoint;
    private String password;
    private int port;
    private String serverAddress;
    private String userName;

    public NetworkServiceSettings() {
        this.serverAddress = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        this.port = 0;
        this.userName = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        this.password = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        this.mountPoint = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
    }

    private NetworkServiceSettings(Builder builder) {
        this.serverAddress = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        this.port = 0;
        this.userName = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        this.password = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        this.mountPoint = DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
        this.serverAddress = builder.serverAddress;
        this.port = builder.port;
        this.userName = builder.userName;
        this.password = builder.password;
        this.mountPoint = builder.mountPoint;
    }

    public String getServerAddress() {
        return this.serverAddress;
    }

    public int getPort() {
        return this.port;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getMountPoint() {
        return this.mountPoint;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public String mountPoint;
        /* access modifiers changed from: private */
        public String password;
        /* access modifiers changed from: private */
        public int port;
        /* access modifiers changed from: private */
        public String serverAddress;
        /* access modifiers changed from: private */
        public String userName;

        public Builder ip(String ip) {
            this.serverAddress = ip;
            return this;
        }

        public Builder port(int port2) {
            this.port = port2;
            return this;
        }

        public Builder userName(String userName2) {
            this.userName = userName2;
            return this;
        }

        public Builder password(String password2) {
            this.password = password2;
            return this;
        }

        public Builder mountPoint(String mountPoint2) {
            this.mountPoint = mountPoint2;
            return this;
        }

        public NetworkServiceSettings build() {
            return new NetworkServiceSettings(this);
        }
    }

    public boolean isValid() {
        if (this.userName.isEmpty() || this.password.isEmpty() || this.serverAddress.isEmpty() || this.port <= 0 || this.mountPoint.isEmpty() || this.userName.equals(DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION) || this.password.equals(DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION) || this.serverAddress.equals(DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION) || this.mountPoint.equals(DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION)) {
            return false;
        }
        return true;
    }
}
