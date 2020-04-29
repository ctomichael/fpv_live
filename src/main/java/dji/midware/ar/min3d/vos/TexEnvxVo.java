package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class TexEnvxVo {
    public int param = 8448;
    public int pname = 8704;

    public TexEnvxVo() {
    }

    public TexEnvxVo(int $pname, int $param) {
        this.pname = $pname;
        this.param = $param;
    }

    public void setAll(int $pname, int $param) {
        this.pname = $pname;
        this.param = $param;
    }
}
