package dji.thirdparty.sanselan.common;

import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.common.mylzw.MyLZWCompressor;
import dji.thirdparty.sanselan.common.mylzw.MyLZWDecompressor;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Compression {
    public byte[] decompressLZW(byte[] compressed, int LZWMinimumCodeSize, int expectedSize, int byteOrder) throws IOException {
        return new MyLZWDecompressor(LZWMinimumCodeSize, byteOrder).decompress(new ByteArrayInputStream(compressed), expectedSize);
    }

    public byte[] decompressPackBits(byte[] compressed, int expectedSize, int byteOrder) throws ImageReadException, IOException {
        return new PackBits().decompress(compressed, expectedSize);
    }

    public byte[] compressLZW(byte[] src, int LZWMinimumCodeSize, int byteOrder, boolean earlyLimit) throws IOException {
        return new MyLZWCompressor(LZWMinimumCodeSize, byteOrder, earlyLimit).compress(src);
    }
}
