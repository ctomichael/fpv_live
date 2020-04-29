package dji.pilot.publics.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Keep
@EXClassNullAway
public class DJIProductVerModel {
    public ArrayList<DJIVerModel> all;
    public ArrayList<DJIVerModel> battery;
    public ArrayList<DJIVerModel> camera;
    public ArrayList<DJIVerModel> ignoreAll;
    public ArrayList<DJIVerModel> ignorebattery;
    public ArrayList<DJIVerModel> ignorecamera;
    public ArrayList<DJIVerModel> ignoremc;
    public ArrayList<DJIVerModel> ignorerc;
    public ArrayList<DJIVerModel> mc;
    public ArrayList<DJIVerModel> rc;

    @Keep
    public static class DJIVerModel {
        public String code;
        public int flag = 0;
        public boolean isNeedUp = false;
        public boolean isSeted = false;
        public String name;
        public int position = 0;
        public String version = "";

        public DJIVerModel code(String code2) {
            this.code = code2;
            return this;
        }

        public DJIVerModel name(String name2) {
            this.name = name2;
            return this;
        }

        public DJIVerModel copy(DJIVerModel verModel) {
            this.code = verModel.code;
            this.name = verModel.name;
            return this;
        }

        public void reset() {
            this.isSeted = false;
            this.isNeedUp = false;
            this.version = "";
            this.position = 0;
            this.flag = 0;
        }

        public void setFlag(int mflag) {
            this.flag = Math.max(this.flag, mflag);
        }
    }

    public ArrayList<DJIVerModel> getAll() {
        if (this.all == null) {
            this.all = new ArrayList<>();
            if (this.camera != null) {
                this.all.addAll(this.camera);
            }
            if (this.battery != null) {
                this.all.addAll(this.battery);
            }
            if (this.rc != null) {
                this.all.addAll(this.rc);
            }
            if (this.mc != null) {
                this.all.addAll(this.mc);
            }
        }
        return this.all;
    }

    public ArrayList<DJIVerModel> getIgnoreList() {
        if (this.ignoreAll == null) {
            this.ignoreAll = new ArrayList<>();
        }
        this.ignoreAll.clear();
        addVerModel(this.ignoreAll, this.ignorecamera);
        addVerModel(this.ignoreAll, this.ignorebattery);
        addVerModel(this.ignoreAll, this.ignorerc);
        addVerModel(this.ignoreAll, this.ignoremc);
        return this.ignoreAll;
    }

    private void addVerModel(ArrayList<DJIVerModel> dst, ArrayList<DJIVerModel> src) {
        if (src != null && !src.isEmpty()) {
            dst.addAll(src);
        }
    }

    private void resetVerModel(ArrayList<DJIVerModel> models) {
        if (models != null && !models.isEmpty()) {
            Iterator<DJIVerModel> it2 = models.iterator();
            while (it2.hasNext()) {
                it2.next().reset();
            }
        }
    }

    public void reset() {
        resetVerModel(this.camera);
        resetVerModel(this.battery);
        resetVerModel(this.rc);
        resetVerModel(this.mc);
        resetVerModel(this.ignorecamera);
        resetVerModel(this.ignorebattery);
        resetVerModel(this.ignorerc);
        resetVerModel(this.ignoremc);
        this.all = null;
    }

    public DJIProductVerModel addCameraVerModels(DJIVerModel... verModels) {
        if (this.camera == null) {
            this.camera = new ArrayList<>();
        }
        for (DJIVerModel verModel : verModels) {
            this.camera.add(verModel);
        }
        return this;
    }

    public DJIProductVerModel addIgnoreCameraVerModels(DJIVerModel... verModels) {
        if (this.ignorecamera == null) {
            this.ignorecamera = new ArrayList<>();
        }
        for (DJIVerModel verModel : verModels) {
            this.ignorecamera.add(verModel);
        }
        return this;
    }

