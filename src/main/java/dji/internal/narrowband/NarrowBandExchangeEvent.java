package dji.internal.narrowband;

import dji.midware.data.model.P3.DataNarrowBandExchangeMode;

public class NarrowBandExchangeEvent {
    private NarrowBandSlaveMode mBeforeCharacter;
    private NarrowBandSlaveMode mDesireCharacter;
    private NarrowBandSlaveMode mFromCharacter;

    public NarrowBandExchangeEvent(DataNarrowBandExchangeMode exchangeInfo) {
        this.mFromCharacter = NarrowBandSlaveMode.find(exchangeInfo.getFromSlaveMode());
        this.mDesireCharacter = NarrowBandSlaveMode.find(exchangeInfo.getDesireSlaveMode());
        this.mBeforeCharacter = NarrowBandSlaveMode.find(exchangeInfo.getBeforeSlaveMode());
    }

    public NarrowBandSlaveMode getFromCharacter() {
        return this.mFromCharacter;
    }

    public NarrowBandSlaveMode getDesireCharacter() {
        return this.mDesireCharacter;
    }

    public NarrowBandSlaveMode getBeforeCharacter() {
        return this.mBeforeCharacter;
    }
}
