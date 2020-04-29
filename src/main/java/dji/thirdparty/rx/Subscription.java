package dji.thirdparty.rx;

public interface Subscription {
    boolean isUnsubscribed();

    void unsubscribe();
}
