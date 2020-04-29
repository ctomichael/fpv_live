package dji.dbox.upgrade.p4.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.storage.StorageManager;
import dji.dbox.R;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.Dpad.DpadProductType;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.util.ContextUtil;
import dji.publics.DJIExecutor;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@EXClassNullAway
public class DJISdBigPackageScanHelper {
    private static final String BIG_PACKAGE_REGEX = "_v[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}_[0-9]{4}[0-9]{2}[0-9]{2}\\.tar";
    private static final String CLASS_STORAGE_VOLUME = "android.os.storage.StorageVolume";
    private static String DATA_ROOT = "/mList/media/0/";
    private static String FLASH_ROOT = (Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator);
    private static String METADATA_TEST_BIN = "/metadata/unstall_device_test.bin";
    private static final String METHOD_GET_PATH = "getPath";
    private static final String METHOD_GET_STATE = "getState";
    private static final String METHOD_GET_VOLUME_LIST = "getVolumeList";
    private static final String METHOD_IS_REMOVABLE = "isRemovable";
    private static String SDCARD_ROOT = "/mnt/external_sd/";
    private static String SDCARD_ROOT1 = "/mnt/external_sd1/";
    private static final String TAG = "BigPackage";
    private static String USB_ROOT = "/mnt/usb_storage/USB_DISK2/udisk0/";
    private static String USB_ROOT1 = "/mnt/usb_storage/USB_DISK2/udisk0(1)/";
    private Callback mCallback;
    private Pattern mPattern;
    private DJIUpDeviceType mProductId;
    /* access modifiers changed from: private */
    public List<String> mScanDirs = new ArrayList();
    /* access modifiers changed from: private */
    public String releaseNote;

    public interface Callback {
        void onScanResult(List<SdTarModel> list);
    }

    public static class SdTarModel {
        public String deviceId;
        public String product_version;
        public String releaseNote;
        public String tarPath;
    }

    public DJISdBigPackageScanHelper(DJIUpDeviceType productId) {
        this.mProductId = productId;
        this.mPattern = Pattern.compile(this.mProductId.getProductId() + BIG_PACKAGE_REGEX);
    }

    public boolean isSupportSdBigPackage() {
        return !new File(METADATA_TEST_BIN).exists() && DpadProductManager.getInstance().getProductType() == DpadProductType.PomatoSdr;
    }

    public void startScan(Callback callback) {
        this.mCallback = callback;
        if (this.mScanDirs.isEmpty()) {
            initScanDirs();
        }
    }

    private void initScanDirs() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            /* class dji.dbox.upgrade.p4.utils.DJISdBigPackageScanHelper.AnonymousClass1 */

            public void run() {
                ScanTask task;
                try {
                    Context context = ContextUtil.getContext();
                    String unused = DJISdBigPackageScanHelper.this.releaseNote = context.getResources().getString(R.string.release_note_from_sd);
                    task = new ScanTask((StorageManager) context.getSystemService("storage"));
                } catch (NullPointerException e) {
                    task = new ScanTask(null);
                }
                DJIExecutor.getExecutorFor(DJIExecutor.Purpose.IO).execute(task);
            }
        });
    }

    private void tryHardCodePaths() {
        if (this.mScanDirs.isEmpty()) {
            this.mScanDirs.add(DATA_ROOT);
            this.mScanDirs.add(FLASH_ROOT);
            this.mScanDirs.add(SDCARD_ROOT);
            this.mScanDirs.add(SDCARD_ROOT1);
            this.mScanDirs.add(USB_ROOT);
            this.mScanDirs.add(USB_ROOT1);
        }
    }

    /* access modifiers changed from: private */
    public void scanDirs() {
        File[] files;
        tryHardCodePaths();
        List<File> bigPackageList = new ArrayList<>();
        for (String path : this.mScanDirs) {
            File scanRoot = new File(path);
            if (scanRoot.exists() && (files = scanRoot.listFiles(new FileFilter() {
                /* class dji.dbox.upgrade.p4.utils.DJISdBigPackageScanHelper.AnonymousClass2 */

                public boolean accept(File file) {
                    if (!file.exists() || file.isDirectory()) {
                        return false;
                    }
                    return DJISdBigPackageScanHelper.this.isBigPackage(file.getName().toLowerCase());
                }
            })) != null && files.length > 0) {
                bigPackageList.addAll(Arrays.asList(files));
            }
        }
        List<SdTarModel> tarModels = fileToTarModel(bigPackageList);
        if (this.mCallback == null || tarModels == null || tarModels.size() <= 0) {
            DJIUpConstants.LOGD(TAG, "sd big package not exist=");
            return;
        }
        DJIUpConstants.LOGD(TAG, "sd big package exist size=" + tarModels.size());
        Collections.sort(tarModels, new Comparator<SdTarModel>() {
            /* class dji.dbox.upgrade.p4.utils.DJISdBigPackageScanHelper.AnonymousClass3 */

            public int compare(SdTarModel o1, SdTarModel o2) {
                return DJIUpgradeBaseUtils.compareFirVer(o1.product_version, o2.product_version);
            }
        });
        this.mCallback.onScanResult(tarModels);
    }

    private List<SdTarModel> fileToTarModel(List<File> tars) {
        List<SdTarModel> tarModels = new ArrayList<>();
        for (File tar : tars) {
            DJIUpConstants.LOGD(TAG, "tar find path=" + tar.getPath());
            SdTarModel model = new SdTarModel();
            model.tarPath = tar.getPath();
            model.releaseNote = this.releaseNote;
            model.deviceId = this.mProductId.getProductId();
            model.product_version = tar.getName().toLowerCase().split("_")[1].replace("v", "");
            tarModels.add(model);
        }
        return tarModels;
    }

    /* access modifiers changed from: private */
    public boolean isBigPackage(String name) {
        return this.mPattern.matcher(name).matches();
    }

    private class ScanTask implements Runnable {
        StorageManager storageManager;

        public ScanTask(StorageManager storageManager2) {
            this.storageManager = storageManager2;
        }

        public void run() {
            try {
                Class storageVolume = Class.forName(DJISdBigPackageScanHelper.CLASS_STORAGE_VOLUME);
                Method getVolumeList = this.storageManager.getClass().getMethod(DJISdBigPackageScanHelper.METHOD_GET_VOLUME_LIST, new Class[0]);
                Method getPath = storageVolume.getMethod(DJISdBigPackageScanHelper.METHOD_GET_PATH, new Class[0]);
                Method isRemovable = storageVolume.getMethod(DJISdBigPackageScanHelper.METHOD_IS_REMOVABLE, new Class[0]);
                Method getState = storageVolume.getMethod(DJISdBigPackageScanHelper.METHOD_GET_STATE, new Class[0]);
                Object result = getVolumeList.invoke(this.storageManager, new Object[0]);
                int length = Array.getLength(result);
                for (int i = 0; i < length; i++) {
                    Object storageVolumeElement = Array.get(result, i);
                    String path = (String) getPath.invoke(storageVolumeElement, new Object[0]);
                    String state = (String) getState.invoke(storageVolumeElement, new Object[0]);
                    boolean removable = ((Boolean) isRemovable.invoke(storageVolumeElement, new Object[0])).booleanValue();
                    DJIUpConstants.LOGD(DJISdBigPackageScanHelper.TAG, "ScanTask path=" + path + " state=" + state + " removable=" + removable);
                    if (removable && "mounted".equals(state)) {
                        DJISdBigPackageScanHelper.this.mScanDirs.add(path);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            DJISdBigPackageScanHelper.this.scanDirs();
        }
    }
}
