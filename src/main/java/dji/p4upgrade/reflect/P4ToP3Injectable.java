package dji.p4upgrade.reflect;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface P4ToP3Injectable {
    String getAircraftVersion();

    String getDJIGlobalService_flycsn();

    String getRcVersion();
}
