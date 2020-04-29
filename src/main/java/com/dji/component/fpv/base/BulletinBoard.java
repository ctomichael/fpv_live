package com.dji.component.fpv.base;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class BulletinBoard {
    private static final String BULLETIN_BOARD_KEY = "bulletin_board_key";
    protected Bundle mData;
    protected HashMap<String, Subject> subjectMap;

    public BulletinBoard() {
        this(null);
    }

    public BulletinBoard(Bundle data) {
        this.mData = data;
        if (this.mData == null) {
            this.mData = new Bundle();
        }
        this.subjectMap = new HashMap<>();
    }

    public synchronized void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.mData = savedInstanceState.getBundle(BULLETIN_BOARD_KEY);
        }
        if (this.mData == null) {
            this.mData = new Bundle();
        }
    }

    public synchronized void onDestroy() {
        this.subjectMap.clear();
        this.mData.clear();
    }

    public synchronized <T> Observable<T> getObservable(String key) {
        Subject res;
        if (this.subjectMap.containsKey(key)) {
            res = this.subjectMap.get(key);
        } else {
            res = PublishSubject.create();
            this.subjectMap.put(key, res);
        }
        return res;
    }

    public synchronized <T> Observable<T> getObservable(String key, @NonNull T defaultValue) {
        Subject res;
        if (this.subjectMap.containsKey(key)) {
            res = this.subjectMap.get(key);
        } else {
            res = PublishSubject.create();
            this.subjectMap.put(key, res);
        }
        return res.startWith((ObservableSource) Observable.fromCallable(new BulletinBoard$$Lambda$0(this, key, defaultValue)));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Object lambda$getObservable$0$BulletinBoard(String key, @NonNull Object defaultValue) throws Exception {
        return getData(key) == null ? defaultValue : getData(key);
    }

    /* access modifiers changed from: protected */
    public synchronized void notifyDataChanged(String key) {
        Object o;
        if (this.subjectMap.containsKey(key) && (o = this.mData.get(key)) != null) {
            this.subjectMap.get(key).onNext(o);
        }
    }

    public synchronized void removeData(String key) {
        this.mData.remove(key);
    }

    public synchronized Object getData(String key) {
        return this.mData.get(key);
    }

    public synchronized boolean containsKey(String key) {
        return this.mData.containsKey(key);
    }

    public synchronized void putAll(Object caller, Bundle bundle) {
        this.mData.putAll(bundle);
        for (String key : bundle.keySet()) {
            notifyDataChanged(key);
        }
    }

    public synchronized Set<String> keySet() {
        return this.mData.keySet();
    }

    public synchronized void putBoolean(@Nullable String key, boolean value) {
        this.mData.putBoolean(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putInt(@Nullable String key, int value) {
        this.mData.putInt(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putLong(@Nullable String key, long value) {
        this.mData.putLong(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putDouble(@Nullable String key, double value) {
        this.mData.putDouble(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putString(@Nullable String key, @Nullable String value) {
        this.mData.putString(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putBooleanArray(@Nullable String key, @Nullable boolean[] value) {
        this.mData.putBooleanArray(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putIntArray(@Nullable String key, @Nullable int[] value) {
        this.mData.putIntArray(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putLongArray(@Nullable String key, @Nullable long[] value) {
        this.mData.putLongArray(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putDoubleArray(@Nullable String key, @Nullable double[] value) {
        this.mData.putDoubleArray(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putStringArray(@Nullable String key, @Nullable String[] value) {
        this.mData.putStringArray(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putByte(@Nullable String key, byte value) {
        this.mData.putByte(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putChar(@Nullable String key, char value) {
        this.mData.putChar(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putShort(@Nullable String key, short value) {
        this.mData.putShort(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putFloat(@Nullable String key, float value) {
        this.mData.putFloat(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putCharSequence(@Nullable String key, @Nullable CharSequence value) {
        this.mData.putCharSequence(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putParcelable(@Nullable String key, @Nullable Parcelable value) {
        this.mData.putParcelable(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        this.mData.putParcelableArray(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        this.mData.putParcelableArrayList(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
        this.mData.putSparseParcelableArray(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        this.mData.putIntegerArrayList(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        this.mData.putStringArrayList(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        this.mData.putCharSequenceArrayList(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putSerializable(@Nullable String key, @Nullable Serializable value) {
        this.mData.putSerializable(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putByteArray(@Nullable String key, @Nullable byte[] value) {
        this.mData.putByteArray(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putShortArray(@Nullable String key, @Nullable short[] value) {
        this.mData.putShortArray(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putCharArray(@Nullable String key, @Nullable char[] value) {
        this.mData.putCharArray(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putFloatArray(@Nullable String key, @Nullable float[] value) {
        this.mData.putFloatArray(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
        this.mData.putCharSequenceArray(key, value);
        notifyDataChanged(key);
    }

    public synchronized void putBundle(@Nullable String key, @Nullable Bundle value) {
        this.mData.putBundle(key, value);
        notifyDataChanged(key);
    }

    public synchronized Bundle getBundle(String key) {
        return this.mData.getBundle(key);
    }

    public synchronized boolean getBoolean(String key) {
        return this.mData.getBoolean(key, false);
    }

    public synchronized boolean getBoolean(String key, boolean defaultValue) {
        return this.mData.getBoolean(key, defaultValue);
    }

    public synchronized boolean[] getBooleanArray(String key) {
        return this.mData.getBooleanArray(key);
    }

    public synchronized byte getByte(String key) {
        return this.mData.getByte(key);
    }

    public synchronized byte getByte(String key, byte defaultValue) {
        return this.mData.getByte(key, defaultValue).byteValue();
    }

    public synchronized byte[] getByteArray(String key) {
        return this.mData.getByteArray(key);
    }

    public synchronized char getChar(String key) {
        return this.mData.getChar(key);
    }

    public synchronized char getChar(String key, char defaultValue) {
        return this.mData.getChar(key, defaultValue);
    }

    public synchronized char[] getCharArray(String key) {
        return this.mData.getCharArray(key);
    }

    public synchronized int getInt(String key) {
        return this.mData.getInt(key);
    }

    public synchronized int getInt(String key, int defaultValue) {
        return this.mData.getInt(key, defaultValue);
    }

    public synchronized int[] getIntArray(String key) {
        return this.mData.getIntArray(key);
    }

    public synchronized ArrayList<Integer> getIntegerArrayList(String key) {
        return this.mData.getIntegerArrayList(key);
    }

    public synchronized long getLong(String key) {
        return this.mData.getLong(key);
    }

    public synchronized long getLong(String key, long defaultValue) {
        return this.mData.getLong(key, defaultValue);
    }

    public synchronized long[] getLongArray(String key) {
        return this.mData.getLongArray(key);
    }

    public synchronized short getShort(String key) {
        return this.mData.getShort(key);
    }

    public synchronized short getShort(String key, short defaultValue) {
        return this.mData.getShort(key, defaultValue);
    }

    public synchronized short[] getShortArray(String key) {
        return this.mData.getShortArray(key);
    }

    public synchronized float getFloat(String key) {
        return this.mData.getFloat(key);
    }

    public synchronized float getFloat(String key, float defaultValue) {
        return this.mData.getFloat(key, defaultValue);
    }

    public synchronized float[] getFloatArray(String key) {
        return this.mData.getFloatArray(key);
    }

    public synchronized double getDouble(String key) {
        return this.mData.getDouble(key);
    }

    public synchronized double getDouble(String key, double defaultValue) {
        return this.mData.getDouble(key, defaultValue);
    }

    public synchronized double[] getDoubleArray(String key) {
        return this.mData.getDoubleArray(key);
    }

    public synchronized String getString(String key) {
        return this.mData.getString(key);
    }

    public synchronized String getString(String key, String defaultValue) {
        return this.mData.getString(key, defaultValue);
    }

    public synchronized String[] getStringArray(String key) {
        return this.mData.getStringArray(key);
    }

    public synchronized ArrayList<String> getStringArrayList(String key) {
        return this.mData.getStringArrayList(key);
    }

    public synchronized Serializable getSerializable(String key) {
        return this.mData.getSerializable(key);
    }

    public synchronized <T extends Parcelable> T getParcelable(String key) {
        return this.mData.getParcelable(key);
    }

    public synchronized <T extends Parcelable> T getParcelable(String key, T defaultValue) {
        T value = this.mData.getParcelable(key);
        if (value != null) {
            defaultValue = value;
        }
        return defaultValue;
    }

    public synchronized Parcelable[] getParcelableArray(String key) {
        return this.mData.getParcelableArray(key);
    }

    public synchronized <T extends Parcelable> ArrayList<T> getParcelableArrayList(String key) {
        return this.mData.getParcelableArrayList(key);
    }

    public synchronized <T extends Parcelable> SparseArray<T> getSparseParcelableArray(String key) {
        return this.mData.getSparseParcelableArray(key);
    }

    public synchronized CharSequence getCharSequence(String key) {
        return this.mData.getCharSequence(key);
    }

    public synchronized CharSequence getCharSequence(String key, CharSequence defaultValue) {
        return this.mData.getCharSequence(key, defaultValue);
    }

    public synchronized CharSequence[] getCharSequenceArray(String key) {
        return this.mData.getCharSequenceArray(key);
    }

    public synchronized ArrayList<CharSequence> getCharSequenceArrayList(String key) {
        return this.mData.getCharSequenceArrayList(key);
    }
}
