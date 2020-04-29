package dji.internal.diagnostics.extramodel;

public class ExtraHeightRadiusLimit {
    private int mHeightLimit;
    private int mRadiusLimit;

    public int getHeightLimit() {
        return this.mHeightLimit;
    }

    public int getRadiusLimit() {
        return this.mRadiusLimit;
    }

    public ExtraHeightRadiusLimit(int heightLimit, int radiusLimit) {
        this.mHeightLimit = heightLimit;
        this.mRadiusLimit = radiusLimit;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("ExtraHeightRadiusLimit{");
        sb.append("mHeightLimit=").append(this.mHeightLimit);
        sb.append(", mRadiusLimit=").append(this.mRadiusLimit);
        sb.append('}');
        return sb.toString();
    }
}
