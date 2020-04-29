package dji.midware.data.manager.packplugin.record;

import dji.midware.data.manager.packplugin.record.CmdPackPlugin;

public class PluginPackRecordEvent {
    private CmdPackPlugin.DJIV1Pack4Save mPack4Save;

    public CmdPackPlugin.DJIV1Pack4Save getPack4Save() {
        return this.mPack4Save;
    }

    public void setPack4Save(CmdPackPlugin.DJIV1Pack4Save pack4Save) {
        this.mPack4Save = pack4Save;
    }
}
