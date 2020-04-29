package com.dji.mapkit.core.maps;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.dji.mapkit.core.Mapkit;
import com.dji.mapkit.core.MapkitOptions;
import com.dji.mapkit.core.R;
import com.dji.mapkit.core.providers.MapProvider;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Keep
public class DJIMapView extends FrameLayout {
    private static final int MAP_PROVIDER_AMAP = 2;
    private static final int MAP_PROVIDER_GOOGLEMAP = 1;
    private static final int MAP_PROVIDER_MAPBOX = 3;
    private DJIMapViewInternal internalMapView;

    public interface OnDJIMapReadyCallback {
        void onDJIMapReady(DJIMap dJIMap);
    }

    public DJIMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DJIMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DJIMapView);
        int defaultProvider = typedArray.getInt(R.styleable.DJIMapView_defaultProvider, 2);
        MapkitOptions.Builder builder = new MapkitOptions.Builder();
        builder.addMapProvider(defaultProvider);
        initialise((Activity) context, builder.build());
        typedArray.recycle();
    }

    public DJIMapView(Context context, MapkitOptions options) {
        super(context);
        initialise(context, options);
    }

    private void initialise(@NonNull Context context, @NonNull MapkitOptions options) {
        List<Integer> providerList = options.getProviderList();
        int i = 0;
        while (i < providerList.size()) {
            try {
                this.internalMapView = ((MapProvider) Class.forName(Mapkit.getMapProviderClassName(providerList.get(i).intValue())).getConstructor(new Class[0]).newInstance(new Object[0])).dispatchMapViewRequest(context, options);
                if (this.internalMapView != null) {
                    break;
                }
                i++;
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoClassDefFoundError | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (this.internalMapView != null) {
            addView((View) this.internalMapView);
            return;
        }
        this.internalMapView = new DJIEmptyMapView();
        Toast.makeText(getContext(), getContext().getString(R.string.provider_init_failed), 1).show();
    }

    public void onCreate(Bundle savedInstanceState) {
        this.internalMapView.onCreate(savedInstanceState);
    }

    public void onStart() {
        this.internalMapView.onStart();
    }

    public void onResume() {
        this.internalMapView.onResume();
    }

    public void onPause() {
        this.internalMapView.onPause();
    }

    public void onStop() {
        this.internalMapView.onStop();
    }

    public void onDestroy() {
        this.internalMapView.onDestroy();
    }

    public void onSaveInstanceState(Bundle outState) {
        this.internalMapView.onSaveInstanceState(outState);
    }

    public void onLowMemory() {
        this.internalMapView.onLowMemory();
    }

    public void getDJIMapAsync(@NonNull OnDJIMapReadyCallback callback) {
        this.internalMapView.getDJIMapAsync(callback);
    }

    public DJIMapViewInternal getInternalMapView() {
        return this.internalMapView;
    }
}
