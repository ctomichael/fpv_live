package dji.midware.media.transcode.online;

import android.media.MediaCodec;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.network.DJIFeatureFlags;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.metadata.VideoMetadataManager;
import dji.midware.media.record.H264FrameListener;
import dji.midware.media.transcode.online.Frame;
import dji.midware.util.BytesUtil;
import dji.midware.util.save.StreamDataObserver;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

@EXClassNullAway
public class OnlineTranscoder implements Runnable, H264FrameListener, DecoderTranscodeListener {
    public static boolean DEBUG = false;
    private static boolean DEBUG_WRITE_TRANSCODED_STREAM = false;
    private static boolean DEBUG_WRITE_YUV_IMAGE = false;
    public static String TAG_Decoder = "Transcoder_Decoder";
    public static String TAG_H264 = "Transcoder_H264";
    public static String TAG_Internal = "Transcoder_Internal";
    public static String TAG_Output = "Transcoder_Output";
    private static OnlineTranscoder instance = null;
    private BufferedOutputStream h264BufferedOutputStream = null;
    private OutputStream h264OutputStream = null;
    boolean hasDumpYUV = false;
    private IFrameMaker iframeMaker = new IFrameMaker();
    private MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
    private long lastIFrame;
    DJILinkDaemonService linkDaemonService = null;
    private LinkedList<OnlineTranscoderListener> listenerList = new LinkedList<>();
    private Object listenerSync = new Object();
    private ProductType mCurrentProduct;
    int numFrameJumped = 0;
    private int numFrameWritten;
    private FrameBuffer originBuffer;
    private int preFrameHeight;
    private int preFrameWidth;
    private FrameBuffer replaceBuffer;
    private boolean running = false;
    private OnlineTranscoderStatus status = OnlineTranscoderStatus.STANDBY;
    private Thread threadTranscoder;
    boolean toSkipHead = false;
    Object waitForReplace = new Object();

    public interface OnlineTranscoderListener {
        void onFrameInput(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo, int i, int i2, int i3);
    }

    protected enum OnlineTranscoderStatus {
        STANDBY,
        TRANSCODING
    }

    public static synchronized OnlineTranscoder getInstance() {
        OnlineTranscoder onlineTranscoder;
        synchronized (OnlineTranscoder.class) {
            if (instance == null) {
                instance = new OnlineTranscoder();
            }
            onlineTranscoder = instance;
        }
        return onlineTranscoder;
    }

    private void h264FileCreate() {
        try {
            if (DEBUG_WRITE_TRANSCODED_STREAM) {
                this.h264OutputStream = new FileOutputStream(VideoMetadataManager.getSourceVideoDirectory() + "test.h264");
                if (this.h264OutputStream != null) {
                    this.h264BufferedOutputStream = new BufferedOutputStream(this.h264OutputStream);
                    if (DJIVideoUtil.isDebug(DEBUG)) {
                        Log.i(TAG_Internal, "An H264 File has been opened");
                    }
                } else if (DJIVideoUtil.isDebug(DEBUG)) {
                    Log.e(TAG_Internal, "error in creating H264 File");
                }
            }
        } catch (IOException e2) {
            MediaLogger.show(e2);
        }
    }

    private void h264FileWrite(ByteBuffer data, int start, int length) {
        if (DEBUG_WRITE_TRANSCODED_STREAM) {
            try {
                if (this.h264BufferedOutputStream != null) {
                    this.h264BufferedOutputStream.write(data.array(), start, length);
                    if (this.numFrameWritten % 15 == 0) {
                        this.h264BufferedOutputStream.flush();
                    }
                }
            } catch (Exception e) {
                MediaLogger.show(e);
            }
        }
    }

    private void h264FileClose() {
        if (DEBUG_WRITE_TRANSCODED_STREAM) {
            try {
                if (this.h264BufferedOutputStream != null) {
                    this.h264BufferedOutputStream.close();
                    this.h264BufferedOutputStream = null;
                }
                if (this.h264OutputStream != null) {
                    this.h264OutputStream.close();
                    this.h264OutputStream = null;
                }
                if (DJIVideoUtil.isDebug(DEBUG)) {
                    Log.i(TAG_Internal, "H264 file has been closed");
                }
            } catch (Exception e) {
                Log.e(TAG_Internal, "error when closing H264 file");
                e.printStackTrace();
            }
        }
    }

