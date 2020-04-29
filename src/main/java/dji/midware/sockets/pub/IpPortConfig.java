package dji.midware.sockets.pub;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class IpPortConfig {
    public String ip;
    public int port;
    public ConnectType type = ConnectType.UNKNOWN;

    public IpPortConfig(String ip2, int port2, ConnectType type2) {
        this.ip = ip2;
        this.port = port2;
        this.type = type2;
    }

    public enum ConnectType {
        DRONE(0),
        RC(1),
        UNKNOWN(-1);
        
        int value;

        private ConnectType(int value2) {
            this.value = value2;
        }
    }

    public boolean isRc() {
        return this.type == ConnectType.RC;
    }

    public boolean isDrone() {
        return this.type == ConnectType.DRONE;
    }
}
