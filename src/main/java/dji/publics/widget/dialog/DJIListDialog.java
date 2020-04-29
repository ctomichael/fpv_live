package dji.publics.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import dji.publics.DJIUI.DJILinearLayout;
import dji.publics.DJIUI.DJIListView;
import dji.publics.DJIUI.DJITextView;
import dji.publics.widget.dialog.DJIDialog;
import dji.publics.widget.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DJIListDialog extends DJIDialog {
    public static final int LIST_DOUBLE_TYPE = 1;
    public static final int LIST_LEFT_MULTI_TYPE = 4;
    public static final int LIST_LEFT_SINGLE_TYPE = 2;
    public static final int LIST_RIGHT_MULTI_TYPE = 5;
    public static final int LIST_RIGHT_SINGLE_TYPE = 3;
    public static final int LIST_SINGLE_TYPE = 0;
    private DJDialogListAdpter mAdapter;
    /* access modifiers changed from: private */
    public List<Boolean> mCheckList;
    private DJIListView mListView;

    public DJIListDialog(Context context) {
        super(context);
        initView();
    }

    public DJIListDialog(Context context, DJIDialogType dialogType, DJIDialog.DJIDialogTheme theme) {
        super(context, dialogType, theme);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.dlg_list_view, (ViewGroup) null);
        this.mListView = (DJIListView) view.findViewById(R.id.dlg_listview);
        setCustomView(view);
        if (this.mDialogType != DJIDialogType.LARGE) {
            updateDividerHeight(this.mTitleContentMargin, Utils.getDimens(this.mContext, R.dimen.new_dialog_margin_left));
        }
    }

    public void setSingleRowItems(CharSequence[] items, boolean isCenter, DialogInterface.OnClickListener listener) {
        this.mListView.setDividerHeight(Utils.getDimens(this.mContext, listener == null ? R.dimen.list_divider_height : R.dimen.list_checkbox_divider_height));
        this.mAdapter = new DJDialogListAdpter(this.mContext, 0, items, null);
        this.mAdapter.setSingleRowCenter(isCenter);
        this.mAdapter.setOnItemClickListener(listener);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        measureListView();
    }

    public void setItems(CharSequence[] items, CharSequence[] nextItems, DialogInterface.OnClickListener listener) {
        this.mListView.setDividerHeight(Utils.getDimens(this.mContext, listener == null ? R.dimen.list_divider_height : R.dimen.list_checkbox_divider_height));
        this.mAdapter = new DJDialogListAdpter(this.mContext, (items == null || nextItems == null) ? 0 : 1, items, nextItems);
        this.mAdapter.setOnItemClickListener(listener);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        measureListView();
    }

    public void setSingleChoiceLeftItems(CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener) {
        this.mListView.setDividerHeight(Utils.getDimens(this.mContext, R.dimen.list_checkbox_divider_height));
        this.mCheckList = new ArrayList(items.length);
        for (int i = 0; i < items.length; i++) {
            if (i == checkedItem) {
                this.mCheckList.add(true);
            } else {
                this.mCheckList.add(false);
            }
        }
        this.mAdapter = new DJDialogListAdpter(this.mContext, 2, items, null);
        this.mAdapter.setOnItemClickListener(listener);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        measureListView();
    }

    public void setSingleChoiceRightItems(CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener) {
        this.mListView.setDividerHeight(Utils.getDimens(this.mContext, R.dimen.list_checkbox_divider_height));
        this.mCheckList = new ArrayList(items.length);
        for (int i = 0; i < items.length; i++) {
            if (i == checkedItem) {
                this.mCheckList.add(true);
            } else {
                this.mCheckList.add(false);
            }
        }
        this.mAdapter = new DJDialogListAdpter(this.mContext, 3, items, null);
        this.mAdapter.setOnItemClickListener(listener);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        measureListView();
    }

    public void setMultiChoiceLeftItems(CharSequence[] items, Boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
        this.mListView.setDividerHeight(Utils.getDimens(this.mContext, R.dimen.list_checkbox_divider_height));
        if (checkedItems == null) {
            this.mCheckList = new ArrayList(items.length);
            for (int i = 0; i < items.length; i++) {
                this.mCheckList.add(false);
            }
        } else {
            this.mCheckList = Arrays.asList(checkedItems);
        }
        this.mAdapter = new DJDialogListAdpter(this.mContext, 4, items, null);
        this.mAdapter.setOnMultiChoiceClickListener(listener);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        measureListView();
    }

    public void setMultiChoiceRightItems(CharSequence[] items, Boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
        this.mListView.setDividerHeight(Utils.getDimens(this.mContext, R.dimen.list_checkbox_divider_height));
        if (checkedItems == null) {
            this.mCheckList = new ArrayList(items.length);
            for (int i = 0; i < items.length; i++) {
                this.mCheckList.add(false);
            }
        } else {
            this.mCheckList = Arrays.asList(checkedItems);
        }
        this.mAdapter = new DJDialogListAdpter(this.mContext, 5, items, null);
        this.mAdapter.setOnMultiChoiceClickListener(listener);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        measureListView();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void measureListView() {
        int count = this.mAdapter.getCount();
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View listItem = this.mAdapter.getView(i, null, this.mListView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            }
            listItem.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = this.mListView.getLayoutParams();
        int height = totalHeight + (this.mListView.getDividerHeight() * (count - 1));
        int minHeight = Utils.getDimens(this.mContext, R.dimen.list_min_height);
        int maxHeight = Utils.getDimens(this.mContext, R.dimen.list_max_height);
        if (minHeight > height) {
            params.height = minHeight;
        } else if (height <= maxHeight) {
            params.height = height;
        } else if (height > maxHeight) {
            params.height = maxHeight;
        }
        this.mListView.setLayoutParams(params);
    }

    private class DJDialogListAdpter extends BaseAdapter {
        private List<CharSequence> mData1;
        private List<CharSequence> mData2;
        private LayoutInflater mInflater;
        /* access modifiers changed from: private */
        public DialogInterface.OnClickListener mItemClickListener;
        private int mListType = 0;
        /* access modifiers changed from: private */
        public DialogInterface.OnMultiChoiceClickListener mOnMultiChoiceClickListener;
        private boolean mSingleRowCenter = false;

        public DJDialogListAdpter(Context context, int listType, CharSequence[] items, CharSequence[] nextItems) {
            this.mListType = listType;
            if (items != null) {
                this.mData1 = Arrays.asList(items);
            }
            if (nextItems != null) {
                this.mData2 = Arrays.asList(nextItems);
            }
            this.mInflater = LayoutInflater.from(context);
        }

        public void setSingleRowCenter(boolean isCenter) {
            this.mSingleRowCenter = isCenter;
        }

        public int getCount() {
            int size1 = 0;
            int size2 = 0;
            if (this.mData1 != null) {
                size1 = this.mData1.size();
            }
            if (this.mData2 != null) {
                size2 = this.mData2.size();
            }
            return size1 > size2 ? size1 : size2;
        }

        public Object getItem(int position) {
            if (this.mData1 != null && position < this.mData1.size()) {
                return this.mData1.get(position);
            }
            if (this.mData2 == null || position >= this.mData2.size()) {
                return null;
            }
            return this.mData2.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = this.mInflater.inflate(R.layout.list_item_view, (ViewGroup) null);
                holder.mLeftCheckBox = (CheckBox) convertView.findViewById(R.id.left_checkbox);
                holder.mLeftTv = (DJITextView) convertView.findViewById(R.id.left_tv);
                holder.mRightTv = (DJITextView) convertView.findViewById(R.id.right_tv);
                holder.mRightCheckBox = (CheckBox) convertView.findViewById(R.id.right_checkbox);
                holder.mItemView = (DJILinearLayout) convertView.findViewById(R.id.list_item_ly);
                if (this.mSingleRowCenter) {
                    holder.mLeftTv.setGravity(17);
                } else {
                    holder.mLeftTv.setGravity(16);
                }
                DJIListDialog.this.initContentColor(holder.mLeftTv);
                DJIListDialog.this.initContentColor(holder.mRightTv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (this.mListType == 0) {
                holder.mLeftCheckBox.setVisibility(8);
                holder.mRightTv.setVisibility(8);
                holder.mRightCheckBox.setVisibility(8);
                if (this.mItemClickListener != null) {
                    holder.mItemView.setOnClickListener(new View.OnClickListener() {
                        /* class dji.publics.widget.dialog.DJIListDialog.DJDialogListAdpter.AnonymousClass1 */

                        public void onClick(View v) {
                            DJDialogListAdpter.this.mItemClickListener.onClick(DJIListDialog.this, position);
                        }
                    });
                }
            } else if (this.mListType == 2) {
                holder.mLeftCheckBox.setChecked(((Boolean) DJIListDialog.this.mCheckList.get(position)).booleanValue());
                holder.mLeftCheckBox.setBackgroundResource(R.drawable.dlg_single_checkbox);
                holder.mLeftCheckBox.setVisibility(0);
                holder.mRightTv.setVisibility(8);
                holder.mRightCheckBox.setVisibility(8);
                holder.mLeftCheckBox.setOnClickListener(new View.OnClickListener() {
                    /* class dji.publics.widget.dialog.DJIListDialog.DJDialogListAdpter.AnonymousClass2 */

                    public void onClick(View v) {
                        DJDialogListAdpter.this.updateSingleBox((CheckBox) v, position);
                    }
                });
                final CheckBox checkBox = holder.mLeftCheckBox;
                holder.mItemView.setOnClickListener(new View.OnClickListener() {
                    /* class dji.publics.widget.dialog.DJIListDialog.DJDialogListAdpter.AnonymousClass3 */

                    public void onClick(View v) {
                        DJDialogListAdpter.this.updateSingleBox(checkBox, position);
                    }
                });
            } else if (this.mListType == 4) {
                holder.mLeftCheckBox.setChecked(((Boolean) DJIListDialog.this.mCheckList.get(position)).booleanValue());
                holder.mLeftCheckBox.setBackgroundResource(R.drawable.dlg_multi_checkbox);
                holder.mLeftCheckBox.setVisibility(0);
                holder.mRightTv.setVisibility(8);
                holder.mRightCheckBox.setVisibility(8);
                holder.mLeftCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    /* class dji.publics.widget.dialog.DJIListDialog.DJDialogListAdpter.AnonymousClass4 */

                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (DJDialogListAdpter.this.mOnMultiChoiceClickListener != null) {
                            DJDialogListAdpter.this.mOnMultiChoiceClickListener.onClick(DJIListDialog.this, position, isChecked);
                        }
                        DJIListDialog.this.mCheckList.set(position, Boolean.valueOf(isChecked));
                    }
                });
                final CheckBox checkBox2 = holder.mLeftCheckBox;
                holder.mItemView.setOnClickListener(new View.OnClickListener() {
                    /* class dji.publics.widget.dialog.DJIListDialog.DJDialogListAdpter.AnonymousClass5 */

                    public void onClick(View v) {
                        boolean z;
                        boolean z2 = true;
                        if (DJDialogListAdpter.this.mOnMultiChoiceClickListener != null) {
                            DialogInterface.OnMultiChoiceClickListener access$400 = DJDialogListAdpter.this.mOnMultiChoiceClickListener;
                            DJIListDialog dJIListDialog = DJIListDialog.this;
                            int i = position;
                            if (!checkBox2.isChecked()) {
                                z = true;
                            } else {
                                z = false;
                            }
                            access$400.onClick(dJIListDialog, i, z);
                        }
                        CheckBox checkBox = checkBox2;
                        if (checkBox2.isChecked()) {
                            z2 = false;
                        }
                        checkBox.setChecked(z2);
                    }
                });
            } else if (this.mListType == 1) {
                holder.mLeftCheckBox.setVisibility(8);
                holder.mRightTv.setVisibility(0);
                holder.mRightCheckBox.setVisibility(8);
                if (this.mItemClickListener != null) {
                    holder.mItemView.setOnClickListener(new View.OnClickListener() {
                        /* class dji.publics.widget.dialog.DJIListDialog.DJDialogListAdpter.AnonymousClass6 */

                        public void onClick(View v) {
                            DJDialogListAdpter.this.mItemClickListener.onClick(DJIListDialog.this, position);
                        }
                    });
                }
            } else if (this.mListType == 3) {
                holder.mLeftCheckBox.setVisibility(8);
                holder.mRightTv.setVisibility(8);
                holder.mRightCheckBox.setChecked(((Boolean) DJIListDialog.this.mCheckList.get(position)).booleanValue());
                holder.mRightCheckBox.setBackgroundResource(R.drawable.dlg_single_checkbox);
                holder.mRightCheckBox.setVisibility(0);
                holder.mRightCheckBox.setOnClickListener(new View.OnClickListener() {
                    /* class dji.publics.widget.dialog.DJIListDialog.DJDialogListAdpter.AnonymousClass7 */

                    public void onClick(View v) {
                        DJDialogListAdpter.this.updateSingleBox((CheckBox) v, position);
                    }
                });
                final CheckBox checkBox3 = holder.mRightCheckBox;
                holder.mItemView.setOnClickListener(new View.OnClickListener() {
                    /* class dji.publics.widget.dialog.DJIListDialog.DJDialogListAdpter.AnonymousClass8 */

                    public void onClick(View v) {
                        DJDialogListAdpter.this.updateSingleBox(checkBox3, position);
                    }
                });
            } else if (this.mListType == 5) {
                holder.mRightCheckBox.setChecked(((Boolean) DJIListDialog.this.mCheckList.get(position)).booleanValue());
                holder.mLeftCheckBox.setVisibility(8);
                holder.mRightTv.setVisibility(8);
                holder.mRightCheckBox.setBackgroundResource(R.drawable.dlg_multi_checkbox);
                holder.mRightCheckBox.setVisibility(0);
                holder.mRightCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    /* class dji.publics.widget.dialog.DJIListDialog.DJDialogListAdpter.AnonymousClass9 */

                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (DJDialogListAdpter.this.mOnMultiChoiceClickListener != null) {
                            DJDialogListAdpter.this.mOnMultiChoiceClickListener.onClick(DJIListDialog.this, position, isChecked);
                        }
                        DJIListDialog.this.mCheckList.set(position, Boolean.valueOf(isChecked));
                    }
                });
                final CheckBox checkBox4 = holder.mRightCheckBox;
                holder.mItemView.setOnClickListener(new View.OnClickListener() {
                    /* class dji.publics.widget.dialog.DJIListDialog.DJDialogListAdpter.AnonymousClass10 */

                    public void onClick(View v) {
                        boolean z;
                        boolean z2 = true;
                        if (DJDialogListAdpter.this.mOnMultiChoiceClickListener != null) {
                            DialogInterface.OnMultiChoiceClickListener access$400 = DJDialogListAdpter.this.mOnMultiChoiceClickListener;
                            DJIListDialog dJIListDialog = DJIListDialog.this;
                            int i = position;
                            if (!checkBox4.isChecked()) {
                                z = true;
                            } else {
                                z = false;
                            }
                            access$400.onClick(dJIListDialog, i, z);
                        }
                        CheckBox checkBox = checkBox4;
                        if (checkBox4.isChecked()) {
                            z2 = false;
                        }
                        checkBox.setChecked(z2);
                    }
                });
            }
            if (this.mData1 != null && position < this.mData1.size()) {
                holder.mLeftTv.setText(this.mData1.get(position));
            }
            if (this.mData2 != null && position < this.mData2.size()) {
                holder.mRightTv.setText(this.mData2.get(position));
            }
            return convertView;
        }

        /* access modifiers changed from: private */
        public void updateSingleBox(CheckBox checkBox, int position) {
            if (this.mItemClickListener != null) {
                this.mItemClickListener.onClick(DJIListDialog.this, position);
            }
            if (((Boolean) DJIListDialog.this.mCheckList.get(position)).booleanValue()) {
                checkBox.setChecked(true);
                return;
            }
            int size = DJIListDialog.this.mCheckList.size();
            for (int i = 0; i < size; i++) {
                if (i == position) {
                    DJIListDialog.this.mCheckList.set(i, true);
                } else {
                    DJIListDialog.this.mCheckList.set(i, false);
                }
            }
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(DialogInterface.OnClickListener listener) {
            this.mItemClickListener = listener;
        }

        public void setOnMultiChoiceClickListener(DialogInterface.OnMultiChoiceClickListener listener) {
            this.mOnMultiChoiceClickListener = listener;
        }

        public void clear() {
            if (this.mData1 != null) {
                this.mData1 = null;
            }
            if (this.mData2 != null) {
                this.mData2 = null;
            }
        }
    }

    private static class ViewHolder {
        public DJILinearLayout mItemView;
        public CheckBox mLeftCheckBox;
        public DJITextView mLeftTv;
        public CheckBox mRightCheckBox;
        public DJITextView mRightTv;

        private ViewHolder() {
        }
    }
}
