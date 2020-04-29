package dji.midware.usb.P3;

import dji.midware.data.manager.P3.DJIArPackManager;
import dji.midware.data.manager.P3.DJIFlightLogPackManager;
import dji.midware.data.manager.P3.DJIPayloadUsbDataManager;
import dji.midware.data.manager.P3.DJIVideoPackManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.common.ByteObject;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.parser.plugins.DJIPluginLBChanneParser;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.util.DoubleCameraSupportUtil;

public class LbChannelHandler implements DJIPluginLBChanneParser.DJILBChannelListener {
    private ServiceManager mLinkServiceManager = ServiceManager.getInstance();
    private DJIVideoDataRecver mVideoDataRecver = DJIVideoDataRecver.getInstance();
    private VideoDataTransferor mVideoDataTransferor;
    private DJIVideoPackManager mVideoPackManager = DJIVideoPackManager.getInstance();

    public LbChannelHandler(VideoDataTransferor videoDataTransferor) {
        this.mVideoDataTransferor = videoDataTransferor;
    }

    public void onRecv(int channelID, byte[] buffer, int offset, int count) {
        boolean hasSecondaryVideoDecoder;
        boolean isGD600PlaybackMode;
        boolean isGD600PlaybackMode2;
        boolean isGD600PlaybackMode3;
        if (this.mLinkServiceManager.getSecondaryVideoDecoder() != null) {
            hasSecondaryVideoDecoder = true;
        } else {
            hasSecondaryVideoDecoder = false;
        }
        if (!DoubleCameraSupportUtil.SupportDoubleCamera) {
            if (channelID == DJIPluginLBChanneParser.DJILBChannelID.LiveView.value() || channelID == DJIPluginLBChanneParser.DJILBChannelID.FourthLiveViewXT.value() || channelID == DJIPluginLBChanneParser.DJILBChannelID.ThirdLiveViewZ30.value()) {
                if ((this.mLinkServiceManager.getDecoder() == null && !this.mVideoDataRecver.isNeedRawData() && !this.mVideoDataRecver.isNeedFrameData()) || this.mLinkServiceManager.getIsFix()) {
                    return;
                }
                if (channelID == DJIPluginLBChanneParser.DJILBChannelID.ThirdLiveViewZ30.value()) {
                    if (DataCameraGetPushStateInfo.getInstance().getMode() == DataCameraGetMode.MODE.PLAYBACK) {
                        isGD600PlaybackMode3 = true;
                    } else {
                        isGD600PlaybackMode3 = false;
                    }
                    if (!isGD600PlaybackMode3) {
                        this.mVideoDataTransferor.putVideoBuffer(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Camera);
                        return;
                    }
                    return;
                }
                this.mVideoDataTransferor.putVideoBuffer(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Camera);
            } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.FileDownload.value()) {
                this.mVideoPackManager.parseData(buffer, offset, count);
            } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.SecondaryLiveView.value()) {
                if (this.mVideoDataRecver.isFpvNeedRawData() || hasSecondaryVideoDecoder) {
                    this.mVideoDataTransferor.putVideoBuffer(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Fpv);
                }
            } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.ARPush.value()) {
                DJIArPackManager.getInstance().parse(buffer, offset, count);
            } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.FlightLog.value()) {
                DJIFlightLogPackManager.getInstance().parse(buffer, offset, count);
            } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.PayloadLiveView.value()) {
                if ((this.mLinkServiceManager.getDecoder() != null || this.mVideoDataRecver.isNeedRawData() || this.mVideoDataRecver.isNeedFrameData()) && !this.mLinkServiceManager.getIsFix()) {
                    this.mVideoDataTransferor.putVideoBuffer(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Camera);
                }
            } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.PayloadCMD.value()) {
                ByteObject byteObject = ByteObject.obtain(count, LbChannelHandler.class.getSimpleName());
                System.arraycopy(buffer, offset, byteObject.getBytes(), 0, count);
                DJIPayloadUsbDataManager.getInstance().feedData(byteObject);
            }
        } else if (DoubleCameraSupportUtil.getMainCameraChannelID() == channelID) {
            if ((this.mLinkServiceManager.getDecoder() == null && !this.mVideoDataRecver.isNeedRawData() && !this.mVideoDataRecver.isNeedFrameData()) || this.mLinkServiceManager.getIsFix()) {
                return;
            }
            if (channelID == DJIPluginLBChanneParser.DJILBChannelID.ThirdLiveViewZ30.value()) {
                if (DataCameraGetPushStateInfo.getInstance().getMode() == DataCameraGetMode.MODE.PLAYBACK) {
                    isGD600PlaybackMode2 = true;
                } else {
                    isGD600PlaybackMode2 = false;
                }
                if (!isGD600PlaybackMode2) {
                    this.mVideoDataTransferor.putVideoBuffer(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Camera);
                    return;
                }
                return;
            }
            this.mVideoDataTransferor.putVideoBuffer(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Camera);
        } else if (DoubleCameraSupportUtil.getSecondaryCameraId() == channelID) {
            if (!this.mVideoDataRecver.isFpvNeedRawData() && !hasSecondaryVideoDecoder) {
                return;
            }
            if (channelID == DJIPluginLBChanneParser.DJILBChannelID.ThirdLiveViewZ30.value()) {
                if (DataCameraGetPushStateInfo.getInstance().getMode() == DataCameraGetMode.MODE.PLAYBACK) {
                    isGD600PlaybackMode = true;
                } else {
                    isGD600PlaybackMode = false;
                }
                if (!isGD600PlaybackMode) {
                    this.mVideoDataTransferor.putVideoBuffer(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Fpv);
                    return;
                }
                return;
            }
            this.mVideoDataTransferor.putVideoBuffer(buffer, offset, count, UsbAccessoryService.VideoStreamSource.Fpv);
        } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.FileDownload.value()) {
            this.mVideoPackManager.parseData(buffer, offset, count);
        } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.ARPush.value()) {
            DJIArPackManager.getInstance().parse(buffer, offset, count);
        } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.FlightLog.value()) {
            DJIFlightLogPackManager.getInstance().parse(buffer, offset, count);
        } else if (channelID == DJIPluginLBChanneParser.DJILBChannelID.PayloadCMD.value()) {
            ByteObject byteObject2 = ByteObject.obtain(count, LbChannelHandler.class.getSimpleName());
            System.arraycopy(buffer, offset, byteObject2.getBytes(), 0, count);
            DJIPayloadUsbDataManager.getInstance().feedData(byteObject2);
        }
    }
}
