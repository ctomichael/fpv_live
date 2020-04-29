package com.dji.widget2.dialog.content.standard.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.Space;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.dji.widget2.R;
import com.dji.widget2.dialog.DialogAttributes;
import com.dji.widget2.dialog.DialogViewInterface;
import java.util.ArrayList;
import java.util.List;

public class SelectableListView extends LinearLayout implements DialogViewInterface {
    private static final int LIST_ITEM_DIVIDER_HEIGHT = 3;
    public static final int LIST_MULTI_TYPE = 1;
    public static final int LIST_SINGLE_TYPE = 0;
    /* access modifiers changed from: private */
    public boolean isShowCheckbox = true;
    private SingleAdapter mAdapter;
    /* access modifiers changed from: private */
    public int mContentListGravity = 8388627;
    private DialogInterface.OnClickListener mItemClickListener;
    protected ImageView mIvIcon;
    protected ListView mListView;
    private DialogInterface.OnMultiChoiceClickListener mMultiChoiceClickListener;
    protected Space mSpace1;
    protected Space mSpace2;
    protected Space mSpace3;
    protected Space mSpace4;
    private DialogAttributes.Theme mTheme = DialogAttributes.Theme.LIGHT;
    protected TextView mTvMain;

    public SelectableListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mListView = (ListView) findViewById(R.id.list);
        this.mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        this.mTvMain = (TextView) findViewById(R.id.tv_main);
        this.mSpace1 = (Space) findViewById(R.id.space_1);
        this.mSpace2 = (Space) findViewById(R.id.space_2);
        this.mSpace3 = (Space) findViewById(R.id.space_3);
        this.mSpace4 = (Space) findViewById(R.id.space_4);
    }

    public void setTheme(@NonNull DialogAttributes.Theme theme) {
        this.mTheme = theme;
        switch (this.mTheme) {
            case LIGHT:
                this.mTvMain.setTextColor(getResources().getColor(R.color.black));
                this.mListView.setBackgroundResource(R.color.grey_1);
                this.mListView.setDivider(new ColorDrawable(getResources().getColor(R.color.black_05)));
                this.mListView.setDividerHeight(3);
                this.mListView.invalidate();
                break;
            case FPV:
                this.mTvMain.setTextColor(getResources().getColor(R.color.white));
                this.mListView.setBackgroundResource(R.color.black_80);
                this.mListView.setDivider(new ColorDrawable(getResources().getColor(R.color.white_10)));
                this.mListView.setDividerHeight(3);
                this.mListView.invalidate();
                break;
        }
        if (this.mAdapter != null) {
            this.mAdapter.setTheme(this.mTheme);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void setIcon(@DrawableRes int resId) {
        this.mIvIcon.setImageResource(resId);
        updateVisibility();
    }

    public void setMainText(@StringRes int resId) {
        this.mTvMain.setText(resId);
        updateVisibility();
    }

    public void setMainText(@NonNull CharSequence text) {
        this.mTvMain.setText(text);
        updateVisibility();
    }

    public void setContentListGravity(int contentListGravity) {
        this.mContentListGravity = contentListGravity;
    }

    public void setShowCheckbox(boolean showCheckbox) {
        this.isShowCheckbox = showCheckbox;
    }

    public void setData(int listType, @NonNull CharSequence[] textArray, int[] selectedIndexArray) {
        this.mAdapter = new SingleAdapter(getContext(), listType, textArray, selectedIndexArray);
        this.mAdapter.setTheme(this.mTheme);
        this.mAdapter.setOnItemClickListener(new SelectableListView$$Lambda$0(this));
        this.mAdapter.setOnMultiChoiceClickListener(new SelectableListView$$Lambda$1(this));
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        int totalHeight = getTotalHeightOfListView(this.mListView);
        if (totalHeight > 0 && totalHeight < this.mListView.getLayoutParams().height) {
            ViewGroup.LayoutParams lp = this.mListView.getLayoutParams();
            lp.height = totalHeight;
            this.mListView.setLayoutParams(lp);
        }
        this.mSpace4.setVisibility(listType == 1 ? 8 : 0);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$setData$0$SelectableListView(DialogInterface dialog, int which) {
        if (this.mItemClickListener != null) {
            this.mItemClickListener.onClick(dialog, which);
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$setData$1$SelectableListView(DialogInterface dialog, int which, boolean isChecked) {
        if (this.mMultiChoiceClickListener != null) {
            this.mMultiChoiceClickListener.onClick(dialog, which, isChecked);
        }
    }

    public void setOnItemClickListener(@Nullable DialogInterface.OnClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnMultiChoiceClickListener(@Nullable DialogInterface.OnMultiChoiceClickListener listener) {
        this.mMultiChoiceClickListener = listener;
    }

    private void updateVisibility() {
        int i;
        int i2;
        int i3 = 0;
        TextView textView = this.mTvMain;
        if (TextUtils.isEmpty(this.mTvMain.getText())) {
            i = 8;
        } else {
            i = 0;
        }
        textView.setVisibility(i);
        ImageView imageView = this.mIvIcon;
        if (this.mIvIcon.getDrawable() == null) {
            i2 = 8;
        } else {
            i2 = 0;
        }
        imageView.setVisibility(i2);
        Space space = this.mSpace2;
        if (!(this.mTvMain.getVisibility() == 0 && this.mIvIcon.getVisibility() == 0)) {
            i3 = 8;
        }
        space.setVisibility(i3);
    }

    private int getTotalHeightOfListView(ListView listView) {
        ListAdapter mAdapter2 = listView.getAdapter();
        int count = mAdapter2.getCount();
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View mView = mAdapter2.getView(i, null, listView);
            mView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            totalHeight += mView.getMeasuredHeight();
        }
        if (count > 1) {
            return totalHeight + ((mAdapter2.getCount() - 1) * 3);
        }
        return totalHeight;
    }

    private class SingleAdapter extends BaseAdapter {
        private Context mContext;
        private List<DataWrapper> mData;
        private int mListType;
        private DialogInterface.OnClickListener mOnItemClickListener;
        private DialogInterface.OnMultiChoiceClickListener mOnMultiChoiceClickListener;
        private int mSelectableItemBackground;
        private DialogAttributes.Theme mTheme;

        SingleAdapter(Context context, int listType, CharSequence[] textArray, int[] selectedIndexArray) {
            this.mListType = listType;
            this.mContext = context;
            this.mData = DataWrapper.createFromArray(textArray, selectedIndexArray);
            TypedValue value = new TypedValue();
            context.getTheme().resolveAttribute(16843534, value, true);
            this.mSelectableItemBackground = value.resourceId;
        }

        public int getCount() {
            if (this.mData == null) {
                return 0;
            }
            return this.mData.size();
        }

        public Object getItem(int position) {
            if (this.mData == null || this.mData.size() <= position) {
                return null;
            }
            return this.mData.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.app_layout_dialog_list_single_item, (ViewGroup) null);
                vh.mRootView = (LinearLayout) convertView;
                vh.mLeftTv = (TextView) convertView.findViewById(16908308);
                vh.mLeftTv.setGravity(SelectableListView.this.mContentListGravity);
                vh.mRightCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                if (!SelectableListView.this.isShowCheckbox) {
                    vh.mRightCheckBox.setVisibility(8);
                }
                vh.mRootView.setBackgroundResource(this.mSelectableItemBackground);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            applyTheme(vh);
            applyData(vh, this.mData.get(position));
            applyListener(vh, position);
            convertView.setTag(vh);
            return convertView;
        }

        public void setTheme(DialogAttributes.Theme theme) {
            this.mTheme = theme;
        }

        public void setOnItemClickListener(@NonNull DialogInterface.OnClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        public void setOnMultiChoiceClickListener(@NonNull DialogInterface.OnMultiChoiceClickListener listener) {
            this.mOnMultiChoiceClickListener = listener;
        }

        private void applyTheme(ViewHolder vh) {
            if (vh != null) {
                switch (this.mTheme) {
                    case LIGHT:
                        vh.mLeftTv.setTextColor(this.mContext.getResources().getColor(R.color.black_100));
                        return;
                    case FPV:
                        vh.mLeftTv.setTextColor(this.mContext.getResources().getColor(R.color.white_100));
                        return;
                    default:
                        return;
                }
            }
        }

        private void applyData(ViewHolder vh, DataWrapper dataWrapper) {
            vh.mLeftTv.setText(dataWrapper.text);
            vh.mRightCheckBox.setChecked(dataWrapper.isSelected);
        }

        private void applyListener(ViewHolder vh, int position) {
            if (this.mListType == 0 && this.mOnItemClickListener != null) {
                applySingleListener(vh, position);
            } else if (this.mListType == 1 && this.mOnMultiChoiceClickListener != null) {
                applyMultiListener(vh, position);
            }
        }

        private void applySingleListener(ViewHolder vh, int position) {
            View.OnClickListener onClickListener = new SelectableListView$SingleAdapter$$Lambda$0(this, vh, position);
            vh.mRootView.setOnClickListener(onClickListener);
            vh.mRightCheckBox.setOnClickListener(onClickListener);
        }

        /* access modifiers changed from: package-private */
        public final /* synthetic */ void lambda$applySingleListener$0$SelectableListView$SingleAdapter(ViewHolder vh, int position, View v) {
            vh.mRightCheckBox.setSelected(true);
            this.mData.get(position).isSelected = true;
            int size = this.mData.size();
            for (int i = 0; i < size; i++) {
                if (i != position) {
                    this.mData.get(i).isSelected = false;
                }
            }
            notifyDataSetChanged();
            this.mOnItemClickListener.onClick(null, position);
        }

        private void applyMultiListener(ViewHolder vh, int position) {
            View.OnClickListener onClickListener = new SelectableListView$SingleAdapter$$Lambda$1(this, position);
            vh.mRootView.setOnClickListener(onClickListener);
            vh.mRightCheckBox.setOnClickListener(onClickListener);
        }

        /* access modifiers changed from: package-private */
        public final /* synthetic */ void lambda$applyMultiListener$1$SelectableListView$SingleAdapter(int position, View v) {
            this.mData.get(position).isSelected = !this.mData.get(position).isSelected;
            notifyDataSetChanged();
            this.mOnMultiChoiceClickListener.onClick(null, position, this.mData.get(position).isSelected);
        }

        private final class ViewHolder {
            TextView mLeftTv;
            CheckBox mRightCheckBox;
            LinearLayout mRootView;

            private ViewHolder() {
            }
        }
    }

    private static final class DataWrapper {
        boolean isSelected;
        CharSequence text;

        DataWrapper(CharSequence text2, boolean isSelected2) {
            this.text = text2;
            this.isSelected = isSelected2;
        }

        static List<DataWrapper> createFromArray(CharSequence[] textArray, int[] selectedIndex) {
            List<DataWrapper> data = new ArrayList<>();
            for (CharSequence charSequence : textArray) {
                data.add(new DataWrapper(charSequence, false));
            }
            for (int index : selectedIndex) {
                if (index <= data.size()) {
                    ((DataWrapper) data.get(index)).isSelected = true;
                }
            }
            return data;
        }
    }
}
