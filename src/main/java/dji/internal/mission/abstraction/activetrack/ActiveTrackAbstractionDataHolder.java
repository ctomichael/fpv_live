package dji.internal.mission.abstraction.activetrack;

import dji.common.mission.MissionEvent;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;

@EXClassNullAway
public class ActiveTrackAbstractionDataHolder extends AbstractionDataHolder {
    public ActiveTrackAbstractionDataHolder(AbstractionDataHolder.Builder builder) {
        super(builder);
    }

    public static class ActiveTrackBuilder extends AbstractionDataHolder.Builder {
        public ActiveTrackBuilder() {
        }

        public ActiveTrackBuilder(MissionEvent event) {
            super(event);
        }

        public ActiveTrackAbstractionDataHolder build() {
            if (this.currentState != null) {
                return new ActiveTrackAbstractionDataHolder(this);
            }
            throw new RuntimeException("Current State cannot be null!");
        }
    }
}
