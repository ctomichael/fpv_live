package dji.component.accountcenter;

public class AccountCenterConfig {
    public static final String LOG_TAG = "AccountCenter";
    private static AccountDataAnalysisReporter daReporter;
    public static boolean isDebugAccountCenterOpenLog = false;
    public static boolean isDebugVerPhone = false;
    private static boolean isDpad = false;

    public static AccountDataAnalysisReporter getDataAnalysicReporter() {
        return daReporter;
    }

    public static void setDataAnalysicReporter(AccountDataAnalysisReporter daReporter2) {
        daReporter = daReporter2;
    }

    public static boolean isDpad() {
        return isDpad;
    }
}
