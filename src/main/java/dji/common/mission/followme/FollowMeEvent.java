package dji.common.mission.followme;

import dji.common.mission.MissionEvent;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FollowMeEvent extends MissionEvent {
    public static final FollowMeEvent GOT_STUCK = new FollowMeEvent("GOT_STUCK");

    public FollowMeEvent(String name) {
        super(name);
    }
}
