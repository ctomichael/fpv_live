package dji.internal.mission.abstraction.panorama;

import dji.common.mission.MissionEvent;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;

@EXClassNullAway
public class PanoramaAbstractionDataHolder extends AbstractionDataHolder {
    public PanoramaAbstractionDataHolder(AbstractionDataHolder.Builder builder) {
        super(builder);
    }

    public static class PanoramaBuilder extends AbstractionDataHolder.Builder {
        public PanoramaBuilder() {
        }

        public PanoramaBuilder(MissionEvent event) {
            super(event);
        }

        public PanoramaAbstractionDataHolder build() {
            return new PanoramaAbstractionDataHolder(this);
        }
    }
}
