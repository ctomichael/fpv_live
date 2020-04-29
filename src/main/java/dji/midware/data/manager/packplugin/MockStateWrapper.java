package dji.midware.data.manager.packplugin;

public class MockStateWrapper {
    private MockCmdModel mMockCmdModel;
    private MockState mMockState = MockState.IDLE;

    public enum MockState {
        MOCKING,
        IDLE
    }

    public MockStateWrapper(MockState mockState) {
        this.mMockState = mockState;
    }

    public MockStateWrapper(MockState mockState, MockCmdModel mockCmdModel) {
        this.mMockState = mockState;
        this.mMockCmdModel = mockCmdModel;
    }

    public MockState getMockState() {
        return this.mMockState;
    }

    public MockCmdModel getMockCmdModel() {
        return this.mMockCmdModel;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("MockStateWrapper{\n");
        sb.append("mMockState=").append(this.mMockState);
        sb.append(", mMockCmdModel=").append(this.mMockCmdModel);
        sb.append('}');
        return sb.toString();
    }
}
