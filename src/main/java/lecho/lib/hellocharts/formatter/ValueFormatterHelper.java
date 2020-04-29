package lecho.lib.hellocharts.formatter;

import android.util.Log;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import lecho.lib.hellocharts.util.FloatUtils;

public class ValueFormatterHelper {
    public static final int DEFAULT_DIGITS_NUMBER = 0;
    private static final String TAG = "ValueFormatterHelper";
    private char[] appendedText = new char[0];
    private int decimalDigitsNumber = Integer.MIN_VALUE;
    private char decimalSeparator = '.';
    private char[] prependedText = new char[0];

    public void determineDecimalSeparator() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        if (numberFormat instanceof DecimalFormat) {
            this.decimalSeparator = ((DecimalFormat) numberFormat).getDecimalFormatSymbols().getDecimalSeparator();
        }
    }

    public int getDecimalDigitsNumber() {
        return this.decimalDigitsNumber;
    }

    public ValueFormatterHelper setDecimalDigitsNumber(int decimalDigitsNumber2) {
        this.decimalDigitsNumber = decimalDigitsNumber2;
        return this;
    }

    public char[] getAppendedText() {
        return this.appendedText;
    }

    public ValueFormatterHelper setAppendedText(char[] appendedText2) {
        if (appendedText2 != null) {
            this.appendedText = appendedText2;
        }
        return this;
    }

    public char[] getPrependedText() {
        return this.prependedText;
    }

    public ValueFormatterHelper setPrependedText(char[] prependedText2) {
        if (prependedText2 != null) {
            this.prependedText = prependedText2;
        }
        return this;
    }

    public char getDecimalSeparator() {
        return this.decimalSeparator;
    }

    public ValueFormatterHelper setDecimalSeparator(char decimalSeparator2) {
        if (0 != decimalSeparator2) {
            this.decimalSeparator = decimalSeparator2;
        }
        return this;
    }

    public int formatFloatValueWithPrependedAndAppendedText(char[] formattedValue, float value, int defaultDigitsNumber, char[] label) {
        if (label != null) {
            int labelLength = label.length;
            if (labelLength > formattedValue.length) {
                Log.w(TAG, "Label length is larger than buffer size(64chars), some chars will be skipped!");
                labelLength = formattedValue.length;
            }
            System.arraycopy(label, 0, formattedValue, formattedValue.length - labelLength, labelLength);
            return labelLength;
        }
        int charsNumber = formatFloatValue(formattedValue, value, getAppliedDecimalDigitsNumber(defaultDigitsNumber));
        appendText(formattedValue);
        prependText(formattedValue, charsNumber);
        return getPrependedText().length + charsNumber + getAppendedText().length;
    }

    public int formatFloatValueWithPrependedAndAppendedText(char[] formattedValue, float value, char[] label) {
        return formatFloatValueWithPrependedAndAppendedText(formattedValue, value, 0, label);
    }

    public int formatFloatValueWithPrependedAndAppendedText(char[] formattedValue, float value, int defaultDigitsNumber) {
        return formatFloatValueWithPrependedAndAppendedText(formattedValue, value, defaultDigitsNumber, null);
    }

    public int formatFloatValue(char[] formattedValue, float value, int decimalDigitsNumber2) {
        return FloatUtils.formatFloat(formattedValue, value, formattedValue.length - this.appendedText.length, decimalDigitsNumber2, this.decimalSeparator);
    }

    public void appendText(char[] formattedValue) {
        if (this.appendedText.length > 0) {
            System.arraycopy(this.appendedText, 0, formattedValue, formattedValue.length - this.appendedText.length, this.appendedText.length);
        }
    }

    public void prependText(char[] formattedValue, int charsNumber) {
        if (this.prependedText.length > 0) {
            System.arraycopy(this.prependedText, 0, formattedValue, ((formattedValue.length - charsNumber) - this.appendedText.length) - this.prependedText.length, this.prependedText.length);
        }
    }

    public int getAppliedDecimalDigitsNumber(int defaultDigitsNumber) {
        if (this.decimalDigitsNumber < 0) {
            return defaultDigitsNumber;
        }
        return this.decimalDigitsNumber;
    }
}
