package dji.internal.mission.abstraction.tapfly;

import dji.common.mission.MissionEvent;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;

@EXClassNullAway
public class TapFlyAbstractionDataHolder extends AbstractionDataHolder {
    public TapFlyAbstractionDataHolder(AbstractionDataHolder.Builder builder) {
        super(builder);
    }

    public static class TapFlyBuilder extends AbstractionDataHolder.Builder {
        public TapFlyBuilder() {
        }

        public TapFlyBuilder(MissionEvent event) {
            super(event);
        }

        public TapFlyAbstractionDataHolder build() {
            if (this.currentState != null) {
                return new TapFlyAbstractionDataHolder(this);
            }
            throw new RuntimeException("Current State cannot be null!");
        }
    }
}
