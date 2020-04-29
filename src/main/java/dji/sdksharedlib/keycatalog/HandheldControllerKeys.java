package dji.sdksharedlib.keycatalog;

import dji.common.Stick;
import dji.common.handheld.HandheldButtonClickEvent;
import dji.common.handheld.LEDCommand;
import dji.common.handheld.PowerMode;
import dji.common.handheld.RecordAndShutterButtons;
import dji.common.handheld.StickHorizontalDirection;
import dji.common.handheld.StickVerticalDirection;
import dji.common.handheld.ZoomState;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.hardware.abstractions.handheldcontroller.Mobile2HandheldControllerAbstraction;
import dji.sdksharedlib.hardware.abstractions.handheldcontroller.MobileHandheldControllerAbstraction;
import dji.sdksharedlib.keycatalog.extension.InternalKey;
import dji.sdksharedlib.keycatalog.extension.Key;

@EXClassNullAway
public class HandheldControllerKeys extends DJISDKCacheKeys {
    public static final String COMPONENT_KEY = "HandheldController";
    public static final String FAKE_ACTION = "FakeAction";
    @InternalKey
    @Key(accessType = 1, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FULL_SERIAL_NUMBER_HASH = "FullSerialNumberHash";
    @Key(accessType = 3, includedAbstractions = {MobileHandheldControllerAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String HANDHELD_NAME = "HandheldName";
    @Key(accessType = 4, includedAbstractions = {MobileHandheldControllerAbstraction.class}, type = Boolean.class)
    public static final String IS_MODE_BUTTON_BEING_PRESSED = "IsModeButtonBeingPressed";
    @Key(accessType = 4, includedAbstractions = {MobileHandheldControllerAbstraction.class}, type = Boolean.class)
    public static final String IS_TRIGGER_BEING_PRESSED = "IsTriggerBeingPressed";
    @Key(accessType = 8, excludedAbstractions = {Mobile2HandheldControllerAbstraction.class}, includedAbstractions = {MobileHandheldControllerAbstraction.class}, types = {LEDCommand.class})
    public static final String LED_COMMAND = "LEDCommand";
    @Key(accessType = 4, includedAbstractions = {MobileHandheldControllerAbstraction.class}, type = HandheldButtonClickEvent.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MODE_BUTTON = "ModeButtonState";
    @Key(accessType = 6, excludedAbstractions = {MobileHandheldControllerAbstraction.class, Mobile2HandheldControllerAbstraction.class}, type = PowerMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String POWER_MODE = "PowerMode";
    @Key(accessType = 4, includedAbstractions = {MobileHandheldControllerAbstraction.class}, type = RecordAndShutterButtons.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RECORD_AND_SHUTTER_BUTTONS = "RecordAndShutterButtons";
    @Key(accessType = 4, includedAbstractions = {MobileHandheldControllerAbstraction.class}, types = {Stick.class})
    public static final String STICK = "Stick";
    @Key(accessType = 6, includedAbstractions = {MobileHandheldControllerAbstraction.class}, types = {Boolean.class})
    public static final String STICK_GIMBAL_CONTROL_ENABLED = "StickGimbalControlEnabled";
    @Key(accessType = 4, includedAbstractions = {MobileHandheldControllerAbstraction.class}, type = StickHorizontalDirection.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String STICK_HORIZONTAL_DIRECTION = "StickHorizontalDirection";
    @Key(accessType = 4, includedAbstractions = {MobileHandheldControllerAbstraction.class}, type = StickVerticalDirection.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String STICK_VERTICAL_DIRECTION = "StickVerticalDirection";
    @Key(accessType = 4, includedAbstractions = {MobileHandheldControllerAbstraction.class}, type = HandheldButtonClickEvent.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TRIGGER_BUTTON = "TriggerButtonState";
    @Key(accessType = 4, includedAbstractions = {Mobile2HandheldControllerAbstraction.class}, type = ZoomState.class)
    public static final String ZOOM_STATE = "ZoomState";

    public HandheldControllerKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return "handHeld";
    }
}
