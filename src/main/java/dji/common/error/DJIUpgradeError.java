package dji.common.error;

import android.support.v4.app.NotificationManagerCompat;
import com.billy.cc.core.component.CCResult;

public class DJIUpgradeError extends DJIError {
    public static final DJIUpgradeError AIRCRAFT_AND_RC_LOW_ELECTRICITY = new DJIUpgradeError("当前飞机和遥控器的电池电量很低!", -12002);
    public static final DJIUpgradeError AIRCRAFT_LOW_ELECTRICITY = new DJIUpgradeError("当前飞机电池电量过低!", -12001);
    public static final DJIUpgradeError AIRCRAFT_NEED_DIRECT_CONNECTED = new DJIUpgradeError("要直连飞机才允许升级!", -11000);
    public static final DJIUpgradeError AIR_CONTROL_LINK_ERROR = new DJIUpgradeError("天空端控制链路不通", -4012);
    public static final DJIUpgradeError AIR_DATA_LINK_ERROR = new DJIUpgradeError("天空端数据链路不通", -4008);
    public static final DJIUpgradeError AIR_FIRMWARE_PACK_ERROR = new DJIUpgradeError("天空端固件打包错误", -4013);
    public static final DJIUpgradeError AIR_SDR_ERROR = new DJIUpgradeError("天空端SDR故障", -4009);
    public static final DJIUpgradeError BATTERY_STATE_INVALID = new DJIUpgradeError("获取到的设备电量值为0, 不合法!", -12003);
    public static final DJIUpgradeError BootNotReady = new DJIUpgradeError("CAM APP收到0x7命令，检测当前UVA系统还没有boot 好，不能支持升级给的错误码", -5024);
    public static final DJIUpgradeError CANNOT_FETCH_DEVICE_VERSION = new DJIUpgradeError("拉取不到设备的版本号。", -1002);
    public static final DJIUpgradeError CANNOT_FETCH_SERVER_VERSION = new DJIUpgradeError("拉取不到服务器版本信息", -8);
    public static final DJIUpgradeError CFG_FILE_ERROR = new DJIUpgradeError("CFG文件错误。", -1003);
    public static final DJIUpgradeError CFG_PARSE_FAILED = new DJIUpgradeError("固件的CFG文件解析失败", -3);
    public static final DJIUpgradeError CHECK_FAILED = new DJIUpgradeError("传输过程中，文件传输完成后进行检查失败!", -3012);
    public static final DJIUpgradeError COMPONENT_NOT_FOUND = new DJIUpgradeError("升级的模块没有找到", -1);
    public static final DJIUpgradeError CONSISTENT_UPGRADE_STATE_ERROR = new DJIUpgradeError("不能执行一致性升级，条件不满足, 当前不需要执行一致性升级或者飞机已经打桨!", -11002);
    public static final DJIUpgradeError CfgFileCreateFailed = new DJIUpgradeError("创建CFG文件失败，反馈售后，应该是B：文件系统异常", -5027);
    public static final DJIUpgradeError CfgFileWriteFailed = new DJIUpgradeError("存储大包CFG文件到B:失败，反馈售后，应该是B：文件系统异常", -5026);
    public static final DJIUpgradeError CheckConnectionError = new DJIUpgradeError("检查连接错误", -5022);
    public static final DJIUpgradeError CheckSecDebugFailed = new DJIUpgradeError("安全调试已关闭，不支持小包升级", -5023);
    public static final DJIUpgradeError CriticalError = new DJIUpgradeError("严重错误", -5011);
    public static final DJIUpgradeError CurrentModuleAccessFileSystemFailed = new DJIUpgradeError("当前模块访问文件系统失败", -6011);
    public static final DJIUpgradeError CurrentModuleDisconnect = new DJIUpgradeError("升级当前模块断开连接", -6001);
    public static final DJIUpgradeError CurrentModuleEnterLoaderFailed = new DJIUpgradeError("当前模块进入loader失败", -6003);
    public static final DJIUpgradeError CurrentModuleExtraFileFailed = new DJIUpgradeError("当前模块加载到内存失败", -6013);
    public static final DJIUpgradeError CurrentModuleFirmwareError = new DJIUpgradeError("更新当前模块固件错误", -6002);
    public static final DJIUpgradeError CurrentModuleHardwareError = new DJIUpgradeError("当前模块硬件错误", -6008);
    public static final DJIUpgradeError CurrentModuleLoadFirmwareToMemoryFailed = new DJIUpgradeError("当前模块加载到内存失败", -6010);
    public static final DJIUpgradeError CurrentModuleWriteFileFailed = new DJIUpgradeError("当前模块写文件失败", -6012);
    public static final DJIUpgradeError CurrentModuleWriteFirmwareFailed = new DJIUpgradeError("当前模块刷入硬件错误", -6009);
    public static final DJIUpgradeError DOWNLOAD_FAILED = new DJIUpgradeError("固件下载失败", -2003);
    public static final DJIUpgradeError ENTER_LOADER_FAILED = new DJIUpgradeError("开始升级失败，进入Loader失败，请重启飞机和遥控器！", -3002);
    public static final DJIUpgradeError ENTER_LOADER_FAILED_LINK_REVERSE_FAILED = new DJIUpgradeError("进入Loader失败, 链路翻转失败!", -3014);
    public static final DJIUpgradeError ENTER_LOADER_FAILED_LOW_BATTERY = new DJIUpgradeError("进入Loader失败，电池电量低！", -3013);
    public static final DJIUpgradeError EnterLoaderTimeout = new DJIUpgradeError("开始升级失败，进入Loader超时，当前链路不稳定!", -3010);
    public static final DJIUpgradeError EraseFlashFailed = new DJIUpgradeError("擦除flash失败", -5017);
    public static final DJIUpgradeError ExceedFlash = new DJIUpgradeError("固件超出flash的内存范围", -5015);
    public static final DJIUpgradeError FETCH_DOWNLOAD_INFO_FAILED = new DJIUpgradeError("拉取固件版本信息失败", -6);
    public static final DJIUpgradeError FIRMWARE_DATA_CHECK_FAILED = new DJIUpgradeError("升级检查当前文件数据都正常失败！", -3001);
    public static final DJIUpgradeError FIRMWARE_DOWNLOADED = new DJIUpgradeError("固件已经下载过，不需要重复下载!", -5);
    public static final DJIUpgradeError FIRMWARE_DOWNLOAD_CANCELED = new DJIUpgradeError("固件下载流程被取消", -10);
    public static final DJIUpgradeError FIRMWARE_ERROR = new DJIUpgradeError("固件错误", -4001);
    public static final DJIUpgradeError FIRMWARE_NOT_DOWNLOADED = new DJIUpgradeError("固件没有下载，无法进行升级!", -3009);
    public static final DJIUpgradeError FIRMWARE_NOT_MATCH = new DJIUpgradeError("固件不匹配", -4006);
    public static final DJIUpgradeError FIRMWARE_PACK_EXTRACT_ERROR = new DJIUpgradeError("大包解压缩出错", -4019);
    public static final DJIUpgradeError FIRMWARE_TRANSFER_FAILED = new DJIUpgradeError("固件传输失败！", -3005);
    public static final DJIUpgradeError FIRMWARE_TRANSFER_TIME_OUT = new DJIUpgradeError("传输过程中发送数据包超时，链路丢包严重！！", -3007);
    public static final DJIUpgradeError FetchFileSizeInfoFailed = new DJIUpgradeError("拉取固件文件大小失败！", -11);
    public static final DJIUpgradeError FirmwareCheckFailed = new DJIUpgradeError("固件检查失败", -5016);
    public static final DJIUpgradeError FirmwareTypeError = new DJIUpgradeError("升级使用协议类型无效", -5020);
    public static final DJIUpgradeError FlashProgramError = new DJIUpgradeError("固件烧写失败", -5018);
    public static final DJIUpgradeError GET_VERSION_ERROR = new DJIUpgradeError("获取版本号失败。", -1004);
    public static final DJIUpgradeError GET_VERSION_PACK_SIZE_ERROR = new DJIUpgradeError("拉取固件的版本信息返回的数据包大小错误", -1005);
    public static final DJIUpgradeError GLASS_FIMRWARE_PACK_ERROR = new DJIUpgradeError("眼镜端固件错误", -4015);
    public static final DJIUpgradeError GROUND_DISCON_GLASS_FIR_TIME = new DJIUpgradeError("地面端和眼镜端天地反转后失联", -4016);
    public static final DJIUpgradeError GROUND_DISCON_GLASS_SEC_TIME = new DJIUpgradeError("地面端和眼镜端天地反转2次后失联", -4017);
    public static final DJIUpgradeError GROUND_SDR_ERROR = new DJIUpgradeError("地面端SDR故障", -4010);
    public static final DJIUpgradeError GROUND_TO_GLASS_ERROR = new DJIUpgradeError("地面端向眼镜端传输固件失败", -4018);
    public static final DJIUpgradeError GetParamFailed = new DJIUpgradeError("获取参数失败", -5007);
    public static final DJIUpgradeError HardwareError = new DJIUpgradeError("硬件错误", -5021);
    public static final DJIUpgradeError INVALIDATE_ROLL_BACK = new DJIUpgradeError("非法降级", -4007);
    public static final DJIUpgradeError INVALID_DOWNLOAD_INFO = new DJIUpgradeError("开始下载错误。", -2000);
    public static final DJIUpgradeError INVALID_PACK_RET_CODE = new DJIUpgradeError("拉取固件cfg文件信息失败，收到一些未定义的ret code。", -1001);
    public static final DJIUpgradeError INVALID_TOKEN = new DJIUpgradeError("用户登陆token不合法", -9);
    public static final DJIUpgradeError INVALID_TRANSFER_STATE = new DJIUpgradeError("传输失败，当前升级模块内部状态机异常！", -3000);
    public static final DJIUpgradeError InvalidParam = new DJIUpgradeError("非法参数", -5003);
    public static final DJIUpgradeError InvalidState = new DJIUpgradeError("状态无效", -5004);
    public static final DJIUpgradeError LAST_DOWNLOAD_NOT_END = new DJIUpgradeError("当前正在下载！", -2001);
    public static final DJIUpgradeError MOTOR_RUNNING = new DJIUpgradeError("电机起转", -4005);
    public static final DJIUpgradeError NOT_FIND_CONFIG_FILE = new DJIUpgradeError("找不到升级配置文件", -4014);
    public static final DJIUpgradeError NO_CACHE_DEVICE_INFO_ERROR = new DJIUpgradeError("APP未连接过设备，无法获取缓存设备的状态!", -13000);
    public static final DJIUpgradeError NO_NETWORK = new DJIUpgradeError("当前没有连接网络", -2002);
    public static final DJIUpgradeError NoSDCard = new DJIUpgradeError("未插入SD Card", -5008);
    public static final DJIUpgradeError NotSupportSDCardUpgrade = new DJIUpgradeError("CAM_APP收到0X8，检查发现不支持SD卡升级的错误码", -5025);
    public static final DJIUpgradeError OperationInvalid = new DJIUpgradeError("命令无效", -5000);
    public static final DJIUpgradeError OperationTimeout = new DJIUpgradeError("命令执行超时", -5001);
    public static final DJIUpgradeError OverMemory = new DJIUpgradeError("超出内存", -5002);
    public static final DJIUpgradeError PROCESSER_START_FAILED = new DJIUpgradeError("开始升级流程失败，固件文件路径未找到，没有配置好文件路径", -2);
    public static final DJIUpgradeError PRODUCT_DISCONNECTED_WHEN_TRANSFERRING = new DJIUpgradeError("固件传输取消，设备已经断连", -3008);
    public static final DJIUpgradeError PackIndexInvalid = new DJIUpgradeError("包乱序", -5014);
    public static final DJIUpgradeError ParamLengthInvalid = new DJIUpgradeError("参数长度错误", -5013);
    public static final DJIUpgradeError RCEnterLoaderInvalidParam = new DJIUpgradeError("进入loader参数错误", -7000);
    public static final DJIUpgradeError RCEnterLoaderInvalidState = new DJIUpgradeError("进入loader遥控器状态错误", -7001);
    public static final DJIUpgradeError RCEnterLoaderLowBattery = new DJIUpgradeError("遥控器电池电量低", -7003);
    public static final DJIUpgradeError RCEnterLoaderMotorIsOn = new DJIUpgradeError("进入loader已经起浆", -7002);
    public static final DJIUpgradeError RCFileParsingFailed = new DJIUpgradeError("文件解析失败", -8000);
    public static final DJIUpgradeError RCFileVerifyFailed = new DJIUpgradeError("文件校验失败", -8003);
    public static final DJIUpgradeError RCFileVerifyInvalidParam = new DJIUpgradeError("文件检验参数错误", -7012);
    public static final DJIUpgradeError RCFileVerifyInvalidState = new DJIUpgradeError("文件检验状态错误", -7013);
    public static final DJIUpgradeError RCFlashEraseFailed = new DJIUpgradeError("Flash擦除失败", -8001);
    public static final DJIUpgradeError RCFlashWriteFailed = new DJIUpgradeError("Flash写失败", -8002);
    public static final DJIUpgradeError RCMCUCommunication = new DJIUpgradeError("MCU无法通信", -8004);
    public static final DJIUpgradeError RCMCUEnterLoaderFailed = new DJIUpgradeError("MCU进入loader失败", -8005);
    public static final DJIUpgradeError RCMCURecvFileFailed = new DJIUpgradeError(" MCU接收文件失败", -8007);
    public static final DJIUpgradeError RCMCUStartReceiveFileFailed = new DJIUpgradeError("MCU开始接收文件失败", -8006);
    public static final DJIUpgradeError RCMCUWriteFirmwareFailed = new DJIUpgradeError("MCU写固件失败", -8008);
    public static final DJIUpgradeError RCMD5Invalid = new DJIUpgradeError("文件检验MD5错误", -7014);
    public static final DJIUpgradeError RCRequestSendFileInvalidParam = new DJIUpgradeError("请求传输文件参数错误", -7004);
    public static final DJIUpgradeError RCRequestSendFileInvalidState = new DJIUpgradeError("请求传输文件错误状态错误", -7005);
    public static final DJIUpgradeError RCRequestSendFileOpenFileFailed = new DJIUpgradeError("请求传输文件打开文件错误", -7006);
    public static final DJIUpgradeError RCTransformFileInvalidParam = new DJIUpgradeError("传输文件参数错误", -7007);
    public static final DJIUpgradeError RCTransformFileInvalidState = new DJIUpgradeError("传输文件状态错误", -7008);
    public static final DJIUpgradeError RCTransformIndexInvalid = new DJIUpgradeError("错误的文件pack序号", -7010);
    public static final DJIUpgradeError RCTransformPackIndexOverRange = new DJIUpgradeError("文件pack序号超过最大的包序号", -7011);
    public static final DJIUpgradeError RCTransformWriteFileInvalid = new DJIUpgradeError("传输写文件错误", -7009);
    public static final DJIUpgradeError RCWaitForUpgradePush = new DJIUpgradeError("文件检验等待client固件推送超时。", -7015);
    public static final DJIUpgradeError RC_LOW_ELECTRICITY = new DJIUpgradeError("当前遥控器电池电量过低!", -12000);
    public static final DJIUpgradeError RECEIVE_FIRMWARE_REQ_FAILED = new DJIUpgradeError("传输过程中，请求飞机接受文件失败", -3003);
    public static final DJIUpgradeError RECOGNISE_DEVICE_CONNECTION_ERROR = new DJIUpgradeError("当前有设备连接，APP不能去获取缓存设备的状态!", -13001);
    public static final DJIUpgradeError ReceiveFirmwareReqTimeout = new DJIUpgradeError("传输过程中，请求开始传输文件超时，当前链路不稳定！", -3011);
    public static final DJIUpgradeError RequestModuleReceiveFileFailed = new DJIUpgradeError("当前模块请求接受文件失败", -6004);
    public static final DJIUpgradeError SAME_VERSION = new DJIUpgradeError("版本一致", -4002);
    public static final DJIUpgradeError SDCardError = new DJIUpgradeError("SD Card错误", -5010);
    public static final DJIUpgradeError SDCardFull = new DJIUpgradeError("SD Card满", -5009);
    public static final DJIUpgradeError SEND_PACK_TIMEOUT = new DJIUpgradeError("拉取固件cfg文件信息发包超时", NotificationManagerCompat.IMPORTANCE_UNSPECIFIED);
    public static final DJIUpgradeError SensorError = new DJIUpgradeError("传感器错误", -5012);
    public static final DJIUpgradeError SetParamFailed = new DJIUpgradeError("设置参数失败", -5006);
    public static final DJIUpgradeError TIME_OUT = new DJIUpgradeError("等待超时取消", -4004);
    public static final DJIUpgradeError TRANSFER_CANCELED = new DJIUpgradeError("固件传输被取消！", -3006);
    public static final DJIUpgradeError TRANSFER_ERROR = new DJIUpgradeError("固件传输错误", -4011);
    public static final DJIUpgradeError TimeNotSync = new DJIUpgradeError("时间不同步", -5005);
    public static final DJIUpgradeError TransformModuleFTPDataFailed = new DJIUpgradeError("当前模块请求FTP传输文件失败", -6007);
    public static final DJIUpgradeError TransformModuleV1DataFailed = new DJIUpgradeError("当前模块请求V1传输数据失败", -6005);
    public static final DJIUpgradeError UNKNOWN_COMPONENT = new DJIUpgradeError("未知的升级模块", -7);
    public static final DJIUpgradeError UPGRADE_COMPONENT_NOT_RECOGNISED = new DJIUpgradeError("没有识别到当前升级的组件!", -11001);
    public static final DJIUpgradeError UPGRADE_END_FAILED = new DJIUpgradeError("Upgrade end failed", -4000);
    public static final DJIUpgradeError USER_CANCEL = new DJIUpgradeError("用户取消", -4003);
    public static final DJIUpgradeError UpgradeCurrentModuleError = new DJIUpgradeError("升级当前模块失败。", -6000);
    public static final DJIUpgradeError UpgradeStateError = new DJIUpgradeError("固件升级状态错误", -5019);
    public static final DJIUpgradeError VERIFY_FAILED = new DJIUpgradeError("文件校验失败!", -3004);
    public static final DJIUpgradeError VERSION_NOT_FETCHED_YET = new DJIUpgradeError("服务器版本还没有拉取到", -4);
    private String desc;
    private int errorCode = -1;

