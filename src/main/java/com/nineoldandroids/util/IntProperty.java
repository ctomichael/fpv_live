package com.nineoldandroids.util;

public abstract class IntProperty<T> extends Property<T, Integer> {
    public abstract void setValue(Object obj, int i);

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.nineoldandroids.util.IntProperty.set(java.lang.Object, java.lang.Integer):void
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      com.nineoldandroids.util.IntProperty.set(java.lang.Object, java.lang.Object):void
      com.nineoldandroids.util.Property.set(java.lang.Object, java.lang.Object):void
      com.nineoldandroids.util.IntProperty.set(java.lang.Object, java.lang.Integer):void */
    public /* bridge */ /* synthetic */ void set(Object x0, Object x1) {
        set(x0, (Integer) ((Integer) x1));
    }

    public IntProperty(String name) {
        super(Integer.class, name);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.nineoldandroids.util.IntProperty.set(java.lang.Object, java.lang.Integer):void
     arg types: [T, java.lang.Integer]
     candidates:
      com.nineoldandroids.util.IntProperty.set(java.lang.Object, java.lang.Object):void
      com.nineoldandroids.util.Property.set(java.lang.Object, java.lang.Object):void
      com.nineoldandroids.util.IntProperty.set(java.lang.Object, java.lang.Integer):void */
    public final void set(T object, Integer value) {
        set((Object) object, Integer.valueOf(value.intValue()));
    }
}
