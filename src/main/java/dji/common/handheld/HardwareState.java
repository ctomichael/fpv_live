package dji.common.handheld;

import dji.common.Stick;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class HardwareState {
    private final boolean isModeButtonBeingPressed;
    private final boolean isTriggerBeingPressed;
    private final HandheldButtonClickEvent modeButton;
    private final RecordAndShutterButtons recordAndShutterButtons;
    private final Stick stick;
    private final StickHorizontalDirection stickHorizontalDirection;
    private final StickVerticalDirection stickVerticalDirection;
    private final HandheldButtonClickEvent triggerButton;
    private final ZoomState zoomState;

    public interface Callback {
        void onUpdate(HardwareState hardwareState);
    }

    private HardwareState(Builder builder) {
        this.recordAndShutterButtons = builder.recordAndShutterButtons;
        this.triggerButton = builder.triggerButton;
        this.modeButton = builder.modeButton;
        this.stickVerticalDirection = builder.stickVerticalDirection;
        this.stickHorizontalDirection = builder.stickHorizontalDirection;
        this.stick = builder.stick;
        this.isTriggerBeingPressed = builder.isTriggerBeingPressed;
        this.isModeButtonBeingPressed = builder.isModeButtonBeingPressed;
        this.zoomState = builder.zoomState;
    }

    public RecordAndShutterButtons getRecordAndShutterButtons() {
        return this.recordAndShutterButtons;
    }

    public HandheldButtonClickEvent getTriggerButton() {
        return this.triggerButton;
    }

    public HandheldButtonClickEvent getModeButton() {
        return this.modeButton;
    }

    public StickVerticalDirection getStickVerticalDirection() {
        return this.stickVerticalDirection;
    }

    public StickHorizontalDirection getStickHorizontalDirection() {
        return this.stickHorizontalDirection;
    }

    public boolean isTriggerBeingPressed() {
        return this.isTriggerBeingPressed;
    }

    public boolean isModeButtonBeingPressed() {
        return this.isModeButtonBeingPressed;
    }

    public Stick getStick() {
        return this.stick;
    }

    public ZoomState getZoomState() {
        return this.zoomState;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HardwareState hardwareState = (HardwareState) o;
        if (getRecordAndShutterButtons() != hardwareState.getRecordAndShutterButtons() || getTriggerButton() != hardwareState.getTriggerButton() || getModeButton() != hardwareState.getModeButton() || getStickVerticalDirection() != hardwareState.getStickVerticalDirection() || getStickHorizontalDirection() != hardwareState.getStickHorizontalDirection() || getZoomState() != hardwareState.getZoomState() || !getStick().equals(hardwareState.getStick())) {
            return false;
        }
        if (!(isTriggerBeingPressed() == hardwareState.isTriggerBeingPressed() && isModeButtonBeingPressed() == hardwareState.isModeButtonBeingPressed())) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8 = 0;
        if (getRecordAndShutterButtons() != null) {
            result = getRecordAndShutterButtons().hashCode();
        } else {
            result = 0;
        }
        int i9 = result * 31;
        if (getTriggerButton() != null) {
            i = getTriggerButton().hashCode();
        } else {
            i = 0;
        }
        int i10 = (i9 + i) * 31;
        if (getModeButton() != null) {
            i2 = getModeButton().hashCode();
        } else {
            i2 = 0;
        }
        int i11 = (i10 + i2) * 31;
        if (getStickVerticalDirection() != null) {
            i3 = getStickVerticalDirection().hashCode();
        } else {
            i3 = 0;
        }
        int i12 = (i11 + i3) * 31;
        if (getStickHorizontalDirection() != null) {
            i4 = getStickHorizontalDirection().hashCode();
        } else {
            i4 = 0;
        }
        int i13 = (i12 + i4) * 31;
        if (getZoomState() != null) {
            i5 = getZoomState().hashCode();
        } else {
            i5 = 0;
        }
        int i14 = (i13 + i5) * 31;
        if (getStick() != null) {
            i6 = getStick().hashCode();
        } else {
            i6 = 0;
        }
        int i15 = (i14 + i6) * 31;
        if (isTriggerBeingPressed()) {
            i7 = 0;
        } else {
            i7 = 1;
        }
        int i16 = (i15 + i7) * 31;
        if (!isModeButtonBeingPressed()) {
            i8 = 1;
        }
        return i16 + i8;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean isModeButtonBeingPressed;
        /* access modifiers changed from: private */
        public boolean isTriggerBeingPressed;
        /* access modifiers changed from: private */
        public HandheldButtonClickEvent modeButton = HandheldButtonClickEvent.IDLE;
        /* access modifiers changed from: private */
        public RecordAndShutterButtons recordAndShutterButtons;
        /* access modifiers changed from: private */
        public Stick stick = new Stick(0, 0);
        /* access modifiers changed from: private */
        public StickHorizontalDirection stickHorizontalDirection;
        /* access modifiers changed from: private */
        public StickVerticalDirection stickVerticalDirection;
        /* access modifiers changed from: private */
        public HandheldButtonClickEvent triggerButton = HandheldButtonClickEvent.IDLE;
        /* access modifiers changed from: private */
        public ZoomState zoomState = ZoomState.IDLE;

        public Builder recordAndShutterButtons(RecordAndShutterButtons recordAndShutterButtons2) {
            this.recordAndShutterButtons = recordAndShutterButtons2;
            return this;
        }

        public Builder triggerButton(HandheldButtonClickEvent triggerButton2) {
            this.triggerButton = triggerButton2;
            return this;
        }

        public Builder modeButton(HandheldButtonClickEvent modeButton2) {
            this.modeButton = modeButton2;
            return this;
        }

        public Builder stickVerticalDirection(StickVerticalDirection stickVerticalDirection2) {
            this.stickVerticalDirection = stickVerticalDirection2;
            return this;
        }

        public Builder stickHorizontalDirection(StickHorizontalDirection stickHorizontalDirection2) {
            this.stickHorizontalDirection = stickHorizontalDirection2;
            return this;
        }

        public Builder stick(Stick stick2) {
            this.stick = stick2;
            return this;
        }

        public Builder zoom(ZoomState state) {
            this.zoomState = state;
            return this;
        }

        public Builder isTriggerBeingPressed(boolean isTriggerBeingPressed2) {
            this.isTriggerBeingPressed = isTriggerBeingPressed2;
            return this;
        }

        public Builder isModeButtonBeingPressed(boolean isModeButtonBeingPressed2) {
            this.isModeButtonBeingPressed = isModeButtonBeingPressed2;
            return this;
        }

        public HardwareState build() {
            return new HardwareState(this);
        }
    }
}
