package dji.component.hostdevice;

import java.io.Serializable;
import java.util.Objects;

public interface IHostDeviceService {
    public static final String COMPONENT_NAME = "HostDeviceService";
    public static final String UNKNOWN = "Unknown";
    public static final float UNKNOWN_FLOAT = -1.0f;
    public static final int UNKNOWN_INT = -1;

    public interface OS {
        public static final String DEVICE_FEATURES_ARRAY = "os_device_features_array";
        public static final String DEVICE_FEATURES_NAME_STRING = "os_device_features_name";
        public static final String SDK_VERSION_BASE_OS = "os_sdk_version_base_os";
        public static final String SDK_VERSION_INCREMENTAL = "os_sdk_version_incremental";
        public static final String SDK_VERSION_INT = "os_sdk_version_int";
        public static final String SDK_VERSION_PREVIEW_INT = "os_sdk_version_preview_int";
        public static final String SDK_VERSION_RELEASE = "os_sdk_version_release";
        public static final String SDK_VERSION_SECURITY_PATCH = "os_sdk_version_security_patch";
    }

    public interface MediaCodec {
        public static final String H264_CODEC = "media_codec_h264_codec";
        public static final String H265_CODEC = "media_codec_h265_codec";
    }

    public interface Memory {
        public static final String RAM_AVAILABLE = "memory_ram_available";
        public static final String RAM_TOTAL = "memory_ram_total";
    }

    public interface Screen {
        public static final String DENSITY_DPI_INT = "screen_density_dpi_int";
        public static final String DENSITY_SCALE_FLOAT = "screen_density_scale";
        public static final String DISPLAY_METRICS_RESOLUTION = "screen_display_metrics_resolution";
        public static final String HEIGHT_INT = "screen_height";
        public static final String IS_HDR = "screen_is_hdr";
        public static final String PHYSICAL_SIZE = "screen_physical_size";
        public static final String REFRESH_RATE = "screen_refresh_rate";
        public static final String SIZE_RANGE = "screen_size_range";
        public static final String WIDTH_INT = "screen_width";
    }

    public interface Storage {
        public static final String INTERNAL_FREE = "storage_internal_free";
        public static final String INTERNAL_TOTAL = "storage_internal_total";
    }

    public interface Board {
        public static final String BRAND = "board_brand";
        public static final String MANUFACTURER = "board_manufacturer";
        public static final String MODEL_NAME = "board_model_name";
        public static final String NAME = "board_name";
        public static final String PRODUCT_NAME = "board_code_name";
    }

    public interface Cpu {
        public static final String CORE_NUM_INT = "cpu_core_num_int";
        public static final String HARDWARE_NAME = "cpu_hardware_name";
        public static final String IS_ARM64 = "cpu_is_arm64";
        public static final String NAME = "cpu_name";
        public static final String SUPPORT_ABI = "cpu_support_abi";
        public static final String TEMPERATURE = "cpu_temperature";
    }

    <T> T getValue(String str, T t);

    public interface Sensor {
        public static final String ACCELEROMETER = "sensor_accelerometer";
        public static final String AMBIENT_TEMPERATURE = "sensor_ambient_temperature";
        public static final String GEOMAGNETIC_ROTATION = "sensor_geomagnetic_rotation";
        public static final String GRAVITY = "sensor_gravity";
        public static final String GYROSCOPE = "sensor_gyroscope";
        public static final String LIGHT = "sensor_light";
        public static final String LINEAR_ACCELERATION = "sensor_linear_acceleration";
        public static final String MAGNETIC_FIELD = "sensor_magnetic_field";
        public static final String ORIENTATION_DEPRECATED = "sensor_orientation_deprecated";
        public static final String PRESSURE = "sensor_pressure";
        public static final String PROXIMITY = "sensor_proximity";
        public static final String RELATIVE_HUMIDITY = "sensor_relative_humidity";
        public static final String ROTATION_VECTOR = "sensor_rotation_vector";
        public static final String STEP_DETECTOR = "sensor_step_detector";
        public static final String TEMPERATURE = "sensor_temperature";
        public static final SimpleSensorBean UNKNOWN_SENSOR = new SimpleSensorBean(-1, "sensor_unknown", "sensor_unknown");

        public static class SimpleSensorBean implements Serializable {
            String name;
            int type;
            String typeName;

            public SimpleSensorBean(int type2, String typeName2, String name2) {
                this.type = type2;
                this.typeName = typeName2;
                this.name = name2;
            }

            public String toString() {
                return "SimpleSensorBean{type=" + this.type + ", typeName='" + this.typeName + '\'' + ", name='" + this.name + '\'' + '}';
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                SimpleSensorBean that = (SimpleSensorBean) o;
                if (this.type != that.type || !Objects.equals(this.typeName, that.typeName) || !Objects.equals(this.name, that.name)) {
                    return false;
                }
                return true;
            }

            public int hashCode() {
                return Objects.hash(Integer.valueOf(this.type), this.typeName, this.name);
            }
        }
    }
}
