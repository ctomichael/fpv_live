package dji.midware.data.manager.packplugin;

import java.util.ArrayList;
import java.util.List;

public class MockCmdModel {
    /* access modifiers changed from: private */
    public int mCmdId;
    /* access modifiers changed from: private */
    public int mCmdSet;
    /* access modifiers changed from: private */
    public List<Byte> mMockingDatas;
    /* access modifiers changed from: private */
    public int mStartPos;

    public int getCmdSet() {
        return this.mCmdSet;
    }

    public int getCmdId() {
        return this.mCmdId;
    }

    public int getStartPos() {
        return this.mStartPos;
    }

    public List<Byte> getMockingDatas() {
        return this.mMockingDatas;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("MockingCmdModel{\n");
        sb.append("mCmdSet=").append(this.mCmdSet);
        sb.append(", mCmdId=").append(this.mCmdId);
        sb.append(", mStartPos=").append(this.mStartPos);
        sb.append(", mMockingDatas=").append(this.mMockingDatas);
        sb.append('}');
        return sb.toString();
    }

    public static final class Builder {
        private int mCmdId;
        private int mCmdSet;
        private List<Byte> mMockingDatas;
        private int mStartPos;

        private Builder() {
        }

        public static Builder aMockingCmdModel() {
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

        public Builder StartPos(int StartPos) {
            this.mStartPos = StartPos;
            return this;
        }

        public Builder MockingDatas(List<Byte> MockingDatas) {
            this.mMockingDatas = new ArrayList(MockingDatas);
            return this;
        }

        public MockCmdModel build() {
            MockCmdModel mockingCmdModel = new MockCmdModel();
            int unused = mockingCmdModel.mCmdId = this.mCmdId;
            int unused2 = mockingCmdModel.mCmdSet = this.mCmdSet;
            List unused3 = mockingCmdModel.mMockingDatas = this.mMockingDatas;
            int unused4 = mockingCmdModel.mStartPos = this.mStartPos;
            return mockingCmdModel;
        }
    }
}
