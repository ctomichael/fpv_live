package dji.thirdparty.sanselan;

public class ImageWriteException extends SanselanException {
    static final long serialVersionUID = -1;

    public ImageWriteException(String s) {
        super(s);
    }

    public ImageWriteException(String s, Exception e) {
        super(s, e);
    }
}
