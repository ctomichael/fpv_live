package dji;

import android.app.Application;
import com.drew.lang.annotations.NotNull;

public class DJIBaseApplication extends Application {
    private static Application appContext;

    public DJIBaseApplication() {
        appContext = this;
    }

    @NotNull
    public static Application getAppContext() {
        return appContext;
    }
}
