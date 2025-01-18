package service;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardService {
    private final Connection connection;

    public DashboardService() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    public void getTotalUsersServedInDay(String date) {
        String query = "SELECT COUNT(*) AS total_users FROM vehicle_exit WHERE DATE(exit_time) = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, date);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int totalUsers = resultSet.getInt("total_users");
                    System.out.println("Total users served on " + date + ": " + totalUsers);
                } else {
                    System.out.println("No data available for the given date.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving total users served: " + e.getMessage());
        }
    }

    public void getTotalRevenueInDateRange(String startDate, String endDate) {
        String query = "SELECT SUM(total_charge) AS total_revenue FROM vehicle_exit WHERE DATE(exit_time) BETWEEN ? AND ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double totalRevenue = resultSet.getDouble("total_revenue");
                    System.out.println("Total revenue from " + startDate + " to " + endDate + ": ₹" + totalRevenue);
                } else {
                    System.out.println("No data available for the given date range.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving total revenue: " + e.getMessage());
        }
    }

}
