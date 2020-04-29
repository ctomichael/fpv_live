package dji.publics.widget.djiviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

@SuppressLint({"NewApi"})
public class IndicatorBar extends LinearLayout {
    private static final int DEFAULT_ITEM_DISTANCE = 0;
    private static final int DEFAULT_ITEM_SIZE = 30;
    private Context context;
    private boolean isResourceColor;
    private int itemDistance;
    private int itemSize;
    private ViewPager mViewPager;
    private int nonSelectedResource;
    private int selectedIndex;
    private int selectedResource;

    public IndicatorBar(Context context2, AttributeSet attrs, int defStyle) {
        super(context2, attrs, defStyle);
        init(context2);
    }

    public IndicatorBar(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        init(context2);
    }

    public IndicatorBar(Context context2) {
        super(context2);
        init(context2);
    }

    private void init(Context context2) {
        this.context = context2;
        this.itemSize = 30;
        this.itemDistance = 0;
        this.isResourceColor = true;
        this.nonSelectedResource = getResources().getColor(17170435);
        this.selectedResource = getResources().getColor(17170444);
        initStyle();
    }

    public void setCount(int size) {
        while (getChildCount() > size) {
            removeViewAt(getChildCount() - 1);
        }
        while (getChildCount() < size) {
            addView(new View(this.context), new LinearLayout.LayoutParams(this.itemSize, this.itemSize));
        }
        initStyle();
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /* class dji.publics.widget.djiviewpager.IndicatorBar.AnonymousClass1 */

            public void onPageSelected(int position) {
                IndicatorBar.this.setSelectedIndex(position);
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setSelectedIndex(int index) {
        if (this.selectedIndex != index) {
            View lastSelectedView = getChildAt(this.selectedIndex);
            View newSelectView = getChildAt(index);
            this.selectedIndex = index;
            if (lastSelectedView != null) {
                if (this.isResourceColor) {
                    lastSelectedView.setBackgroundColor(this.nonSelectedResource);
                } else {
                    lastSelectedView.setBackgroundResource(this.nonSelectedResource);
                }
            }
            if (newSelectView == null) {
                return;
            }
            if (this.isResourceColor) {
                newSelectView.setBackgroundColor(this.selectedResource);
            } else {
                newSelectView.setBackgroundResource(this.selectedResource);
            }
        }
    }

    public void setIndicatorResource(int selectedResource2, int nonSelectedResource2) {
        this.isResourceColor = false;
        this.selectedResource = selectedResource2;
        this.nonSelectedResource = nonSelectedResource2;
        initSelectState();
    }

    public void setIndicatorColor(int selectedColor, int nonSelectedColor) {
        this.isResourceColor = true;
        this.selectedResource = selectedColor;
        this.nonSelectedResource = nonSelectedColor;
        initSelectState();
    }

    public void setItemSize(int size) {
        this.itemSize = size;
        initStyle();
    }

    public void setItemDisatance(int distance) {
        this.itemDistance = distance;
        initStyle();
    }

    private void initStyle() {
        for (int i = 0; i < getChildCount(); i++) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getChildAt(i).getLayoutParams();
            if (i == 0) {
                lp.setMargins(0, 0, 0, 0);
            } else if (getOrientation() == 0) {
                lp.setMargins(this.itemDistance, 0, 0, 0);
            } else {
                lp.setMargins(0, this.itemDistance, 0, 0);
            }
            lp.width = this.itemSize;
            lp.height = this.itemSize;
            getChildAt(i).setLayoutParams(lp);
        }
        initSelectState();
    }

    private void initSelectState() {
        for (int i = 0; i < getChildCount(); i++) {
            if (i == this.selectedIndex) {
                if (this.selectedResource != 0) {
                    if (this.isResourceColor) {
                        getChildAt(i).setBackgroundColor(this.selectedResource);
                    } else {
                        getChildAt(i).setBackgroundResource(this.selectedResource);
                    }
                }
            } else if (this.nonSelectedResource != 0) {
                if (this.isResourceColor) {
                    getChildAt(i).setBackgroundColor(this.nonSelectedResource);
                } else {
                    getChildAt(i).setBackgroundResource(this.nonSelectedResource);
                }
            }
        }
    }
}
