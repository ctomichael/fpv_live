package com.dji.component.fpv.base.menu;

import android.view.animation.Animation;

public interface IMenuAnimation {
    Animation animationOnAttached();

    Animation animationOnDetached();
}
