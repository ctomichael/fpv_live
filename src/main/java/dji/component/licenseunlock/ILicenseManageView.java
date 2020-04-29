package dji.component.licenseunlock;

public interface ILicenseManageView {

    public interface OnLicenseManageViewActionListener {
        void onViewClose();
    }

    void setOnLicenseManageViewActionListener(OnLicenseManageViewActionListener onLicenseManageViewActionListener);
}
