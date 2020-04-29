package dji.publics.DJIObject;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class DJIBaseAdapter extends BaseAdapter {
    private int count;

    public abstract View createItemView(int i, ViewGroup viewGroup);

    public abstract void freshItemView(int i, View view);

    /* access modifiers changed from: protected */
    public void setCount(int count2) {
        this.count = count2;
    }

    public int getCount() {
        return this.count;
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createItemView(position, parent);
        }
        freshItemView(position, convertView);
        return convertView;
    }
}
