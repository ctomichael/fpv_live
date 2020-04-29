package dji.midware.usb.P3;

import android.os.SystemClock;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import com.dji.video.framing.DJIVideoDecoderInterface;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DJIVideoPackManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.parser.plugins.DJIPluginLBChanneParser;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.util.BytesUtil;
import dji.midware.util.save.StreamDataObserver;
import dji.midware.util.save.StreamDelaySaver;
import dji.midware.util.save.StreamSaver;
import dji.midware.util.save.VideoFrameObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class VideoDataTransferor {
    private static final int DEFAULT_FRAME_QUEUE_SIZE = 1200;
    private static final boolean IS_BUFFER_VIDEODATA = true;
    private static final boolean IS_SAVE_DOWN_DATA = false;
    private int aoa_log_update = 0;
    private boolean isDataMode;
    private long last_frame_num_packet = 0;
    private long last_frame_size = 0;
    private long last_frame_start_time = -1;
    private File mDownFile = new File("/sdcard/aoa_dowon.bin");
    private FileOutputStream mDownFileOutputStream;
    private VideoRawBufferReceiver mFpvCameraReceiver;
    private VideoRawBufferReceiver mMainCameraReceiver;
    private DJIPluginLBChanneParser mPluginLBChannelParser = new DJIPluginLBChanneParser(new LbChannelHandler(this));
    private ServiceManager mServiceManager = ServiceManager.getInstance();
    private DJIVideoDataRecver mVideoDataRecver = DJIVideoDataRecver.getInstance();
    private StreamDataObserver newMethodObserver;
    private StreamDataObserver oldMethodObserver;

    public VideoDataTransferor(VideoRawBufferReceiver cameraParser, VideoRawBufferReceiver fpvParser) {
        this.mMainCameraReceiver = cameraParser;
        this.mFpvCameraReceiver = fpvParser;
    }

    public void setDataMode(boolean dataMode) {
        this.isDataMode = dataMode;
    }

    public void handleOldMethod(byte[] buffer, int offset, int count, UsbAccessoryService.VideoStreamSource source) {
        if (this.oldMethodObserver == null) {
            this.oldMethodObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaHandleOldMethod);
        }
        this.oldMethodObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) count);
        if (this.isDataMode && !isAllwaysLiveViewWhenDownload()) {
            DJIVideoPackManager.getInstance().parseData(buffer, offset, count);
        } else if (source == UsbAccessoryService.VideoStreamSource.Camera) {
            DJIVideoDecoderInterface decoderInterface = DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface();
            if (ServiceManager.getInstance().getDecoder() != null || ServiceManager.getInstance().isNeedRawData() || (decoderInterface != null && decoderInterface.getDJIVideoDecoder() != null)) {
                putVideoBuffer(buffer, offset, count, source);
            }
        } else if (ServiceManager.getInstance().getSecondaryVideoDecoder() != null || ServiceManager.getInstance().isNeedFpvRawData()) {
            putVideoBuffer(buffer, offset, count, source);
        }
    }

    public void handleNewMethod(byte[] buffer, int offset, int count) {
        if (this.mPluginLBChannelParser != null) {
            if (this.newMethodObserver == null) {
                this.newMethodObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaHandleNewMethod);
            }
            this.newMethodObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) count);
            this.mPluginLBChannelParser.parse(buffer, offset, count);
        }
    }

    public void putVideoBuffer(byte[] buffer, int offset, int length, UsbAccessoryService.VideoStreamSource source) {
        StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaPutVideoBuffer, source).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) length);
        if (source == UsbAccessoryService.VideoStreamSource.Camera) {
            VideoFrameObserver.getInstance().saveTimeStamp(VideoFrameObserver.TimeSavingPoint.UsbGetBody, buffer, offset, false);
            this.mMainCameraReceiver.offerData(buffer, offset, length);
            return;
        }
        this.mFpvCameraReceiver.offerData(buffer, offset, length);
    }

    private boolean isAllwaysLiveViewWhenDownload() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.KumquatX || type == ProductType.KumquatS || type == ProductType.PomatoSDR || type == ProductType.Mammoth || type == ProductType.WM230 || type == ProductType.WM240 || type == ProductType.WM245 || type == ProductType.WM160;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.midware.media.DJIVideoDataRecver.onVideoRecv(byte[], int, boolean, int):void
     arg types: [byte[], int, int, int]
     candidates:
      dji.midware.media.DJIVideoDataRecver.onVideoRecv(byte[], int, int, boolean):void
      dji.midware.media.DJIVideoDataRecver.onVideoRecv(byte[], int, boolean, int):void */
    public void toTransVideoData(byte[] buffer, int length, UsbAccessoryService.VideoStreamSource source) {
        DJIVideoDecoderInterface decoderInterface = DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface();
        if (decoderInterface == null) {
            return;
        }
        if (this.mServiceManager.isNeedPacked()) {
            StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaTransVideoDataNeedPack, source).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) length);
            if (source == UsbAccessoryService.VideoStreamSource.Camera) {
                if (this.mServiceManager.isNeedRawData()) {
                    this.mVideoDataRecver.onVideoRecv(buffer, length, true, source.getIndex());
                } else {
                    decoderInterface.getParseController().feedData(buffer, source.getIndex(), length);
                }
            } else if (this.mServiceManager.isNeedFpvRawData()) {
                this.mVideoDataRecver.onVideoRecv(buffer, length, true, source.getIndex());
            } else {
                decoderInterface.getParseController().feedData(buffer, source.getIndex(), length);
            }
            if (StreamDelaySaver.IS_SAVE_PACKET_DELAY) {
                try {
                    if (this.last_frame_start_time == -1) {
                        this.last_frame_start_time = SystemClock.uptimeMillis();
                    }
                    String text = String.format(Locale.US, "[After Sending To FFMpeg] word 0: %X word 1: %X word 2: %X size=%d time=%d \n", Integer.valueOf(BytesUtil.getInt(buffer, 0)), Integer.valueOf(BytesUtil.getInt(buffer, 4)), Integer.valueOf(BytesUtil.getInt(buffer, 8)), Integer.valueOf(length), Long.valueOf(System.currentTimeMillis()));
                    this.last_frame_size += (long) length;
                    this.last_frame_num_packet++;
                    if (length != 2048) {
                        long frameReceiveDelay = SystemClock.uptimeMillis() - this.last_frame_start_time;
                        text = text + String.format(Locale.US, "frameReceiveDelay=%d frame_size=%d num_packet=%d", Long.valueOf(frameReceiveDelay), Long.valueOf(this.last_frame_size), Long.valueOf(this.last_frame_num_packet));
                        this.last_frame_start_time = -1;
                        this.last_frame_size = 0;
                        this.last_frame_num_packet = 0;
                    }
                    StreamDelaySaver.getInstance().packetDelayFile.append((CharSequence) (text + "\n"));
                    this.aoa_log_update = (this.aoa_log_update + 1) % 100;
                    if (this.aoa_log_update == 0) {
                        StreamDelaySaver.getInstance().packetDelayFile.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (StreamSaver.SAVE_videoUsb_Open) {
                StreamSaver.getInstance("dji_video_usbaccessary_" + source.name()).write(buffer, 0, length);
                return;
            }
            return;
        }
        StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaTransVideoDataNoNeedPack, source).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) length);
        decoderInterface.getParseController().feedData(buffer, source.getIndex(), length);
    }
}
