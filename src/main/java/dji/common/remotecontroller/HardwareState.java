package dji.common.remotecontroller;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import dji.common.Stick;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.sdksharedlib.util.abstractions.DJIRCAbstractionUtil;

@EXClassNullAway
public class HardwareState {
    private Button c1Button;
    private Button c2Button;
    private Button c3Button;
    private FiveDButton fiveDButton;
    private FlightModeSwitch flightModeSwitch;
    private Button functionButton;
    private Button goHomeButton;
    private Stick left;
    private int leftWheel;
    private Button menuButton;
    private Button pauseButton;
    private Button playbackButton;
    private Button recordButton;
    private Stick right;
    private RightWheel rightWheel;
    private Button shutterButton;
    private TransformationSwitch transformationSwitch;

    public interface HardwareStateCallback {
        void onUpdate(@NonNull HardwareState hardwareState);
    }

    private HardwareState(Builder builder) {
        this.left = builder.left;
        this.right = builder.right;
        this.leftWheel = builder.leftWheel;
        this.rightWheel = builder.rightWheel;
        this.transformationSwitch = builder.transformationSwitch;
        this.flightModeSwitch = builder.flightModeSwitch;
        this.goHomeButton = builder.goHomeButton;
        this.recordButton = builder.recordButton;
        this.shutterButton = builder.shutterButton;
        this.playbackButton = builder.playbackButton;
        this.pauseButton = builder.pauseButton;
        this.c1Button = builder.c1Button;
        this.c2Button = builder.c2Button;
        this.c3Button = builder.c3Button;
        this.functionButton = builder.functionButton;
        this.fiveDButton = builder.fiveDButton;
        this.menuButton = builder.menuButton;
    }

    public Stick getLeftStick() {
        return this.left;
    }

    public Stick getRightStick() {
        return this.right;
    }

    @IntRange(from = -660, to = 660)
    public int getLeftWheel() {
        return this.leftWheel;
    }

    public RightWheel getRightWheel() {
        return this.rightWheel;
    }

    public TransformationSwitch getTransformationSwitch() {
        return this.transformationSwitch;
    }

    public FlightModeSwitch getFlightModeSwitch() {
        return this.flightModeSwitch;
    }

    public Button getGoHomeButton() {
        return this.goHomeButton;
    }

    public Button getRecordButton() {
        return this.recordButton;
    }

    public Button getShutterButton() {
        return this.shutterButton;
    }

    public Button getPlaybackButton() {
        return this.playbackButton;
    }

    public Button getPauseButton() {
        return this.pauseButton;
    }

    public Button getC1Button() {
        return this.c1Button;
    }

    public Button getC2Button() {
        return this.c2Button;
    }

    public Button getC3Button() {
        return this.c3Button;
    }

    public Button getFunctionButton() {
        return this.functionButton;
    }

    public FiveDButton getFiveDButton() {
        return this.fiveDButton;
    }

    public Button getMenuButton() {
        return this.menuButton;
    }

    public void setLeft(Stick left2) {
        this.left = left2;
    }

    public void setRight(Stick right2) {
        this.right = right2;
    }

    public void setLeftWheel(@IntRange(from = -660, to = 660) int leftWheel2) {
        this.leftWheel = leftWheel2;
    }

    public void setRightWheel(RightWheel rightWheel2) {
        this.rightWheel = rightWheel2;
    }

    public void setTransformationSwitch(TransformationSwitch transformationSwitch2) {
        this.transformationSwitch = transformationSwitch2;
    }

    public void setFlightModeSwitch(FlightModeSwitch flightModeSwitch2) {
        this.flightModeSwitch = flightModeSwitch2;
    }

    public void setGoHomeButton(Button goHomeButton2) {
        this.goHomeButton = goHomeButton2;
    }

    public void setRecordButton(Button recordButton2) {
        this.recordButton = recordButton2;
    }

    public void setShutterButton(Button shutterButton2) {
        this.shutterButton = shutterButton2;
    }

    public void setPlaybackButton(Button playbackButton2) {
        this.playbackButton = playbackButton2;
    }

    public void setPauseButton(Button pauseButton2) {
        this.pauseButton = pauseButton2;
    }

