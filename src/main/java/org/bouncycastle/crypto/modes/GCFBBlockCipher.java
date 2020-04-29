package org.bouncycastle.crypto.modes;

import android.support.v4.media.session.PlaybackStateCompat;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.ParametersWithSBox;
import org.msgpack.core.MessagePack;

public class GCFBBlockCipher extends StreamBlockCipher {
    private static final byte[] C = {105, 0, 114, 34, 100, MessagePack.Code.EXT32, 4, 35, -115, 58, MessagePack.Code.STR32, -106, 70, -23, 42, MessagePack.Code.BIN8, 24, -2, -84, -108, 0, -19, 7, 18, MessagePack.Code.NIL, -122, MessagePack.Code.ARRAY16, MessagePack.Code.FALSE, -17, 76, -87, 43};
    private final CFBBlockCipher cfbEngine;
    private long counter = 0;
    private boolean forEncryption;
    private KeyParameter key;

    public GCFBBlockCipher(BlockCipher blockCipher) {
        super(blockCipher);
        this.cfbEngine = new CFBBlockCipher(blockCipher, blockCipher.getBlockSize() * 8);
    }

    /* access modifiers changed from: protected */
    public byte calculateByte(byte b) {
        if (this.counter > 0 && this.counter % PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID == 0) {
            BlockCipher underlyingCipher = this.cfbEngine.getUnderlyingCipher();
            underlyingCipher.init(false, this.key);
            byte[] bArr = new byte[32];
            underlyingCipher.processBlock(C, 0, bArr, 0);
            underlyingCipher.processBlock(C, 8, bArr, 8);
            underlyingCipher.processBlock(C, 16, bArr, 16);
            underlyingCipher.processBlock(C, 24, bArr, 24);
            this.key = new KeyParameter(bArr);
            underlyingCipher.init(true, this.key);
            byte[] currentIV = this.cfbEngine.getCurrentIV();
            underlyingCipher.processBlock(currentIV, 0, currentIV, 0);
            this.cfbEngine.init(this.forEncryption, new ParametersWithIV(this.key, currentIV));
        }
        this.counter++;
        return this.cfbEngine.calculateByte(b);
    }

    public String getAlgorithmName() {
        String algorithmName = this.cfbEngine.getAlgorithmName();
        return algorithmName.substring(0, algorithmName.indexOf(47)) + "/G" + algorithmName.substring(algorithmName.indexOf(47) + 1);
    }

    public int getBlockSize() {
        return this.cfbEngine.getBlockSize();
    }

    public void init(boolean z, CipherParameters cipherParameters) throws IllegalArgumentException {
        this.counter = 0;
        this.cfbEngine.init(z, cipherParameters);
        this.forEncryption = z;
        CipherParameters parameters = cipherParameters instanceof ParametersWithIV ? ((ParametersWithIV) cipherParameters).getParameters() : cipherParameters;
        if (parameters instanceof ParametersWithRandom) {
            parameters = ((ParametersWithRandom) parameters).getParameters();
        }
        if (parameters instanceof ParametersWithSBox) {
            parameters = ((ParametersWithSBox) parameters).getParameters();
        }
        this.key = (KeyParameter) parameters;
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) throws DataLengthException, IllegalStateException {
        processBytes(bArr, i, this.cfbEngine.getBlockSize(), bArr2, i2);
        return this.cfbEngine.getBlockSize();
    }

    public void reset() {
        this.counter = 0;
        this.cfbEngine.reset();
    }
}
