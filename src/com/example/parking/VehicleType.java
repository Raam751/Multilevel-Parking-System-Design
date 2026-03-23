package com.example.parking;

import java.util.List;

/**
 * Types of vehicles the parking lot supports.
 * Each type knows which slot types it can fit into.
 */
public enum VehicleType {

    TWO_WHEELER(List.of(SlotType.SMALL, SlotType.MEDIUM, SlotType.LARGE)),
    CAR(List.of(SlotType.MEDIUM, SlotType.LARGE)),
    BUS(List.of(SlotType.LARGE));

    private final List<SlotType> compatibleSlots;

    VehicleType(List<SlotType> compatibleSlots) {
        this.compatibleSlots = compatibleSlots;
    }

    public List<SlotType> getCompatibleSlots() {
        return compatibleSlots;
    }

    public boolean canFitIn(SlotType slotType) {
        return compatibleSlots.contains(slotType);
    }
}
