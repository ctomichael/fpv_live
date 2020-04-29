package dji.component.flysafe.unlock.model;

import android.support.annotation.Keep;
import dji.component.flysafe.util.ProtobufHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.flysafe.v3.License;
import dji.flysafe.v3.LicenseModel;
import dji.midware.data.model.P3.DataWhiteListRequestLicense;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class WhiteListLicense {
    protected String description;
    protected boolean enabled;
    protected int endTimeStamp;
    protected int index;
    protected boolean isInValidDateByApp;
    protected boolean isUserOnly;
    protected boolean isValid;
    protected int level;
    protected LicenseType licenseType;
    protected byte[] liscenseAllBytes;
    protected int mHeightLimit;
    protected int mLicenseId;
    protected int startTimeStamp;

    public int hashCode() {
        int i;
        int i2;
        int i3 = 1;
        int i4 = 0;
        int i5 = ((((((((this.startTimeStamp * 31) + this.endTimeStamp) * 31) + this.level) * 31) + this.index) * 31) + this.mLicenseId) * 31;
        if (this.enabled) {
            i = 1;
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (!this.isValid) {
            i3 = 0;
        }
        int i7 = (i6 + i3) * 31;
        if (this.liscenseAllBytes != null) {
            i2 = this.liscenseAllBytes.hashCode();
        } else {
            i2 = 0;
        }
        int i8 = (i7 + i2) * 31;
        if (this.licenseType != null) {
            i4 = this.licenseType.hashCode();
        }
        return i8 + i4;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof WhiteListLicense)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.startTimeStamp != ((WhiteListLicense) o).startTimeStamp) {
            return false;
        }
        if (this.endTimeStamp != ((WhiteListLicense) o).endTimeStamp) {
            return false;
        }
        if (this.level != ((WhiteListLicense) o).level) {
            return false;
        }
        if (this.index != ((WhiteListLicense) o).index) {
            return false;
        }
        if (this.mLicenseId != ((WhiteListLicense) o).mLicenseId) {
            return false;
        }
        if (this.enabled != ((WhiteListLicense) o).enabled) {
            return false;
        }
        if (this.isValid != ((WhiteListLicense) o).isValid) {
            return false;
        }
        if (this.description != null && ((WhiteListLicense) o).description != null) {
            return false;
        }
        if (this.licenseType != null && ((WhiteListLicense) o).licenseType != null) {
            return false;
        }
        if (this.liscenseAllBytes == null || ((WhiteListLicense) o).liscenseAllBytes == null) {
            if (this.liscenseAllBytes == null || ((WhiteListLicense) o).liscenseAllBytes == null) {
                return false;
            }
        } else if (this.liscenseAllBytes.length != ((WhiteListLicense) o).liscenseAllBytes.length) {
            return false;
        } else {
            for (int i = 0; i < this.liscenseAllBytes.length; i++) {
                if (this.liscenseAllBytes[i] != ((WhiteListLicense) o).liscenseAllBytes[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level2) {
        this.level = level2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public long getStartTime() {
        return ProtobufHelper.uint32toLong(this.startTimeStamp);
    }

    public void setStartTime(int startTime) {
        this.startTimeStamp = startTime;
    }

    public long getEndTime() {
        return ProtobufHelper.uint32toLong(this.endTimeStamp);
    }

    public void setEndTime(int endTimeStamp2) {
        this.endTimeStamp = endTimeStamp2;
    }

    public LicenseType getLicenseType() {
        return this.licenseType;
    }

    public void setLicenseType(LicenseType licenseType2) {
        this.licenseType = licenseType2;
    }

    public byte[] getLiscenseAllBytes() {
        return this.liscenseAllBytes;
    }

    public void setLiscenseAllBytes(byte[] liscenseAllBytes2) {
        this.liscenseAllBytes = liscenseAllBytes2;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index2) {
        this.index = index2;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled2) {
        this.enabled = enabled2;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    public int getLicenseId() {
        return this.mLicenseId;
    }

    public void setLicenseId(int licenseId) {
        this.mLicenseId = licenseId;
    }

    public int getHeightLimit() {
        return this.mHeightLimit;
    }

    public WhiteListLicense(DataWhiteListRequestLicense licenseData, int index2) {
        this.mHeightLimit = -1;
        this.isUserOnly = false;
        this.description = BytesUtil.getStringUTF8(licenseData.getDescription());
        this.startTimeStamp = BytesUtil.getInt(licenseData.getStartTime());
        this.endTimeStamp = BytesUtil.getInt(licenseData.getEndTime());
        this.licenseType = LicenseType.find(licenseData.getType());
        this.level = licenseData.getLevel();
        this.liscenseAllBytes = licenseData.getLicenseBytes();
        this.enabled = licenseData.isEnable();
        this.isValid = licenseData.isValid();
        this.mLicenseId = licenseData.getLicenseId();
        this.index = index2;
    }

    public WhiteListLicense(License protobufLicense) {
        this.mHeightLimit = -1;
        this.isUserOnly = false;
        this.description = protobufLicense.description;
        this.startTimeStamp = protobufLicense.start_at.intValue();
        this.endTimeStamp = protobufLicense.end_at.intValue();
        this.licenseType = LicenseType.find(protobufLicense.data);
        this.mLicenseId = protobufLicense.id.intValue();
        if (protobufLicense.user_only != null) {
            this.isUserOnly = protobufLicense.user_only.booleanValue();
        }
    }

    public static WhiteListLicense parseFromProtoBufLicenseModel(LicenseModel protobufLicenseModel) {
        WhiteListLicense whiteListLicense = parseFromProtoBufData(protobufLicenseModel.license);
        whiteListLicense.enabled = ProtobufHelper.toBool(protobufLicenseModel.license_status.enable);
        whiteListLicense.isValid = ProtobufHelper.toBool(protobufLicenseModel.license_status.invalid);
        whiteListLicense.isInValidDateByApp = ProtobufHelper.toBool(protobufLicenseModel.license_status.in_valid_date);
        return whiteListLicense;
    }

    public WhiteListLicense(FlyfrbLicenseV3Info licenseV3Info) {
        this.mHeightLimit = -1;
        this.isUserOnly = false;
        this.description = licenseV3Info.description;
        this.startTimeStamp = (int) licenseV3Info.start_at;
        this.endTimeStamp = (int) licenseV3Info.end_at;
        this.licenseType = LicenseType.find(licenseV3Info.type);
        this.mLicenseId = licenseV3Info.id;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public String description;
        /* access modifiers changed from: private */
        public boolean enabled;
        /* access modifiers changed from: private */
        public int endTimeStamp;
        /* access modifiers changed from: private */
        public int index;
        /* access modifiers changed from: private */
        public boolean isValid;
        /* access modifiers changed from: private */
        public int level;
        /* access modifiers changed from: private */
        public LicenseType licenseType;
        /* access modifiers changed from: private */
        public byte[] liscenseAllBytes;
        /* access modifiers changed from: private */
        public int mLicenseId;
        /* access modifiers changed from: private */
        public int startTimeStamp;

        public Builder startTimeStamp(int startTimeStamp2) {
            this.startTimeStamp = startTimeStamp2;
            return this;
        }

        public Builder endTimeStamp(int endTimeStamp2) {
            this.endTimeStamp = endTimeStamp2;
            return this;
        }

        public Builder level(int level2) {
            this.level = level2;
            return this;
        }

        public Builder index(int index2) {
            this.index = index2;
            return this;
        }

        public Builder licenseId(int licenseId) {
            this.mLicenseId = licenseId;
            return this;
        }

        public Builder isEnabled(boolean enabled2) {
            this.enabled = enabled2;
            return this;
        }

        public Builder isValid(boolean isValid2) {
            this.isValid = isValid2;
            return this;
        }

        public Builder licenseAllBytes(byte[] liscenseAllBytes2) {
            this.liscenseAllBytes = liscenseAllBytes2;
            return this;
        }

        public Builder description(String description2) {
            this.description = description2;
            return this;
        }

        public Builder licenseType(LicenseType licenseType2) {
            this.licenseType = licenseType2;
            return this;
        }

        public WhiteListLicense build() {
            return new WhiteListLicense(this);
        }
    }

    private WhiteListLicense(Builder builder) {
        this.mHeightLimit = -1;
        this.isUserOnly = false;
        this.startTimeStamp = builder.startTimeStamp;
        this.endTimeStamp = builder.endTimeStamp;
        this.level = builder.level;
        this.index = builder.index;
        this.mLicenseId = builder.mLicenseId;
        this.enabled = builder.enabled;
        this.isValid = builder.isValid;
        this.liscenseAllBytes = builder.liscenseAllBytes;
        this.description = builder.description;
        this.licenseType = builder.licenseType;
    }

    public static WhiteListLicense parseFromProtoBufData(License protobufLicense) {
        LicenseType type = LicenseType.find(protobufLicense.data);
        if (type == LicenseType.GEO_UNLOCK) {
            return new GeoUnlockLicense(protobufLicense);
        }
        if (type == LicenseType.COUNTRY_UNLOCK) {
            return new CountryUnlockLicense(protobufLicense);
        }
        if (type == LicenseType.CIRCLE_UNLOCK_AREA) {
            return new CircleUnlockAreaLicense(protobufLicense);
        }
        if (type == LicenseType.PARAMETER_CONFIGURATION) {
            return new HeightUnlockLicense(protobufLicense);
        }
        return new PentagonUnlockAreaLicense(protobufLicense);
    }

    public WhiteListLicense() {
        this.mHeightLimit = -1;
        this.isUserOnly = false;
    }
}
