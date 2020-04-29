package com.dji.proto;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.proto.RootData;
import com.dji.util.Util;
import com.squareup.wire.Message;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import okio.ByteString;

@EXClassNullAway
public class DJIProtoUtil {
    private static final String AES = "AES";
    private static final String AES_ALG = "AES/CBC/PKCS5Padding";
    private static final byte[] AES_IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final String DEFAULT_AES_KEY = "";
    private static final int DEFAULT_PROTO_VERSION = 1;
    public static final int ENCRYPT_TYPE_AES = 1;
    public static final int ENCRYPT_TYPE_NONE = 0;
    private static final int OS_TYPE_ANDROID = 1;
    private static IDJIProtoInject injectAppInfo;

    @Retention(RetentionPolicy.SOURCE)
    public @interface EncryptType {
    }

    public static void setInject(IDJIProtoInject inject) {
        injectAppInfo = inject;
    }

    public static byte[] encodePackage(int protoVersion, int encryptType, @NonNull String aesKey, @NonNull Message message) throws Exception {
        return null;
    }

    public static byte[] encodePackageV1(int protoVersion, RootData.EncryptionType encryptType, @NonNull String aesKey, @NonNull Message message) {
        PackageInfo packageInfo = null;
        try {
            Context applicationContext = Util.getApplication().getApplicationContext();
            packageInfo = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (PackageManager.NameNotFoundException e5) {
            e5.printStackTrace();
        }
        RootData.Builder builder = new RootData.Builder();
        builder.encryption(encryptType);
        builder.version(Integer.valueOf(protoVersion));
        builder.platform(RootData.PlatformType.ANDROID);
        builder.app_version(Integer.valueOf(packageInfo.versionCode));
        if (encryptType == RootData.EncryptionType.NONE) {
            builder.content(ByteString.of(message.encode()));
        } else if (encryptType == RootData.EncryptionType.AES) {
            byte[] aesData = encrypt(message.encode(), aesKey);
            builder.content(ByteString.of(aesData, 0, aesData.length));
        } else {
            builder.content(ByteString.of(message.encode()));
        }
        return builder.build().encode();
    }

    public static byte[] encodeDefaultPackage(@NonNull Message message) throws Exception {
        return encodePackage(1, 0, "", message);
    }

    public static byte[] encodeDefaultPackageV1(@NonNull Message message) {
        return encodePackageV1(1, RootData.EncryptionType.NONE, "", message);
    }

    public static byte[] encodeDefaultAesPackage(@NonNull Message message) throws Exception {
        return encodePackage(1, 1, "", message);
    }

    public static byte[] encodeAesPackage(@NonNull String aesKey, @NonNull Message message) throws Exception {
        return encodePackage(1, 1, aesKey, message);
    }

    public static byte[] decodePackage(@NonNull String aesKey, @NonNull InputStream stream) throws Exception {
        return null;
    }

    public static byte[] decodePackageV1(@NonNull String aesKey, @NonNull byte[] bytes) {
        try {
            RootData rootData = RootData.ADAPTER.decode(bytes);
            RootData.EncryptionType encryptionType = rootData.encryption;
            if (rootData.content == null) {
                return null;
            }
            if (encryptionType == RootData.EncryptionType.NONE) {
                return rootData.content.toByteArray();
            }
            if (encryptionType == RootData.EncryptionType.AES) {
                return decrypt(rootData.content.toByteArray(), aesKey);
            }
            return rootData.content.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decodeDefaultPackage(@NonNull InputStream stream) throws Exception {
        return decodePackage("", stream);
    }

    public static byte[] decodeDefaultPackageV1(@NonNull byte[] bytes) {
        return decodePackageV1("", bytes);
    }

    public static byte[] decodeAesPackage(@NonNull String aesKey, @NonNull InputStream stream) throws Exception {
        return decodePackage(aesKey, stream);
    }

    public static byte[] decrypt(byte[] data, String key) {
        byte[] bArr = new byte[0];
        byte[] result = new byte[0];
        try {
            SecretKeySpec sKey = new SecretKeySpec(key.getBytes("utf-8"), AES);
            Cipher dCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            dCipher.init(2, sKey, new IvParameterSpec(AES_IV));
            byte[] result2 = dCipher.doFinal(data);
            if (result2 == null) {
                return new byte[0];
            }
            return result2;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            if (result == null) {
                return new byte[0];
            }
            return result;
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            if (result == null) {
                return new byte[0];
            }
            return result;
        } catch (InvalidKeyException e3) {
            e3.printStackTrace();
            if (result == null) {
                return new byte[0];
            }
            return result;
        } catch (InvalidAlgorithmParameterException e4) {
            e4.printStackTrace();
            if (result == null) {
                return new byte[0];
            }
            return result;
        } catch (NoSuchPaddingException e5) {
            e5.printStackTrace();
            if (result == null) {
                return new byte[0];
            }
            return result;
        } catch (BadPaddingException e6) {
            e6.printStackTrace();
            if (result == null) {
                return new byte[0];
            }
            return result;
        } catch (IllegalBlockSizeException e7) {
            e7.printStackTrace();
            if (result == null) {
                return new byte[0];
            }
            return result;
        } catch (Throwable th) {
            if (result == null) {
                byte[] result3 = new byte[0];
            }
            throw th;
        }
    }

    public static byte[] encrypt(byte[] input, String key) {
        byte[] bArr = new byte[0];
        try {
            SecretKeySpec skc = new SecretKeySpec(key.getBytes("utf-8"), AES);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, skc, new IvParameterSpec(AES_IV));
            return cipher.doFinal(input);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (NoSuchAlgorithmException e3) {
            e3.printStackTrace();
        } catch (InvalidAlgorithmParameterException e4) {
            e4.printStackTrace();
        } catch (InvalidKeyException e5) {
            e5.printStackTrace();
        } catch (BadPaddingException e6) {
            e6.printStackTrace();
        } catch (IllegalBlockSizeException e7) {
            e7.printStackTrace();
        }
        return null;
    }
}
