package dji.publics.utils;

import android.text.InputFilter;
import android.text.Spanned;
import dji.component.accountcenter.IMemberProtocol;
import dji.log.DJILog;
import dji.midware.util.BytesUtil;
import java.io.UnsupportedEncodingException;

public class ByteLengthFilter implements InputFilter {
    private static final String TAG = "ByteLengthFilter";
    private String mCharset;
    protected int mMaxByte;

    public ByteLengthFilter(int maxbyte, String charset) {
        this.mMaxByte = maxbyte;
        this.mCharset = charset;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int sourceStringByteLen = BytesUtil.getBytesUTF8(source.toString()).length;
        DJILog.d(TAG, "onBindViewHolder: source [" + start + ", " + end + "], dest [" + dstart + ", " + dend + IMemberProtocol.STRING_SEPERATOR_RIGHT, new Object[0]);
        DJILog.d(TAG, "filter: source length " + source.length() + ", " + ((Object) source) + ", bytes len " + sourceStringByteLen, new Object[0]);
        DJILog.d(TAG, "filter: dest length " + dest.length() + ", " + ((Object) dest) + ", bytes len " + BytesUtil.getBytesUTF8(dest.toString()).length, new Object[0]);
        CharSequence before = dest.subSequence(0, dstart);
        CharSequence middle = source.subSequence(start, end);
        CharSequence last = dest.subSequence(dend, dest.length());
        if (BytesUtil.getBytesUTF8(String.valueOf(before) + ((Object) middle) + ((Object) last)).length <= this.mMaxByte) {
            return null;
        }
        int remain = this.mMaxByte - (BytesUtil.getBytesUTF8(before.toString()).length + BytesUtil.getBytesUTF8(last.toString()).length);
        for (int i = end; i > start; i--) {
            String data = middle.subSequence(start, i).toString();
            if (BytesUtil.getBytesUTF8(data).length <= remain) {
                return data;
            }
        }
        return "";
    }

    /* access modifiers changed from: protected */
    public int plusMaxLength(String expected, String source, int start) {
        int keep = source.length();
        while (getByteLength(source.subSequence(start, start + keep).toString()) > this.mMaxByte - getByteLength(expected)) {
            keep--;
        }
        return keep;
    }

    /* access modifiers changed from: protected */
    public int calculateMaxLength(String expected) {
        if (getByteLength(expected) == 0) {
            return 0;
        }
        return this.mMaxByte - (getByteLength(expected) - expected.length());
    }

    private int getByteLength(String str) {
        try {
            return str.getBytes(this.mCharset).length;
        } catch (UnsupportedEncodingException e) {
            return 0;
        }
    }
}
