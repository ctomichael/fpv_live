package lecho.lib.hellocharts.animation;

import java.util.EventListener;

public interface ChartAnimationListener extends EventListener {
    void onAnimationFinished();

    void onAnimationStarted();
}
