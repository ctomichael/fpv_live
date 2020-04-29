package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;

@EXClassNullAway
public class TextureVo {
    public float offsetU = 0.0f;
    public float offsetV = 0.0f;
    public boolean repeatU = true;
    public boolean repeatV = true;
    public ArrayList<TexEnvxVo> textureEnvs;
    public String textureId;

    public TextureVo(String $textureId, ArrayList<TexEnvxVo> $textureEnvVo) {
        this.textureId = $textureId;
        this.textureEnvs = $textureEnvVo;
    }

    public TextureVo(String $textureId) {
        this.textureId = $textureId;
        this.textureEnvs = new ArrayList<>();
        this.textureEnvs.add(new TexEnvxVo());
    }
}
