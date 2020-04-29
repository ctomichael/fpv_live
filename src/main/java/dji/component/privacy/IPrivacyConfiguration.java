package dji.component.privacy;

public interface IPrivacyConfiguration {

    public enum PrivacyConfigEvent {
        PRIVACY_CONFIG_FLOW_FINISH
    }

    public interface AuthorizeDialogClickListener {
        void onLeftCancelClick();

        void onRightAuthorizeClick();
    }
}
