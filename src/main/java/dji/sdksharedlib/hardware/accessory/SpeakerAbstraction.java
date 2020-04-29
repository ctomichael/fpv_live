package dji.sdksharedlib.hardware.accessory;

import android.text.TextUtils;
import dji.common.accessory.SettingsDefinitions;
import dji.common.accessory.SpeakerState;
import dji.common.error.DJIAccessoryAggregationError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.internal.FirmwareVersionLoader;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonGetSNOfMavicRC;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataOnBoardSDKGetPushSpeakerInfo;
import dji.midware.data.model.P3.DataOnBoardSDKSetSpeakerParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.SpeakerKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SpeakerAbstraction extends DJISubComponentHWAbstraction {
    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(SpeakerKeys.class, getClass());
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataOnBoardSDKGetPushSpeakerInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOnBoardSDKGetPushSpeakerInfo.getInstance());
        }
        FirmwareVersionLoader.getInstance();
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        super.destroy();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOnBoardSDKGetPushSpeakerInfo info) {
        SpeakerState.Builder speakerStateBuilder;
        notifyValueChangeForKeyPath(Integer.valueOf(info.getSpeakerVolume()), convertKeyToPath(SpeakerKeys.SPEAKER_VOLUME));
        if (info.isSpeakerPlaying()) {
            speakerStateBuilder = new SpeakerState.Builder().playingState(SettingsDefinitions.SpeakerPlayingState.PLAYING).index(info.getPlayingSoundID());
        } else {
            speakerStateBuilder = new SpeakerState.Builder().playingState(SettingsDefinitions.SpeakerPlayingState.STOPPED).index(-1);
        }
        notifyValueChangeForKeyPath(speakerStateBuilder.storageLocation(SettingsDefinitions.AudioStorageLocation.find(info.getSpeakerDataSource())).volume(info.getSpeakerVolume()).playingMode(SettingsDefinitions.PlayMode.find(info.getSpeakerPlaybackMode())).build(), convertKeyToPath(SpeakerKeys.SPEAKER_STATE));
        notifyValueChangeForKeyPath(SettingsDefinitions.PlayMode.find(info.getSpeakerPlaybackMode()), convertKeyToPath(SpeakerKeys.PLAY_MODE));
    }

    @Setter(SpeakerKeys.SPEAKER_VOLUME)
    public void setSpeakerVolume(int volume, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataOnBoardSDKSetSpeakerParams().setParamType(DataOnBoardSDKSetSpeakerParams.ParamType.VOLUME.value()).setVolume(volume).start(CallbackUtils.defaultCB(callback, DJIAccessoryAggregationError.class));
    }

    @Setter(SpeakerKeys.PLAY_MODE)
    public void setSpeakerPlayMode(SettingsDefinitions.PlayMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (mode == null || mode == SettingsDefinitions.PlayMode.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            new DataOnBoardSDKSetSpeakerParams().setParamType(DataOnBoardSDKSetSpeakerParams.ParamType.PLAY_MODE.value()).setPlayMode(DataOnBoardSDKSetSpeakerParams.PlayMode.find(mode.value())).start(CallbackUtils.defaultCB(callback, DJIAccessoryAggregationError.class));
        }
    }

    @Action(SpeakerKeys.PLAY)
    public void play(DJISDKCacheHWAbstraction.InnerCallback callback, int index) {
        DataOnBoardSDKSetSpeakerParams setter = new DataOnBoardSDKSetSpeakerParams();
        ArrayList<Integer> indexs = new ArrayList<>();
        indexs.add(Integer.valueOf(index));
        setter.setParamType(DataOnBoardSDKSetSpeakerParams.ParamType.PLAY_CONTROL.value()).setControlType(DataOnBoardSDKSetSpeakerParams.ControlType.PLAY).setIndexList(indexs).start(CallbackUtils.defaultCB(callback, DJIAccessoryAggregationError.class));
    }

    @Action(SpeakerKeys.STOP)
    public void stop(DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataOnBoardSDKSetSpeakerParams().setParamType(DataOnBoardSDKSetSpeakerParams.ParamType.PLAY_CONTROL.value()).setControlType(DataOnBoardSDKSetSpeakerParams.ControlType.STOP).start(CallbackUtils.defaultCB(callback, DJIAccessoryAggregationError.class));
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetSNOfMavicRC getSNOfMavicRC = new DataCommonGetSNOfMavicRC();
        ((DataCommonGetSNOfMavicRC) getSNOfMavicRC.setDeviceType(DeviceType.OFDM).setReceiverId(3, DataCommonGetSNOfMavicRC.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.accessory.SpeakerAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (TextUtils.isEmpty(getSNOfMavicRC.getSN())) {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                } else {
                    CallbackUtils.onSuccess(callback, getSNOfMavicRC.getSN());
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(DJISDKCacheKeys.FIRMWARE_VERSION)
    public void getFirmwareVersion(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetVersion dataCommonGetVersion = new DataCommonGetVersion();
        dataCommonGetVersion.setDeviceType(DeviceType.OFDM);
        dataCommonGetVersion.setDeviceModel(3);
        dataCommonGetVersion.startForce(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.accessory.SpeakerAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                String firmVersion = dataCommonGetVersion.getFirmVer(".");
                if (!TextUtils.isEmpty(firmVersion)) {
                    CallbackUtils.onSuccess(callback, firmVersion);
                } else {
                    CallbackUtils.onFailure(callback, DJIError.UNABLE_TO_GET_FIRMWARE_VERSION);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }
}
