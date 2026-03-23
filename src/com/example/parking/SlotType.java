package com.example.parking;

/**
 * Types of parking slots with their hourly rates.
 */
public enum SlotType {

    SMALL(10),    // for 2-wheelers
    MEDIUM(20),   // for cars
    LARGE(50);    // for buses

    private final int hourlyRate;

    SlotType(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }
}
