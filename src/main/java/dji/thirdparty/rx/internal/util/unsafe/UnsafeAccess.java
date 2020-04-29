package dji.thirdparty.rx.internal.util.unsafe;

import sun.misc.Unsafe;

public final class UnsafeAccess {
    private static final boolean DISABLED_BY_USER;
    public static final Unsafe UNSAFE;

    private UnsafeAccess() {
        throw new IllegalStateException("No instances!");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: sun.misc.Unsafe} */
    /* JADX WARNING: Multi-variable type inference failed */
    static {
        /*
            r3 = 1
            java.lang.String r4 = "rx.unsafe-disable"
            java.lang.String r4 = java.lang.System.getProperty(r4)
            if (r4 == 0) goto L_0x0026
        L_0x000a:
            dji.thirdparty.rx.internal.util.unsafe.UnsafeAccess.DISABLED_BY_USER = r3
            r2 = 0
            java.lang.Class<sun.misc.Unsafe> r3 = sun.misc.Unsafe.class
            java.lang.String r4 = "theUnsafe"
            java.lang.reflect.Field r1 = r3.getDeclaredField(r4)     // Catch:{ Throwable -> 0x0028 }
            r3 = 1
            r1.setAccessible(r3)     // Catch:{ Throwable -> 0x0028 }
            r3 = 0
            java.lang.Object r3 = r1.get(r3)     // Catch:{ Throwable -> 0x0028 }
            r0 = r3
            sun.misc.Unsafe r0 = (sun.misc.Unsafe) r0     // Catch:{ Throwable -> 0x0028 }
            r2 = r0
        L_0x0023:
            dji.thirdparty.rx.internal.util.unsafe.UnsafeAccess.UNSAFE = r2
            return
        L_0x0026:
            r3 = 0
            goto L_0x000a
        L_0x0028:
            r3 = move-exception
            goto L_0x0023
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.util.unsafe.UnsafeAccess.<clinit>():void");
    }

    public static boolean isUnsafeAvailable() {
        return UNSAFE != null && !DISABLED_BY_USER;
    }

    public static int getAndIncrementInt(Object obj, long offset) {
        int current;
        do {
            current = UNSAFE.getIntVolatile(obj, offset);
        } while (!UNSAFE.compareAndSwapInt(obj, offset, current, current + 1));
        return current;
    }

    public static int getAndAddInt(Object obj, long offset, int n) {
        int current;
        do {
            current = UNSAFE.getIntVolatile(obj, offset);
        } while (!UNSAFE.compareAndSwapInt(obj, offset, current, current + n));
        return current;
    }

    public static int getAndSetInt(Object obj, long offset, int newValue) {
        int current;
        do {
            current = UNSAFE.getIntVolatile(obj, offset);
        } while (!UNSAFE.compareAndSwapInt(obj, offset, current, newValue));
        return current;
    }

    public static boolean compareAndSwapInt(Object obj, long offset, int expected, int newValue) {
        return UNSAFE.compareAndSwapInt(obj, offset, expected, newValue);
    }

    public static long addressOf(Class<?> clazz, String fieldName) {
        try {
            return UNSAFE.objectFieldOffset(clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException ex) {
            InternalError ie = new InternalError();
            ie.initCause(ex);
            throw ie;
        }
    }
}
