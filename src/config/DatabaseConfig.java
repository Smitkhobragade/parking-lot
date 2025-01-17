package config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConfig {
    private static DatabaseConfig instance; // Singleton instance
    private static Connection connection;

    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./")  // Path to the .env file
            .load();
    private static final String HOST = dotenv.get("MYSQL_HOST");
    private static final String DB = dotenv.get("MYSQL_DB");
    private static final String USERNAME = dotenv.get("MYSQL_USER");
    private static final String PASSWORD = dotenv.get("MYSQL_PASSWORD");
    private static final String PORT = dotenv.get("MYSQL_PORT");

    private static final String URL = String.format("jdbc:mysql://%s:%s/%s", HOST, PORT, DB);

    // prevent instantiation again
    private DatabaseConfig() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error: Unable to connect to the database.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConfig getInstance() {
        if(instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public static void testConnection() {
        try {
            if (getInstance().getConnection() != null) {
                System.out.println("Database connection successful!");
            }
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    public static void createSchemaSetup() {
        try (Connection connection = getInstance().getConnection()) {
            System.out.println("Database connection successful!");
            runSchemaSetup(connection);
            System.out.println("Database connection successful and schema set up!");
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    private static void runSchemaSetup(Connection connection) {
        try {
            InputStream schemaStream = DatabaseConfig.class.getClassLoader().getResourceAsStream("schema/schema.sql");
            if (schemaStream == null) {
                throw new RuntimeException("schema.sql file not found in resources");
            }

            String schema = new String(schemaStream.readAllBytes(), StandardCharsets.UTF_8);

            String[] queries = schema.split(";");

            try (Statement statement = connection.createStatement()) {
                for (String query : queries) {
                    if (!query.trim().isEmpty()) {
                        statement.executeUpdate(query.trim());
                    }
                }
                System.out.println("Schema setup executed successfully.");
            }
        } catch (Exception e) {
            System.err.println("Error executing schema setup: " + e.getMessage());
        }
    }
}