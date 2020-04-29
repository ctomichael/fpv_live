package com.nineoldandroids.util;

public abstract class FloatProperty<T> extends Property<T, Float> {
    public abstract void setValue(Object obj, float f);

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.nineoldandroids.util.FloatProperty.set(java.lang.Object, java.lang.Float):void
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      com.nineoldandroids.util.FloatProperty.set(java.lang.Object, java.lang.Object):void
      com.nineoldandroids.util.Property.set(java.lang.Object, java.lang.Object):void
      com.nineoldandroids.util.FloatProperty.set(java.lang.Object, java.lang.Float):void */
    public /* bridge */ /* synthetic */ void set(Object x0, Object x1) {
        set(x0, (Float) ((Float) x1));
    }

    public FloatProperty(String name) {
        super(Float.class, name);
    }

    public final void set(T object, Float value) {
        setValue(object, value.floatValue());
    }
}
