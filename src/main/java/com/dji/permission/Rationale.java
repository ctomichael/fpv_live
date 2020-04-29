package com.dji.permission;

import android.content.Context;
import java.util.List;

public interface Rationale {
    void showRationale(Context context, List<String> list, RequestExecutor requestExecutor);
}
