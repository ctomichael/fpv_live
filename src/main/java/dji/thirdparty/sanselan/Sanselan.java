package dji.thirdparty.sanselan;

import dji.thirdparty.sanselan.common.IImageMetadata;
import dji.thirdparty.sanselan.common.byteSources.ByteSource;
import dji.thirdparty.sanselan.common.byteSources.ByteSourceArray;
import dji.thirdparty.sanselan.common.byteSources.ByteSourceFile;
import dji.thirdparty.sanselan.common.byteSources.ByteSourceInputStream;
import dji.thirdparty.sanselan.util.Debug;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public abstract class Sanselan implements SanselanConstants {
    public static ImageFormat guessFormat(ByteSource byteSource) throws ImageReadException, IOException {
        ImageFormat imageFormat;
        InputStream is = null;
        try {
            is = byteSource.getInputStream();
            int i1 = is.read();
            int i2 = is.read();
            if (i1 < 0 || i2 < 0) {
                throw new ImageReadException("Couldn't read magic numbers to guess format.");
            }
            int b1 = i1 & 255;
            int b2 = i2 & 255;
            if (b1 == 71 && b2 == 73) {
                imageFormat = ImageFormat.IMAGE_FORMAT_GIF;
                if (is != null) {
                    try {
                    } catch (IOException e) {
                        Debug.debug((Throwable) e);
                    }
                }
            } else if (b1 == 137 && b2 == 80) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PNG;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e2) {
                        Debug.debug((Throwable) e2);
                    }
                }
            } else if (b1 == 255 && b2 == 216) {
                imageFormat = ImageFormat.IMAGE_FORMAT_JPEG;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e3) {
                        Debug.debug((Throwable) e3);
                    }
                }
            } else if (b1 == 66 && b2 == 77) {
                imageFormat = ImageFormat.IMAGE_FORMAT_BMP;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e4) {
                        Debug.debug((Throwable) e4);
                    }
                }
            } else if (b1 == 77 && b2 == 77) {
                imageFormat = ImageFormat.IMAGE_FORMAT_TIFF;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e5) {
                        Debug.debug((Throwable) e5);
                    }
                }
            } else if (b1 == 73 && b2 == 73) {
                imageFormat = ImageFormat.IMAGE_FORMAT_TIFF;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e6) {
                        Debug.debug((Throwable) e6);
                    }
                }
            } else if (b1 == 56 && b2 == 66) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PSD;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e7) {
                        Debug.debug((Throwable) e7);
                    }
                }
            } else if (b1 == 80 && b2 == 49) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PBM;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e8) {
                        Debug.debug((Throwable) e8);
                    }
                }
            } else if (b1 == 80 && b2 == 52) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PBM;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e9) {
                        Debug.debug((Throwable) e9);
                    }
                }
            } else if (b1 == 80 && b2 == 50) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PGM;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e10) {
                        Debug.debug((Throwable) e10);
                    }
                }
            } else if (b1 == 80 && b2 == 53) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PGM;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e11) {
                        Debug.debug((Throwable) e11);
                    }
                }
            } else if (b1 == 80 && b2 == 51) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PPM;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e12) {
                        Debug.debug((Throwable) e12);
                    }
                }
            } else if (b1 == 80 && b2 == 54) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PPM;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e13) {
                        Debug.debug((Throwable) e13);
                    }
                }
            } else {
                if (b1 == 151 && b2 == 74) {
                    int i3 = is.read();
                    int i4 = is.read();
                    if (i3 < 0 || i4 < 0) {
                        throw new ImageReadException("Couldn't read magic numbers to guess format.");
                    }
                    int b4 = i4 & 255;
                    if ((i3 & 255) == 66 && b4 == 50) {
                        imageFormat = ImageFormat.IMAGE_FORMAT_JBIG2;
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e14) {
                                Debug.debug((Throwable) e14);
                            }
                        }
                    }
                }
                imageFormat = ImageFormat.IMAGE_FORMAT_UNKNOWN;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e15) {
                        Debug.debug((Throwable) e15);
                    }
                }
            }
            return imageFormat;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e16) {
                    Debug.debug((Throwable) e16);
                }
            }
        }
    }

    private static final ImageParser getImageParser(ByteSource byteSource) throws ImageReadException, IOException {
        ImageFormat format = guessFormat(byteSource);
        if (!format.equals(ImageFormat.IMAGE_FORMAT_UNKNOWN)) {
            ImageParser[] imageParsers = ImageParser.getAllImageParsers();
            int i = 0;
            while (true) {
                if (i >= imageParsers.length) {
                    break;
                }
                imageParser = imageParsers[i];
                if (imageParser.canAcceptType(format)) {
                    break;
                }
                i++;
            }
            return imageParser;
        }
        String filename = byteSource.getFilename();
        if (filename != null) {
            ImageParser[] imageParsers2 = ImageParser.getAllImageParsers();
            for (ImageParser imageParser : imageParsers2) {
                if (imageParser.canAcceptExtension(filename)) {
                    return imageParser;
                }
            }
        }
        throw new ImageReadException("Can't parse this format.");
    }

    public static IImageMetadata getMetadata(byte[] bytes) throws ImageReadException, IOException {
        return getMetadata(bytes, (Map) null);
    }

    public static IImageMetadata getMetadata(byte[] bytes, Map params) throws ImageReadException, IOException {
        return getMetadata(new ByteSourceArray(bytes), params);
    }

    public static IImageMetadata getMetadata(InputStream is, String filename) throws ImageReadException, IOException {
        return getMetadata(is, filename, null);
    }

    public static IImageMetadata getMetadata(InputStream is, String filename, Map params) throws ImageReadException, IOException {
        return getMetadata(new ByteSourceInputStream(is, filename), params);
    }

    public static IImageMetadata getMetadata(File file) throws ImageReadException, IOException {
        return getMetadata(file, (Map) null);
    }

    public static IImageMetadata getMetadata(File file, Map params) throws ImageReadException, IOException {
        return getMetadata(new ByteSourceFile(file), params);
    }

    private static IImageMetadata getMetadata(ByteSource byteSource, Map params) throws ImageReadException, IOException {
        return getImageParser(byteSource).getMetadata(byteSource, params);
    }
}
