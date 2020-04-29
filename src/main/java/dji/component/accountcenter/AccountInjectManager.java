package dji.component.accountcenter;

public class AccountInjectManager {
    private static AccountInjectManager instance = null;
    private AccountToP3Injectable accountToP3Injectable;

    public static AccountInjectManager getInstance() {
        if (instance == null) {
            instance = new AccountInjectManager();
        }
        return instance;
    }

    private AccountInjectManager() {
    }

    public static AccountToP3Injectable getAccountToP3Injectable() {
        return getInstance().accountToP3Injectable;
    }

    public static void setAccountToP3Injectable(AccountToP3Injectable accountToP3Injectable2) {
        getInstance().accountToP3Injectable = accountToP3Injectable2;
    }
}
