package dji.internal.mission.abstraction.intelligenthotpoint;

import dji.common.mission.MissionEvent;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;

@EXClassNullAway
public class IntelligentHotpointAbstractionDataHolder extends AbstractionDataHolder {
    public IntelligentHotpointAbstractionDataHolder(AbstractionDataHolder.Builder builder) {
        super(builder);
    }

    public static class HotpointBuilder extends AbstractionDataHolder.Builder {
        public HotpointBuilder() {
        }

        public HotpointBuilder(MissionEvent event) {
            super(event);
        }

        public IntelligentHotpointAbstractionDataHolder build() {
            return new IntelligentHotpointAbstractionDataHolder(this);
        }
    }
}
