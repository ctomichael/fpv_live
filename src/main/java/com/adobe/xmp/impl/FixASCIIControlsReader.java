package com.adobe.xmp.impl;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

public class FixASCIIControlsReader extends PushbackReader {
    private static final int BUFFER_SIZE = 8;
    private static final int STATE_AMP = 1;
    private static final int STATE_DIG1 = 4;
    private static final int STATE_ERROR = 5;
    private static final int STATE_HASH = 2;
    private static final int STATE_HEX = 3;
    private static final int STATE_START = 0;
    private int control = 0;
    private int digits = 0;
    private int state = 0;

    public FixASCIIControlsReader(Reader reader) {
        super(reader, 8);
    }

    private char processChar(char c) {
        switch (this.state) {
            case 0:
                if (c != '&') {
                    return c;
                }
                this.state = 1;
                return c;
            case 1:
                if (c == '#') {
                    this.state = 2;
                    return c;
                }
                this.state = 5;
                return c;
            case 2:
                if (c == 'x') {
                    this.control = 0;
                    this.digits = 0;
                    this.state = 3;
                    return c;
                } else if ('0' > c || c > '9') {
                    this.state = 5;
                    return c;
                } else {
                    this.control = Character.digit(c, 10);
                    this.digits = 1;
                    this.state = 4;
                    return c;
                }
            case 3:
                if (('0' <= c && c <= '9') || (('a' <= c && c <= 'f') || ('A' <= c && c <= 'F'))) {
                    this.control = (this.control * 16) + Character.digit(c, 16);
                    this.digits++;
                    if (this.digits <= 4) {
                        this.state = 3;
                        return c;
                    }
                    this.state = 5;
                    return c;
                } else if (c != ';' || !Utils.isControlChar((char) this.control)) {
                    this.state = 5;
                    return c;
                } else {
                    this.state = 0;
                    return (char) this.control;
                }
            case 4:
                if ('0' <= c && c <= '9') {
                    this.control = (this.control * 10) + Character.digit(c, 10);
                    this.digits++;
                    if (this.digits <= 5) {
                        this.state = 4;
                        return c;
                    }
                    this.state = 5;
                    return c;
                } else if (c != ';' || !Utils.isControlChar((char) this.control)) {
                    this.state = 5;
                    return c;
                } else {
                    this.state = 0;
                    return (char) this.control;
                }
            case 5:
                this.state = 0;
                return c;
            default:
                return c;
        }
    }

    public int read(char[] cArr, int i, int i2) throws IOException {
        int i3;
        char[] cArr2 = new char[8];
        boolean z = true;
        int i4 = i;
        int i5 = 0;
        int i6 = 0;
        while (z && i5 < i2) {
            z = super.read(cArr2, i6, 1) == 1;
            if (z) {
                char processChar = processChar(cArr2[i6]);
                if (this.state == 0) {
                    if (Utils.isControlChar(processChar)) {
                        processChar = ' ';
                    }
                    cArr[i4] = processChar;
                    i3 = i4 + 1;
                    i5++;
                    i6 = 0;
                } else if (this.state == 5) {
                    unread(cArr2, 0, i6 + 1);
                    i3 = i4;
                    i6 = 0;
                } else {
                    i3 = i4;
                    i6++;
                }
                i4 = i3;
            } else if (i6 > 0) {
                unread(cArr2, 0, i6);
                this.state = 5;
                z = true;
                i6 = 0;
            }
        }
        if (i5 > 0 || z) {
            return i5;
        }
        return -1;
    }
}
