package dji.midware.media.metadata;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.util.BytesUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;

@EXClassNullAway
public class OriginalVideoMetadataRetriever {
    private static final int MDL = -1452448660;
    private static final int MOOV = 1836019574;
    private static final int UDATA = 1969517665;
    private static final int XYZ = -1451722335;
    private int atomSize = 0;
    private int atomType = 0;
    private byte[] buffer = new byte[4];
    private byte[] bufferValue = new byte[1000];
    private File file;
    private HashMap<String, String> hashMap = new HashMap<>();
    private int moovlength = 0;
    private long offset = 0;
    RandomAccessFile randomAccessFile;
    private String typeName = "";

    public ProductType getProductType() {
        ProductType productType;
        ProductType productType2 = ProductType.None;
        String camera = this.hashMap.get("mdl");
        DataCameraGetPushStateInfo.CameraType cameraType = null;
        if ("FC300S".equals(camera)) {
            cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300S;
        } else if ("FC300X".equals(camera)) {
            cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300X;
        } else if ("FC260".equals(camera)) {
            cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC260;
        } else if ("FC350".equals(camera)) {
            cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350;
        } else if ("HG310".equals(camera)) {
            return ProductType.Longan;
        } else {
            if ("OSMO RAW".equals(camera)) {
                return ProductType.LonganRaw;
            }
            if ("OSMO PRO".equals(camera)) {
                return ProductType.LonganPro;
            }
            if ("FC300XW".equalsIgnoreCase(camera)) {
                cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300XW;
            } else if ("FC550RAW".equalsIgnoreCase(camera)) {
                cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw;
            } else if ("FC550".equalsIgnoreCase(camera)) {
                cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550;
            } else if ("FC330".equalsIgnoreCase(camera)) {
                cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC330X;
            } else if ("FC6310".equals(camera)) {
                cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310;
            } else if ("FC6310S".equals(camera)) {
                cameraType = DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310S;
            } else if (camera == null) {
            }
        }
        if (productType2 != ProductType.None) {
            return productType2;
        }
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300S) {
            productType = ProductType.litchiS;
        } else if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300X) {
            productType = ProductType.litchiX;
        } else if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC260) {
            productType = ProductType.litchiC;
        } else if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350) {
            productType = ProductType.Orange;
        } else if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300XW) {
            productType = ProductType.P34K;
        } else if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550) {
            productType = ProductType.BigBanana;
        } else if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw) {
            productType = ProductType.OrangeRAW;
        } else if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC330X) {
            productType = ProductType.Tomato;
        } else if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310) {
            productType = ProductType.Pomato;
        } else if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310S) {
            productType = ProductType.PomatoSDR;
        } else if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540) {
            productType = ProductType.Orange2;
        } else {
            productType = ProductType.OTHER;
        }
        return productType;
    }

    public double[] getGPS() {
        double[] gps = new double[3];
        String xyz = this.hashMap.get("xyz");
        if (xyz != null && !xyz.isEmpty()) {
            String[] s = xyz.split("\\+");
            if (s.length >= 4) {
                gps[0] = Double.parseDouble(s[1]);
                gps[1] = Double.parseDouble(s[2]);
                gps[2] = Double.parseDouble(s[3]);
            } else {
                gps[0] = 0.0d;
                gps[1] = 0.0d;
                gps[2] = 0.0d;
            }
        }
        return gps;
    }

    public HashMap<String, String> getMap() {
        return this.hashMap;
    }

    public void setDataSource(String path) throws FileNotFoundException {
        this.file = new File(path);
        this.randomAccessFile = new RandomAccessFile(this.file, "rw");
    }

    public void extract() throws IOException {
        long length = this.randomAccessFile.length();
        this.offset = 0;
        this.atomSize = 0;
        this.atomType = 0;
        goNextStep();
        while (length > this.offset) {
            switch (this.atomType) {
                case MOOV /*1836019574*/:
                    this.moovlength = this.atomSize - 8;
                    goNextStep(false);
                    this.moovlength -= 8;
                    while (this.moovlength > 0) {
                        switch (this.atomType) {
                            case UDATA /*1969517665*/:
                                int udatalength = this.atomSize - 8;
                                while (udatalength > 0) {
                                    goNextStep(false);
                                    int size = this.atomSize - 8;
                                    udatalength = (udatalength - 8) - size;
                                    this.randomAccessFile.read(this.bufferValue, 0, size);
                                    this.hashMap.put(this.typeName, getString(ByteBuffer.wrap(this.bufferValue, 0, size).order(ByteOrder.BIG_ENDIAN)).trim());
                                }
                                this.moovlength = 0;
                                break;
                            default:
                                goNextStep();
                                this.moovlength -= this.atomSize + 8;
                                break;
                        }
                    }
                    this.offset = length;
                    break;
                default:
                    goNextStep();
                    break;
            }
        }
    }

    private void goNextStep() throws IOException {
        goNextStep(true);
    }

    private void goNextStep(boolean isSeek) throws IOException {
        if (this.atomSize > 0 && isSeek) {
            this.randomAccessFile.skipBytes(this.atomSize - 8);
            this.offset += (long) (this.atomSize - 8);
        }
        this.randomAccessFile.read(this.buffer);
        this.offset += 4;
        this.atomSize = get(this.buffer);
        this.randomAccessFile.read(this.buffer);
        this.offset += 4;
        this.atomType = get(this.buffer);
        this.typeName = getString(this.buffer);
    }

    private int get(byte[] buffer2) {
        return ByteBuffer.wrap(buffer2, 0, 4).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    private String getString(byte[] buffer2) {
        buffer2[0] = 0;
        return new String(ByteBuffer.wrap(buffer2, 0, 4).order(ByteOrder.BIG_ENDIAN).array(), Charset.forName("UTF-8")).trim();
    }

    private String getString(ByteBuffer buffer2) {
        return getStringUTF8(buffer2.array());
    }

    public static String getStringUTF8(byte[] bytes) {
        int i = 1;
        while (true) {
            if (i >= bytes.length) {
                break;
            } else if (bytes[i] == 0) {
                bytes = BytesUtil.readBytes(bytes, 0, i);
                break;
            } else {
                i++;
            }
        }
        return new String(bytes, Charset.forName("GBK"));
    }

    public Date getCaptureDate() {
        String[] splitArray = this.file.getName().split("_");
        if (!splitArray[0].equals("org") || splitArray.length != 3 || splitArray[2].length() < 4) {
            return new Date();
        }
        try {
            return new Date(Long.parseLong(splitArray[2].substring(0, splitArray[2].length() - 4)));
        } catch (Exception e) {
            return new Date();
        }
    }
}
