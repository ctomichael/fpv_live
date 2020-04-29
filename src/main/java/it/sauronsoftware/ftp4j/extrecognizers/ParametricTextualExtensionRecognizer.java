package it.sauronsoftware.ftp4j.extrecognizers;

import it.sauronsoftware.ftp4j.FTPTextualExtensionRecognizer;
import java.util.ArrayList;

public class ParametricTextualExtensionRecognizer implements FTPTextualExtensionRecognizer {
    private ArrayList exts = new ArrayList();

    public ParametricTextualExtensionRecognizer() {
    }

    public ParametricTextualExtensionRecognizer(String[] exts2) {
        for (String str : exts2) {
            addExtension(str);
        }
    }

    public ParametricTextualExtensionRecognizer(ArrayList exts2) {
        int size = exts2.size();
        for (int i = 0; i < size; i++) {
            Object aux = exts2.get(i);
            if (aux instanceof String) {
                addExtension((String) aux);
            }
        }
    }

    public void addExtension(String ext) {
        synchronized (this.exts) {
            this.exts.add(ext.toLowerCase());
        }
    }

    public void removeExtension(String ext) {
        synchronized (this.exts) {
            this.exts.remove(ext.toLowerCase());
        }
    }

    public String[] getExtensions() {
        String[] ret;
        synchronized (this.exts) {
            int size = this.exts.size();
            ret = new String[size];
            for (int i = 0; i < size; i++) {
                ret[i] = (String) this.exts.get(i);
            }
        }
        return ret;
    }

    public boolean isTextualExt(String ext) {
        boolean contains;
        synchronized (this.exts) {
            contains = this.exts.contains(ext);
        }
        return contains;
    }
}
