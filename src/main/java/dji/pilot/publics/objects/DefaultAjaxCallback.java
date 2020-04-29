package dji.pilot.publics.objects;

import dji.fieldAnnotation.EXClassNullAway;
import dji.thirdparty.afinal.http.AjaxCallBack;

@EXClassNullAway
public class DefaultAjaxCallback<T> extends AjaxCallBack<T> {
    public void onStart(boolean isResume) {
    }

    public void onLoading(long count, long current) {
    }

    public void onSuccess(T t) {
    }

    public void onFailure(Throwable throwable, int errorNo, String errMsg) {
    }
}
