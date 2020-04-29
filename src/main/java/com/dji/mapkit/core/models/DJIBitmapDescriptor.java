package com.dji.mapkit.core.models;

import android.graphics.Bitmap;
import android.view.View;

public final class DJIBitmapDescriptor {
    private static final String TAG = "DJIBitmapDescriptor";
    private String id;
    private Bitmap mBitmap;
    private String mPathString;
    private int mResourceId;
    private Type mType;
    private View mView;

    public enum Type {
        BITMAP,
        PATH_ABSOLUTE,
        PATH_ASSET,
        PATH_FILEINPUT,
        RESOURCE_ID,
        View
    }

    DJIBitmapDescriptor(Bitmap bitmap) {
        this.mType = Type.BITMAP;
        this.mBitmap = bitmap;
        this.mPathString = null;
        this.mResourceId = 0;
    }

    DJIBitmapDescriptor(String pathString, Type type) {
        this.mType = type;
        this.mBitmap = null;
        this.mPathString = pathString;
        this.mResourceId = 0;
    }

    DJIBitmapDescriptor(int resourceId) {
        this.mType = Type.RESOURCE_ID;
        this.mBitmap = null;
        this.mPathString = null;
        this.mResourceId = resourceId;
    }

    DJIBitmapDescriptor(View view) {
        this.mType = Type.View;
        this.mView = view;
        this.mBitmap = null;
        this.mPathString = null;
        this.mResourceId = 0;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void updateBitmap(Bitmap bitmap) {
        this.mType = Type.BITMAP;
        this.mBitmap = bitmap;
        this.mPathString = null;
        this.mResourceId = 0;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getId() {
        return this.id;
    }

    public Type getType() {
        return this.mType;
    }

    public String getPath() {
        return this.mPathString;
    }

    public int getResourceId() {
        return this.mResourceId;
    }

    public View getView() {
        return this.mView;
    }
}
