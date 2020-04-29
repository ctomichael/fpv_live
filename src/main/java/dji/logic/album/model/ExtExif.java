package dji.logic.album.model;

import android.support.annotation.Keep;
import com.dji.cmd.v1.base.ProtocolBytesUtil;
import java.io.Serializable;

@Keep
public class ExtExif implements Serializable {
    public static final int DATA_LENGTH = 35;
    private byte[] innerData;

    public ExtExif() {
        this.innerData = null;
        this.innerData = new byte[35];
    }

    public ExtExif(byte[] bytes) {
        this.innerData = null;
        this.innerData = new byte[bytes.length];
        System.arraycopy(bytes, 0, this.innerData, 0, bytes.length);
    }

    public static ExtExif dataFromBytes(byte[] bytes) {
        ExtExif res = new ExtExif();
        res.innerData = new byte[bytes.length];
        System.arraycopy(bytes, 0, res.innerData, 0, bytes.length);
        return res;
    }

    public static ExtExif dataFromBytes(byte[] bytes, int offset) {
        ExtExif res = new ExtExif();
        res.innerData = new byte[35];
        try {
            System.arraycopy(bytes, offset, res.innerData, 0, 35);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public byte[] getBytes() {
        byte[] res = new byte[this.innerData.length];
        System.arraycopy(this.innerData, 0, res, 0, this.innerData.length);
        return res;
    }

    public int getEnableExposureTime() {
        return ProtocolBytesUtil.getInt(this.innerData, 0, 1, 0, 1);
    }

    public ExtExif setEnableExposureTime(int enableExposureTime) {
        ProtocolBytesUtil.setValue(enableExposureTime, this.innerData, 0, 1, 0, 1);
        return this;
    }

    public int getEnableFnumber() {
        return ProtocolBytesUtil.getInt(this.innerData, 0, 1, 1, 1);
    }

    public ExtExif setEnableFnumber(int enableFnumber) {
        ProtocolBytesUtil.setValue(enableFnumber, this.innerData, 0, 1, 1, 1);
        return this;
    }

    public int getEnableExposureProgram() {
        return ProtocolBytesUtil.getInt(this.innerData, 0, 1, 2, 1);
    }

    public ExtExif setEnableExposureProgram(int enableExposureProgram) {
        ProtocolBytesUtil.setValue(enableExposureProgram, this.innerData, 0, 1, 2, 1);
        return this;
    }

    public int getEnableIso() {
        return ProtocolBytesUtil.getInt(this.innerData, 0, 1, 3, 1);
    }

    public ExtExif setEnableIso(int enableIso) {
        ProtocolBytesUtil.setValue(enableIso, this.innerData, 0, 1, 3, 1);
        return this;
    }

    public int getEnableExposureCompensation() {
        return ProtocolBytesUtil.getInt(this.innerData, 0, 1, 4, 1);
    }

    public ExtExif setEnableExposureCompensation(int enableExposureCompensation) {
        ProtocolBytesUtil.setValue(enableExposureCompensation, this.innerData, 0, 1, 4, 1);
        return this;
    }

    public int getEnableMeteringMode() {
        return ProtocolBytesUtil.getInt(this.innerData, 0, 1, 5, 1);
    }

    public ExtExif setEnableMeteringMode(int enableMeteringMode) {
        ProtocolBytesUtil.setValue(enableMeteringMode, this.innerData, 0, 1, 5, 1);
        return this;
    }

    public int getEnableLightSource() {
        return ProtocolBytesUtil.getInt(this.innerData, 0, 1, 6, 1);
    }

    public ExtExif setEnableLightSource(int enableLightSource) {
        ProtocolBytesUtil.setValue(enableLightSource, this.innerData, 0, 1, 6, 1);
        return this;
    }

    public int getEnableFocalLength35mmFormat() {
        return ProtocolBytesUtil.getInt(this.innerData, 0, 1, 7, 1);
    }

    public ExtExif setEnableFocalLength35mmFormat(int enableFocalLength35mmFormat) {
        ProtocolBytesUtil.setValue(enableFocalLength35mmFormat, this.innerData, 0, 1, 7, 1);
        return this;
    }

    public long getExposureTimeNum() {
        return ProtocolBytesUtil.getLong(this.innerData, 1, 4);
    }

    public ExtExif setExposureTimeNum(long exposureTimeNum) {
        ProtocolBytesUtil.setValue(exposureTimeNum, this.innerData, 1, 4);
        return this;
    }

    public long getExposureTimeDen() {
        return ProtocolBytesUtil.getLong(this.innerData, 5, 4);
    }

    public ExtExif setExposureTimeDen(long exposureTimeDen) {
        ProtocolBytesUtil.setValue(exposureTimeDen, this.innerData, 5, 4);
        return this;
    }

    public long getFnumberNum() {
        return ProtocolBytesUtil.getLong(this.innerData, 9, 4);
    }

    public ExtExif setFnumberNum(long fnumberNum) {
        ProtocolBytesUtil.setValue(fnumberNum, this.innerData, 9, 4);
        return this;
    }

    public long getFnumberDen() {
        return ProtocolBytesUtil.getLong(this.innerData, 13, 4);
    }

    public ExtExif setFnumberDen(long fnumberDen) {
        ProtocolBytesUtil.setValue(fnumberDen, this.innerData, 13, 4);
        return this;
    }

    public int getExposureProgram() {
        return ProtocolBytesUtil.getInt(this.innerData, 17, 2);
    }

    public ExtExif setExposureProgram(int exposureProgram) {
        ProtocolBytesUtil.setValue(exposureProgram, this.innerData, 17, 2);
        return this;
    }

    public int getIso() {
        return ProtocolBytesUtil.getInt(this.innerData, 19, 2);
    }

    public ExtExif setIso(int iso) {
        ProtocolBytesUtil.setValue(iso, this.innerData, 19, 2);
        return this;
    }

    public int getExposureCompensationNum() {
        return ProtocolBytesUtil.getInt(this.innerData, 21, 4);
    }

    public ExtExif setExposureCompensationNum(int exposureCompensationNum) {
        ProtocolBytesUtil.setValue(exposureCompensationNum, this.innerData, 21, 4);
        return this;
    }

    public int getExposureCompensationDen() {
        return ProtocolBytesUtil.getInt(this.innerData, 25, 4);
    }

    public ExtExif setExposureCompensationDen(int exposureCompensationDen) {
        ProtocolBytesUtil.setValue(exposureCompensationDen, this.innerData, 25, 4);
        return this;
    }

    public int getMeteringMode() {
        return ProtocolBytesUtil.getInt(this.innerData, 29, 2);
    }

    public ExtExif setMeteringMode(int meteringMode) {
        ProtocolBytesUtil.setValue(meteringMode, this.innerData, 29, 2);
        return this;
    }

    public int getLightSource() {
        return ProtocolBytesUtil.getInt(this.innerData, 31, 2);
    }

    public ExtExif setLightSource(int lightSource) {
        ProtocolBytesUtil.setValue(lightSource, this.innerData, 31, 2);
        return this;
    }

    public int getFocalLength35mmFormat() {
        return ProtocolBytesUtil.getInt(this.innerData, 33, 2);
    }

    public ExtExif setFocalLength35mmFormat(int focalLength35mmFormat) {
        ProtocolBytesUtil.setValue(focalLength35mmFormat, this.innerData, 33, 2);
        return this;
    }
}
