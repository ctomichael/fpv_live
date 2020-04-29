package dji.component.device.flightmode;

public interface IFlightModeService {
    public static final String NAME = "FlightModeService";

    void addFlightModeChangeListener(FlightModeChangeListener flightModeChangeListener);

    UAVFlightMode getCurrentFlightMode();

    void removeFlightModeChangeListener(FlightModeChangeListener flightModeChangeListener);
}
