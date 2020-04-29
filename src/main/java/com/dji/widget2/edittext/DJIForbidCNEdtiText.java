package com.dji.widget2.edittext;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class DJIForbidCNEdtiText extends EditText {
    public DJIForbidCNEdtiText(Context context) {
        super(context);
        init();
    }

    public DJIForbidCNEdtiText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DJIForbidCNEdtiText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DJIForbidCNEdtiText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        ArrayList<InputFilter> list = new ArrayList<>();
        InputFilter[] filters = getFilters();
        if (!(filters == null || filters.length == 0)) {
            list.addAll(Arrays.asList(filters));
        }
        list.add(DJIForbidCNEdtiText$$Lambda$0.$instance);
        setFilters((InputFilter[]) list.toArray(new InputFilter[0]));
    }

    static final /* synthetic */ CharSequence lambda$init$0$DJIForbidCNEdtiText(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (Pattern.compile("[一-龥]+$").matcher(source).find()) {
            return "";
        }
        return null;
    }
}
