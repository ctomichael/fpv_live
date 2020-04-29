package dji.component.flightrecord;

import java.util.List;

public interface IFlightRecordService {
    public static final String NAME = "FlightRecordService";

    public interface OnFlightRecordFileStateChangedListener {
        void onFileClosed();

        void onFileCreated();
    }

    void addFileStateChangedListener(OnFlightRecordFileStateChangedListener onFlightRecordFileStateChangedListener);

    void removeFileStateChangedListener(OnFlightRecordFileStateChangedListener onFlightRecordFileStateChangedListener);

    void updateCheckListData(List<FlightRecordCheckListData> list);

    void updateTipsData(List<FlightRecordTipData> list);

    void updateTopBarData(FlightRecordTopBarData flightRecordTopBarData);
}
