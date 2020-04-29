package dji.internal.mission.abstraction;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.common.mission.MissionEvent;
import dji.common.mission.MissionState;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class AbstractionDataHolder {
    @NonNull
    private final MissionState currentState;
    @Nullable
    private final DJIError error;
    @Nullable
    private final MissionEvent event;
    @Nullable
    private final Object extra;
    @Nullable
    private final MissionState previousState;

    public AbstractionDataHolder(Builder builder) {
        this.previousState = builder.previousState;
        this.currentState = builder.currentState;
        this.extra = builder.extra;
        this.error = builder.error;
        this.event = builder.event;
    }

    @Nullable
    public MissionState getPreviousState() {
        return this.previousState;
    }

    @NonNull
    public MissionState getCurrentState() {
        return this.currentState;
    }

    @Nullable
    public MissionEvent getEvent() {
        return this.event;
    }

    @Nullable
    public Object getExtra() {
        return this.extra;
    }

    @Nullable
    public DJIError getError() {
        return this.error;
    }

    public static class Builder {
        @NonNull
        protected MissionState currentState;
        @Nullable
        protected DJIError error;
        @Nullable
        protected MissionEvent event;
        @Nullable
        protected Object extra;
        @Nullable
        protected MissionState previousState;

        public Builder(MissionEvent event2) {
            this.event = event2;
        }

        public Builder() {
        }

        public Builder previousState(MissionState previousState2) {
            this.previousState = previousState2;
            return this;
        }

        public Builder currentState(@NonNull MissionState currentState2) {
            if (currentState2 == null) {
                throw new RuntimeException("CurrentState can't be NULL!");
            }
            this.currentState = currentState2;
            return this;
        }

        public Builder extra(Object extra2) {
            this.extra = extra2;
            return this;
        }

        public Builder error(DJIError error2) {
            this.error = error2;
            return this;
        }

        public Builder event(MissionEvent event2) {
            this.event = event2;
            return this;
        }

        public AbstractionDataHolder build() {
            if (this.currentState != null) {
                return new AbstractionDataHolder(this);
            }
            throw new RuntimeException("Current State cannot be null!");
        }
    }

    public boolean equals(Object o) {
        boolean isPreviousStateEqual;
        boolean isCurrentStateEqual;
        boolean isExtraEqual;
        boolean isErrorEqual;
        boolean isEventEqual;
        if (this == o) {
            return true;
        }
        boolean isTypeEqual = o instanceof AbstractionDataHolder;
        if (!(this.previousState == null && ((AbstractionDataHolder) o).previousState == null) && (this.previousState == null || ((AbstractionDataHolder) o).previousState == null || !this.previousState.equals(((AbstractionDataHolder) o).getPreviousState()))) {
            isPreviousStateEqual = false;
        } else {
            isPreviousStateEqual = true;
        }
        if (!(this.currentState == null && ((AbstractionDataHolder) o).currentState == null) && (this.currentState == null || ((AbstractionDataHolder) o).currentState == null || !this.currentState.equals(((AbstractionDataHolder) o).getCurrentState()))) {
            isCurrentStateEqual = false;
        } else {
            isCurrentStateEqual = true;
        }
        if (!(this.extra == null && ((AbstractionDataHolder) o).extra == null) && (this.extra == null || ((AbstractionDataHolder) o).extra == null || !this.extra.equals(((AbstractionDataHolder) o).extra))) {
            isExtraEqual = false;
        } else {
            isExtraEqual = true;
        }
        if (!(this.error == null && ((AbstractionDataHolder) o).error == null) && (this.error == null || ((AbstractionDataHolder) o).error == null || !this.error.equals(((AbstractionDataHolder) o).error))) {
            isErrorEqual = false;
        } else {
            isErrorEqual = true;
        }
        if (!(this.event == null && ((AbstractionDataHolder) o).event == null) && (this.event == null || ((AbstractionDataHolder) o).event == null || !this.event.equals(((AbstractionDataHolder) o).event))) {
            isEventEqual = false;
        } else {
            isEventEqual = true;
        }
        if (!isTypeEqual || !isPreviousStateEqual || !isCurrentStateEqual || !isExtraEqual || !isErrorEqual || !isEventEqual) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((((((this.previousState == null ? 0 : this.previousState.hashCode()) + 31) * 31) + (this.extra == null ? 0 : this.extra.hashCode())) * 31) + (this.error == null ? 0 : this.error.hashCode())) * 31;
        if (this.event != null) {
            i = this.event.hashCode();
        }
        return hashCode + i + this.currentState.hashCode();
    }
}
