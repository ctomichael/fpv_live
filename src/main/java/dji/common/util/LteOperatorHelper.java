package dji.common.util;

import android.util.SparseArray;
import com.dji.frame.util.V_FileUtil;
import dji.log.DJILog;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.ContextUtil;
import dji.sdkcache.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LteOperatorHelper {
    private SparseArray<String> mOperatorCache;

    public static LteOperatorHelper getInstance() {
        return Holder.INSTANCE;
    }

    private LteOperatorHelper() {
        this.mOperatorCache = new SparseArray<>();
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static LteOperatorHelper INSTANCE = new LteOperatorHelper();

        private Holder() {
        }
    }

    public String getName(int mcc, int mnc) {
        String name = this.mOperatorCache.get(getKey(mcc, mnc));
        if (name == null) {
            syncNameFromLocate(mcc, mnc);
        }
        return name == null ? "" : name;
    }

    private void syncNameFromLocate(int mcc, int mnc) {
        BackgroundLooper.post(new LteOperatorHelper$$Lambda$0(this, mcc, mnc));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$syncNameFromLocate$0$LteOperatorHelper(int mcc, int mnc) {
        String name = readName(mcc, mnc);
        this.mOperatorCache.put(getKey(mcc, mnc), name);
    }

    private String readName(int mcc, int mnc) {
        InputStream ioStream = ContextUtil.getContext().getResources().openRawResource(R.raw.mcc_mnc_operator);
        BufferedReader reader = new BufferedReader(new InputStreamReader(ioStream));
        String name = "";
        try {
            int key = getKey(mcc, mnc);
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String[] result = line.split("\t");
                if (result != null && result.length >= 1) {
                    try {
                        Integer pKey = Integer.valueOf(result[0]);
                        String pName = result[1];
                        if (key == pKey.intValue()) {
                            name = pName;
                            break;
                        }
                    } catch (Exception ex) {
                        DJILog.e("Lte", "parse error " + ex.getMessage(), new Object[0]);
                    }
                }
            }
        } catch (IOException ex2) {
            ex2.printStackTrace();
        } finally {
            V_FileUtil.safeClose(ioStream);
            V_FileUtil.safeClose(reader);
        }
        return name;
    }

    private int getKey(int mcc, int mnc) {
        return (mcc * 100) + mnc;
    }
}
