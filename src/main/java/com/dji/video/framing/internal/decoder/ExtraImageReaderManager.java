package com.dji.video.framing.internal.decoder;

import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.Pools;
import android.view.Surface;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.VideoFrame;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ExtraImageReaderManager {
    private static final int MAX_IMAGE_READER_CACHE_SIZE = 2;
    private static final int MAX_POOL_SIZE = 256;
    private static final String TAG = "ExtraImageReaderManager";
    /* access modifiers changed from: private */
    public ExtraImageReaderCallback mExtraImageReaderCallback;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public int mHeight;
    private ImageReader mImageReader;
    /* access modifiers changed from: private */
    public ConcurrentLinkedDeque<ImageFromVideoFrame> mLinkedQueue;
    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        /* class com.dji.video.framing.internal.decoder.ExtraImageReaderManager.AnonymousClass1 */

        public void onImageAvailable(ImageReader reader) {
            Image image = reader.acquireNextImage();
            try {
                if (ExtraImageReaderManager.this.mExtraImageReaderCallback != null) {
                    while (image != null) {
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        ImageFromVideoFrame imageFromVideoFrame = (ImageFromVideoFrame) ExtraImageReaderManager.this.mLinkedQueue.poll();
                        int timestampFromFrame = -1;
                        if (imageFromVideoFrame == null) {
                            VideoLog.e(ExtraImageReaderManager.TAG, "onImageAvailable() imageFromVideoFrame == null", new Object[0]);
                        } else {
                            imageFromVideoFrame.timestampQueueOut = System.currentTimeMillis();
                            timestampFromFrame = imageFromVideoFrame.timestampFromFrame;
                            imageFromVideoFrame.recycle();
                        }
                        if (timestampFromFrame == -1) {
                            timestampFromFrame = ExtraImageReaderManager.this.mTimestampPredictor.getPredictValue();
                        }
                        if (reader.getMaxImages() == 2 && ExtraImageReaderManager.this.mLinkedQueue.size() > 2) {
                            ExtraImageReaderManager.this.mLinkedQueue.clear();
                            ExtraImageReaderManager.this.mLinkedQueue.offer((ImageFromVideoFrame) ExtraImageReaderManager.this.mLinkedQueue.peekLast());
                        }
                        ExtraImageReaderManager.this.mExtraImageReaderCallback.onImageOutput(buffer, ExtraImageReaderManager.this.mWidth, ExtraImageReaderManager.this.mHeight, timestampFromFrame);
                        image.close();
                        image = reader.acquireNextImage();
                    }
                }
                if (image != null) {
                    image.close();
                }
            } catch (Exception e) {
                VideoLog.e(ExtraImageReaderManager.TAG, "onImageAvailable() error", e, new Object[0]);
                if (image != null) {
                    image.close();
                }
            } catch (Throwable th) {
                if (image != null) {
                    image.close();
                }
                throw th;
            }
        }
    };
    /* access modifiers changed from: private */
    public TimestampPredictor mTimestampPredictor;
    /* access modifiers changed from: private */
    public int mWidth;

    interface TimestampPredictor {
        void destroy();

        int getPredictValue();

        void inputValue(int i);
    }

    public interface ExtraImageReaderCallback {
        void onImageOutput(ByteBuffer byteBuffer, int i, int i2, int i3);
    }

    public ExtraImageReaderManager(Looper looper) {
        VideoLog.d(TAG, "ExtraImageReaderManager()", new Object[0]);
        this.mHandler = new Handler(looper);
        this.mLinkedQueue = new ConcurrentLinkedDeque<>();
        this.mTimestampPredictor = new SimpleTimestampPredictor();
    }

    public Surface getSurface() {
        return this.mImageReader.getSurface();
    }

    public void resetVideo(int width, int height) {
        VideoLog.d(TAG, "resetVideo() width:" + width + " height:" + height, new Object[0]);
        stopGetExtraARGBImage(false);
        this.mWidth = width;
        this.mHeight = height;
        startGetExtraARGBImage(this.mWidth, this.mHeight);
    }

    public void stopGetExtraARGBImage() {
        stopGetExtraARGBImage(true);
    }

    private void stopGetExtraARGBImage(boolean isClearCallback) {
        VideoLog.w(TAG, "stopGetExtraARGBImage()", new Object[0]);
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        this.mTimestampPredictor.destroy();
        this.mLinkedQueue.clear();
        if (this.mImageReader != null) {
            this.mImageReader.close();
            this.mImageReader = null;
        }
        if (isClearCallback) {
            this.mExtraImageReaderCallback = null;
        }
    }

    public void setExtraImageReaderCallback(ExtraImageReaderCallback callback) {
        this.mExtraImageReaderCallback = callback;
    }

    public boolean hasImageReaderCallback() {
        return this.mExtraImageReaderCallback != null;
    }

    public void offer(VideoFrame videoFrame) {
        ImageFromVideoFrame image = ImageFromVideoFrame.obtain();
        image.frameNum = videoFrame.frameNum;
        image.timestampFromFrame = videoFrame.timeStamp;
        image.timestampQueueIn = System.currentTimeMillis();
        this.mLinkedQueue.offer(image);
        if (videoFrame.timeStamp != -1) {
            this.mTimestampPredictor.inputValue(videoFrame.timeStamp);
        }
    }

    public void startGetExtraARGBImage(int width, int height) {
        VideoLog.w(TAG, "startGetExtraARGBImage() width:" + width + " height:" + height, new Object[0]);
        if (this.mImageReader == null) {
            this.mWidth = width;
            this.mHeight = height;
            this.mImageReader = ImageReader.newInstance(this.mWidth, this.mHeight, 1, 2);
            this.mImageReader.setOnImageAvailableListener(this.mOnImageAvailableListener, this.mHandler);
        }
    }

    static final class ImageFromVideoFrame {
        private static final Pools.SynchronizedPool<ImageFromVideoFrame> sPool = new Pools.SynchronizedPool<>(256);
        int frameNum;
        int timestampFromFrame;
        long timestampQueueIn;
        long timestampQueueOut;

        public static ImageFromVideoFrame obtain() {
            ImageFromVideoFrame instance = sPool.acquire();
            return instance != null ? instance : new ImageFromVideoFrame();
        }

        public void recycle() {
            sPool.release(this);
        }
    }

    private static class SimpleTimestampPredictor implements TimestampPredictor {
        private long offset;

        private SimpleTimestampPredictor() {
        }

        public void inputValue(int timestampFromFrame) {
            this.offset = System.currentTimeMillis() - ((long) timestampFromFrame);
        }

        public int getPredictValue() {
            return (int) (System.currentTimeMillis() - this.offset);
        }

        public void destroy() {
        }
    }
}
