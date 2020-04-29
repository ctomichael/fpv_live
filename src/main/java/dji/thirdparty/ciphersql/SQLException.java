package dji.thirdparty.ciphersql;

public class SQLException extends RuntimeException {
    public SQLException() {
    }

    public SQLException(String error) {
        super(error);
    }
}
