package com.google.zxing.client.result;

public final class VINParsedResult extends ParsedResult {
    private final String countryCode;
    private final int modelYear;
    private final char plantCode;
    private final String sequentialNumber;
    private final String vehicleAttributes;
    private final String vehicleDescriptorSection;
    private final String vehicleIdentifierSection;
    private final String vin;
    private final String worldManufacturerID;

    public VINParsedResult(String vin2, String worldManufacturerID2, String vehicleDescriptorSection2, String vehicleIdentifierSection2, String countryCode2, String vehicleAttributes2, int modelYear2, char plantCode2, String sequentialNumber2) {
        super(ParsedResultType.VIN);
        this.vin = vin2;
        this.worldManufacturerID = worldManufacturerID2;
        this.vehicleDescriptorSection = vehicleDescriptorSection2;
        this.vehicleIdentifierSection = vehicleIdentifierSection2;
        this.countryCode = countryCode2;
        this.vehicleAttributes = vehicleAttributes2;
        this.modelYear = modelYear2;
        this.plantCode = plantCode2;
        this.sequentialNumber = sequentialNumber2;
    }

    public String getVIN() {
        return this.vin;
    }

    public String getWorldManufacturerID() {
        return this.worldManufacturerID;
    }

    public String getVehicleDescriptorSection() {
        return this.vehicleDescriptorSection;
    }

    public String getVehicleIdentifierSection() {
        return this.vehicleIdentifierSection;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getVehicleAttributes() {
        return this.vehicleAttributes;
    }

    public int getModelYear() {
        return this.modelYear;
    }

    public char getPlantCode() {
        return this.plantCode;
    }

    public String getSequentialNumber() {
        return this.sequentialNumber;
    }

    public String getDisplayResult() {
        StringBuilder result = new StringBuilder(50);
        result.append(this.worldManufacturerID).append(' ');
        result.append(this.vehicleDescriptorSection).append(' ');
        result.append(this.vehicleIdentifierSection).append(10);
        if (this.countryCode != null) {
            result.append(this.countryCode).append(' ');
        }
        result.append(this.modelYear).append(' ');
        result.append(this.plantCode).append(' ');
        result.append(this.sequentialNumber).append(10);
        return result.toString();
    }
}
