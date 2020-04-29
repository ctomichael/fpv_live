package com.dji.component.fpv.base;

import com.dji.component.fpv.base.distanceunit.DistanceUnitConstant;
import dji.common.mission.activetrack.QuickShotMode;
import dji.utils.function.Consumer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FpvConstant {

    public static class QuickShot {
        private static final float METRIC_DEFAULT_DISTANCE = 25.0f;
        private static QuickShotMode sCurrentQuickShotMode = QuickShotMode.DRONIE;
        private static Map<QuickShotMode, Boolean> sDirectionByQuickShotMode = new HashMap();
        private static Map<QuickShotMode, Float> sDistanceByQuickShotMode = new HashMap();
        private static PersistenceObservable sDistanceUnitObservable = new PersistenceObservable(DistanceUnitConstant.getDistanceUnitKey(), Integer.class, 0);
        private static List<Consumer<Boolean>> sQuickShotDirectionConsumers = new LinkedList();
        private static List<Consumer<Float>> sQuickShotDistanceConsumers = new LinkedList();
        private static List<Consumer<QuickShotMode>> sQuickShotModeConsumers = new LinkedList();

        static {
            sDistanceUnitObservable.map(FpvConstant$QuickShot$$Lambda$0.$instance).subscribe(FpvConstant$QuickShot$$Lambda$1.$instance);
        }

        public static void resetQuickShotDistance() {
            sDistanceByQuickShotMode.put(QuickShotMode.DRONIE, Float.valueOf(25.0f));
            sDistanceByQuickShotMode.put(QuickShotMode.ASTEROID, Float.valueOf(25.0f));
            sDistanceByQuickShotMode.put(QuickShotMode.HELIX, Float.valueOf(25.0f));
            sDistanceByQuickShotMode.put(QuickShotMode.ROCKET, Float.valueOf(25.0f));
            for (Consumer<Float> distanceConsumer : sQuickShotDistanceConsumers) {
                distanceConsumer.accept(Float.valueOf(25.0f));
            }
        }

        public static void setQuickShotMode(QuickShotMode mode) {
            sCurrentQuickShotMode = mode;
            for (Consumer<QuickShotMode> modeConsumer : sQuickShotModeConsumers) {
                modeConsumer.accept(mode);
            }
            setDistance(mode, getDistance(mode));
        }

        public static void setDistance(QuickShotMode mode, float distance) {
            sDistanceByQuickShotMode.put(mode, Float.valueOf(distance));
            for (Consumer<Float> distanceConsumer : sQuickShotDistanceConsumers) {
                distanceConsumer.accept(Float.valueOf(distance));
            }
        }

        public static void setDirection(QuickShotMode mode, boolean clockwise) {
            sDirectionByQuickShotMode.put(mode, Boolean.valueOf(clockwise));
            for (Consumer<Boolean> directionConsumer : sQuickShotDirectionConsumers) {
                directionConsumer.accept(Boolean.valueOf(clockwise));
            }
        }

        public static QuickShotMode getQuickShotMode() {
            return sCurrentQuickShotMode;
        }

        public static float getDistance(QuickShotMode mode) {
            if (sDistanceByQuickShotMode.get(mode) == null) {
                sDistanceByQuickShotMode.put(mode, Float.valueOf(25.0f));
            }
            return sDistanceByQuickShotMode.get(mode).floatValue();
        }

        public static boolean getDirection(QuickShotMode mode) {
            if (sDirectionByQuickShotMode.get(mode) == null) {
                if (mode == QuickShotMode.HELIX || mode == QuickShotMode.ASTEROID) {
                    sDirectionByQuickShotMode.put(mode, true);
                } else {
                    sDirectionByQuickShotMode.put(mode, false);
                }
            }
            return sDirectionByQuickShotMode.get(mode).booleanValue();
        }

        public static Consumer<QuickShotMode> addQuickShotModeConsumer(Consumer<QuickShotMode> consumer) {
            sQuickShotModeConsumers.add(consumer);
            consumer.accept(sCurrentQuickShotMode);
            return consumer;
        }

        public static Consumer<Float> addDistanceConsumer(Consumer<Float> consumer) {
            sQuickShotDistanceConsumers.add(consumer);
            consumer.accept(Float.valueOf(getDistance(sCurrentQuickShotMode)));
            return consumer;
        }

        public static Consumer<Boolean> addDirectionConsumer(Consumer<Boolean> consumer) {
            sQuickShotDirectionConsumers.add(consumer);
            consumer.accept(Boolean.valueOf(getDirection(sCurrentQuickShotMode)));
            return consumer;
        }

        public static void removeShotModeConsumer(Consumer<QuickShotMode> consumer) {
            sQuickShotModeConsumers.remove(consumer);
        }

        public static void removeDistanceConsumer(Consumer<Float> consumer) {
            sQuickShotDistanceConsumers.remove(consumer);
        }

        public static void removeDirectionConsumer(Consumer<Boolean> consumer) {
            sQuickShotDirectionConsumers.remove(consumer);
        }

        private static boolean isMetricUnit() {
            return DistanceUnitConstant.getDistanceUnit() != DistanceUnitConstant.DistanceUnit.MILE;
        }
    }

    public static class NewbieGuide {
        private static boolean sNeverRemind = false;

        public static void setNeverRemind(boolean neverRemind) {
            sNeverRemind = neverRemind;
        }

        public static boolean getIsNeverRemind() {
            return sNeverRemind;
        }
    }
}
