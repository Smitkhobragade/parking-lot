package service;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleService {
    private final Connection connection;
    private final SlotService slotService;

    public VehicleService(SlotService slotService) {
        this.connection = DatabaseConfig.getInstance().getConnection();
        this.slotService = slotService;
    }

    public void addVehicle(String vehicleNumber) {
        // Check if the vehicle already exists with an active state
        String checkQuery = "SELECT id FROM vehicles WHERE vehicle_number = ? AND is_active = true";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, vehicleNumber);

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Vehicle " + vehicleNumber + " is already parked and active. Cannot add it again.");
                    return;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking vehicle existence: " + e.getMessage());
            return;
        }

        // Find Free Slot
        String slotNumber = slotService.getFirstAvailableSlot();

        if (slotNumber == null) {
            System.out.println("No empty slots available for vehicle " + vehicleNumber + ".");
            return;
        }

        String query = "INSERT INTO vehicles (vehicle_number, slot_number, entry_time, is_active) VALUES (?, ?, ?, true)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, vehicleNumber);
            preparedStatement.setString(2, slotNumber);
            preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
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

    public void exitVehicle(String vehicleNumber) {
        String selectQuery = "SELECT id, slot_number, entry_time FROM vehicles WHERE vehicle_number = ? AND is_active = true";
        String insertExitQuery = "INSERT INTO vehicle_exit (vehicle_id, vehicle_number, exit_time, total_hours, total_charge) VALUES (?, ?, ?, ?, ?)";
        String deactivateVehicleQuery = "UPDATE vehicles SET is_active = false WHERE id = ?";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setString(1, vehicleNumber);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    int vehicleId = resultSet.getInt("id");
                    String slotNumber = resultSet.getString("slot_number");
                    java.sql.Timestamp entryTime = resultSet.getTimestamp("entry_time");

                    long entryTimeMillis = entryTime.getTime();
                    long currentTimeMillis = System.currentTimeMillis();
                    long durationMillis = currentTimeMillis - entryTimeMillis;
                    int totalHours = (int) Math.ceil(durationMillis / (1000.0 * 60 * 60));

                    // Calculate total charge
                    double totalCharge = 0.0;
                    if (totalHours <= 3) {
                        totalCharge = totalHours * 100.0; // First 3 hours at ₹100/hour
                    } else {
                        totalCharge = (3 * 100.0) + ((totalHours - 3) * 150.0); // Additional hours at ₹150/hour
                    }

                    try (PreparedStatement insertExitStatement = connection.prepareStatement(insertExitQuery)) {
                        insertExitStatement.setInt(1, vehicleId);
                        insertExitStatement.setString(2, vehicleNumber);
                        insertExitStatement.setTimestamp(3, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                        insertExitStatement.setInt(4, totalHours);
                        insertExitStatement.setDouble(5, totalCharge);

                        int rowsInserted = insertExitStatement.executeUpdate();
                        if (rowsInserted > 0) {
                            try (PreparedStatement deactivateStatement = connection.prepareStatement(deactivateVehicleQuery)) {
                                deactivateStatement.setInt(1, vehicleId);
                                deactivateStatement.executeUpdate();

                                slotService.releaseSlot(slotNumber);

                                System.out.println("Vehicle " + vehicleNumber + " exited successfully!");
                                System.out.println("Total Hours Parked: " + totalHours);
                                System.out.println("Total Charge: ₹" + totalCharge);
                            }
                        }
                    }
                } else {
                    System.out.println("Vehicle " + vehicleNumber + " is not currently parked.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during vehicle exit: " + e.getMessage());
        }
    }


}
