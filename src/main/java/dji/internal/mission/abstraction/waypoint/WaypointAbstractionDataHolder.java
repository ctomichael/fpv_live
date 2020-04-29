package dji.internal.mission.abstraction.waypoint;

import dji.common.mission.MissionEvent;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;

@EXClassNullAway
public class WaypointAbstractionDataHolder extends AbstractionDataHolder {
    public WaypointAbstractionDataHolder(AbstractionDataHolder.Builder builder) {
        super(builder);
    }

    public static class WaypointBuilder extends AbstractionDataHolder.Builder {
        public WaypointBuilder() {
        }

        public WaypointBuilder(MissionEvent event) {
            this.event = event;
        }

        public WaypointAbstractionDataHolder build() {
            if (this.currentState != null) {
                return new WaypointAbstractionDataHolder(this);
            }
            throw new RuntimeException("Current State cannot be null!");
        }
    }
}
