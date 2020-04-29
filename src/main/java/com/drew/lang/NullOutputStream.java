package com.drew.lang;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {
    public void write(int b) throws IOException {
    }
}
