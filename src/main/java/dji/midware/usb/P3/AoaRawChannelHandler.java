package dji.midware.usb.P3;

import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.DJIPayloadUsbDataManager;
import dji.midware.data.model.common.ByteObject;
import dji.midware.parser.plugins.DJIPluginRingBufferParser;
import dji.midware.sdr.log.DJIAoaChannelLogDispatcher;
import dji.midware.usb.P3.LB2VideoController;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.util.BytesUtil;
import dji.midware.util.save.StreamDataObserver;
import dji.midware.util.save.StreamSaver;

public class AoaRawChannelHandler implements DJIPluginRingBufferParser.DJIRingBufferParserListener {
    private static final int CHANNEL_CMD = 22345;
    private static final int CHANNEL_LTE_VIDEO = 22352;
    private static final int CHANNEL_PM420_PSDK_DATA = 22361;
    private static final int CHANNEL_PM420_PSDK_VIDEO = 22360;
    private static final int CHANNEL_SDR_LOG1 = 22348;
    private static final int CHANNEL_SDR_LOG2 = 22349;
    private static final int CHANNEL_SDR_THIRD = 22350;
    private static final int CHANNEL_VIDEO = 22346;
    private static final int CHANNEL_VIDEO_FPV = 22347;
    public static boolean IS_OPEN_VIDEO_PARSE_INNER = true;
    private static final int MAX_CHANNEL = 22361;
    private int dataLen = 0;
    private short dataType = 0;
    private boolean isPauseRecvThread;
    private boolean isStartStream;
    private DJIAoaChannelLogDispatcher mAoaChannelLogDispatcher = new DJIAoaChannelLogDispatcher();
    private StreamDataObserver mBodyDefaultObserver;
    private StreamDataObserver mBodyDualObserver;
    private StreamDataObserver mBodyObserver;
    private StreamDataObserver mBodySingleObserver;
    private StreamDataObserver mBodyVideoObserver;
    private StreamDataObserver mBodyVideoObserver2;
    private StreamDataObserver mBodyVideoObserver3;
    private DJIPackManager mCmdDataReceiver = DJIPackManager.getInstance();
    private VideoDataTransferor mVideoDataReceiver;

    public AoaRawChannelHandler(VideoDataTransferor videoDataTransferor) {
        this.mVideoDataReceiver = videoDataTransferor;
    }

    public DJIPackManager getCmdDataReceiver() {
        return this.mCmdDataReceiver;
    }

    public void setStartStream(boolean startStream) {
        this.isStartStream = startStream;
    }

    public void setPauseRecvThread(boolean pauseRecvThread) {
        this.isPauseRecvThread = pauseRecvThread;
    }

    private boolean isValidChannel(int dataType2) {
        return dataType2 >= CHANNEL_CMD && dataType2 <= 22361;
    }

    public int parseSecondHeader(byte[] buffer, int offset, int count) {
        this.dataType = BytesUtil.getShort(buffer, offset, 2);
        this.dataLen = BytesUtil.getInt(buffer, offset + 2, 4);
        if (StreamSaver.SAVE_usbHybridDataType_Open) {
            byte[] bytes = (System.currentTimeMillis() + " type=" + ((int) this.dataType) + " len=" + this.dataLen + "\n").getBytes();
            StreamSaver.getInstance("dji_usbHybridDataType").write(bytes, 0, bytes.length);
        }
        if (!isValidChannel(this.dataType)) {
            return -1;
        }
        return this.dataLen;
    }

