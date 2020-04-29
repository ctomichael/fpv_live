package org.msgpack.core;

import java.nio.charset.CharacterCodingException;

public class MessageStringCodingException extends MessagePackException {
    public MessageStringCodingException(String str, CharacterCodingException characterCodingException) {
        super(str, characterCodingException);
    }

    public MessageStringCodingException(CharacterCodingException characterCodingException) {
        super(characterCodingException);
    }

    public CharacterCodingException getCause() {
        return (CharacterCodingException) super.getCause();
    }
}
