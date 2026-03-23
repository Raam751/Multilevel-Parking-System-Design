package com.example.parking;

/**
 * A single parking slot in the lot.
 * Knows its floor, slot number, type, and occupancy state.
 */
public class ParkingSlot {

    private final int slotNumber;
    private final SlotType slotType;
    private final int floor;
    private boolean occupied;

    public ParkingSlot(int slotNumber, SlotType slotType, int floor) {
        this.slotNumber = slotNumber;
        this.slotType = slotType;
        this.floor = floor;
        this.occupied = false;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public int getFloor() {
        return floor;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void occupy() {
        this.occupied = true;
    }

    public void vacate() {
        this.occupied = false;
    }

    /**
     * Checks if a given vehicle type can fit in this slot.
     */
    public boolean isCompatible(VehicleType vehicleType) {
        return vehicleType.canFitIn(this.slotType);
    }

    @Override
    public String toString() {
        return "Slot{#" + slotNumber + ", type=" + slotType
                + ", floor=" + floor + ", occupied=" + occupied + "}";
    }
}
