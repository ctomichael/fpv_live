package dji.internal.mission.abstraction.followme;

import dji.common.mission.MissionEvent;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;

@EXClassNullAway
public class FollowMeAbstractionDataHolder extends AbstractionDataHolder {
    public FollowMeAbstractionDataHolder(AbstractionDataHolder.Builder builder) {
        super(builder);
    }

    public static class FollowMeBuilder extends AbstractionDataHolder.Builder {
        public FollowMeBuilder() {
        }

        public FollowMeBuilder(MissionEvent event) {
            super(event);
        }

        public FollowMeAbstractionDataHolder build() {
            return new FollowMeAbstractionDataHolder(this);
        }
    }
}
