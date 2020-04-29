package dji.publics.DJIUI;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import dji.frame.widget.R;
import java.util.Arrays;
import java.util.HashMap;

public class DJISengmentedGroup extends RadioGroup {
    /* access modifiers changed from: private */
    public RadioGroup.OnCheckedChangeListener mCheckedChangeListener;
    private int mCheckedTextColor;
    private String[] mChildAr;
    private RadioButton[] mChilds;
    private float mChileTextSize;
    private Float mCornerRadius;
    private int mDisableColor;
    /* access modifiers changed from: private */
    public HashMap<Integer, TransitionDrawable> mDrawableMap;
    /* access modifiers changed from: private */
    public int mLastCheckId;
    private LayoutSelector mLayoutSelector;
    private int mMarginDp;
    /* access modifiers changed from: private */
    public OnSgCheckedChangeListener mSgCheckedChangeListener;
    private int mTintColor;
    private int mUnCheckedTintColor;
    private Resources resources;

    public interface OnSgCheckedChangeListener {
        void onCheckedChanged(DJISengmentedGroup dJISengmentedGroup, int i, int i2);
    }

    public DJISengmentedGroup(Context context) {
        super(context);
        this.mCheckedTextColor = -1;
        this.mLastCheckId = -1;
        this.resources = getResources();
        this.mTintColor = this.resources.getColor(17170443);
        this.mUnCheckedTintColor = this.resources.getColor(17170444);
        this.mDisableColor = this.resources.getColor(R.color.radio_text_disable_color);
        this.mMarginDp = (int) getResources().getDimension(R.dimen.dp_1_in_sw320dp);
        this.mCornerRadius = Float.valueOf(getResources().getDimension(R.dimen.dp_5_in_sw320dp));
        this.mLayoutSelector = new LayoutSelector(this.mCornerRadius.floatValue());
    }

