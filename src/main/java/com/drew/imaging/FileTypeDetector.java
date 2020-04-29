package com.drew.imaging;

import com.drew.lang.ByteTrie;
import com.drew.lang.annotations.NotNull;
import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import java.io.BufferedInputStream;
import java.io.IOException;
import org.msgpack.core.MessagePack;

public class FileTypeDetector {
    private static final ByteTrie<FileType> _root = new ByteTrie<>();

    static {
        _root.setDefaultValue(FileType.Unknown);
        _root.addPath(FileType.Jpeg, new byte[]{-1, MessagePack.Code.FIXEXT16});
        _root.addPath(FileType.Tiff, "II".getBytes(), new byte[]{42, 0});
        _root.addPath(FileType.Tiff, "MM".getBytes(), new byte[]{0, 42});
        _root.addPath(FileType.Psd, "8BPS".getBytes());
        _root.addPath(FileType.Png, new byte[]{-119, 80, 78, 71, Draft_75.CR, 10, 26, 10, 0, 0, 0, Draft_75.CR, 73, 72, 68, 82});
        _root.addPath(FileType.Bmp, "BM".getBytes());
        _root.addPath(FileType.Gif, "GIF87a".getBytes());
        _root.addPath(FileType.Gif, "GIF89a".getBytes());
        _root.addPath(FileType.Ico, new byte[]{0, 0, 1, 0});
        _root.addPath(FileType.Pcx, new byte[]{10, 0, 1});
        _root.addPath(FileType.Pcx, new byte[]{10, 2, 1});
        _root.addPath(FileType.Pcx, new byte[]{10, 3, 1});
        _root.addPath(FileType.Pcx, new byte[]{10, 5, 1});
        _root.addPath(FileType.Riff, "RIFF".getBytes());
        _root.addPath(FileType.Arw, "II".getBytes(), new byte[]{42, 0, 8, 0});
        _root.addPath(FileType.Crw, "II".getBytes(), new byte[]{26, 0, 0, 0}, "HEAPCCDR".getBytes());
        _root.addPath(FileType.Cr2, "II".getBytes(), new byte[]{42, 0, Tnaf.POW_2_WIDTH, 0, 0, 0, 67, 82});
        _root.addPath(FileType.Nef, "MM".getBytes(), new byte[]{0, 42, 0, 0, 0, Byte.MIN_VALUE, 0});
        _root.addPath(FileType.Orf, "IIRO".getBytes(), new byte[]{8, 0});
        _root.addPath(FileType.Orf, "MMOR".getBytes(), new byte[]{0, 0});
        _root.addPath(FileType.Orf, "IIRS".getBytes(), new byte[]{8, 0});
        _root.addPath(FileType.Raf, "FUJIFILMCCD-RAW".getBytes());
        _root.addPath(FileType.Rw2, "II".getBytes(), new byte[]{85, 0});
    }

    private FileTypeDetector() throws Exception {
        throw new Exception("Not intended for instantiation");
    }

    @NotNull
    public static FileType detectFileType(@NotNull BufferedInputStream inputStream) throws IOException {
        if (!inputStream.markSupported()) {
            throw new IOException("Stream must support mark/reset");
        }
        int maxByteCount = _root.getMaxDepth();
        inputStream.mark(maxByteCount);
        byte[] bytes = new byte[maxByteCount];
        if (inputStream.read(bytes) == -1) {
            throw new IOException("Stream ended before file's magic number could be determined.");
        }
        inputStream.reset();
        return _root.find(bytes);
    }
}
