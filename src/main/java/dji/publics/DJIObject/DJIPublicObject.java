package dji.publics.DJIObject;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIPublicObject {

    public enum CameraSnGetted {
        SUCCESS,
        FAIL;
        
        public String sn = "";
    }

    public enum RCSnGetted {
        SUCCESS,
        FAIL;
        
        public String sn = "";
    }

    public enum FlycSnGetted {
        SUCCESS,
        FAIL;
        
        public String sn = "";
    }
}
