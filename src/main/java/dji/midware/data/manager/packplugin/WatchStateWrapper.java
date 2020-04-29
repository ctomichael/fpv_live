package dji.midware.data.manager.packplugin;

public class WatchStateWrapper {
    private WatchState mMockState = WatchState.IDLE;
    private WatchCmdModel mWatchCmd = WatchCmdModel.DEFAULT_WATCH_CMD_MODEL;

    public enum WatchState {
        WATCHING,
        IDLE
    }

    public WatchStateWrapper(WatchState mockState) {
        this.mMockState = mockState;
    }

    public WatchStateWrapper(WatchCmdModel watchCmd, WatchState mockState) {
        this.mWatchCmd = watchCmd;
        this.mMockState = mockState;
    }

    public WatchCmdModel getWatchCmd() {
        return this.mWatchCmd;
    }

    public WatchState getMockState() {
        return this.mMockState;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("WatchStateWrapper{\n");
        sb.append("mMockState=").append(this.mMockState);
        sb.append(", mWatchCmd=").append(this.mWatchCmd);
        sb.append('}');
        return sb.toString();
    }
}
