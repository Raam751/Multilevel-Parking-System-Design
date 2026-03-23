package com.example.parking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Ticket generated when a vehicle is parked.
 * Records vehicle details, allocated slot, and entry time.
 */
public class ParkingTicket {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSlot allocatedSlot;
    private final LocalDateTime entryTime;

    public ParkingTicket(String ticketId, Vehicle vehicle,
                         ParkingSlot allocatedSlot, LocalDateTime entryTime) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.allocatedSlot = allocatedSlot;
        this.entryTime = entryTime;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSlot getAllocatedSlot() {
        return allocatedSlot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    @Override
    public String toString() {
        return "ParkingTicket{"
                + "id='" + ticketId + '\''
                + ", vehicle=" + vehicle
                + ", slot=#" + allocatedSlot.getSlotNumber()
                + " (" + allocatedSlot.getSlotType() + ")"
                + ", entry=" + entryTime.format(FMT)
                + '}';
    }
}
