package service;

import config.DatabaseConfig;

import java.util.Scanner;

public class ParkingService {
    private final SlotService slotService;
    private final VehicleService vehicleService;

    public ParkingService() {
        this.slotService = new SlotService();
        this.vehicleService = new VehicleService(this.slotService);
    }

    public void startCLI() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Parking Lot System ===");
            System.out.println("1. Test Database Connection");
            System.out.println("2. Create Schema Setup");
            System.out.println("3. Add Slot");
            System.out.println("4. Delete Slot");
            System.out.println("5. View All Slots");
            System.out.println("6. Occupy SLots");
            System.out.println("7. Release Slots");
            System.out.println("8. View Empty Slots");
            System.out.println("9. View Occupied Slots");
            System.out.println("10. View First Free Slot");
            System.out.println("11. Add Vehicle");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // invalid input
            }
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> DatabaseConfig.testConnection();
                case 2 -> DatabaseConfig.createSchemaSetup();
                case 3 -> {
                    System.out.print("Enter slot number to add: ");
                    String slotNumber = scanner.next();
                    slotService.addSlot(slotNumber);
                }
                case 4 -> {
                    System.out.print("Enter slot number to delete: ");
                    String slotNumber = scanner.next();
                    System.out.print("Are you sure you want to delete slot " + slotNumber + "? (yes/no): ");
                    String confirmation = scanner.next();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        slotService.deleteSlot(slotNumber);
                    } else {
                        System.out.println("Deletion canceled.");
                    }
                }
                case 5 -> slotService.viewAllSlots();
                case 6 -> {
                    System.out.print("Enter slot number to occupy (e.g., A1, B2): ");
                    String slotNumber = scanner.next();
                    slotService.occupySlot(slotNumber);
                }
                case 7 -> {
                    System.out.print("Enter slot number to release (e.g., A1, B2): ");
                    String slotNumber = scanner.next();
                    slotService.releaseSlot(slotNumber);
                }
                case 8 -> slotService.viewEmptySlots();
                case 9 -> slotService.viewOccupiedSlots();
                case 10 -> System.out.println(slotService.getFirstAvailableSlot());
                case 11 -> {
                    System.out.print("Enter vehicle number to add: ");
                    String vehicleNumber = scanner.next();
                    vehicleService.addVehicle(vehicleNumber);
                }
                case 0 -> System.out.println("Exiting... Thank you!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
