package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./")  // Path to the .env file
            .load();
    private static final String HOST = dotenv.get("MYSQL_HOST");
    private static final String DB = dotenv.get("MYSQL_DB");
    private static final String USERNAME = dotenv.get("MYSQL_USER");
    private static final String PASSWORD = dotenv.get("MYSQL_PASSWORD");
    private static final String PORT = dotenv.get("MYSQL_PORT");

    private static final String URL = String.format("jdbc:mysql://%s:%s/%s", HOST, PORT, DB);

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error: Unable to connect to the database.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void testConnection() {
        try (Connection connection = getConnection()) {
            System.out.println("Database connection successful!");
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}