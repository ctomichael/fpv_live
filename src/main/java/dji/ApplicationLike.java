package dji;

import android.content.Context;

public interface ApplicationLike {
    void onCreate(Context context);

    void onDestroy(Context context);

    int priority();
}
