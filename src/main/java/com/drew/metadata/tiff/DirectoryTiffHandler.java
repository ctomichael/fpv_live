package com.drew.metadata.tiff;

import com.drew.imaging.tiff.TiffHandler;
import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import com.drew.metadata.ErrorDirectory;
import com.drew.metadata.Metadata;
import com.drew.metadata.StringValue;
import java.util.Stack;

public abstract class DirectoryTiffHandler implements TiffHandler {
    protected Directory _currentDirectory;
    private final Stack<Directory> _directoryStack = new Stack<>();
    protected final Metadata _metadata;

    protected DirectoryTiffHandler(Metadata metadata) {
        this._metadata = metadata;
    }

    public void endingIFD() {
        this._currentDirectory = this._directoryStack.empty() ? null : this._directoryStack.pop();
    }

    /* access modifiers changed from: protected */
    public void pushDirectory(@NotNull Class<? extends Directory> directoryClass) {
        try {
            Directory newDirectory = (Directory) directoryClass.newInstance();
            if (newDirectory != null) {
                if (this._currentDirectory != null) {
                    this._directoryStack.push(this._currentDirectory);
                    newDirectory.setParent(this._currentDirectory);
                }
                this._currentDirectory = newDirectory;
                this._metadata.addDirectory(this._currentDirectory);
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        }
    }

    public void warn(@NotNull String message) {
        getCurrentOrErrorDirectory().addError(message);
    }

    public void error(@NotNull String message) {
        getCurrentOrErrorDirectory().addError(message);
    }

    @NotNull
    private Directory getCurrentOrErrorDirectory() {
        if (this._currentDirectory != null) {
            return this._currentDirectory;
        }
        ErrorDirectory error = (ErrorDirectory) this._metadata.getFirstDirectoryOfType(ErrorDirectory.class);
        if (error != null) {
            return error;
        }
        pushDirectory(ErrorDirectory.class);
        return this._currentDirectory;
    }

    public void setByteArray(int tagId, @NotNull byte[] bytes) {
        this._currentDirectory.setByteArray(tagId, bytes);
    }

    public void setString(int tagId, @NotNull StringValue string) {
        this._currentDirectory.setStringValue(tagId, string);
    }

    public void setRational(int tagId, @NotNull Rational rational) {
        this._currentDirectory.setRational(tagId, rational);
    }

    public void setRationalArray(int tagId, @NotNull Rational[] array) {
        this._currentDirectory.setRationalArray(tagId, array);
    }

    public void setFloat(int tagId, float float32) {
        this._currentDirectory.setFloat(tagId, float32);
    }

    public void setFloatArray(int tagId, @NotNull float[] array) {
        this._currentDirectory.setFloatArray(tagId, array);
    }

    public void setDouble(int tagId, double double64) {
        this._currentDirectory.setDouble(tagId, double64);
    }

    public void setDoubleArray(int tagId, @NotNull double[] array) {
        this._currentDirectory.setDoubleArray(tagId, array);
    }

    public void setInt8s(int tagId, byte int8s) {
        this._currentDirectory.setInt(tagId, int8s);
    }

    public void setInt8sArray(int tagId, @NotNull byte[] array) {
        this._currentDirectory.setByteArray(tagId, array);
    }

    public void setInt8u(int tagId, short int8u) {
        this._currentDirectory.setInt(tagId, int8u);
    }

    public void setInt8uArray(int tagId, @NotNull short[] array) {
        this._currentDirectory.setObjectArray(tagId, array);
    }

    public void setInt16s(int tagId, int int16s) {
        this._currentDirectory.setInt(tagId, int16s);
    }

    public void setInt16sArray(int tagId, @NotNull short[] array) {
        this._currentDirectory.setObjectArray(tagId, array);
    }

    public void setInt16u(int tagId, int int16u) {
        this._currentDirectory.setInt(tagId, int16u);
    }

    public void setInt16uArray(int tagId, @NotNull int[] array) {
        this._currentDirectory.setObjectArray(tagId, array);
    }

    public void setInt32s(int tagId, int int32s) {
        this._currentDirectory.setInt(tagId, int32s);
    }

    public void setInt32sArray(int tagId, @NotNull int[] array) {
        this._currentDirectory.setIntArray(tagId, array);
    }

    public void setInt32u(int tagId, long int32u) {
        this._currentDirectory.setLong(tagId, int32u);
    }

    public void setInt32uArray(int tagId, @NotNull long[] array) {
        this._currentDirectory.setObjectArray(tagId, array);
    }
}
