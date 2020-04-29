package org.greenrobot.eventbus.meta;

import org.greenrobot.eventbus.ThreadMode;

public class SubscriberMethodInfo {
    final Class<?> eventType;
    final String methodName;
    final int priority;
    final boolean sticky;
    final ThreadMode threadMode;

    public SubscriberMethodInfo(String methodName2, Class<?> eventType2, ThreadMode threadMode2, int priority2, boolean sticky2) {
        this.methodName = methodName2;
        this.threadMode = threadMode2;
        this.eventType = eventType2;
        this.priority = priority2;
        this.sticky = sticky2;
    }

    public SubscriberMethodInfo(String methodName2, Class<?> eventType2) {
        this(methodName2, eventType2, ThreadMode.POSTING, 0, false);
    }

    public SubscriberMethodInfo(String methodName2, Class<?> eventType2, ThreadMode threadMode2) {
        this(methodName2, eventType2, threadMode2, 0, false);
    }
}
