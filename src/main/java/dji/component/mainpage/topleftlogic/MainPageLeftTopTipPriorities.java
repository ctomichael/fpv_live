package dji.component.mainpage.topleftlogic;

public class MainPageLeftTopTipPriorities {
    public static final int PRIORITY_DB_UPGRADE = mInitialPriority;
    public static final int PRIORITY_DB_UPGRADE_FORCE;
    public static final int PRIORITY_DB_UPGRADING;
    public static final int PRIORITY_FIRMWARE_CONSISTENT;
    public static final int PRIORITY_FIRMWARE_UPGRADE;
    public static final int PRIORITY_FIRMWARE_UPGRADE_FORCE;
    public static final int PRIORITY_FIRMWARE_UPGRADING;
    private static int mInitialPriority;

    static {
        mInitialPriority = 0;
        int i = mInitialPriority + 1;
        mInitialPriority = i;
        PRIORITY_FIRMWARE_CONSISTENT = i;
        int i2 = mInitialPriority + 1;
        mInitialPriority = i2;
        PRIORITY_FIRMWARE_UPGRADE = i2;
        int i3 = mInitialPriority + 1;
        mInitialPriority = i3;
        PRIORITY_DB_UPGRADE_FORCE = i3;
        int i4 = mInitialPriority + 1;
        mInitialPriority = i4;
        PRIORITY_FIRMWARE_UPGRADE_FORCE = i4;
        int i5 = mInitialPriority + 1;
        mInitialPriority = i5;
        PRIORITY_DB_UPGRADING = i5;
        int i6 = mInitialPriority + 1;
        mInitialPriority = i6;
        PRIORITY_FIRMWARE_UPGRADING = i6;
    }
}
