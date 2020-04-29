package com.google.zxing.multi;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import java.util.Map;

public final class ByQuadrantReader implements Reader {
    private final Reader delegate;

    public ByQuadrantReader(Reader delegate2) {
        this.delegate = delegate2;
    }

    public Result decode(BinaryBitmap image) throws NotFoundException, ChecksumException, FormatException {
        return decode(image, null);
    }

    public Result decode(BinaryBitmap image, Map<DecodeHintType, ?> hints) throws NotFoundException, ChecksumException, FormatException {
        int halfWidth = image.getWidth() / 2;
        int halfHeight = image.getHeight() / 2;
        try {
            return this.delegate.decode(image.crop(0, 0, halfWidth, halfHeight), hints);
        } catch (NotFoundException e) {
            try {
                Result result = this.delegate.decode(image.crop(halfWidth, 0, halfWidth, halfHeight), hints);
                makeAbsolute(result.getResultPoints(), halfWidth, 0);
                return result;
            } catch (NotFoundException e2) {
                try {
                    Result result2 = this.delegate.decode(image.crop(0, halfHeight, halfWidth, halfHeight), hints);
                    makeAbsolute(result2.getResultPoints(), 0, halfHeight);
                    return result2;
                } catch (NotFoundException e3) {
                    try {
                        Result result3 = this.delegate.decode(image.crop(halfWidth, halfHeight, halfWidth, halfHeight), hints);
                        makeAbsolute(result3.getResultPoints(), halfWidth, halfHeight);
                        return result3;
                    } catch (NotFoundException e4) {
                        int quarterWidth = halfWidth / 2;
                        int quarterHeight = halfHeight / 2;
                        Result result4 = this.delegate.decode(image.crop(quarterWidth, quarterHeight, halfWidth, halfHeight), hints);
                        makeAbsolute(result4.getResultPoints(), quarterWidth, quarterHeight);
                        return result4;
                    }
                }
            }
        }
    }

    public void reset() {
        this.delegate.reset();
    }

    private static void makeAbsolute(ResultPoint[] points, int leftOffset, int topOffset) {
        if (points != null) {
            for (int i = 0; i < points.length; i++) {
                ResultPoint relative = points[i];
                points[i] = new ResultPoint(relative.getX() + ((float) leftOffset), relative.getY() + ((float) topOffset));
            }
        }
    }
}