    public void onGetBody(byte[] buffer, int offset, int count) {
        if (this.mBodyObserver == null) {
            this.mBodyObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaGetBody);
        }
        this.mBodyObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) count);
        if (this.dataType == 22346) {
            if (IS_OPEN_VIDEO_PARSE_INNER) {
                if (StreamSaver.SAVE_WM230VideoDebug_Open) {
                    StreamSaver.getInstance(StreamSaver.Save_wm230VideoDebug_Name).write(buffer, 0, buffer.length);
                }
                if (this.mBodyVideoObserver == null) {
                    this.mBodyVideoObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaGetStream);
                }
                this.mBodyVideoObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) count);
                if (this.isStartStream && !this.isPauseRecvThread) {
                    if (!SdrLteVideoController.getInstance().isOpenLteVideoAndReady()) {
                        switch (LB2VideoController.getInstance().getEncodeMode()) {
                            case DEFAULT:
                                if (this.mBodyDefaultObserver == null) {
                                    this.mBodyDefaultObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaGetStreamDefault);
                                }
                                this.mBodyDefaultObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) count);
                                this.mVideoDataReceiver.handleOldMethod(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Camera);
                                return;
                            case SINGLE:
                                if (this.mBodySingleObserver == null) {
                                    this.mBodySingleObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaGetStreamSingle);
                                }
                                this.mBodySingleObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) count);
                                LB2VideoController.SingleType singleType = LB2VideoController.getInstance().getCurSingleType();
                                if (LB2VideoController.SingleType.LB == singleType || LB2VideoController.SingleType.MIX == singleType) {
                                    this.mVideoDataReceiver.handleOldMethod(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Camera);
                                    return;
                                }
                                return;
                            case DUAL:
                                if (this.mBodyDualObserver == null) {
                                    this.mBodyDualObserver = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaGetStreamDual);
                                }
                                this.mBodyDualObserver.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) count);
                                this.mVideoDataReceiver.handleNewMethod(buffer, offset, count);
                                return;
                            default:
                                return;
                        }
                    } else if (SdrLteVideoController.getInstance().isMainVideoOpenLte() && SdrLteVideoController.getInstance().isLteDebug()) {
                        this.mVideoDataReceiver.handleOldMethod(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Fpv);
                    }
                }
            }
        } else if (this.dataType == CHANNEL_VIDEO_FPV || this.dataType == 22350) {
            if (this.mBodyVideoObserver2 == null) {
                this.mBodyVideoObserver2 = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaGetStream2);
            }
            this.mBodyVideoObserver2.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) count);
            if (this.isStartStream && !this.isPauseRecvThread) {
                if (SdrLteVideoController.getInstance().isOpenLteVideoAndReady()) {
                    if (SdrLteVideoController.getInstance().isSecondaryVideoOpenLte() && SdrLteVideoController.getInstance().isLteDebug()) {
                        this.mVideoDataReceiver.handleOldMethod(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Camera);
                    }
                } else if (LB2VideoController.getInstance().getEncodeMode() == LB2VideoController.EncodeMode.DEFAULT) {
                    this.mVideoDataReceiver.handleOldMethod(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Fpv);
                } else {
                    LB2VideoController.SingleType singleType2 = LB2VideoController.getInstance().getCurSingleType();
                    if (LB2VideoController.SingleType.MIX == singleType2 || LB2VideoController.SingleType.EXT == singleType2) {
                        this.mVideoDataReceiver.handleOldMethod(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Fpv);
                    }
                }
            }
            if (this.dataType == CHANNEL_VIDEO_FPV) {
                this.mAoaChannelLogDispatcher.onPortLogRawDataReceive(1, buffer, offset, count);
            }
        } else if (this.dataType == 22352) {
            if (this.mBodyVideoObserver3 == null) {
                this.mBodyVideoObserver3 = StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AoaGetStream3);
            }
            this.mBodyVideoObserver3.onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) count);
            if (SdrLteVideoController.getInstance().isMainVideoOpenLte()) {
                this.mVideoDataReceiver.handleOldMethod(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Camera);
            } else if (SdrLteVideoController.getInstance().isSecondaryVideoOpenLte()) {
                this.mVideoDataReceiver.handleOldMethod(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Fpv);
            }
            SdrLteVideoController.getInstance().setLteVideoReady(true);
        } else if (this.dataType == CHANNEL_CMD) {
            this.mCmdDataReceiver.parse(buffer, offset, count);
        } else if (this.dataType == CHANNEL_SDR_LOG1) {
            this.mAoaChannelLogDispatcher.onPortLogRawDataReceive(2, buffer, offset, count);
        } else if (this.dataType == CHANNEL_SDR_LOG2) {
            this.mAoaChannelLogDispatcher.onPortLogRawDataReceive(3, buffer, offset, count);
        } else if (this.dataType == 22361) {
            ByteObject byteObject = ByteObject.obtain(count, LbChannelHandler.class.getSimpleName());
            System.arraycopy(buffer, offset, byteObject.getBytes(), 0, count);
            DJIPayloadUsbDataManager.getInstance().feedData(byteObject);
        } else if (this.dataType == CHANNEL_PM420_PSDK_VIDEO) {
            this.mVideoDataReceiver.handleOldMethod(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Camera);
        }
    }
}
