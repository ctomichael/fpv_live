package dji.thirdparty.sanselan;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class FormatCompliance {
    private final ArrayList comments = new ArrayList();
    private final String description;
    private final boolean failOnError;

    public FormatCompliance(String description2) {
        this.description = description2;
        this.failOnError = false;
    }

    public FormatCompliance(String description2, boolean fail_on_error) {
        this.description = description2;
        this.failOnError = fail_on_error;
    }

    public static final FormatCompliance getDefault() {
        return new FormatCompliance("ignore", false);
    }

    public void addComment(String s) throws ImageReadException {
        this.comments.add(s);
        if (this.failOnError) {
            throw new ImageReadException(s);
        }
    }

    public void addComment(String s, int value) throws ImageReadException {
        addComment(s + ": " + getValueDescription(value));
    }

    public String toString() {
        StringWriter sw = new StringWriter();
        dump(new PrintWriter(sw));
        return sw.getBuffer().toString();
    }

    public void dump() {
        dump(new PrintWriter(new OutputStreamWriter(System.out)));
    }

    public void dump(PrintWriter pw) {
        pw.println("Format Compliance: " + this.description);
        if (this.comments.size() == 0) {
            pw.println("\tNo comments.");
        } else {
            for (int i = 0; i < this.comments.size(); i++) {
                pw.println("\t" + (i + 1) + ": " + this.comments.get(i));
            }
        }
        pw.println("");
        pw.flush();
    }

    private String getValueDescription(int value) {
        return value + " (" + Integer.toHexString(value) + ")";
    }

    public boolean compare_bytes(String name, byte[] expected, byte[] actual) throws ImageReadException {
        if (expected.length != actual.length) {
            addComment(name + ": Unexpected length: (expected: " + expected.length + ", actual: " + actual.length + ")");
            return false;
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                addComment(name + ": Unexpected value: (expected: " + getValueDescription(expected[i]) + ", actual: " + getValueDescription(actual[i]) + ")");
                return false;
            }
        }
        return true;
    }

    public boolean checkBounds(String name, int min, int max, int actual) throws ImageReadException {
        if (actual >= min && actual <= max) {
            return true;
        }
        addComment(name + ": bounds check: " + min + " <= " + actual + " <= " + max + ": false");
        return false;
    }

    public boolean compare(String name, int valid, int actual) throws ImageReadException {
        return compare(name, new int[]{valid}, actual);
    }

    public boolean compare(String name, int[] valid, int actual) throws ImageReadException {
        for (int i : valid) {
            if (actual == i) {
                return true;
            }
        }
        StringBuffer result = new StringBuffer();
        result.append(name + ": Unexpected value: (valid: ");
        if (valid.length > 1) {
            result.append("{");
        }
        for (int i2 = 0; i2 < valid.length; i2++) {
            if (i2 > 0) {
                result.append(", ");
            }
            result.append(getValueDescription(valid[i2]));
        }
        if (valid.length > 1) {
            result.append("}");
        }
        result.append(", actual: " + getValueDescription(actual) + ")");
        addComment(result.toString());
        return false;
    }
}
