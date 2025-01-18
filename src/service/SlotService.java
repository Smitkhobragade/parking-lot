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
        String checkQuery = "SELECT is_occupied FROM parking_slots WHERE slot_number = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            // Check if the slot is occupied
            checkStatement.setString(1, slotNumber);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    boolean isOccupied = resultSet.getBoolean("is_occupied");
                    if (isOccupied) {
                        System.out.println("Cannot delete slot " + slotNumber + " because it is currently occupied.");
                        return;
                    }
                } else {
                    System.out.println("Slot " + slotNumber + " does not exist.");
                    return;
                }
            }

            String query = "DELETE FROM parking_slots WHERE slot_number = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, slotNumber);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Slot " + slotNumber + " deleted successfully!");
                } else {
                    System.out.println("Slot " + slotNumber + " does not exist.");
                }
            }
        }catch (SQLException e) {
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

    public void occupySlot(String slotNumber) {
        String query = "UPDATE parking_slots SET is_occupied = true WHERE slot_number = ? AND is_occupied = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, slotNumber);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Slot " + slotNumber + " is now occupied.");
            } else {
                System.out.println("Slot " + slotNumber + " is already occupied or does not exist.");
            }
        } catch (SQLException e) {
            System.err.println("Error occupying slot: " + e.getMessage());
        }
    }

    public void releaseSlot(String slotNumber) {
        String query = "UPDATE parking_slots SET is_occupied = false WHERE slot_number = ? AND is_occupied = true";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, slotNumber);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Slot " + slotNumber + " is now free.");
            } else {
                System.out.println("Slot " + slotNumber + " is already free or does not exist.");
            }
        } catch (SQLException e) {
            System.err.println("Error releasing slot: " + e.getMessage());
        }
    }

    public void viewEmptySlots() {
        String query = "SELECT slot_number FROM parking_slots WHERE is_occupied = false ORDER BY slot_number";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("\n=== Empty Parking Slots ===");
            while (resultSet.next()) {
                String slotNumber = resultSet.getString("slot_number");
                System.out.println("Slot " + slotNumber);
            }
        } catch (SQLException e) {
            System.err.println("Error viewing empty slots: " + e.getMessage());
        }
    }

    public void viewOccupiedSlots() {
        String query = "SELECT slot_number FROM parking_slots WHERE is_occupied = true ORDER BY slot_number";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("\n=== Occupied Parking Slots ===");
            while (resultSet.next()) {
                String slotNumber = resultSet.getString("slot_number");
                System.out.println("Slot " + slotNumber);
            }
        } catch (SQLException e) {
            System.err.println("Error viewing occupied slots: " + e.getMessage());
        }
    }

    public String getFirstAvailableSlot() {
        String query = "SELECT slot_number FROM parking_slots WHERE is_occupied = false ORDER BY slot_number LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getString("slot_number");
            } else {
                return null; // No slots available
            }
        } catch (SQLException e) {
            System.err.println("Error finding an available slot: " + e.getMessage());
            return null;
        }
    }


}
