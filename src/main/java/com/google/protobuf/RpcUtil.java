package com.google.protobuf;

public final class RpcUtil {
    private RpcUtil() {
    }

    public static <Type extends Message> RpcCallback<Type> specializeCallback(RpcCallback<Message> originalCallback) {
        return originalCallback;
    }

    public static <Type extends Message> RpcCallback<Message> generalizeCallback(final RpcCallback<Type> originalCallback, final Class<Type> originalClass, final Type defaultInstance) {
        return new RpcCallback<Message>() {
            /* class com.google.protobuf.RpcUtil.AnonymousClass1 */

            public void run(Message parameter) {
                Type typedParameter;
                try {
                    typedParameter = (Message) originalClass.cast(parameter);
                } catch (ClassCastException e) {
                    typedParameter = RpcUtil.copyAsType(defaultInstance, parameter);
                }
                originalCallback.run(typedParameter);
            }
        };
    }

    /* access modifiers changed from: private */
    public static <Type extends Message> Type copyAsType(Type typeDefaultInstance, Message source) {
        return typeDefaultInstance.newBuilderForType().mergeFrom(source).build();
    }

    public static <ParameterType> RpcCallback<ParameterType> newOneTimeCallback(final RpcCallback<ParameterType> originalCallback) {
        return new RpcCallback<ParameterType>() {
            /* class com.google.protobuf.RpcUtil.AnonymousClass2 */
            private boolean alreadyCalled = false;

            public void run(ParameterType parameter) {
                synchronized (this) {
                    if (this.alreadyCalled) {
                        throw new AlreadyCalledException();
                    }
                    this.alreadyCalled = true;
                }
                originalCallback.run(parameter);
            }
        };
    }

    public static final class AlreadyCalledException extends RuntimeException {
        private static final long serialVersionUID = 5469741279507848266L;

        public AlreadyCalledException() {
            super("This RpcCallback was already called and cannot be called multiple times.");
        }
    }
}
