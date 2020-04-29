package dji.midware.data.manager.packplugin;

import dji.midware.data.manager.packplugin.record.DJIPackRecordPlugin;
import java.util.ArrayList;
import java.util.List;

public class WatchCmdModel {
    public static final WatchCmdModel DEFAULT_WATCH_CMD_MODEL = Builder.aWatchCmdModel().CmdId(-1).CmdSet(-1).SenderType(-1).StartPos(-1).Length(-1).build();
    /* access modifiers changed from: private */
    public int mCmdId;
    /* access modifiers changed from: private */
    public int mCmdSet;
    /* access modifiers changed from: private */
    public int mLength;
    /* access modifiers changed from: private */
    public DJIPackRecordPlugin.PackType4Plugin mPackType = DJIPackRecordPlugin.PackType4Plugin.PUSH;
    /* access modifiers changed from: private */
    public int mSenderType;
    /* access modifiers changed from: private */
    public int mStartPos;
    private List<Byte> mWatchBytes = new ArrayList();

    public String toString() {
        StringBuffer sb = new StringBuffer("WatchCmdModel{");
        sb.append("mCmdSet=").append(this.mCmdSet);
        sb.append(", mCmdId=").append(this.mCmdId);
        sb.append(", mSenderType=").append(this.mSenderType);
        sb.append(", mStartPos=").append(this.mStartPos);
        sb.append(", mLength=").append(this.mLength);
        sb.append(", mPackType=").append(this.mPackType);
        sb.append(", mWatchBytes=").append(this.mWatchBytes);
        sb.append('}');
        return sb.toString();
    }

    public int getCmdSet() {
        return this.mCmdSet;
    }

    public int getCmdId() {
        return this.mCmdId;
    }

    public int getSenderType() {
        return this.mSenderType;
    }

    public int getStartPos() {
        return this.mStartPos;
    }

    public int getLength() {
        return this.mLength;
    }

    public void setWatchBytes(List<Byte> watchBytes) {
        this.mWatchBytes = watchBytes;
    }

    public List<Byte> getWatchBytes() {
        return this.mWatchBytes;
    }

    public DJIPackRecordPlugin.PackType4Plugin getPackType() {
        return this.mPackType;
    }

    public static final class Builder {
        public static WatchCmdModel DEFAULT_WATCH_CMD_MODEL = aWatchCmdModel().CmdId(-1).CmdSet(-1).SenderType(-1).StartPos(-1).Length(-1).build();
        private int mCmdId;
        private int mCmdSet;
        private int mLength;
        private DJIPackRecordPlugin.PackType4Plugin mPackType = DJIPackRecordPlugin.PackType4Plugin.PUSH;
        private int mSenderType;
        private int mStartPos;
        private List<Byte> mWatchBytes = new ArrayList();

        private Builder() {
        }

        public static Builder aWatchCmdModel() {
            return new Builder();
        }

        public Builder CmdSet(int CmdSet) {
            this.mCmdSet = CmdSet;
            return this;
        }

        public Builder CmdId(int CmdId) {
            this.mCmdId = CmdId;
            return this;
        }

        public Builder SenderType(int SenderType) {
            this.mSenderType = SenderType;
            return this;
        }

        public Builder StartPos(int StartPos) {
            this.mStartPos = StartPos;
            return this;
        }

        public Builder Length(int Length) {
            this.mLength = Length;
            return this;
        }

        public Builder PackType(DJIPackRecordPlugin.PackType4Plugin PackType) {
            this.mPackType = PackType;
            return this;
        }

        public Builder WatchBytes(List<Byte> WatchBytes) {
            this.mWatchBytes = WatchBytes;
            return this;
        }

        public Builder DEFAULT_WATCH_CMD_MODEL(WatchCmdModel DEFAULT_WATCH_CMD_MODEL2) {
            DEFAULT_WATCH_CMD_MODEL = DEFAULT_WATCH_CMD_MODEL2;
            return this;
        }

        public WatchCmdModel build() {
            WatchCmdModel watchCmdModel = new WatchCmdModel();
            watchCmdModel.setWatchBytes(this.mWatchBytes);
            int unused = watchCmdModel.mLength = this.mLength;
            int unused2 = watchCmdModel.mCmdSet = this.mCmdSet;
            int unused3 = watchCmdModel.mStartPos = this.mStartPos;
            int unused4 = watchCmdModel.mSenderType = this.mSenderType;
            int unused5 = watchCmdModel.mCmdId = this.mCmdId;
            DJIPackRecordPlugin.PackType4Plugin unused6 = watchCmdModel.mPackType = this.mPackType;
            return watchCmdModel;
        }
    }
}
