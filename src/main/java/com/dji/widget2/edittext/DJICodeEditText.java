package com.dji.widget2.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dji.widget2.DensityUtils;
import com.dji.widget2.R;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public class DJICodeEditText extends FrameLayout implements TextWatcher, View.OnClickListener {
    private final int DEFAULT_BOARDER_MARGIN = 10;
    private final int DEFAULT_BOARDER_SIZE = 105;
    private final int DEFAULT_EDIT_VIEW_NUMBER = 6;
    private final int DEFAULT_IME_OPTIONS = 0;
    private final int DEFAULT_PERCENT_WIDTH = 0;
    private final String DEFAULT_PLACE_HOLDER = "-";
    private final int DEFAULT_TEXT_COLOR = -1;
    private final int DEFAULT_TEXT_SIZE = 8;
    private int borderMargin = 10;
    private int borderSize = 105;
    private InputChangedListener callBack;
    private int imeOptions = 0;
    private int inputTyte = 2;
    private LinearLayout mContentLinearLayout;
    /* access modifiers changed from: private */
    public EditText mEditText;
    private int mEditViewNum = 6;
    private float mPercentWidth;
    /* access modifiers changed from: private */
    public ArrayList<TextView> mTextViewsList = new ArrayList<>();
    private int textColor = -1;
    private int textSize = 8;

    public interface InputChangedListener {
        void onInput(String str, boolean z);
    }

    public DJICodeEditText(Context context) {
        super(context);
        init(context);
    }

    public DJICodeEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);
        init(context);
    }

    public DJICodeEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
        init(context);
    }

    private void initData(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DJICodeEditText);
        this.borderSize = array.getDimensionPixelSize(R.styleable.DJICodeEditText_borderSize, 105);
        this.borderMargin = array.getDimensionPixelSize(R.styleable.DJICodeEditText_borderMargin, 10);
        this.textSize = array.getInteger(R.styleable.DJICodeEditText_textSize, 8);
        this.textColor = array.getColor(R.styleable.DJICodeEditText_textColor, -1);
        this.inputTyte = array.getInt(R.styleable.DJICodeEditText_android_inputType, 0);
        this.imeOptions = array.getInt(R.styleable.DJICodeEditText_android_imeOptions, 0);
        this.mPercentWidth = array.getFloat(R.styleable.DJICodeEditText_layout_constraintWidth_percent, 0.0f);
        array.recycle();
    }

    public String getText() {
        return this.mEditText.getText().toString();
    }

    public void setOnInputEndCallBack(InputChangedListener onInputEndCallBack) {
        this.callBack = onInputEndCallBack;
    }

    private void init(Context context) {
        initContentLayout(context);
        initEditText(context);
        LinearLayout.LayoutParams firstParams = new LinearLayout.LayoutParams(this.borderSize, this.borderSize);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(this.borderSize, this.borderSize);
        params.setMargins(calculateBlockMargin(), 0, 0, 0);
        for (int i = 0; i < this.mEditViewNum; i++) {
            TextView textView = new TextView(getContext());
            textView.setBackgroundResource(R.drawable.shape_border_normal);
            textView.setGravity(17);
            textView.setTextSize((float) DensityUtils.sp2px(getContext(), (float) this.textSize));
            textView.getPaint().setFakeBoldText(true);
            if (i == 0) {
                textView.setLayoutParams(firstParams);
            } else {
                textView.setLayoutParams(params);
            }
            textView.setInputType(this.inputTyte);
            textView.setTextColor(this.textColor);
            textView.setText("-");
            textView.setOnClickListener(this);
            this.mTextViewsList.add(textView);
            addToContentView(textView);
        }
        this.mEditText.setOnKeyListener(new View.OnKeyListener() {
            /* class com.dji.widget2.edittext.DJICodeEditText.AnonymousClass1 */

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 67 && DJICodeEditText.this.mEditText.getText().length() < DJICodeEditText.this.mTextViewsList.size()) {
                    ((TextView) DJICodeEditText.this.mTextViewsList.get(DJICodeEditText.this.mEditText.getText().length())).setText("-");
                }
                return false;
            }
        });
    }

    private void initContentLayout(Context context) {
        this.mContentLinearLayout = new LinearLayout(context);
        this.mContentLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        addView(this.mContentLinearLayout);
    }

    private void initEditText(Context context) {
        this.mEditText = new EditText(context);
        this.mEditText.setBackgroundColor(Color.parseColor("#00000000"));
        this.mEditText.setMaxLines(1);
        this.mEditText.setInputType(2);
        this.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(this.mEditViewNum)});
        this.mEditText.addTextChangedListener(this);
        this.mEditText.setTextSize(0.0f);
        this.mEditText.setInputType(this.inputTyte);
        this.mEditText.setImeOptions(this.imeOptions);
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(this.mEditText, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mEditText.setTextSize(0.01f);
        this.mEditText.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.mEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            /* class com.dji.widget2.edittext.DJICodeEditText.AnonymousClass2 */

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }
        });
        addView(this.mEditText);
    }

    private int calculateBlockMargin() {
        if (this.mPercentWidth <= 0.0f) {
            return this.borderMargin;
        }
        float widthInPx = ((float) getContext().getResources().getDisplayMetrics().widthPixels) * this.mPercentWidth;
        if (this.mEditViewNum > 1) {
            return (int) ((widthInPx - ((float) (this.mEditViewNum * this.borderSize))) / ((float) (this.mEditViewNum - 1)));
        }
        return 0;
    }

    private void addToContentView(View view) {
        this.mContentLinearLayout.addView(view);
    }

    public void clearText() {
        this.mEditText.setText("");
        Iterator<TextView> it2 = this.mTextViewsList.iterator();
        while (it2.hasNext()) {
            it2.next().setText("-");
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(Editable s) {
        boolean z;
        String inputContent = this.mEditText.getText().toString();
        if (inputContent.length() > 0) {
            this.mEditText.setLongClickable(false);
            this.mEditText.setCursorVisible(false);
        } else {
            this.mEditText.setLongClickable(true);
            this.mEditText.setCursorVisible(true);
        }
        int len = this.mTextViewsList.size();
        for (int i = 0; i < len; i++) {
            TextView textView = this.mTextViewsList.get(i);
            if (i < inputContent.length()) {
                textView.setText(String.valueOf(inputContent.charAt(i)));
            } else {
                textView.setText("-");
            }
        }
        if (this.callBack != null) {
            if (s.length() <= 1) {
                this.mTextViewsList.get(0).setText(s);
            } else {
                this.mTextViewsList.get(this.mEditText.getText().length() - 1).setText(s.subSequence(s.length() - 1, s.length()));
            }
            InputChangedListener inputChangedListener = this.callBack;
            String obj = this.mEditText.getText().toString();
            if (s.length() == this.mEditViewNum) {
                z = true;
            } else {
                z = false;
            }
            inputChangedListener.onInput(obj, z);
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.mEditText.setEnabled(enabled);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService("input_method");
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 2);
        }
    }

    public void onClick(View v) {
        this.mEditText.setFocusable(true);
        this.mEditText.setFocusableInTouchMode(true);
        this.mEditText.requestFocus();
        ((InputMethodManager) getContext().getSystemService("input_method")).toggleSoftInput(0, 2);
    }
}
