package org.greenrobot.eventbus;

import java.lang.reflect.Method;

public class SubscriberMethod {
    final Class<?> eventType;
    final Method method;
    String methodString;
    final int priority;
    final boolean sticky;
    final ThreadMode threadMode;

    public SubscriberMethod(Method method2, Class<?> eventType2, ThreadMode threadMode2, int priority2, boolean sticky2) {
        this.method = method2;
        this.threadMode = threadMode2;
        this.eventType = eventType2;
        this.priority = priority2;
        this.sticky = sticky2;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SubscriberMethod)) {
            return false;
        }
        checkMethodString();
        SubscriberMethod otherSubscriberMethod = (SubscriberMethod) other;
        otherSubscriberMethod.checkMethodString();
        return this.methodString.equals(otherSubscriberMethod.methodString);
    }

    private synchronized void checkMethodString() {
        if (this.methodString == null) {
            StringBuilder builder = new StringBuilder(64);
            builder.append(this.method.getDeclaringClass().getName());
            builder.append('#').append(this.method.getName());
            builder.append('(').append(this.eventType.getName());
            this.methodString = builder.toString();
        }
    }

    public int hashCode() {
        return this.method.hashCode();
    }
}
