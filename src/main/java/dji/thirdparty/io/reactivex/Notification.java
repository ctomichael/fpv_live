package dji.thirdparty.io.reactivex;

import dji.component.accountcenter.IMemberProtocol;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.util.NotificationLite;

public final class Notification<T> {
    static final Notification<Object> COMPLETE = new Notification<>(null);
    final Object value;

    private Notification(Object value2) {
        this.value = value2;
    }

    public boolean isOnComplete() {
        return this.value == null;
    }

    public boolean isOnError() {
        return NotificationLite.isError(this.value);
    }

    public boolean isOnNext() {
        Object o = this.value;
        return o != null && !NotificationLite.isError(o);
    }

    public T getValue() {
        Object o = this.value;
        if (o == null || NotificationLite.isError(o)) {
            return null;
        }
        return this.value;
    }

    public Throwable getError() {
        Object o = this.value;
        if (NotificationLite.isError(o)) {
            return NotificationLite.getError(o);
        }
        return null;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Notification) {
            return ObjectHelper.equals(this.value, ((Notification) obj).value);
        }
        return false;
    }

    public int hashCode() {
        Object o = this.value;
        if (o != null) {
            return o.hashCode();
        }
        return 0;
    }

    public String toString() {
        Object o = this.value;
        if (o == null) {
            return "OnCompleteNotification";
        }
        if (NotificationLite.isError(o)) {
            return "OnErrorNotification[" + NotificationLite.getError(o) + IMemberProtocol.STRING_SEPERATOR_RIGHT;
        }
        return "OnNextNotification[" + this.value + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }

    public static <T> Notification<T> createOnNext(T value2) {
        ObjectHelper.requireNonNull(value2, "value is null");
        return new Notification<>(value2);
    }

    public static <T> Notification<T> createOnError(Throwable error) {
        ObjectHelper.requireNonNull(error, "error is null");
        return new Notification<>(NotificationLite.error(error));
    }

    public static <T> Notification<T> createOnComplete() {
        return COMPLETE;
    }
}
