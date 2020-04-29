package com.dji.widget2.wheel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public abstract class DJIWheelAdapter<T, V extends View> extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_PLACEHOLDER = 1;
    private final Context mContext;
    protected ArrayList<T> mData = new ArrayList<>();
    protected boolean mIsEnabled = true;
    private final Orientation mOrientation;
    private OnItemClickListener mOuterListener;
    private int mPlaceHolderSize;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    /* access modifiers changed from: protected */
    public abstract int getItemFixSize();

    /* access modifiers changed from: protected */
    public abstract void onBindItemView(V v, T t);

    /* access modifiers changed from: protected */
    public abstract V onCreateItemView();

    public DJIWheelAdapter(@NonNull Orientation orientation, @NonNull Context context) {
        this.mOrientation = orientation;
        this.mContext = context;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new PlaceHolder(new View(parent.getContext()));
        }
        if (viewType == 0) {
            return new ItemHolder(onCreateItemView());
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(Integer.valueOf(position));
        if (holder instanceof ItemHolder) {
            holder.itemView.setEnabled(this.mIsEnabled);
            holder.itemView.setOnClickListener(new DJIWheelAdapter$$Lambda$0(this, position));
            onBindItemView(((ItemHolder) holder).itemView, this.mData.get(position - 1));
        }
        if (!(holder instanceof PlaceHolder)) {
            return;
        }
        if (this.mOrientation == Orientation.VERTICAL) {
            holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(-1, this.mPlaceHolderSize));
        } else {
            holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(this.mPlaceHolderSize, -1));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onBindViewHolder$0$DJIWheelAdapter(int position, View view) {
        if (this.mOuterListener != null) {
            this.mOuterListener.onItemClick(position - 1);
        }
    }

    public int getItemCount() {
        return this.mData.size() + 2;
    }

    public int getItemViewType(int position) {
        return (position == 0 || position == this.mData.size() + 1) ? 1 : 0;
    }

    public void updateData(List<T> list) {
        this.mData.clear();
        this.mData.addAll(list);
        notifyDataSetChanged();
    }

    /* access modifiers changed from: package-private */
    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.mOuterListener = onItemClickListener;
    }

    /* access modifiers changed from: package-private */
    public void setEnabled(boolean isEnabled) {
        this.mIsEnabled = isEnabled;
        notifyDataSetChanged();
    }

    /* access modifiers changed from: package-private */
    public void setWidgetSize(int size) {
        this.mPlaceHolderSize = size / 2;
    }

    /* access modifiers changed from: package-private */
    public int calculateTargetOffset() {
        return this.mPlaceHolderSize - (getItemFixSize() / 2);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public Context getContext() {
        return this.mContext;
    }

    private static class PlaceHolder extends RecyclerView.ViewHolder {
        PlaceHolder(View itemView) {
            super(itemView);
        }
    }

    private static class ItemHolder<V extends View> extends RecyclerView.ViewHolder {
        ItemHolder(@NonNull V itemView) {
            super(itemView);
        }
    }
}
