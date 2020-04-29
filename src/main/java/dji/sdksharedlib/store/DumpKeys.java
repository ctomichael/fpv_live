package dji.sdksharedlib.store;

public class DumpKeys {
    private String componentName;
    private int index;

    public DumpKeys(String componentName2, int index2) {
        this.componentName = componentName2;
        this.index = index2;
    }

    public String getComponentName() {
        return this.componentName;
    }

    public int getIndex() {
        return this.index;
    }
}
