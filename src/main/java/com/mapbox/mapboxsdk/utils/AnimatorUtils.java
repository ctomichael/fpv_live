package com.mapbox.mapboxsdk.utils;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;

public class AnimatorUtils {

    public interface OnAnimationEndListener {
        void onAnimationEnd();
    }

    public static void animate(@NonNull View view, @AnimatorRes int animatorRes, @Nullable OnAnimationEndListener listener) {
        animate(view, animatorRes, -1, listener);
    }

    public static void animate(@Nullable final View view, @AnimatorRes int animatorRes, int duration, @Nullable final OnAnimationEndListener listener) {
        if (view != null) {
            view.setLayerType(2, null);
            Animator animator = AnimatorInflater.loadAnimator(view.getContext(), animatorRes);
            if (duration != -1) {
                animator.setDuration((long) duration);
            }
            animator.addListener(new AnimatorListenerAdapter() {
                /* class com.mapbox.mapboxsdk.utils.AnimatorUtils.AnonymousClass1 */

                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setLayerType(0, null);
                    if (listener != null) {
                        listener.onAnimationEnd();
                    }
                }
            });
            animator.setTarget(view);
            animator.start();
        }
    }

    public static void animate(@NonNull View view, @AnimatorRes int animatorRes) {
        animate(view, animatorRes, -1);
    }

    public static void animate(@NonNull View view, @AnimatorRes int animatorRes, int duration) {
        animate(view, animatorRes, duration, null);
    }

    public static void rotate(@NonNull final View view, float rotation) {
        view.setLayerType(2, null);
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, view.getRotation(), rotation);
        rotateAnimator.addListener(new AnimatorListenerAdapter() {
            /* class com.mapbox.mapboxsdk.utils.AnimatorUtils.AnonymousClass2 */

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setLayerType(0, null);
            }
        });
        rotateAnimator.start();
    }

    public static void rotateBy(@NonNull final View view, float rotationBy) {
        view.setLayerType(2, null);
        view.animate().rotationBy(rotationBy).setInterpolator(new FastOutSlowInInterpolator()).setListener(new AnimatorListenerAdapter() {
            /* class com.mapbox.mapboxsdk.utils.AnimatorUtils.AnonymousClass3 */

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setLayerType(0, null);
            }
        });
    }

    public static void alpha(@NonNull final View convertView, float alpha, @Nullable final OnAnimationEndListener listener) {
        convertView.setLayerType(2, null);
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(convertView, View.ALPHA, convertView.getAlpha(), alpha);
        rotateAnimator.addListener(new AnimatorListenerAdapter() {
            /* class com.mapbox.mapboxsdk.utils.AnimatorUtils.AnonymousClass4 */

            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                convertView.setVisibility(0);
            }

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                convertView.setLayerType(0, null);
                if (listener != null) {
                    listener.onAnimationEnd();
                }
            }
        });
        rotateAnimator.start();
    }

    public static void alpha(@NonNull View convertView, float alpha) {
        alpha(convertView, alpha, null);
    }
}
