package dji.component.activate.model;

public enum ActivateStatusEvent {
    Activated(0),
    UnActive(1),
    GetFail(2),
    Unknown(3);
    
    int id;

    private ActivateStatusEvent(int id2) {
        this.id = id2;
    }
}
