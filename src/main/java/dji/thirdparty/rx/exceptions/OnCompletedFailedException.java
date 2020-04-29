package dji.thirdparty.rx.exceptions;

public final class OnCompletedFailedException extends RuntimeException {
    private static final long serialVersionUID = 8622579378868820554L;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public OnCompletedFailedException(Throwable throwable) {
        super(throwable == null ? new NullPointerException() : throwable);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public OnCompletedFailedException(String message, Throwable throwable) {
        super(message, throwable == null ? new NullPointerException() : throwable);
    }
}
