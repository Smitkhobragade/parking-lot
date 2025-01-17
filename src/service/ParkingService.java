package service;

import config.DatabaseConfig;

import java.util.Scanner;

public class ParkingService {
    public void startCLI() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Parking Lot System ===");
            System.out.println("1. Test Database Connection");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> DatabaseConfig.testConnection();
                case 0 -> System.out.println("Exiting... Thank you!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
