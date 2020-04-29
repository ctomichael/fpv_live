package dji.component.privacy;

public class PrivacyDataWrapper {
    public boolean isAuthorized;
    public PrivacyType mPrivacyType;

    public PrivacyDataWrapper(PrivacyType type, boolean authorizeState) {
        this.mPrivacyType = type;
        this.isAuthorized = authorizeState;
    }
}
