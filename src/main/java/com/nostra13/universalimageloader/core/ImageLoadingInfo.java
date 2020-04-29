package com.nostra13.universalimageloader.core;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import java.util.concurrent.locks.ReentrantLock;

final class ImageLoadingInfo {
    final ImageAware imageAware;
    final ImageLoadingListener listener;
    final ReentrantLock loadFromUriLock;
    final String memoryCacheKey;
    final DisplayImageOptions options;
    final ImageLoadingProgressListener progressListener;
    final ImageSize targetSize;
    final String uri;

    public ImageLoadingInfo(String uri2, ImageAware imageAware2, ImageSize targetSize2, String memoryCacheKey2, DisplayImageOptions options2, ImageLoadingListener listener2, ImageLoadingProgressListener progressListener2, ReentrantLock loadFromUriLock2) {
        this.uri = uri2;
        this.imageAware = imageAware2;
        this.targetSize = targetSize2;
        this.options = options2;
        this.listener = listener2;
        this.progressListener = progressListener2;
        this.loadFromUriLock = loadFromUriLock2;
        this.memoryCacheKey = memoryCacheKey2;
    }
}
