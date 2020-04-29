package dji.component.motorlock.model;

public interface LockKey {
    public static final String DATABASE_UPGRADE_LOCK = "database";
    public static final String FIRMWARE_UPGRADE_LOCK = "firmware";

    String getKey();

    boolean valid();
}
