package com.adobe.xmp.options;

import com.adobe.xmp.XMPException;
import java.util.HashMap;
import java.util.Map;

public abstract class Options {
    private Map optionNames = null;
    private int options = 0;

    public Options() {
    }

    public Options(int i) throws XMPException {
        assertOptionsValid(i);
        setOptions(i);
    }

    private void assertOptionsValid(int i) throws XMPException {
        int validOptions = (getValidOptions() ^ -1) & i;
        if (validOptions == 0) {
            assertConsistency(i);
            return;
        }
        throw new XMPException("The option bit(s) 0x" + Integer.toHexString(validOptions) + " are invalid!", 103);
    }

    private String getOptionName(int i) {
        Map procureOptionNames = procureOptionNames();
        Integer num = new Integer(i);
        String str = (String) procureOptionNames.get(num);
        if (str != null) {
            return str;
        }
        String defineOptionName = defineOptionName(i);
        if (defineOptionName == null) {
            return "<option name not defined>";
        }
        procureOptionNames.put(num, defineOptionName);
        return defineOptionName;
    }

    private Map procureOptionNames() {
        if (this.optionNames == null) {
            this.optionNames = new HashMap();
        }
        return this.optionNames;
    }

    /* access modifiers changed from: protected */
    public void assertConsistency(int i) throws XMPException {
    }

    public void clear() {
        this.options = 0;
    }

    public boolean containsAllOptions(int i) {
        return (getOptions() & i) == i;
    }

    public boolean containsOneOf(int i) {
        return (getOptions() & i) != 0;
    }

    /* access modifiers changed from: protected */
    public abstract String defineOptionName(int i);

    public boolean equals(Object obj) {
        return getOptions() == ((Options) obj).getOptions();
    }

    /* access modifiers changed from: protected */
    public boolean getOption(int i) {
        return (this.options & i) != 0;
    }

    public int getOptions() {
        return this.options;
    }

    public String getOptionsString() {
        if (this.options == 0) {
            return "<none>";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i = this.options;
        while (i != 0) {
            int i2 = (i - 1) & i;
            stringBuffer.append(getOptionName(i ^ i2));
            if (i2 != 0) {
                stringBuffer.append(" | ");
            }
            i = i2;
        }
        return stringBuffer.toString();
    }

    /* access modifiers changed from: protected */
    public abstract int getValidOptions();

    public int hashCode() {
        return getOptions();
    }

    public boolean isExactly(int i) {
        return getOptions() == i;
    }

    public void setOption(int i, boolean z) {
        this.options = z ? this.options | i : this.options & (i ^ -1);
    }

    public void setOptions(int i) throws XMPException {
        assertOptionsValid(i);
        this.options = i;
    }

    public String toString() {
        return "0x" + Integer.toHexString(this.options);
    }
}
