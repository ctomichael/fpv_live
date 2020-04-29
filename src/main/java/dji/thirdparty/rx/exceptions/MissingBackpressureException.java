package dji.thirdparty.rx.exceptions;

public class MissingBackpressureException extends Exception {
    private static final long serialVersionUID = 7250870679677032194L;

    public MissingBackpressureException() {
    }

    public MissingBackpressureException(String message) {
        super(message);
    }
}