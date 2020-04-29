package dji.common.error;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJISDKError extends DJIError {
    public static final DJISDKError APPLICATION_NOT_ACTIVATED = new DJISDKError("Application is not registered");
    public static final DJISDKError APP_KEY_INVALID_PLATFORM = new DJISDKError("The app key is not valid for this platform.");
    public static final DJISDKError APP_KEY_LEVEL_NOT_PERMITTED = new DJISDKError("The app key does not have the required permission.");
    public static final DJISDKError APP_KEY_NOT_ENROLLED_BY_BETA_SDK = new DJISDKError("The application is not enrolled in the Beta Program. Use an app key under the Beta Program instead.");
    public static final DJISDKError APP_KEY_NOT_EXIST = new DJISDKError("The app key does not exist. Please check the app key you provided.");
    public static final DJISDKError APP_KEY_PROHIBITED = new DJISDKError("The app key is prohibited, please contact <dev@dji.com> for help.");
    public static final DJISDKError BUNDLE_NOT_MATCH = new DJISDKError("The bundle identifier of your app should be identical to the one you registered on the website.");
    public static final DJISDKError COMMAND_EXECUTION_FAILED = new DJISDKError("There was an error executing the command");
    public static final DJISDKError CONNECTION_TO_SDK_FAILED = new DJISDKError("There was an error connecting to the SDK");
    public static final DJISDKError COULD_NOT_CONNECT_TO_INTERNET = new DJISDKError("For first time registration, app should be connected to Internet.");
    public static final DJISDKError DEVICE_NOT_FOUND = new DJISDKError("Device is not connected or does not exist.");
    public static final DJISDKError DEVICE_NOT_MATCH = new DJISDKError("Attempt to copy metadata from another registered device is not permitted.");
    public static final DJISDKError EMPTY_APP_KEY = new DJISDKError("The app key was not provided.");
    public static final DJISDKError FEATURE_NOT_SUPPORTED = new DJISDKError("This feature is not supported in the SDK");
    public static final DJISDKError HTTP_TIMEOUT = new DJISDKError("The server may be busy or is not reachable.");
    public static final DJISDKError INVALID_APP_KEY = new DJISDKError("The app key submitted is invalid. Please check the app key you provided.");
    public static final DJISDKError INVALID_APP_KEY_FOR_BETA_VERSION = new DJISDKError("The app key is invalid for this beta version.");
    public static final DJISDKError INVALID_METADATA = new DJISDKError("The metadata received from server is invalid, please reconnect to the server and try.");
    public static final DJISDKError INVALID_PARAMETERS = new DJISDKError("The parameters are invalid. Please review and submit the request");
    public static final DJISDKError MAX_ACTIVATION_COUNT_REACHED = new DJISDKError("The app key reached maximum number of activations, please contact <dev@dji.com> for help.");
    public static final DJISDKError NOT_DEFINED = new DJISDKError("Not defined error.");
    public static final DJISDKError OVER_MAX_ACTIVATIONS_COUNT = new DJISDKError("SDK user who do not have the professional membership only could have the maximum of 20 unique activations.");
    public static final DJISDKError PARAMETER_GET_FAILED = new DJISDKError("Getting parameters operation failed");
    public static final DJISDKError PARAMETER_SET_FAILED = new DJISDKError("Setting parameters operation failed");
    public static final DJISDKError REGISTRATION_AESENCRYPT_ERROR = new DJISDKError("Server error, please contact <dev@dji.com> for help.");
    public static final DJISDKError REGISTRATION_AESENCRYPT_FAILED = new DJISDKError("Server error, please contact <dev@dji.com> for help");
    public static final DJISDKError REGISTRATION_INVALID_UUID = new DJISDKError("Server error, please contact <dev@dji.com> for help.");
    public static final DJISDKError REGISTRATION_SUCCESS = new DJISDKError("API Key successfully registered");
    public static final DJISDKError SEND_DATA_FAILED = new DJISDKError("There was an error sending the data");
    public static final DJISDKError SERVER_DATA_ABNORMAL = new DJISDKError("Server error, please contact <dev@dji.com> for help.");
    public static final DJISDKError SERVER_PARSE_FAILURE = new DJISDKError("Server error, please contact <dev@dji.com> for help.");
    public static final DJISDKError SERVER_WRITE_FAILURE = new DJISDKError("Server error, please contact <dev@dji.com> for help.");
    public static final DJISDKError SYSTEM_BUSY = new DJISDKError("System is busy, please retry later");
    public static final DJISDKError UNKNOWN = new DJISDKError("Unknown error occurred during registration");

    private DJISDKError(String desc) {
        super(desc);
    }
}
