package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ChannelInterference {
    private int channel;
    private int power;

    public int hashCode() {
        return (this.channel * 31) + this.power;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ChannelInterference)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.channel == ((ChannelInterference) o).channel && this.power == ((ChannelInterference) o).power;
    }

    public int getChannel() {
        return this.channel;
    }

    public void setChannel(int channel2) {
        this.channel = channel2;
    }

    public int getPower() {
        return this.power;
    }

    public void setPower(int power2) {
        this.power = power2;
    }
}
