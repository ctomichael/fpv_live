package dji.thirdparty.rx.exceptions;

public final class UnsubscribeFailedException extends RuntimeException {
    private static final long serialVersionUID = 4594672310593167598L;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UnsubscribeFailedException(Throwable throwable) {
        super(throwable == null ? new NullPointerException() : throwable);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UnsubscribeFailedException(String message, Throwable throwable) {
        super(message, throwable == null ? new NullPointerException() : throwable);
    }
}
