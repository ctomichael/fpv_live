package com.dji.component.fpv.base.navigation;

public class MultiQuickshotSubState extends ModeSubState {
    public static final MultiQuickshotSubState CAN_NOT_CONFIRM = new MultiQuickshotSubState("CannotConfirm");
    public static final MultiQuickshotSubState COMEBACK_NORMALLY = new MultiQuickshotSubState("ComebackNormally");
    public static final MultiQuickshotSubState COUNTING_DOWN = new MultiQuickshotSubState("CountingDown");
    public static final MultiQuickshotSubState IDLE = new MultiQuickshotSubState("Idle");
    public static final MultiQuickshotSubState PRE_START_QUICK_SHOT = new MultiQuickshotSubState("PreStartQuickShot");
    public static final MultiQuickshotSubState QUICK_SHOTING = new MultiQuickshotSubState("Quickshoting");
    public static final MultiQuickshotSubState STANDBY = new MultiQuickshotSubState("Standby");
    public static final MultiQuickshotSubState WAITING_TO_CONFIRM = new MultiQuickshotSubState("WaitingToConfirm");

    public MultiQuickshotSubState(String simpleName) {
        super(simpleName);
        this.prefix = MultiQuickshotSubState.class.getCanonicalName();
    }
}
