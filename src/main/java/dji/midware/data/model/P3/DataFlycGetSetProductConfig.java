package dji.midware.data.model.P3;

import com.adobe.xmp.XMPError;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusFocusInfoMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusRawInfoMakernoteDirectory;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SanyoMakernoteDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import com.google.zxing.client.result.ExpandedProductParsedResult;
import dji.component.areacode.IAreaCode;
import dji.log.DJILogPaths;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import it.sauronsoftware.ftp4j.FTPCodes;
import java.util.Arrays;
import java.util.HashMap;
import org.bouncycastle.crypto.tls.CipherSuite;

public class DataFlycGetSetProductConfig extends DataBase implements DJIDataSyncListener {
    private static HashMap<String, Integer> data = new HashMap<>(512);
    private static DataFlycGetSetProductConfig instance = null;
    private long mAreaCode;
    private SubCmdType mSubCmdType = SubCmdType.GET_AREA_CODE;

    static {
        data.put("AF", 4);
        data.put("AX", 248);
        data.put("AL", 8);
        data.put("DZ", 12);
        data.put("AS", 16);
        data.put("AD", 20);
        data.put("AO", 24);
        data.put("AI", 660);
        data.put("AQ", 10);
        data.put("AG", 28);
        data.put(DJILogPaths.LOG_AR, 32);
        data.put("AM", 51);
        data.put("AW", 533);
        data.put("AU", 36);
        data.put("AT", 40);
        data.put("AZ", 31);
        data.put("BS", 44);
        data.put("BH", 48);
        data.put("BD", 50);
        data.put("BB", 52);
        data.put("BY", 112);
        data.put("BE", 56);
        data.put("BZ", 84);
        data.put("BJ", Integer.valueOf((int) XMPError.BADSTREAM));
        data.put("BM", 60);
        data.put("BT", 64);
        data.put("BO", 68);
        data.put("BQ", Integer.valueOf((int) SanyoMakernoteDirectory.TAG_RECORD_SHUTTER_RELEASE));
        data.put("BA", 70);
        data.put("BW", 72);
        data.put("BV", 74);
        data.put("BR", 76);
        data.put("IO", 86);
        data.put("BN", 96);
        data.put("BG", 100);
        data.put("BF", 854);
        data.put("BI", 108);
        data.put("CV", 132);
        data.put("KH", 116);
        data.put("CM", 120);
        data.put("CA", Integer.valueOf((int) PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH));
        data.put("KY", 136);
        data.put("CF", 140);
        data.put("TD", 148);
        data.put("CL", 152);
        data.put(IAreaCode.STR_AREA_CODE_CHINA, 156);
        data.put("CX", 162);
        data.put("CC", 166);
        data.put("CO", 170);
        data.put("KM", 174);
        data.put("CD", 180);
        data.put("CG", 178);
        data.put("CK", 184);
        data.put("CR", 188);
        data.put("CI", 384);
        data.put("HR", Integer.valueOf((int) CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA256));
        data.put("CU", 192);
        data.put("CW", 531);
        data.put("CY", Integer.valueOf((int) CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256));
        data.put("CZ", Integer.valueOf((int) XMPError.BADXMP));
        data.put("DK", Integer.valueOf((int) CanonMakernoteDirectory.TAG_VRD_OFFSET));
        data.put("DJ", 262);
        data.put("DM", Integer.valueOf((int) FTPCodes.DIRECTORY_STATUS));
        data.put("DO", Integer.valueOf((int) FTPCodes.HELP_MESSAGE));
        data.put("EC", 218);
        data.put("EG", Integer.valueOf((int) LeicaMakernoteDirectory.TAG_CONTROLLER_BOARD_VERSION));
        data.put("SV", 222);
        data.put("GQ", Integer.valueOf((int) FTPCodes.DATA_CONNECTION_CLOSING));
        data.put("ER", 232);
        data.put("EE", 233);
        data.put("SZ", 748);
        data.put("ET", 231);
        data.put("FK", 238);
        data.put("FO", 234);
        data.put("FJ", 242);
        data.put("FI", 246);
        data.put("FR", 250);
        data.put("GF", 254);
        data.put("PF", 258);
        data.put("TF", 260);
        data.put("GA", 266);
        data.put("GM", 270);
        data.put("GE", 268);
        data.put("DE", 276);
        data.put("GH", 288);
        data.put("GI", Integer.valueOf((int) OlympusRawInfoMakernoteDirectory.TagWbRbLevelsEveningSunlight));
        data.put("GR", 300);
        data.put("GL", Integer.valueOf((int) OlympusRawInfoMakernoteDirectory.TagWbRbLevelsDaylightFluor));
        data.put("GD", 308);
        data.put("GP", 312);
        data.put("GU", 316);
        data.put("GT", 320);
        data.put("GG", 831);
        data.put("GN", Integer.valueOf((int) ExifDirectoryBase.TAG_TILE_OFFSETS));
        data.put("GW", 624);
        data.put("GY", 328);
        data.put("HT", Integer.valueOf((int) FTPCodes.NEED_ACCOUNT));
        data.put("HM", 334);
        data.put("VA", Integer.valueOf((int) IptcDirectory.TAG_TIME_SENT));
        data.put("HN", 340);
        data.put(IAreaCode.STR_AREA_CODE_HONGKONG, 344);
        data.put("HU", 348);
        data.put("IS", 352);
        data.put("IN", Integer.valueOf((int) IptcDirectory.TAG_UNIQUE_OBJECT_NAME));
        data.put("ID", 360);
        data.put("IR", 364);
        data.put("IQ", 368);
        data.put("IE", 372);
        data.put("IM", 833);
        data.put("IL", Integer.valueOf((int) IptcDirectory.TAG_ARM_IDENTIFIER));
        data.put("IT", 380);
        data.put("JM", 388);
        data.put(IAreaCode.STR_AREA_CODE_JAPAN, 392);
        data.put("JE", Integer.valueOf((int) LeicaMakernoteDirectory.TAG_IMAGE_ID_NUMBER));
        data.put("JO", 400);
        data.put("KZ", 398);
        data.put("KE", 404);
        data.put("KI", 296);
        data.put("KP", 408);
        data.put("KR", 410);
        data.put("KW", 414);
        data.put(ExpandedProductParsedResult.KILOGRAM, 417);
        data.put("LA", 418);
        data.put("LV", 428);
        data.put(ExpandedProductParsedResult.POUND, 422);
        data.put("LS", Integer.valueOf((int) FTPCodes.CONNECTION_CLOSED));
        data.put("LR", 430);
        data.put("LY", 434);
        data.put("LI", 438);
        data.put("LT", 440);
        data.put("LU", 442);
        data.put(IAreaCode.STR_AREA_CODE_MACAU, 446);
        data.put("MK", 807);
        data.put("MG", Integer.valueOf((int) FTPCodes.FILE_ACTION_NOT_TAKEN));
        data.put("MW", 454);
        data.put("MY", 458);
        data.put("MV", 462);
        data.put("ML", 466);
        data.put("MT", 470);
        data.put("MH", 584);
        data.put("MQ", 474);
        data.put("MR", 478);
        data.put("MU", 480);
        data.put("YT", 175);
        data.put("MX", 484);
        data.put("FM", 583);
        data.put("MD", 498);
        data.put("MC", 492);
        data.put("MN", 496);
        data.put("ME", 499);
        data.put("MS", 500);
        data.put("MA", Integer.valueOf((int) FTPCodes.COMMAND_PARAMETER_NOT_IMPLEMENTED));
        data.put("MZ", 508);
        data.put("MM", 104);
        data.put("NA", 516);
        data.put("NR", 520);
        data.put("NP", 524);
        data.put("NL", 528);
        data.put("NC", 540);
        data.put("NZ", Integer.valueOf((int) IptcDirectory.TAG_ACTION_ADVISED));
        data.put("NI", 558);
        data.put("NE", Integer.valueOf((int) IptcDirectory.TAG_REFERENCE_NUMBER));
        data.put("NG", 566);
        data.put("NU", 570);
        data.put("NF", Integer.valueOf((int) IptcDirectory.TAG_DIGITAL_DATE_CREATED));
        data.put("MP", 580);
        data.put("NO", 578);
        data.put("OM", 512);
        data.put("PK", 586);
        data.put("PW", 585);
        data.put("PS", 275);
        data.put("PA", 591);
        data.put("PG", 598);
        data.put("PY", 600);
        data.put("PE", Integer.valueOf((int) IptcDirectory.TAG_SUB_LOCATION));
        data.put("PH", 608);
        data.put("PN", Integer.valueOf((int) IptcDirectory.TAG_COUNTRY_OR_PRIMARY_LOCATION_CODE));
        data.put("PL", 616);
        data.put("PT", 620);
        data.put("PR", Integer.valueOf((int) IptcDirectory.TAG_CONTACT));
        data.put("QA", Integer.valueOf((int) IptcDirectory.TAG_CAPTION_WRITER));
        data.put("RE", 638);
        data.put("RO", Integer.valueOf((int) IptcDirectory.TAG_IMAGE_TYPE));
        data.put("RU", Integer.valueOf((int) IptcDirectory.TAG_IMAGE_ORIENTATION));
        data.put("RW", 646);
        data.put("BL", 652);
        data.put("SH", 654);
        data.put("KN", 659);
        data.put("LC", Integer.valueOf((int) IptcDirectory.TAG_AUDIO_TYPE));
        data.put("MF", Integer.valueOf((int) IptcDirectory.TAG_AUDIO_SAMPLING_RATE));
        data.put("PM", Integer.valueOf((int) IptcDirectory.TAG_AUDIO_OUTCUE));
        data.put("VC", 670);
        data.put("WS", 882);
        data.put("SM", 674);
        data.put("ST", 678);
        data.put("SA", 682);
        data.put("SN", 686);
        data.put("RS", 688);
        data.put("SC", 690);
        data.put("SL", 694);
        data.put("SG", 702);
        data.put("SX", 534);
        data.put("SK", 703);
        data.put("SI", 705);
        data.put("SB", 90);
        data.put("SO", 706);
        data.put("ZA", 710);
        data.put("GS", 239);
        data.put("SS", 728);
        data.put("ES", 724);
        data.put("LK", 144);
        data.put("SD", 729);
        data.put("SR", 740);
        data.put("SJ", 744);
        data.put("SE", 752);
        data.put("CH", 756);
        data.put("SY", 760);
        data.put("TW", 158);
        data.put("TJ", 762);
        data.put("TZ", 834);
        data.put("TH", 764);
        data.put("TL", 626);
        data.put("TG", 768);
        data.put("TK", 772);
        data.put("TO", Integer.valueOf((int) OlympusFocusInfoMakernoteDirectory.TagAfPoint));
        data.put("TT", 780);
        data.put("TN", 788);
        data.put("TR", 792);
        data.put("TM", 795);
        data.put("TC", 796);
        data.put("TV", 798);
        data.put("UG", Integer.valueOf((int) LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE));
        data.put("UA", Integer.valueOf((int) LeicaMakernoteDirectory.TAG_WB_BLUE_LEVEL));
        data.put("AE", 784);
        data.put("GB", 826);
        data.put("UM", 581);
        data.put("US", 840);
        data.put("UY", 858);
        data.put("UZ", 860);
        data.put("VU", Integer.valueOf((int) SanyoMakernoteDirectory.TAG_SEQUENCE_SHOT_INTERVAL));
        data.put("VE", 862);
        data.put("VN", 704);
        data.put("VG", 92);
        data.put("VI", 850);
        data.put("WF", 876);
        data.put("EH", 732);
        data.put("YE", 887);
        data.put("ZM", 894);
        data.put("ZW", 716);
    }

