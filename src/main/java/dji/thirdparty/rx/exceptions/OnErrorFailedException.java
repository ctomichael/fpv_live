package dji.thirdparty.rx.exceptions;

public class OnErrorFailedException extends RuntimeException {
    private static final long serialVersionUID = -419289748403337611L;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public OnErrorFailedException(String message, Throwable e) {
        super(message, e == null ? new NullPointerException() : e);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public OnErrorFailedException(Throwable e) {
        super(e != null ? e.getMessage() : null, e == null ? new NullPointerException() : e);
    }
}
