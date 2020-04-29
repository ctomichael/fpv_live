package com.drew.imaging.riff;

public class RiffReader {
    /* JADX WARNING: CFG modification limit reached, blocks count: 125 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void processRiff(@com.drew.lang.annotations.NotNull com.drew.lang.SequentialReader r10, @com.drew.lang.annotations.NotNull com.drew.imaging.riff.RiffHandler r11) throws com.drew.imaging.riff.RiffProcessingException, java.io.IOException {
        /*
            r9 = this;
            r8 = 4
            r6 = 0
            r10.setMotorolaByteOrder(r6)
            java.lang.String r2 = r10.getString(r8)
            java.lang.String r6 = "RIFF"
            boolean r6 = r2.equals(r6)
            if (r6 != 0) goto L_0x002c
            com.drew.imaging.riff.RiffProcessingException r6 = new com.drew.imaging.riff.RiffProcessingException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Invalid RIFF header: "
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r2)
            java.lang.String r7 = r7.toString()
            r6.<init>(r7)
            throw r6
        L_0x002c:
            int r3 = r10.getInt32()
            r5 = r3
            java.lang.String r4 = r10.getString(r8)
            int r5 = r5 + -4
            boolean r6 = r11.shouldAcceptRiffIdentifier(r4)
            if (r6 != 0) goto L_0x0056
        L_0x003d:
            return
        L_0x003e:
            boolean r6 = r11.shouldAcceptChunk(r0)
            if (r6 == 0) goto L_0x006f
            byte[] r6 = r10.getBytes(r1)
            r11.processChunk(r0, r6)
        L_0x004b:
            int r5 = r5 - r1
            int r6 = r1 % 2
            r7 = 1
            if (r6 != r7) goto L_0x0056
            r10.getInt8()
            int r5 = r5 + -1
        L_0x0056:
            if (r5 == 0) goto L_0x003d
            java.lang.String r0 = r10.getString(r8)
            int r1 = r10.getInt32()
            int r5 = r5 + -8
            if (r1 < 0) goto L_0x0066
            if (r5 >= r1) goto L_0x003e
        L_0x0066:
            com.drew.imaging.riff.RiffProcessingException r6 = new com.drew.imaging.riff.RiffProcessingException
            java.lang.String r7 = "Invalid RIFF chunk size"
            r6.<init>(r7)
            throw r6
        L_0x006f:
            long r6 = (long) r1
            r10.skip(r6)
            goto L_0x004b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.drew.imaging.riff.RiffReader.processRiff(com.drew.lang.SequentialReader, com.drew.imaging.riff.RiffHandler):void");
    }
}
