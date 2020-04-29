package dji.midware.media.player;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;

@EXClassNullAway
public class DJIMediaPlayerFactory {
    public static DJIMediaPlayer buildMediaPlayer(ProductType productType) {
        return buildMediaPlayer(productType, 0);
    }

    public static DJIMediaPlayer buildMediaPlayer(ProductType productType, int protocolIdOfCamera) {
        DJIMediaPlayer mediaPlayer;
        DJIMediaPlayer mediaPlayer2 = null;
        switch (DataCameraGetPushStateInfo.getInstance().getCameraType(protocolIdOfCamera)) {
            case DJICameraTypeTau336:
            case DJICameraTypeTau640:
            case DJICameraTypeGD600:
            case DJICameraTypeFC1705:
                mediaPlayer2 = new DJIMediaPlayerLitchis(protocolIdOfCamera);
                break;
            case DJICameraTypeFC6510:
            case DJICameraTypeFC6520:
            case DJICameraTypeFC6540:
                mediaPlayer2 = new DJIMediaPlayerH1(protocolIdOfCamera);
                break;
        }
        if (mediaPlayer2 != null) {
            return mediaPlayer2;
        }
        switch (productType) {
            case Orange:
            case litchiX:
            case Longan:
            case N1:
            case BigBanana:
            case OrangeRAW:
            case OrangeCV600:
                mediaPlayer = new DJIMediaPlayerLitchix();
                break;
            case litchiC:
            case litchiS:
            case P34K:
                mediaPlayer = new DJIMediaPlayerLitchis(new int[0]);
                break;
            case Tomato:
                mediaPlayer = new DJIMediaPlayerLitchix();
                break;
            case Pomato:
            case PomatoSDR:
            case Orange2:
            case M200:
            case M210:
            case M210RTK:
            case PM420:
            case PM420PRO:
            case PM420PRO_RTK:
            case Potato:
            case PomatoRTK:
                mediaPlayer = new DJIMediaPlayerH1(new int[0]);
                break;
            default:
                mediaPlayer = new DJIMediaPlayerLitchix();
                break;
        }
        return mediaPlayer;
    }
}
