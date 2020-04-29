package com.amap.location.common.model;

import android.os.SystemClock;
import dji.component.accountcenter.IMemberProtocol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CellStatus {
    public static final int MASK_CDMA_CELL_TYPE = 2;
    public static final int MASK_GSM_CELL_TYPE = 1;
    public static final int MASK_NEW_VERSION_INDICATOR = 4;
    public static final int MASK_OPERATOR_INDICATOR = 8;
    private static final int MAX_HISTORY_CELLS = 3;
    public List<CellState> cellStateList2 = Collections.emptyList();
    public int cellType = 0;
    private final List<HistoryCell> mHistoryCells = new ArrayList(3);
    public CellState mainCell;
    public CellState mainCell2;
    public List<CellState> neighbors = Collections.emptyList();
    public String networkOperator;
    public long updateTime;

    public static class HistoryCell {
        public int bid = 0;
        public int cid = 0;
        public int lac = 0;
        public long lastUpdateTimeMills = 0;
        public int nid = 0;
        public int rssi = 0;
        public int sid = 0;
        public int type = 0;

        public HistoryCell() {
        }

        public HistoryCell(CellState cellState) {
            this.type = cellState.type;
            this.rssi = cellState.signalStrength;
            this.lac = cellState.lac;
            this.cid = cellState.cid;
            this.sid = cellState.sid;
            this.nid = cellState.nid;
            this.bid = cellState.bid;
            if (cellState.lastUpdateTimeMills <= 0) {
                this.lastUpdateTimeMills = SystemClock.elapsedRealtime();
            } else {
                this.lastUpdateTimeMills = cellState.lastUpdateTimeMills;
            }
        }

        public HistoryCell clone() {
            HistoryCell historyCell = new HistoryCell();
            historyCell.type = this.type;
            historyCell.rssi = this.rssi;
            historyCell.lac = this.lac;
            historyCell.cid = this.cid;
            historyCell.sid = this.sid;
            historyCell.nid = this.nid;
            historyCell.bid = this.bid;
            historyCell.lastUpdateTimeMills = this.lastUpdateTimeMills;
            return historyCell;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof HistoryCell)) {
                return false;
            }
            HistoryCell historyCell = (HistoryCell) obj;
            return this.type == historyCell.type && this.lac == historyCell.lac && this.cid == historyCell.cid && this.nid == historyCell.nid && this.bid == historyCell.bid && this.sid == historyCell.sid;
        }

        public String toString() {
            return String.format(Locale.CHINA, "[type=%d,rssi=%d,lac=%d, cid=%d,sid=%d,nid=%d, bid=%d, time=%d]", Integer.valueOf(this.type), Integer.valueOf(this.rssi), Integer.valueOf(this.lac), Integer.valueOf(this.cid), Integer.valueOf(this.sid), Integer.valueOf(this.nid), Integer.valueOf(this.bid), Long.valueOf(this.lastUpdateTimeMills));
        }
    }

    private String toStr(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("CellStatus:[");
        sb.append("updateTime=" + this.updateTime + ",");
        sb.append("cellType=" + this.cellType + ",");
        sb.append("networkOperator=" + this.networkOperator + ",");
        if (this.mainCell != null) {
            sb.append("mainCell=" + this.mainCell.toString() + ",");
        } else {
            sb.append("mainCell=null ,");
        }
        if (this.mainCell2 != null) {
            sb.append("mainCell2=" + this.mainCell2.toString() + ",");
        } else {
            sb.append("mainCell2=null ,");
        }
        if (this.neighbors == null || this.neighbors.size() <= 0) {
            sb.append("neighbors=null");
        } else {
            ArrayList arrayList = new ArrayList();
            if (this.neighbors.size() <= 5) {
                arrayList.addAll(this.neighbors);
                sb.append("neighbors=" + arrayList.toString());
            } else if (z) {
                arrayList.addAll(this.neighbors.subList(0, 5));
                sb.append("neighbors=" + arrayList.toString());
            } else {
                arrayList.addAll(this.neighbors);
                sb.append("neighbors=" + arrayList.toString());
            }
        }
        sb.append(";");
        if (this.cellStateList2 == null || this.cellStateList2.size() <= 0) {
            sb.append("cellStateList2=null");
        } else {
            ArrayList arrayList2 = new ArrayList();
            if (this.cellStateList2.size() <= 5) {
                arrayList2.addAll(this.cellStateList2);
                sb.append("cellStateList2=" + arrayList2.toString());
            } else if (z) {
                arrayList2.addAll(this.cellStateList2.subList(0, 5));
                sb.append("cellStateList2=" + arrayList2.toString());
            } else {
                arrayList2.addAll(this.cellStateList2);
                sb.append("cellStateList2=" + arrayList2.toString());
            }
        }
        sb.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        StringBuilder sb2 = new StringBuilder(" [HistoryCell:");
        int size = this.mHistoryCells.size();
        for (int i = 0; i < size; i++) {
            sb2.append(i).append(":").append(this.mHistoryCells.get(i).toString()).append(" ");
        }
        sb2.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return sb.toString() + sb2.toString();
    }

    public void addHistoryCell(List<HistoryCell> list) {
        if (list != null && list.size() > 0) {
            this.mHistoryCells.clear();
            this.mHistoryCells.addAll(list);
        }
    }

    public CellStatus clone() {
        CellStatus cellStatus = new CellStatus();
        cellStatus.updateTime = this.updateTime;
        cellStatus.cellType = this.cellType;
        cellStatus.networkOperator = this.networkOperator;
        if (this.mainCell != null) {
            cellStatus.mainCell = this.mainCell.clone();
        }
        if (this.mainCell2 != null) {
            cellStatus.mainCell2 = this.mainCell2.clone();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.neighbors);
        cellStatus.neighbors = arrayList;
        ArrayList arrayList2 = new ArrayList();
        arrayList2.addAll(this.cellStateList2);
        cellStatus.cellStateList2 = arrayList2;
        for (HistoryCell historyCell : this.mHistoryCells) {
            cellStatus.mHistoryCells.add(historyCell.clone());
        }
        return cellStatus;
    }

    public List<HistoryCell> getHistoryCells() {
        return this.mHistoryCells;
    }

    public String toString() {
        return toStr(false);
    }

    public String toStringSimple() {
        return toStr(true);
    }
}
