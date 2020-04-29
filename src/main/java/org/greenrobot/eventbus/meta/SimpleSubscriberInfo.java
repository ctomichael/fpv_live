package org.greenrobot.eventbus.meta;

import org.greenrobot.eventbus.SubscriberMethod;

public class SimpleSubscriberInfo extends AbstractSubscriberInfo {
    private final SubscriberMethodInfo[] methodInfos;

    public SimpleSubscriberInfo(Class subscriberClass, boolean shouldCheckSuperclass, SubscriberMethodInfo[] methodInfos2) {
        super(subscriberClass, null, shouldCheckSuperclass);
        this.methodInfos = methodInfos2;
    }

    public synchronized SubscriberMethod[] getSubscriberMethods() {
        SubscriberMethod[] methods;
        int length = this.methodInfos.length;
        methods = new SubscriberMethod[length];
        for (int i = 0; i < length; i++) {
            SubscriberMethodInfo info = this.methodInfos[i];
            methods[i] = createSubscriberMethod(info.methodName, info.eventType, info.threadMode, info.priority, info.sticky);
        }
        return methods;
    }
}
