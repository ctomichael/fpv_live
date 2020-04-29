package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantPhantom4ProAbstraction;

@EXClassNullAway
public class FlightControllerPhantom4PAbstraction extends FlightControllerPhantom4Abstraction {
    /* access modifiers changed from: protected */
    public IntelligentFlightAssistantAbstraction newIntelligentFlightAssistantIfSupport() {
        return new IntelligentFlightAssistantPhantom4ProAbstraction();
    }
}
