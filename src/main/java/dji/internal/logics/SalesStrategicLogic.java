package dji.internal.logics;

import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.ArrayList;
import java.util.Iterator;

public class SalesStrategicLogic implements DJIParamAccessListener {
    private SalesStrategy salesStrategy;

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        this.salesStrategy = (SalesStrategy) CacheHelper.getFlightController(FlightControllerKeys.SALES_STRATEGY);
    }

    private static final class HOLDER {
        /* access modifiers changed from: private */
        public static SalesStrategicLogic instance = new SalesStrategicLogic();

        private HOLDER() {
        }
    }

    private SalesStrategicLogic() {
        CacheHelper.addFlightControllerListener(this, FlightControllerKeys.SALES_STRATEGY);
        CacheHelper.addProductListener(this, DJISDKCacheKeys.CONNECTION);
    }

    public void init() {
        getListenKeyValue();
    }

    public void destroy() {
        CacheHelper.removeListener(this);
    }

    private void getListenKeyValue() {
        this.salesStrategy = (SalesStrategy) CacheHelper.getFlightController(FlightControllerKeys.SALES_STRATEGY);
    }

    public static SalesStrategicLogic getInstance() {
        return HOLDER.instance;
    }

    public static final class SalesStrategy {
        int allowToFlyAreaNumbers;
        ArrayList<StrategyAreaCode> areaCodes;
        StrategyAreaCode currentAreaCode;
        boolean isSupported;
        boolean isTakeOffAllowed;
        int strategy;
        int version;

        public int getVersion() {
            return this.version;
        }

        public boolean isTakeOffAllowed() {
            return this.isTakeOffAllowed;
        }

        public int getStrategy() {
            return this.strategy;
        }

        public StrategyAreaCode getCurrentAreaCode() {
            return this.currentAreaCode;
        }

        public int getAllowToFlyAreaNumbers() {
            return this.allowToFlyAreaNumbers;
        }

        public ArrayList<StrategyAreaCode> getAreaCodes() {
            return this.areaCodes;
        }

        private SalesStrategy(Builder builder) {
            this.isSupported = builder.isSupported;
            this.version = builder.version;
            this.isTakeOffAllowed = builder.isTakeOffAllowed;
            this.strategy = builder.strategy;
            this.currentAreaCode = builder.currentAreaCode;
            this.allowToFlyAreaNumbers = builder.allowToFlyAreaNumbers;
            this.areaCodes = builder.areaCodes;
        }

        public static final class Builder {
            int allowToFlyAreaNumbers;
            ArrayList<StrategyAreaCode> areaCodes;
            StrategyAreaCode currentAreaCode;
            boolean isSupported;
            boolean isTakeOffAllowed;
            int strategy;
            int version;

            public Builder isSupported(boolean isSupported2) {
                this.isSupported = isSupported2;
                return this;
            }

            public Builder version(int version2) {
                this.version = version2;
                return this;
            }

            public Builder isTakeOffAllowed(boolean isTakeOffAllowed2) {
                this.isTakeOffAllowed = isTakeOffAllowed2;
                return this;
            }

            public Builder strategy(int strategy2) {
                this.strategy = strategy2;
                return this;
            }

            public Builder currentAreaCode(int currentAreaCode2) {
                this.currentAreaCode = StrategyAreaCode.find(currentAreaCode2);
                return this;
            }

            public Builder allowToFlyAreaNumbers(int allowToFlyAreaNumbers2) {
                this.allowToFlyAreaNumbers = allowToFlyAreaNumbers2;
                return this;
            }

            public Builder areaCodes(ArrayList<Integer> areaCodes2) {
                ArrayList<StrategyAreaCode> codes = new ArrayList<>();
                Iterator<Integer> it2 = areaCodes2.iterator();
                while (it2.hasNext()) {
                    codes.add(StrategyAreaCode.find(it2.next().intValue()));
                }
                this.areaCodes = codes;
                return this;
            }

            public SalesStrategy build() {
                return new SalesStrategy(this);
            }
        }
    }

    public enum StrategyAreaCode {
        DJI_AREA_CODE_GLOBAL(0),
        DJI_AREA_CODE_GLOBAL_CHN(156),
        DJI_AREA_CODE_TWN(158),
        DJI_AREA_CODE_JPN(392),
        DJI_AREA_CODE_PRK(408),
        DJI_AREA_CODE_KOR(410),
        DJI_AREA_CODE_CHN_MAIN_LAND(1000),
        DJI_AREA_CODE_ASIA_PACIFIC(1001),
        DJI_AREA_CODE_UNKNOWN(-1);
        
        private final int mValue;

        private StrategyAreaCode(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        private boolean _equals(int b) {
            return this.mValue == b;
        }

        public static StrategyAreaCode find(int b) {
            StrategyAreaCode result = DJI_AREA_CODE_UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
