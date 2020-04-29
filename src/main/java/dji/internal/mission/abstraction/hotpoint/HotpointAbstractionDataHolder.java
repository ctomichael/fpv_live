package dji.internal.mission.abstraction.hotpoint;

import dji.common.mission.MissionEvent;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;

@EXClassNullAway
public class HotpointAbstractionDataHolder extends AbstractionDataHolder {
    public HotpointAbstractionDataHolder(AbstractionDataHolder.Builder builder) {
        super(builder);
    }

    public static class HotpointBuilder extends AbstractionDataHolder.Builder {
        public HotpointBuilder() {
        }

        public HotpointBuilder(MissionEvent event) {
            super(event);
        }

        public HotpointAbstractionDataHolder build() {
            return new HotpointAbstractionDataHolder(this);
        }
    }
}
