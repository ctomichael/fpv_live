package dji.sdksharedlib.util.configuration;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;

@EXClassNullAway
public class DJISDKCacheProductConfigsKey {
    public int accessMask;
    public String backupKey;
    public int dataSize;
    public Class dataType;
    public Number defaultValue;
    public String keyHashName;
    public String keyName;
    public Number maxValue;
    public int maxVersion;
    public Number minValue;
    public int minVersion;
    public DJISDKCacheUpdateType updateType;

    public DJISDKCacheProductConfigsKey(int dataSize2, Number minValue2, Number maxValue2, Number defaultValue2, int accessMask2, DJISDKCacheUpdateType updateType2, String keyName2, String keyHashName2, int maxVersion2, int minVersion2, String backupKey2, Class dataType2) {
        this.dataSize = dataSize2;
        this.minValue = minValue2;
        this.maxValue = maxValue2;
        this.defaultValue = defaultValue2;
        this.accessMask = accessMask2;
        this.updateType = updateType2;
        this.keyName = keyName2;
        this.keyHashName = keyHashName2;
        this.maxVersion = maxVersion2;
        this.minVersion = minVersion2;
        this.backupKey = backupKey2;
        this.dataType = dataType2;
    }

    public static class Builder {
        private int accessMask;
        private String backupKey;
        private int dataSize;
        private Class dataType;
        private Number defaultValue;
        private String keyHashName;
        private String keyName;
        private Number maxValue;
        private int maxVersion;
        private Number minValue;
        private int minVersion;
        private DJISDKCacheUpdateType updateType;

        public DJISDKCacheProductConfigsKey build() {
            return new DJISDKCacheProductConfigsKey(this.dataSize, this.minValue, this.maxValue, this.defaultValue, this.accessMask, this.updateType, this.keyName, this.keyHashName, this.maxVersion, this.minVersion, this.backupKey, this.dataType);
        }

        public Builder dataType(Class dataType2) {
            this.dataType = dataType2;
            return this;
        }

        public Builder updateType(DJISDKCacheUpdateType updateType2) {
            this.updateType = updateType2;
            return this;
        }

        public Builder backupKey(String backupKey2) {
            this.backupKey = backupKey2;
            return this;
        }

        public Builder keyName(String keyName2) {
            this.keyName = keyName2;
            return this;
        }

        public Builder keyHashName(String keyHashName2) {
            this.keyHashName = keyHashName2;
            return this;
        }

        public Builder maxVersion(int maxVersion2) {
            this.maxVersion = maxVersion2;
            return this;
        }

        public Builder minVersion(int minVersion2) {
            this.minVersion = minVersion2;
            return this;
        }

        public Builder dataSize(int dataSize2) {
            this.dataSize = dataSize2;
            return this;
        }

        public Builder accessMask(int accessMask2) {
            this.accessMask = accessMask2;
            return this;
        }

        public Builder minValue(Number minValue2) {
            this.minValue = minValue2;
            return this;
        }

        public Builder maxValue(Number maxValue2) {
            this.maxValue = maxValue2;
            return this;
        }

        public Builder defaultValue(Number defaultValue2) {
            this.defaultValue = defaultValue2;
            return this;
        }
    }
}
