package org.greenrobot.eventbus.meta;

import org.greenrobot.eventbus.SubscriberMethod;

public interface SubscriberInfo {
    Class<?> getSubscriberClass();

    SubscriberMethod[] getSubscriberMethods();

    SubscriberInfo getSuperSubscriberInfo();

    boolean shouldCheckSuperclass();
}
