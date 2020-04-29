package dji.common.flightcontroller.flightassistant;

import com.dji.cmd.v1.protocol.TimeLapseWaypointInfo;
import dji.thirdparty.afinal.annotation.sqlite.Id;

public class TimeLapseWaypoint {
    public float altitude;
    public float gimbalPitch;
    public float gimbalRoll;
    public float gimbalYaw;
    public float height;
    @Id
    public int id;
    public double latitude;
    public double longitude;
    public long pointIndex;
    public long trajId;
    public float x;
    public float y;
    public float z;

    public static TimeLapseWaypoint getFromProtocolData(TimeLapseWaypointInfo info) {
        TimeLapseWaypoint res = new TimeLapseWaypoint();
        res.pointIndex = info.getIndex();
        res.x = info.getPx();
        res.y = info.getPy();
        res.z = info.getPz();
        res.height = info.getHeight();
        res.gimbalPitch = info.getGimbalPitch();
        res.gimbalRoll = info.getGimbalRoll();
        res.gimbalYaw = info.getGimbalYaw();
        res.longitude = info.getLongitude();
        res.latitude = info.getLatitude();
        res.altitude = info.getAltitude();
        res.trajId = info.getTrajId();
        return res;
    }

    public TimeLapseWaypointInfo toProtocolData() {
        TimeLapseWaypointInfo res = new TimeLapseWaypointInfo();
        res.setIndex(this.pointIndex);
        res.setPx(this.x);
        res.setPy(this.y);
        res.setPz(this.z);
        res.setHeight(this.height);
        res.setGimbalPitch(this.gimbalPitch);
        res.setGimbalRoll(this.gimbalRoll);
        res.setGimbalYaw(this.gimbalYaw);
        res.setLongitude(this.longitude);
        res.setLatitude(this.latitude);
        res.setAltitude(this.altitude);
        res.setTrajId(this.trajId);
        return res;
    }
}
