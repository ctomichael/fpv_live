package dji.internal.diagnostics.handler.redundancy;

import android.support.annotation.Keep;
import java.util.List;

@Keep
public class RedundancyErrorCodeDescJson {
    private List<RedundancyLocalInfo> err_code;

    public List<RedundancyLocalInfo> getErr_code() {
        return this.err_code;
    }

    public void setErr_code(List<RedundancyLocalInfo> err_code2) {
        this.err_code = err_code2;
    }
}
