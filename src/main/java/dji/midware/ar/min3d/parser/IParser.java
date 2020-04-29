package dji.midware.ar.min3d.parser;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.animation.AnimationObject3d;
import dji.midware.ar.min3d.core.Object3dContainer;

@EXClassNullAway
public interface IParser {
    AnimationObject3d getParsedAnimationObject();

    Object3dContainer getParsedObject();

    void parse();
}
