package dji.component.accountcenter.response;

public enum AccountCenterSmsType {
    BIND(1),
    REGISTER(2),
    RESET_PW(3);
    
    public String id;

    private AccountCenterSmsType(int i) {
        this.id = i + "";
    }
}
