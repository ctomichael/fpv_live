package org.greenrobot.eventbus.meta;

import org.greenrobot.eventbus.EventBusException;
import org.greenrobot.eventbus.SubscriberMethod;
import org.greenrobot.eventbus.ThreadMode;

public abstract class AbstractSubscriberInfo implements SubscriberInfo {
    private final boolean shouldCheckSuperclass;
    private final Class subscriberClass;
    private final Class<? extends SubscriberInfo> superSubscriberInfoClass;

    protected AbstractSubscriberInfo(Class subscriberClass2, Class<? extends SubscriberInfo> superSubscriberInfoClass2, boolean shouldCheckSuperclass2) {
        this.subscriberClass = subscriberClass2;
        this.superSubscriberInfoClass = superSubscriberInfoClass2;
        this.shouldCheckSuperclass = shouldCheckSuperclass2;
    }

    public Class getSubscriberClass() {
        return this.subscriberClass;
    }

    public SubscriberInfo getSuperSubscriberInfo() {
        if (this.superSubscriberInfoClass == null) {
            return null;
        }
        try {
            return (SubscriberInfo) this.superSubscriberInfoClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean shouldCheckSuperclass() {
        return this.shouldCheckSuperclass;
    }

    /* access modifiers changed from: protected */
    public SubscriberMethod createSubscriberMethod(String methodName, Class<?> eventType) {
        return createSubscriberMethod(methodName, eventType, ThreadMode.POSTING, 0, false);
    }

    /* access modifiers changed from: protected */
    public SubscriberMethod createSubscriberMethod(String methodName, Class<?> eventType, ThreadMode threadMode) {
        return createSubscriberMethod(methodName, eventType, threadMode, 0, false);
    }

    /* access modifiers changed from: protected */
    public SubscriberMethod createSubscriberMethod(String methodName, Class<?> eventType, ThreadMode threadMode, int priority, boolean sticky) {
        try {
            return new SubscriberMethod(this.subscriberClass.getDeclaredMethod(methodName, eventType), eventType, threadMode, priority, sticky);
        } catch (NoSuchMethodException e) {
            throw new EventBusException("Could not find subscriber method in " + this.subscriberClass + ". Maybe a missing ProGuard rule?", e);
        }
    }
}