    private void startService() {
        if (this.originBuffer == null) {
            this.originBuffer = new FrameBuffer(138240, 30, Frame.FrameType.H264);
        }
        if (this.replaceBuffer == null) {
            this.replaceBuffer = new FrameBuffer(11059200, 3, Frame.FrameType.YUV);
        }
        this.originBuffer.init();
        this.replaceBuffer.init();
        this.numFrameJumped = 0;
        this.numFrameWritten = 0;
        this.preFrameWidth = 0;
        this.preFrameHeight = 0;
        this.hasDumpYUV = false;
        this.toSkipHead = true;
        this.lastIFrame = -1;
        h264FileCreate();
        this.running = true;
        this.threadTranscoder = new Thread(this, "OnlineTranscoder");
        this.threadTranscoder.start();
    }

    public void attachListenerToUpstream() {
        DJIVideoDataRecver.getInstance().setH264FrameListener(true, this);
        while (true) {
            DJIVideoDecoder decoder = ServiceManager.getInstance().getDecoder();
            if (decoder == null) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                decoder.setDecoderTranscodeListener(this);
                return;
            }
        }
    }

    public void detachListenerToUpstream() {
        try {
            DJIVideoDataRecver dataRecver = DJIVideoDataRecver.getInstance();
            if (dataRecver != null) {
                dataRecver.setH264FrameListener(true, null);
            }
            DJIVideoDecoder decoder = ServiceManager.getInstance().getDecoder();
            if (decoder != null) {
                decoder.setDecoderTranscodeListener(null);
            }
        } catch (Exception e) {
            MediaLogger.show(e);
        }
    }

    public void destroy() {
        this.iframeMaker.deinit();
    }

    public synchronized void addListener(OnlineTranscoderListener listener) {
        synchronized (this.listenerSync) {
            if (!this.listenerList.contains(listener)) {
                this.listenerList.add(listener);
            }
        }
        if (this.status == OnlineTranscoderStatus.STANDBY) {
            this.status = OnlineTranscoderStatus.TRANSCODING;
            startService();
        }
    }

    public synchronized void removeListener(OnlineTranscoderListener listener) {
        synchronized (this.listenerSync) {
            this.listenerList.remove(listener);
        }
        if (this.listenerList.isEmpty() && this.status == OnlineTranscoderStatus.TRANSCODING) {
            stopService();
            this.status = OnlineTranscoderStatus.STANDBY;
        }
    }

    private boolean isGDR() {
        if (this.linkDaemonService == null) {
            this.linkDaemonService = DJILinkDaemonService.getInstance();
        }
        return DJIVideoDecoder.getIframeRawId(DJIProductManager.getInstance().getType(), 1920, 720) > 0 || DJIVideoDecoder.needFetchIframe();
    }

    public void onH264FrameInput(byte[] videoBuffer, int size, long frameIndex, boolean isKeyFrame) {
        long j = 1;
        if (this.running) {
            StreamDataObserver onDataRecv = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.OnlineTranscoderFrameInput).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) size);
            StreamDataObserver.ObservingContext observingContext = StreamDataObserver.ObservingContext.KeyframeNum;
            if (!isKeyFrame) {
                j = 0;
            }
            onDataRecv.onDataRecv(observingContext, j);
            try {
                if (ServiceManager.getInstance().getDecoder() == null) {
                    return;
                }
                if (!isKeyFrame || isGDR()) {
                    Frame frame = this.originBuffer.getFrame();
                    if (frame != null) {
                        frame.getBuffer().setData(ByteBuffer.wrap(videoBuffer, 0, size));
                        frame.setIndex(frameIndex);
                        frame.setIsKeyFrame(false);
                        this.originBuffer.queue(frame);
                    } else if (DJIVideoUtil.isDebug(DEBUG)) {
                        Log.i(TAG_H264, "notKeyFrame h264 frame pool is empty");
                    }
                } else {
                    Frame frame2 = this.originBuffer.getFrame();
                    if (frame2 != null) {
                        frame2.getBuffer().setData(ByteBuffer.wrap(videoBuffer, 0, size));
                        frame2.setIndex(frameIndex);
                        frame2.setIsKeyFrame(true);
                        this.originBuffer.queue(frame2);
                    } else if (DJIVideoUtil.isDebug(DEBUG)) {
                        Log.i(TAG_H264, "notKeyFrame h264 frame pool is empty");
                    }
                }
            } catch (Exception e) {
                Log.e(TAG_H264, MediaLogger.eToStr(e));
            }
        }
    }

    private boolean needWaitIFrame(long com_pts) {
        boolean re;
        int frame_num = DJIVideoUtil.getFrameNum(com_pts);
        int frame_index = DJIVideoUtil.getFrameIndex(com_pts);
        long org_pts = DJIVideoUtil.getPtsMs(com_pts);
        if (frame_num != 0) {
            re = false;
        } else if (this.toSkipHead) {
            re = true;
        } else {
            re = ((long) frame_index) - this.lastIFrame >= 31;
        }
        if (DJIVideoUtil.isDebug(DEBUG)) {
            Log.i("needWaitIFrame", "Thread " + Thread.currentThread().getName() + String.format(Locale.US, " queries needWaitIFrame com_pts=%s, frame_index=%d frame_num=%d, org_pts=%d, re=%b", Long.toHexString(com_pts), Integer.valueOf(frame_index), Integer.valueOf(frame_num), Long.valueOf(org_pts), Boolean.valueOf(re)));
        }
        return re;
    }

    public boolean needMakeIFrame(long com_pts) {
        if (!isGDR()) {
            return false;
        }
        return needWaitIFrame(com_pts);
    }

    public Frame getRawFrameContainer() {
        return this.replaceBuffer.getFrame();
    }

    public void feedRawFrame(Frame frame) {
        if (!this.running) {
            if (frame != null) {
                releaseFrame(frame);
            }
        } else if (frame != null) {
            if (DJIVideoUtil.isDebug(DEBUG)) {
                Log.i(TAG_Decoder, "receive a frame from decoder with pts=" + frame.getIndex());
            }
            try {
                this.replaceBuffer.queue(frame);
            } catch (Exception e) {
                Log.e(TAG_Decoder, MediaLogger.eToStr(e));
            }
            synchronized (this.waitForReplace) {
                this.waitForReplace.notify();
            }
        } else if (DJIVideoUtil.isDebug(DEBUG)) {
            Log.e(TAG_Decoder, "error: feed a null raw frame");
        }
    }

    private void writeYUVToFile(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) throws IOException {
        if (DEBUG_WRITE_YUV_IMAGE && !this.hasDumpYUV) {
            this.hasDumpYUV = true;
            byte[] temp = new byte[DJIFeatureFlags.DEFAULT_MAXIMUM_CACHE_SIZE];
            OutputStream os = new FileOutputStream(VideoMetadataManager.getSourceVideoDirectory() + System.currentTimeMillis() + "_bi.yuv");
            BufferedOutputStream bos = new BufferedOutputStream(os);
            int lg = bufferInfo.size;
            byteBuffer.position(bufferInfo.offset);
            byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
            byteBuffer.get(temp, 0, lg);
            bos.write(temp, 0, lg);
            bos.flush();
            bos.close();
            os.close();
            Log.i(TAG_Decoder, "yuv dump done");
        }
    }

    public void onWidthHeightChanged(int newWidth, int newHeight) {
    }

    public void onColorFormatChanged(int colorFormat) {
    }

    public void onSpsPpsChanged(byte[] sps, int spsLen, byte[] pps, int ppsLen, byte[] frame, int frameLen) {
        Log.i(TAG_Internal, "onSpsPpschange");
        MediaLogger.show(TAG_Internal, "onSpsPpschange");
    }

    private void stopService() {
        Log.i(TAG_Internal, "OnlineTranscoder service is being stopped");
        detachListenerToUpstream();
        Log.i(TAG_Internal, "OnlineTranscoder has detached from upstream");
        this.running = false;
        try {
            this.threadTranscoder.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG_Internal, "Transcoding thread has ended");
        h264FileClose();
        this.iframeMaker.deinit();
        this.originBuffer.deinit();
        this.replaceBuffer.deinit();
        Log.i(TAG_Internal, "OnlineTranscoder service has stopped");
    }

    public void run() {
        try {
            Log.i(TAG_Internal, "OnlineTranscoder service has started");
            attachListenerToUpstream();
            while (this.running) {
                Frame frame = getFrame();
                if (frame == null) {
                    Log.d(TAG_Internal, "cannot get frame");
                    Thread.sleep(50);
                } else {
                    if (this.toSkipHead) {
                        if (frame.type == Frame.FrameType.YUV || frame.isKeyFrame()) {
                            this.toSkipHead = false;
                            MediaLogger.show(TAG_Internal, this.numFrameJumped + " has been jumped");
                        } else {
                            this.numFrameJumped++;
                            if (DEBUG) {
                                Log.i(TAG_Internal, this.numFrameJumped + " has been jumped");
                            }
                            releaseFrame(frame);
                            Thread.sleep(10);
                        }
                    }
                    StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.OnlineTranscoderRun).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) frame.getSize()).onDataRecv(StreamDataObserver.ObservingContext.KeyframeNum, frame.isKeyFrame() ? 1 : 0).onDataRecv(StreamDataObserver.ObservingContext.Width, (long) frame.width).onDataRecv(StreamDataObserver.ObservingContext.Height, (long) frame.height);
                    if (frame.type == Frame.FrameType.YUV || frame.isKeyFrame()) {
                        this.lastIFrame = (long) DJIVideoUtil.getFrameIndex(frame.getIndex());
                    }
                    h264FileWrite(frame.getBuffer().lockAndReadData(), 0, frame.getSize());
                    frame.getBuffer().unLock();
                    if (DJIVideoUtil.isDebug(DEBUG)) {
                        Log.i(TAG_Internal, "written Frames=" + this.numFrameWritten);
                    }
                    this.info.offset = 0;
                    this.info.size = frame.getSize();
                    this.info.presentationTimeUs = DJIVideoUtil.getPresentationTimeUs(DJIVideoUtil.getFrameIndex(frame.getIndex()));
                    this.info.flags = 0;
                    if (frame.type == Frame.FrameType.YUV || frame.isKeyFrame()) {
                        this.info.flags |= 1;
                    }
                    boolean z = DEBUG;
                    String str = TAG_Output;
                    Locale locale = Locale.US;
                    Object[] objArr = new Object[6];
                    objArr[0] = (this.info.flags & 1) > 0 ? " I-frame" : "";
                    objArr[1] = Integer.valueOf(this.numFrameWritten);
                    objArr[2] = Long.valueOf(this.info.presentationTimeUs);
                    objArr[3] = Integer.valueOf(this.info.size);
                    objArr[4] = Integer.valueOf(this.preFrameWidth);
                    objArr[5] = Integer.valueOf(this.preFrameHeight);
                    MediaLogger.i(z, str, String.format(locale, "output: %s, Index=%d, pts=%d, size=%d, w=%d, h=%d", objArr));
                    ByteBuffer frameData = frame.getBuffer().lockAndReadData();
                    synchronized (this.listenerSync) {
                        Iterator<OnlineTranscoderListener> it2 = this.listenerList.iterator();
                        while (it2.hasNext()) {
                            OnlineTranscoderListener listener = it2.next();
                            try {
                                long beforeCallListener = System.currentTimeMillis();
                                listener.onFrameInput(frameData, this.info, this.numFrameJumped + this.numFrameWritten, this.preFrameWidth, this.preFrameHeight);
                                MediaLogger.i(DEBUG, TAG_Output, "call " + listener.getClass().getName() + " takes " + (System.currentTimeMillis() - beforeCallListener) + " ms");
                            } catch (Exception e) {
                                MediaLogger.e(TAG_Internal, e);
                            }
                        }
                    }
                    frame.getBuffer().unLock();
                    this.numFrameWritten++;
                    releaseFrame(frame);
                }
            }
        } catch (Exception e2) {
            Log.e(TAG_Internal, MediaLogger.eToStr(e2));
        }
        Log.i(TAG_Internal, "Online Transcoder Thread ends");
    }

    private Frame getFrame() {
        Frame re = _getFrame();
        if (re != null) {
            MediaLogger.i(DEBUG, TAG_Internal, String.format(Locale.US, "Return: Type=%s, index=%d, Origin Queue=%d, replace queue=%d", re.type.toString(), Long.valueOf(re.getIndex()), Integer.valueOf(this.originBuffer.getQueueSize()), Integer.valueOf(this.replaceBuffer.getQueueSize())));
        }
        return re;
    }

    private Frame _getFrame() {
        Frame origin = this.originBuffer.poll();
        if (origin == null) {
            return null;
        }
        if (!needMakeIFrame(origin.getIndex())) {
            return origin;
        }
        MediaLogger.i(DEBUG, TAG_Internal, "the frame need to be replaced");
        Frame replace = this.replaceBuffer.peek();
        if (replace != null) {
            while (true) {
                if (replace.getIndex() >= origin.getIndex()) {
                    break;
                }
                MediaLogger.show(DEBUG, TAG_Internal, "the replace element is out-of-date. replace=" + replace.getIndex() + " origin=" + origin.getIndex());
                this.replaceBuffer.poll();
                releaseFrame(replace);
                replace = this.replaceBuffer.peek();
                if (replace == null) {
                    MediaLogger.i(DEBUG, TAG_Internal, "after removing the out-of-date elements, replace queue is empty");
                    break;
                }
            }
        }
        if (replace == null) {
            MediaLogger.i(DEBUG, TAG_Internal, "there is no element in the replace queue. will wait 1 sec");
            synchronized (this.waitForReplace) {
                try {
                    this.waitForReplace.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            replace = this.replaceBuffer.peek();
            if (replace == null) {
                MediaLogger.i(DEBUG, TAG_Internal, "after waiting, still, there is no element in the replace queue");
                return origin;
            }
        }
        while (replace.getIndex() < origin.getIndex()) {
            MediaLogger.show(DEBUG, TAG_Internal, "the replace element is out-of-date. replace index=" + replace.getIndex() + " origin frameIndex=" + origin.getIndex());
            this.replaceBuffer.poll();
            releaseFrame(replace);
            replace = this.replaceBuffer.peek();
            if (replace == null) {
                MediaLogger.i(DEBUG, TAG_Internal, "after removing the out-of-date elements, there is no proper replace element");
                return origin;
            }
        }
        if (replace.getIndex() > origin.getIndex()) {
            MediaLogger.i(DEBUG, TAG_Internal, "the got replace element is for the future. The origin progress is far behind");
            return origin;
        }
        MediaLogger.i(DEBUG, TAG_Internal, "Now we have a frame that is proper for replace");
        this.replaceBuffer.poll();
        if (makeIFrame(replace)) {
            releaseFrame(origin);
            return replace;
        }
        releaseFrame(replace);
        return origin;
    }

    private boolean makeIFrame(Frame frame) {
        if (!needMakeIFrame(frame.getIndex())) {
            Log.i(TAG_Internal, "no need make I frame.");
            return true;
        }
        if (!(frame.getWidth() == this.preFrameWidth && frame.getHeight() == this.preFrameHeight)) {
            DJIVideoDecoder decoder = ServiceManager.getInstance().getDecoder();
            if (decoder == null || decoder.sps_header == null || decoder.sps_header.length == 0 || decoder.pps_header == null || decoder.pps_header.length == 0) {
                Log.i(TAG_Internal, "the Iframemaker can't be initialized. still return origin.");
                return false;
            }
            this.iframeMaker.deinit();
            Log.i(TAG_Internal, String.format(Locale.US, "width/height changed: [pre_Width=%d pre_height=%d] [new_width=%d new_height=%d]", Integer.valueOf(this.preFrameWidth), Integer.valueOf(this.preFrameHeight), Integer.valueOf(frame.getWidth()), Integer.valueOf(frame.getHeight())));
            this.preFrameWidth = frame.getWidth();
            this.preFrameHeight = frame.getHeight();
            byte[] sps_pps = new byte[(decoder.sps_header.length + decoder.pps_header.length)];
            System.arraycopy(decoder.sps_header, 0, sps_pps, 0, decoder.sps_header.length);
            System.arraycopy(decoder.pps_header, 0, sps_pps, decoder.sps_header.length, decoder.pps_header.length);
            MediaLogger.show(TAG_Internal, "init iFrameMaker: sps_pps=" + BytesUtil.byte2hex(sps_pps));
            this.iframeMaker.init(sps_pps, 0, this.preFrameWidth, this.preFrameHeight);
            Log.i(TAG_Internal, "Video width and height changed. re-init ");
        }
        Log.i(TAG_Internal, String.format(Locale.US, "before making I frame. origin queue=%d, replace queue=%d", Integer.valueOf(this.originBuffer.getQueueSize()), Integer.valueOf(this.replaceBuffer.getQueueSize())));
        this.iframeMaker.convertFromYUVtoIDR(frame);
        return true;
    }

    private void releaseFrame(Frame frame) {
        switch (frame.type) {
            case H264:
                this.originBuffer.release(frame);
                return;
            case YUV:
                this.replaceBuffer.release(frame);
                return;
            default:
                return;
        }
    }
}
