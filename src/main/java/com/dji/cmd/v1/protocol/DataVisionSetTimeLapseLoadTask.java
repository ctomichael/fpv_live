package com.dji.cmd.v1.protocol;

import com.dji.cmd.v1.base.Protocol;
import com.dji.cmd.v1.base.ProtocolBytesUtil;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;

public class DataVisionSetTimeLapseLoadTask extends Protocol {
    public DataVisionSetTimeLapseLoadTask() {
        this.reqSize = 14;
        this.rspSize = 3;
        this.cmdSet = 10;
        this.cmdId = PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH;
    }

    public long getReqTaskId() {
        return ProtocolBytesUtil.getLong(this.innerReq, 0, 8);
    }

    public DataVisionSetTimeLapseLoadTask setReqTaskId(long taskId) {
        ProtocolBytesUtil.setValue(taskId, this.innerReq, 0, 8);
        return this;
    }

    public int getReqTotalCnt() {
        return ProtocolBytesUtil.getInt(this.innerReq, 8, 2);
    }

    public DataVisionSetTimeLapseLoadTask setReqTotalCnt(int totalCnt) {
        ProtocolBytesUtil.setValue(totalCnt, this.innerReq, 8, 2);
        return this;
    }

    public int getReqFirstSequenceCurPack() {
        return ProtocolBytesUtil.getInt(this.innerReq, 10, 2);
    }

    public DataVisionSetTimeLapseLoadTask setReqFirstSequenceCurPack(int firstSequenceCurPack) {
        ProtocolBytesUtil.setValue(firstSequenceCurPack, this.innerReq, 10, 2);
        return this;
    }

    public int getReqWptCntCurPack() {
        return ProtocolBytesUtil.getInt(this.innerReq, 12, 2);
    }

    public DataVisionSetTimeLapseLoadTask setReqWptCntCurPack(int wptCntCurPack) {
        ProtocolBytesUtil.setValue(wptCntCurPack, this.innerReq, 12, 2);
        return this;
    }

    public TimeLapseWaypointInfo[] getReqWaypointSet() {
        int len = getReqWptCntCurPack();
        TimeLapseWaypointInfo[] tmpArrRes = new TimeLapseWaypointInfo[len];
        for (int i = 0; i < len; i++) {
            byte[] tmpInnerRes = new byte[64];
            ProtocolBytesUtil.arraycopy(this.innerReq, (i * 64) + 14, tmpInnerRes, 0, 64);
            tmpArrRes[i] = new TimeLapseWaypointInfo(tmpInnerRes);
        }
        return tmpArrRes;
    }

    public DataVisionSetTimeLapseLoadTask setReqWaypointSet(TimeLapseWaypointInfo[] waypointSet) {
        byte[] buffer = new byte[((waypointSet.length * 64) + 14)];
        System.arraycopy(this.innerReq, 0, buffer, 0, 14);
        this.innerReq = buffer;
        for (int i = 0; i < waypointSet.length; i++) {
            System.arraycopy(waypointSet[i].getBytes(), 0, this.innerReq, (i * 64) + 14, 64);
        }
        return this;
    }

    public int getRspReturnType() {
        return ProtocolBytesUtil.getInt(this.innerRsp, 0, 1);
    }

    public DataVisionSetTimeLapseLoadTask setRspReturnType(int returnType) {
        ProtocolBytesUtil.setValue(returnType, this.innerRsp, 0, 1);
        return this;
    }

    public int getRspLastSequenceCurPack() {
        return ProtocolBytesUtil.getInt(this.innerRsp, 1, 2);
    }

    public DataVisionSetTimeLapseLoadTask setRspLastSequenceCurPack(int lastSequenceCurPack) {
        ProtocolBytesUtil.setValue(lastSequenceCurPack, this.innerRsp, 1, 2);
        return this;
    }
}