    public void setC1Button(Button c1Button2) {
        this.c1Button = c1Button2;
    }

    public void setC2Button(Button c2Button2) {
        this.c2Button = c2Button2;
    }

    public void setC3Button(Button c3Button2) {
        this.c3Button = c3Button2;
    }

    public void setFiveDButton(FiveDButton fiveDButton2) {
        this.fiveDButton = fiveDButton2;
    }

    public void setMenuButton(Button button) {
        this.menuButton = button;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof HardwareState)) {
            return false;
        }
        HardwareState that = (HardwareState) o;
        if (getLeftWheel() != that.getLeftWheel()) {
            return false;
        }
        if (this.left != null) {
            if (!this.left.equals(that.left)) {
                return false;
            }
        } else if (that.left != null) {
            return false;
        }
        if (this.right != null) {
            if (!this.right.equals(that.right)) {
                return false;
            }
        } else if (that.right != null) {
            return false;
        }
        if (getRightWheel() != null) {
            if (!getRightWheel().equals(that.getRightWheel())) {
                return false;
            }
        } else if (that.getRightWheel() != null) {
            return false;
        }
        if (getTransformationSwitch() != null) {
            if (!getTransformationSwitch().equals(that.getTransformationSwitch())) {
                return false;
            }
        } else if (that.getTransformationSwitch() != null) {
            return false;
        }
        if (getFlightModeSwitch() != that.getFlightModeSwitch()) {
            return false;
        }
        if (getGoHomeButton() != null) {
            if (!getGoHomeButton().equals(that.getGoHomeButton())) {
                return false;
            }
        } else if (that.getGoHomeButton() != null) {
            return false;
        }
        if (getRecordButton() != null) {
            if (!getRecordButton().equals(that.getRecordButton())) {
                return false;
            }
        } else if (that.getRecordButton() != null) {
            return false;
        }
        if (getShutterButton() != null) {
            if (!getShutterButton().equals(that.getShutterButton())) {
                return false;
            }
        } else if (that.getShutterButton() != null) {
            return false;
        }
        if (getPlaybackButton() != null) {
            if (!getPlaybackButton().equals(that.getPlaybackButton())) {
                return false;
            }
        } else if (that.getPlaybackButton() != null) {
            return false;
        }
        if (getPauseButton() != null) {
            if (!getPauseButton().equals(that.getPauseButton())) {
                return false;
            }
        } else if (that.getPauseButton() != null) {
            return false;
        }
        if (getC1Button() != null) {
            if (!getC1Button().equals(that.getC1Button())) {
                return false;
            }
        } else if (that.getC1Button() != null) {
            return false;
        }
        if (getC2Button() != null) {
            if (!getC2Button().equals(that.getC2Button())) {
                return false;
            }
        } else if (that.getC2Button() != null) {
            return false;
        }
        if (getMenuButton() != null) {
            if (!getMenuButton().equals(that.getMenuButton())) {
                return false;
            }
        } else if (that.getMenuButton() != null) {
            return false;
        }
        if (getC3Button() != null) {
            if (!getC3Button().equals(that.getC3Button())) {
                return false;
            }
        } else if (that.getC3Button() != null) {
            return false;
        }
        if (getFunctionButton() != null) {
            if (!getFunctionButton().equals(that.getFunctionButton())) {
                return false;
            }
        } else if (that.getFunctionButton() != null) {
            return false;
        }
        if (getFiveDButton() != null) {
            z = getFiveDButton().equals(that.getFiveDButton());
        } else if (that.getFiveDButton() != null) {
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
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15 = 0;
        if (this.left != null) {
            result = this.left.hashCode();
        } else {
            result = 0;
        }
        int i16 = result * 31;
        if (this.right != null) {
            i = this.right.hashCode();
        } else {
            i = 0;
        }
        int leftWheel2 = (((i16 + i) * 31) + getLeftWheel()) * 31;
        if (getRightWheel() != null) {
            i2 = getRightWheel().hashCode();
        } else {
            i2 = 0;
        }
        int i17 = (leftWheel2 + i2) * 31;
        if (getTransformationSwitch() != null) {
            i3 = getTransformationSwitch().hashCode();
        } else {
            i3 = 0;
        }
        int i18 = (i17 + i3) * 31;
        if (getFlightModeSwitch() != null) {
            i4 = getFlightModeSwitch().hashCode();
        } else {
            i4 = 0;
        }
        int i19 = (i18 + i4) * 31;
        if (getGoHomeButton() != null) {
            i5 = getGoHomeButton().hashCode();
        } else {
            i5 = 0;
        }
        int i20 = (i19 + i5) * 31;
        if (getRecordButton() != null) {
            i6 = getRecordButton().hashCode();
        } else {
            i6 = 0;
        }
        int i21 = (i20 + i6) * 31;
        if (getShutterButton() != null) {
            i7 = getShutterButton().hashCode();
        } else {
            i7 = 0;
        }
        int i22 = (i21 + i7) * 31;
        if (getPlaybackButton() != null) {
            i8 = getPlaybackButton().hashCode();
        } else {
            i8 = 0;
        }
        int i23 = (i22 + i8) * 31;
        if (getPauseButton() != null) {
            i9 = getPauseButton().hashCode();
        } else {
            i9 = 0;
        }
        int i24 = (i23 + i9) * 31;
        if (getC1Button() != null) {
            i10 = getC1Button().hashCode();
        } else {
            i10 = 0;
        }
        int i25 = (i24 + i10) * 31;
        if (getC2Button() != null) {
            i11 = getC2Button().hashCode();
        } else {
            i11 = 0;
        }
        int i26 = (i25 + i11) * 31;
        if (getC3Button() != null) {
            i12 = getC3Button().hashCode();
        } else {
            i12 = 0;
        }
        int i27 = (i26 + i12) * 31;
        if (getFunctionButton() != null) {
            i13 = getFunctionButton().hashCode();
        } else {
            i13 = 0;
        }
        int i28 = (i27 + i13) * 31;
        if (getFiveDButton() != null) {
            i14 = getFiveDButton().hashCode();
        } else {
            i14 = 0;
        }
        int i29 = (i28 + i14) * 31;
        if (getMenuButton() != null) {
            i15 = getMenuButton().hashCode();
        }
        return i29 + i15;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public Button c1Button = new Button(false, false);
        /* access modifiers changed from: private */
        public Button c2Button = new Button(false, false);
        /* access modifiers changed from: private */
        public Button c3Button = new Button(false, false);
        /* access modifiers changed from: private */
        public FiveDButton fiveDButton = new FiveDButton(false);
        /* access modifiers changed from: private */
        public FlightModeSwitch flightModeSwitch;
        /* access modifiers changed from: private */
        public Button functionButton = new Button(false, false);
        /* access modifiers changed from: private */
        public Button goHomeButton = new Button(false, false);
        /* access modifiers changed from: private */
        public Stick left;
        /* access modifiers changed from: private */
        public int leftWheel;
        /* access modifiers changed from: private */
        public Button menuButton = new Button(false, false);
        /* access modifiers changed from: private */
        public Button pauseButton = new Button(false, false);
        /* access modifiers changed from: private */
        public Button playbackButton = new Button(false, false);
        /* access modifiers changed from: private */
        public Button recordButton = new Button(false, false);
        /* access modifiers changed from: private */
        public Stick right;
        /* access modifiers changed from: private */
        public RightWheel rightWheel = new RightWheel();
        /* access modifiers changed from: private */
        public Button shutterButton = new Button(false, false);
        /* access modifiers changed from: private */
        public TransformationSwitch transformationSwitch = new TransformationSwitch();

        public Builder leftStick(Stick left2) {
            this.left = left2;
            return this;
        }

        public Builder rightStick(Stick right2) {
            this.right = right2;
            return this;
        }

        public Builder leftWheel(@IntRange(from = -660, to = 660) int leftWheel2) {
            this.leftWheel = leftWheel2;
            return this;
        }

        public Builder rightWheel(RightWheel rightWheel2) {
            this.rightWheel = rightWheel2;
            return this;
        }

        public Builder transformationSwitch(TransformationSwitch transformationSwitch2) {
            this.transformationSwitch = transformationSwitch2;
            return this;
        }

        public Builder flightModeSwitch(FlightModeSwitch flightModeSwitch2) {
            this.flightModeSwitch = flightModeSwitch2;
            return this;
        }

        public Builder goHomeButton(Button goHomeButton2) {
            this.goHomeButton = goHomeButton2;
            return this;
        }

        public Builder recordButton(Button recordButton2) {
            this.recordButton = recordButton2;
            return this;
        }

        public Builder pauseButton(Button pauseButton2) {
            this.pauseButton = pauseButton2;
            return this;
        }

        public Builder shutterButton(Button shutterButton2) {
            this.shutterButton = shutterButton2;
            return this;
        }

        public Builder playbackButton(Button playbackButton2) {
            this.playbackButton = playbackButton2;
            return this;
        }

        public Builder c1Button(Button c1Button2) {
            this.c1Button = c1Button2;
            return this;
        }

        public Builder c2Button(Button c2Button2) {
            this.c2Button = c2Button2;
            return this;
        }

        public Builder c3Button(Button c3Button2) {
            this.c3Button = c3Button2;
            return this;
        }

        public Builder functionButton(Button functionButton2) {
            this.functionButton = functionButton2;
            return this;
        }

        public Builder fiveDButton(FiveDButton fiveDButton2) {
            this.fiveDButton = fiveDButton2;
            return this;
        }

        public FiveDButton getFiveDButton() {
            return this.fiveDButton;
        }

        public Builder menuButton(Button menuButton2) {
            this.menuButton = menuButton2;
            return this;
        }

        public HardwareState build() {
            return new HardwareState(this);
        }
    }

    public static class RightWheel extends Button {
        private boolean isTurned;
        private int value;

        public RightWheel() {
        }

        public RightWheel(boolean isTurned2, boolean isClicked, @IntRange(from = -1320, to = 1320) int value2) {
            super(true, isClicked);
            this.isTurned = isTurned2;
            this.value = value2;
        }

        public RightWheel(boolean present, boolean isTurned2, boolean isClicked, @IntRange(from = -1320, to = 1320) int value2) {
            super(present, isClicked);
            this.isTurned = isTurned2;
            this.value = value2;
        }

        public boolean isTurned() {
            return this.isTurned;
        }

        public int getValue() {
            return this.value;
        }

        public String toString() {
            return "wheel changed: " + this.isTurned + "\nwheel button down: " + super.isClicked() + "\nwheel offset: " + this.value;
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass() || !super.equals(o)) {
                return false;
            }
            RightWheel that = (RightWheel) o;
            if (this.isTurned != that.isTurned) {
                return false;
            }
            if (this.value != that.value) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return (((super.hashCode() * 31) + (this.isTurned ? 1 : 0)) * 31) + this.value;
        }
    }

    public static class TransformationSwitch {
        private boolean isPresent;
        private State state;

        public TransformationSwitch() {
        }

        public TransformationSwitch(State state2) {
            this.state = state2;
        }

        public TransformationSwitch(boolean isPresent2, State state2) {
            this.isPresent = isPresent2;
            this.state = state2;
        }

        public TransformationSwitch setPresent(boolean isPresent2) {
            this.isPresent = isPresent2;
            return this;
        }

        public void setState(State state2) {
            this.state = state2;
        }

        public boolean isPresent() {
            return this.isPresent;
        }

        public State getState() {
            return this.state;
        }

        public enum State {
            RETRACT(0),
            DEPLOY(1);
            
            private int value;

            private State(int value2) {
                this.value = value2;
            }

            public int value() {
                return this.value;
            }

            private boolean _equals(int b) {
                return this.value == b;
            }

            public static State find(int value2) {
                State result = RETRACT;
                for (int i = 0; i < values().length; i++) {
                    if (values()[i]._equals(value2)) {
                        return values()[i];
                    }
                }
                return result;
            }
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TransformationSwitch that = (TransformationSwitch) o;
            if (this.isPresent != that.isPresent) {
                return false;
            }
            if (this.state != that.state) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int result;
            int i = 0;
            if (this.isPresent) {
                result = 1;
            } else {
                result = 0;
            }
            int i2 = result * 31;
            if (this.state != null) {
                i = this.state.hashCode();
            }
            return i2 + i;
        }
    }

    public static class Button {
        private boolean clicked;
        private boolean present;

        public Button() {
        }

        public Button(boolean present2, boolean clicked2) {
            this.present = present2;
            this.clicked = clicked2;
        }

        public boolean isPresent() {
            return this.present;
        }

        public boolean isClicked() {
            return this.clicked;
        }

        public void setPresent(boolean present2) {
            this.present = present2;
        }

        public void setClicked(boolean clicked2) {
            this.clicked = clicked2;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Button button = (Button) o;
            if (this.present != button.present) {
                return false;
            }
            if (this.clicked != button.clicked) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int result;
            int i = 1;
            if (this.present) {
                result = 1;
            } else {
                result = 0;
            }
            int i2 = result * 31;
            if (!this.clicked) {
                i = 0;
            }
            return i2 + i;
        }
    }

    public enum FiveDButtonDirection {
        MIDDLE(0),
        POSITIVE(1),
        NEGATIVE(2);
        
        private int value;

        private FiveDButtonDirection(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        public boolean _equals(int b) {
            return this.value == b;
        }

        public static FiveDButtonDirection find(int value2) {
            FiveDButtonDirection result = MIDDLE;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public static class FiveDButton extends Button {
        private boolean clicked;
        private FiveDButtonDirection horizontalDirection;
        private boolean present;
        private FiveDButtonDirection verticalDirection;

        public FiveDButton(boolean present2, boolean clicked2) {
            super(present2, clicked2);
            this.present = present2;
            this.clicked = clicked2;
        }

        public FiveDButton(boolean present2) {
            this.present = present2;
        }

        public FiveDButton(boolean clicked2, boolean present2, FiveDButtonDirection verticalDirection2, FiveDButtonDirection horizontalDirection2) {
            this.clicked = clicked2;
            this.present = present2;
            this.verticalDirection = verticalDirection2;
            this.horizontalDirection = horizontalDirection2;
        }

        public boolean isPresent() {
            return this.present;
        }

        public boolean isClicked() {
            return this.clicked;
        }

        public void setPresent(boolean present2) {
            this.present = present2;
        }

        public void setClicked(boolean clicked2) {
            this.clicked = clicked2;
        }

        public void setVerticalDirection(FiveDButtonDirection verticalDirection2) {
            this.verticalDirection = verticalDirection2;
        }

        public void setHorizontalDirection(FiveDButtonDirection horizontalDirection2) {
            this.horizontalDirection = horizontalDirection2;
        }

        public FiveDButtonDirection getVerticalDirection() {
            return this.verticalDirection;
        }

        public FiveDButtonDirection getHorizontalDirection() {
            return this.horizontalDirection;
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            FiveDButton that = (FiveDButton) o;
            if (this.present != that.present || this.clicked != that.clicked || this.verticalDirection != that.verticalDirection) {
                return false;
            }
            if (this.horizontalDirection != that.horizontalDirection) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int result;
            int i;
            int i2 = 1;
            int i3 = 0;
            if (this.present) {
                result = 1;
            } else {
                result = 0;
            }
            int i4 = result * 31;
            if (!this.clicked) {
                i2 = 0;
            }
            int i5 = (i4 + i2) * 31;
            if (this.verticalDirection != null) {
                i = this.verticalDirection.hashCode();
            } else {
                i = 0;
            }
            int i6 = (i5 + i) * 31;
            if (this.horizontalDirection != null) {
                i3 = this.horizontalDirection.hashCode();
            }
            return i6 + i3;
        }
    }

    public enum FlightModeSwitch {
        POSITION_ONE(0),
        POSITION_TWO(1),
        POSITION_THREE(2);
        
        private int value;

        private FlightModeSwitch(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        public boolean _equals(int b) {
            return this.value == b;
        }

        public static FlightModeSwitch find(ProductType productType, int value2) {
            if (DJIRCAbstractionUtil.isRemoteControllerHaveTwoSwitchMode(productType)) {
                switch (value2) {
                    case 0:
                        return POSITION_ONE;
                    case 1:
                        return POSITION_TWO;
                }
            } else {
                switch (value2) {
                    case 0:
                        return POSITION_TWO;
                    case 1:
                        return POSITION_THREE;
                    case 2:
                        return POSITION_ONE;
                }
            }
            return POSITION_ONE;
        }
    }
}
