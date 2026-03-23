package com.example.parking;

/**
 * Represents an entry gate of the parking lot, located on a specific floor.
 */
public class EntryGate {

    private final int gateId;
    private final int floor;

    public EntryGate(int gateId, int floor) {
        this.gateId = gateId;
        this.floor = floor;
    }

    public int getGateId() {
        return gateId;
    }

    public int getFloor() {
        return floor;
    }

    @Override
    public String toString() {
        return "EntryGate{id=" + gateId + ", floor=" + floor + "}";
    }
}