    public DJIProductVerModel addBatteryVerModels(DJIVerModel... verModels) {
        if (this.battery == null) {
            this.battery = new ArrayList<>();
        }
        for (DJIVerModel verModel : verModels) {
            this.battery.add(verModel);
        }
        return this;
    }

    public DJIProductVerModel addIgnoreBatteryVerModels(DJIVerModel... verModels) {
        if (this.ignorebattery == null) {
            this.ignorebattery = new ArrayList<>();
        }
        for (DJIVerModel verModel : verModels) {
            this.ignorebattery.add(verModel);
        }
        return this;
    }

    public DJIProductVerModel addRcVerModels(DJIVerModel... verModels) {
        if (this.rc == null) {
            this.rc = new ArrayList<>();
        }
        for (DJIVerModel verModel : verModels) {
            this.rc.add(verModel);
        }
        return this;
    }

    public DJIProductVerModel addIgnoreRcVerModels(DJIVerModel... verModels) {
        if (this.ignorerc == null) {
            this.ignorerc = new ArrayList<>();
        }
        for (DJIVerModel verModel : verModels) {
            this.ignorerc.add(verModel);
        }
        return this;
    }

    public DJIProductVerModel addMcVerModels(DJIVerModel... verModels) {
        if (this.mc == null) {
            this.mc = new ArrayList<>();
        }
        for (DJIVerModel verModel : verModels) {
            this.mc.add(verModel);
        }
        return this;
    }

    public DJIProductVerModel addIgnoreMcVerModels(DJIVerModel... verModels) {
        if (this.ignoremc == null) {
            this.ignoremc = new ArrayList<>();
        }
        for (DJIVerModel verModel : verModels) {
            this.ignoremc.add(verModel);
        }
        return this;
    }

    public DJIProductVerModel copy(DJIProductVerModel verModel) {
        if (verModel != null) {
            if (isNotEmpty(verModel.camera)) {
                if (this.camera == null) {
                    this.camera = new ArrayList<>();
                }
                deepCopyVerList(this.camera, verModel.camera);
            }
            if (isNotEmpty(verModel.battery)) {
                if (this.battery == null) {
                    this.battery = new ArrayList<>();
                }
                deepCopyVerList(this.battery, verModel.battery);
            }
            if (isNotEmpty(verModel.rc)) {
                if (this.rc == null) {
                    this.rc = new ArrayList<>();
                }
                deepCopyVerList(this.rc, verModel.rc);
            }
            if (isNotEmpty(verModel.mc)) {
                if (this.mc == null) {
                    this.mc = new ArrayList<>();
                }
                deepCopyVerList(this.mc, verModel.mc);
            }
            if (isNotEmpty(verModel.ignorecamera)) {
                if (this.ignorecamera == null) {
                    this.ignorecamera = new ArrayList<>();
                }
                deepCopyVerList(this.ignorecamera, verModel.ignorecamera);
            }
            if (isNotEmpty(verModel.ignorebattery)) {
                if (this.ignorebattery == null) {
                    this.ignorebattery = new ArrayList<>();
                }
                deepCopyVerList(this.ignorebattery, verModel.ignorebattery);
            }
            if (isNotEmpty(verModel.ignorerc)) {
                if (this.ignorerc == null) {
                    this.ignorerc = new ArrayList<>();
                }
                deepCopyVerList(this.ignorerc, verModel.ignorerc);
            }
            if (isNotEmpty(verModel.ignoremc)) {
                if (this.ignoremc == null) {
                    this.ignoremc = new ArrayList<>();
                }
                deepCopyVerList(this.ignoremc, verModel.ignoremc);
            }
        }
        return this;
    }

    private boolean isNotEmpty(List<DJIVerModel> list) {
        return list != null && !list.isEmpty();
    }

    private void deepCopyVerList(List<DJIVerModel> dst, List<DJIVerModel> src) {
        for (DJIVerModel model : src) {
            dst.add(new DJIVerModel().copy(model));
        }
    }
}
