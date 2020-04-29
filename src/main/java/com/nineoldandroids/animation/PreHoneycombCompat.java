package com.nineoldandroids.animation;

import android.view.View;
import com.nineoldandroids.util.FloatProperty;
import com.nineoldandroids.util.IntProperty;
import com.nineoldandroids.util.Property;
import com.nineoldandroids.view.animation.AnimatorProxy;
import dji.component.mediaprovider.DJIMediaStore;

final class PreHoneycombCompat {
    static Property<View, Float> ALPHA = new FloatProperty<View>("alpha") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass1 */

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setAlpha(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getAlpha());
        }
    };
    static Property<View, Float> PIVOT_X = new FloatProperty<View>("pivotX") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass2 */

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setPivotX(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getPivotX());
        }
    };
    static Property<View, Float> PIVOT_Y = new FloatProperty<View>("pivotY") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass3 */

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setPivotY(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getPivotY());
        }
    };
    static Property<View, Float> ROTATION = new FloatProperty<View>(DJIMediaStore.FileColumns.ROTATION) {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass6 */

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setRotation(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getRotation());
        }
    };
    static Property<View, Float> ROTATION_X = new FloatProperty<View>("rotationX") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass7 */

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setRotationX(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getRotationX());
        }
    };
    static Property<View, Float> ROTATION_Y = new FloatProperty<View>("rotationY") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass8 */

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setRotationY(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getRotationY());
        }
    };
    static Property<View, Float> SCALE_X = new FloatProperty<View>("scaleX") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass9 */

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setScaleX(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getScaleX());
        }
    };
    static Property<View, Float> SCALE_Y = new FloatProperty<View>("scaleY") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass10 */

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setScaleY(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getScaleY());
        }
    };
    static Property<View, Integer> SCROLL_X = new IntProperty<View>("scrollX") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass11 */

        public void setValue(View object, int value) {
            AnimatorProxy.wrap(object).setScrollX(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(AnimatorProxy.wrap(object).getScrollX());
        }
    };
    static Property<View, Integer> SCROLL_Y = new IntProperty<View>("scrollY") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass12 */

        public void setValue(View object, int value) {
            AnimatorProxy.wrap(object).setScrollY(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(AnimatorProxy.wrap(object).getScrollY());
        }
    };
    static Property<View, Float> TRANSLATION_X = new FloatProperty<View>("translationX") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass4 */

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setTranslationX(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getTranslationX());
        }
    };
    static Property<View, Float> TRANSLATION_Y = new FloatProperty<View>("translationY") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass5 */

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setTranslationY(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getTranslationY());
        }
    };
    static Property<View, Float> X = new FloatProperty<View>("x") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass13 */

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setX(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getX());
        }
    };
    static Property<View, Float> Y = new FloatProperty<View>("y") {
        /* class com.nineoldandroids.animation.PreHoneycombCompat.AnonymousClass14 */

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setY(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getY());
        }
    };

    private PreHoneycombCompat() {
    }
}
