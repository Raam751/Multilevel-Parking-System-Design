package com.example.parking;

/**
 * Represents a vehicle entering the parking lot.
 */
public class Vehicle {

    private final String licensePlate;
    private final VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Vehicle{plate='" + licensePlate + "', type=" + type + "}";
    }
}
