package com.example.parking;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Bill generated when a vehicle exits the parking lot.
 * Billing is based on the ALLOCATED SLOT TYPE, not the vehicle type.
 */
public class Bill {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ParkingTicket ticket;
    private final LocalDateTime exitTime;
    private final long durationHours;   // ceiling-rounded
    private final double totalAmount;

    public Bill(ParkingTicket ticket, LocalDateTime exitTime) {
        this.ticket = ticket;
        this.exitTime = exitTime;

        // ceiling: any partial hour counts as a full hour, minimum 1 hour
        Duration d = Duration.between(ticket.getEntryTime(), exitTime);
        long minutes = d.toMinutes();
        this.durationHours = Math.max(1, (minutes + 59) / 60);

        // billing based on allocated slot type
        int rate = ticket.getAllocatedSlot().getSlotType().getHourlyRate();
        this.totalAmount = this.durationHours * rate;
    }

    public ParkingTicket getTicket() {
        return ticket;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public long getDurationHours() {
        return durationHours;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String toString() {
        return "===== BILL =====\n"
                + "Ticket ID   : " + ticket.getTicketId() + "\n"
                + "Vehicle     : " + ticket.getVehicle().getLicensePlate()
                + " (" + ticket.getVehicle().getType() + ")\n"
                + "Slot        : #" + ticket.getAllocatedSlot().getSlotNumber()
                + " (" + ticket.getAllocatedSlot().getSlotType() + ")\n"
                + "Entry       : " + ticket.getEntryTime().format(FMT) + "\n"
                + "Exit        : " + exitTime.format(FMT) + "\n"
                + "Duration    : " + durationHours + " hour(s)\n"
                + "Rate        : Rs." + ticket.getAllocatedSlot().getSlotType().getHourlyRate() + "/hr\n"
                + "TOTAL       : Rs." + (int) totalAmount + "\n"
                + "================";
    }
}
