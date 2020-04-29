package com.drew.metadata.iptc;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.Directory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class IptcDirectory extends Directory {
    public static final int TAG_ACTION_ADVISED = 554;
    public static final int TAG_APPLICATION_RECORD_VERSION = 512;
    public static final int TAG_ARM_IDENTIFIER = 376;
    public static final int TAG_ARM_VERSION = 378;
    public static final int TAG_AUDIO_DURATION = 665;
    public static final int TAG_AUDIO_OUTCUE = 666;
    public static final int TAG_AUDIO_SAMPLING_RATE = 663;
    public static final int TAG_AUDIO_SAMPLING_RESOLUTION = 664;
    public static final int TAG_AUDIO_TYPE = 662;
    public static final int TAG_BY_LINE = 592;
    public static final int TAG_BY_LINE_TITLE = 597;
    public static final int TAG_CAPTION = 632;
    public static final int TAG_CAPTION_WRITER = 634;
    public static final int TAG_CATEGORY = 527;
    public static final int TAG_CITY = 602;
    public static final int TAG_CODED_CHARACTER_SET = 346;
    public static final int TAG_CONTACT = 630;
    public static final int TAG_CONTENT_LOCATION_CODE = 538;
    public static final int TAG_CONTENT_LOCATION_NAME = 539;
    public static final int TAG_COPYRIGHT_NOTICE = 628;
    public static final int TAG_COUNTRY_OR_PRIMARY_LOCATION_CODE = 612;
    public static final int TAG_COUNTRY_OR_PRIMARY_LOCATION_NAME = 613;
    public static final int TAG_CREDIT = 622;
    public static final int TAG_DATE_CREATED = 567;
    public static final int TAG_DATE_SENT = 326;
    public static final int TAG_DESTINATION = 261;
    public static final int TAG_DIGITAL_DATE_CREATED = 574;
    public static final int TAG_DIGITAL_TIME_CREATED = 575;
    public static final int TAG_EDITORIAL_UPDATE = 520;
    public static final int TAG_EDIT_STATUS = 519;
    public static final int TAG_ENVELOPE_NUMBER = 296;
    public static final int TAG_ENVELOPE_PRIORITY = 316;
    public static final int TAG_ENVELOPE_RECORD_VERSION = 256;
    public static final int TAG_EXPIRATION_DATE = 549;
    public static final int TAG_EXPIRATION_TIME = 550;
    public static final int TAG_FILE_FORMAT = 276;
    public static final int TAG_FILE_VERSION = 278;
    public static final int TAG_FIXTURE_ID = 534;
    public static final int TAG_HEADLINE = 617;
    public static final int TAG_IMAGE_ORIENTATION = 643;
    public static final int TAG_IMAGE_TYPE = 642;
    public static final int TAG_JOB_ID = 696;
    public static final int TAG_KEYWORDS = 537;
    public static final int TAG_LANGUAGE_IDENTIFIER = 647;
    public static final int TAG_LOCAL_CAPTION = 633;
    public static final int TAG_MASTER_DOCUMENT_ID = 697;
    public static final int TAG_OBJECT_ATTRIBUTE_REFERENCE = 516;
    public static final int TAG_OBJECT_CYCLE = 587;
    public static final int TAG_OBJECT_NAME = 517;
    public static final int TAG_OBJECT_PREVIEW_DATA = 714;
    public static final int TAG_OBJECT_PREVIEW_FILE_FORMAT = 712;
    public static final int TAG_OBJECT_PREVIEW_FILE_FORMAT_VERSION = 713;
    public static final int TAG_OBJECT_TYPE_REFERENCE = 515;
    public static final int TAG_ORIGINAL_TRANSMISSION_REFERENCE = 615;
    public static final int TAG_ORIGINATING_PROGRAM = 577;
    public static final int TAG_OWNER_ID = 700;
    public static final int TAG_PRODUCT_ID = 306;
    public static final int TAG_PROGRAM_VERSION = 582;
    public static final int TAG_PROVINCE_OR_STATE = 607;
    public static final int TAG_RASTERIZED_CAPTION = 637;
    public static final int TAG_REFERENCE_DATE = 559;
    public static final int TAG_REFERENCE_NUMBER = 562;
    public static final int TAG_REFERENCE_SERVICE = 557;
    public static final int TAG_RELEASE_DATE = 542;
    public static final int TAG_RELEASE_TIME = 547;
    public static final int TAG_SERVICE_ID = 286;
    public static final int TAG_SHORT_DOCUMENT_ID = 698;
    public static final int TAG_SOURCE = 627;
    public static final int TAG_SPECIAL_INSTRUCTIONS = 552;
    public static final int TAG_SUBJECT_REFERENCE = 524;
    public static final int TAG_SUB_LOCATION = 604;
    public static final int TAG_SUPPLEMENTAL_CATEGORIES = 532;
    public static final int TAG_TIME_CREATED = 572;
    public static final int TAG_TIME_SENT = 336;
    public static final int TAG_UNIQUE_DOCUMENT_ID = 699;
    public static final int TAG_UNIQUE_OBJECT_NAME = 356;
    public static final int TAG_URGENCY = 522;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(256, "Enveloped Record Version");
        _tagNameMap.put(261, "Destination");
        _tagNameMap.put(276, "File Format");
        _tagNameMap.put(278, "File Version");
        _tagNameMap.put(286, "Service Identifier");
        _tagNameMap.put(296, "Envelope Number");
        _tagNameMap.put(306, "Product Identifier");
        _tagNameMap.put(316, "Envelope Priority");
        _tagNameMap.put(Integer.valueOf((int) TAG_DATE_SENT), "Date Sent");
        _tagNameMap.put(Integer.valueOf((int) TAG_TIME_SENT), "Time Sent");
        _tagNameMap.put(Integer.valueOf((int) TAG_CODED_CHARACTER_SET), "Coded Character Set");
        _tagNameMap.put(Integer.valueOf((int) TAG_UNIQUE_OBJECT_NAME), "Unique Object Name");
        _tagNameMap.put(Integer.valueOf((int) TAG_ARM_IDENTIFIER), "ARM Identifier");
        _tagNameMap.put(Integer.valueOf((int) TAG_ARM_VERSION), "ARM Version");
        _tagNameMap.put(512, "Application Record Version");
        _tagNameMap.put(515, "Object Type Reference");
        _tagNameMap.put(516, "Object Attribute Reference");
        _tagNameMap.put(517, "Object Name");
        _tagNameMap.put(519, "Edit Status");
        _tagNameMap.put(520, "Editorial Update");
        _tagNameMap.put(522, "Urgency");
        _tagNameMap.put(524, "Subject Reference");
        _tagNameMap.put(527, "Category");
        _tagNameMap.put(532, "Supplemental Category(s)");
        _tagNameMap.put(534, "Fixture Identifier");
        _tagNameMap.put(537, "Keywords");
        _tagNameMap.put(Integer.valueOf((int) TAG_CONTENT_LOCATION_CODE), "Content Location Code");
        _tagNameMap.put(539, "Content Location Name");
        _tagNameMap.put(542, "Release Date");
        _tagNameMap.put(547, "Release Time");
        _tagNameMap.put(549, "Expiration Date");
        _tagNameMap.put(550, "Expiration Time");
        _tagNameMap.put(552, "Special Instructions");
        _tagNameMap.put(Integer.valueOf((int) TAG_ACTION_ADVISED), "Action Advised");
        _tagNameMap.put(Integer.valueOf((int) TAG_REFERENCE_SERVICE), "Reference Service");
        _tagNameMap.put(559, "Reference Date");
        _tagNameMap.put(Integer.valueOf((int) TAG_REFERENCE_NUMBER), "Reference Number");
        _tagNameMap.put(Integer.valueOf((int) TAG_DATE_CREATED), "Date Created");
        _tagNameMap.put(Integer.valueOf((int) TAG_TIME_CREATED), "Time Created");
        _tagNameMap.put(Integer.valueOf((int) TAG_DIGITAL_DATE_CREATED), "Digital Date Created");
        _tagNameMap.put(Integer.valueOf((int) TAG_DIGITAL_TIME_CREATED), "Digital Time Created");
        _tagNameMap.put(Integer.valueOf((int) TAG_ORIGINATING_PROGRAM), "Originating Program");
        _tagNameMap.put(Integer.valueOf((int) TAG_PROGRAM_VERSION), "Program Version");
        _tagNameMap.put(587, "Object Cycle");
        _tagNameMap.put(Integer.valueOf((int) TAG_BY_LINE), "By-line");
        _tagNameMap.put(Integer.valueOf((int) TAG_BY_LINE_TITLE), "By-line Title");
        _tagNameMap.put(Integer.valueOf((int) TAG_CITY), "City");
        _tagNameMap.put(Integer.valueOf((int) TAG_SUB_LOCATION), "Sub-location");
        _tagNameMap.put(Integer.valueOf((int) TAG_PROVINCE_OR_STATE), "Province/State");
        _tagNameMap.put(Integer.valueOf((int) TAG_COUNTRY_OR_PRIMARY_LOCATION_CODE), "Country/Primary Location Code");
        _tagNameMap.put(Integer.valueOf((int) TAG_COUNTRY_OR_PRIMARY_LOCATION_NAME), "Country/Primary Location Name");
        _tagNameMap.put(Integer.valueOf((int) TAG_ORIGINAL_TRANSMISSION_REFERENCE), "Original Transmission Reference");
        _tagNameMap.put(Integer.valueOf((int) TAG_HEADLINE), "Headline");
        _tagNameMap.put(Integer.valueOf((int) TAG_CREDIT), "Credit");
        _tagNameMap.put(Integer.valueOf((int) TAG_SOURCE), "Source");
        _tagNameMap.put(Integer.valueOf((int) TAG_COPYRIGHT_NOTICE), "Copyright Notice");
        _tagNameMap.put(Integer.valueOf((int) TAG_CONTACT), "Contact");
        _tagNameMap.put(Integer.valueOf((int) TAG_CAPTION), "Caption/Abstract");
        _tagNameMap.put(Integer.valueOf((int) TAG_LOCAL_CAPTION), "Local Caption");
        _tagNameMap.put(Integer.valueOf((int) TAG_CAPTION_WRITER), "Caption Writer/Editor");
        _tagNameMap.put(Integer.valueOf((int) TAG_RASTERIZED_CAPTION), "Rasterized Caption");
        _tagNameMap.put(Integer.valueOf((int) TAG_IMAGE_TYPE), "Image Type");
        _tagNameMap.put(Integer.valueOf((int) TAG_IMAGE_ORIENTATION), "Image Orientation");
        _tagNameMap.put(Integer.valueOf((int) TAG_LANGUAGE_IDENTIFIER), "Language Identifier");
        _tagNameMap.put(Integer.valueOf((int) TAG_AUDIO_TYPE), "Audio Type");
        _tagNameMap.put(Integer.valueOf((int) TAG_AUDIO_SAMPLING_RATE), "Audio Sampling Rate");
        _tagNameMap.put(Integer.valueOf((int) TAG_AUDIO_SAMPLING_RESOLUTION), "Audio Sampling Resolution");
        _tagNameMap.put(Integer.valueOf((int) TAG_AUDIO_DURATION), "Audio Duration");
        _tagNameMap.put(Integer.valueOf((int) TAG_AUDIO_OUTCUE), "Audio Outcue");
        _tagNameMap.put(Integer.valueOf((int) TAG_JOB_ID), "Job Identifier");
        _tagNameMap.put(Integer.valueOf((int) TAG_MASTER_DOCUMENT_ID), "Master Document Identifier");
        _tagNameMap.put(Integer.valueOf((int) TAG_SHORT_DOCUMENT_ID), "Short Document Identifier");
        _tagNameMap.put(Integer.valueOf((int) TAG_UNIQUE_DOCUMENT_ID), "Unique Document Identifier");
        _tagNameMap.put(700, "Owner Identifier");
        _tagNameMap.put(Integer.valueOf((int) TAG_OBJECT_PREVIEW_FILE_FORMAT), "Object Data Preview File Format");
        _tagNameMap.put(Integer.valueOf((int) TAG_OBJECT_PREVIEW_FILE_FORMAT_VERSION), "Object Data Preview File Format Version");
        _tagNameMap.put(Integer.valueOf((int) TAG_OBJECT_PREVIEW_DATA), "Object Data Preview Data");
    }

    public IptcDirectory() {
        setDescriptor(new IptcDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "IPTC";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }

    @Nullable
    public List<String> getKeywords() {
        String[] array = getStringArray(537);
        if (array == null) {
            return null;
        }
        return Arrays.asList(array);
    }

    @Nullable
    public Date getDateSent() {
        return getDate((int) TAG_DATE_SENT, (int) TAG_TIME_SENT);
    }

    @Nullable
    public Date getReleaseDate() {
        return getDate(542, 547);
    }

    @Nullable
    public Date getExpirationDate() {
        return getDate(549, 550);
    }

    @Nullable
    public Date getDateCreated() {
        return getDate((int) TAG_DATE_CREATED, (int) TAG_TIME_CREATED);
    }

    @Nullable
    public Date getDigitalDateCreated() {
        return getDate((int) TAG_DIGITAL_DATE_CREATED, (int) TAG_DIGITAL_TIME_CREATED);
    }

    @Nullable
    private Date getDate(int dateTagType, int timeTagType) {
        String date = getString(dateTagType);
        String time = getString(timeTagType);
        if (date == null || time == null) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyyMMddHHmmssZ").parse(date + time);
        } catch (ParseException e) {
            return null;
        }
    }
}
