package dji.midware.ar.min3d.objectPrimitives;

import android.graphics.Bitmap;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Shared;
import dji.midware.ar.min3d.Utils;
import dji.midware.ar.min3d.core.Object3dContainer;
import dji.midware.ar.min3d.vos.Color4;

@EXClassNullAway
public class SkyBox extends Object3dContainer {
    private Color4 color;
    private Rectangle[] faces;
    private float halfSize;
    private int quality;
    private float size;

    public enum Face {
        North,
        East,
        South,
        West,
        Up,
        Down,
        All
    }

    public SkyBox(float size2, int quality2) {
        super(0, 0);
        this.size = size2;
        this.halfSize = 0.5f * size2;
        this.quality = quality2;
        build();
    }

    private void build() {
        this.color = new Color4();
        this.faces = new Rectangle[6];
        Rectangle north = new Rectangle(this.size, this.size, this.quality, this.quality, this.color);
        Rectangle east = new Rectangle(this.size, this.size, this.quality, this.quality, this.color);
        Rectangle south = new Rectangle(this.size, this.size, this.quality, this.quality, this.color);
        Rectangle west = new Rectangle(this.size, this.size, this.quality, this.quality, this.color);
        Rectangle up = new Rectangle(this.size, this.size, this.quality, this.quality, this.color);
        Rectangle down = new Rectangle(this.size, this.size, this.quality, this.quality, this.color);
        north.position().z = this.halfSize;
        north.lightingEnabled(false);
        east.rotation().y = -90.0f;
        east.position().x = this.halfSize;
        east.doubleSidedEnabled(true);
        east.lightingEnabled(false);
        south.rotation().y = 180.0f;
        south.position().z = -this.halfSize;
        south.lightingEnabled(false);
        west.rotation().y = 90.0f;
        west.position().x = -this.halfSize;
        west.doubleSidedEnabled(true);
        west.lightingEnabled(false);
        up.rotation().x = 90.0f;
        up.position().y = this.halfSize;
        up.doubleSidedEnabled(true);
        up.lightingEnabled(false);
        down.rotation().x = -90.0f;
        down.position().y = -this.halfSize;
        down.doubleSidedEnabled(true);
        down.lightingEnabled(false);
        this.faces[Face.North.ordinal()] = north;
        this.faces[Face.East.ordinal()] = east;
        this.faces[Face.South.ordinal()] = south;
        this.faces[Face.West.ordinal()] = west;
        this.faces[Face.Up.ordinal()] = up;
        this.faces[Face.Down.ordinal()] = down;
        addChild(north);
        addChild(east);
        addChild(south);
        addChild(west);
        addChild(up);
        addChild(down);
    }

    public void addTexture(Face face, int resourceId, String id) {
        Bitmap bitmap = Utils.makeBitmapFromResourceId(resourceId);
        Shared.textureManager().addTextureId(bitmap, id, true);
        bitmap.recycle();
        addTexture(face, bitmap, id);
    }

    public void addTexture(Face face, Bitmap bitmap, String id) {
        if (face == Face.All) {
            for (int i = 0; i < 6; i++) {
                this.faces[i].textures().addById(id);
            }
            return;
        }
        this.faces[face.ordinal()].textures().addById(id);
    }
}
