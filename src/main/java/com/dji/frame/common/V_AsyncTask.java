package com.dji.frame.common;

import android.os.AsyncTask;
import com.dji.frame.interfaces.V_CallBack_Async;

public class V_AsyncTask extends AsyncTask<Object, Object, Object> {
    private V_CallBack_Async callback;

    public V_AsyncTask(V_CallBack_Async callback2) {
        this.callback = callback2;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        super.onPreExecute();
    }

    /* access modifiers changed from: protected */
    public Object doInBackground(Object... arg0) {
        return this.callback.execAsync().toString();
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Object result) {
        this.callback.receive(result);
        super.onPostExecute(result);
    }
}
