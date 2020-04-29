package com.drew.metadata.iptc;

import com.drew.lang.StringUtil;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class IptcDescriptor extends TagDescriptor<IptcDirectory> {
    public IptcDescriptor(@NotNull IptcDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 276:
                return getFileFormatDescription();
            case IptcDirectory.TAG_DATE_SENT /*326*/:
                return getDateSentDescription();
            case IptcDirectory.TAG_TIME_SENT /*336*/:
                return getTimeSentDescription();
            case 537:
                return getKeywordsDescription();
            case 542:
                return getReleaseDateDescription();
            case 547:
                return getReleaseTimeDescription();
            case 549:
                return getExpirationDateDescription();
            case 550:
                return getExpirationTimeDescription();
            case 559:
                return getReferenceDateDescription();
            case IptcDirectory.TAG_DATE_CREATED /*567*/:
                return getDateCreatedDescription();
            case IptcDirectory.TAG_TIME_CREATED /*572*/:
                return getTimeCreatedDescription();
            case IptcDirectory.TAG_DIGITAL_DATE_CREATED /*574*/:
                return getDigitalDateCreatedDescription();
            case IptcDirectory.TAG_DIGITAL_TIME_CREATED /*575*/:
                return getDigitalTimeCreatedDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getDateDescription(int tagType) {
        String s = ((IptcDirectory) this._directory).getString(tagType);
        if (s == null) {
            return null;
        }
        if (s.length() == 8) {
            return s.substring(0, 4) + ':' + s.substring(4, 6) + ':' + s.substring(6);
        }
        return s;
    }

    @Nullable
    public String getTimeDescription(int tagType) {
        String s = ((IptcDirectory) this._directory).getString(tagType);
        if (s == null) {
            return null;
        }
        if (s.length() == 6 || s.length() == 11) {
            return s.substring(0, 2) + ':' + s.substring(2, 4) + ':' + s.substring(4);
        }
        return s;
    }

    @Nullable
    public String getFileFormatDescription() {
        Integer value = ((IptcDirectory) this._directory).getInteger(276);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 0:
                return "No ObjectData";
            case 1:
                return "IPTC-NAA Digital Newsphoto Parameter Record";
            case 2:
                return "IPTC7901 Recommended Message Format";
            case 3:
                return "Tagged Image File Format (Adobe/Aldus Image data)";
            case 4:
                return "Illustrator (Adobe Graphics data)";
            case 5:
                return "AppleSingle (Apple Computer Inc)";
            case 6:
                return "NAA 89-3 (ANPA 1312)";
            case 7:
                return "MacBinary II";
            case 8:
                return "IPTC Unstructured Character Oriented File Format (UCOFF)";
            case 9:
                return "United Press International ANPA 1312 variant";
            case 10:
                return "United Press International Down-Load Message";
            case 11:
                return "JPEG File Interchange (JFIF)";
            case 12:
                return "Photo-CD Image-Pac (Eastman Kodak)";
            case 13:
                return "Bit Mapped Graphics File [.BMP] (Microsoft)";
            case 14:
                return "Digital Audio File [.WAV] (Microsoft & Creative Labs)";
            case 15:
                return "Audio plus Moving Video [.AVI] (Microsoft)";
            case 16:
                return "PC DOS/Windows Executable Files [.COM][.EXE]";
            case 17:
                return "Compressed Binary File [.ZIP] (PKWare Inc)";
            case 18:
                return "Audio Interchange File Format AIFF (Apple Computer Inc)";
            case 19:
                return "RIFF Wave (Microsoft Corporation)";
            case 20:
                return "Freehand (Macromedia/Aldus)";
            case 21:
                return "Hypertext Markup Language [.HTML] (The Internet Society)";
            case 22:
                return "MPEG 2 Audio Layer 2 (Musicom), ISO/IEC";
            case 23:
                return "MPEG 2 Audio Layer 3, ISO/IEC";
            case 24:
                return "Portable Document File [.PDF] Adobe";
            case 25:
                return "News Industry Text Format (NITF)";
            case 26:
                return "Tape Archive [.TAR]";
            case 27:
                return "Tidningarnas Telegrambyra NITF version (TTNITF DTD)";
            case 28:
                return "Ritzaus Bureau NITF version (RBNITF DTD)";
            case 29:
                return "Corel Draw [.CDR]";
            default:
                return String.format("Unknown (%d)", value);
        }
    }

    @Nullable
    public String getByLineDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_BY_LINE);
    }

    @Nullable
    public String getByLineTitleDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_BY_LINE_TITLE);
    }

    @Nullable
    public String getCaptionDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_CAPTION);
    }

    @Nullable
    public String getCategoryDescription() {
        return ((IptcDirectory) this._directory).getString(527);
    }

    @Nullable
    public String getCityDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_CITY);
    }

    @Nullable
    public String getCopyrightNoticeDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_COPYRIGHT_NOTICE);
    }

    @Nullable
    public String getCountryOrPrimaryLocationDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_COUNTRY_OR_PRIMARY_LOCATION_NAME);
    }

    @Nullable
    public String getCreditDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_CREDIT);
    }

    @Nullable
    public String getDateCreatedDescription() {
        return getDateDescription(IptcDirectory.TAG_DATE_CREATED);
    }

    @Nullable
    public String getDigitalDateCreatedDescription() {
        return getDateDescription(IptcDirectory.TAG_DIGITAL_DATE_CREATED);
    }

    @Nullable
    public String getDateSentDescription() {
        return getDateDescription(IptcDirectory.TAG_DATE_SENT);
    }

    @Nullable
    public String getExpirationDateDescription() {
        return getDateDescription(549);
    }

    @Nullable
    public String getExpirationTimeDescription() {
        return getTimeDescription(550);
    }

    @Nullable
    public String getHeadlineDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_HEADLINE);
    }

    @Nullable
    public String getKeywordsDescription() {
        String[] keywords = ((IptcDirectory) this._directory).getStringArray(537);
        if (keywords == null) {
            return null;
        }
        return StringUtil.join(keywords, ";");
    }

    @Nullable
    public String getObjectNameDescription() {
        return ((IptcDirectory) this._directory).getString(517);
    }

    @Nullable
    public String getOriginalTransmissionReferenceDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_ORIGINAL_TRANSMISSION_REFERENCE);
    }

    @Nullable
    public String getOriginatingProgramDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_ORIGINATING_PROGRAM);
    }

    @Nullable
    public String getProvinceOrStateDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_PROVINCE_OR_STATE);
    }

    @Nullable
    public String getRecordVersionDescription() {
        return ((IptcDirectory) this._directory).getString(512);
    }

    @Nullable
    public String getReferenceDateDescription() {
        return getDateDescription(559);
    }

    @Nullable
    public String getReleaseDateDescription() {
        return getDateDescription(542);
    }

    @Nullable
    public String getReleaseTimeDescription() {
        return getTimeDescription(547);
    }

    @Nullable
    public String getSourceDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_SOURCE);
    }

    @Nullable
    public String getSpecialInstructionsDescription() {
        return ((IptcDirectory) this._directory).getString(552);
    }

    @Nullable
    public String getSupplementalCategoriesDescription() {
        return ((IptcDirectory) this._directory).getString(532);
    }

    @Nullable
    public String getTimeCreatedDescription() {
        return getTimeDescription(IptcDirectory.TAG_TIME_CREATED);
    }

    @Nullable
    public String getDigitalTimeCreatedDescription() {
        return getTimeDescription(IptcDirectory.TAG_DIGITAL_TIME_CREATED);
    }

    @Nullable
    public String getTimeSentDescription() {
        return getTimeDescription(IptcDirectory.TAG_TIME_SENT);
    }

    @Nullable
    public String getUrgencyDescription() {
        return ((IptcDirectory) this._directory).getString(522);
    }

    @Nullable
    public String getWriterDescription() {
        return ((IptcDirectory) this._directory).getString(IptcDirectory.TAG_CAPTION_WRITER);
    }
}
