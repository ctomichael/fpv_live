package dji.thirdparty.sanselan;

public class ImageReadException extends SanselanException {
    static final long serialVersionUID = -1;

    public ImageReadException(String s) {
        super(s);
    }

    public ImageReadException(String s, Exception e) {
        super(s, e);
    }
}
