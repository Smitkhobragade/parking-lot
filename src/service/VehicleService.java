package service;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VehicleService {
    private final Connection connection;
    private final SlotService slotService;

    public VehicleService(SlotService slotService) {
        this.connection = DatabaseConfig.getInstance().getConnection();
        this.slotService = slotService;
    }

    public void addVehicle(String vehicleNumber) {
        // Find Free Slot
        String slotNumber = slotService.getFirstAvailableSlot();

        if (slotNumber == null) {
            System.out.println("No empty slots available for vehicle " + vehicleNumber + ".");
            return;
        }

        String query = "INSERT INTO vehicles (vehicle_number, slot_number, is_active) VALUES (?, ?, true)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, vehicleNumber);
            preparedStatement.setString(2, slotNumber);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                slotService.occupySlot(slotNumber);
                System.out.println("Vehicle " + vehicleNumber + " added successfully to slot " + slotNumber + "!");
            } else {
                System.out.println("Failed to add vehicle " + vehicleNumber + ".");
            }
        } catch (SQLException e) {
            System.err.println("Error adding vehicle: " + e.getMessage());
        }
    }

}
