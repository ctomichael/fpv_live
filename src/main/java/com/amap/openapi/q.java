package com.amap.openapi;

import com.amap.location.common.model.CellStatus;
import java.util.ArrayList;
import java.util.List;

/* compiled from: Cell */
public class q {
    public byte a;
    public String b;
    public ArrayList<r> c = new ArrayList<>();
    public List<CellStatus.HistoryCell> d = new ArrayList();

    public void a(byte b2, String str) {
        this.a = b2;
        this.b = str;
        this.c.clear();
        this.d.clear();
    }

    public String toString() {
        return "Cell{mRadioType=" + ((int) this.a) + ", mOperator='" + this.b + '\'' + ", mCellPart=" + this.c + ", mHistoryCellList=" + this.d + '}';
    }
}
