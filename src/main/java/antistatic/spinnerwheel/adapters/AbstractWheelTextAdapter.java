package antistatic.spinnerwheel.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class AbstractWheelTextAdapter extends AbstractWheelAdapter {
    public static final int DEFAULT_TEXT_COLOR = -15724528;
    public static final int DEFAULT_TEXT_SIZE = 24;
    public static final int LABEL_COLOR = -9437072;
    protected static final int NO_RESOURCE = 0;
    public static final int TEXT_VIEW_ITEM_RESOURCE = -1;
    protected Context context;
    protected int emptyItemResourceId;
    protected LayoutInflater inflater;
    protected int itemResourceId;
    protected int itemTextResourceId;
    private int textColor;
    private int textSize;
    private Typeface textTypeface;

    /* access modifiers changed from: protected */
    public abstract CharSequence getItemText(int i);

    protected AbstractWheelTextAdapter(Context context2) {
        this(context2, -1);
    }

    protected AbstractWheelTextAdapter(Context context2, int itemResource) {
        this(context2, itemResource, 0);
    }

    protected AbstractWheelTextAdapter(Context context2, int itemResource, int itemTextResource) {
        this.textColor = DEFAULT_TEXT_COLOR;
        this.textSize = 24;
        this.context = context2;
        this.itemResourceId = itemResource;
        this.itemTextResourceId = itemTextResource;
        this.inflater = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor2) {
        this.textColor = textColor2;
    }

    public void setTextTypeface(Typeface typeface) {
        this.textTypeface = typeface;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize2) {
        this.textSize = textSize2;
    }

    public int getItemResource() {
        return this.itemResourceId;
    }

    public void setItemResource(int itemResourceId2) {
        this.itemResourceId = itemResourceId2;
    }

    public int getItemTextResource() {
        return this.itemTextResourceId;
    }

    public void setItemTextResource(int itemTextResourceId2) {
        this.itemTextResourceId = itemTextResourceId2;
    }

    public int getEmptyItemResource() {
        return this.emptyItemResourceId;
    }

    public void setEmptyItemResource(int emptyItemResourceId2) {
        this.emptyItemResourceId = emptyItemResourceId2;
    }

    public View getItem(int index, View convertView, ViewGroup parent) {
        if (index < 0 || index >= getItemsCount()) {
            return null;
        }
        if (convertView == null) {
            convertView = getView(this.itemResourceId, parent);
        }
        TextView textView = getTextView(convertView, this.itemTextResourceId);
        if (textView != null) {
            CharSequence text = getItemText(index);
            if (text == null) {
                text = "";
            }
            textView.setText(text);
            configureTextView(textView);
        }
        return convertView;
    }

    public View getEmptyItem(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getView(this.emptyItemResourceId, parent);
        }
        if (convertView instanceof TextView) {
            configureTextView((TextView) convertView);
        }
        return convertView;
    }

    /* access modifiers changed from: protected */
    public void configureTextView(TextView view) {
        if (this.itemResourceId == -1) {
            view.setTextColor(this.textColor);
            view.setGravity(17);
            view.setTextSize((float) this.textSize);
            view.setLines(1);
        }
        if (this.textTypeface != null) {
            view.setTypeface(this.textTypeface);
        } else {
            view.setTypeface(Typeface.SANS_SERIF, 1);
        }
    }

    private TextView getTextView(View view, int textResource) {
        if (textResource == 0) {
            try {
                if (view instanceof TextView) {
                    return (TextView) view;
                }
            } catch (ClassCastException e) {
                Log.e("AbstractWheelAdapter", "You must supply a resource ID for a TextView");
                throw new IllegalStateException("AbstractWheelAdapter requires the resource ID to be a TextView", e);
            }
        }
        if (textResource != 0) {
            return (TextView) view.findViewById(textResource);
        }
        return null;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.view.LayoutInflater.inflate(int, android.view.ViewGroup, boolean):android.view.View}
     arg types: [int, android.view.ViewGroup, int]
     candidates:
      ClspMth{android.view.LayoutInflater.inflate(org.xmlpull.v1.XmlPullParser, android.view.ViewGroup, boolean):android.view.View}
      ClspMth{android.view.LayoutInflater.inflate(int, android.view.ViewGroup, boolean):android.view.View} */
    private View getView(int resource, ViewGroup parent) {
        switch (resource) {
            case -1:
                return new TextView(this.context);
            case 0:
                return null;
            default:
                return this.inflater.inflate(resource, parent, false);
        }
    }
}
