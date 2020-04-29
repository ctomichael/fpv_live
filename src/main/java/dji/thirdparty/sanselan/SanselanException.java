package dji.thirdparty.sanselan;

public class SanselanException extends Exception {
    static final long serialVersionUID = -1;

    public SanselanException(String s) {
        super(s);
    }

    public SanselanException(String s, Exception e) {
        super(s, e);
    }
}
