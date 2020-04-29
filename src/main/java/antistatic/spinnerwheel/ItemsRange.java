package antistatic.spinnerwheel;

public class ItemsRange {
    private int count;
    private int first;

    public ItemsRange() {
        this(0, 0);
    }

    public ItemsRange(int first2, int count2) {
        this.first = first2;
        this.count = count2;
    }

    public int getFirst() {
        return this.first;
    }

    public int getLast() {
        return (getFirst() + getCount()) - 1;
    }

    public int getCount() {
        return this.count;
    }

    public boolean contains(int index) {
        return index >= getFirst() && index <= getLast();
    }
}
