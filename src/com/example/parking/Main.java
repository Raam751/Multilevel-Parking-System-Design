package com.example.parking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Demo driver that exercises all parking lot features.
 *
 * Build & run:
 *   cd Multilevel-Parking-System/src
 *   javac com/example/parking/*.java
 *   java com.example.parking.Main
 */
public class Main {

    public static void main(String[] args) {

        // ========== SET UP PARKING LOT ==========
        // 3 floors, mix of slot types
        List<ParkingSlot> slots = new ArrayList<>();
        int slotNo = 1;

        // Floor 1: 3 SMALL, 2 MEDIUM, 1 LARGE
        for (int i = 0; i < 3; i++) slots.add(new ParkingSlot(slotNo++, SlotType.SMALL, 1));
        for (int i = 0; i < 2; i++) slots.add(new ParkingSlot(slotNo++, SlotType.MEDIUM, 1));
        slots.add(new ParkingSlot(slotNo++, SlotType.LARGE, 1));

        // Floor 2: 2 SMALL, 3 MEDIUM, 1 LARGE
        for (int i = 0; i < 2; i++) slots.add(new ParkingSlot(slotNo++, SlotType.SMALL, 2));
        for (int i = 0; i < 3; i++) slots.add(new ParkingSlot(slotNo++, SlotType.MEDIUM, 2));
        slots.add(new ParkingSlot(slotNo++, SlotType.LARGE, 2));

        // Floor 3: 1 SMALL, 2 MEDIUM, 2 LARGE
        slots.add(new ParkingSlot(slotNo++, SlotType.SMALL, 3));
        for (int i = 0; i < 2; i++) slots.add(new ParkingSlot(slotNo++, SlotType.MEDIUM, 3));
        for (int i = 0; i < 2; i++) slots.add(new ParkingSlot(slotNo++, SlotType.LARGE, 3));

        // 2 entry gates
        List<EntryGate> gates = List.of(
                new EntryGate(1, 1),   // gate 1 on floor 1
                new EntryGate(2, 3)    // gate 2 on floor 3
        );

        ParkingLot lot = new ParkingLot(slots, gates);

        System.out.println("=== Multilevel Parking System Demo ===\n");

        // ---------- INITIAL STATUS ----------
        System.out.println("--- Initial Status ---");
        printStatus(lot);

        // ---------- SCENARIO 1: Park a 2-wheeler from gate 1 ----------
        System.out.println("\n--- Scenario 1: Park a 2-wheeler (gate 1) ---");
        Vehicle bike = new Vehicle("KA-01-1234", VehicleType.TWO_WHEELER);
        LocalDateTime bikeEntry = LocalDateTime.of(2026, 3, 23, 10, 0);
        ParkingTicket bikeTicket = lot.park(bike, bikeEntry, SlotType.SMALL, 1);
        System.out.println(bikeTicket);

        // ---------- SCENARIO 2: Park a car from gate 2 ----------
        System.out.println("\n--- Scenario 2: Park a car (gate 2) ---");
        Vehicle car = new Vehicle("MH-12-5678", VehicleType.CAR);
        LocalDateTime carEntry = LocalDateTime.of(2026, 3, 23, 10, 15);
        ParkingTicket carTicket = lot.park(car, carEntry, SlotType.MEDIUM, 2);
        System.out.println(carTicket);

        // ---------- SCENARIO 3: Park a bus from gate 1 ----------
        System.out.println("\n--- Scenario 3: Park a bus (gate 1) ---");
        Vehicle bus = new Vehicle("TN-09-9999", VehicleType.BUS);
        LocalDateTime busEntry = LocalDateTime.of(2026, 3, 23, 10, 30);
        ParkingTicket busTicket = lot.park(bus, busEntry, SlotType.LARGE, 1);
        System.out.println(busTicket);

        // ---------- STATUS AFTER PARKING ----------
        System.out.println("\n--- Status After 3 Vehicles Parked ---");
        printStatus(lot);

        // ---------- SCENARIO 4: Upsizing — fill remaining SMALL slots, then park another bike ----------
        System.out.println("\n--- Scenario 4: Fill SMALL slots, then park another bike (should upsize) ---");
        // fill remaining 5 small slots
        String[] plates = {"KA-02-0001", "KA-02-0002", "KA-02-0003", "KA-02-0004", "KA-02-0005"};
        for (String plate : plates) {
            lot.park(new Vehicle(plate, VehicleType.TWO_WHEELER),
                    LocalDateTime.of(2026, 3, 23, 11, 0), SlotType.SMALL, 1);
        }
        System.out.println("All SMALL slots filled. Parking one more bike...");
        Vehicle extraBike = new Vehicle("KA-03-7777", VehicleType.TWO_WHEELER);
        ParkingTicket upsizedTicket = lot.park(extraBike,
                LocalDateTime.of(2026, 3, 23, 11, 5), SlotType.SMALL, 1);
        System.out.println("Upsized ticket: " + upsizedTicket);

        // ---------- SCENARIO 5: Nearest gate demo ----------
        System.out.println("\n--- Scenario 5: Nearest gate — park a car from gate 1 vs gate 2 ---");
        Vehicle car2 = new Vehicle("DL-05-1111", VehicleType.CAR);
        ParkingTicket car2Ticket = lot.park(car2,
                LocalDateTime.of(2026, 3, 23, 11, 30), SlotType.MEDIUM, 1);
        Vehicle car3 = new Vehicle("DL-05-2222", VehicleType.CAR);
        ParkingTicket car3Ticket = lot.park(car3,
                LocalDateTime.of(2026, 3, 23, 11, 30), SlotType.MEDIUM, 2);
        System.out.println("Car from gate 1 -> " + (car2Ticket != null
                ? "Slot #" + car2Ticket.getAllocatedSlot().getSlotNumber()
                  + " (floor " + car2Ticket.getAllocatedSlot().getFloor() + ")"
                : "No slot"));
        System.out.println("Car from gate 2 -> " + (car3Ticket != null
                ? "Slot #" + car3Ticket.getAllocatedSlot().getSlotNumber()
                  + " (floor " + car3Ticket.getAllocatedSlot().getFloor() + ")"
                : "No slot"));

        // ---------- SCENARIO 6: Exit + Billing ----------
        System.out.println("\n--- Scenario 6: Exit the first bike (parked 3 hours) ---");
        LocalDateTime bikeExit = LocalDateTime.of(2026, 3, 23, 13, 0);
        Bill bikeBill = lot.exit(bikeTicket, bikeExit);
        System.out.println(bikeBill);

        System.out.println("\n--- Exit the car (parked 2h 15m -> billed 3h) ---");
        LocalDateTime carExit = LocalDateTime.of(2026, 3, 23, 12, 30);
        Bill carBill = lot.exit(carTicket, carExit);
        System.out.println(carBill);

        // ---------- SCENARIO 7: Status after exits ----------
        System.out.println("\n--- Status After Exits ---");
        printStatus(lot);

        // ---------- SCENARIO 8: Edge case — park when all LARGE slots full ----------
        System.out.println("\n--- Scenario 8: Try parking buses until all LARGE slots full ---");
        // fill all remaining large slots
        for (int i = 1; i <= 10; i++) {
            ParkingTicket t = lot.park(new Vehicle("BUS-" + i, VehicleType.BUS),
                    LocalDateTime.of(2026, 3, 23, 12, 0), SlotType.LARGE, 1);
            if (t == null) {
                System.out.println("Failed on bus #" + i + " -> no LARGE slot available.");
                break;
            } else {
                System.out.println("Parked BUS-" + i + " in slot #"
                        + t.getAllocatedSlot().getSlotNumber());
            }
        }
    }

    // helper to print status nicely
    private static void printStatus(ParkingLot lot) {
        Map<SlotType, int[]> status = lot.status();
        System.out.printf("| %-8s | %-5s | %-9s |%n", "Type", "Total", "Available");
        System.out.println("|----------|-------|-----------|");
        for (Map.Entry<SlotType, int[]> e : status.entrySet()) {
            System.out.printf("| %-8s | %-5d | %-9d |%n",
                    e.getKey(), e.getValue()[0], e.getValue()[1]);
        }
    }
}
