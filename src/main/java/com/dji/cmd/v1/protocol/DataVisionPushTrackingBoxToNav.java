package com.dji.cmd.v1.protocol;

import com.dji.cmd.v1.base.Protocol;
import com.dji.cmd.v1.base.ProtocolBytesUtil;

public class DataVisionPushTrackingBoxToNav extends Protocol {
    public DataVisionPushTrackingBoxToNav() {
        this.reqSize = 33;
        this.rspSize = 0;
        this.cmdSet = 10;
        this.cmdId = 231;
    }

    public float getReqCenterX() {
        return ProtocolBytesUtil.getFloat(this.innerReq, 0, 4);
    }

    public DataVisionPushTrackingBoxToNav setReqCenterX(float centerX) {
        ProtocolBytesUtil.setValue(centerX, this.innerReq, 0, 4);
        return this;
    }

    public float getReqCenterY() {
        return ProtocolBytesUtil.getFloat(this.innerReq, 4, 4);
    }

    public DataVisionPushTrackingBoxToNav setReqCenterY(float centerY) {
        ProtocolBytesUtil.setValue(centerY, this.innerReq, 4, 4);
        return this;
    }

    public float getReqWidth() {
        return ProtocolBytesUtil.getFloat(this.innerReq, 8, 4);
    }

    public DataVisionPushTrackingBoxToNav setReqWidth(float width) {
        ProtocolBytesUtil.setValue(width, this.innerReq, 8, 4);
        return this;
    }

    public float getReqHeight() {
        return ProtocolBytesUtil.getFloat(this.innerReq, 12, 4);
    }

    public DataVisionPushTrackingBoxToNav setReqHeight(float height) {
        ProtocolBytesUtil.setValue(height, this.innerReq, 12, 4);
        return this;
    }

    public MlTkModeE getReqMode() {
        return MlTkModeE.find(ProtocolBytesUtil.getInt(this.innerReq, 16, 4));
    }

    public DataVisionPushTrackingBoxToNav setReqMode(MlTkModeE mode) {
        ProtocolBytesUtil.setValue(mode.value(), this.innerReq, 16, 4);
        return this;
    }

    public long getReqFlag() {
        return ProtocolBytesUtil.getLong(this.innerReq, 20, 4);
    }

    public DataVisionPushTrackingBoxToNav setReqFlag(long flag) {
        ProtocolBytesUtil.setValue(flag, this.innerReq, 20, 4);
        return this;
    }

    public long getReqTimeStamp() {
        return ProtocolBytesUtil.getLong(this.innerReq, 24, 8);
    }

    public DataVisionPushTrackingBoxToNav setReqTimeStamp(long timeStamp) {
        ProtocolBytesUtil.setValue(timeStamp, this.innerReq, 24, 8);
        return this;
    }

    public int getReqObjType() {
        return ProtocolBytesUtil.getInt(this.innerReq, 32, 1);
    }

    public DataVisionPushTrackingBoxToNav setReqObjType(int objType) {
        ProtocolBytesUtil.setValue(objType, this.innerReq, 32, 1);
        return this;
    }
}
