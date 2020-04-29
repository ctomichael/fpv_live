package com.google.protobuf;

import java.nio.ByteBuffer;

abstract class AllocatedBuffer {
    public abstract byte[] array();

    public abstract int arrayOffset();

    public abstract boolean hasArray();

    public abstract boolean hasNioBuffer();

    public abstract int limit();

    public abstract ByteBuffer nioBuffer();

    public abstract int position();

    public abstract AllocatedBuffer position(int i);

    public abstract int remaining();

    AllocatedBuffer() {
    }

    public static AllocatedBuffer wrap(byte[] bytes) {
        return wrapNoCheck(bytes, 0, bytes.length);
    }

    public static AllocatedBuffer wrap(byte[] bytes, int offset, int length) {
        if (offset >= 0 && length >= 0 && offset + length <= bytes.length) {
            return wrapNoCheck(bytes, offset, length);
        }
        throw new IndexOutOfBoundsException(String.format("bytes.length=%d, offset=%d, length=%d", Integer.valueOf(bytes.length), Integer.valueOf(offset), Integer.valueOf(length)));
    }

    public static AllocatedBuffer wrap(final ByteBuffer buffer) {
        Internal.checkNotNull(buffer, "buffer");
        return new AllocatedBuffer() {
            /* class com.google.protobuf.AllocatedBuffer.AnonymousClass1 */

            public boolean hasNioBuffer() {
                return true;
            }

            public ByteBuffer nioBuffer() {
                return buffer;
            }

            public boolean hasArray() {
                return buffer.hasArray();
            }

            public byte[] array() {
                return buffer.array();
            }

            public int arrayOffset() {
                return buffer.arrayOffset();
            }

            public int position() {
                return buffer.position();
            }

            public AllocatedBuffer position(int position) {
                buffer.position(position);
                return this;
            }

            public int limit() {
                return buffer.limit();
            }

            public int remaining() {
                return buffer.remaining();
            }
        };
    }

    private static AllocatedBuffer wrapNoCheck(final byte[] bytes, final int offset, final int length) {
        return new AllocatedBuffer() {
            /* class com.google.protobuf.AllocatedBuffer.AnonymousClass2 */
            private int position;

            public boolean hasNioBuffer() {
                return false;
            }

            public ByteBuffer nioBuffer() {
                throw new UnsupportedOperationException();
            }

            public boolean hasArray() {
                return true;
            }

            public byte[] array() {
                return bytes;
            }

            public int arrayOffset() {
                return offset;
            }

            public int position() {
                return this.position;
            }

            public AllocatedBuffer position(int position2) {
                if (position2 < 0 || position2 > length) {
                    throw new IllegalArgumentException("Invalid position: " + position2);
                }
                this.position = position2;
                return this;
            }

            public int limit() {
                return length;
            }

            public int remaining() {
                return length - this.position;
            }
        };
    }
}
