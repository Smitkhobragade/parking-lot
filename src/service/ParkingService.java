package service;

import config.DatabaseConfig;

import java.util.Scanner;

public class ParkingService {
    private final SlotService slotService;
    private final VehicleService vehicleService;
    private final DashboardService dashboardService;

    public ParkingService() {
        this.slotService = new SlotService();
        this.vehicleService = new VehicleService(this.slotService);
        this.dashboardService = new DashboardService();
    }

    public void startCLI() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Parking Lot System ===");
            System.out.println("1. Test Database Connection");
            System.out.println("2. Slots Menu");
            System.out.println("3. Park Vehicle");
            System.out.println("4. Remove Vehicle");
            System.out.println("5. View Total Users Served");
            System.out.println("6. View Total Revenue");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Invalid input
            }
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> DatabaseConfig.testConnection();
                case 2 -> slotsMenu(scanner);
                case 3 -> {
                    System.out.print("Enter vehicle number to park: ");
                    String vehicleNumber = scanner.next();
                    vehicleService.addVehicle(vehicleNumber);
                }
                case 4 -> {
                    System.out.print("Enter vehicle number to remove: ");
                    String vehicleNumber = scanner.next();
                    vehicleService.exitVehicle(vehicleNumber);
                }
                case 5 -> {
                    System.out.print("Enter the date (YYYY-MM-DD): ");
                    String date = scanner.next();
                    dashboardService.getTotalUsersServedInDay(date);
                }
                case 6 -> {
                    System.out.print("Enter the start date (YYYY-MM-DD): ");
                    String startDate = scanner.next();
                    System.out.print("Enter the end date (YYYY-MM-DD): ");
                    String endDate = scanner.next();
                    dashboardService.getTotalRevenueInDateRange(startDate, endDate);
                }
                case 0 -> System.out.println("Exiting... Thank you!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }

    private void slotsMenu(Scanner scanner) {
        int slotChoice;
        do {
            System.out.println("\n=== Slots Menu ===");
            System.out.println("1. Add Slot");
            System.out.println("2. Delete Slot");
            System.out.println("3. View All Slots");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Invalid input
            }
            slotChoice = scanner.nextInt();

            switch (slotChoice) {
                case 1 -> {
                    System.out.print("Enter slot number to add: ");
                    String slotNumber = scanner.next();
                    slotService.addSlot(slotNumber);
                }
                case 2 -> {
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
                case 3 -> slotService.viewAllSlots();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (slotChoice != 0);
    }
}