    public static synchronized DataFlycGetSetProductConfig getInstance() {
        DataFlycGetSetProductConfig dataFlycGetSetProductConfig;
        synchronized (DataFlycGetSetProductConfig.class) {
            if (instance == null) {
                instance = new DataFlycGetSetProductConfig();
            }
            dataFlycGetSetProductConfig = instance;
        }
        return dataFlycGetSetProductConfig;
    }

    public DataFlycGetSetProductConfig setAreaCode(String areaCode) {
        this.mSubCmdType = SubCmdType.SET_AREA_CODE;
        this.mAreaCode = transIsoA2To3166(areaCode);
        return this;
    }

    public long getAreaCode() {
        return ((Long) get(1, 8, Long.class)).longValue();
    }

    public String getAreaCodeString() {
        int areaCode = (int) getAreaCode();
        for (String ac : data.keySet()) {
            if (areaCode == data.get(ac).intValue()) {
                return ac;
            }
        }
        return "";
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.mSubCmdType == SubCmdType.GET_AREA_CODE) {
            this._sendData = new byte[9];
            Arrays.fill(this._sendData, (byte) 0);
        } else if (this.mSubCmdType == SubCmdType.SET_AREA_CODE) {
            this._sendData = new byte[9];
            Arrays.fill(this._sendData, (byte) 0);
            byte[] data2 = BytesUtil.getBytes(this.mAreaCode);
            System.arraycopy(data2, 0, this._sendData, 1, data2.length);
        }
        this._sendData[0] = BytesUtil.getByte(this.mSubCmdType.cmd);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.GetAreaCode.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 3;
        start(pack, callBack);
    }

    public enum SubCmdType {
        SET_AREA_CODE(3),
        GET_AREA_CODE(4);
        
        int cmd;

        private SubCmdType(int i) {
            this.cmd = i;
        }
    }

    private long transIsoA2To3166(String areaCode) {
        if (data.containsKey(areaCode.toUpperCase())) {
            return (long) data.get(areaCode.toUpperCase()).intValue();
        }
        return 840;
    }
}
