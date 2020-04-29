package org.msgpack.core.buffer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

class DirectBufferAccess {
    static Constructor byteBufferConstructor;
    static DirectBufferConstructorType directBufferConstructorType;
    static Class<?> directByteBufferClass;
    static Method mClean;
    static Method mCleaner;
    static Method mGetAddress;
    static Method memoryBlockWrapFromJni;

    enum DirectBufferConstructorType {
        ARGS_LONG_INT_REF,
        ARGS_LONG_INT,
        ARGS_INT_INT,
        ARGS_MB_INT_INT
    }

    private DirectBufferAccess() {
    }

    static {
        Constructor<?> declaredConstructor;
        DirectBufferConstructorType directBufferConstructorType2;
        try {
            directByteBufferClass = ClassLoader.getSystemClassLoader().loadClass("java.nio.DirectByteBuffer");
            Method method = null;
            try {
                declaredConstructor = directByteBufferClass.getDeclaredConstructor(Long.TYPE, Integer.TYPE, Object.class);
                directBufferConstructorType2 = DirectBufferConstructorType.ARGS_LONG_INT_REF;
            } catch (NoSuchMethodException e) {
                try {
                    declaredConstructor = directByteBufferClass.getDeclaredConstructor(Long.TYPE, Integer.TYPE);
                    directBufferConstructorType2 = DirectBufferConstructorType.ARGS_LONG_INT;
                } catch (NoSuchMethodException e2) {
                    try {
                        declaredConstructor = directByteBufferClass.getDeclaredConstructor(Integer.TYPE, Integer.TYPE);
                        directBufferConstructorType2 = DirectBufferConstructorType.ARGS_INT_INT;
                    } catch (NoSuchMethodException e3) {
                        Class<?> cls = Class.forName("java.nio.MemoryBlock");
                        method = cls.getDeclaredMethod("wrapFromJni", Integer.TYPE, Long.TYPE);
                        method.setAccessible(true);
                        declaredConstructor = directByteBufferClass.getDeclaredConstructor(cls, Integer.TYPE, Integer.TYPE);
                        directBufferConstructorType2 = DirectBufferConstructorType.ARGS_MB_INT_INT;
                    }
                }
            }
            byteBufferConstructor = declaredConstructor;
            directBufferConstructorType = directBufferConstructorType2;
            memoryBlockWrapFromJni = method;
            if (byteBufferConstructor == null) {
                throw new RuntimeException("Constructor of DirectByteBuffer is not found");
            }
            byteBufferConstructor.setAccessible(true);
            mGetAddress = directByteBufferClass.getDeclaredMethod("address", new Class[0]);
            mGetAddress.setAccessible(true);
            mCleaner = directByteBufferClass.getDeclaredMethod("cleaner", new Class[0]);
            mCleaner.setAccessible(true);
            mClean = mCleaner.getReturnType().getDeclaredMethod("clean", new Class[0]);
            mClean.setAccessible(true);
        } catch (Exception e4) {
            throw new RuntimeException(e4);
        }
    }

    static long getAddress(Object obj) {
        try {
            return ((Long) mGetAddress.invoke(obj, new Object[0])).longValue();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e2) {
            throw new RuntimeException(e2);
        }
    }

    static void clean(Object obj) {
        try {
            mClean.invoke(mCleaner.invoke(obj, new Object[0]), new Object[0]);
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
    }

    static boolean isDirectByteBufferInstance(Object obj) {
        return directByteBufferClass.isInstance(obj);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    static ByteBuffer newByteBuffer(long j, int i, int i2, ByteBuffer byteBuffer) {
        try {
            switch (directBufferConstructorType) {
                case ARGS_LONG_INT_REF:
                    return (ByteBuffer) byteBufferConstructor.newInstance(Long.valueOf(((long) i) + j), Integer.valueOf(i2), byteBuffer);
                case ARGS_LONG_INT:
                    return (ByteBuffer) byteBufferConstructor.newInstance(Long.valueOf(((long) i) + j), Integer.valueOf(i2));
                case ARGS_INT_INT:
                    return (ByteBuffer) byteBufferConstructor.newInstance(Integer.valueOf(((int) j) + i), Integer.valueOf(i2));
                case ARGS_MB_INT_INT:
                    return (ByteBuffer) byteBufferConstructor.newInstance(memoryBlockWrapFromJni.invoke(null, Long.valueOf(((long) i) + j), Integer.valueOf(i2)), Integer.valueOf(i2), 0);
                default:
                    throw new IllegalStateException("Unexpected value");
            }
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
        throw new RuntimeException(th);
    }
}
