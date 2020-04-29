package dji.pilot.configs;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;

@EXClassNullAway
public interface IProductConfigs {
    public static final ProductType[] ACADEMY_SHOW_LIST = {ProductType.WM240, ProductType.WM230, ProductType.Mammoth, ProductType.Potato, ProductType.Pomato, ProductType.PomatoSDR, ProductType.Orange2, ProductType.KumquatX, ProductType.Tomato, ProductType.M200, ProductType.M210, ProductType.M210RTK};
    public static final ProductType[] ACADEMY_SHOW_LIST_DPAD = {ProductType.Potato, ProductType.Pomato, ProductType.Tomato};
    public static final ProductType[] ACADEMY_SHOW_LIST_DPAD_SDR = {ProductType.PomatoSDR};
    public static final ProductType[] APP_SUPPORT_LIST = {ProductType.WM240, ProductType.WM230, ProductType.Mammoth, ProductType.Potato, ProductType.Pomato, ProductType.PomatoSDR, ProductType.Orange2, ProductType.KumquatX, ProductType.Tomato, ProductType.M200, ProductType.M210, ProductType.M210RTK};
    public static final ProductType[] MAINPAGE_SHOW_LIST = {ProductType.WM240, ProductType.WM230, ProductType.Mammoth, ProductType.Potato, ProductType.Pomato, ProductType.PomatoSDR, ProductType.Orange2, ProductType.KumquatX, ProductType.Tomato, ProductType.M200, ProductType.M210, ProductType.M210RTK};
}