    protected DJIUpgradeError(String desc2) {
        super(desc2);
    }

    private DJIUpgradeError(String desc2, int errorCode2) {
        super(desc2, errorCode2);
    }

    public static DJIUpgradeError getDJIErrorByCode(int code) {
        switch (code) {
            case -8008:
                return RCMCUWriteFirmwareFailed;
            case -8007:
                return RCMCURecvFileFailed;
            case -8006:
                return RCMCUStartReceiveFileFailed;
            case -8005:
                return RCMCUEnterLoaderFailed;
            case -8004:
                return RCMCUCommunication;
            case -8003:
                return RCFileVerifyFailed;
            case -8002:
                return RCFlashWriteFailed;
            case -8001:
                return RCFlashEraseFailed;
            case -8000:
                return RCFileParsingFailed;
            case -7015:
                return RCWaitForUpgradePush;
            case -7014:
                return RCMD5Invalid;
            case -7013:
                return RCFileVerifyInvalidState;
            case -7012:
                return RCFileVerifyInvalidParam;
            case -7011:
                return RCTransformPackIndexOverRange;
            case -7010:
                return RCTransformIndexInvalid;
            case -7009:
                return RCTransformWriteFileInvalid;
            case -7008:
                return RCTransformFileInvalidState;
            case -7007:
                return RCTransformFileInvalidParam;
            case -7006:
                return RCRequestSendFileOpenFileFailed;
            case -7005:
                return RCRequestSendFileInvalidState;
            case -7004:
                return RCRequestSendFileInvalidParam;
            case -7003:
                return RCEnterLoaderLowBattery;
            case -7002:
                return RCEnterLoaderMotorIsOn;
            case -7001:
                return RCEnterLoaderInvalidState;
            case -7000:
                return RCEnterLoaderInvalidParam;
            case -6013:
                return CurrentModuleExtraFileFailed;
            case -6012:
                return CurrentModuleWriteFileFailed;
            case -6011:
                return CurrentModuleAccessFileSystemFailed;
            case -6010:
                return CurrentModuleLoadFirmwareToMemoryFailed;
            case -6009:
                return CurrentModuleWriteFirmwareFailed;
            case -6008:
                return CurrentModuleHardwareError;
            case -6007:
                return TransformModuleFTPDataFailed;
            case -6005:
                return TransformModuleV1DataFailed;
            case -6004:
                return RequestModuleReceiveFileFailed;
            case -6003:
                return CurrentModuleEnterLoaderFailed;
            case -6002:
                return CurrentModuleFirmwareError;
            case -6001:
                return CurrentModuleDisconnect;
            case -6000:
                return UpgradeCurrentModuleError;
            case -5027:
                return CfgFileCreateFailed;
            case -5026:
                return CfgFileWriteFailed;
            case -5025:
                return NotSupportSDCardUpgrade;
            case -5024:
                return BootNotReady;
            case -5023:
                return CheckSecDebugFailed;
            case -5022:
                return CheckConnectionError;
            case -5021:
                return HardwareError;
            case -5020:
                return FirmwareTypeError;
            case -5019:
                return UpgradeStateError;
            case -5018:
                return FlashProgramError;
            case -5017:
                return EraseFlashFailed;
            case -5016:
                return FirmwareCheckFailed;
            case -5015:
                return ExceedFlash;
            case -5014:
                return PackIndexInvalid;
            case -5013:
                return ParamLengthInvalid;
            case -5012:
                return SensorError;
            case -5011:
                return CriticalError;
            case -5010:
                return SDCardError;
            case -5009:
                return SDCardFull;
            case -5008:
                return NoSDCard;
            case -5007:
                return GetParamFailed;
            case -5006:
                return SetParamFailed;
            case -5005:
                return TimeNotSync;
            case -5004:
                return InvalidState;
            case -5003:
                return InvalidParam;
            case -5002:
                return OverMemory;
            case -5001:
                return OperationTimeout;
            case -5000:
                return OperationInvalid;
            case -4019:
                return FIRMWARE_PACK_EXTRACT_ERROR;
            case -4018:
                return GROUND_TO_GLASS_ERROR;
            case -4017:
                return GROUND_DISCON_GLASS_SEC_TIME;
            case -4016:
                return GROUND_DISCON_GLASS_FIR_TIME;
            case -4015:
                return GLASS_FIMRWARE_PACK_ERROR;
            case -4014:
                return NOT_FIND_CONFIG_FILE;
            case -4013:
                return AIR_FIRMWARE_PACK_ERROR;
            case -4012:
                return AIR_CONTROL_LINK_ERROR;
            case -4011:
                return TRANSFER_ERROR;
            case -4010:
                return GROUND_SDR_ERROR;
            case -4009:
                return AIR_SDR_ERROR;
            case -4008:
                return AIR_DATA_LINK_ERROR;
            case -4007:
                return INVALIDATE_ROLL_BACK;
            case -4006:
                return FIRMWARE_NOT_MATCH;
            case -4005:
                return MOTOR_RUNNING;
            case -4004:
                return TIME_OUT;
            case -4003:
                return USER_CANCEL;
            case -4002:
                return SAME_VERSION;
            case -4001:
                return FIRMWARE_ERROR;
            case -4000:
                return UPGRADE_END_FAILED;
            case -3014:
                return ENTER_LOADER_FAILED_LINK_REVERSE_FAILED;
            case -3013:
                return ENTER_LOADER_FAILED_LOW_BATTERY;
            case -3012:
                return CHECK_FAILED;
            case -3011:
                return ReceiveFirmwareReqTimeout;
            case -3010:
                return EnterLoaderTimeout;
            case -3009:
                return FIRMWARE_NOT_DOWNLOADED;
            case -3008:
                return PRODUCT_DISCONNECTED_WHEN_TRANSFERRING;
            case -3007:
                return FIRMWARE_TRANSFER_TIME_OUT;
            case -3006:
                return TRANSFER_CANCELED;
            case -3005:
                return FIRMWARE_TRANSFER_FAILED;
            case -3004:
                return VERIFY_FAILED;
            case -3003:
                return RECEIVE_FIRMWARE_REQ_FAILED;
            case -3002:
                return ENTER_LOADER_FAILED;
            case -3001:
                return FIRMWARE_DATA_CHECK_FAILED;
            case -3000:
                return INVALID_TRANSFER_STATE;
            case -2003:
                return DOWNLOAD_FAILED;
            case -2002:
                return NO_NETWORK;
            case -2001:
                return LAST_DOWNLOAD_NOT_END;
            case -2000:
                return INVALID_DOWNLOAD_INFO;
            case -1005:
                return GET_VERSION_PACK_SIZE_ERROR;
            case -1004:
                return GET_VERSION_ERROR;
            case -1003:
                return CFG_FILE_ERROR;
            case -1002:
                return CANNOT_FETCH_DEVICE_VERSION;
            case -1001:
                return INVALID_PACK_RET_CODE;
            case NotificationManagerCompat.IMPORTANCE_UNSPECIFIED /*-1000*/:
                return SEND_PACK_TIMEOUT;
            case CCResult.CODE_ERROR_REMOTE_CC_DELIVERY_FAILED /*-11*/:
                return FetchFileSizeInfoFailed;
            case CCResult.CODE_ERROR_CALLBACK_NOT_INVOKED /*-10*/:
                return FIRMWARE_DOWNLOAD_CANCELED;
            case CCResult.CODE_ERROR_TIMEOUT /*-9*/:
                return INVALID_TOKEN;
            case CCResult.CODE_ERROR_CANCELED /*-8*/:
                return CANNOT_FETCH_SERVER_VERSION;
            case CCResult.CODE_ERROR_CONNECT_FAILED /*-7*/:
                return UNKNOWN_COMPONENT;
            case CCResult.CODE_ERROR_CONTEXT_NULL /*-6*/:
                return FETCH_DOWNLOAD_INFO_FAILED;
            case -5:
                return FIRMWARE_DOWNLOADED;
            case -4:
                return VERSION_NOT_FETCHED_YET;
            case -3:
                return CFG_PARSE_FAILED;
            case -2:
                return PROCESSER_START_FAILED;
            case -1:
                return COMPONENT_NOT_FOUND;
            case 0:
                return null;
            default:
                if (code > UPGRADE_END_FAILED.errorCode || code < AIRCRAFT_NEED_DIRECT_CONNECTED.errorCode) {
                    return new DJIUpgradeError("App定义错误码, 找App确认问题", code);
                }
                return new DJIUpgradeError("固件定义错误码，App没有进行转换，告知App，联系固件分析原因", code);
        }
    }
}
