package lecho.lib.hellocharts.model;

import dji.component.accountcenter.IMemberProtocol;

public class SelectedValue {
    private int firstIndex;
    private int secondIndex;
    private SelectedValueType type = SelectedValueType.NONE;

    public enum SelectedValueType {
        NONE,
        LINE,
        COLUMN
    }

    public SelectedValue() {
        clear();
    }

    public SelectedValue(int firstIndex2, int secondIndex2, SelectedValueType type2) {
        set(firstIndex2, secondIndex2, type2);
    }

    public void set(int firstIndex2, int secondIndex2, SelectedValueType type2) {
        this.firstIndex = firstIndex2;
        this.secondIndex = secondIndex2;
        if (type2 != null) {
            this.type = type2;
        } else {
            this.type = SelectedValueType.NONE;
        }
    }

    public void set(SelectedValue selectedValue) {
        this.firstIndex = selectedValue.firstIndex;
        this.secondIndex = selectedValue.secondIndex;
        this.type = selectedValue.type;
    }

    public void clear() {
        set(Integer.MIN_VALUE, Integer.MIN_VALUE, SelectedValueType.NONE);
    }

    public boolean isSet() {
        if (this.firstIndex < 0 || this.secondIndex < 0) {
            return false;
        }
        return true;
    }

    public int getFirstIndex() {
        return this.firstIndex;
    }

    public void setFirstIndex(int firstIndex2) {
        this.firstIndex = firstIndex2;
    }

    public int getSecondIndex() {
        return this.secondIndex;
    }

    public void setSecondIndex(int secondIndex2) {
        this.secondIndex = secondIndex2;
    }

    public SelectedValueType getType() {
        return this.type;
    }

    public void setType(SelectedValueType type2) {
        this.type = type2;
    }

    public int hashCode() {
        return ((((this.firstIndex + 31) * 31) + this.secondIndex) * 31) + (this.type == null ? 0 : this.type.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SelectedValue other = (SelectedValue) obj;
        if (this.firstIndex != other.firstIndex) {
            return false;
        }
        if (this.secondIndex != other.secondIndex) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "SelectedValue [firstIndex=" + this.firstIndex + ", secondIndex=" + this.secondIndex + ", type=" + this.type + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }
}