    public DJISengmentedGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCheckedTextColor = -1;
        this.mLastCheckId = -1;
        this.resources = getResources();
        this.mTintColor = this.resources.getColor(17170443);
        this.mUnCheckedTintColor = this.resources.getColor(17170444);
        this.mMarginDp = (int) getResources().getDimension(R.dimen.dp_1_in_sw320dp);
        this.mCornerRadius = Float.valueOf(getResources().getDimension(R.dimen.dp_5_in_sw320dp));
        initAttrs(attrs);
        this.mLayoutSelector = new LayoutSelector(this.mCornerRadius.floatValue());
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SegmentedGroup, 0, 0);
        try {
            this.mMarginDp = (int) typedArray.getDimension(R.styleable.SegmentedGroup_sg_border_width, getResources().getDimension(R.dimen.dp_1_in_sw320dp));
            this.mCornerRadius = Float.valueOf(typedArray.getDimension(R.styleable.SegmentedGroup_sg_corner_radius, getResources().getDimension(R.dimen.dp_5_in_sw320dp)));
            this.mTintColor = typedArray.getColor(R.styleable.SegmentedGroup_sg_tint_color, getResources().getColor(17170443));
            this.mCheckedTextColor = typedArray.getColor(R.styleable.SegmentedGroup_sg_checked_text_color, getResources().getColor(17170444));
            this.mUnCheckedTintColor = typedArray.getColor(R.styleable.SegmentedGroup_sg_unchecked_tint_color, getResources().getColor(17170444));
            CharSequence[] sequences = typedArray.getTextArray(R.styleable.SegmentedGroup_sg_child_ar);
            if (sequences != null) {
                this.mChildAr = new String[sequences.length];
                for (int index = 0; index < sequences.length; index++) {
                    this.mChildAr[index] = sequences[index].toString();
                }
            }
            this.mChileTextSize = typedArray.getDimension(R.styleable.SegmentedGroup_sg_corner_radius, getResources().getDimension(R.dimen.widget_child_text_size));
        } finally {
            typedArray.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            this.mLastCheckId = getCheckedRadioButtonId();
            if (this.mChildAr != null) {
                this.mChilds = new RadioButton[this.mChildAr.length];
                for (int i = 0; i < this.mChildAr.length; i++) {
                    addButton(i);
                }
            }
            updateBackground();
        }
    }

    private void addButton(int index) {
        RadioButton radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.widget_radio_button_item, (ViewGroup) null);
        radioButton.setText(this.mChildAr[index]);
        radioButton.setTextSize(0, this.mChileTextSize);
        radioButton.setTag(Integer.valueOf(index));
        radioButton.setLayoutParams(new RadioGroup.LayoutParams(-2, -2, 1.0f));
        addView(radioButton);
        this.mChilds[index] = radioButton;
    }

    public void setTintColor(int tintColor) {
        this.mTintColor = tintColor;
        updateBackground();
    }

    public void setTintColor(int tintColor, int checkedTextColor) {
        this.mTintColor = tintColor;
        this.mCheckedTextColor = checkedTextColor;
        updateBackground();
    }

    public void setUnCheckedTintColor(int unCheckedTintColor, int unCheckedTextColor) {
        this.mUnCheckedTintColor = unCheckedTintColor;
        updateBackground();
    }

    public void updateBackground() {
        this.mDrawableMap = new HashMap<>();
        int count = super.getChildCount();
        int i = 0;
        while (i < count) {
            View child = getChildAt(i);
            updateBackground(child);
            if (i != count - 1) {
                RadioGroup.LayoutParams initParams = (RadioGroup.LayoutParams) child.getLayoutParams();
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(initParams.width, initParams.height, initParams.weight);
                if (getOrientation() == 0) {
                    params.setMargins(0, 0, -this.mMarginDp, 0);
                } else {
                    params.setMargins(0, 0, 0, -this.mMarginDp);
                }
                child.setLayoutParams(params);
                i++;
            } else {
                return;
            }
        }
    }

    private void updateBackground(View view) {
        int checked = this.mLayoutSelector.getSelected();
        int unchecked = this.mLayoutSelector.getUnselected();
        int disable = this.mLayoutSelector.getDisable();
        Drawable checkedDrawable = this.resources.getDrawable(checked).mutate();
        Drawable uncheckedDrawable = this.resources.getDrawable(unchecked).mutate();
        Drawable disableDrawable = this.resources.getDrawable(disable).mutate();
        ((GradientDrawable) checkedDrawable).setColor(this.mTintColor);
        ((GradientDrawable) checkedDrawable).setStroke(this.mMarginDp, this.mTintColor);
        ((GradientDrawable) uncheckedDrawable).setStroke(this.mMarginDp, this.mTintColor);
        ((GradientDrawable) uncheckedDrawable).setColor(this.mUnCheckedTintColor);
        ((GradientDrawable) checkedDrawable).setCornerRadii(this.mLayoutSelector.getChildRadii(view));
        ((GradientDrawable) uncheckedDrawable).setCornerRadii(this.mLayoutSelector.getChildRadii(view));
        ((GradientDrawable) disableDrawable).setCornerRadii(this.mLayoutSelector.getChildRadii(view));
        GradientDrawable maskDrawable = (GradientDrawable) this.resources.getDrawable(unchecked).mutate();
        maskDrawable.setStroke(this.mMarginDp, this.mTintColor);
        maskDrawable.setColor(this.mUnCheckedTintColor);
        maskDrawable.setCornerRadii(this.mLayoutSelector.getChildRadii(view));
        maskDrawable.setColor(Color.argb(50, Color.red(this.mTintColor), Color.green(this.mTintColor), Color.blue(this.mTintColor)));
        LayerDrawable pressedDrawable = new LayerDrawable(new Drawable[]{uncheckedDrawable, maskDrawable});
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{uncheckedDrawable, checkedDrawable});
        if (((RadioButton) view).isChecked()) {
            transitionDrawable.reverseTransition(0);
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-16842912, 16842919}, pressedDrawable);
        stateListDrawable.addState(new int[]{-16842910}, disableDrawable);
        stateListDrawable.addState(StateSet.WILD_CARD, transitionDrawable);
        this.mDrawableMap.put(Integer.valueOf(view.getId()), transitionDrawable);
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(stateListDrawable);
        } else {
            view.setBackgroundDrawable(stateListDrawable);
        }
        super.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            /* class dji.publics.DJIUI.DJISengmentedGroup.AnonymousClass1 */

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                TransitionDrawable last;
                TransitionDrawable last2;
                if (checkedId == -1) {
                    if (!(DJISengmentedGroup.this.mLastCheckId == -1 || (last = (TransitionDrawable) DJISengmentedGroup.this.mDrawableMap.get(Integer.valueOf(DJISengmentedGroup.this.mLastCheckId))) == null)) {
                        last.reverseTransition(200);
                    }
                    int unused = DJISengmentedGroup.this.mLastCheckId = checkedId;
                } else if (checkedId != DJISengmentedGroup.this.mLastCheckId) {
                    ((TransitionDrawable) DJISengmentedGroup.this.mDrawableMap.get(Integer.valueOf(checkedId))).reverseTransition(200);
                    if (!(DJISengmentedGroup.this.mLastCheckId == -1 || (last2 = (TransitionDrawable) DJISengmentedGroup.this.mDrawableMap.get(Integer.valueOf(DJISengmentedGroup.this.mLastCheckId))) == null)) {
                        last2.reverseTransition(200);
                    }
                    int unused2 = DJISengmentedGroup.this.mLastCheckId = checkedId;
                    if (DJISengmentedGroup.this.mSgCheckedChangeListener != null) {
                        DJISengmentedGroup.this.mSgCheckedChangeListener.onCheckedChanged(DJISengmentedGroup.this, ((Integer) group.findViewById(checkedId).getTag()).intValue(), checkedId);
                    }
                    if (DJISengmentedGroup.this.mCheckedChangeListener != null) {
                        DJISengmentedGroup.this.mCheckedChangeListener.onCheckedChanged(group, checkedId);
                    }
                }
            }
        });
        if (isEnabled()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(disableDrawable);
        } else {
            view.setBackgroundDrawable(disableDrawable);
        }
    }

    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        this.mDrawableMap.remove(Integer.valueOf(child.getId()));
    }

    public void setOnSgCheckedChangeListener(OnSgCheckedChangeListener listener) {
        this.mSgCheckedChangeListener = listener;
    }

    public void setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener listener) {
        this.mCheckedChangeListener = listener;
    }

    private class LayoutSelector {
        private final int DISABLE_LAYOUT = R.drawable.widget_radio_disable;
        private final int SELECTED_LAYOUT = R.drawable.widget_radio_checked;
        private final int UNSELECTED_LAYOUT = R.drawable.widget_radio_unchecked;
        private int child = -1;
        private int children = -1;
        private float r;
        private final float r1 = TypedValue.applyDimension(1, 0.1f, DJISengmentedGroup.this.getResources().getDisplayMetrics());
        private final float[] rBot;
        private final float[] rDefault;
        private final float[] rLeft;
        private final float[] rMiddle;
        private final float[] rRight;
        private final float[] rTop;
        private float[] radii;

        public LayoutSelector(float cornerRadius) {
            this.r = cornerRadius;
            this.rLeft = new float[]{this.r, this.r, this.r1, this.r1, this.r1, this.r1, this.r, this.r};
            this.rRight = new float[]{this.r1, this.r1, this.r, this.r, this.r, this.r, this.r1, this.r1};
            this.rMiddle = new float[]{this.r1, this.r1, this.r1, this.r1, this.r1, this.r1, this.r1, this.r1};
            this.rDefault = new float[]{this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r};
            this.rTop = new float[]{this.r, this.r, this.r, this.r, this.r1, this.r1, this.r1, this.r1};
            this.rBot = new float[]{this.r1, this.r1, this.r1, this.r1, this.r, this.r, this.r, this.r};
        }

        private int getChildren() {
            return DJISengmentedGroup.this.getChildCount();
        }

        private int getChildIndex(View view) {
            return DJISengmentedGroup.this.indexOfChild(view);
        }

        private void setChildRadii(int newChildren, int newChild) {
            if (this.children != newChildren || this.child != newChild) {
                this.children = newChildren;
                this.child = newChild;
                if (this.children == 1) {
                    this.radii = this.rDefault;
                } else if (this.child == 0) {
                    this.radii = DJISengmentedGroup.this.getOrientation() == 0 ? this.rLeft : this.rTop;
                } else if (this.child == this.children - 1) {
                    this.radii = DJISengmentedGroup.this.getOrientation() == 0 ? this.rRight : this.rBot;
                } else {
                    this.radii = this.rMiddle;
                }
            }
        }

        public int getSelected() {
            return this.SELECTED_LAYOUT;
        }

        public int getUnselected() {
            return this.UNSELECTED_LAYOUT;
        }

        public int getDisable() {
            return this.DISABLE_LAYOUT;
        }

        public float[] getChildRadii(View view) {
            setChildRadii(getChildren(), getChildIndex(view));
            return this.radii;
        }
    }

    public void checkButton(int id) {
        RadioButton radioButton = (RadioButton) findViewById(id);
        if (radioButton != null) {
            radioButton.setChecked(true);
        }
    }

    public void setEnabled(boolean enabled) {
        if (this.mChilds != null) {
            for (RadioButton button : this.mChilds) {
                button.setEnabled(enabled);
            }
        } else {
            super.setEnabled(enabled);
        }
        updateBackground();
    }

    public void setChecked(int index, boolean checked) {
        if (this.mChilds != null && this.mChilds.length > index) {
            this.mChilds[index].setChecked(checked);
        }
    }

    public void setEnabled(int index, boolean enabled) {
        if (this.mChilds != null && this.mChilds.length > index) {
            this.mChilds[index].setEnabled(enabled);
            updateBackground(this.mChilds[index]);
        }
    }

    public void setDatas(String[] datas) {
        if (datas != null && !Arrays.equals(datas, this.mChildAr)) {
            this.mChilds = new RadioButton[datas.length];
            this.mChildAr = (String[]) datas.clone();
            removeAllViews();
            for (int i = 0; i < datas.length; i++) {
                addButton(i);
            }
            updateBackground();
        }
    }
}
