package com.drew.imaging.png;

import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PngChunkReader {
    private static final byte[] PNG_SIGNATURE_BYTES = {-119, 80, 78, 71, Draft_75.CR, 10, 26, 10};

    public Iterable<PngChunk> extract(@NotNull SequentialReader reader, @Nullable Set<PngChunkType> desiredChunkTypes) throws PngProcessingException, IOException {
        reader.setMotorolaByteOrder(true);
        if (!Arrays.equals(PNG_SIGNATURE_BYTES, reader.getBytes(PNG_SIGNATURE_BYTES.length))) {
            throw new PngProcessingException("PNG signature mismatch");
        }
        boolean seenImageHeader = false;
        boolean seenImageTrailer = false;
        List<PngChunk> chunks = new ArrayList<>();
        Set<PngChunkType> seenChunkTypes = new HashSet<>();
        while (!seenImageTrailer) {
            int chunkDataLength = reader.getInt32();
            PngChunkType chunkType = new PngChunkType(reader.getBytes(4));
            boolean willStoreChunk = desiredChunkTypes == null || desiredChunkTypes.contains(chunkType);
            byte[] chunkData = reader.getBytes(chunkDataLength);
            reader.skip(4);
            if (!willStoreChunk || !seenChunkTypes.contains(chunkType) || chunkType.areMultipleAllowed()) {
                if (chunkType.equals(PngChunkType.IHDR)) {
                    seenImageHeader = true;
                } else if (!seenImageHeader) {
                    throw new PngProcessingException(String.format("First chunk should be '%s', but '%s' was observed", PngChunkType.IHDR, chunkType));
                }
                if (chunkType.equals(PngChunkType.IEND)) {
                    seenImageTrailer = true;
                }
                if (willStoreChunk) {
                    chunks.add(new PngChunk(chunkType, chunkData));
                }
                seenChunkTypes.add(chunkType);
            } else {
                throw new PngProcessingException(String.format("Observed multiple instances of PNG chunk '%s', for which multiples are not allowed", chunkType));
            }
        }
        return chunks;
    }
}
