package com.example.parking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Core parking lot system.
 *
 * Responsibilities:
 *   - park()   : find nearest compatible slot, generate ticket
 *   - exit()   : calculate bill based on slot type and duration
 *   - status() : return available slot counts by type
 */
public class ParkingLot {

    private final List<ParkingSlot> slots;
    private final Map<Integer, EntryGate> gateMap;           // gateId -> gate
    private final Map<String, ParkingTicket> activeTickets;   // ticketId -> ticket
    private int ticketCounter;

    public ParkingLot(List<ParkingSlot> slots, List<EntryGate> gates) {
        this.slots = new ArrayList<>(slots);
        this.gateMap = new HashMap<>();
        for (EntryGate g : gates) {
            gateMap.put(g.getGateId(), g);
        }
        this.activeTickets = new HashMap<>();
        this.ticketCounter = 0;
    }

    // ---- API 1: park ----

    /**
     * Parks a vehicle and returns the generated ticket.
     *
     * Strategy:
     *  1. Look up the entry gate to know its floor.
     *  2. Gather all free slots whose type is compatible with the vehicle
     *     AND whose type ordinal >= requestedSlotType ordinal
     *     (i.e. honour the minimum slot type requested, but allow upsizing).
     *  3. Sort by floor distance to gate (nearest first), then by slot number.
     *  4. Pick the first one.
     *
     * @param vehicle          the vehicle entering
     * @param entryTime        timestamp of entry
     * @param requestedSlotType minimum slot type to consider
     * @param entryGateId      which gate the vehicle entered from
     * @return the parking ticket, or null if no slot is available
     */
    public ParkingTicket park(Vehicle vehicle, LocalDateTime entryTime,
                              SlotType requestedSlotType, int entryGateId) {

        EntryGate gate = gateMap.get(entryGateId);
        if (gate == null) {
            System.out.println("[ERROR] Unknown gate ID: " + entryGateId);
            return null;
        }

        // find nearest compatible free slot
        ParkingSlot best = slots.stream()
                .filter(s -> !s.isOccupied())
                .filter(s -> s.isCompatible(vehicle.getType()))
                .filter(s -> s.getSlotType().ordinal() >= requestedSlotType.ordinal())
                .min(Comparator.comparingInt((ParkingSlot s) ->
                                Math.abs(s.getFloor() - gate.getFloor()))
                        .thenComparingInt(ParkingSlot::getSlotNumber))
                .orElse(null);

        if (best == null) {
            System.out.println("[ERROR] No compatible slot available for "
                    + vehicle.getType() + " (requested >= " + requestedSlotType + ")");
            return null;
        }

        best.occupy();

        String ticketId = "TKT-" + String.format("%04d", ++ticketCounter);
        ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, best, entryTime);
        activeTickets.put(ticketId, ticket);

        return ticket;
    }

    // ---- API 2: status ----

    /**
     * Returns current slot availability grouped by slot type.
     * The map has keys in order: SMALL, MEDIUM, LARGE.
     */
    public Map<SlotType, int[]> status() {
        // int[] = { total, available }
        Map<SlotType, int[]> result = new LinkedHashMap<>();
        for (SlotType t : SlotType.values()) {
            result.put(t, new int[]{0, 0});
        }
        for (ParkingSlot s : slots) {
            int[] counts = result.get(s.getSlotType());
            counts[0]++;                      // total
            if (!s.isOccupied()) counts[1]++; // available
        }
        return result;
    }

    // ---- API 3: exit ----

    /**
     * Processes a vehicle exit.
     *
     * @param ticket   the parking ticket (obtained during park)
     * @param exitTime timestamp of exit
     * @return the generated bill
     */
    public Bill exit(ParkingTicket ticket, LocalDateTime exitTime) {
        ParkingSlot slot = ticket.getAllocatedSlot();
        slot.vacate();
        activeTickets.remove(ticket.getTicketId());

        return new Bill(ticket, exitTime);
    }
}
