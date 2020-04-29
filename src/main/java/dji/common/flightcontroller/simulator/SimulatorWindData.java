package dji.common.flightcontroller.simulator;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class SimulatorWindData {
    private int windSpeedX;
    private int windSpeedY;
    private int windSpeedZ;

    public int getWindSpeedX() {
        return this.windSpeedX;
    }

    public int getWindSpeedY() {
        return this.windSpeedY;
    }

    public int getWindSpeedZ() {
        return this.windSpeedZ;
    }

    private SimulatorWindData(int windSpeedX2, int windSpeedY2, int windSpeedZ2) {
        this.windSpeedX = windSpeedX2;
        this.windSpeedY = windSpeedY2;
        this.windSpeedZ = windSpeedZ2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimulatorWindData)) {
            return false;
        }
        SimulatorWindData that = (SimulatorWindData) o;
        if (this.windSpeedX == that.windSpeedX && this.windSpeedY == that.windSpeedY && this.windSpeedZ == that.windSpeedZ) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((this.windSpeedX * 31) + this.windSpeedY) * 31) + this.windSpeedZ;
    }

    public static class Builder {
        private int wind_speed_x = 0;
        private int wind_speed_y = 0;
        private int wind_speed_z = 0;

        public Builder windSpeedX(int x) {
            this.wind_speed_x = x;
            return this;
        }

        public Builder windSpeedY(int y) {
            this.wind_speed_y = y;
            return this;
        }

        public Builder windSpeedZ(int z) {
            this.wind_speed_z = z;
            return this;
        }

        public SimulatorWindData build() {
            return new SimulatorWindData(this.wind_speed_x, this.wind_speed_y, this.wind_speed_z);
        }
    }
}
