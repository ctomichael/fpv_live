package dji.log;

public interface IEncryption {
    String decrypt(String str) throws Exception;

    String encrypt(String str) throws Exception;
}
