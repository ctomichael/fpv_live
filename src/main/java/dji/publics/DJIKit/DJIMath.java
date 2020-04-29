package dji.publics.DJIKit;

public class DJIMath {
    public static float getNiceValue(float input, float max) {
        float result = input;
        if (input > 0.0f) {
            return Math.min(max, result);
        }
        return Math.max(-max, result);
    }
}
