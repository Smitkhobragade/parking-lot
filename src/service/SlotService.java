package service;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SlotService {
    private final Connection connection;

    public SlotService() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    public void addSlot(String slotNumber) {
        String query = "INSERT INTO parking_slots (slot_number, is_occupied) VALUES (?, false)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, slotNumber);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Slot " + slotNumber + " added successfully!");
            } else {
                System.out.println("Failed to add slot " + slotNumber + ".");
            }
        } catch (SQLException e) {
            System.err.println("Error adding slot: " + e.getMessage());
        }
    }

    public void deleteSlot(String slotNumber) {
        String query = "DELETE FROM parking_slots WHERE slot_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, slotNumber);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Slot " + slotNumber + " deleted successfully!");
            } else {
                System.out.println("Slot " + slotNumber + " does not exist.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting slot: " + e.getMessage());
        }
    }

    public void viewAllSlots() {
        String query = "SELECT slot_number, is_occupied FROM parking_slots ORDER BY slot_number";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("\n=== All Parking Slots ===");
            while (resultSet.next()) {
                String slotNumber = resultSet.getString("slot_number");
                boolean isOccupied = resultSet.getBoolean("is_occupied");
                System.out.println("Slot " + slotNumber + " - " + (isOccupied ? "Occupied" : "Available"));
            }
        } catch (SQLException e) {
            System.err.println("Error viewing slots: " + e.getMessage());
        }
    }


}
