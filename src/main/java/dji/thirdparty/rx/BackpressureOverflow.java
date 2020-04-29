package dji.thirdparty.rx;

import dji.thirdparty.rx.annotations.Experimental;
import dji.thirdparty.rx.exceptions.MissingBackpressureException;

@Experimental
public final class BackpressureOverflow {
    public static final Strategy ON_OVERFLOW_DEFAULT = Error.INSTANCE;
    public static final Strategy ON_OVERFLOW_DROP_LATEST = DropLatest.INSTANCE;
    public static final Strategy ON_OVERFLOW_DROP_OLDEST = DropOldest.INSTANCE;
    public static final Strategy ON_OVERFLOW_ERROR = Error.INSTANCE;

    public interface Strategy {
        boolean mayAttemptDrop() throws MissingBackpressureException;
    }

    static class DropOldest implements Strategy {
        static final DropOldest INSTANCE = new DropOldest();

        private DropOldest() {
        }

        public boolean mayAttemptDrop() {
            return true;
        }
    }

    static class DropLatest implements Strategy {
        static final DropLatest INSTANCE = new DropLatest();

        private DropLatest() {
        }

        public boolean mayAttemptDrop() {
            return false;
        }
    }

    static class Error implements Strategy {
        static final Error INSTANCE = new Error();

        private Error() {
        }

        public boolean mayAttemptDrop() throws MissingBackpressureException {
            throw new MissingBackpressureException("Overflowed buffer");
        }
    }
}
