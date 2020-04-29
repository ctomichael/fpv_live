package dji.midware.parser.plugins;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;

@EXClassNullAway
public class DJIPluginRingBufferParser {
    private String TAG = getClass().getSimpleName();
    protected byte[] buffer;
    protected DJIRingBufferModel bufferModel;
    private int byteOffset = 0;
    private int expendSize = 0;
    private final float factor = 0.125f;
    private final boolean isDebug = false;
    private boolean isGettedHeader = false;
    protected DJIRingBufferParserListener listener;
    private int myHeaderIndex = 0;
    private String name = "default";

    public interface DJIRingBufferParserListener {
        void onGetBody(byte[] bArr, int i, int i2);

        int parseSecondHeader(byte[] bArr, int i, int i2);
    }

    public DJIPluginRingBufferParser(int bufferSize, DJIRingBufferModel bufferModel2, DJIRingBufferParserListener listener2) {
        this.bufferModel = bufferModel2;
        this.buffer = new byte[bufferSize];
        this.listener = listener2;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getName() {
        return this.name;
    }

    public void parse(byte[] buffer2, int offset, int count) {
        if (count > this.buffer.length - this.byteOffset) {
            tryToExpandCapacity(count);
        }
        System.arraycopy(buffer2, offset, this.buffer, this.byteOffset, count);
        this.byteOffset += count;
        findPack();
    }

    private void tryToExpandCapacity(int length) {
        int newCapacity = newCapacity(this.buffer.length, length);
        DJILogHelper.getInstance().LOGD(this.TAG, "Try to expand capacity:" + (newCapacity - this.buffer.length), false, true);
        byte[] newArray = new byte[newCapacity];
        System.arraycopy(this.buffer, 0, newArray, 0, this.buffer.length);
        this.buffer = newArray;
    }

    private int newCapacity(int originLength, int length) {
        int size = 0;
        do {
            size += (int) (0.125f * ((float) originLength));
        } while (size <= length);
        return size + originLength;
    }

    private void resetBuffer() {
        if (this.expendSize > 0) {
            this.byteOffset -= this.expendSize;
            if (this.byteOffset > 0) {
                System.arraycopy(this.buffer, this.expendSize, this.buffer, 0, this.byteOffset);
            } else {
                this.byteOffset = 0;
            }
            this.expendSize = 0;
        }
    }

    private void findPack() {
        while (true) {
            resetBuffer();
            if (!this.isGettedHeader) {
                int i = 0;
                while (true) {
                    if (i >= this.byteOffset) {
                        break;
                    }
                    if (this.buffer[i] == this.bufferModel.header[0]) {
                        this.myHeaderIndex = i;
                        if ((this.myHeaderIndex + this.bufferModel.header.length) - 1 >= this.byteOffset) {
                            this.expendSize = this.myHeaderIndex;
                            break;
                        }
                        this.isGettedHeader = true;
                        int j = 1;
                        while (true) {
                            if (j >= this.bufferModel.header.length) {
                                break;
                            } else if (this.buffer[this.myHeaderIndex + j] != this.bufferModel.header[j]) {
                                this.isGettedHeader = false;
                                break;
                            } else {
                                j++;
                            }
                        }
                        if (this.isGettedHeader) {
                            this.expendSize = this.myHeaderIndex + this.bufferModel.header.length;
                            break;
                        }
                    }
                    this.expendSize++;
                    i++;
                }
            }
            if (!this.isGettedHeader || this.byteOffset < this.expendSize + this.bufferModel.secondHeaderLen) {
                break;
            }
            int result = this.listener.parseSecondHeader(this.buffer, this.expendSize, this.bufferModel.secondHeaderLen);
            if (result >= 0) {
                this.bufferModel.bodyLen = result;
                if (this.byteOffset < this.expendSize + this.bufferModel.secondHeaderLen + result) {
                    break;
                }
                this.expendSize += this.bufferModel.secondHeaderLen;
                this.listener.onGetBody(this.buffer, this.expendSize, result);
                this.expendSize += result;
                this.isGettedHeader = false;
            } else {
                this.isGettedHeader = false;
            }
        }
        resetBuffer();
    }

    public void shutDown() {
    }
}
