package config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final String PROPERTIES_FILE = "/config/db.properties";
    private static final String JDBC_URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        try (InputStream input = ConnectionFactory.class.getResourceAsStream(PROPERTIES_FILE)) {

            if (input == null) {
                throw new IOException("Unable to find " + PROPERTIES_FILE);
            }

            Properties prop = new Properties();
            prop.load(input);

            String host = prop.getProperty("db.host");
            String port = prop.getProperty("db.port");
            String dbName = prop.getProperty("db.name");
            String sslMode = prop.getProperty("db.sslmode");

            USER = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");

            JDBC_URL = String.format(
                    "jdbc:postgresql://%s:%s/%s?sslmode=%s",
                    host, port, dbName, sslMode
            );

//            JDBC_URL = String.format(
//                    "jdbc:postgresql://%s:%s/%s?sslmode=%s&prepareThreshold=0",
//                    host, port, dbName, sslMode
//            );

        } catch (IOException e) {

            e.printStackTrace();
            throw new RuntimeException("Failed to load DB configuration from " + PROPERTIES_FILE, e);

        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
}