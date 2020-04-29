package com.google.protobuf;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.MessageLite;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public abstract class AbstractParser<MessageType extends MessageLite> implements Parser<MessageType> {
    private static final ExtensionRegistryLite EMPTY_REGISTRY = ExtensionRegistryLite.getEmptyRegistry();

    private UninitializedMessageException newUninitializedMessageException(MessageType message) {
        if (message instanceof AbstractMessageLite) {
            return ((AbstractMessageLite) message).newUninitializedMessageException();
        }
        return new UninitializedMessageException((MessageLite) message);
    }

    private MessageType checkMessageInitialized(MessageType message) throws InvalidProtocolBufferException {
        if (message == null || message.isInitialized()) {
            return message;
        }
        throw newUninitializedMessageException(message).asInvalidProtocolBufferException().setUnfinishedMessage(message);
    }

    public MessageType parsePartialFrom(CodedInputStream input) throws InvalidProtocolBufferException {
        return (MessageLite) parsePartialFrom(input, EMPTY_REGISTRY);
    }

    public MessageType parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized((MessageLite) parsePartialFrom(input, extensionRegistry));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parseFrom(CodedInputStream input) throws InvalidProtocolBufferException {
        return parseFrom(input, EMPTY_REGISTRY);
    }

    public MessageType parsePartialFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        MessageType message;
        try {
            CodedInputStream input = data.newCodedInput();
            message = (MessageLite) parsePartialFrom(input, extensionRegistry);
            input.checkLastTagWas(0);
            return message;
        } catch (InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(message);
        } catch (InvalidProtocolBufferException e2) {
            throw e2;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parsePartialFrom(ByteString data) throws InvalidProtocolBufferException {
        return parsePartialFrom(data, EMPTY_REGISTRY);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized(parsePartialFrom(data, extensionRegistry));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return parseFrom(data, EMPTY_REGISTRY);
    }

    public MessageType parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        MessageType message;
        try {
            CodedInputStream input = CodedInputStream.newInstance(data);
            message = (MessageLite) parsePartialFrom(input, extensionRegistry);
            input.checkLastTagWas(0);
            return checkMessageInitialized(message);
        } catch (InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(message);
        } catch (InvalidProtocolBufferException e2) {
            throw e2;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return parseFrom(data, EMPTY_REGISTRY);
    }

    public MessageType parsePartialFrom(byte[] data, int off, int len, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        MessageType message;
        try {
            CodedInputStream input = CodedInputStream.newInstance(data, off, len);
            message = (MessageLite) parsePartialFrom(input, extensionRegistry);
            input.checkLastTagWas(0);
            return message;
        } catch (InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(message);
        } catch (InvalidProtocolBufferException e2) {
            throw e2;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [byte[], int, int, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parsePartialFrom(byte[] data, int off, int len) throws InvalidProtocolBufferException {
        return parsePartialFrom(data, off, len, EMPTY_REGISTRY);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [byte[], int, int, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parsePartialFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return parsePartialFrom(data, 0, data.length, extensionRegistry);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [byte[], int, int, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parsePartialFrom(byte[] data) throws InvalidProtocolBufferException {
        return parsePartialFrom(data, 0, data.length, EMPTY_REGISTRY);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [byte[], int, int, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parseFrom(byte[] data, int off, int len, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized(parsePartialFrom(data, off, len, extensionRegistry));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parseFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [byte[], int, int, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parseFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parseFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parseFrom(byte[] data, int off, int len) throws InvalidProtocolBufferException {
        return parseFrom(data, off, len, EMPTY_REGISTRY);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parseFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [byte[], int, int, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parseFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parseFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(byte[], int, int, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return parseFrom(data, 0, data.length, extensionRegistry);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [byte[], com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return parseFrom(data, EMPTY_REGISTRY);
    }

    public MessageType parsePartialFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        CodedInputStream codedInput = CodedInputStream.newInstance(input);
        MessageType message = (MessageLite) parsePartialFrom(codedInput, extensionRegistry);
        try {
            codedInput.checkLastTagWas(0);
            return message;
        } catch (InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(message);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [java.io.InputStream, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parsePartialFrom(InputStream input) throws InvalidProtocolBufferException {
        return parsePartialFrom(input, EMPTY_REGISTRY);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [java.io.InputStream, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized(parsePartialFrom(input, extensionRegistry));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [java.io.InputStream, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parseFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(java.nio.ByteBuffer, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parseFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parseFrom(InputStream input) throws InvalidProtocolBufferException {
        return parseFrom(input, EMPTY_REGISTRY);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [java.io.InputStream, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.AbstractParser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parsePartialFrom(com.google.protobuf.ByteString, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.Parser.parsePartialFrom(byte[], com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parsePartialDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        try {
            int firstByte = input.read();
            if (firstByte == -1) {
                return null;
            }
            return parsePartialFrom(new AbstractMessageLite.Builder.LimitedInputStream(input, CodedInputStream.readRawVarint32(firstByte, input)), extensionRegistry);
        } catch (IOException e) {
            throw new InvalidProtocolBufferException(e);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parsePartialDelimitedFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [java.io.InputStream, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parsePartialDelimitedFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parsePartialDelimitedFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialDelimitedFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parsePartialDelimitedFrom(InputStream input) throws InvalidProtocolBufferException {
        return parsePartialDelimitedFrom(input, EMPTY_REGISTRY);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parsePartialDelimitedFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [java.io.InputStream, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parsePartialDelimitedFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parsePartialDelimitedFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parsePartialDelimitedFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized(parsePartialDelimitedFrom(input, extensionRegistry));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.AbstractParser.parseDelimitedFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
     arg types: [java.io.InputStream, com.google.protobuf.ExtensionRegistryLite]
     candidates:
      com.google.protobuf.AbstractParser.parseDelimitedFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):java.lang.Object
      com.google.protobuf.Parser.parseDelimitedFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType
      com.google.protobuf.AbstractParser.parseDelimitedFrom(java.io.InputStream, com.google.protobuf.ExtensionRegistryLite):MessageType */
    public MessageType parseDelimitedFrom(InputStream input) throws InvalidProtocolBufferException {
        return parseDelimitedFrom(input, EMPTY_REGISTRY);
    }
}
