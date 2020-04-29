package dji.component.privacy;

public interface IPrivacyService {
    public static final String COMPONENT_NAME = "PrivacyService";

    void addPrivacyChangeListener(IPrivacyChangeListener iPrivacyChangeListener);

    void finishCheckPrivacyProcess();

    boolean getPrivacyDataState(PrivacyType privacyType);

    boolean getPrivacyDataState(PrivacyType privacyType, boolean z);

    boolean isNeedCheckPrivacy();

    void removePrivacyChangeListener(IPrivacyChangeListener iPrivacyChangeListener);

    void setPrivacyDataState(PrivacyType privacyType, boolean z);
}
