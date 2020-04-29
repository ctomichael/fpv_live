package com.mapbox.mapboxsdk.maps;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.TransitionOptions;
import com.mapbox.mapboxsdk.style.light.Light;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Style {
    public static final String DARK = "mapbox://styles/mapbox/dark-v10";
    static final String EMPTY_JSON = "{\"version\": 8,\"sources\": {},\"layers\": []}";
    public static final String LIGHT = "mapbox://styles/mapbox/light-v10";
    public static final String MAPBOX_STREETS = "mapbox://styles/mapbox/streets-v11";
    public static final String OUTDOORS = "mapbox://styles/mapbox/outdoors-v11";
    public static final String SATELLITE = "mapbox://styles/mapbox/satellite-v9";
    public static final String SATELLITE_STREETS = "mapbox://styles/mapbox/satellite-streets-v11";
    public static final String TRAFFIC_DAY = "mapbox://styles/mapbox/traffic-day-v2";
    public static final String TRAFFIC_NIGHT = "mapbox://styles/mapbox/traffic-night-v2";
    private final Builder builder;
    private boolean fullyLoaded;
    private final HashMap<String, Bitmap> images;
    private final HashMap<String, Layer> layers;
    private final NativeMap nativeMap;
    private final HashMap<String, Source> sources;

    public interface OnStyleLoaded {
        void onStyleLoaded(@NonNull Style style);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface StyleUrl {
    }

    private Style(@NonNull Builder builder2, @NonNull NativeMap nativeMap2) {
        this.sources = new HashMap<>();
        this.layers = new HashMap<>();
        this.images = new HashMap<>();
        this.builder = builder2;
        this.nativeMap = nativeMap2;
    }

    @Deprecated
    @NonNull
    public String getUrl() {
        validateState("getUrl");
        return this.nativeMap.getStyleUri();
    }

    @NonNull
    public String getUri() {
        validateState("getUri");
        return this.nativeMap.getStyleUri();
    }

    @NonNull
    public String getJson() {
        validateState("getJson");
        return this.nativeMap.getStyleJson();
    }

    @NonNull
    public List<Source> getSources() {
        validateState("getSources");
        return this.nativeMap.getSources();
    }

    public void addSource(@NonNull Source source) {
        validateState("addSource");
        this.nativeMap.addSource(source);
        this.sources.put(source.getId(), source);
    }

    @Nullable
    public Source getSource(String id) {
        validateState("getSource");
        Source source = this.sources.get(id);
        if (source == null) {
            return this.nativeMap.getSource(id);
        }
        return source;
    }

    @Nullable
    public <T extends Source> T getSourceAs(@NonNull String sourceId) {
        validateState("getSourceAs");
        if (this.sources.containsKey(sourceId)) {
            return (Source) this.sources.get(sourceId);
        }
        return this.nativeMap.getSource(sourceId);
    }

    public boolean removeSource(@NonNull String sourceId) {
        validateState("removeSource");
        this.sources.remove(sourceId);
        return this.nativeMap.removeSource(sourceId);
    }

    public boolean removeSource(@NonNull Source source) {
        validateState("removeSource");
        this.sources.remove(source.getId());
        return this.nativeMap.removeSource(source);
    }

    public void addLayer(@NonNull Layer layer) {
        validateState("addLayer");
        this.nativeMap.addLayer(layer);
        this.layers.put(layer.getId(), layer);
    }

    public void addLayerBelow(@NonNull Layer layer, @NonNull String below) {
        validateState("addLayerBelow");
        this.nativeMap.addLayerBelow(layer, below);
        this.layers.put(layer.getId(), layer);
    }

    public void addLayerAbove(@NonNull Layer layer, @NonNull String above) {
        validateState("addLayerAbove");
        this.nativeMap.addLayerAbove(layer, above);
        this.layers.put(layer.getId(), layer);
    }

    public void addLayerAt(@NonNull Layer layer, @IntRange(from = 0) int index) {
        validateState("addLayerAbove");
        this.nativeMap.addLayerAt(layer, index);
        this.layers.put(layer.getId(), layer);
    }

    @Nullable
    public Layer getLayer(@NonNull String id) {
        validateState("getLayer");
        Layer layer = this.layers.get(id);
        if (layer == null) {
            return this.nativeMap.getLayer(id);
        }
        return layer;
    }

    @Nullable
    public <T extends Layer> T getLayerAs(@NonNull String layerId) {
        validateState("getLayerAs");
        return this.nativeMap.getLayer(layerId);
    }

    @NonNull
    public List<Layer> getLayers() {
        validateState("getLayers");
        return this.nativeMap.getLayers();
    }

    public boolean removeLayer(@NonNull String layerId) {
        validateState("removeLayer");
        this.layers.remove(layerId);
        return this.nativeMap.removeLayer(layerId);
    }

    public boolean removeLayer(@NonNull Layer layer) {
        validateState("removeLayer");
        this.layers.remove(layer.getId());
        return this.nativeMap.removeLayer(layer);
    }

    public boolean removeLayerAt(@IntRange(from = 0) int index) {
        validateState("removeLayerAt");
        return this.nativeMap.removeLayerAt(index);
    }

    public void addImage(@NonNull String name, @NonNull Bitmap image) {
        addImage(name, image, false);
    }

    public void addImage(@NonNull String name, @NonNull Drawable drawable) {
        Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);
        if (bitmap == null) {
            throw new IllegalArgumentException("Provided drawable couldn't be converted to a Bitmap.");
        }
        addImage(name, bitmap, false);
    }

    public void addImage(@NonNull String name, @NonNull Bitmap bitmap, boolean sdf) {
        validateState("addImage");
        this.nativeMap.addImages(new Image[]{toImage(new Builder.ImageWrapper(name, bitmap, sdf))});
    }

    public void addImageAsync(@NonNull String name, @NonNull Bitmap image) {
        addImageAsync(name, image, false);
    }

    public void addImageAsync(@NonNull String name, @NonNull Drawable drawable) {
        Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);
        if (bitmap == null) {
            throw new IllegalArgumentException("Provided drawable couldn't be converted to a Bitmap.");
        }
        addImageAsync(name, bitmap, false);
    }

    public void addImageAsync(@NonNull String name, @NonNull Bitmap bitmap, boolean sdf) {
        validateState("addImage");
        new BitmapImageConversionTask(this.nativeMap).execute(new Builder.ImageWrapper(name, bitmap, sdf));
    }

    public void addImages(@NonNull HashMap<String, Bitmap> images2) {
        addImages(images2, false);
    }

    public void addImages(@NonNull HashMap<String, Bitmap> images2, boolean sdf) {
        validateState("addImage");
        Image[] convertedImages = new Image[images2.size()];
        int index = 0;
        for (Builder.ImageWrapper imageWrapper : Builder.ImageWrapper.convertToImageArray(images2, sdf)) {
            convertedImages[index] = toImage(imageWrapper);
            index++;
        }
        this.nativeMap.addImages(convertedImages);
    }

    public void addImagesAsync(@NonNull HashMap<String, Bitmap> images2) {
        addImagesAsync(images2, false);
    }

    public void addImagesAsync(@NonNull HashMap<String, Bitmap> images2, boolean sdf) {
        validateState("addImages");
        new BitmapImageConversionTask(this.nativeMap).execute(Builder.ImageWrapper.convertToImageArray(images2, sdf));
    }

    public void removeImage(@NonNull String name) {
        validateState("removeImage");
        this.nativeMap.removeImage(name);
    }

    @Nullable
    public Bitmap getImage(@NonNull String id) {
        validateState("getImage");
        return this.nativeMap.getImage(id);
    }

    public void setTransition(@NonNull TransitionOptions transitionOptions) {
        validateState("setTransition");
        this.nativeMap.setTransitionOptions(transitionOptions);
    }

    @NonNull
    public TransitionOptions getTransition() {
        validateState("getTransition");
        return this.nativeMap.getTransitionOptions();
    }

    @Nullable
    public Light getLight() {
        validateState("getLight");
        return this.nativeMap.getLight();
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        this.fullyLoaded = false;
        for (Layer layer : this.layers.values()) {
            if (layer != null) {
                layer.setDetached();
            }
        }
        for (Source source : this.sources.values()) {
            if (source != null) {
                source.setDetached();
            }
        }
        for (Map.Entry<String, Bitmap> bitmapEntry : this.images.entrySet()) {
            this.nativeMap.removeImage((String) bitmapEntry.getKey());
            ((Bitmap) bitmapEntry.getValue()).recycle();
        }
        this.sources.clear();
        this.layers.clear();
        this.images.clear();
    }

    /* access modifiers changed from: package-private */
    public void onDidFinishLoadingStyle() {
        if (!this.fullyLoaded) {
            this.fullyLoaded = true;
            for (Source source : this.builder.sources) {
                addSource(source);
            }
            for (Builder.LayerWrapper layerWrapper : this.builder.layers) {
                if (layerWrapper instanceof Builder.LayerAtWrapper) {
                    addLayerAt(layerWrapper.layer, ((Builder.LayerAtWrapper) layerWrapper).index);
                } else if (layerWrapper instanceof Builder.LayerAboveWrapper) {
                    addLayerAbove(layerWrapper.layer, ((Builder.LayerAboveWrapper) layerWrapper).aboveLayer);
                } else if (layerWrapper instanceof Builder.LayerBelowWrapper) {
                    addLayerBelow(layerWrapper.layer, ((Builder.LayerBelowWrapper) layerWrapper).belowLayer);
                } else {
                    addLayerBelow(layerWrapper.layer, MapboxConstants.LAYER_ID_ANNOTATIONS);
                }
            }
            for (Builder.ImageWrapper image : this.builder.images) {
                addImage(image.id, image.bitmap, image.sdf);
            }
            if (this.builder.transitionOptions != null) {
                setTransition(this.builder.transitionOptions);
            }
        }
    }

    public boolean isFullyLoaded() {
        return this.fullyLoaded;
    }

    private void validateState(String methodCall) {
        if (!this.fullyLoaded) {
            throw new IllegalStateException(String.format("Calling %s when a newer style is loading/has loaded.", methodCall));
        }
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public final List<ImageWrapper> images = new ArrayList();
        /* access modifiers changed from: private */
        public final List<LayerWrapper> layers = new ArrayList();
        /* access modifiers changed from: private */
        public final List<Source> sources = new ArrayList();
        private String styleJson;
        private String styleUri;
        /* access modifiers changed from: private */
        public TransitionOptions transitionOptions;

        @Deprecated
        @NonNull
        public Builder fromUrl(@NonNull String url) {
            this.styleUri = url;
            return this;
        }

        @NonNull
        public Builder fromUri(@NonNull String uri) {
            this.styleUri = uri;
            return this;
        }

        @NonNull
        public Builder fromJson(@NonNull String styleJson2) {
            this.styleJson = styleJson2;
            return this;
        }

        @NonNull
        public Builder withSource(@NonNull Source source) {
            this.sources.add(source);
            return this;
        }

        @NonNull
        public Builder withSources(@NonNull Source... sources2) {
            this.sources.addAll(Arrays.asList(sources2));
            return this;
        }

        @NonNull
        public Builder withLayer(@NonNull Layer layer) {
            this.layers.add(new LayerWrapper(layer));
            return this;
        }

        @NonNull
        public Builder withLayers(@NonNull Layer... layers2) {
            for (Layer layer : layers2) {
                this.layers.add(new LayerWrapper(layer));
            }
            return this;
        }

        @NonNull
        public Builder withLayerAt(@NonNull Layer layer, int index) {
            this.layers.add(new LayerAtWrapper(layer, index));
            return this;
        }

        @NonNull
        public Builder withLayerAbove(@NonNull Layer layer, @NonNull String aboveLayerId) {
            this.layers.add(new LayerAboveWrapper(layer, aboveLayerId));
            return this;
        }

        @NonNull
        public Builder withLayerBelow(@NonNull Layer layer, @NonNull String belowLayerId) {
            this.layers.add(new LayerBelowWrapper(layer, belowLayerId));
            return this;
        }

        @NonNull
        public Builder withTransition(@NonNull TransitionOptions transition) {
            this.transitionOptions = transition;
            return this;
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.mapbox.mapboxsdk.maps.Style.Builder.withImage(java.lang.String, android.graphics.Bitmap, boolean):com.mapbox.mapboxsdk.maps.Style$Builder
         arg types: [java.lang.String, android.graphics.Bitmap, int]
         candidates:
          com.mapbox.mapboxsdk.maps.Style.Builder.withImage(java.lang.String, android.graphics.drawable.Drawable, boolean):com.mapbox.mapboxsdk.maps.Style$Builder
          com.mapbox.mapboxsdk.maps.Style.Builder.withImage(java.lang.String, android.graphics.Bitmap, boolean):com.mapbox.mapboxsdk.maps.Style$Builder */
        @NonNull
        public Builder withImage(@NonNull String id, @NonNull Drawable drawable) {
            Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);
            if (bitmap != null) {
                return withImage(id, bitmap, false);
            }
            throw new IllegalArgumentException("Provided drawable couldn't be converted to a Bitmap.");
        }

        @NonNull
        public Builder withDrawableImages(@NonNull Pair<String, Drawable>... values) {
            return withDrawableImages(false, values);
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.mapbox.mapboxsdk.maps.Style.Builder.withImage(java.lang.String, android.graphics.Bitmap, boolean):com.mapbox.mapboxsdk.maps.Style$Builder
         arg types: [java.lang.String, android.graphics.Bitmap, int]
         candidates:
          com.mapbox.mapboxsdk.maps.Style.Builder.withImage(java.lang.String, android.graphics.drawable.Drawable, boolean):com.mapbox.mapboxsdk.maps.Style$Builder
          com.mapbox.mapboxsdk.maps.Style.Builder.withImage(java.lang.String, android.graphics.Bitmap, boolean):com.mapbox.mapboxsdk.maps.Style$Builder */
        @NonNull
        public Builder withImage(@NonNull String id, @NonNull Bitmap image) {
            return withImage(id, image, false);
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: com.mapbox.mapboxsdk.maps.Style.Builder.withImage(java.lang.String, android.graphics.Bitmap, boolean):com.mapbox.mapboxsdk.maps.Style$Builder
         arg types: [java.lang.String, android.graphics.Bitmap, int]
         candidates:
          com.mapbox.mapboxsdk.maps.Style.Builder.withImage(java.lang.String, android.graphics.drawable.Drawable, boolean):com.mapbox.mapboxsdk.maps.Style$Builder
          com.mapbox.mapboxsdk.maps.Style.Builder.withImage(java.lang.String, android.graphics.Bitmap, boolean):com.mapbox.mapboxsdk.maps.Style$Builder */
        @NonNull
        public Builder withBitmapImages(@NonNull Pair<String, Bitmap>... values) {
            for (Pair<String, Bitmap> value : values) {
                withImage((String) value.first, (Bitmap) value.second, false);
            }
            return this;
        }

        @NonNull
        public Builder withImage(@NonNull String id, @NonNull Drawable drawable, boolean sdf) {
            Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);
            if (bitmap != null) {
                return withImage(id, bitmap, sdf);
            }
            throw new IllegalArgumentException("Provided drawable couldn't be converted to a Bitmap.");
        }

        @NonNull
        public Builder withDrawableImages(boolean sdf, @NonNull Pair<String, Drawable>... values) {
            for (Pair<String, Drawable> value : values) {
                Bitmap bitmap = BitmapUtils.getBitmapFromDrawable((Drawable) value.second);
                if (bitmap == null) {
                    throw new IllegalArgumentException("Provided drawable couldn't be converted to a Bitmap.");
                }
                withImage((String) value.first, bitmap, sdf);
            }
            return this;
        }

        @NonNull
        public Builder withImage(@NonNull String id, @NonNull Bitmap image, boolean sdf) {
            this.images.add(new ImageWrapper(id, image, sdf));
            return this;
        }

        @NonNull
        public Builder withBitmapImages(boolean sdf, @NonNull Pair<String, Bitmap>... values) {
            for (Pair<String, Bitmap> value : values) {
                withImage((String) value.first, (Bitmap) value.second, sdf);
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public String getUri() {
            return this.styleUri;
        }

        /* access modifiers changed from: package-private */
        public String getJson() {
            return this.styleJson;
        }

        /* access modifiers changed from: package-private */
        public List<Source> getSources() {
            return this.sources;
        }

        /* access modifiers changed from: package-private */
        public List<LayerWrapper> getLayers() {
            return this.layers;
        }

        /* access modifiers changed from: package-private */
        public List<ImageWrapper> getImages() {
            return this.images;
        }

        /* access modifiers changed from: package-private */
        public TransitionOptions getTransitionOptions() {
            return this.transitionOptions;
        }

        /* access modifiers changed from: package-private */
        public Style build(@NonNull NativeMap nativeMap) {
            return new Style(this, nativeMap);
        }

        static class ImageWrapper {
            Bitmap bitmap;
            String id;
            boolean sdf;

            ImageWrapper(String id2, Bitmap bitmap2, boolean sdf2) {
                this.id = id2;
                this.bitmap = bitmap2;
                this.sdf = sdf2;
            }

            static ImageWrapper[] convertToImageArray(HashMap<String, Bitmap> bitmapHashMap, boolean sdf2) {
                ImageWrapper[] images = new ImageWrapper[bitmapHashMap.size()];
                List<String> keyList = new ArrayList<>(bitmapHashMap.keySet());
                for (int i = 0; i < bitmapHashMap.size(); i++) {
                    String id2 = (String) keyList.get(i);
                    images[i] = new ImageWrapper(id2, bitmapHashMap.get(id2), sdf2);
                }
                return images;
            }
        }

        class LayerWrapper {
            Layer layer;

            LayerWrapper(Layer layer2) {
                this.layer = layer2;
            }
        }

        class LayerAboveWrapper extends LayerWrapper {
            String aboveLayer;

            LayerAboveWrapper(Layer layer, String aboveLayer2) {
                super(layer);
                this.aboveLayer = aboveLayer2;
            }
        }

        class LayerBelowWrapper extends LayerWrapper {
            String belowLayer;

            LayerBelowWrapper(Layer layer, String belowLayer2) {
                super(layer);
                this.belowLayer = belowLayer2;
            }
        }

        class LayerAtWrapper extends LayerWrapper {
            int index;

            LayerAtWrapper(Layer layer, int index2) {
                super(layer);
                this.index = index2;
            }
        }
    }

    /* access modifiers changed from: private */
    public static Image toImage(Builder.ImageWrapper imageWrapper) {
        Bitmap bitmap = imageWrapper.bitmap;
        if (bitmap.getConfig() != Bitmap.Config.ARGB_8888) {
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        }
        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(buffer);
        return new Image(buffer.array(), ((float) bitmap.getDensity()) / 160.0f, imageWrapper.id, bitmap.getWidth(), bitmap.getHeight(), imageWrapper.sdf);
    }

    private static class BitmapImageConversionTask extends AsyncTask<Builder.ImageWrapper, Void, Image[]> {
        private WeakReference<NativeMap> nativeMap;

        BitmapImageConversionTask(NativeMap nativeMap2) {
            this.nativeMap = new WeakReference<>(nativeMap2);
        }

        /* access modifiers changed from: protected */
        @NonNull
        public Image[] doInBackground(Builder.ImageWrapper... params) {
            List<Image> images = new ArrayList<>();
            for (Builder.ImageWrapper param : params) {
                images.add(Style.toImage(param));
            }
            return (Image[]) images.toArray(new Image[images.size()]);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(@NonNull Image[] images) {
            super.onPostExecute((Object) images);
            NativeMap nativeMap2 = this.nativeMap.get();
            if (nativeMap2 != null && !nativeMap2.isDestroyed()) {
                nativeMap2.addImages(images);
            }
        }
    }
}
