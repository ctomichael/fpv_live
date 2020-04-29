package dji.midware.sdk;

import android.content.Context;
import android.util.Base64;
import com.dji.api.protocol.IAccountCenterHttpApi;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@EXClassNullAway
public class AES {
    private static String TAG = "AES";
    private static byte[] iv = new byte[16];

    public static String decrypt(String encrypted, String key) throws Exception {
        byte[] bArr = new byte[32];
        SecretKeySpec skey = new SecretKeySpec(key.getBytes("utf-8"), "AES");
        Cipher dcipher = Cipher.getInstance(IAccountCenterHttpApi.ENCRYPT_TRANSFORMATION);
        dcipher.init(2, skey, new IvParameterSpec(iv));
        return new String(dcipher.doFinal(Base64.decode(encrypted, 8)));
    }

    public static String encrypt(String content, String key) throws Exception {
        byte[] input = content.getBytes("utf-8");
        byte[] bArr = new byte[32];
        SecretKeySpec skc = new SecretKeySpec(key.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance(IAccountCenterHttpApi.ENCRYPT_TRANSFORMATION);
        cipher.init(1, skc, new IvParameterSpec(iv));
        return Base64.encodeToString(cipher.doFinal(input), 8).replace("\n", "");
    }

    public static byte[] decrypt1860Database(byte[] content, SecretKeySpec key) {
        return decrypt(content, key, true);
    }

    public static byte[] decrypt(byte[] content, SecretKeySpec key, boolean is1860DatabaseFromServer) {
        Cipher cipher;
        if (is1860DatabaseFromServer) {
            try {
                cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                byte[] iv2 = new byte[16];
                Arrays.fill(iv2, (byte) 0);
                cipher.init(2, key, new IvParameterSpec(iv2));
            } catch (Exception e) {
                DJILog.d(TAG, "decrypt failed: " + e.getMessage(), new Object[0]);
                return null;
            }
        } else {
            cipher = Cipher.getInstance("AES");
            cipher.init(2, key);
        }
        return cipher.doFinal(content);
    }

    public static void decryptToFile(Context context, byte[] content, SecretKeySpec key, String outputFileName) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            byte[] iv2 = new byte[16];
            Arrays.fill(iv2, (byte) 0);
            cipher.init(2, key, new IvParameterSpec(iv2));
            byte[] result = cipher.doFinal(content);
            FileOutputStream fos = context.openFileOutput(outputFileName, 0);
            fos.write(result);
            fos.close();
        } catch (Exception e) {
            DJILog.d(TAG, "decrypt to file() failed: " + e.getMessage(), new Object[0]);
        }
    }

    public static void encrypt(Context context, byte[] content, SecretKeySpec key, String outputFileName) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(1, key);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
            cipherOutputStream.write(content);
            cipherOutputStream.flush();
            cipherOutputStream.close();
            FileOutputStream fos = context.openFileOutput(outputFileName, 0);
            fos.write(outputStream.toByteArray());
            fos.close();
        } catch (Exception e) {
            DJILog.d(TAG, "encrypt failed: " + e.getMessage(), new Object[0]);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v1, resolved type: java.security.InvalidAlgorithmParameterException} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:119:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0097 A[SYNTHETIC, Splitter:B:28:0x0097] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x009c A[SYNTHETIC, Splitter:B:31:0x009c] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00cd A[SYNTHETIC, Splitter:B:53:0x00cd] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00d2 A[SYNTHETIC, Splitter:B:56:0x00d2] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.File decryptFile(java.lang.String r20, java.lang.String r21, java.lang.String r22) {
        /*
            r8 = 0
            r13 = 0
            r5 = 0
            r16 = 0
            java.io.File r17 = new java.io.File     // Catch:{ IOException -> 0x015e, NoSuchPaddingException -> 0x00e0, NoSuchAlgorithmException -> 0x00e4, InvalidAlgorithmParameterException -> 0x00e8, InvalidKeyException -> 0x00ec }
            r0 = r17
            r1 = r21
            r0.<init>(r1)     // Catch:{ IOException -> 0x015e, NoSuchPaddingException -> 0x00e0, NoSuchAlgorithmException -> 0x00e4, InvalidAlgorithmParameterException -> 0x00e8, InvalidKeyException -> 0x00ec }
            java.io.File r6 = new java.io.File     // Catch:{ IOException -> 0x0161, NoSuchPaddingException -> 0x0132, NoSuchAlgorithmException -> 0x011c, InvalidAlgorithmParameterException -> 0x0106, InvalidKeyException -> 0x0148, all -> 0x00f0 }
            r0 = r22
            r6.<init>(r0)     // Catch:{ IOException -> 0x0161, NoSuchPaddingException -> 0x0132, NoSuchAlgorithmException -> 0x011c, InvalidAlgorithmParameterException -> 0x0106, InvalidKeyException -> 0x0148, all -> 0x00f0 }
            boolean r18 = r17.exists()     // Catch:{ IOException -> 0x0166, NoSuchPaddingException -> 0x0136, NoSuchAlgorithmException -> 0x0120, InvalidAlgorithmParameterException -> 0x010a, InvalidKeyException -> 0x014c, all -> 0x00f4 }
            if (r18 == 0) goto L_0x00a5
            boolean r18 = r17.isFile()     // Catch:{ IOException -> 0x0166, NoSuchPaddingException -> 0x0136, NoSuchAlgorithmException -> 0x0120, InvalidAlgorithmParameterException -> 0x010a, InvalidKeyException -> 0x014c, all -> 0x00f4 }
            if (r18 == 0) goto L_0x00a5
            java.io.File r18 = r6.getParentFile()     // Catch:{ IOException -> 0x0166, NoSuchPaddingException -> 0x0136, NoSuchAlgorithmException -> 0x0120, InvalidAlgorithmParameterException -> 0x010a, InvalidKeyException -> 0x014c, all -> 0x00f4 }
            boolean r18 = r18.exists()     // Catch:{ IOException -> 0x0166, NoSuchPaddingException -> 0x0136, NoSuchAlgorithmException -> 0x0120, InvalidAlgorithmParameterException -> 0x010a, InvalidKeyException -> 0x014c, all -> 0x00f4 }
            if (r18 != 0) goto L_0x0032
            java.io.File r18 = r6.getParentFile()     // Catch:{ IOException -> 0x0166, NoSuchPaddingException -> 0x0136, NoSuchAlgorithmException -> 0x0120, InvalidAlgorithmParameterException -> 0x010a, InvalidKeyException -> 0x014c, all -> 0x00f4 }
            r18.mkdirs()     // Catch:{ IOException -> 0x0166, NoSuchPaddingException -> 0x0136, NoSuchAlgorithmException -> 0x0120, InvalidAlgorithmParameterException -> 0x010a, InvalidKeyException -> 0x014c, all -> 0x00f4 }
        L_0x0032:
            r6.createNewFile()     // Catch:{ IOException -> 0x0166, NoSuchPaddingException -> 0x0136, NoSuchAlgorithmException -> 0x0120, InvalidAlgorithmParameterException -> 0x010a, InvalidKeyException -> 0x014c, all -> 0x00f4 }
            java.io.FileInputStream r9 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0166, NoSuchPaddingException -> 0x0136, NoSuchAlgorithmException -> 0x0120, InvalidAlgorithmParameterException -> 0x010a, InvalidKeyException -> 0x014c, all -> 0x00f4 }
            r0 = r17
            r9.<init>(r0)     // Catch:{ IOException -> 0x0166, NoSuchPaddingException -> 0x0136, NoSuchAlgorithmException -> 0x0120, InvalidAlgorithmParameterException -> 0x010a, InvalidKeyException -> 0x014c, all -> 0x00f4 }
            java.io.FileOutputStream r14 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x016c, NoSuchPaddingException -> 0x013b, NoSuchAlgorithmException -> 0x0125, InvalidAlgorithmParameterException -> 0x010f, InvalidKeyException -> 0x0151, all -> 0x00f9 }
            r14.<init>(r6)     // Catch:{ IOException -> 0x016c, NoSuchPaddingException -> 0x013b, NoSuchAlgorithmException -> 0x0125, InvalidAlgorithmParameterException -> 0x010f, InvalidKeyException -> 0x0151, all -> 0x00f9 }
            java.lang.String r18 = "AES/CBC/PKCS7Padding"
            javax.crypto.Cipher r3 = javax.crypto.Cipher.getInstance(r18)     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            r18 = 16
            r0 = r18
            byte[] r10 = new byte[r0]     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            r18 = 0
            r0 = r18
            java.util.Arrays.fill(r10, r0)     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            javax.crypto.spec.IvParameterSpec r11 = new javax.crypto.spec.IvParameterSpec     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            r11.<init>(r10)     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            javax.crypto.spec.SecretKeySpec r12 = new javax.crypto.spec.SecretKeySpec     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            byte[] r18 = dji.midware.encryption.util.AES256Encryption.getAESKeyFromString(r20)     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            java.lang.String r19 = "AES"
            r0 = r18
            r1 = r19
            r12.<init>(r0, r1)     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            r18 = 2
            r0 = r18
            r3.init(r0, r12, r11)     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            javax.crypto.CipherOutputStream r4 = new javax.crypto.CipherOutputStream     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            r4.<init>(r14, r3)     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            r18 = 1024(0x400, float:1.435E-42)
            r0 = r18
            byte[] r2 = new byte[r0]     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
        L_0x007c:
            int r15 = r9.read(r2)     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            if (r15 < 0) goto L_0x00a0
            r18 = 0
            r0 = r18
            r4.write(r2, r0, r15)     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            goto L_0x007c
        L_0x008a:
            r18 = move-exception
            r16 = r17
            r5 = r6
            r13 = r14
            r8 = r9
        L_0x0090:
            r7 = r18
        L_0x0092:
            r7.printStackTrace()     // Catch:{ all -> 0x00ca }
            if (r8 == 0) goto L_0x009a
            r8.close()     // Catch:{ IOException -> 0x00c0 }
        L_0x009a:
            if (r13 == 0) goto L_0x009f
            r13.close()     // Catch:{ IOException -> 0x00c5 }
        L_0x009f:
            return r5
        L_0x00a0:
            r4.close()     // Catch:{ IOException -> 0x008a, NoSuchPaddingException -> 0x0141, NoSuchAlgorithmException -> 0x012b, InvalidAlgorithmParameterException -> 0x0115, InvalidKeyException -> 0x0157, all -> 0x00ff }
            r13 = r14
            r8 = r9
        L_0x00a5:
            if (r8 == 0) goto L_0x00aa
            r8.close()     // Catch:{ IOException -> 0x00b3 }
        L_0x00aa:
            if (r13 == 0) goto L_0x00af
            r13.close()     // Catch:{ IOException -> 0x00b8 }
        L_0x00af:
            r16 = r17
            r5 = r6
            goto L_0x009f
        L_0x00b3:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x00aa
        L_0x00b8:
            r7 = move-exception
            r7.printStackTrace()
            r16 = r17
            r5 = r6
            goto L_0x009f
        L_0x00c0:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x009a
        L_0x00c5:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x009f
        L_0x00ca:
            r18 = move-exception
        L_0x00cb:
            if (r8 == 0) goto L_0x00d0
            r8.close()     // Catch:{ IOException -> 0x00d6 }
        L_0x00d0:
            if (r13 == 0) goto L_0x00d5
            r13.close()     // Catch:{ IOException -> 0x00db }
        L_0x00d5:
            throw r18
        L_0x00d6:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x00d0
        L_0x00db:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x00d5
        L_0x00e0:
            r18 = move-exception
        L_0x00e1:
            r7 = r18
            goto L_0x0092
        L_0x00e4:
            r18 = move-exception
        L_0x00e5:
            r7 = r18
            goto L_0x0092
        L_0x00e8:
            r18 = move-exception
        L_0x00e9:
            r7 = r18
            goto L_0x0092
        L_0x00ec:
            r18 = move-exception
        L_0x00ed:
            r7 = r18
            goto L_0x0092
        L_0x00f0:
            r18 = move-exception
            r16 = r17
            goto L_0x00cb
        L_0x00f4:
            r18 = move-exception
            r16 = r17
            r5 = r6
            goto L_0x00cb
        L_0x00f9:
            r18 = move-exception
            r16 = r17
            r5 = r6
            r8 = r9
            goto L_0x00cb
        L_0x00ff:
            r18 = move-exception
            r16 = r17
            r5 = r6
            r13 = r14
            r8 = r9
            goto L_0x00cb
        L_0x0106:
            r18 = move-exception
            r16 = r17
            goto L_0x00e9
        L_0x010a:
            r18 = move-exception
            r16 = r17
            r5 = r6
            goto L_0x00e9
        L_0x010f:
            r18 = move-exception
            r16 = r17
            r5 = r6
            r8 = r9
            goto L_0x00e9
        L_0x0115:
            r18 = move-exception
            r16 = r17
            r5 = r6
            r13 = r14
            r8 = r9
            goto L_0x00e9
        L_0x011c:
            r18 = move-exception
            r16 = r17
            goto L_0x00e5
        L_0x0120:
            r18 = move-exception
            r16 = r17
            r5 = r6
            goto L_0x00e5
        L_0x0125:
            r18 = move-exception
            r16 = r17
            r5 = r6
            r8 = r9
            goto L_0x00e5
        L_0x012b:
            r18 = move-exception
            r16 = r17
            r5 = r6
            r13 = r14
            r8 = r9
            goto L_0x00e5
        L_0x0132:
            r18 = move-exception
            r16 = r17
            goto L_0x00e1
        L_0x0136:
            r18 = move-exception
            r16 = r17
            r5 = r6
            goto L_0x00e1
        L_0x013b:
            r18 = move-exception
            r16 = r17
            r5 = r6
            r8 = r9
            goto L_0x00e1
        L_0x0141:
            r18 = move-exception
            r16 = r17
            r5 = r6
            r13 = r14
            r8 = r9
            goto L_0x00e1
        L_0x0148:
            r18 = move-exception
            r16 = r17
            goto L_0x00ed
        L_0x014c:
            r18 = move-exception
            r16 = r17
            r5 = r6
            goto L_0x00ed
        L_0x0151:
            r18 = move-exception
            r16 = r17
            r5 = r6
            r8 = r9
            goto L_0x00ed
        L_0x0157:
            r18 = move-exception
            r16 = r17
            r5 = r6
            r13 = r14
            r8 = r9
            goto L_0x00ed
        L_0x015e:
            r18 = move-exception
            goto L_0x0090
        L_0x0161:
            r18 = move-exception
            r16 = r17
            goto L_0x0090
        L_0x0166:
            r18 = move-exception
            r16 = r17
            r5 = r6
            goto L_0x0090
        L_0x016c:
            r18 = move-exception
            r16 = r17
            r5 = r6
            r8 = r9
            goto L_0x0090
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.sdk.AES.decryptFile(java.lang.String, java.lang.String, java.lang.String):java.io.File");
    }

    public static String encryptHmacSha256(String data, String key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "HmacSha256");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            byte[] res = mac.doFinal(data.getBytes());
            return String.format(Locale.US, "%0" + (res.length * 2) + "X", new BigInteger(1, res));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
